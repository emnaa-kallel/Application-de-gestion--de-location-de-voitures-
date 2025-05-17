package location.models;

import java.util.Date;

/**
 * Classe représentant une location selon le schéma Oracle.
 * id_location NUMBER(10)  PRIMARY KEY,
 * CIN_client NUMBER(8),
 * immatriculation VARCHAR2(20),
 * date_debut DATE,
 * date_fin DATE,
 * date_retour DATE,
 * mode_paiement VARCHAR2(20) CHECK (mode_paiement IN ('carte bancaire', 'application D17')),
 * statut VARCHAR2(50) CHECK (statut IN ('en_cours', 'terminee')),
 * FOREIGN KEY (CIN_client) REFERENCES Client(CIN) ON DELETE CASCADE,  
 * FOREIGN KEY (immatriculation) REFERENCES Voiture(immatriculation) ON DELETE CASCADE
 */
public class Location {
    private int id_location;
    private int CIN_client;
    private String immatriculation;
    private Date date_debut;
    private Date date_fin;
    private Date date_retour;
    private String mode_paiement;
    private String statut;
    
    // Références aux objets liés
    private Client client;
    private Voiture voiture;

    public Location() {
        // Initialiser avec des valeurs par défaut
        this.statut = "en_cours";
    }

    // Constructeur selon le schéma Oracle
    public Location(int id_location, int CIN_client, String immatriculation, Date date_debut, Date date_fin,
                    Date date_retour, String mode_paiement, String statut) {
        this.id_location = id_location;
        this.CIN_client = CIN_client;
        this.immatriculation = immatriculation;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.date_retour = date_retour;
        this.mode_paiement=mode_paiement;//setModePaiement(mode_paiement);
        this.statut=statut;//setStatut(statut);
    }
    
    // Constructeur pour compatibilité avec le code existant
    public Location(int id_location, int CIN_client, int CIN_utilisateur, String immatriculation, Date date_debut, Date date_fin,
                    double prix_total, String statut, double km_depart, double km_retour) {
        this.id_location = id_location;
        this.CIN_client = CIN_client;
        this.immatriculation = immatriculation;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.statut=statut;//setStatut(statut);
    }

    // Getters et Setters
    public int getId() { return id_location; }
    public void setId(int id_location) { this.id_location = id_location; }

    public int getClientCin() { return CIN_client; }
    public void setClientCin(int CIN_client) { this.CIN_client = CIN_client; }
 

    public String getVoitureId() { return immatriculation; }
    public void setVoitureId(String immatriculation) { this.immatriculation = immatriculation; }

    public Date getDateDebut() { return date_debut; }
    public void setDateDebut(Date date_debut) { this.date_debut = date_debut; }

    public Date getDateFin() { return date_fin; }
    public void setDateFin(Date date_fin) { this.date_fin = date_fin; }

    public String getStatut() { return statut; }  
    public void setStatut(String statut) { 
        if (statut != null && (statut.equalsIgnoreCase("en_cours") || statut.equalsIgnoreCase("terminee"))) {
            this.statut = statut.toLowerCase();
        } else {
            // Pour compatibilité avec l'ancien code
            if (statut != null && statut.equalsIgnoreCase("En cours")) {
                this.statut = "en_cours";
            } else if (statut != null && statut.equalsIgnoreCase("Terminée")) {
                this.statut = "terminee";
            } else {
                throw new IllegalArgumentException("Le statut doit être 'en_cours' ou 'terminee'");
            }
        }
    }
    
    public Date getDateRetour() { return date_retour; }
    public void setDateRetour(Date date_retour) { this.date_retour = date_retour; }
    
    public String getModePaiement() { return mode_paiement; }
    public void setModePaiement(String mode_paiement) {
        if (mode_paiement != null && (mode_paiement.equalsIgnoreCase("carte bancaire") || mode_paiement.equalsIgnoreCase("application D17"))) {
            this.mode_paiement = mode_paiement;
        } else {
            //throw new IllegalArgumentException("Le mode de paiement doit être 'carte bancaire' ou 'application D17'");
        }
    }

    
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    
    public Voiture getVoiture() { return voiture; }
    public void setVoiture(Voiture voiture) { this.voiture = voiture; }

    // Méthodes utilitaires
    
    // Calcul de la durée de location en jours
    public int getDureeJours() {
        if (date_debut == null || date_fin == null) {
            return 0;
        }
        long diff = date_fin.getTime() - date_debut.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24)) + 1; // +1 car on compte le jour de début
    }
    
    

    // Méthodes pour la compatibilité avec le code existant
    public String getCommentaires() { 
        return ""; // Cette colonne n'existe pas dans la table de base de données
    }
    
    public void setCommentaires(String commentaires) {
        // Ne rien faire, cette colonne n'existe pas dans la table de base de données
    }
}
