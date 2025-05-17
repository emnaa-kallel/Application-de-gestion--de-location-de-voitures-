package location.models;

import java.util.Date;
import location.dao.PersonnelDAO;

/**
 * Classe représentant un utilisateur selon le schéma Oracle.
 * CIN NUMBER(8) PRIMARY KEY,
 * login VARCHAR2(50) UNIQUE NOT NULL,
 * mot_de_passe VARCHAR2(255) NOT NULL,
 * nom VARCHAR2(50),
 * prenom VARCHAR2(50),
 * tel NUMBER(8),
 * email VARCHAR2(100),
 * type_utilisateur VARCHAR2(20) NOT NULL CHECK (type_utilisateur IN ('client', 'personnel')),
 * CONSTRAINT email_format CHECK (email LIKE '%@%.%')
 */
public class Utilisateur {
    private int cin;
    private String login;
    private String mot_de_passe;
    private String nom;
    private String prenom;
    private int tel;
    private String email;
    private String type_utilisateur;

    public Utilisateur() {}

    public Utilisateur(int cin, String login, String mot_de_passe, String nom, String prenom, int tel, String email, String type_utilisateur) {
        this.cin = cin;
        this.login = login;
        this.mot_de_passe = mot_de_passe;
        this.nom = nom;
        this.prenom = prenom;
        this.tel = tel;
        this.email = email;
        this.type_utilisateur = type_utilisateur;
    }

    // Getters et Setters
    public int getCin() { return cin; }
    public void setCin(int cin) { this.cin = cin; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getMotDePasse() { return mot_de_passe; }
    public void setMotDePasse(String mot_de_passe) { this.mot_de_passe = mot_de_passe; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public int getTel() { return tel; }
    public void setTel(int tel) { this.tel = tel; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTypeUtilisateur() { return type_utilisateur; }
    public void setTypeUtilisateur(String type_utilisateur) { 
        if (type_utilisateur != null && (type_utilisateur.equals("client") || type_utilisateur.equals("personnel"))) {
            this.type_utilisateur = type_utilisateur;
        } else {
            throw new IllegalArgumentException("Le type d'utilisateur doit être 'client' ou 'personnel'");
        }
    }

    // Méthodes pour compatibilité avec le reste du code
    public void setId(int id) {
        this.cin = id;
    }

    public void setUsername(String username) {
        this.login = username;
    }

    public void setPassword(String password) {
        this.mot_de_passe = password;
    }

    public int getId() {
        return this.cin;
    }

    public String getUsername() {
        return this.login;
    }

    public String getPassword() {
        return this.mot_de_passe;
    }

    public String getRole() {
        var pdao = new PersonnelDAO();
        var p = pdao.trouverParCin(this.getCin());
        if (p!=null)
            return p.getRole();
        return "client";
    }
}
