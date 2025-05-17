package location.utils;

import location.models.Voiture;
import location.models.Utilisateur;
import location.models.Location;
import location.models.Client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe utilitaire qui fournit des données de démonstration
 * en l'absence de connexion à la base de données
 */
public class DemoDataProvider {
    
    private static final Map<String, List<?>> dataStore = new HashMap<>();
    
    static {
        initializeData();
    }
    
    private static void initializeData() {
        // Initialiser les voitures de démonstration
        List<Voiture> voitures = new ArrayList<>();
        
        Voiture v1 = new Voiture();
        v1.setImmatriculation("1");
        v1.setMarque("BMW");
        v1.setModele("X5");
        v1.setAnnee(2023);
        v1.setCouleur("Noir");
        v1.setTransmission("Automatique");
        v1.setPrixJournalier(800);
        v1.setDisponible(true);
        v1.setPhotoPath("/images/bmw_x5.jpg");
        voitures.add(v1);
        
        Voiture v2 = new Voiture();
        v2.setImmatriculation("2");
        v2.setMarque("Mercedes");
        v2.setModele("Classe C");
        v2.setAnnee(2022);
        v2.setCouleur("Gris");
        v2.setTransmission("Automatique");
        v2.setPrixJournalier(600);
        v2.setDisponible(true);
        v2.setPhotoPath("/images/mercedes_c.jpg");
        voitures.add(v2);
        
        Voiture v3 = new Voiture();
        v3.setImmatriculation("3");
        v3.setMarque("Audi");
        v3.setModele("A4");
        v3.setAnnee(2021);
        v3.setCouleur("Blanc");
        v3.setTransmission("Automatique");
        v3.setPrixJournalier(550);
        v3.setDisponible(true);
        v3.setPhotoPath("/images/audi_a4.jpg");
        voitures.add(v3);
        
        Voiture v4 = new Voiture();
        v4.setImmatriculation("2");
        v4.setMarque("Renault");
        v4.setModele("Megane");
        v4.setAnnee(2020);
        v4.setCouleur("Rouge");
        v4.setTransmission("Manuelle");
        v4.setPrixJournalier(300);
        v4.setDisponible(false);
        v4.setPhotoPath("/images/renault_megane.jpg");
        voitures.add(v4);
        
        Voiture v5 = new Voiture();
        v5.setImmatriculation("5");
        v5.setMarque("Peugeot");
        v5.setModele("3008");
        v5.setAnnee(2022);
        v5.setCouleur("Bleu");
        v5.setTransmission("Automatique");
        v5.setPrixJournalier(450);
        v5.setDisponible(true);
        v5.setPhotoPath("/images/peugeot_3008.jpg");
        voitures.add(v5);
        
        // Initialiser les utilisateurs de démonstration
        List<Utilisateur> utilisateurs = new ArrayList<>();
        
        Utilisateur u1 = new Utilisateur();
        u1.setId(1);
        u1.setEmail("client@example.com");
        u1.setPassword("client123");
        u1.setNom("Nom Client");
        u1.setPrenom("Prénom Client");
        u1.setCin(1000);
        u1.setTypeUtilisateur("CLIENT");
        u1.setTel(060000000);
        utilisateurs.add(u1);
        
        Utilisateur u2 = new Utilisateur();
        u2.setId(2);
        u2.setEmail("employe@example.com");
        u2.setPassword("employe123");
        u2.setNom("Nom Employé");
        u2.setPrenom("Prénom Employé");
        u2.setCin(2000);
        u2.setTypeUtilisateur("employe");
        u2.setTel(600000002);
        utilisateurs.add(u2);
        
        Utilisateur u3 = new Utilisateur();
        u3.setId(3);
        u3.setEmail("admin@example.com");
        u3.setPassword("admin123");
        u3.setNom("Nom Admin");
        u3.setPrenom("Prénom Admin");
        u3.setCin(3000);
        u3.setTypeUtilisateur("client");
        u3.setTel(60000003);
        utilisateurs.add(u3);
        
        // Initialiser les clients de démonstration
        List<Client> clients = new ArrayList<>();
        
        Client c1 = new Client();
        c1.setCin(1000);
        c1.setNom("Dupont");
        c1.setPrenom("Jean");
        c1.setTelephone("0600000011");
        c1.setEmail("jean.dupont@example.com");
        c1.setAdresse("123 Rue de Paris, 75001 Paris");
        Calendar cal = Calendar.getInstance();
        cal.set(1980, Calendar.FEBRUARY, 15);
        c1.setDateNaissance(cal.getTime()); // 15/02/1980
        c1.setPermisConduire("P12345678");
        cal.set(2030, Calendar.FEBRUARY, 15);
        c1.setDateExpirationPermis(cal.getTime()); // 15/02/2030
        clients.add(c1);
        
        Client c2 = new Client();
        c2.setCin(2000);
        c2.setNom("Martin");
        c2.setPrenom("Sophie");
        c2.setTelephone("0600000022");
        c2.setEmail("sophie.martin@example.com");
        c2.setAdresse("45 Avenue Victor Hugo, 69002 Lyon");
        cal = Calendar.getInstance();
        cal.set(1985, Calendar.MAY, 22);
        c2.setDateNaissance(cal.getTime()); // 22/05/1985
        c2.setPermisConduire("P87654321");
        cal.set(2035, Calendar.MAY, 22);
        c2.setDateExpirationPermis(cal.getTime()); // 22/05/2035
        clients.add(c2);
        
        Client c3 = new Client();
        c3.setCin(3000);
        c3.setNom("Dubois");
        c3.setPrenom("Pierre");
        c3.setTelephone("0600000033");
        c3.setEmail("pierre.dubois@example.com");
        c3.setAdresse("78 Boulevard Saint-Michel, 44000 Nantes");
        cal = Calendar.getInstance();
        cal.set(1978, Calendar.AUGUST, 10);
        c3.setDateNaissance(cal.getTime()); // 10/08/1978
        c3.setPermisConduire("P45678912");
        cal.set(2028, Calendar.AUGUST, 10);
        c3.setDateExpirationPermis(cal.getTime()); // 10/08/2028
        clients.add(c3);
        
        // Initialiser les locations de démonstration
        List<Location> locations = new ArrayList<>();
        
        Location l1 = new Location();
        l1.setId(1);
        l1.setClient(c1);
        l1.setVoiture(v4);
        l1.setDateDebut(new Date());
        l1.setDateFin(new Date(System.currentTimeMillis() + 7*24*60*60*1000)); // 7 jours plus tard
       // l1.setPrixTotal(v4.getPrixJournalier() * 7);
        l1.setStatut("En cours");
        locations.add(l1);
        
        // Stocker les données dans le dataStore
        dataStore.put("voitures", voitures);
        dataStore.put("utilisateurs", utilisateurs);
        dataStore.put("clients", clients);
        dataStore.put("locations", locations);
    }
    
    /**
     * Récupère toutes les voitures
     * @return Liste de toutes les voitures
     */
    @SuppressWarnings("unchecked")
    public static List<Voiture> getAllVoitures() {
        return (List<Voiture>) dataStore.getOrDefault("voitures", new ArrayList<>());
    }
    
    /**
     * Récupère toutes les voitures disponibles
     * @return Liste des voitures disponibles
     */
    @SuppressWarnings("unchecked")
    public static List<Voiture> getAvailableVoitures() {
        List<Voiture> availableVoitures = new ArrayList<>();
        List<Voiture> allVoitures = (List<Voiture>) dataStore.getOrDefault("voitures", new ArrayList<>());
        
        for (Voiture voiture : allVoitures) {
            if (voiture.isDisponible()) {
                availableVoitures.add(voiture);
            }
        }
        
        return availableVoitures;
    }
    
    /**
     * Récupère une voiture par son ID
     * @param id ID de la voiture
     * @return La voiture correspondante ou null
     */
    @SuppressWarnings("unchecked")
    public static Voiture getVoitureById(String id) {
        List<Voiture> voitures = (List<Voiture>) dataStore.getOrDefault("voitures", new ArrayList<>());
        
        for (Voiture voiture : voitures) {
            if (voiture.getId()== id) {
                return voiture;
            }
        }
        
        return null;
    }
    
    /**
     * Authentifie un utilisateur
     * @param email Email de l'utilisateur
     * @param password Mot de passe de l'utilisateur
     * @return L'utilisateur authentifié ou null
     */
    @SuppressWarnings("unchecked")
    public static Utilisateur authentifierUtilisateur(String email, String password) {
        // Exemples d'utilisateurs pour la démonstration
        if (email.equals("client@example.com") && password.equals("client123")) {
            Utilisateur u = new Utilisateur();
            u.setId(1);
            u.setEmail("client@example.com");
            u.setPassword("client123");
            u.setNom("Nom Client");
            u.setPrenom("Prénom Client");
            u.setCin(1000);
            u.setTypeUtilisateur("LIENT");
            u.setTel(600000001);
            return u;
        } 
        else if (email.equals("employe@example.com") && password.equals("employe123")) {
            Utilisateur u = new Utilisateur();
            u.setId(2);
            u.setEmail("employe@example.com");
            u.setPassword("employe123");
            u.setNom("Nom Employé");
            u.setPrenom("Prénom Employé");
            u.setCin(2000);
            u.setTypeUtilisateur("employe");
            u.setTel(600000002);
            return u;
        }
        else if (email.equals("admin@example.com") && password.equals("admin123")) {
            Utilisateur u = new Utilisateur();
            u.setId(3);
            u.setEmail("admin@example.com");
            u.setPassword("admin123");
            u.setNom("Nom Admin");
            u.setPrenom("Prénom Admin");
            u.setCin(3000);
            u.setTypeUtilisateur("admin");
            u.setTel(600000003);
            return u;
        }

        // Authentification avec les données stockées (pour référence)
        List<Utilisateur> utilisateurs = (List<Utilisateur>) dataStore.getOrDefault("utilisateurs", new ArrayList<>());
        
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getEmail().equals(email) && utilisateur.getPassword().equals(password)) {
                return utilisateur;
            }
        }
        
        return null;
    }
    
    /**
     * Renvoie des exemples d'identifiants pour la démonstration
     * @return Un tableau avec des exemples d'identifiants
     */
    public static String[][] getUserExamples() {
        return new String[][] {
            {"client@example.com", "client123", "CLIENT"},
            {"employe@example.com", "employe123", "EMPLOYE"},
            {"admin@example.com", "admin123", "ADMIN"}
        };
    }
    
    /**
     * Récupère toutes les locations
     * @return Liste de toutes les locations
     */
    @SuppressWarnings("unchecked")
    public static List<Location> getAllLocations() {
        return (List<Location>) dataStore.getOrDefault("locations", new ArrayList<>());
    }
    
    /**
     * Récupère tous les clients
     * @return Liste de tous les clients
     */
    @SuppressWarnings("unchecked")
    public static List<Client> getAllClients() {
        return (List<Client>) dataStore.getOrDefault("clients", new ArrayList<>());
    }
    
    /**
     * Récupère un client par son CIN
     * @param cin CIN du client
     * @return Le client correspondant ou null
     */
    @SuppressWarnings("unchecked")
    public static Client getClientByCIN(int cin) {
        List<Client> clients = (List<Client>) dataStore.getOrDefault("clients", new ArrayList<>());
        
        for (Client client : clients) {
            if (client.getCin() == cin) {
                return client;
            }
        }
        
        return null;
    }
    
    /**
     * Ajoute un client à la liste des clients
     * @param client Le client à ajouter
     * @return true si l'ajout est réussi, false sinon
     */
    @SuppressWarnings("unchecked")
    public static boolean addClient(Client client) {
        List<Client> clients = (List<Client>) dataStore.getOrDefault("clients", new ArrayList<>());
        
        // Vérifier si un client avec ce CIN existe déjà
        for (Client c : clients) {
            if (c.getCin() == client.getCin()) {
                return false; // Client existe déjà
            }
        }
        
        // Ajouter le client
        clients.add(client);
        dataStore.put("clients", clients);
        return true;
    }
    
    /**
     * Met à jour un client existant
     * @param client Le client à mettre à jour
     * @return true si la mise à jour est réussie, false sinon
     */
    @SuppressWarnings("unchecked")
    public static boolean updateClient(Client client) {
        List<Client> clients = (List<Client>) dataStore.getOrDefault("clients", new ArrayList<>());
        
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getCin() == client.getCin()) {
                clients.set(i, client);
                dataStore.put("clients", clients);
                return true;
            }
        }
        
        return false; // Client non trouvé
    }
    
    /**
     * Supprime un client par son CIN
     * @param cin CIN du client à supprimer
     * @return true si la suppression est réussie, false sinon
     */
    @SuppressWarnings("unchecked")
    public static boolean deleteClient(int cin) {
        List<Client> clients = (List<Client>) dataStore.getOrDefault("clients", new ArrayList<>());
        
        for (int i = 0; i < clients.size(); i++) {
            if (clients.get(i).getCin() == cin) {
                clients.remove(i);
                dataStore.put("clients", clients);
                return true;
            }
        }
        
        return false; // Client non trouvé
    }
}
