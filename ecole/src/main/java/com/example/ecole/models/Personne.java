package com.example.ecole.models;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "personne", uniqueConstraints = @UniqueConstraint(columnNames = {"prenom", "nom"}))
public class Personne {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotEmpty
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$", message = "Le prénom doit contenir uniquement des lettres et des espaces.")
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @NotEmpty
    @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ\\s]+$", message = "Le nom doit contenir uniquement des lettres et des espaces.")
    @Column(name = "nom", nullable = false)
    private String nom;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "naissance", nullable = false)
    private LocalDate naissance;

    @NotEmpty
    @Pattern(regexp = "^[\\p{L}]+$", message = "Nationalité invalide")
    @Column(name = "nationalite", nullable = false)
    private String nationalite;

    @NotEmpty
    @Pattern(regexp = "^\\s*[A-Za-z][A-Za-z\\d\\s./]*\\s*$", message = "Entrer une adresse valide")
    @Column(name = "adresse", nullable = false)
    private String adresse;

    @NotEmpty
    @Pattern(regexp = "^(homme|femme)$", message = "entrer homme ou femme'")
    @Column(name = "sexe", nullable = false)
    private String sexe;

    @NotEmpty
    @Pattern(regexp = "^(etudiant|professeur)$", message = "entrer professeur ou etudiant'")
    private String statut;

    @JsonIgnore
    @OneToMany(mappedBy = "professeur_id")
    private List<Matiere> matieres;

    @JsonBackReference
    @OneToMany(mappedBy = "personne_id")
    private List<Inscription> inscriptions;

    public Personne(String prenom, String nom, LocalDate naissance, String nationalite, String adresse, String sexe, String statut, List<Matiere> matieres, List<Inscription> inscriptions) {
        this.prenom = prenom;
        this.nom = nom;
        this.naissance = naissance;
        this.nationalite = nationalite;
        this.adresse = adresse;
        this.matieres = matieres;
        this.sexe = sexe;
        this.inscriptions = inscriptions;
        this.statut = statut;
    }

    public Personne() {
    }

    public Personne(String prenom, String nom, LocalDate naissance, String nationalite, String adresse, String sexe, String statut) {
        this.prenom = prenom;
        this.nom = nom;
        this.naissance = naissance;
        this.nationalite = nationalite;
        this.adresse = adresse;
        this.sexe = sexe;
        this.statut = statut;
    }

    public Personne(String uuid) {
        this.id = UUID.fromString(uuid);
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
    public String getNom() {
        return nom;
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
    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getSexe() {
        return sexe;
    }
    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
    public void setMatieres(List<Matiere> matieres) {
        this.matieres = matieres;
    }
    public void setInscriptions(List<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
    }
}
