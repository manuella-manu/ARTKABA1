package com.artkaba.artkaba.controller;

import com.artkaba.artkaba.entity.Etudiant;
import com.artkaba.artkaba.entity.Message;
import com.artkaba.artkaba.repository.EtudiantRepository;
import com.artkaba.artkaba.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "http://localhost:5173")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private EtudiantRepository etudiantRepository;

    /**
     * POST : Envoyer un message à un artiste depuis la galerie publique
     */
    @PostMapping("/send")
    public ResponseEntity<?> envoyerMessage(@RequestBody Map<String, Object> payload) {
        try {
            Long etudiantId = Long.valueOf(payload.get("etudiantId").toString());
            String nom = (String) payload.get("expediteurNom");
            String email = (String) payload.get("expediteurEmail");
            String contenu = (String) payload.get("contenu");

            // Vérifier si l'artiste ciblé existe
            Etudiant etudiant = etudiantRepository.findById(etudiantId)
                    .orElseThrow(() -> new RuntimeException("Artiste introuvable"));

            Message message = new Message();
            message.setExpediteurNom(nom);
            message.setExpediteurEmail(email);
            message.setContenu(contenu);
            message.setEtudiant(etudiant);

            Message nouveauMessage = messageRepository.save(message);
            return ResponseEntity.ok(nouveauMessage);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur lors de l'envoi du message : " + e.getMessage());
        }
    }

    /**
     * GET : Récupérer la boîte de réception d'un étudiant spécifique
     */
    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<?> getMessagesPourEtudiant(@PathVariable Long etudiantId) {
        if (!etudiantRepository.existsById(etudiantId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Étudiant introuvable");
        }

        List<Message> messages = messageRepository.findByEtudiantIdOrderByDateEnvoiDesc(etudiantId);
        return ResponseEntity.ok(messages);
    }
}