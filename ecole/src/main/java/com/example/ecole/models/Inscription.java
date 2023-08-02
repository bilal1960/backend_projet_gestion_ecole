package com.example.ecole.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.validator.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.UUID;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name="inscription")
public class Inscription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "commune", nullable = false)
    @Pattern(regexp =  "^[\\p{L}]+$" , message = "commune invalide")
    private String commune;

    @NotNull
    @Positive
    @Column(name = "minerval", nullable = false)
    private float minerval;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate date_inscrit;

    @PositiveOrZero
    @NotNull
    @Column(name = "rembourser")
    private float rembourser;

    @NotEmpty
    @Pattern(regexp = "^(?i)(General|Technique|Professionnel)$", message = "Entrer une section valide (General, Technique, Professionnel)")
    @Column(name = "section")
    private String section;

    @NotEmpty
    @Pattern(regexp = "^[1-6] secondaire$", message = "La valeur doit être entre '1 secondaire' et '6 secondaire'")
    @Column(name = "secondaire_anne")
    private String secondaire_anne;

    @ManyToOne
    @JoinColumn(name = "personne_id")
    private Personne personne_id;

    public Inscription() {
    }

    public Inscription(String commune, float minerval, Personne personne_id, LocalDate date_inscrit, float rembourser, String section, String secondaire_anne) {
        this.commune = commune;
        this.minerval = minerval;
        this.date_inscrit = date_inscrit;
        this.rembourser = rembourser;
        this.section = section;
        this.personne_id = personne_id;
        this.secondaire_anne = secondaire_anne;
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

    public LocalDate getDate_inscrit() {
        return date_inscrit;
    }

    public void setDate_inscrit(LocalDate date_inscrit) {
        this.date_inscrit = date_inscrit;
    }

    public float getRembourser() {
        return rembourser;
    }

    public void setRembourser(float rembourser) {
        this.rembourser = rembourser;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSecondaire_anne() {
        return secondaire_anne;
    }

    public void setSecondaire_anne(String secondaire_anne) {
        this.secondaire_anne = secondaire_anne;
    }

    public Personne getPersonne() {
        return personne_id;
    }

    public void setPersonne(Personne personne_id) {
        this.personne_id = personne_id;
    }
}
