package location.dao;

import location.models.Personnel;
import location.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonnelDAO {
    
    // Ajouter un nouveau personnel
    public boolean ajouter(Personnel personnel) {
        String sql = "INSERT INTO Personnel (CIN, role) VALUES (?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, personnel.getCin());
            pstmt.setString(2, personnel.getRole());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du personnel: " + e.getMessage());
            return false;
        }
    }
    
    // Mettre à jour un personnel existant
    public boolean modifier(Personnel personnel) {
        String sql = "UPDATE Personnel SET role = ? WHERE CIN = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, personnel.getRole());
            pstmt.setInt(2, personnel.getCin());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du personnel: " + e.getMessage());
            return false;
        }
    }
    
    // Supprimer un personnel
    public boolean supprimer(int cin) {
        String sql = "DELETE FROM Personnel WHERE CIN = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cin);
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du personnel: " + e.getMessage());
            return false;
        }
    }
    
    // Trouver un personnel par CIN
    public Personnel trouverParCin(int cin) {
        String sql = "SELECT * FROM Personnel WHERE CIN = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cin);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPersonnel(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du personnel: " + e.getMessage());
        }
        
        return null;
    }
    
    // Lister tous les personnels
    public List<Personnel> listerTous() {
        List<Personnel> personnels = new ArrayList<>();
        String sql = "SELECT * FROM Personnel";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Personnel personnel = mapResultSetToPersonnel(rs);
                personnels.add(personnel);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des personnels: " + e.getMessage());
        }
        
        return personnels;
    }
    
    // Méthode utilitaire pour mapper un ResultSet à un objet Personnel
    private Personnel mapResultSetToPersonnel(ResultSet rs) throws SQLException {
        Personnel personnel = new Personnel();
        personnel.setCin(rs.getInt("CIN"));
        personnel.setRole(rs.getString("role"));
        return personnel;
    }
    
    // Vérifier si un utilisateur est un personnel
    public boolean estPersonnel(int cin) {
        String sql = "SELECT COUNT(*) FROM Personnel WHERE CIN = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cin);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification du personnel: " + e.getMessage());
        }
        
        return false;
    }
    
    // Vérifier si un utilisateur est un admin
    public boolean estAdmin(int cin) {
        String sql = "SELECT role FROM Personnel WHERE CIN = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cin);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return "admin".equals(rs.getString("role"));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification du rôle admin: " + e.getMessage());
        }
        
        return false;
    }
}
