package location.dao;

import location.models.Reservation;
import location.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

/**
 * Classe pour gérer les opérations CRUD sur les réservations
 */
public class ReservationDAO {
    
    /**
     * Ajouter une nouvelle réservation
     * @param reservation La réservation à ajouter
     * @return true si l'ajout a réussi, false sinon
     */
    public boolean ajouter(Reservation reservation) {
        String sql = "INSERT INTO reservation (id_reservation, id_client, immatriculation, date_debut, date_fin, " +
                      "prix_total, statut) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reservation.getIdReservation());
            pstmt.setInt(2, reservation.getIdClient());
            pstmt.setInt(3, reservation.getIdVoiture());
            pstmt.setDate(4, new java.sql.Date(reservation.getDateDebut().getTime()));
            pstmt.setDate(5, new java.sql.Date(reservation.getDateFin().getTime()));
            pstmt.setDouble(6, reservation.getPrixTotal());
            pstmt.setString(7, reservation.getStatut());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la réservation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Trouver une réservation par son ID
     * @param idReservation ID de la réservation à trouver
     * @return La réservation trouvée ou null si aucune réservation n'est trouvée
     */
    public Reservation trouverParId(int idReservation) {
        String sql = "SELECT * FROM reservation WHERE id_reservation = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idReservation);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToReservation(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la réservation: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    /**
     * Mettre à jour le statut d'une réservation
     * @param idReservation ID de la réservation à mettre à jour
     * @param nouveauStatut Nouveau statut de la réservation
     * @return true si la mise à jour a réussi, false sinon
     */
    public boolean mettreAJourStatut(int idReservation, String nouveauStatut) {
        String sql = "UPDATE reservation SET statut = ? WHERE id_reservation = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, nouveauStatut);
            pstmt.setInt(2, idReservation);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du statut de la réservation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Supprimer une réservation
     * @param idReservation ID de la réservation à supprimer
     * @return true si la suppression a réussi, false sinon
     */
    public boolean supprimer(int idReservation) {
        String sql = "DELETE FROM reservation WHERE id_reservation = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idReservation);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la réservation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Lister toutes les réservations d'un client
     * @param idClient ID du client
     * @return Liste des réservations du client
     */
    public List<Reservation> listerReservationsClient(int idClient) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation WHERE id_client = ? ORDER BY date_debut DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idClient);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des réservations du client: " + e.getMessage());
            e.printStackTrace();
        }
        
        return reservations;
    }
    
    /**
     * Vérifier si une voiture est disponible pour une période donnée
     * @param idVoiture ID de la voiture
     * @param dateDebut Date de début de la période
     * @param dateFin Date de fin de la période
     * @return true si la voiture est disponible, false sinon
     */
    public boolean estVoitureDisponible(int idVoiture, Date dateDebut, Date dateFin) {
        String sql = "SELECT COUNT(*) FROM reservation " +
                    "WHERE immatriculation = ? " +
                    "AND statut != 'Annulée' " +
                    "AND ((date_debut BETWEEN ? AND ?) " +
                    "OR (date_fin BETWEEN ? AND ?) " +
                    "OR (date_debut <= ? AND date_fin >= ?))";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, idVoiture);
            pstmt.setDate(2, new java.sql.Date(dateDebut.getTime()));
            pstmt.setDate(3, new java.sql.Date(dateFin.getTime()));
            pstmt.setDate(4, new java.sql.Date(dateDebut.getTime()));
            pstmt.setDate(5, new java.sql.Date(dateFin.getTime()));
            pstmt.setDate(6, new java.sql.Date(dateDebut.getTime()));
            pstmt.setDate(7, new java.sql.Date(dateFin.getTime()));
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) == 0; // Si le compte est 0, alors la voiture est disponible
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de la disponibilite de la voiture: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false; // En cas d'erreur, on considère que la voiture n'est pas disponible
    }
    
    /**
     * Mapper un ResultSet à un objet Reservation
     * @param rs ResultSet à mapper
     * @return Objet Reservation créé à partir du ResultSet
     * @throws SQLException Si une erreur se produit lors de l'accès aux données du ResultSet
     */
    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setIdReservation(rs.getInt("id_reservation"));
        reservation.setIdClient(rs.getInt("id_client"));
        reservation.setIdVoiture(rs.getInt("immatriculation"));
        
        Date dateDebut = rs.getDate("date_debut");
        if (dateDebut != null) {
            reservation.setDateDebut(new Date(dateDebut.getTime()));
        }
        
        Date dateFin = rs.getDate("date_fin");
        if (dateFin != null) {
            reservation.setDateFin(new Date(dateFin.getTime()));
        }
        
        reservation.setPrixTotal(rs.getDouble("prix_total"));
        reservation.setStatut(rs.getString("statut"));
        
        return reservation;
    }
}
