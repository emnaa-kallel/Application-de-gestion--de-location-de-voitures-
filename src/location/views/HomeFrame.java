package location.views;

import location.dao.VoitureDAO;
import location.models.Voiture;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Interface d'accueil pour consulter les voitures sans être connecté
 * @author sahar
 */
public class HomeFrame extends JFrame {
    
    private final VoitureDAO voitureDAO;
    private JTable tableVoitures;
    
    public HomeFrame() {
        this.voitureDAO = new VoitureDAO();
        
        // Configuration de la fenêtre
        setTitle("E.S.O Car Manager - Accueil");
        setSize(1000, 800); // Augmenter la taille de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Initialiser l'interface
        initialiserInterface();
    }
    
    private void initialiserInterface() {
        // Panneau principal avec un BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(0, 10)); // Ajouter un espacement vertical
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0)); // Marge en bas
        
        // En-tête avec titre et boutons
        JPanel headerPanel = creerEnTete();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Créer un panel central qui contiendra la recherche et les voitures
        JPanel centrePanel = new JPanel(new BorderLayout(0, 20)); // Ajouter un espacement vertical important
        centrePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Marges latérales
        
        // Panneau pour la recherche et les filtres - MAINTENANT EN HAUT
        JPanel searchPanel = creerPanelRecherche();
        searchPanel.setPreferredSize(new Dimension(980, 220)); // Spécifier une taille fixe pour s'assurer qu'il est visible
        centrePanel.add(searchPanel, BorderLayout.NORTH);
        
        // Panneau pour la liste des voitures
        JPanel voituresPanel = creerPanelVoitures();
        centrePanel.add(voituresPanel, BorderLayout.CENTER);
        
        // Ajouter le panneau central au panneau principal
        mainPanel.add(centrePanel, BorderLayout.CENTER);
        
        // Ajouter le panneau principal à la fenêtre
        setContentPane(mainPanel);
    }
    
    private JPanel creerEnTete() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(25, 25, 112)); // Bleu foncé
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Titre
        JLabel lblTitre = new JLabel("E.S.O", JLabel.CENTER);
        lblTitre.setForeground(Color.WHITE);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 22));
        
        // Panneau pour les boutons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setOpaque(false);
        
        // Bouton de connexion
        JButton btnLogin = new JButton("Connexion");
        btnLogin.setBackground(new Color(186, 85, 211)); // Violet clair
        btnLogin.setForeground(Color.WHITE);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirection vers l'écran de connexion
                new loginframe().setVisible(true);
                dispose();
            }
        });
        
        // Bouton d'inscription
        JButton btnRegister = new JButton("Créer un compte");
        btnRegister.setBackground(new Color(60, 179, 113)); // Vert
        btnRegister.setForeground(Color.WHITE);
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Redirection vers l'écran d'inscription
                new RegisterFrame().setVisible(true);
                dispose();
            }
        });
        
        buttonsPanel.add(btnLogin);
        buttonsPanel.add(btnRegister);
        
        panel.add(lblTitre, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel creerPanelRecherche() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        panel.setBackground(new Color(245, 245, 245)); // Fond légèrement grisé pour bien voir le panneau
        
        // Message d'accueil
        JLabel lblBienvenue = new JLabel("Bienvenue dans notre service de location de voitures. Veuillez consulter notre parc de véhicules.");
        lblBienvenue.setFont(new Font("Arial", Font.ITALIC, 14));
        panel.add(lblBienvenue, BorderLayout.NORTH);
        
        // Panel central qui contiendra tous les contrôles de recherche
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new GridLayout(3, 1, 0, 10)); // 3 lignes avec espacement vertical
        controlsPanel.setBackground(new Color(245, 245, 245));
        
        // 1. Panneau pour le bouton Afficher toutes les voitures
        JPanel afficherPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        afficherPanel.setBackground(new Color(245, 245, 245));
        JButton btnAfficherTout = new JButton("Afficher toutes les voitures disponibles");
        btnAfficherTout.setBackground(new Color(102, 178, 255));
        btnAfficherTout.setForeground(Color.WHITE);
        btnAfficherTout.setFont(new Font("Arial", Font.BOLD, 12));
        afficherPanel.add(btnAfficherTout);
        
        // 2. Panneau de recherche
        JPanel recherchePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        recherchePanel.setBackground(new Color(245, 245, 245));
        JLabel lblRecherche = new JLabel("Rechercher une voiture : ");
        lblRecherche.setFont(new Font("Arial", Font.PLAIN, 14));
        recherchePanel.add(lblRecherche);
        
        final JTextField txtRecherche = new JTextField(20);
        txtRecherche.setFont(new Font("Arial", Font.PLAIN, 14));
        recherchePanel.add(txtRecherche);
        
        JButton btnRechercher = new JButton("Rechercher");
        btnRechercher.setBackground(new Color(0, 153, 51));
        btnRechercher.setForeground(Color.WHITE);
        btnRechercher.setFont(new Font("Arial", Font.BOLD, 12));
        recherchePanel.add(btnRechercher);
        
        // 3. Panneau de filtres
        JPanel filtresPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtresPanel.setBackground(new Color(245, 245, 245));
        JLabel lblMarque = new JLabel("Filtrer par marque : ");
        lblMarque.setFont(new Font("Arial", Font.PLAIN, 14));
        filtresPanel.add(lblMarque);
        
        final JComboBox<String> comboMarque = new JComboBox<>(new String[]{"Toutes", "BMW", "Mercedes", "Renault", "Peugeot", "Audi"});
        comboMarque.setFont(new Font("Arial", Font.PLAIN, 14));
        filtresPanel.add(comboMarque);
        
        JLabel lblPrix = new JLabel("Prix max/jour : ");
        lblPrix.setFont(new Font("Arial", Font.PLAIN, 14));
        filtresPanel.add(lblPrix);
        
        final JSpinner spinnerPrix = new JSpinner(new SpinnerNumberModel(1000, 100, 5000, 100));
        spinnerPrix.setFont(new Font("Arial", Font.PLAIN, 14));
        filtresPanel.add(spinnerPrix);
        
        JButton btnFiltrer = new JButton("Appliquer les filtres");
        btnFiltrer.setBackground(new Color(153, 51, 255));
        btnFiltrer.setForeground(Color.WHITE);
        btnFiltrer.setFont(new Font("Arial", Font.BOLD, 12));
        filtresPanel.add(btnFiltrer);
        
        JButton btnReinitialiser = new JButton("Réinitialiser");
        btnReinitialiser.setBackground(Color.GRAY);
        btnReinitialiser.setForeground(Color.WHITE);
        btnReinitialiser.setFont(new Font("Arial", Font.BOLD, 12));
        filtresPanel.add(btnReinitialiser);
        
        // Ajouter les panneaux au panel de contrôle
        controlsPanel.add(afficherPanel);
        controlsPanel.add(recherchePanel);
        controlsPanel.add(filtresPanel);
        
        // Ajouter le panel de contrôle au panel principal
        panel.add(controlsPanel, BorderLayout.CENTER);
        
        // Ajouter un panel pour les informations en bas
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(new Color(245, 245, 245));
        JLabel lblInfo = new JLabel("* Utilisez les options ci-dessus pour trouver la voiture qui vous convient");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        lblInfo.setForeground(new Color(128, 128, 128));
        infoPanel.add(lblInfo);
        panel.add(infoPanel, BorderLayout.SOUTH);
        
        // Ajouter les listeners pour les boutons
        btnAfficherTout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtRecherche.setText("");
                comboMarque.setSelectedIndex(0); // "Toutes"
                spinnerPrix.setValue(1000);
                afficherToutesLesVoitures();
            }
        });
        
        btnRechercher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String modeleRecherche = txtRecherche.getText().trim();
                if (!modeleRecherche.isEmpty()) {
                    rechercherVoituresParModele(modeleRecherche);
                } else {
                    JOptionPane.showMessageDialog(HomeFrame.this, 
                        "Veuillez entrer un modele ou une marque de voiture à rechercher", 
                        "Recherche vide", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        
        btnFiltrer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String marque = comboMarque.getSelectedItem().toString();
                int prixMax = (Integer) spinnerPrix.getValue();
                filtrerVoitures(marque, prixMax);
            }
        });
        
        btnReinitialiser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtRecherche.setText("");
                comboMarque.setSelectedIndex(0); // "Toutes"
                spinnerPrix.setValue(1000);
                afficherToutesLesVoitures();
            }
        });
        
        return panel;
    }
    
    private JPanel creerPanelVoitures() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));
        
        // Table des voitures
        String[] colonnes = {"ID", "Marque", "modele", "annee", "Prix/Jour", "Couleur", "Transmission", "disponibilite"};
        DefaultTableModel modele = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Aucune cellule n'est éditable
            }
        };
        
        tableVoitures = new JTable(modele);
        tableVoitures.getColumnModel().getColumn(0).setPreferredWidth(30); // ID plus étroit
        tableVoitures.getColumnModel().getColumn(1).setPreferredWidth(100); // Marque
        tableVoitures.getColumnModel().getColumn(2).setPreferredWidth(120); // modele
        tableVoitures.getColumnModel().getColumn(4).setPreferredWidth(80); // Prix
        
        JScrollPane scrollPane = new JScrollPane(tableVoitures);
        scrollPane.setPreferredSize(new Dimension(850, 400));
        
        // Charger les données initiales
        afficherToutesLesVoitures();
        
        // Message en bas de page
        JLabel lblInfo = new JLabel("Pour réserver une voiture, veuillez vous connecter ou créer un compte.");
        lblInfo.setFont(new Font("Arial", Font.ITALIC, 12));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(lblInfo, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // Méthode pour afficher toutes les voitures
    private void afficherToutesLesVoitures() {
        DefaultTableModel modele = (DefaultTableModel) tableVoitures.getModel();
        modele.setRowCount(0); // Effacer les données actuelles
        
        List<Voiture> voitures = voitureDAO.listerTout();
        for (Voiture v : voitures) {
            modele.addRow(new Object[]{
                v.getId(), v.getMarque(), v.getModele(), 
                v.getAnnee(), v.getPrixJour() + " DH", 
                v.getCouleur(), v.getTransmission(), 
                v.isDisponible() ? "Disponible" : "Indisponible"
            });
        }
    }
    
    // Méthode pour rechercher des voitures par modele
    private void rechercherVoituresParModele(String modele) {
        DefaultTableModel tableModel = (DefaultTableModel) tableVoitures.getModel();
        tableModel.setRowCount(0); // Effacer les données actuelles
        
        List<Voiture> voitures = voitureDAO.listerTout();
        boolean trouve = false;
        
        for (Voiture v : voitures) {
            if (v.getModele().toLowerCase().contains(modele.toLowerCase()) || 
                v.getMarque().toLowerCase().contains(modele.toLowerCase())) {
                tableModel.addRow(new Object[]{
                    v.getId(), v.getMarque(), v.getModele(), 
                    v.getAnnee(), v.getPrixJour() + " DH", 
                    v.getCouleur(), v.getTransmission(), 
                    v.isDisponible() ? "Disponible" : "Indisponible"
                });
                trouve = true;
            }
        }
        
        if (!trouve) {
            JOptionPane.showMessageDialog(this, 
                "Aucune voiture ne correspond à votre recherche.", 
                "Pas de résultat", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Méthode pour filtrer les voitures par marque et prix
    private void filtrerVoitures(String marque, double prixMax) {
        DefaultTableModel tableModel = (DefaultTableModel) tableVoitures.getModel();
        tableModel.setRowCount(0); // Effacer les données actuelles
        
        List<Voiture> voitures = voitureDAO.listerTout();
        boolean trouve = false;
        
        for (Voiture v : voitures) {
            boolean matchMarque = marque.equals("Toutes") || v.getMarque().equals(marque);
            boolean matchPrix = v.getPrixJour() <= prixMax;
            
            if (matchMarque && matchPrix) {
                tableModel.addRow(new Object[]{
                    v.getId(), v.getMarque(), v.getModele(), 
                    v.getAnnee(), v.getPrixJour() + " DH", 
                    v.getCouleur(), v.getTransmission(), 
                    v.isDisponible() ? "Disponible" : "Indisponible"
                });
                trouve = true;
            }
        }
        
        if (!trouve) {
            JOptionPane.showMessageDialog(this, 
                "Aucune voiture ne correspond à vos critères de filtrage.", 
                "Pas de résultat", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomeFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomeFrame().setVisible(true);
            }
        });
    }
}
