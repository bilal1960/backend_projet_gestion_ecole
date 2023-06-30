package com.example.ecole.models;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import  java.util.UUID;
@Entity
@Table(name="personne", uniqueConstraints = @UniqueConstraint(columnNames = {"prenom", "nom"}))
public class Personne {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    @Column(name = "prenom", nullable = false)
    private  String prenom;
    @Column(name = "nom", nullable = false)

    private  String nom;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "naissance", nullable = false)
    private LocalDate naissance;
    @Column(name = "nationalite", nullable = false)
    private  String nationalite;
    @Column(name = "adresse", nullable = false)
    private  String adresse;
    @Column(name = "sexe", nullable = false)
    private  String sexe;
    @ManyToMany
    @JoinTable(name = "personne_matiere", joinColumns = @JoinColumn(name = "personne_id"),
            inverseJoinColumns = @JoinColumn(name = "matiere_id"))
    private List<Matiere> matieres;

    @OneToMany(mappedBy = "personnes")
    private List<Inscription> inscriptions;


    public Personne(String prenom, String nom, LocalDate naissance, String nationalite, String adresse, String sexe, List<Matiere> matieres) {
        this.prenom = prenom;
        this.nom = nom;
        this.naissance = naissance;
        this.nationalite = nationalite;
        this.adresse = adresse;
        this.matieres = matieres;
        this.sexe = sexe;
        this.inscriptions = inscriptions;
    }
    public   Personne(){

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
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

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public List<Matiere> getMatieres(){return  matieres;}



    public List<Inscription> getInscriptions() {
        return inscriptions;
    }

    public void setInscriptions(List<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
   }


}
