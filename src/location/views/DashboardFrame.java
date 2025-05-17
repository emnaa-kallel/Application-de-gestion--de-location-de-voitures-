package location.views;

import location.dao.VoitureDAO;
import location.dao.LocationDAO;
import location.dao.UtilisateurDAO;
import location.dao.PenaliteDAO;
import location.models.Utilisateur;
import location.models.Voiture;
import location.models.Location;
import location.models.Penalite;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import location.models.Personnel;

/**
 * Interface de tableau de bord adaptée au rôle de l'utilisateur
 * @author sahar
 */
public class DashboardFrame extends JFrame {
    
    private final Utilisateur utilisateurConnecte;
    private final Personnel personnel;
    private JTabbedPane tabbedPane;
    private VoitureDAO voitureDAO;
    private LocationDAO locationDAO;
    private UtilisateurDAO utilisateurDAO;
    private PenaliteDAO penaliteDAO;
    
    // Tableaux pour afficher les données
    private JTable tableVoitures;
    private JTable tableLocations;
    private JTable tableUtilisateurs;
    private JTable tablePenalites;
    
    // Panels pour les différentes fonctionnalités
    private JPanel panelVoitures;
    private JPanel panelLocations;
    private JPanel panelUtilisateurs;
    private JPanel panelPenalites;
    private JPanel panelCompte;
    
        public DashboardFrame(Utilisateur utilisateur, Personnel personnel) {
        this.utilisateurConnecte = utilisateur;
        this.personnel = personnel;
        this.voitureDAO = new VoitureDAO();
        this.locationDAO = new LocationDAO();
        this.utilisateurDAO = new UtilisateurDAO();
        this.penaliteDAO = new PenaliteDAO();
        
        // Configuration de la fenêtre
        setTitle("Location de Voitures - Tableau de Bord " + roleLisible(personnel.getRole()));
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        // Définir une icône pour l'application (si disponible)
        try {
            // Vous pouvez ajouter une icône personnalisée ici
            // setIconImage(new ImageIcon(getClass().getResource("/images/car_icon.png")).getImage());
        } catch (Exception e) {
            System.out.println("Icône non disponible: " + e.getMessage());
        }
        
        // Initialiser l'interface selon le rôle
        initialiserInterface();
    }
    
    private void initialiserInterface() {
        // Panneau principal avec un BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // En-tête avec informations utilisateur
        JPanel headerPanel = creerEnTete();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Initialiser le TabbedPane pour les différentes fonctionnalités
        tabbedPane = new JTabbedPane();
        
        // Créer les panneaux spécifiques au rôle
        creerPanneauxSelonRole();
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Ajouter le panneau principal à la fenêtre
        setContentPane(mainPanel);
    }
    
    /**
     * Convertit le rôle technique en libellé lisible
     * @param role Le rôle technique (ADMIN, CLIENT, EMPLOYE)
     * @return Le libellé lisible du rôle
     */
    private String roleLisible(String role) {
        return switch (role.toUpperCase()) {
            case "ADMIN" -> "Administrateur";
            case "CLIENT" -> "Client";
            case "EMPLOYE" -> "Employé";
            default -> role;
        };
    }
    
    private JPanel creerEnTete() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(25, 25, 112)); // Bleu foncé
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Label de bienvenue avec nom et rôle
        JLabel lblBienvenue = new JLabel("Bienvenue, " + 
                utilisateurConnecte.getPrenom() + " " + utilisateurConnecte.getNom() + 
                " - " + roleLisible(personnel.getRole()));
        lblBienvenue.setForeground(Color.WHITE);
        lblBienvenue.setFont(new Font("Arial", Font.BOLD, 16));
        
        // Bouton de déconnexion
        JButton btnDeconnexion = new JButton("Déconnexion");
        btnDeconnexion.setBackground(new Color(186, 85, 211)); // Violet clair
        btnDeconnexion.setForeground(Color.WHITE);
        btnDeconnexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Retour à l'écran de connexion
                new loginframe().setVisible(true);
                dispose();
            }
        });
        
        panel.add(lblBienvenue, BorderLayout.WEST);
        panel.add(btnDeconnexion, BorderLayout.EAST);
        
        return panel;
    }
    
    private void creerPanneauxSelonRole() {
        // Panneau commun : gestion du compte
        creerPanelCompte();
        tabbedPane.addTab("Mon Compte", panelCompte);
        
        String role = personnel.getRole();
        
        if (role.equalsIgnoreCase("CLIENT")) {
            // Pour les clients
            // - Recherche et location de voitures
            panelVoitures = creerPanelVoituresClient();
            tabbedPane.addTab("Rechercher Voitures", panelVoitures);
            
            // - Voir l'historique des locations
            panelLocations = creerPanelHistoriqueLocations();
            tabbedPane.addTab("Mes Locations", panelLocations);
            
        } else if (role.equalsIgnoreCase("EMPLOYE")) {
            // Pour les employés
            // - Gestion des voitures (ajouter, modifier, supprimer)
            panelVoitures = creerPanelGestionVoitures();
            tabbedPane.addTab("Gestion Voitures", panelVoitures);
            
            // - Gestion des pénalités
            panelPenalites = creerPanelGestionPenalites();
            tabbedPane.addTab("Gestion Pénalités", panelPenalites);
            
        } else if (role.equalsIgnoreCase("ADMIN")) {
            // Pour les administrateurs
            // - Gestion des voitures
            panelVoitures = creerPanelGestionVoitures();
            tabbedPane.addTab("Gestion Voitures", panelVoitures);
            
            // - Gestion des employés
            creerPanelGestionUtilisateurs();
            tabbedPane.addTab("Gestion Employés", panelUtilisateurs);
            
            // - Gestion des pénalités
            creerPanelGestionPenalites();
            tabbedPane.addTab("Gestion Pénalités", panelPenalites);
        }
    }
    
    private void creerPanelCompte() {
        panelCompte = new JPanel(new BorderLayout(10, 10));
        panelCompte.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Informations utilisateur
        JPanel infoPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        
        // Formulaire pour les informations du compte
        JTextField txtCin = new JTextField(String.valueOf(utilisateurConnecte.getCin()));
        txtCin.setEditable(false);
        JTextField txtNom = new JTextField(utilisateurConnecte.getNom());
        JTextField txtPrenom = new JTextField(utilisateurConnecte.getPrenom());
        JTextField txtTelephone = new JTextField(utilisateurConnecte.getTel());
        JTextField txtEmail = new JTextField(utilisateurConnecte.getEmail());
        JPasswordField txtMotDePasse = new JPasswordField(10);
        
        infoPanel.add(new JLabel("CIN:"));
        infoPanel.add(txtCin);
        infoPanel.add(new JLabel("Nom:"));
        infoPanel.add(txtNom);
        infoPanel.add(new JLabel("Prénom:"));
        infoPanel.add(txtPrenom);
        infoPanel.add(new JLabel("Téléphone:"));
        infoPanel.add(txtTelephone);
        infoPanel.add(new JLabel("Email:"));
        infoPanel.add(txtEmail);
        infoPanel.add(new JLabel("Nouveau mot de passe:"));
        infoPanel.add(txtMotDePasse);
        
        // Bouton de mise à jour
        JButton btnMajCompte = new JButton("Mettre à jour mon compte");
        btnMajCompte.setBackground(new Color(102, 51, 153)); // Violet
        btnMajCompte.setForeground(Color.WHITE);
        btnMajCompte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mise à jour des informations du compte
                String nouveauMotDePasse = new String(txtMotDePasse.getPassword());
                
                // Mettre à jour l'objet utilisateur
                utilisateurConnecte.setNom(txtNom.getText());
                utilisateurConnecte.setPrenom(txtPrenom.getText());
                //utilisateurConnecte.setTelephone(txtTelephone.getText());
                utilisateurConnecte.setEmail(txtEmail.getText());
                
                if (!nouveauMotDePasse.isEmpty()) {
                    utilisateurConnecte.setMotDePasse(nouveauMotDePasse);
                }
                
                // Mettre à jour dans la base de données
                boolean success = utilisateurDAO.mettreAJour(utilisateurConnecte);
                
                if (success) {
                    JOptionPane.showMessageDialog(DashboardFrame.this, 
                        "Informations mises à jour avec succès!", 
                        "Mise à jour réussie", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(DashboardFrame.this, 
                        "Erreur lors de la mise à jour des informations", 
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Zone pour la photo de profil (à implémenter)
        JPanel photoPanel = new JPanel(new BorderLayout());
        photoPanel.setPreferredSize(new Dimension(150, 150));
        photoPanel.setBorder(BorderFactory.createTitledBorder("Photo de profil"));
        
        JLabel lblPhoto = new JLabel("Aucune photo", JLabel.CENTER);
        photoPanel.add(lblPhoto, BorderLayout.CENTER);
        
        JButton btnChoisirPhoto = new JButton("Choisir une photo");
        photoPanel.add(btnChoisirPhoto, BorderLayout.SOUTH);
        
        // Layout final
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(photoPanel, BorderLayout.NORTH);
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.add(infoPanel, BorderLayout.CENTER);
        rightPanel.add(btnMajCompte, BorderLayout.SOUTH);
        
        panelCompte.add(leftPanel, BorderLayout.WEST);
        panelCompte.add(rightPanel, BorderLayout.CENTER);
    }
    
    /**
     * Crée le panneau de recherche de voitures pour les clients
     * @return Le panneau configuré
     */
    private JPanel creerPanelVoituresClient() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panneau de recherche
        JPanel recherchePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        recherchePanel.add(new JLabel("Rechercher : "));
        JTextField txtRecherche = new JTextField(20);
        recherchePanel.add(txtRecherche);
        JButton btnRechercher = new JButton("Rechercher");
        recherchePanel.add(btnRechercher);
        
        // Panneau de filtres
        JPanel filtresPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtresPanel.add(new JLabel("Marque : "));
        JComboBox<String> comboMarque = new JComboBox<>(new String[]{"Toutes", "BMW", "Mercedes", "Renault", "Peugeot", "Audi"});
        filtresPanel.add(comboMarque);
        
        filtresPanel.add(new JLabel("Prix max/jour : "));
        JSpinner spinnerPrix = new JSpinner(new SpinnerNumberModel(1000, 100, 5000, 100));
        filtresPanel.add(spinnerPrix);
        
        // Combiner les panneaux de recherche et filtres
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(recherchePanel, BorderLayout.NORTH);
        topPanel.add(filtresPanel, BorderLayout.SOUTH);
        
        // Table des voitures
        String[] colonnes = {"ID", "Marque", "modele", "annee", "Prix/Jour", "disponibilite", "Actions"};
        DefaultTableModel modele = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Seule la colonne Actions est éditable
            }
        };
        
        tableVoitures = new JTable(modele);
        JScrollPane scrollPane = new JScrollPane(tableVoitures);
        
        // Configurer les boutons dans la colonne d'actions
        configurerBoutonsTable(tableVoitures, 6);
        
        // Charger les données initiales
        List<Voiture> voitures = voitureDAO.listerDisponibles();
        for (Voiture v : voitures) {
            String disponibilite = v.isDisponible() ? "Disponible" : "Indisponible";
            modele.addRow(new Object[]{
                v.getId(), v.getMarque(), v.getModele(), 
                v.getAnnee(), v.getPrixJournalier() + " DH", 
                disponibilite, "Réserver"
            });
        }
        
        // Ajouter au panneau principal
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Crée le panneau d'historique des locations pour les clients
     * @return Le panneau configuré
     */
    private JPanel creerPanelHistoriqueLocations() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Filtres pour l'historique
        JPanel filtresPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtresPanel.add(new JLabel("Statut : "));
        JComboBox<String> comboStatut = new JComboBox<>(new String[]{"Tous", "En cours", "Terminée", "Annulée"});
        filtresPanel.add(comboStatut);
        
        JButton btnFiltrer = new JButton("Filtrer");
        filtresPanel.add(btnFiltrer);
        
        // Table des locations
        String[] colonnes = {"ID", "Voiture", "Date Début", "Date Fin", "Prix Total", "Statut", "Actions"};
        DefaultTableModel modele = new DefaultTableModel(colonnes, 0);
        
        tableLocations = new JTable(modele);
        JScrollPane scrollPane = new JScrollPane(tableLocations);
        
        // Configurer les boutons dans la colonne d'actions
        configurerBoutonsTable(tableLocations, 6);
        
        // Charger les données de l'historique
        List<Location> locations = locationDAO.listerParClient(utilisateurConnecte.getCin());
        for (Location loc : locations) {
            // Récupérer les infos de la voiture associée à la location
            Voiture voiture = voitureDAO.trouverParId(loc.getVoitureId());
            String nomVoiture = voiture != null ? voiture.getMarque() + " " + voiture.getModele() : "Inconnue";
            
            modele.addRow(new Object[]{
                loc.getId(), nomVoiture, 
                loc.getDateDebut(), loc.getDateFin(), 
                //loc.getPrixTotal() + " DH", loc.getStatut(), 
                "Détails"
            });
        }
        
        // Ajouter au panneau principal
        panel.add(filtresPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    public void rafraichirTableVoitures(){
        
    }
    /**
     * Crée le panneau de gestion des voitures pour les employés et administrateurs
     * @return Le panneau configuré
     */
    private JPanel creerPanelGestionVoitures() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panneau de boutons pour les actions
        JPanel boutonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAjouter = new JButton("Ajouter une voiture");
        btnAjouter.setBackground(new Color(60, 179, 113)); // Vert moyen
        btnAjouter.setForeground(Color.WHITE);
        
        JButton btnRafraichir = new JButton("Rafraîchir");
        btnRafraichir.setBackground(new Color(30, 144, 255)); // DodgerBlue
        btnRafraichir.setForeground(Color.WHITE);
        
        // Ajouter des écouteurs d'événements aux boutons
        btnAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // À implémenter : Ouvrir un formulaire pour ajouter une voiture
                JOptionPane.showMessageDialog(DashboardFrame.this, 
                    "La fonctionnalité d'ajout de voiture sera implémentée dans une version future.", 
                    "Fonctionnalité à venir", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        btnRafraichir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Rafraîchir la liste des voitures
                rafraichirTableVoitures();
            }
        });
        
        boutonsPanel.add(btnAjouter);
        boutonsPanel.add(btnRafraichir);
        
        // Table des voitures
        String[] colonnes = {"ID", "Marque", "modele", "annee", "Prix/Jour", "Couleur", "Transmission", "etat", "Actions"};
        DefaultTableModel modele = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Rendre uniquement la colonne d'actions éditable
                return column == 8;
            }
        };
        
        tableVoitures = new JTable(modele);
        tableVoitures.setRowHeight(30); // Améliore la lisibilité
        JScrollPane scrollPane = new JScrollPane(tableVoitures);
        
        // Configurer les boutons dans la colonne d'actions
        configurerBoutonsTable(tableVoitures, 8);
        
        // Charger toutes les voitures
        List<Voiture> voitures = voitureDAO.listerTout();
        for (Voiture v : voitures) {
            modele.addRow(new Object[]{
                v.getId(), v.getMarque(), v.getModele(), 
                v.getAnnee(), v.getPrixJournalier() + " DH", 
                v.getCouleur(), v.getTransmission(), v.getDisponibilite(),
                "Modifier"
            });
        }
        
        // Ajouter au panneau principal
        panel.add(boutonsPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Crée le panneau de gestion des pénalités pour les employés et administrateurs
     * @return Le panneau configuré
     */
    private JPanel creerPanelGestionPenalites() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panneau de boutons pour les actions
        JPanel boutonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAjouter = new JButton("Ajouter une pénalité");
        JButton btnRafraichir = new JButton("Rafraîchir");
        
        boutonsPanel.add(btnAjouter);
        boutonsPanel.add(btnRafraichir);
        
        // Table des pénalités
        String[] colonnes = {"ID", "Location ID", "Montant", "Raison", "Date", "Actions"};
        DefaultTableModel modele = new DefaultTableModel(colonnes, 0);
        
        tablePenalites = new JTable(modele);
        JScrollPane scrollPane = new JScrollPane(tablePenalites);
        
        // Configurer les boutons dans la colonne d'actions
        configurerBoutonsTable(tablePenalites, 5);
        
        // Charger toutes les pénalités
        List<Penalite> penalites = penaliteDAO.listerToutes();
        for (Penalite p : penalites) {
            modele.addRow(new Object[]{
                p.getIdPenalite(), p.getIdLocation(), 
                p.getMontant() + " DH", p.getRaison(), 
                p.getDatePenalite(), "Détails"
            });
        }
        
        // Ajouter au panneau principal
        panel.add(boutonsPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Crée le panneau de gestion des utilisateurs pour les administrateurs
     * @return Le panneau configuré
     */
    private JPanel creerPanelGestionUtilisateurs() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panneau de boutons pour les actions
        JPanel boutonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAjouter = new JButton("Ajouter un utilisateur");
        JButton btnRafraichir = new JButton("Rafraîchir");
        
        boutonsPanel.add(btnAjouter);
        boutonsPanel.add(btnRafraichir);
        
        // Table des utilisateurs
        String[] colonnes = {"CIN", "Nom", "Prénom", "Email", "Téléphone", "Rôle", "Actions"};
        DefaultTableModel modele = new DefaultTableModel(colonnes, 0);
        
        tableUtilisateurs = new JTable(modele);
        JScrollPane scrollPane = new JScrollPane(tableUtilisateurs);
        
        // Configurer les boutons dans la colonne d'actions
        configurerBoutonsTable(tableUtilisateurs, 6);
        
        // Charger tous les utilisateurs
        List<Utilisateur> utilisateurs = utilisateurDAO.listerTous();
        for (Utilisateur u : utilisateurs) {
            modele.addRow(new Object[]{
                u.getCin(), u.getNom(), u.getPrenom(), 
                u.getEmail(), u.getTel(), 
                roleLisible(u.getRole()), "Modifier"
            });
        }
        
        // Ajouter au panneau principal
        panel.add(boutonsPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Configure les boutons dans une table pour une colonne spécifique
     * @param table La table à configurer
     * @param colonneAction L'index de la colonne qui contiendra les boutons
     */
    private void configurerBoutonsTable(JTable table, int colonneAction) {
        // Configurer le rendu des boutons dans la colonne d'action
        table.getColumnModel().getColumn(colonneAction).setCellRenderer(new ButtonRenderer());
        
        // Configurer l'éditeur de cellule pour les boutons dans la colonne d'action
        table.getColumnModel().getColumn(colonneAction).setCellEditor(
            new ButtonEditor(new JCheckBox())
        );
    }
    
    /**
     * Classes utilitaires pour les boutons dans les tables
     * Permet d'afficher des boutons cliquables dans les cellules de table
     */
    private class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        private static final long serialVersionUID = 1L;
        
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    /**
     * Éditeur de cellule pour les boutons dans les tables
     * Permet de rendre les boutons cliquables et de gérer les événements
     */
    private class ButtonEditor extends DefaultCellEditor {
        private static final long serialVersionUID = 1L;
        protected JButton button;
        private String label;
        private boolean isPushed;
        private int currentRow;
        private JTable currentTable;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(UIManager.getColor("Button.background"));
            }
            
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            currentRow = row;
            currentTable = table;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Déterminer quelle action effectuer en fonction de la table et du bouton
                if (currentTable == tableVoitures) {
                    handleVoitureAction(currentRow, label);
                } else if (currentTable == tableLocations) {
                    handleLocationAction(currentRow, label);
                } else if (currentTable == tablePenalites) {
                    handlePenaliteAction(currentRow, label);
                } else if (currentTable == tableUtilisateurs) {
                    handleUtilisateurAction(currentRow, label);
                }
            }
            isPushed = false;
            return label;
        }
        
        private void handleVoitureAction(int row, String action) {
            String immatriculation = tableVoitures.getValueAt(row, 0).toString();
            String marque = tableVoitures.getValueAt(row, 1).toString();
            String modele = tableVoitures.getValueAt(row, 2).toString();
            
            if (action.equals("Réserver")) {
                // Action de réservation pour les clients
                JOptionPane.showMessageDialog(DashboardFrame.this,
                    "Fonctionnalité de réservation à implémenter pour la voiture: " + marque + " " + modele,
                    "Réservation", JOptionPane.INFORMATION_MESSAGE);
                
            } else if (action.equals("Modifier")) {
                // Action de modification pour admin/employé
                JOptionPane.showMessageDialog(DashboardFrame.this,
                    "Formulaire de modification à implémenter pour la voiture: " + marque + " " + modele,
                    "Modification", JOptionPane.INFORMATION_MESSAGE);
                
            } else if (action.equals("Supprimer")) {
                // Action de suppression pour admin/employé
                int response = JOptionPane.showConfirmDialog(DashboardFrame.this,
                    "Êtes-vous sûr de vouloir supprimer la voiture: " + marque + " " + modele + "?",
                    "Confirmation de suppression", JOptionPane.YES_NO_OPTION);
                
                if (response == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(DashboardFrame.this,
                        "Suppression à implémenter pour la voiture ID: " + immatriculation,
                        "Suppression", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        
        private void handleLocationAction(int row, String action) {
            String idLocation = tableLocations.getValueAt(row, 0).toString();
            
            if (action.equals("Détails")) {
                JOptionPane.showMessageDialog(DashboardFrame.this,
                    "Affichage des détails à implémenter pour la location ID: " + idLocation,
                    "Détails", JOptionPane.INFORMATION_MESSAGE);
                
            } else if (action.equals("Terminer")) {
                int response = JOptionPane.showConfirmDialog(DashboardFrame.this,
                    "Êtes-vous sûr de vouloir terminer la location ID: " + idLocation + "?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);
                
                if (response == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(DashboardFrame.this,
                        "Opération 'Terminer location' à implémenter",
                        "Terminer location", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        
        private void handlePenaliteAction(int row, String action) {
            String idPenalite = tablePenalites.getValueAt(row, 0).toString();
            
            if (action.equals("Payer")) {
                JOptionPane.showMessageDialog(DashboardFrame.this,
                    "Fonctionnalité de paiement à implémenter pour la pénalité ID: " + idPenalite,
                    "Paiement", JOptionPane.INFORMATION_MESSAGE);
                
            } else if (action.equals("Annuler")) {
                int response = JOptionPane.showConfirmDialog(DashboardFrame.this,
                    "Êtes-vous sûr de vouloir annuler la pénalité ID: " + idPenalite + "?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);
                
                if (response == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(DashboardFrame.this,
                        "Opération d'annulation à implémenter",
                        "Annulation", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        
        private void handleUtilisateurAction(int row, String action) {
            int cin = Integer.parseInt(tableUtilisateurs.getValueAt(row, 0).toString());
            String nom = tableUtilisateurs.getValueAt(row, 1).toString();
            String prenom = tableUtilisateurs.getValueAt(row, 2).toString();
            
            if (action.equals("Modifier")) {
                JOptionPane.showMessageDialog(DashboardFrame.this,
                    "Formulaire de modification à implémenter pour l'utilisateur: " + prenom + " " + nom,
                    "Modification", JOptionPane.INFORMATION_MESSAGE);
                
            } else if (action.equals("Supprimer")) {
                int response = JOptionPane.showConfirmDialog(DashboardFrame.this,
                    "Êtes-vous sûr de vouloir supprimer l'utilisateur: " + prenom + " " + nom + "?",
                    "Confirmation de suppression", JOptionPane.YES_NO_OPTION);
                
                if (response == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(DashboardFrame.this,
                        "Suppression à implémenter pour l'utilisateur CIN: " + cin,
                        "Suppression", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
        
        public void rafraichirTableVoitures(){
            
        }
        
        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}
