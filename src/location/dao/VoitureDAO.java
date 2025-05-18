package location.dao;

import location.models.Voiture;
import location.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoitureDAO {
    
        // Méthode pour ajouter une voiture
     public boolean ajouterVoiture(Voiture voiture) {
        String sql = "INSERT INTO Voiture (immatriculation, marque, modele, annee, etat, disponibilite, prix_jour, carburant, transmission, date_debut_assurance, date_expiration_assurance, id_assurance) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Récupérer l'ID de voiture maximal actuel et ajouter 1
            //int newId = getNextVoitureId();
            
            
            stmt.setString(1, voiture.getImmatriculation());
            stmt.setString(2, voiture.getMarque());
            stmt.setString(3, voiture.getModele());
            stmt.setInt(4, voiture.getAnnee());
            stmt.setString(5, voiture.getEtat());
            stmt.setString(6, voiture.getDisponibilite());
            stmt.setDouble(7, voiture.getPrixJour());
            stmt.setString(8, voiture.getCarburant());
            stmt.setString(9, voiture.getTransmission());
            stmt.setDate(10, new java.sql.Date(voiture.getDateDebutAssurance().getTime()));
            stmt.setDate(11, new java.sql.Date(voiture.getDateExpirationAssurance().getTime()));
            stmt.setInt(12, voiture.getIdAssurance());

            
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la voiture: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
     
    // Méthode pour ajouter une voiture
    public boolean ajouter(Voiture voiture) {
        String sql = "INSERT INTO Voiture (marque, modele, annee, immatriculation, etat, disponibilite, prix_jour, carburant, transmission) "
               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Récupérer l'ID de voiture maximal actuel et ajouter 1
            int newId = getNextVoitureId();
            
            stmt.setInt(1, newId);
            stmt.setString(2, voiture.getMarque());
            stmt.setString(3, voiture.getModele());
            stmt.setInt(4, voiture.getAnnee());
            stmt.setString(5, voiture.getImmatriculation());
            stmt.setString(6, voiture.getEtat() != null ? voiture.getEtat() : "En marche");
            stmt.setString(7, voiture.isDisponible() ? "Disponible" : "Louée");
            stmt.setDouble(8, voiture.getPrixJour());
            stmt.setString(9, voiture.getCarburant());
            stmt.setString(10, voiture.getTransmission());
            
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout de la voiture: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Obtenir le prochain ID pour une nouvelle voiture
    private int getNextVoitureId() {
        int maxId = 0;
        String sql = "SELECT MAX(immatriculation) FROM Voiture";
        
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

    // Méthode pour modifier une voiture
    public boolean modifier(Voiture voiture) {
         String sql = "UPDATE Voiture SET marque = ?, modele = ?, annee = ?, etat = ?, prix_jour = ?, carburant = ?, transmission = ?, date_debut_assurance = ?, date_expiration_assurance = ?, id_assurance = ? WHERE immatriculation = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, voiture.getMarque());
            stmt.setString(2, voiture.getModele());
            stmt.setInt(3, voiture.getAnnee());
            stmt.setString(4, voiture.getEtat());
            stmt.setDouble(5, voiture.getPrixJour());
            stmt.setString(6, voiture.getCarburant());
            stmt.setString(7, voiture.getTransmission());
            stmt.setDate(8, new java.sql.Date(voiture.getDateDebutAssurance().getTime()));
            stmt.setDate(9, new java.sql.Date(voiture.getDateExpirationAssurance().getTime()));
            stmt.setInt(10, voiture.getIdAssurance());
            stmt.setString(11, voiture.getImmatriculation());
            
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de la voiture: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    // Méthode pour supprimer une voiture
    public boolean supprimer(String immatriculation) {
        String sql = "DELETE FROM Voiture WHERE immatriculation = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, immatriculation);
            
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la voiture: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Méthode pour supprimer une voiture par son ID
    public boolean supprimerParId(int idVoiture) {
        String sql = "DELETE FROM Voiture WHERE immatriculation = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, idVoiture);
            
            int result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de la voiture: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Méthode pour trouver une voiture par son immatriculation
    public Voiture trouverParImmatriculation(String immatriculation) {
        String sql = "SELECT * FROM Voiture WHERE immatriculation = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, immatriculation);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToVoiture(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la voiture: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    
    public List<Integer> getassurance() {
     List<Integer> assurances = new ArrayList<>();
    String query = "SELECT DISTINCT id_assurance FROM VOITURE";

    try (Connection conn = DatabaseConnection.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            assurances.add(rs.getInt("id_assurance")); // Corrigé ici
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return assurances;
}
    
    public List<Voiture> trouverParMarqueOuModele(String recherche) {
    List<Voiture> voitures = new ArrayList<>();
    String sql = "SELECT * FROM Voiture WHERE LOWER(marque) LIKE ? OR LOWER(modele) LIKE ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        String critere = "%" + recherche.toLowerCase() + "%";
        stmt.setString(1, critere);
        stmt.setString(2, critere);
        
        ResultSet rs = stmt.executeQuery();
        
        while (rs.next()) {
            Voiture voiture = mapResultSetToVoiture(rs);
            voitures.add(voiture);
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de la recherche par marque ou modèle: " + e.getMessage());
        e.printStackTrace();
    }
    
    return voitures;
}


    // Méthode pour lister toutes les voitures
    public List<Voiture> listerTout() {
        List<Voiture> voitures = new ArrayList<>();
        String sql = "SELECT * FROM Voiture ORDER BY immatriculation";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                voitures.add(mapResultSetToVoiture(rs));
            }
        }catch (Exception e) {
            System.err.println("Erreur lors de l'authentification de l'utilisateur: " + e.getMessage());
            e.printStackTrace();
        }
        
        return voitures;
    }
    
    // Méthode pour trouver une voiture par son ID
    public Voiture trouverParId(String idVoiture) {
        String sql = "SELECT * FROM Voiture WHERE immatriculation = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, idVoiture);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToVoiture(rs);
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de la voiture: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Méthode pour lister uniquement les voitures disponibles
    public List<Voiture> listerDisponibles() {
        List<Voiture> voitures = new ArrayList<>();
        String sql = "SELECT * FROM Voiture WHERE disponibilite = 'disponible' ORDER BY immatriculation";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                voitures.add(mapResultSetToVoiture(rs));
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des voitures disponibles: " + e.getMessage());
            e.printStackTrace();
        }
        
        return voitures;
    }
    
    // Méthode utilitaire pour mapper un ResultSet vers un objet Voiture
    private Voiture mapResultSetToVoiture(ResultSet rs) throws SQLException {
        Voiture voiture = new Voiture();
        voiture.setMarque(rs.getString("marque"));
        voiture.setModele(rs.getString("modele"));
        voiture.setAnnee(rs.getInt("annee"));
        voiture.setImmatriculation(rs.getString("immatriculation"));
        voiture.setEtat(rs.getString("etat"));
        voiture.setDisponibilite(rs.getString("disponibilite"));
        voiture.setPrixJour(rs.getDouble("prix_jour"));
        voiture.setcarburant(rs.getString("carburant"));
        voiture.setTransmission(rs.getString("transmission"));
        voiture.setDateDebutAssurance(rs.getDate("date_debut_assurance"));
        voiture.setDateExpirationAssurance(rs.getDate("date_expiration_assurance"));

        return voiture;
    }
    
    
          public List<String> getAnnees() {
        List<String> annees = new ArrayList<>();
        String query = "SELECT DISTINCT ANNEE FROM sys.VOITURE ORDER BY ANNEE DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                annees.add(rs.getString("ANNEE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return annees;
    }
          
          public List<Double> getPrixJourList() {
    List<Double> prixList = new ArrayList<>();
    String sql = "SELECT DISTINCT PRIX_JOUR FROM VOITURE ORDER BY PRIX_JOUR";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            prixList.add(rs.getDouble("PRIX_JOUR"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return prixList;
}

          public List<Voiture> rechercherVoituresDAO(String marque, String annee,
                                        String carburant, String transmission,
                                        int prixMin, int prixMax) {
    List<Voiture> liste = new ArrayList<>();

    StringBuilder query = new StringBuilder("SELECT * FROM VOITURE WHERE PRIX_JOUR BETWEEN ? AND ?");
    if (marque != null) query.append(" AND MARQUE = ?");
    if (annee != null) query.append(" AND ANNEE = ?");
    if (carburant != null) query.append(" AND CARBURANT = ?");
    if (transmission != null) query.append(" AND TRANSMISSION = ?");

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query.toString())) {

        int index = 1;
        stmt.setDouble(index++, prixMin);
        stmt.setDouble(index++, prixMax);
        if (marque != null) stmt.setString(index++, marque);
        if (annee != null) stmt.setInt(index++, Integer.parseInt(annee));
        if (carburant != null) stmt.setString(index++, carburant);
        if (transmission != null) stmt.setString(index++, transmission);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Voiture v = new Voiture(
                rs.getString("IMMATRICULATION"),
                rs.getString("MARQUE"),
                rs.getString("MODELE"),
                rs.getInt("ANNEE"),
                rs.getString("ETAT"),
                rs.getString("DISPONIBILITE"),
                rs.getDouble("PRIX_JOUR"),
                rs.getString("CARBURANT"),
                rs.getString("TRANSMISSION"),
                rs.getDate("DATE_DEBUT_ASSURANCE"),
                rs.getDate("DATE_EXPIRATION_ASSURANCE"),
                rs.getInt("ID_ASSURANCE")
            );
            liste.add(v);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return liste;
}
    public List<Voiture> getVoituresParDisponibilite(String disponibilite) {
    List<Voiture> voitures = new ArrayList<>();
    String sql = "SELECT * FROM VOITURE WHERE DISPONIBILITE = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, disponibilite);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            voitures.add(mapResultSetToVoiture(rs));
        }

    } catch (SQLException e) {
        System.err.println("Erreur lors de la récupération des voitures par disponibilité : " + e.getMessage());
    }

    return voitures;
}
public List<Voiture> getVoituresParEtat(String etat) {
    List<Voiture> voitures = new ArrayList<>();
    String sql = "SELECT * FROM VOITURE WHERE ETAT = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, etat);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            voitures.add(mapResultSetToVoiture(rs));
        }

    } catch (SQLException e) {
        System.err.println("Erreur lors de la récupération des voitures par état : " + e.getMessage());
    }

    return voitures;
}
     
                public List<String> getMarques() {
        List<String> marques = new ArrayList<>();
        String query = "SELECT DISTINCT MARQUE FROM VOITURE";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                marques.add(rs.getString("MARQUE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return marques;
    }
                
    public List<Voiture> trouverParPrixMax(double prixMax) {
    List<Voiture> voitures = new ArrayList<>();
    String sql = "SELECT * FROM Voiture WHERE prix_jour <= ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setDouble(1, prixMax);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            voitures.add(mapResultSetToVoiture(rs));
        }

    } catch (SQLException e) {
        System.err.println("Erreur lors de la recherche par prix max: " + e.getMessage());
        e.printStackTrace();
    }

    return voitures;
}

}
