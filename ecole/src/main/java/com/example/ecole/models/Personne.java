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
    @OneToMany(mappedBy = "professeur")
    private List<Matiere> matieres;

    @JsonBackReference
    @OneToMany(mappedBy = "personne_id")
    private List<Inscription> inscriptions;

    @JsonIgnore
    @OneToMany(mappedBy = "personne_idpresence", cascade = CascadeType.ALL)
    private List<Absence> absences;

    @JsonIgnore
    @OneToMany(mappedBy = "personne_vacance", cascade = CascadeType.ALL)
    private  List<Vacance> vacances;
    @JsonIgnore
    @OneToMany(mappedBy = "personne_note", cascade = CascadeType.ALL)
    private  List<Note> notes;

    private  float moyenne;
    private String mail;
    @Column(name = "auth0_id")
    private String auth0Id;

    public Personne(String prenom, String nom, LocalDate naissance, String nationalite, String adresse, String sexe, String statut, List<Matiere> matieres, List<Inscription> inscriptions, List<Absence> absences, List<Vacance> vacances, List<Note> notes,float moyenne,String mail, String auth0Id ) {
        this.prenom = prenom;
        this.nom = nom;
        this.naissance = naissance;
        this.nationalite = nationalite;
        this.adresse = adresse;
        this.matieres = matieres;
        this.sexe = sexe;
        this.inscriptions = inscriptions;
        this.statut = statut;
        this.absences = absences;
        this.vacances = vacances;
        this.notes = notes;
        this.moyenne = moyenne;
        this.mail = mail;
        this.auth0Id = auth0Id;
    }

    public Personne() {
    }

    public Personne(String prenom, String nom, LocalDate naissance, String nationalite, String adresse, String sexe, String statut,float moyenne, String mail, String auth0Id) {
        this.prenom = prenom;
        this.nom = nom;
        this.naissance = naissance;
        this.nationalite = nationalite;
        this.adresse = adresse;
        this.sexe = sexe;
        this.statut = statut;
        this.moyenne = moyenne;
        this.mail = mail;
        this.auth0Id = auth0Id;
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

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setMatieres(List<Matiere> matieres) {
        this.matieres = matieres;
    }
    public void setInscriptions(List<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
    }

    public void setAbsences(List<Absence> absences) {
        this.absences = absences;
    }

    public void setVacances(List<Vacance> vacances) {
        this.vacances = vacances;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }

    public List<Note> getNotes() {
        return notes;
    }

    public float getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(float moyenne) {
        this.moyenne = moyenne;
    }

    public String getAuth0Id() {
        return auth0Id;
    }

    public void setAuth0Id(String auth0Id) {
        this.auth0Id = auth0Id;
    }

    public void calculerMoyenne() {

        int cpt = 0;

        if (this.notes == null || this.notes.isEmpty()) {
                this.moyenne = 0;
                return;
            }
            int somme = 0;
            for (Note note : this.notes) {
                somme += note.getResultat();
                cpt++;
            }

            this.moyenne = (float) somme / cpt;
    }
}
