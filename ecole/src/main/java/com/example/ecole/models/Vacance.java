package com.example.ecole.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;

import java.rmi.server.UID;
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
    @PositiveOrZero
    @Column(name = "conge", nullable = false)
    private  int conge;
    @Column(name = "statut")
    private String statut;

    @Column(name = "type")
    private String type;
    @Column(name = "commentaire")
    private String commentaire;

    public Vacance() {
    }

    // Constructeur avec paramètres
    public Vacance(LocalDate datedebut, LocalDate dateFin, int conge, String statut, String type, String commentaire) {
        this.datedebut = datedebut;
        this.datefin = dateFin;
        this.conge = conge;
        this.statut = statut;
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

    public int getConge() {
        return conge;
    }

    public void setConge(int conge) {
        this.conge = conge;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
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


}
