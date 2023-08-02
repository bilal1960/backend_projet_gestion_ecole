package com.example.ecole.models;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;


@Entity
@Table(name = "matiere")
public class Matiere {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;
    @NotEmpty(message = "Le nom ne peut pas être vide")
    @Column(name = "nom", nullable = false)
    private String nom;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate debut;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private  LocalDate fin;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime debutime;

    @JsonFormat(pattern = "HH:mm")
    private
    LocalTime fintime;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "professeur_id")
    private Personne professeur_id;
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]*$", message = "Le local doit commencer par une lettre de l'alphabet ensuite elle peut contenir des lettres ou chiffres positif.")
    @Column(name = "local")
    private  String local;
   @NotEmpty
   @Column(name = "jour")
   private String jour;
    @Pattern(regexp = "^[1-6] secondaire$", message = "La valeur doit être entre '1 secondaire' et '6 secondaire'")
    @Column(name = "secondaire")
    private  String secondaire;


    public  Matiere(){

    }
  public   Matiere(  String nom, LocalDate debut, LocalDate fin, Personne professeur_id, LocalTime debutime, LocalTime fintime,String local, String jour, String secondaire){

        this.nom = nom;
        this.debut = debut;
        this.fin = fin;
        this.professeur_id = professeur_id;
        this.debutime = debutime;
        this.fintime = fintime;
        this.local = local;
        this.jour = jour;
        this.secondaire = secondaire;
    }

    public Matiere(String uuid) {
        this.id = UUID.fromString(uuid);
    }
    public UUID getId() {
        return id;
    }
    public  LocalDate getDebut(){return  debut;}
    public  LocalDate getFin(){return  fin;}

    public void setFin(LocalDate fin) {
        this.fin = fin;
    }

    public void setDebut(LocalDate debut) {
        this.debut = debut;
    }


    public  void setId(UUID id){
        this.id = id;
    }
    public String getNom(){return  nom;}
    public  void  setNom(String nom){
        this.nom = nom;
    }
    public  Personne getPersonne(){return  professeur_id;}
    public void setPersonne(Personne professeur_id) {this.professeur_id = professeur_id;}
    public  LocalTime getDebutime(){return  debutime;}
    public  LocalTime getFintime(){return  fintime;}
    public void setDebutime(LocalTime debutime) {this.debutime = debutime;}
    public  void  setFintime(LocalTime fintime){this.fintime = fintime;}

    public  String getLocal(){return  local;}

    public void setLocal(String local) {this.local = local;}

    public  String getJour(){return  jour;}

    public  void  setJour(String jour){this.jour = jour;}

    public  String getSecondaire(){return  secondaire;}

    public void setSecondaire(String secondaire) {this.secondaire = secondaire;}
}