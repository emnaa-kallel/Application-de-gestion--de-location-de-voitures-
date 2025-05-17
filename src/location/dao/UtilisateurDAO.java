package location.dao;

import location.models.Utilisateur;
import location.utils.DatabaseConnection;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class UtilisateurDAO {

    // Méthode pour ajouter un utilisateur
    public boolean ajouter(Utilisateur utilisateur) {
    String sql = "INSERT INTO Utilisateur (CIN, login, mot_de_passe, nom, prenom, tel, email, type_utilisateur) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.setAutoCommit(false); // pour gérer la transaction manuellement
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, utilisateur.getCin());
            pstmt.setString(2, utilisateur.getLogin());
            pstmt.setString(3, utilisateur.getMotDePasse());
            pstmt.setString(4, utilisateur.getNom());
            pstmt.setString(5, utilisateur.getPrenom());
            pstmt.setInt(6, utilisateur.getTel());
            pstmt.setString(7, utilisateur.getEmail());
            pstmt.setString(8, utilisateur.getTypeUtilisateur());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0 && utilisateur.getTypeUtilisateur().equals("personnel")) {
                String sqlPersonnel = "INSERT INTO Personnel (CIN, role) VALUES (?, ?)";
                try (PreparedStatement pst2 = conn.prepareStatement(sqlPersonnel)) {
                    pst2.setInt(1, utilisateur.getCin());
                    pst2.setString(2, "employe");
                    pst2.executeUpdate();
                }
            }
            
            conn.commit();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            conn.rollback();  // rollback en cas d'erreur
            throw e;
        }
        
    } catch (SQLException e) {
        System.err.println("Erreur lors de l'ajout de l'utilisateur: " + e.getMessage());
        e.printStackTrace();
        return false;
    }

    }

    // Méthode pour lister tous les utilisateurs
    public List<Utilisateur> listerTous() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM Utilisateur ORDER BY nom, prenom";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                utilisateurs.add(mapResultSetToUtilisateur(rs));
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs: " + e.getMessage());
            e.printStackTrace();
        }
        
        return utilisateurs;
    }

    public Utilisateur trouverParLogin(String login) {
        String sql = "SELECT * FROM Utilisateur WHERE login = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUtilisateur(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'utilisateur par email: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Méthode pour authentifier un utilisateur
  
    public Utilisateur authentifier(String login, String motDePasse) {
        System.out.println("Tentative d'authentification utilisateur avec login: " + login);
        
        Utilisateur utilisateur = null;
        String sql = "SELECT * FROM Utilisateur WHERE login = ? AND mot_de_passe = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, motDePasse);
            System.out.println("Exécution de la requête SQL...");
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Utilisateur trouvé, création de l'objet...");
                    utilisateur = mapResultSetToUtilisateur(rs);
                } else {
                    System.out.println("Note: Aucun utilisateur trouvé avec ce login et ce mot de passe");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification de l'utilisateur: " + e.getMessage());
            e.printStackTrace();
        }
        return utilisateur;
    }
    public Utilisateur authentifier2(String login, int CIN) {
    System.out.println("Tentative d'authentification utilisateur avec login: " + login + " et CIN: " + CIN);
    
    Utilisateur utilisateur = null;
    // Mise à jour de la requête pour utiliser à la fois le login et le CIN
    String sql = "SELECT * FROM Utilisateur WHERE login = ? AND CIN = ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        // Paramétrage des valeurs pour le login et le CIN
        pstmt.setString(1, login);
        pstmt.setInt(2, CIN);  // CIN est de type INT, donc on utilise setInt
        
        System.out.println("Exécution de la requête SQL...");
        
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                System.out.println("Utilisateur trouvé, création de l'objet...");
                utilisateur = mapResultSetToUtilisateur(rs);
            } else {
                System.out.println("Note: Aucun utilisateur trouvé avec ce login et ce CIN");
            }
        }
    } catch (SQLException e) {
        System.err.println("Erreur lors de l'authentification de l'utilisateur: " + e.getMessage());
        e.printStackTrace();
    }
    
    return utilisateur;
}

    
    // Méthode pour créer un nouvel utilisateur (inscription)
    public boolean inscrire(Utilisateur utilisateur) {
        // Vérifier d'abord si le login existe déjà
        if (trouverParLogin(utilisateur.getLogin()) != null) {
            return false; // L'utilisateur existe déjà
        }
        
        // Ajouter l'utilisateur
        return ajouter(utilisateur);
    }
    
    // Méthode pour mettre à jour les informations d'un utilisateur
    public boolean mettreAJour(Utilisateur utilisateur) {
        String sql = "UPDATE Utilisateur SET login = ?, mot_de_passe = ?, nom = ?, prenom = ?, "
                + "tel = ?, email = ?, type_utilisateur = ? WHERE CIN = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, utilisateur.getLogin());
            pstmt.setString(2, utilisateur.getMotDePasse());
            pstmt.setString(3, utilisateur.getNom());
            pstmt.setString(4, utilisateur.getPrenom());
            pstmt.setInt(5, utilisateur.getTel());
            pstmt.setString(6, utilisateur.getEmail());
            pstmt.setString(7, utilisateur.getTypeUtilisateur());
            pstmt.setInt(8, utilisateur.getCin());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'utilisateur: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // Trouver un utilisateur par son CIN
    public Utilisateur trouverParCIN(int cin) {
        String sql = "SELECT * FROM Utilisateur WHERE CIN = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cin);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUtilisateur(rs);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'utilisateur par CIN: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    // Méthode utilitaire pour convertir un ResultSet en objet Utilisateur
  

    
    
        
public boolean modifcompte(Utilisateur utilisateur) {
    String sql = "UPDATE Utilisateur SET mot_de_passe = ?, nom = ?, prenom = ?, tel = ?, email = ? WHERE CIN = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setString(1, utilisateur.getMotDePasse());
        pstmt.setString(2, utilisateur.getNom());
        pstmt.setString(3, utilisateur.getPrenom());
        pstmt.setInt(4, utilisateur.getTel());
        pstmt.setString(5, utilisateur.getEmail());
        pstmt.setInt(6, utilisateur.getCin());

        return pstmt.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}


private Utilisateur mapResultSetToUtilisateur(ResultSet rs) throws SQLException {
    Utilisateur user = new Utilisateur();
    user.setCin(rs.getInt("CIN"));
    user.setLogin(rs.getString("login"));
    user.setMotDePasse(rs.getString("mot_de_passe"));
    user.setNom(rs.getString("nom"));
    user.setPrenom(rs.getString("prenom"));
    user.setTel(rs.getInt("tel"));
    user.setEmail(rs.getString("email"));
    user.setTypeUtilisateur(rs.getString("type_utilisateur"));
    return user;
}
}