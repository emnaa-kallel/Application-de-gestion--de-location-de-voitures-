package location.dao;

import location.models.Utilisateur;
import location.utils.DemoDataProvider;

/**
 * Implémentation de démonstration du DAO Utilisateur
 * Utilise des données en mémoire au lieu de la base de données
 */
public class UtilisateurDemoDAO {
    
    /**
     * Authentifie un utilisateur en utilisant les données de démonstration
     * @param email Email de l'utilisateur
     * @param motDePasse Mot de passe de l'utilisateur
     * @return L'utilisateur authentifié ou null si l'authentification échoue
     */
    public Utilisateur authentifier(String email, String motDePasse) {
        return DemoDataProvider.authentifierUtilisateur(email, motDePasse);
    }
}
