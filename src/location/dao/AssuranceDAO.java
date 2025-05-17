package location.dao;

import location.models.Assurance;
import location.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AssuranceDAO {

    // Méthode pour ajouter une assurance
    public boolean ajouter(Assurance assurance) {
        String sql = "INSERT INTO assurances (id_location, compagnie, numero_police, date_debut, date_fin) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, assurance.getLocation().getId());  // Lier l'assurance à la location
            pstmt.setString(2, assurance.getCompagnie());
            pstmt.setString(3, assurance.getNumeroPolice());
            pstmt.setDate(4, new java.sql.Date(assurance.getDateDebut().getTime()));
            pstmt.setDate(5, new java.sql.Date(assurance.getDateFin().getTime()));
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de l'assurance: " + e.getMessage());
            return false;
        }
    }

    // Méthode pour lister toutes les assurances
    public List<Assurance> listerToutes() {
        List<Assurance> assurances = new ArrayList<>();
        String sql = "SELECT * FROM assurances";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Assurance assurance = new Assurance();
                assurance.setId(rs.getInt("id"));
                assurance.setCompagnie(rs.getString("compagnie"));
                assurance.setNumeroPolice(rs.getString("numero_police"));
                assurance.setDateDebut(rs.getDate("date_debut"));
                assurance.setDateFin(rs.getDate("date_fin"));
                
                // Lier l'assurance à une location (vous devrez probablement récupérer l'objet Location en fonction de l'ID)
                assurance.setLocation(new LocationDAO().trouverParId(rs.getInt("id_location")));
                
                assurances.add(assurance);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des assurances: " + e.getMessage());
        }
        
        return assurances;
    }
    
      // Obtenir toutes les assurances
public List<Assurance> getAllAssurances() {
    List<Assurance> liste = new ArrayList<>();
    String sql = "SELECT * FROM SYS.Assurance";

    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement stmt = con.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Assurance a = new Assurance(
                    rs.getInt("id_assurance"),
                    rs.getString("compagnie")
            );
            liste.add(a);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return liste;
}

}
