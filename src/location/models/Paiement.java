package location.models;

import java.util.Date;

public class Paiement {
    private int idPaiement;
    private int idLocation;
    private double montant;
    private String modePaiement;
    private Date datePaiement;
    private String statutPaiement;
    private Location location; // Référence à l'objet Location

    public Paiement() {}

    public Paiement(int idPaiement, int idLocation, double montant, String modePaiement, Date datePaiement, String statutPaiement) {
        this.idPaiement = idPaiement;
        this.idLocation = idLocation;
        this.montant = montant;
        this.modePaiement = modePaiement;
        this.datePaiement = datePaiement;
        this.statutPaiement = statutPaiement;
    }

    // Getters et Setters
    public int getIdPaiement() { return idPaiement; }
    public void setIdPaiement(int idPaiement) { this.idPaiement = idPaiement; }

    public int getIdLocation() { return idLocation; }
    public void setIdLocation(int idLocation) { this.idLocation = idLocation; }

    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }

    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) { this.modePaiement = modePaiement; }

    public Date getDatePaiement() { return datePaiement; }
    public void setDatePaiement(Date datePaiement) { this.datePaiement = datePaiement; }

    public String getStatutPaiement() { return statutPaiement; }
    public void setStatutPaiement(String statutPaiement) { this.statutPaiement = statutPaiement; }

    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
        if (location != null) {
            this.idLocation = location.getId();
        }
    }

    public void setId(int id) {
        this.idPaiement = id;
    }
}
