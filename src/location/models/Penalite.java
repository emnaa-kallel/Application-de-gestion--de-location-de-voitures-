package location.models;

import java.util.Date;

/**
 * Classe représentant une pénalité selon le schéma Oracle.
 * id_penalite NUMBER(10) PRIMARY KEY,
 * id_location NUMBER(10),
 * mode_paiement VARCHAR2(20) CHECK (mode_paiement IN ('carte bancaire', 'application D17')),
 * etat_paiement VARCHAR2(50) CHECK (etat_paiement IN ('en_attente', 'effectue')),
 * FOREIGN KEY (id_location) REFERENCES Location(id_location) ON DELETE CASCADE
 */
public class Penalite {
    private int idPenalite;
    private int idLocation;
    private String modePaiement;
    private String etatPaiement;
    
    // Attributs supplémentaires pour compatibilité avec le code existant
    private double montant;
    private String raison;
    private Date datePenalite;
    private Location location; // Référence à l'objet Location

    public Penalite() {}
    
    // Constructeur selon le schéma Oracle
    public Penalite(int idPenalite, int idLocation, String modePaiement, String etatPaiement) {
        this.idPenalite = idPenalite;
        this.idLocation = idLocation;
        setModePaiement(modePaiement);
        setEtatPaiement(etatPaiement);
    }

    // Constructeur pour compatibilité avec le code existant
    public Penalite(int idPenalite, int idLocation, double montant, String raison, Date datePenalite) {
        this.idPenalite = idPenalite;
        this.idLocation = idLocation;
        this.montant = montant;
        this.raison = raison;
        this.datePenalite = datePenalite;
    }

    // Getters et Setters
    public int getIdPenalite() { return idPenalite; }
    public void setIdPenalite(int idPenalite) { this.idPenalite = idPenalite; }

    public int getIdLocation() { return idLocation; }
    public void setIdLocation(int idLocation) { this.idLocation = idLocation; }
    
    public String getModePaiement() { return modePaiement; }
    public void setModePaiement(String modePaiement) {
        if (modePaiement != null && (modePaiement.equalsIgnoreCase("carte bancaire") || modePaiement.equalsIgnoreCase("application D17"))) {
            this.modePaiement = modePaiement;
        } else {
            throw new IllegalArgumentException("Le mode de paiement doit être 'carte bancaire' ou 'application D17'");
        }
    }
    
    public String getEtatPaiement() { return etatPaiement; }
    public void setEtatPaiement(String etatPaiement) {
        if (etatPaiement != null && (etatPaiement.equalsIgnoreCase("en_attente") || etatPaiement.equalsIgnoreCase("effectue"))) {
            this.etatPaiement = etatPaiement.toLowerCase();
        } else {
            throw new IllegalArgumentException("L'état du paiement doit être 'en_attente' ou 'effectue'");
        }
    }

    // Getters et Setters pour compatibilité avec le code existant
    public double getMontant() { return montant; }
    public void setMontant(double montant) { this.montant = montant; }

    public String getRaison() { return raison; }
    public void setRaison(String raison) { this.raison = raison; }

    public Date getDatePenalite() { return datePenalite; }
    public void setDatePenalite(Date datePenalite) { this.datePenalite = datePenalite; }

    public Location getLocation() {
        return location;
    }
    
    public void setLocation(Location location) {
        this.location = location;
        if (location != null) {
            this.idLocation = location.getId();
        }
    }

    public String getDescription() {
        return "Pénalité du " + (datePenalite != null ? datePenalite.toString() : "non datée") + ": " + raison + " - " + montant + " DH";
    }
    
    public void setId(int id) {
        this.idPenalite = id;
    }

    public void setDescription(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
