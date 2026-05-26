package com.artkaba.artkaba.controller;

import com.artkaba.artkaba.entity.Etudiant;
import com.artkaba.artkaba.entity.Oeuvre;
import com.artkaba.artkaba.repository.EtudiantRepository;
import com.artkaba.artkaba.repository.OeuvreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/oeuvres")
@CrossOrigin(origins = "http://localhost:5173")
public class OeuvreController {

    @Autowired
    private OeuvreRepository oeuvreRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    // Emplacement racine pour le stockage des images physiques
    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/uploads/";

    /**
     * POST : Ajouter une nouvelle œuvre avec upload d'image binaire (FormData)
     */
    @PostMapping("/add")
    public ResponseEntity<?> ajouterOeuvre(
            @RequestParam("titre") String titre,
            @RequestParam("categorie") String categorie,
            @RequestParam("dimensions") String dimensions,
            @RequestParam("prix") String prix,
            @RequestParam("etudiantId") Long etudiantId,
            @RequestParam("image") MultipartFile file) {

        try {
            // 1. Vérifier si l'étudiant existe en BDD
            Etudiant etudiant = etudiantRepository.findById(etudiantId)
                    .orElseThrow(() -> new RuntimeException("Étudiant introuvable"));

            // 2. Vérifier si le fichier image est bien présent
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Veuillez sélectionner une image.");
            }

            // Créer le dossier "uploads" à la racine s'il n'existe pas encore
            Path uploadPath = Paths.get(UPLOAD_DIRECTORY);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Générer un nom unique pour éviter les conflits de doublons
            String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(uniqueFileName);
            
            // Écriture physique du fichier sur le disque dur
            Files.copy(file.getInputStream(), filePath);

            // 3. Hydrater l'objet Oeuvre et persister dans MySQL
            Oeuvre oeuvre = new Oeuvre();
            oeuvre.setTitre(titre);
            oeuvre.setCategorie(categorie);
            oeuvre.setDimensions(dimensions);
            oeuvre.setPrix(prix);
            oeuvre.setEtudiant(etudiant);
            
            // URL relative que React utilisera pour appeler le GetMapping ci-dessous
            oeuvre.setImageUrl("/api/oeuvres/images/" + uniqueFileName); 

            Oeuvre nouvelleOeuvre = oeuvreRepository.save(oeuvre);
            return ResponseEntity.ok(nouvelleOeuvre);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'enregistrement de l'image : " + e.getMessage());
        }
    }

    /**
     * GET : Récupérer toutes les œuvres pour alimenter la galerie publique (App.jsx)
     */
    @GetMapping("/all")
    public ResponseEntity<List<Oeuvre>> getAllOeuvres() {
        List<Oeuvre> oeuvres = oeuvreRepository.findAll();
        return ResponseEntity.ok(oeuvres);
    }

    /**
     * GET : Récupérer toutes les œuvres d'un étudiant spécifique (StudentDashboard.jsx)
     */
   @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<?> getOeuvresByEtudiant(@PathVariable Long etudiantId) {
        // On vérifie d'abord si l'étudiant existe
        if (!etudiantRepository.existsById(etudiantId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Étudiant introuvable");
        }
        
        // On demande directement au repository de nous donner ses œuvres
        List<Oeuvre> oeuvres = oeuvreRepository.findByEtudiantId(etudiantId);
        return ResponseEntity.ok(oeuvres);
    }

    /**
     * GET : Servir les fichiers images physiques au navigateur web via la balise <img>
     */
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<org.springframework.core.io.Resource> getImage(@PathVariable String filename) {
        try {
            Path file = Paths.get(UPLOAD_DIRECTORY).resolve(filename);
            org.springframework.core.io.Resource resource = new org.springframework.core.io.UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(org.springframework.http.HttpHeaders.CONTENT_TYPE, Files.probeContentType(file))
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * PUT : Modifier une œuvre existante
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<?> modifierOeuvre(@PathVariable Long id, @RequestBody Oeuvre details) {
        return oeuvreRepository.findById(id)
                .map(oeuvre -> {
                    oeuvre.setTitre(details.getTitre());
                    oeuvre.setCategorie(details.getCategorie());
                    oeuvre.setDimensions(details.getDimensions());
                    oeuvre.setPrix(details.getPrix());
                    // On ne change pas l'image ici pour rester simple (logiciel de gestion de texte)
                    return ResponseEntity.ok(oeuvreRepository.save(oeuvre));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE : Supprimer une œuvre
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> supprimerOeuvre(@PathVariable Long id) {
        if (oeuvreRepository.existsById(id)) {
            oeuvreRepository.deleteById(id);
            return ResponseEntity.ok().body("{\"message\": \"Oeuvre supprimée\"}");
        }
        return ResponseEntity.notFound().build();
    }
}