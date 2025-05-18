package location.dao;

import location.models.Penalite;
import location.models.Location;
import location.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class PenaliteDAO {

    // Méthode pour ajouter une pénalité
    public boolean ajouter(Penalite penalite) {
        String sql = "INSERT INTO Penalite (id_penalite, id_location, mode_paiement, etat_paiement) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Récupérer le prochain ID disponible
            int newId = getNextPenaliteId();
            
            pstmt.setInt(1, newId);
            pstmt.setInt(2, penalite.getIdLocation());
            pstmt.setString(3, penalite.getModePaiement());
            pstmt.setString(4, penalite.getEtatPaiement());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                penalite.setIdPenalite(newId);
                return true;
            }
            return false;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la pénalité: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Obtenir le prochain ID disponible pour une pénalité
    private int getNextPenaliteId() {
        int maxId = 0;
        String sql = "SELECT MAX(id_penalite) FROM Penalite";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                maxId = rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du prochain ID: " + e.getMessage());
        }
        
        return maxId + 1;
    }
    
    
    public List<Penalite> getAllPenalites() {
    List<Penalite> penalites = new ArrayList<>();
    String sql = "SELECT * FROM PENALITE";

    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            penalites.add(mapResultSetToPenalite(rs));
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la récupération des pénalités : " + e.getMessage());
    }

    return penalites;
}
private Penalite mapResultSetToPenalite(ResultSet rs) throws SQLException {
    int idPenalite = rs.getInt("id_penalite");
    int idLocation = rs.getInt("id_location");
    String modePaiement = rs.getString("mode_paiement");
    String etatPaiement = rs.getString("etat_paiement");

    return new Penalite(idPenalite, idLocation, modePaiement, etatPaiement);
}
    
    public List<Penalite> listerPenalitesParClient(int cinClient) {
        List<Penalite> penalites = new ArrayList<>();
        String sql = "SELECT p.id_penalite, p.id_location, p.mode_paiement, p.etat_paiement " +
                     "FROM Penalite p " +
                     "JOIN Location l ON p.id_location = l.id_location " +
                     "WHERE l.CIN_client = ?";

        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(sql)) {
            statement.setInt(1, cinClient);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Penalite penalite = new Penalite();
                penalite.setIdPenalite(resultSet.getInt("id_penalite"));
                penalite.setIdLocation(resultSet.getInt("id_location"));
                penalite.setModePaiement(resultSet.getString("mode_paiement"));
                penalite.setEtatPaiement(resultSet.getString("etat_paiement"));
                penalites.add(penalite);
            }
        } catch (Exception e) {
            e.printStackTrace(); // Gérer l'exception de manière appropriée
        }

        return penalites;
    }

    // Méthode pour lister toutes les pénalités
    public List<Penalite> listerToutes() {
        List<Penalite> penalites = new ArrayList<>();
        String sql = "SELECT * FROM Penalite";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Penalite penalite = mapResultSetToPenalite(rs);
                penalites.add(penalite);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des pénalités: " + e.getMessage());
            e.printStackTrace();
        }
        
        return penalites;
    }
    
    // Lister les pénalités par location
    public List<Penalite> listerParLocation(int idLocation) {
        List<Penalite> penalites = new ArrayList<>();
        String sql = "SELECT * FROM Penalite WHERE id_location = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idLocation);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Penalite penalite = mapResultSetToPenalite(rs);
                penalites.add(penalite);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des pénalités: " + e.getMessage());
            e.printStackTrace();
        }
        
        return penalites;
    }
    
    // Trouver une pénalité par son ID
    public Penalite trouverParId(int idPenalite) {
        String sql = "SELECT * FROM Penalite WHERE id_penalite = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPenalite);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPenalite(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la pénalité: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Mettre à jour une pénalité
    public boolean mettreAJour(Penalite penalite) {
        String sql = "UPDATE Penalite SET mode_paiement = ?, etat_paiement = ? WHERE id_penalite = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, penalite.getModePaiement());
            pstmt.setString(2, penalite.getEtatPaiement());
            pstmt.setInt(3, penalite.getIdPenalite());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la pénalité: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Supprimer une pénalité
    public boolean supprimer(int idPenalite) {
        String sql = "DELETE FROM Penalite WHERE id_penalite = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idPenalite);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la pénalité: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public List<Penalite> getByCin(String cin) {
    List<Penalite> penalites = new ArrayList<>();
    String sql = """
        SELECT p.id_penalite, p.id_location, p.mode_paiement, p.etat_paiement
        FROM Penalite p
        JOIN Location l ON p.id_location = l.id_location
        WHERE l.cin_client = ?
    """;

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, cin);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Penalite p = new Penalite();
            p.setIdPenalite(rs.getInt("id_penalite"));
            p.setIdLocation(rs.getInt("id_location"));
            p.setModePaiement(rs.getString("mode_paiement"));
            p.setEtatPaiement(rs.getString("etat_paiement"));
            penalites.add(p);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return penalites;
}

}
    
    // Méthode utilitaire pour mapper un ResultSet à un objet Penalite
    /*private Penalite mapResultSetToPenalite(ResultSet rs) throws SQLException {
        Penalite penalite = new Penalite();
        penalite.setIdPenalite(rs.getInt("id_penalite"));
        penalite.setIdLocation(rs.getInt("id_location"));
        penalite.setModePaiement(rs.getString("mode_paiement"));
        penalite.setEtatPaiement(rs.getString("etat_paiement"));
        
        // Pour compatibilité avec le code existant, on peut récupérer la location associée
        try {
            Location location = new LocationDAO().trouverParId(rs.getInt("id_location"));
            if (location != null) {
                penalite.setLocation(location);
            }
        } catch (SQLException e) {
            // Ignorer si on ne peut pas récupérer la location
        }
        
        return penalite;
    }
}
*/