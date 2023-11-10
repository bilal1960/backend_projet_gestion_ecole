package com.example.ecole.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "absence")
public class Absence {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // UUID generation strategy set to AUTO.
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private UUID id;

    @Column(name = "presence", nullable = false, length = 50)
    private String presence;  // This could be an Enum based on the types of presence you have.
    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "date", nullable = false)
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm")
    @Column(name = "heuredebut")
    private LocalTime heuredebut;
    @JsonFormat(pattern = "HH:mm")
    @Column(name = "heurefin")
    private LocalTime heurefin;

    @Column(name = "certificat")
    private boolean certficat;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "personne_idpresence")
    private Personne personne_idpresence;

    public Absence() {
    }

    public Absence(String presence, LocalDate date, LocalTime heuredebut, LocalTime heurefin, boolean certficat) {
        this.presence = presence;
        this.date = date;
        this.heuredebut = heuredebut;
        this.heurefin = heurefin;
        this.certficat = certficat;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPresence() {
        return presence;
    }

    public void setPresence(String presence) {
        this.presence = presence;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeuredebut() {
        return heuredebut;
    }

    public void setHeuredebut(LocalTime heuredebut) {
        this.heuredebut = heuredebut;
    }

    public LocalTime getHeurefin() {
        return heurefin;
    }

    public void setHeurefin(LocalTime heurefin) {
        this.heurefin = heurefin;
    }

    public boolean isCertficat() {
        return certficat;
    }

    public void setCertficat(boolean certficat) {
        this.certficat = certficat;
    }

    public Personne getPersonne() {
        return personne_idpresence;
    }

    public void setPersonne(Personne personne_idpresence) {
        this.personne_idpresence = personne_idpresence;
    }
}
