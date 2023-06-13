package models;
import jakarta.persistence.*;

@Entity
@Table(name="ecole")
public class Ecole {
    @Id
    @Column(name="ecoleid")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    @Column(length=30, nullable=false)
    private String nom;
    @Basic
    @Column(length=30, nullable=false)
    private String adresse;
    private int nombreEtudiants;
    @Basic
    @Column(length=30, nullable=false)
    private  String type;



    public Ecole(String nom, String adresse, int nombreEtudiants, String type) {
        this.nom = nom;
        this.adresse = adresse;
        this.nombreEtudiants = nombreEtudiants;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public int getNombreEtudiants() {
        return nombreEtudiants;
    }

    public String getType() {
        return type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public void setNombreEtudiants(int nombreEtudiants) {
        this.nombreEtudiants = nombreEtudiants;
    }

    public void setType(String type) {
        this.type = type;
    }

}
