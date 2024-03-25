package com.example.hirehub;

public class Post {
    private long id; // Identifiant unique du poste
    private String title; // Titre du poste
    private String category; // Catégorie du poste
    private String sector; // Secteur du poste
    private String contractType; // Type de contrat du poste
    private String description; // Description du poste
    private String city; // Ville du poste

    // Constructeur
    public Post(String title, String category, String sector, String contractType, String description, String city) {
        this.title = title;
        this.category = category;
        this.sector = sector;
        this.contractType = contractType;
        this.description = description;
        this.city = city;
    }

    public Post(String email, String title, String description) {
    }

    // Getters et setters pour tous les attributs (omis pour la brièveté)

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    
}



