package location.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Informations de connexion - à adapter selon votre configuration
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "sahar";
    private static final String PASSWORD = "oracle";
    
    private static Connection connection = null;
    
    // Méthode pour obtenir la connexion
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Tentative de connexion à la base de données Oracle...");
                System.out.println("URL: " + URL);
                System.out.println("Utilisateur: " + USER);
                
                // Charger le pilote Oracle
                try {
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    System.out.println("Pilote Oracle chargé avec succès");
                } catch (ClassNotFoundException e) {
                    System.err.println("ERREUR: Pilote Oracle introuvable: " + e.getMessage());
                    e.printStackTrace();
                    return null;
                }
                
                // Établir la connexion
                try {
                    connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    System.out.println("Connexion à la base de données établie avec succès!");
                    
                    // Test de la connexion
                    if (connection.isValid(5)) {
                        System.out.println("Connexion testée et validée");
                    } else {
                        System.err.println("ERREUR: La connexion est établie mais semble invalide");
                    }
                } catch (SQLException e) {
                    System.err.println("ERREUR: Impossible de se connecter à la base de données Oracle: " + e.getMessage());
                    e.printStackTrace();
                    return null;
                }
            } else {
                // Vérifier si la connexion existante est toujours valide
                if (!connection.isValid(2)) {
                    System.out.println("La connexion existante n'est plus valide, tentative de reconnexion...");
                    connection.close();
                    return getConnection(); // Récursion pour rétablir la connexion
                }
            }
        } catch (SQLException e) {
            System.err.println("ERREUR lors de la vérification/fermeture de la connexion: " + e.getMessage());
            e.printStackTrace();
            connection = null; // Réinitialiser la connexion en cas d'erreur
        }
        
        return connection;
    }
    
    // Méthode pour fermer la connexion
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connexion à la base de données fermée");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
        }
    }
}