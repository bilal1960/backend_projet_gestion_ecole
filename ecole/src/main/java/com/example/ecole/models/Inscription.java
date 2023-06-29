package com.example.ecole.models;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name="inscription")
public class Inscription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
@Column(name = "nom", nullable = false)
    private  String nom;
@Column(name = "prenom",nullable = false)
    private  String prenom;
@Column(name = "naissance", nullable = false)
@JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate naissance;
@Column(name = "nationalite", nullable = false)
    private  String   nationalite;
@Column(name = "sexe", nullable = false)
    private  String   sexe;
@Column(name = "adresse", nullable = false)
    private  String  adresse;
@Column(name = "commune", nullable = false)
    private  String commune;
@Column(name = "minerval", nullable = false)
    private  float minerval;
    @ManyToOne
    @JoinColumn(name = "personne_id")
    private  Personne personnes;
    @OneToMany
    @JoinColumn(name= "inscriptions_id")
    private  List<Matiere> matieres;

    protected  Inscription(){

    }

    public Inscription(UUID id, String nom, String prenom, LocalDate naissance, String nationalite,
                       String sexe, String adresse, String commune, float minerval, Personne personnes,
                       List<Matiere> matieres) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.naissance = naissance;
        this.nationalite = nationalite;
        this.sexe = sexe;
        this.adresse = adresse;
        this.commune = commune;
        this.minerval = minerval;
        this.personnes = personnes;
        this.matieres = matieres;
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
