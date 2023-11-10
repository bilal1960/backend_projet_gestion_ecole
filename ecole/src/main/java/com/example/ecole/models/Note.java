package com.example.ecole.models;

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
    private LocalDate deliberation;
    private  String     session;
    private int  resultat;
    private float moyenne;
    private boolean reussi;

    public Note() {
    }

    // Constructeur paramétré
    public Note( String nom, LocalDate deliberation, String session, int resultat, float moyenne, boolean reussi) {

        this.nom = nom;
        this.deliberation = deliberation;
        this.session = session;
        this.resultat = resultat;
        this.moyenne = moyenne;
        this.reussi = reussi;
    }

    // Getters et setters
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

    public int getResultat() {
        return resultat;
    }

    public void setResultat(int resultat) {
        this.resultat = resultat;
    }

    public float getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(float moyenne) {
        this.moyenne = moyenne;
    }

    public boolean isReussi() {
        return reussi;
    }

    public void setReussi(boolean reussi) {
        this.reussi = reussi;
    }


}
