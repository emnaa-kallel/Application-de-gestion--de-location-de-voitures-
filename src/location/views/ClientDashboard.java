package location.views;

import location.dao.VoitureDAO;
import location.dao.ClientDAO;
import location.dao.ReservationDAO;
import location.models.Utilisateur;
import location.models.Voiture;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Tableau de bord pour les clients
 * Permet de réserver des voitures, payer et modifier son compte
 */
public class ClientDashboard extends JFrame {
    
    private Utilisateur utilisateur;
    private VoitureDAO voitureDAO;
    // Nous utiliserons ces DAOs dans les futures versions
    private ClientDAO clientDAO; 
    private ReservationDAO reservationDAO;
    private JTable tableVoitures;
    private JTable tableReservations;
    private JTabbedPane tabbedPane;
    
    public ClientDashboard(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
        this.voitureDAO = new VoitureDAO();
        this.clientDAO = new ClientDAO();
        this.reservationDAO = new ReservationDAO();
        
        // Configuration de la fenêtre
        setTitle("Espace Client - " + utilisateur.getNom() + " " + utilisateur.getPrenom());
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Initialiser l'interface
        initialiserInterface();
    }
    
    private void initialiserInterface() {
        // Panneau principal avec un BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // En-tête avec info utilisateur et boutons
        JPanel headerPanel = creerEnTete();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Panneau central avec les onglets
        tabbedPane = new JTabbedPane();
        
        // Onglet "Réserver une voiture"
        JPanel reservationPanel = creerPanelReservation();
        tabbedPane.addTab("Réserver une voiture", null, reservationPanel, "Rechercher et réserver une voiture");
        
        // Onglet "Mes réservations"
        JPanel mesReservationsPanel = creerPanelMesReservations();
        tabbedPane.addTab("Mes réservations", null, mesReservationsPanel, "Voir et gérer vos réservations");
        
        // Onglet "Mon profil"
        JPanel profilPanel = creerPanelProfil();
        tabbedPane.addTab("Mon profil", null, profilPanel, "Gérer vos informations personnelles");
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblFooter = new JLabel("© 2025 Location de Voitures - Tous droits réservés");
        lblFooter.setFont(new Font("Arial", Font.ITALIC, 12));
        footerPanel.add(lblFooter);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // Ajouter le panneau principal à la fenêtre
        setContentPane(mainPanel);
    }
    
    private JPanel creerEnTete() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(25, 118, 210)); // Bleu primaire
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Info utilisateur à gauche
        JPanel userInfoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userInfoPanel.setOpaque(false);
        
        JLabel lblUserIcon = new JLabel("\uD83D\uDC64"); // Icône utilisateur (emoji)
        lblUserIcon.setFont(new Font("Arial", Font.BOLD, 24));
        lblUserIcon.setForeground(Color.WHITE);
        
        JLabel lblUserInfo = new JLabel("Bienvenue, " + utilisateur.getPrenom() + " " + utilisateur.getNom());
        lblUserInfo.setFont(new Font("Arial", Font.BOLD, 16));
        lblUserInfo.setForeground(Color.WHITE);
        
        userInfoPanel.add(lblUserIcon);
        userInfoPanel.add(lblUserInfo);
        
        // Titre au centre
        JLabel lblTitre = new JLabel("ESPACE CLIENT", JLabel.CENTER);
        lblTitre.setForeground(Color.WHITE);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 22));
        
        // Boutons à droite
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setOpaque(false);
        
        JButton btnLogout = new JButton("Déconnexion");
        btnLogout.setBackground(new Color(211, 47, 47)); // Rouge
        btnLogout.setForeground(Color.WHITE);
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirection vers l'écran de connexion
                new loginframe().setVisible(true);
                dispose();
            }
        });
        
        buttonsPanel.add(btnLogout);
        
        panel.add(userInfoPanel, BorderLayout.WEST);
        panel.add(lblTitre, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel creerPanelReservation() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de recherche et filtres
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Rechercher une voiture"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Première ligne : recherche par marque et modele
        JPanel marqueModelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        marqueModelPanel.add(new JLabel("Marque/modele:"));
        final JTextField txtRecherche = new JTextField(20);
        marqueModelPanel.add(txtRecherche);
        
        // Deuxième ligne : filtres
        JPanel filtresPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtresPanel.add(new JLabel("Prix max/jour:"));
        final JSpinner spinnerPrix = new JSpinner(new SpinnerNumberModel(1000, 100, 5000, 100));
        filtresPanel.add(spinnerPrix);
        
        filtresPanel.add(Box.createHorizontalStrut(20));
        
        filtresPanel.add(new JLabel("Disponible du:"));
        final JSpinner spinnerDateDebut = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditorDebut = new JSpinner.DateEditor(spinnerDateDebut, "dd/MM/yyyy");
        spinnerDateDebut.setEditor(dateEditorDebut);
        filtresPanel.add(spinnerDateDebut);
        
        filtresPanel.add(new JLabel("au:"));
        final JSpinner spinnerDateFin = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditorFin = new JSpinner.DateEditor(spinnerDateFin, "dd/MM/yyyy");
        spinnerDateFin.setEditor(dateEditorFin);
        filtresPanel.add(spinnerDateFin);
        
        // Troisième ligne : boutons d'action
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRechercher = new JButton("Rechercher");
        btnRechercher.setBackground(new Color(33, 150, 243)); // Bleu
        btnRechercher.setForeground(Color.WHITE);
        
        JButton btnAfficherTout = new JButton("Afficher toutes les voitures");
        btnAfficherTout.setBackground(new Color(76, 175, 80)); // Vert
        btnAfficherTout.setForeground(Color.WHITE);
        
        buttonsPanel.add(btnRechercher);
        buttonsPanel.add(btnAfficherTout);
        
        searchPanel.add(marqueModelPanel);
        searchPanel.add(filtresPanel);
        searchPanel.add(buttonsPanel);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        
        // Liste des voitures
        JPanel voituresPanel = new JPanel(new BorderLayout());
        voituresPanel.setBorder(BorderFactory.createTitledBorder("Voitures disponibles"));
        
        String[] colonnes = {"ID", "Marque", "modele", "annee", "Prix/Jour", "Couleur", "Transmission", "Action"};
        DefaultTableModel modele = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Seule la colonne "Action" est éditable
            }
        };
        
        tableVoitures = new JTable(modele);
        tableVoitures.setRowHeight(30);
        
        // Ajouter un bouton "Réserver" dans la colonne Action
        tableVoitures.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        tableVoitures.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(tableVoitures);
        voituresPanel.add(scrollPane, BorderLayout.CENTER);
        
        panel.add(voituresPanel, BorderLayout.CENTER);
        
        // Chargement initial des voitures
        chargerVoituresDisponibles();
        
        return panel;
    }
    
    private JPanel creerPanelMesReservations() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Titre et message d'information
        JPanel infoPanel = new JPanel(new BorderLayout());
        JLabel lblInfo = new JLabel("Vos réservations de voitures");
        lblInfo.setFont(new Font("Arial", Font.BOLD, 16));
        infoPanel.add(lblInfo, BorderLayout.NORTH);
        
        JLabel lblSubInfo = new JLabel("Consultez et gérez toutes vos réservations ici.");
        lblSubInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        infoPanel.add(lblSubInfo, BorderLayout.SOUTH);
        panel.add(infoPanel, BorderLayout.NORTH);
        
        // Liste des réservations
        String[] colonnes = {"ID", "Voiture", "Date début", "Date fin", "Prix total", "Statut", "Actions"};
        DefaultTableModel modele = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6; // Seule la colonne "Actions" est éditable
            }
        };
        
        tableReservations = new JTable(modele);
        tableReservations.setRowHeight(30);
        
        // Ajouter des boutons dans la colonne Actions (Payer, Annuler)
        tableReservations.getColumnModel().getColumn(6).setCellRenderer(new ButtonRenderer());
        tableReservations.getColumnModel().getColumn(6).setCellEditor(new ButtonEditor(new JCheckBox()));
        
        JScrollPane scrollPane = new JScrollPane(tableReservations);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Charger les réservations du client
        chargerReservationsClient();
        
        return panel;
    }
    
    private JPanel creerPanelProfil() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel d'informations personnelles
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Informations personnelles"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Nom
        infoPanel.add(new JLabel("Nom:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JTextField txtNom = new JTextField(utilisateur.getNom(), 20);
        infoPanel.add(txtNom, gbc);
        
        // Prénom
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        infoPanel.add(new JLabel("Prénom:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField txtPrenom = new JTextField(utilisateur.getPrenom(), 20);
        infoPanel.add(txtPrenom, gbc);
        
        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        infoPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField txtEmail = new JTextField(utilisateur.getEmail(), 20);
        infoPanel.add(txtEmail, gbc);
        
        // Téléphone
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        infoPanel.add(new JLabel("Téléphone:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JTextField txtTelephone;
        txtTelephone = new JTextField(utilisateur.getTel() +"", 20);
        infoPanel.add(txtTelephone, gbc);
        
        // Adresse
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        infoPanel.add(new JLabel("Adresse:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        //JTextField txtAdresse = new JTextField(utilisateur.get(), 20);
        //infoPanel.add(txtAdresse, gbc);
        
        // Bouton de mise à jour
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        JButton btnMiseAJour = new JButton("Mettre à jour mes informations");
        btnMiseAJour.setBackground(new Color(33, 150, 243)); // Bleu
        btnMiseAJour.setForeground(Color.WHITE);
        infoPanel.add(btnMiseAJour, gbc);
        
        panel.add(infoPanel, BorderLayout.NORTH);
        
        // Panel de sécurité pour changement de mot de passe
        JPanel securityPanel = new JPanel(new GridBagLayout());
        securityPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Sécurité"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Ancien mot de passe
        securityPanel.add(new JLabel("Ancien mot de passe:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        JPasswordField txtOldPassword = new JPasswordField(20);
        securityPanel.add(txtOldPassword, gbc);
        
        // Nouveau mot de passe
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        securityPanel.add(new JLabel("Nouveau mot de passe:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPasswordField txtNewPassword = new JPasswordField(20);
        securityPanel.add(txtNewPassword, gbc);
        
        // Confirmer mot de passe
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        securityPanel.add(new JLabel("Confirmer mot de passe:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        JPasswordField txtConfirmPassword = new JPasswordField(20);
        securityPanel.add(txtConfirmPassword, gbc);
        
        // Bouton de changement de mot de passe
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        JButton btnChangerMdp = new JButton("Changer mon mot de passe");
        btnChangerMdp.setBackground(new Color(76, 175, 80)); // Vert
        btnChangerMdp.setForeground(Color.WHITE);
        securityPanel.add(btnChangerMdp, gbc);
        
        panel.add(securityPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Méthode pour charger les voitures disponibles
    private void chargerVoituresDisponibles() {
        DefaultTableModel modele = (DefaultTableModel) tableVoitures.getModel();
        modele.setRowCount(0); // Effacer les données actuelles
        
        List<Voiture> voitures = voitureDAO.listerTout();
        for (Voiture v : voitures) {
            if (v.isDisponible()) {
                modele.addRow(new Object[]{
                    v.getId(), 
                    v.getMarque(), 
                    v.getModele(), 
                    v.getAnnee(), 
                    v.getPrixJour() + " DH", 
                    v.getCouleur(), 
                    v.getTransmission(),
                    "Réserver"
                });
            }
        }
    }
    
    // Méthode pour charger les réservations du client
    private void chargerReservationsClient() {
        DefaultTableModel modele = (DefaultTableModel) tableReservations.getModel();
        modele.setRowCount(0); // Effacer les données actuelles
        
        // Dans une application réelle, vous utiliseriez ici votre DAO pour récupérer
        // les réservations du client depuis la base de données
        
        // Pour l'exemple, nous allons créer quelques données fictives
        // Format de date pour l'affichage
        modele.addRow(new Object[]{
            "1",
            "BMW X5",
            "01/05/2025",
            "05/05/2025",
            "2000 DH",
            "En attente de paiement",
            "Payer"
        });
        
        modele.addRow(new Object[]{
            "2",
            "Mercedes Classe C",
            "10/06/2025",
            "15/06/2025",
            "2500 DH",
            "Confirmée",
            "Annuler"
        });
    }
    
    // Classe pour le rendu des boutons dans les tableaux
    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    // Classe pour l'édition des boutons dans les tableaux
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        
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
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
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
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                if (label.equals("Réserver")) {
                    int row = tableVoitures.getSelectedRow();
                    if (row != -1) {
                        int idVoiture = Integer.parseInt(tableVoitures.getValueAt(row, 0).toString());
                        String marque = tableVoitures.getValueAt(row, 1).toString();
                        String modele = tableVoitures.getValueAt(row, 2).toString();
                        
                        // Afficher un dialogue de confirmation
                        JOptionPane.showMessageDialog(
                            button,
                            "Réservation de la voiture: " + marque + " " + modele + " (ID: " + idVoiture + ")\n" +
                            "Cette fonctionnalité sera implémentée dans une version future.",
                            "Réservation de voiture",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                } else if (label.equals("Payer")) {
                    int row = tableReservations.getSelectedRow();
                    if (row != -1) {
                        String idReservation = tableReservations.getValueAt(row, 0).toString();
                        String voiture = tableReservations.getValueAt(row, 1).toString();
                        String prixTotal = tableReservations.getValueAt(row, 4).toString();
                        
                        // Afficher un dialogue de confirmation
                        JOptionPane.showMessageDialog(
                            button,
                            "Paiement de la réservation n°" + idReservation + " pour la voiture " + voiture + "\n" +
                            "Montant: " + prixTotal + "\n" +
                            "Cette fonctionnalité sera implémentée dans une version future.",
                            "Paiement de réservation",
                            JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                } else if (label.equals("Annuler")) {
                    int row = tableReservations.getSelectedRow();
                    if (row != -1) {
                        String idReservation = tableReservations.getValueAt(row, 0).toString();
                        String voiture = tableReservations.getValueAt(row, 1).toString();
                        
                        // Afficher un dialogue de confirmation
                        int response = JOptionPane.showConfirmDialog(
                            button,
                            "Êtes-vous sûr de vouloir annuler la réservation n°" + idReservation + " pour la voiture " + voiture + "?",
                            "Confirmation d'annulation",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                        );
                        
                        if (response == JOptionPane.YES_OPTION) {
                            JOptionPane.showMessageDialog(
                                button,
                                "La réservation a été annulée avec succès.\n" +
                                "Cette fonctionnalité sera implémentée dans une version future.",
                                "Annulation de réservation",
                                JOptionPane.INFORMATION_MESSAGE
                            );
                        }
                    }
                }
            }
            isPushed = false;
            return label;
        }
        
        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
    
    // Pour tester l'interface
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            //e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Créer un utilisateur client de test
                Utilisateur utilisateur = new Utilisateur();
                utilisateur.setCin(1000);
                utilisateur.setNom("Nom Test");
                utilisateur.setPrenom("Prénom Test");
                utilisateur.setEmail("client@example.com");
                //utilisateur.setTelephone("06123456789");
                //utilisateur.setRole("CLIENT");
                
                ClientDashboard dashboard = new ClientDashboard(utilisateur);
                dashboard.setVisible(true);
            }
        });
    }
    
    
}
