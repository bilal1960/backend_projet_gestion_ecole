package com.example.ecole.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "matiere")
public class Matiere {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotEmpty(message = "Le nom ne peut pas être vide")
    @Column(name = "nom", nullable = false)
    private String nom;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate debut;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fin;

    private String revision;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime debutime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime fintime;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "professeur_id")
    private Personne professeur;

    @NotEmpty
    @Pattern(regexp = "^\\s*[A-Za-z][A-Za-z\\d\\s./]*\\s*$", message = "le local doit avoir une lettre au début")
    @Column(name = "local")
    private String local;

    @NotEmpty
    @Column(name = "jour")
    private String jour;

    @Pattern(regexp = "^[1-6] secondaire$", message = "La valeur doit être entre '1 secondaire' et '6 secondaire'")
    @Column(name = "secondaire")
    private String secondaire;
    @Column(name = "professeurauth0")
    private  String professeurAuth0Id;

    public Matiere() {
    }

    public Matiere(String nom, LocalDate debut, LocalDate fin, Personne professeur, LocalTime debutime, LocalTime fintime, String local, String jour, String secondaire, String professeurAuth0Id) {
        this.nom = nom;
        this.debut = debut;
        this.fin = fin;
        this.professeur = professeur;
        this.debutime = debutime;
        this.fintime = fintime;
        this.local = local;
        this.jour = jour;
        this.secondaire = secondaire;
        this.professeurAuth0Id = professeurAuth0Id;
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

    public LocalDate getDebut() {
        return debut;
    }

    public void setDebut(LocalDate debut) {
        this.debut = debut;
    }

    public LocalDate getFin() {
        return fin;
    }

    public void setFin(LocalDate fin) {
        this.fin = fin;
    }

    public LocalTime getDebutime() {
        return debutime;
    }

    public void setDebutime(LocalTime debutime) {
        this.debutime = debutime;
    }

    public LocalTime getFintime() {
        return fintime;
    }

    public void setFintime(LocalTime fintime) {
        this.fintime = fintime;
    }

    public Personne getPersonne() {
        return professeur;
    }

    public void setPersonne(Personne professeur) {
        this.professeur = professeur;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getJour() {
        return jour;
    }

    public void setJour(String jour) {
        this.jour = jour;
    }

    public String getSecondaire() {
        return secondaire;
    }

    public void setSecondaire(String secondaire) {
        this.secondaire = secondaire;
    }

    public String getProfesseurAuth0Id() {
        return professeurAuth0Id;
    }

    public void setProfesseurAuth0Id(String professeurAuth0Id) {
        this.professeurAuth0Id = professeurAuth0Id;
    }
}
