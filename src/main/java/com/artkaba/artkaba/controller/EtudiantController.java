package com.artkaba.artkaba.controller;

import com.artkaba.artkaba.entity.Etudiant;
import com.artkaba.artkaba.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/etudiants")
@CrossOrigin(origins = "https://artkaba.netlify.app") // Ajuste selon le port de ton appli React
public class EtudiantController {

    @Autowired
    private EtudiantRepository etudiantRepository;

    // L'ENDPOINT QUI MANQUE :
    @PutMapping("/{id}/activer")
    public ResponseEntity<?> activerCompteEtudiant(@PathVariable Long id) {
        // 1. Chercher l'étudiant par son ID
        Optional<Etudiant> etudiantOpt = etudiantRepository.findById(id);
        
        if (etudiantOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("Étudiant introuvable avec l'ID : " + id);
        }

        // 2. Récupérer l'étudiant et passer son statut à true
        Etudiant etudiant = etudiantOpt.get();
        etudiant.setActivated(true); // Vérifie bien que ton champ s'appelle isActivated ou juste activated dans ton entité

        // 3. Sauvegarder la modification dans MySQL
        etudiantRepository.save(etudiant);

        // 4. Retourner une réponse positive
        return ResponseEntity.ok(etudiant);
    }
}