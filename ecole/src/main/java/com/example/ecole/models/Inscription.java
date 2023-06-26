package com.example.ecole.models;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;
//@Entity
//@Table(name="inscription")
public class Inscription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    private  String nom;
    private  String prenom;
    private LocalDate naissance;
    private  String   nationalite;
    private  String   sexe;
    private  String  adresse;
    private  String commune;
    private  float minerval;
    @ManyToOne
    @JoinColumn(name = "personne_id")
    private  Personne personnes;

    protected  Inscription(){

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getNaissance() {
        return naissance;
    }

    public void setNaissance(LocalDate naissance) {
        this.naissance = naissance;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public float getMinerval() {
        return minerval;
    }

    public void setMinerval(float minerval) {
        this.minerval = minerval;
    }

    public Personne getPersonne() {
        return personnes;
    }

    public void setPersonne(Personne personnes) {
        this.personnes = personnes;
    }
}
