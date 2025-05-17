package location.dao;

import location.models.Paiement;
import location.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaiementDAO {

    // Méthode pour ajouter un paiement
    public boolean ajouter(Paiement paiement) {
        String sql = "INSERT INTO paiement (id_location, montant, date_paiement, mode_paiement, statut_paiement) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, paiement.getLocation().getId());  // Lier le paiement à la location
            pstmt.setDouble(2, paiement.getMontant());
            pstmt.setDate(3, new java.sql.Date(paiement.getDatePaiement().getTime()));
            pstmt.setString(4, paiement.getModePaiement());
            pstmt.setString(5, paiement.getStatutPaiement());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du paiement: " + e.getMessage());
            return false;
        }
    }

    // Méthode pour lister tous les paiements
    public List<Paiement> listerTous() {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Paiement paiement = new Paiement();
                paiement.setId(rs.getInt("id"));
                paiement.setMontant(rs.getDouble("montant"));
                paiement.setDatePaiement(rs.getDate("date_paiement"));
                paiement.setModePaiement(rs.getString("mode_paiement"));
                
                // Lier le paiement à une location (vous devrez probablement récupérer l'objet Location en fonction de l'ID)
                paiement.setLocation(new LocationDAO().trouverParId(rs.getInt("id_location")));
                
                paiements.add(paiement);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des paiements: " + e.getMessage());
        }
        
        return paiements;
    }

    // Méthode pour trouver un paiement par son ID
    public Paiement trouverParId(int id) {
        String sql = "SELECT * FROM paiement WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Paiement paiement = new Paiement();
                    paiement.setId(rs.getInt("id"));
                    paiement.setMontant(rs.getDouble("montant"));
                    paiement.setDatePaiement(rs.getDate("date_paiement"));
                    paiement.setModePaiement(rs.getString("mode_paiement"));
                    
                    // Lier le paiement à une location (vous devrez probablement récupérer l'objet Location en fonction de l'ID)
                    paiement.setLocation(new LocationDAO().trouverParId(rs.getInt("id_location")));
                    
                    return paiement;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du paiement: " + e.getMessage());
        }
        
        return null;
    }

    // Méthode pour lister tous les paiements pour une location spécifique
    public List<Paiement> listerPaiementsParLocation(int idLocation) {
        List<Paiement> paiements = new ArrayList<>();
        String sql = "SELECT * FROM paiement WHERE id_location = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idLocation);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Paiement paiement = new Paiement();
                    paiement.setId(rs.getInt("id"));
                    paiement.setMontant(rs.getDouble("montant"));
                    paiement.setDatePaiement(rs.getDate("date_paiement"));
                    paiement.setModePaiement(rs.getString("mode_paiement"));
                    
                    // Lier le paiement à une location (vous devrez probablement récupérer l'objet Location en fonction de l'ID)
                    paiement.setLocation(new LocationDAO().trouverParId(rs.getInt("id_location")));
                    
                    paiements.add(paiement);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des paiements pour cette location: " + e.getMessage());
        }
        
        return paiements;
    }
}
