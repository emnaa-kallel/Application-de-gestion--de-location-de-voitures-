package location.models;

import java.util.Date;

/**
 * Classe représentant une voiture selon le schéma Oracle.
 * immatriculation VARCHAR2(20) PRIMARY KEY,
 * marque VARCHAR2(50),
 * modele VARCHAR2(50),
 * annee NUMBER(4),
 * etat VARCHAR2(20) CHECK (etat IN ('en_marche', 'en_panne')),
 * disponibilite VARCHAR2(20) CHECK (disponibilite IN ('disponible', 'louee', 'indisponible')),
 * prix_jour DECIMAL(10,3),
 * carburant VARCHAR2(20) CHECK (carburant IN ('essence', 'diesel', 'electrique')),  
 * transmission VARCHAR2(20) CHECK (transmission IN ('automatique', 'manuel')),
 * date_debut_assurance DATE,
 * date_expiration_assurance DATE,
 * id_assurance NUMBER(10),
 * FOREIGN KEY (id_assurance) REFERENCES Assurance(id_assurance) ON DELETE CASCADE
 */
public class Voiture {
    private String immatriculation;
    private String marque;
    private String modele;
    private int annee;
    private String etat;
    private String disponibilite;
    private double prixJour;
    private String carburant;
    private String transmission;
    private Date dateDebutAssurance;
    private Date dateExpirationAssurance;
    private int idAssurance;
    
    // Attributs pour compatibilité avec le code existant
    private String couleur;
    private String photoPath;

    public Voiture() {}

    // Constructeur complet selon le schéma Oracle
    public Voiture(String immatriculation, String marque, String modele, int annee,
                   String etat, String disponibilite, double prixJour, String carburant, 
                   String transmission, Date dateDebutAssurance, Date dateExpirationAssurance, 
                   int idAssurance) {
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
        this.annee = annee;
        this.etat = etat;
        this.disponibilite = disponibilite;
        this.prixJour = prixJour;
        this.carburant = carburant;
        this.transmission =transmission;
        this.dateDebutAssurance = dateDebutAssurance;
        this.dateExpirationAssurance = dateExpirationAssurance;
        this.idAssurance = idAssurance;
    }

    // Constructeur pour compatibilité avec le code existant
    public Voiture(String marque, String modele, int annee, String immatriculation,
                   String etat, String disponibilite, double prixJour, String carburant, String transmission) {
        this.marque = marque;
        this.modele = modele;
        this.annee = annee;
        this.immatriculation = immatriculation;
        setEtat(etat);
        setDisponibilite(disponibilite);
        this.prixJour = prixJour;
        setCarburant(carburant);
        setTransmission(transmission);
    }

    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }

    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }

    public int getAnnee() { return annee; }
    public void setAnnee(int annee) { this.annee = annee; }

    public String getImmatriculation() { return immatriculation; }
    public void setImmatriculation(String immatriculation) { this.immatriculation = immatriculation; }

    public String getEtat() { return etat; }
    public void setEtat(String etat) {
        if (etat != null && (etat.equalsIgnoreCase("en_marche") || etat.equalsIgnoreCase("en_panne"))) {
            this.etat = etat.toLowerCase();
        } else {
            // Pour compatibilité avec l'ancien code
            if (etat != null && etat.equalsIgnoreCase("En marche")) {
                this.etat = "en_marche";
            } else if (etat != null && etat.equalsIgnoreCase("En panne")) {
                this.etat = "en_panne";
            } else {
                throw new IllegalArgumentException("L'état doit être 'en_marche' ou 'en_panne'");
            }
        }
    }

    public String getDisponibilite() { return disponibilite; }
    public void setDisponibilite(String disponibilite) {
        if (disponibilite != null && (disponibilite.equalsIgnoreCase("disponible") || 
                                     disponibilite.equalsIgnoreCase("louee") || 
                                     disponibilite.equalsIgnoreCase("indisponible"))) {
            this.disponibilite = disponibilite.toLowerCase();
        } else {
            // Pour compatibilité avec l'ancien code
            if (disponibilite != null && disponibilite.equalsIgnoreCase("Disponible")) {
                this.disponibilite = "disponible";
            } else if (disponibilite != null && disponibilite.equalsIgnoreCase("Louée")) {
                this.disponibilite = "louee";
            } else if (disponibilite != null && disponibilite.equalsIgnoreCase("Indisponible")) {
                this.disponibilite = "indisponible";
            } else {
                throw new IllegalArgumentException("La disponibilité doit être 'disponible', 'louee' ou 'indisponible'");
            }
        }
    }

    public double getPrixJour() { return prixJour; }
    public void setPrixJour(double prixJour) { this.prixJour = prixJour; }

    public String getCarburant() { return carburant; }
    public void setCarburant(String carburant) {
        if (carburant != null && (carburant.equalsIgnoreCase("essence") || 
                                carburant.equalsIgnoreCase("diesel") || 
                                carburant.equalsIgnoreCase("electrique"))) {
            this.carburant = carburant.toLowerCase();
        } else {
            throw new IllegalArgumentException("Le carburant doit être 'essence', 'diesel' ou 'electrique'");
        }
    }

    public String getTransmission() { return transmission; }
    public void setTransmission(String transmission) {
        if (transmission != null && (transmission.equalsIgnoreCase("automatique") || transmission.equalsIgnoreCase("manuel"))) {
            this.transmission = transmission.toLowerCase();
        } else {
            throw new IllegalArgumentException("La transmission doit être 'automatique' ou 'manuel'");
        }
    }
    
    public Date getDateDebutAssurance() { return dateDebutAssurance; }
    public void setDateDebutAssurance(Date dateDebutAssurance) { this.dateDebutAssurance = dateDebutAssurance; }

    public Date getDateExpirationAssurance() { return dateExpirationAssurance; }
    public void setDateExpirationAssurance(Date dateExpirationAssurance) { this.dateExpirationAssurance = dateExpirationAssurance; }

    public int getIdAssurance() { return idAssurance; }
    public void setIdAssurance(int idAssurance) { this.idAssurance = idAssurance; }

    // Pour compatibilité avec le code précédent
    public void setDisponible(boolean disponible) {
        this.disponibilite = disponible ? "disponible" : "louee";
    }

    public void setPrixJournalier(double prix) {
        this.prixJour = prix;
    }

    public double getPrixJournalier() {
        return this.prixJour;
    }

    public boolean isDisponible() {
        return "disponible".equalsIgnoreCase(this.disponibilite);
    }
    
    public boolean isEnPanne() {
        return "en_panne".equalsIgnoreCase(this.etat);
    }
    
    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public String getCouleur() {
        return this.couleur != null ? this.couleur : "Non spécifiée";
    }
    
    public String getPhotoPath() {
        return photoPath;
    }
    
    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
    
    // Méthode pour obtenir l'ID pour la compatibilité avec le code existant
    public String getId() {
        return this.immatriculation;
    }

    // Méthodes de compatibilité pour le carburant
    public String getcarburant() {
        return getCarburant();
    }
    
    public void setcarburant(String carburant) {
        setCarburant(carburant);
    }
    
    @Override
    public String toString() {
        return "Voiture{" +
                "immatriculation='" + immatriculation + '\'' +
                ", marque='" + marque + '\'' +
                ", modele='" + modele + '\'' +
                ", annee=" + annee +
                ", etat='" + etat + '\'' +
                ", disponibilite='" + disponibilite + '\'' +
                ", prixJour=" + prixJour +
                '}';
    }
}
