package location.dao;

import location.models.Client;
import location.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class ClientDAO {
    // Méthode pour ajouter un client
    public boolean ajouter(Client client) {
        String sql = "INSERT INTO client (CIN, nom, prenom, telephone, email, adresse, date_naissance, permis_conduire, date_expiration_permis) "
                  + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, client.getCin());
            pstmt.setString(2, client.getNom());
            pstmt.setString(3, client.getPrenom());
            pstmt.setString(4, client.getTelephone());
            pstmt.setString(5, client.getEmail());
            pstmt.setString(6, client.getAdresse());
            
            if (client.getDateNaissance() != null) {
                pstmt.setDate(7, new java.sql.Date(client.getDateNaissance().getTime()));
            } else {
                pstmt.setNull(7, java.sql.Types.DATE);
            }
            
            pstmt.setInt(8, client.getPermisConduire());
            
            if (client.getDateExpirationPermis() != null) {
                pstmt.setDate(9, new java.sql.Date(client.getDateExpirationPermis().getTime()));
            } else {
                pstmt.setNull(9, java.sql.Types.DATE);
            }
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'ajout du client: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Méthode pour modifier un client
    public boolean modifier(Client client) {
        String sql = "UPDATE client SET nom = ?, prenom = ?, telephone = ?, email = ?, adresse = ?, "
                  + "date_naissance = ?, permis_conduire = ?, date_expiration_permis = ? "
                  + "WHERE CIN = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, client.getNom());
            pstmt.setString(2, client.getPrenom());
            pstmt.setString(3, client.getTelephone());
            pstmt.setString(4, client.getEmail());
            pstmt.setString(5, client.getAdresse());
            
            if (client.getDateNaissance() != null) {
                pstmt.setDate(6, new java.sql.Date(client.getDateNaissance().getTime()));
            } else {
                pstmt.setNull(6, java.sql.Types.DATE);
            }
            
            pstmt.setInt(7, client.getPermisConduire());
            
            if (client.getDateExpirationPermis() != null) {
                pstmt.setDate(8, new java.sql.Date(client.getDateExpirationPermis().getTime()));
            } else {
                pstmt.setNull(8, java.sql.Types.DATE);
            }
            
            pstmt.setInt(9, client.getCin());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification du client: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Méthode pour supprimer un client
    public boolean supprimer(int cin) {
        String sql = "DELETE FROM client WHERE CIN = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cin);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression du client: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    
    // Méthode pour trouver un client par son CIN
    public  Client trouverParCIN(int cin) {
        String sql = "SELECT * FROM client WHERE CIN = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cin);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToClient(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du client: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
public static Client trouverParnid(int cin) {
    Client client = null;
    try {
        // Connexion à la base de données
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection conn = DatabaseConnection.getConnection();

        // Requête SQL pour récupérer les informations du client par CIN
        String sql = "SELECT * FROM Client WHERE cin = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, cin);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            client = new Client();
            client.setCin(rs.getInt("cin"));
            client.setAdresse(rs.getString("adresse"));
            // Ajoutez d'autres propriétés du client si nécessaire
        } else {
            System.out.println("Aucun client trouvé pour CIN = " + cin);
        }

        rs.close();
        ps.close();
        conn.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return client;
}



    
    
    
    
    // Méthode pour authentifier un client par email et CIN
    public Client authentifier(String email, int cin) {
        System.out.println("Tentative d'authentification client avec email: " + email + " et CIN: " + cin);
        
        // Vérifier la connexion à la base de données
        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            System.err.println("Erreur: Impossible de se connecter à la base de données.");
            return null;
        }
        
        // Désactiver temporairement la vérification de l'existence de la table pour faciliter la connexion
        // Nous supposons que la table existe
        
        // Essayer diverses formes du nom de la table pour s'adapter aux différentes configurations
        String[] tableNames = {"client", "Client", "CLIENT"};
        ResultSet rs = null;
        boolean success = false;
        
        for (String tableName : tableNames) {
            if (success) break;
            
            String sql = "SELECT * FROM " + tableName + " WHERE email = ? AND CIN = ?";
            
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, email);
                pstmt.setInt(2, cin);
                
                System.out.println("Tentative avec la table '" + tableName + "': " + sql);
                
                try {
                    rs = pstmt.executeQuery();
                    success = true;
                    System.out.println("Requête réussie avec la table '" + tableName + "'");
                } catch (SQLException e) {
                    System.out.println("Échec de la requête avec la table '" + tableName + "': " + e.getMessage());
                }
            } catch (SQLException e) {
                System.err.println("Erreur de préparation de la requête pour '" + tableName + "': " + e.getMessage());
            }
        }
        
        if (!success) {
            System.err.println("Impossible de trouver une table client valide. Vérifiez votre base de données.");
            return null;
        }
        
        try {
            
            if (rs.next()) {
                System.out.println("Authentification réussie pour le client: " + email);
                return mapResultSetToClient(rs);
            } else {
                System.out.println("Aucun client trouvé avec email: " + email + " et CIN: " + cin);
                
                // Pour le débogage, vérifions si le client existe avec cet email
                // Utiliser la même table qui a fonctionné pour la requête précédente
                for (String tableName : tableNames) {
                    try {
                        PreparedStatement checkEmail = conn.prepareStatement("SELECT * FROM " + tableName + " WHERE email = ?");
                        checkEmail.setString(1, email);
                        ResultSet rsCheck = checkEmail.executeQuery();
                        if (rsCheck.next()) {
                            System.out.println("Note: Un client avec cet email existe dans la table '" + tableName + "', mais le CIN est incorrect");
                            break; // On a trouvé le client, pas besoin de chercher dans d'autres tables
                        } else {
                            System.out.println("Note: Aucun client trouvé avec cet email dans la table '" + tableName + "'");
                        }
                        break; // On a pu exécuter la requête, pas besoin d'essayer d'autres tables
                    } catch (SQLException e) {
                        System.out.println("Échec de la vérification d'email dans la table '" + tableName + "': " + e.getMessage());
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification du client: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Méthode pour trouver un client par son email
    public Client trouverParEmail(String email) {
        String sql = "SELECT * FROM client WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToClient(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche du client par email: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
   public List<Client> getAllClients() {
    List<Client> clients = new ArrayList<>();

    String sql = "SELECT u.CIN, u.nom, u.prenom, u.tel, u.email, " +
                 "c.adresse, c.date_naissance, c.permis_conduire, c.date_expiration_permis " +
                 "FROM Utilisateur u " +
                 "JOIN Client c ON u.CIN = c.CIN";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql);
         ResultSet rs = pstmt.executeQuery()) {

        while (rs.next()) {
            Client client = new Client();
            client.setCin(rs.getInt("CIN"));
            client.setNom(rs.getString("nom"));
            client.setPrenom(rs.getString("prenom"));
            client.setTelephone(rs.getString("Tel"));
            client.setEmail(rs.getString("email"));
            client.setAdresse(rs.getString("adresse"));
            client.setDateNaissance(rs.getDate("date_naissance"));  // OK
            client.setPermisConduire(rs.getInt("permis_conduire"));
            client.setDateExpirationPermis(rs.getDate("date_expiration_permis"));  // OK
            clients.add(client);
        }

    } catch (SQLException e) {
        System.err.println("Erreur lors du chargement des clients: " + e.getMessage());
        e.printStackTrace();
    }

    return clients;
}


    
    // Méthode pour lister tous les clients
    public List<Client> listerTous() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client ORDER BY cin";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des clients: " + e.getMessage());
            e.printStackTrace();
        }
        
        return clients;
    }
    
    // Méthode utilitaire pour convertir un ResultSet en objet Client
    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setCin(rs.getInt("CIN"));
        
        client.setAdresse(rs.getString("adresse"));
        
        Date dateNaissance = rs.getDate("date_naissance");
        if (dateNaissance != null) {
            client.setDateNaissance(new java.util.Date(dateNaissance.getTime()));
        }
        
        client.setPermisConduire(rs.getString("permis_conduire"));
        
        Date dateExpiration = rs.getDate("date_expiration_permis");
        if (dateExpiration != null) {
            client.setDateExpirationPermis(new java.util.Date(dateExpiration.getTime()));
        }
        
        return client;
    }
}