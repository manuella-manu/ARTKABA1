package com.artkaba.artkaba.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String expediteurNom;

    @Column(nullable = false)
    private String expediteurEmail;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenu;

    private LocalDateTime dateEnvoi;

    // La relation clé : Plusieurs messages peuvent être envoyés à un seul étudiant
    @ManyToOne
    @JoinColumn(name = "etudiant_id", nullable = false)
    private Etudiant etudiant;

    // Constructeur par défaut (Obligatoire pour JPA)
    public Message() {
        this.dateEnvoi = LocalDateTime.now(); // Génère la date automatiquement
    }

    // Getters et Setters (ou laisse Lombok gérer si tu as @Data)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getExpediteurNom() { return expediteurNom; }
    public void setExpediteurNom(String expediteurNom) { this.expediteurNom = expediteurNom; }

    public String getExpediteurEmail() { return expediteurEmail; }
    public void setExpediteurEmail(String expediteurEmail) { this.expediteurEmail = expediteurEmail; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public LocalDateTime getDateEnvoi() { return dateEnvoi; }
    public void setDateEnvoi(LocalDateTime dateEnvoi) { this.dateEnvoi = dateEnvoi; }

    public Etudiant getEtudiant() { return etudiant; }
    public void setEtudiant(Etudiant etudiant) { this.etudiant = etudiant; }
}