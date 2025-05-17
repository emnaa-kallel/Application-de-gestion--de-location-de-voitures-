package location.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * Classe pour tester la connexion à la base de données Oracle
 */
public class OracleConnectionTest {
    
    public static void main(String[] args) {
        System.out.println("Test de connexion à la base de données Oracle...");
        
        try {
            // Tenter d'établir la connexion
            Connection conn = DatabaseConnection.getConnection();
            
            if (conn != null && !conn.isClosed()) {
                // Récupérer les métadonnées
                DatabaseMetaData metaData = conn.getMetaData();
                
                // Afficher les informations de connexion
                System.out.println("================ Connexion réussie ================");
                System.out.println("URL de connexion : " + metaData.getURL());
                System.out.println("Nom du serveur : " + metaData.getDatabaseProductName());
                System.out.println("Version du serveur : " + metaData.getDatabaseProductVersion());
                System.out.println("Pilote JDBC : " + metaData.getDriverName());
                System.out.println("Version du pilote : " + metaData.getDriverVersion());
                System.out.println("================================================");
                
                // Fermer la connexion
                DatabaseConnection.closeConnection();
            } else {
                System.err.println("Échec de connexion à la base de données.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur SQL lors du test de connexion : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erreur lors du test de connexion : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
