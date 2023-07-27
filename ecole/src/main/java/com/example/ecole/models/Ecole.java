package com.example.ecole.models;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name="ecole")
public class Ecole {
    @Id
    @Column(name="id",nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Basic
    @Column(length=30, nullable=false)
    private String nom;
    @Basic
    @Column(length=30, nullable=false)
    private String adresse;
    @Basic
    @Column(length=30, nullable=false)
    private  String type;
    @Column(length = 30, nullable = false)
    private String mail;
    @Column(length = 30, nullable = false)
    private  String number;



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

    public  Ecole(){

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

