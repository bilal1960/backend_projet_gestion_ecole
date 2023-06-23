package com.example.ecole.models;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;
import  java.util.List;

@Entity
@Table(name = "matiere")
public class Matiere {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    @Column(name = "nom", nullable = false)
    private String nom;
    @Column(name = "debut", nullable = false)
    private LocalDate debut;
    @Column(name = "fin", nullable = false)
    private  LocalDate fin;
    @ManyToMany(mappedBy = "matieres")
    private List<Personne> personnes;
    public String getUsername() {
        return nom;
    }

    protected Matiere(){

    }

    public  void  setNom(String nom){
        this.nom = nom;
    }

  public   Matiere(String nom, LocalDate debut, LocalDate fin, List<Personne> personnes){
        this.nom = nom;
        this.debut = debut;
        this.fin = fin;
        this.personnes = personnes;
    }

    public UUID getId() {
        return id;
    }

    public  LocalDate getDebut(){return  debut;}

    public  LocalDate getFin(){return  fin;}

    public  List<Personne> getPersonnes(){return  personnes;}

    public  void setId(UUID id){
        this.id = id;
    }





}