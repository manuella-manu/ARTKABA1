package com.artkaba.artkaba.controller;

import com.artkaba.artkaba.entity.Etudiant;
import com.artkaba.artkaba.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "https://artkaba.netlify.app")// Aligné sur le port par défaut de Vite/React
public class AuthController {

    @Autowired
    private EtudiantRepository etudiantRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerEtudiant(@RequestBody Map<String, String> requestData) {
        try {
            // 1. Récupération et validation des données du formulaire React
            String nom = requestData.get("nom");
            String email = requestData.get("email");
            String carteEtudiantUrl = requestData.get("carteEtudiantUrl");

            if (nom == null || nom.trim().isEmpty() || email == null || email.trim().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Le nom et l'adresse email sont obligatoires.");
            }

            // 2. Vérification de l'unicité de l'email dans MySQL
            if (etudiantRepository.existsByEmail(email)) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Cette adresse email est déjà enregistrée.");
            }

            // 3. Génération automatique et propre du matricule ABA
            Random random = new Random();
            int numAleatoire = 100 + random.nextInt(900); // Génère un nombre entre 100 et 999
            String matriculeAutomatique = "ABA-2026-" + numAleatoire;

            // 4. Création et hydratation de l'entité Etudiant
            Etudiant nouvelEtudiant = new Etudiant();
            nouvelEtudiant.setNom(nom);
            nouvelEtudiant.setEmail(email);
            nouvelEtudiant.setMatricule(matriculeAutomatique);
            nouvelEtudiant.setCarteEtudiantUrl(carteEtudiantUrl != null ? carteEtudiantUrl : "default_carte.jpeg");
            
            // Initialisation forcée à false (frais de 2$ en attente)
            nouvelEtudiant.setActivated(false); 

            // 5. Sauvegarde en Base de données
            Etudiant etudiantSauvegarde = etudiantRepository.save(nouvelEtudiant);

            // 6. Retour des données réelles créées vers le frontend React
            return ResponseEntity.status(HttpStatus.CREATED).body(etudiantSauvegarde);

        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur interne du serveur lors de l'inscription : " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginEtudiant(@RequestBody Map<String, String> loginData) {
        String email = loginData.get("email");
        
        // 1. Chercher l'étudiant dans la base de données par son email
        Optional<Etudiant> etudiantOpt = etudiantRepository.findByEmail(email.trim().toLowerCase());
        
        // 2. Si l'étudiant n'existe pas, renvoyer une erreur 404 propre
        if (etudiantOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("Aucun compte artiste n'est enregistré avec cette adresse email.");
        }
        
        // 3. Si l'étudiant existe, on le renvoie au complet (avec son ID, son matricule et son statut)
        return ResponseEntity.ok(etudiantOpt.get());
    }
}