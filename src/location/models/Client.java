package location.models;

import java.util.Date;

/**
 * Classe représentant un client selon le schéma Oracle.
 * CIN NUMBER(8) PRIMARY KEY,
 * adresse VARCHAR2(255),
 * date_naissance DATE,
 * permis_conduire NUMBER(6) UNIQUE,
 * date_expiration_permis DATE,
 * FOREIGN KEY (CIN) REFERENCES Utilisateur(CIN) ON DELETE CASCADE
 */
public class Client {
    private int cin;
    private String adresse;
    private Date dateNaissance;
    private int permisConduire;
    private Date dateExpirationPermis;
    
    // Attributs supplémentaires pour compatibilité avec le code existant
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    
    // Constructeur vide
    public Client() {}
    
    // Constructeur avec paramètres essentiels selon le schéma Oracle
    public Client(int cin, String adresse, Date dateNaissance, int permisConduire, Date dateExpirationPermis) {
        this.cin = cin;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
        this.permisConduire = permisConduire;
        this.dateExpirationPermis = dateExpirationPermis;
    }
    
    // Constructeur complet pour compatibilité avec le code existant
    public Client(int cin, String nom, String prenom, String telephone, String email, 
                 String adresse, Date dateNaissance, int permisConduire, Date dateExpirationPermis) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
        this.dateNaissance = dateNaissance;
        this.permisConduire = permisConduire;
        this.dateExpirationPermis = dateExpirationPermis;
    }
    
    // Getters et Setters
    public int getCin() { return cin; }
    public void setCin(int cin) { this.cin = cin; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }
    
    public Date getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(Date dateNaissance) { this.dateNaissance = dateNaissance; }
    
    public int getPermisConduire() { return permisConduire; }
    public void setPermisConduire(int permisConduire) { this.permisConduire = permisConduire; }
    
    // Méthode pour compatibilité avec l'ancien code qui utilisait une chaîne pour le permis
    public void setPermisConduire(String permisConduire) {
        try {
            this.permisConduire = Integer.parseInt(permisConduire);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Le permis de conduire doit être un nombre de 6 chiffres");
        }
    }
    
    // Méthode pour compatibilité avec l'ancien code qui utilisait une chaîne pour le permis
    public String getPermisConduireAsString() {
        return String.valueOf(this.permisConduire);
    }
    
    public Date getDateExpirationPermis() { return dateExpirationPermis; }
    public void setDateExpirationPermis(Date dateExpirationPermis) { this.dateExpirationPermis = dateExpirationPermis; }
    
    @Override
    public String toString() {
        return nom + " " + prenom;
    }

    public void setCIN(int aInt) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}