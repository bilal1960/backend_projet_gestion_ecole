package com.example.ecole.models;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name="inscription")
public class Inscription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
@Column(name = "commune", nullable = false)
    private  String commune;
@Column(name = "minerval", nullable = false)
    private  float minerval;

    @ManyToOne
    @JoinColumn(name = "personne_id")
    private  Personne personne_id;


    public   Inscription(){
    }
    public Inscription(UUID id, String commune, float minerval, Personne personne_id) {
        this.id = id;
        this.commune = commune;
        this.minerval = minerval;
        this.personne_id = personne_id;
    }
    public Inscription(String uuid) {
        this.id = UUID.fromString(uuid);
    }


    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getCommune() {
        return commune;
    }
    public void setCommune(String commune) {
        this.commune = commune;
    }
    public float getMinerval() {
        return minerval;
    }
    public void setMinerval(float minerval) {
        this.minerval = minerval;
    }
    public Personne getPersonne() {return personne_id;
    }
    public void setPersonne(Personne personne_id) {
        this.personne_id = personne_id;
    }




}
