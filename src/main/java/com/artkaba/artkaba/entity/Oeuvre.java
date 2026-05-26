package com.artkaba.artkaba.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "oeuvres")
public class Oeuvre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(name = "image_url")
    private String imageUrl; // Stockera le nom du fichier (ex: "tableau-1716634.jpg")

    @Column(nullable = false)
    private String categorie; // Peinture, Sculpture, Dessin, Design

    private String dimensions; // Ex: "120 x 90 cm"
    private String prix;       // Ex: "150 $"
    private String bgStyle;    // Pour stocker le dégradé Tailwind (ex: "from-zinc-900 to-stone-800")

    // LA SOUDURE : Relation ManyToOne avec l'Étudiant
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
    private Etudiant etudiant;

    // Constructeurs
    public Oeuvre() {}

    public Oeuvre(String titre, String categorie, String dimensions, String prix, String bgStyle, Etudiant etudiant) {
        this.titre = titre;
        this.categorie = categorie;
        this.dimensions = dimensions;
        this.prix = prix;
        this.bgStyle = bgStyle;
        this.etudiant = etudiant;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public String getDimensions() { return dimensions; }
    public void setDimensions(String dimensions) { this.dimensions = dimensions; }

    public String getPrix() { return prix; }
    public void setPrix(String prix) { this.prix = prix; }

    public String getBgStyle() { return bgStyle; }
    public void setBgStyle(String bgStyle) { this.bgStyle = bgStyle; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Etudiant getEtudiant() { return etudiant; }
    public void setEtudiant(Etudiant etudiant) { this.etudiant = etudiant; }
}
                                                                    

