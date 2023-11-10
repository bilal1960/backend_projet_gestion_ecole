package com.example.ecole.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "vacance")
public class Vacance {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "datedebut", nullable = false)
    private LocalDate datedebut;
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "datefin", nullable = false)
    private LocalDate datefin;

    @Column(name = "type")
    private String type;
    @Column(name = "commentaire")
    private String commentaire;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "personne_vacance")
    private Personne personne_vacance;

    public Vacance() {
    }

    public Vacance(LocalDate datedebut, LocalDate dateFin,  String type, String commentaire) {
        this.datedebut = datedebut;
        this.datefin = dateFin;
        this.type = type;
        this.commentaire = commentaire;
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getDateDebut() {
        return datedebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.datedebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return datefin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.datefin = dateFin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Personne getPersonne() {
        return personne_vacance;
    }

    public void setPersonne(Personne personne_vacance) {
        this.personne_vacance = personne_vacance;
    }
}
