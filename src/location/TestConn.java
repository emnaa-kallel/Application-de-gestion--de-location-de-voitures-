package location;

import location.utils.DatabaseConnection;
import java.sql.Connection;

public class TestConn {
    public static void main(String[] args) {
        Connection conn = DatabaseConnection.getConnection();
        if (conn != null) {
            System.out.println("Connexion réussie!");
            DatabaseConnection.closeConnection();
        } else {
            System.out.println("Échec de la connexion!");
        }
    }
}