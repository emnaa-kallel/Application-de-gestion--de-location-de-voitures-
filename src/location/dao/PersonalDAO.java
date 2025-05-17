package location.dao;

import location.models.Personal;
import location.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonalDAO {
    
    // Ajouter un nouveau personnel
    public boolean ajouter(Personal personal) {
        String sql = "INSERT INTO Personal (CIN, nom, prenom, adresse, tel, email, salaire, date_embauche, date_fin_contrat) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, personal.getCin());
            pstmt.setString(2, personal.getNom());
            pstmt.setString(3, personal.getPrenom());
            pstmt.setString(4, personal.getAdresse());
            pstmt.setString(5, personal.getTel());
            pstmt.setString(6, personal.getEmail());
            pstmt.setDouble(7, personal.getSalaire());
            pstmt.setString(8, personal.getDateEmbauche());
            pstmt.setString(9, personal.getDateFinContrat());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du personnel: " + e.getMessage());
            return false;
        }
    }
    
    // Mettre à jour un personnel existant
    public boolean modifier(Personal personal) {
        String sql = "UPDATE Personal SET nom = ?, prenom = ?, adresse = ?, tel = ?, " +
                    "email = ?, salaire = ?, date_embauche = ?, date_fin_contrat = ? WHERE CIN = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, personal.getNom());
            pstmt.setString(2, personal.getPrenom());
            pstmt.setString(3, personal.getAdresse());
            pstmt.setString(4, personal.getTel());
            pstmt.setString(5, personal.getEmail());
            pstmt.setDouble(6, personal.getSalaire());
            pstmt.setString(7, personal.getDateEmbauche());
            pstmt.setString(8, personal.getDateFinContrat());
            pstmt.setInt(9, personal.getCin());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du personnel: " + e.getMessage());
            return false;
        }
    }
    
    // Supprimer un personnel
    public boolean supprimer(int cin) {
        String sql = "DELETE FROM Personal WHERE CIN = ?";
        
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
    public Personal trouverParCin(int cin) {
        String sql = "SELECT * FROM Personal WHERE CIN = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cin);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToPersonal(rs);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du personnel: " + e.getMessage());
        }
        
        return null;
    }
    
    // Lister tous les personnels
    public List<Personal> listerTous() {
        List<Personal> personnels = new ArrayList<>();
        String sql = "SELECT * FROM Personal";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Personal personal = mapResultSetToPersonal(rs);
                personnels.add(personal);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des personnels: " + e.getMessage());
        }
        
        return personnels;
    }
    
    // Méthode utilitaire pour mapper un ResultSet à un objet Personal
    private Personal mapResultSetToPersonal(ResultSet rs) throws SQLException {
        Personal personal = new Personal();
        personal.setCin(rs.getInt("CIN"));
        personal.setNom(rs.getString("nom"));
        personal.setPrenom(rs.getString("prenom"));
        personal.setAdresse(rs.getString("adresse"));
        personal.setTel(rs.getString("tel"));
        personal.setEmail(rs.getString("email"));
        personal.setSalaire(rs.getDouble("salaire"));
        personal.setDateEmbauche(rs.getString("date_embauche"));
        personal.setDateFinContrat(rs.getString("date_fin_contrat"));
        return personal;
    }
}
