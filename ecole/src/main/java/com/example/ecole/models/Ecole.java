package com.example.ecole.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.util.UUID;

@Entity
@Table(name = "ecole")
public class Ecole {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 30, nullable = false)
    private String nom;
    @Pattern(regexp = "^\\s*[A-Za-z][A-Za-z\\d\\s./]*\\s*$", message = "Entrer une adresse valide")
    @Column(length = 30, nullable = false)
    private String adresse;

    @Pattern(regexp = "secondaire(\\s?/\\s?supérieur)?", message = "Le type doit être 'secondaire' ou 'secondaire/supérieur'")
    @Column(length = 30, nullable = false)
    private String type;

    @Pattern(regexp = ".*@.*\\..*", message = "Entrer une adresse e-mail valide")
    @Column(length = 30, nullable = false)
    private String mail;

    @Pattern(regexp = "(\\+32\\s?)?\\d{1,}(\\s?\\d{2}){1,}", message = "Le numéro de téléphone doit être au format +32 XXX XX XX XX ou XXX XX XX XX")
    @Column(length = 30, nullable = false)
    private String number;

    public Ecole(String nom, String adresse, String mail, String number, String type) {
        this.nom = nom;
        this.adresse = adresse;
        this.mail = mail;
        this.type = type;
        this.number = number;
    }

    public UUID getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getType() {
        return type;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Ecole() {

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
