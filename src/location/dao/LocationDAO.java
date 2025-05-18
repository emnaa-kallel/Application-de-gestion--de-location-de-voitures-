package location.dao;

import location.models.Location;
import location.models.Client;
import location.models.Voiture;
import location.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO {

    // Méthode pour ajouter une location selon le schéma Oracle
    public boolean ajouter(Location location) {
        String sql = "INSERT INTO location (id_location, CIN_client, immatriculation, "
                + "date_debut, date_fin, date_retour, mode_paiement, statut) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
int newId = getNextLocationId();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Récupérer le prochain ID disponible
            

            pstmt.setInt(1, newId);
            pstmt.setInt(2, location.getClientCin());
            pstmt.setString(3, location.getVoitureId());
            pstmt.setDate(4, new java.sql.Date(location.getDateDebut().getTime()));
            pstmt.setDate(5, location.getDateFin() != null ? new java.sql.Date(location.getDateFin().getTime()) : null);
            pstmt.setDate(6, location.getDateRetour() != null ? new java.sql.Date(location.getDateRetour().getTime()) : null);
            pstmt.setString(7, location.getModePaiement());
            pstmt.setString(8, location.getStatut());

            int affectedRows = pstmt.executeUpdate();
            
            // Si l'opération a réussi, mettre à jour l'ID de l'objet location
            if (affectedRows > 0) {
                location.setId(newId);
                // Mettre à jour la disponibilite de la voiture
                updateCarAvailability(location.getVoitureId(), false);
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la location: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Obtenir le prochain ID disponible pour une location
    private int getNextLocationId() {
        int maxId = 0;
        String sql = "SELECT MAX(id_location) FROM location";
        
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
    
    // Mettre à jour la disponibilite d'une voiture
    private void updateCarAvailability(String carId, boolean available) {
        String sql = "UPDATE Voiture SET disponibilite = ? WHERE immatriculation = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, available ? "disponible" : "louee");
            pstmt.setString(2, carId);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la disponibilite de la voiture: " + e.getMessage());
        }
    }

    // Méthode pour lister toutes les locations
    public List<Location> listerTous() {
        List<Location> locations = new ArrayList<>();
        String sql = "SELECT l.*, c.nom AS client_nom, c.prenom AS client_prenom, "
                + "v.marque, v.modele, v.immatriculation, v.prix_jour "
                + "FROM location l "
                + "LEFT JOIN utilisateur c ON l.CIN_client = c.CIN "
                + "LEFT JOIN Voiture v ON l.immatriculation = v.immatriculation "
                + "ORDER BY l.date_debut DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Location location = mapResultSetToLocation(rs);
                
                // Ajouter des informations complémentaires sur le client et la voiture
                if (rs.getString("client_nom") != null) {
                    Client client = new Client();
                    client.setCin(rs.getInt("CIN_client"));
                    client.setNom(rs.getString("client_nom"));
                    client.setPrenom(rs.getString("client_prenom"));
                    location.setClient(client);
                }
                
                if (rs.getString("marque") != null) {
                    Voiture voiture = new Voiture();
                    voiture.setPrixJour(rs.getDouble("prix_jour"));
                    voiture.setMarque(rs.getString("marque"));
                    voiture.setModele(rs.getString("modele"));
                    voiture.setImmatriculation(rs.getString("immatriculation"));
                    location.setVoiture(voiture);
                }
                
                locations.add(location);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des locations: " + e.getMessage());
            e.printStackTrace();
        }

        return locations;
    }
    
    // Trouver une location par son ID
    public Location trouverParId(int id) {
        String sql = "SELECT l.*, c.nom AS client_nom, c.prenom AS client_prenom, "
                + "v.marque, v.modele, v.immatriculation "
                + "FROM location l "
                + "LEFT JOIN utilisateur c ON l.CIN_client = c.CIN "
                + "LEFT JOIN Voiture v ON l.immatriculation = v.immatriculation "
                + "WHERE l.id_location = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Location location = mapResultSetToLocation(rs);
                
                // Ajouter des informations complémentaires sur le client et la voiture
                if (rs.getString("client_nom") != null) {
                    Client client = new Client();
                    client.setCin(rs.getInt("CIN_client"));
                    client.setNom(rs.getString("client_nom"));
                    client.setPrenom(rs.getString("client_prenom"));
                    location.setClient(client);
                }
                
                if (rs.getString("marque") != null) {
                    Voiture voiture = new Voiture();
                    voiture.setImmatriculation(rs.getString("immatriculation"));
                    voiture.setMarque(rs.getString("marque"));
                    voiture.setModele(rs.getString("modele"));
                    voiture.setImmatriculation(rs.getString("immatriculation"));
                    location.setVoiture(voiture);
                }
                
                return location;
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la location: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }
    
    
    public List<Location> getAllLocations() {
        List<Location> locations = new ArrayList<>();
        String sql = "SELECT * FROM LOCATION";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                locations.add(mapResultSetToLocation(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des locations : " + e.getMessage());
        }

        return locations;
}
   
private Location mapResultSetToLocation(ResultSet rs) throws SQLException {
    int id = rs.getInt("id_location");
    int cin = rs.getInt("cin_client");
    String immat = rs.getString("immatriculation");

        
        Date DateDebut = rs.getDate("date_debut");

        Date dateFin = rs.getDate("date_fin");
        Date dateretour = rs.getDate("date_retour");

        String paiement = rs.getString("mode_paiement");
        String statut = rs.getString("statut");

        return new Location(id, cin, immat, DateDebut, dateFin, dateretour, paiement, statut);
    }
    // Lister les locations d'un client
    public List<Location> listerParClient(int clientCin) {
        List<Location> locations = new ArrayList<>();
        String sql = "SELECT l.*, c.nom AS client_nom, c.prenom AS client_prenom, "
                + "v.marque, v.modele, v.immatriculation, v.prix_jour, v.carburant "
                + "FROM location l "
                + "LEFT JOIN utilisateur c ON l.CIN_client = c.CIN "
                + "LEFT JOIN Voiture v ON l.immatriculation = v.immatriculation "
                + "WHERE l.CIN_client = ? "
                + "ORDER BY l.date_debut DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, clientCin);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Location location = mapResultSetToLocation(rs);
                
                // Ajouter des informations complémentaires sur le client et la voiture
                if (rs.getString("client_nom") != null) {
                    Client client = new Client();
                    client.setCin(rs.getInt("CIN_client"));
                    client.setNom(rs.getString("client_nom"));
                    client.setPrenom(rs.getString("client_prenom"));
                    location.setClient(client);
                }
                
                if (rs.getString("marque") != null) {
                    Voiture voiture = new Voiture();
                    voiture.setPrixJour(rs.getDouble("prix_jour"));
                    voiture.setMarque(rs.getString("marque"));
                    voiture.setModele(rs.getString("modele"));
                    voiture.setImmatriculation(rs.getString("immatriculation"));
                    voiture.setCarburant(rs.getString("carburant"));
                    location.setVoiture(voiture);
                }
                
                locations.add(location);
            }

        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des locations du client: " + e.getMessage());
            e.printStackTrace();
        }

        return locations;
    }
    
    // Mettre à jour une location
    public boolean mettreAJour(Location location) {
        String sql = "UPDATE location SET date_debut = ?, date_fin = ?, date_retour = ?, "
                + "mode_paiement = ?, statut = ? WHERE id_location = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, new java.sql.Date(location.getDateDebut().getTime()));
            pstmt.setDate(2, location.getDateFin() != null ? new java.sql.Date(location.getDateFin().getTime()) : null);
            pstmt.setDate(3, location.getDateRetour() != null ? new java.sql.Date(location.getDateRetour().getTime()) : null);
            pstmt.setString(4, location.getModePaiement());
            pstmt.setString(5, location.getStatut());
            pstmt.setInt(6, location.getId());

            int affectedRows = pstmt.executeUpdate();
            
            // Si on termine la location (statut = 'Terminée'), on libère la voiture
            if (affectedRows > 0 && "Terminée".equals(location.getStatut())) {
                updateCarAvailability(location.getVoitureId(), true);
            }
            
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de la location: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public List<Location> getByCin(String cin) {
    List<Location> locations = new ArrayList<>();
    String sql = "SELECT id_location, immatriculation, date_debut, date_fin, date_retour, mode_paiement, statut FROM Location WHERE cin_client = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, cin);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Location loc = new Location();
            loc.setId(rs.getInt("id_location"));
            loc.setVoitureId(rs.getString("immatriculation"));
            loc.setDateDebut(rs.getDate("date_debut"));
            loc.setDateFin(rs.getDate("date_fin"));
            loc.setDateRetour(rs.getDate("date_retour"));
            loc.setModePaiement(rs.getString("mode_paiement"));
            loc.setStatut(rs.getString("statut"));

            locations.add(loc);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return locations;
}

    // Supprimer une location
    public boolean supprimer(int idLocation) {
        String sql = "DELETE FROM location WHERE id_location = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idLocation);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la location: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    
    // Méthode utilitaire pour mapper un ResultSet à un objet Location
   /*private Location mapResultSetToLocation(ResultSet rs) throws SQLException {
    int id = rs.getInt("id_location");
    int cin = rs.getInt("cin_client");
    String immat = rs.getString("immatriculation");

    Date debut = rs.getDate("date_debut");
    Date fin = rs.getDate("date_fin");
    Date retour = rs.getDate("date_retour");

    String paiement = rs.getString("mode_paiement");
    String statut = rs.getString("statut");

    return new Location(id, cin, immat, debut, fin, retour, paiement, statut);
}*/
}