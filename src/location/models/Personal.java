package location.models;

public class Personal {
    private int cin;
    private String nom;
    private String prenom;
    private String adresse;
    private String tel;
    private String email;
    private double salaire;
    private String dateEmbauche;
    private String dateFinContrat;
    
    // Default constructor
    public Personal() {}
    
    // Full constructor
    public Personal(int cin, String nom, String prenom, String adresse, String tel, 
                   String email, double salaire, String dateEmbauche, String dateFinContrat) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.tel = tel;
        this.email = email;
        this.salaire = salaire;
        this.dateEmbauche = dateEmbauche;
        this.dateFinContrat = dateFinContrat;
    }
    
    // Getters and Setters
    public int getCin() {
        return cin;
    }
    
    public void setCin(int cin) {
        this.cin = cin;
    }
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    
    public String getAdresse() {
        return adresse;
    }
    
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    
    public String getTel() {
        return tel;
    }
    
    public void setTel(String tel) {
        this.tel = tel;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public double getSalaire() {
        return salaire;
    }
    
    public void setSalaire(double salaire) {
        this.salaire = salaire;
    }
    
    public String getDateEmbauche() {
        return dateEmbauche;
    }
    
    public void setDateEmbauche(String dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }
    
    public String getDateFinContrat() {
        return dateFinContrat;
    }
    
    public void setDateFinContrat(String dateFinContrat) {
        this.dateFinContrat = dateFinContrat;
    }
    
    @Override
    public String toString() {
        return "Personal{" +
                "cin=" + cin +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", tel='" + tel + '\'' +
                ", email='" + email + '\'' +
                ", salaire=" + salaire +
                ", dateEmbauche='" + dateEmbauche + '\'' +
                ", dateFinContrat='" + dateFinContrat + '\'' +
                '}';
    }
}
