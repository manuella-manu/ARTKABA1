package com.artkaba.artkaba.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "etudiants")
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    
    @Column(unique = true, nullable = false)
    private String email;

    private String matricule;

    @Column(name = "carte_etudiant_url")
    private String carteEtudiantUrl;
    
    @Column(name = "is_activated", nullable = false)
    private boolean isActivated;

    // Constructeur Vide (Obligatoire pour JPA/Hibernate)
    public Etudiant() {
    }

    // Constructeur Complet
    public Etudiant(Long id, String nom, String email, String matricule, String carteEtudiantUrl, boolean isActivated) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.matricule = matricule;
        this.carteEtudiantUrl = carteEtudiantUrl;
        this.isActivated = isActivated;
    }

    // --- GETTERS & SETTERS CLASSIQUES ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getCarteEtudiantUrl() {
        return carteEtudiantUrl;
    }

    public void setCarteEtudiantUrl(String carteEtudiantUrl) {
        this.carteEtudiantUrl = carteEtudiantUrl;
    }

    public boolean isActivated() {
        return isActivated;
    }

    // Cette double méthode garantit la compatibilité avec AuthController et EtudiantController
    public void setActivated(boolean activated) {
        this.isActivated = activated;
    }
}