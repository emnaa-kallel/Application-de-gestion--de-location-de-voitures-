package location.models;

import java.util.Date;

/**
 * Classe représentant une réservation de voiture
 */
public class Reservation {
    private int idReservation;
    private int idClient;
    private int idVoiture;
    private Date dateDebut;
    private Date dateFin;
    private double prixTotal;
    private String statut; // "En attente", "Confirmée", "Annulée", "Terminée"
    
    // Variables temporaires pour faciliter l'affichage (non persistantes)
    private String nomClient;
    private String voitureInfo;
    
    /**
     * Constructeur par défaut
     */
    public Reservation() {
    }
    
    /**
     * Constructeur avec paramètres
     * @param idReservation Identifiant de la réservation
     * @param idClient Identifiant du client
     * @param idVoiture Identifiant de la voiture
     * @param dateDebut Date de début de la réservation
     * @param dateFin Date de fin de la réservation
     * @param prixTotal Prix total de la réservation
     * @param statut Statut de la réservation
     */
    public Reservation(int idReservation, int idClient, int idVoiture, Date dateDebut, Date dateFin, double prixTotal, String statut) {
        this.idReservation = idReservation;
        this.idClient = idClient;
        this.idVoiture = idVoiture;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prixTotal = prixTotal;
        this.statut = statut;
    }
    
    /**
     * Calcule le nombre de jours de la réservation
     * @return Nombre de jours entre la date de début et la date de fin
     */
    public int getNombreJours() {
        if (dateDebut != null && dateFin != null) {
            long diff = dateFin.getTime() - dateDebut.getTime();
            return (int) (diff / (1000 * 60 * 60 * 24)) + 1; // +1 car on compte le jour de début
        }
        return 0;
    }
    
    // Getters et Setters
    
    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getIdVoiture() {
        return idVoiture;
    }

    public void setIdVoiture(int idVoiture) {
        this.idVoiture = idVoiture;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public String getVoitureInfo() {
        return voitureInfo;
    }

    public void setVoitureInfo(String voitureInfo) {
        this.voitureInfo = voitureInfo;
    }
    
    @Override
    public String toString() {
        return "Reservation{" + "idReservation=" + idReservation + ", idClient=" + idClient + 
               ", idVoiture=" + idVoiture + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin + 
               ", prixTotal=" + prixTotal + ", statut=" + statut + '}';
    }
}
