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
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate debut;
    @Column(name = "fin", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private  LocalDate fin;
    @ManyToMany(mappedBy = "matieres")
    private List<Personne> personnes;
    @ManyToOne
    @JoinColumn(name = "inscriptions_id")

    private  Inscription inscriptions;

    protected Matiere(){

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

    public String getNom(){return  nom;}

    public  void  setNom(String nom){
        this.nom = nom;
    }

}