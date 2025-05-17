package location.services;

import location.dao.UtilisateurDAO;
import location.models.Utilisateur;

public class AuthenticationService {
    private static Utilisateur utilisateurConnecte = null;
    
    // Méthode pour authentifier un utilisateur
    public static boolean login(String login, String motDePasse) {
        UtilisateurDAO dao = new UtilisateurDAO();
        Utilisateur utilisateur = dao.authentifier(login, motDePasse);
        
        if (utilisateur != null) {
            utilisateurConnecte = utilisateur;
            return true;
        }
        
        return false;
    }
    
    // Méthode pour déconnecter un utilisateur
    public static void logout() {
        utilisateurConnecte = null;
    }
    
    // Méthode pour obtenir l'utilisateur connecté
    public static Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }
    
    // Méthodes pour vérifier les permissions
    public static boolean peutAjouter() {
        return utilisateurConnecte != null && 
               utilisateurConnecte.getTypeUtilisateur().equals("personnel");
    }
    
    public static boolean peutModifier() {
        return utilisateurConnecte != null && 
               utilisateurConnecte.getTypeUtilisateur().equals("personnel");
    }
    
    public static boolean peutSupprimer() {
        return utilisateurConnecte != null && 
               utilisateurConnecte.getTypeUtilisateur().equals("personnel");
    }
}