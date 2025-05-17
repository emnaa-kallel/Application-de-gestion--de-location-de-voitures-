package location.models;

import java.util.Date;

/**
 * Classe représentant une assurance selon le schéma Oracle.
 * id_assurance NUMBER(10) PRIMARY KEY,
 * compagnie VARCHAR2(100)
 */
public class Assurance {
    private int idAssurance;
    private String compagnie;
    
    // Attributs supplémentaires pour compatibilité avec le code existant
    private String numeroPolice;
    private double montant;
    private Location location;
    private Date dateDebut;
    private Date dateFin;
    private int idVoiture;

    public Assurance() {}

    // Constructeur selon le schéma Oracle
    public Assurance(int idAssurance, String compagnie) {
        this.idAssurance = idAssurance;
        this.compagnie = compagnie;
    }
    
    // Constructeur complet pour compatibilité avec le code existant
    public Assurance(int idAssurance, int idVoiture, String compagnie, String numeroPolice, Date dateDebut, Date dateFin, double montant) {
        this.idAssurance = idAssurance;
        this.idVoiture = idVoiture;
        this.compagnie = compagnie;
        this.numeroPolice = numeroPolice;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.montant = montant;
    }

    // Getters et Setters
    public int getIdAssurance() { return idAssurance; }
    public void setIdAssurance(int idAssurance) { this.idAssurance = idAssurance; }

    public int getIdVoiture() { return idVoiture; }
    public void setIdVoiture(int idVoiture) { this.idVoiture = idVoiture; }

    public String getCompagnie() { return compagnie; }
    public void setCompagnie(String compagnie) { this.compagnie = compagnie; }

    public Date getDateDebut() { return dateDebut; }
    public void setDateDebut(Date dateDebut) { this.dateDebut = dateDebut; }

    public Date getDateFin() { return dateFin; }
    public void setDateFin(Date dateFin) { this.dateFin = dateFin; }

    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }

    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
    }

    public String getNumeroPolice() {
        return numeroPolice;
    }

    public void setId(int id) {
        this.idAssurance = id;
    }

    public void setNumeroPolice(String numeroPolice) {
        this.numeroPolice = numeroPolice;
    }
}
