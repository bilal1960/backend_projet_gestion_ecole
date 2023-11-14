package com.example.ecole.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;
@Entity
@Table(name = "note")
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nom;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate deliberation;
    private  String     session;
    private double  resultat;
    @Column(name = "reussi")
    private boolean reussi;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "personne_note")
    private Personne personne_note;

    public Note() {
    }


    public Note( String nom, LocalDate deliberation, String session, double resultat, boolean reussi) {

        this.nom = nom;
        this.deliberation = deliberation;
        this.session = session;
        this.resultat = resultat;
        this.reussi = reussi;
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

    public LocalDate getDeliberation() {
        return deliberation;
    }

    public void setDeliberation(LocalDate deliberation) {
        this.deliberation = deliberation;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public double getResultat() {
        return resultat;
    }

    public void setResultat(double resultat) {
        this.resultat = resultat;
    }

    public boolean isReussi() {
        return reussi;
    }

    public void setReussi(boolean reussi) {
        this.reussi = reussi;
    }

    public Personne getPersonne() {
        return personne_note;
    }

    public void setPersonne(Personne personne_note) {
        this.personne_note = personne_note;
    }

}
