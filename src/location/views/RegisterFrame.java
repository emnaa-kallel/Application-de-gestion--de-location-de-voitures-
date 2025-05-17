package location.views;

import location.dao.UtilisateurDAO;
import location.models.Utilisateur;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Interface d'inscription pour créer un nouveau compte utilisateur
 * @author sahar
 */
public class RegisterFrame extends JFrame {
    private JTextField txtCin;
    private JTextField txtNom;
    private JTextField txtPrenom;
    private JTextField txtTelephone;
    private JTextField txtEmail;
    private JPasswordField txtMotDePasse;
    private JPasswordField txtConfirmation;
    private JButton btnInscrire;
    private JButton btnAnnuler;
    
    public RegisterFrame() {
        setTitle("Création de compte");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Créer le panneau principal avec un espacement
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Créer le panneau de formulaire
        JPanel formPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        
        // Titre
        JLabel lblTitre = new JLabel("CRÉER UN NOUVEAU COMPTE", JLabel.CENTER);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitre.setForeground(new Color(102, 51, 153)); // Violet
        
        // Champs du formulaire
        formPanel.add(new JLabel("CIN:"));
        txtCin = new JTextField();
        formPanel.add(txtCin);
        
        formPanel.add(new JLabel("Nom:"));
        txtNom = new JTextField();
        formPanel.add(txtNom);
        
        formPanel.add(new JLabel("Prénom:"));
        txtPrenom = new JTextField();
        formPanel.add(txtPrenom);
        
        formPanel.add(new JLabel("Téléphone:"));
        txtTelephone = new JTextField();
        formPanel.add(txtTelephone);
        
        formPanel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        formPanel.add(txtEmail);
        
        formPanel.add(new JLabel("Mot de passe:"));
        txtMotDePasse = new JPasswordField();
        formPanel.add(txtMotDePasse);
        
        formPanel.add(new JLabel("Confirmer mot de passe:"));
        txtConfirmation = new JPasswordField();
        formPanel.add(txtConfirmation);
        
        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        
        btnInscrire = new JButton("S'INSCRIRE");
        btnInscrire.setBackground(new Color(102, 51, 153)); // Violet
        btnInscrire.setForeground(Color.WHITE);
        btnInscrire.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inscrireUtilisateur();
            }
        });
        
        btnAnnuler = new JButton("ANNULER");
        btnAnnuler.setBackground(Color.LIGHT_GRAY);
        btnAnnuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Revenir à l'écran de connexion
                loginframe loginFrame = new loginframe();
                loginFrame.setVisible(true);
                dispose();
            }
        });
        
        buttonPanel.add(btnInscrire);
        buttonPanel.add(btnAnnuler);
        
        // Ajouter les composants au panneau principal
        mainPanel.add(lblTitre, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Ajouter le panneau principal à la fenêtre
        add(mainPanel);
    }
    
    private void inscrireUtilisateur() {
        // Récupérer les valeurs saisies
        String cinStr = txtCin.getText().trim();
        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String telephone = txtTelephone.getText().trim();
        String email = txtEmail.getText().trim();
        String motDePasse = new String(txtMotDePasse.getPassword());
        String confirmation = new String(txtConfirmation.getPassword());
        
        // Valider les entrées
        if (cinStr.isEmpty() || nom.isEmpty() || prenom.isEmpty() || telephone.isEmpty() || 
            email.isEmpty() || motDePasse.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez remplir tous les champs obligatoires", 
                "Champs manquants", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Vérifier que le mot de passe et la confirmation sont identiques
        if (!motDePasse.equals(confirmation)) {
            JOptionPane.showMessageDialog(this, 
                "Le mot de passe et sa confirmation ne correspondent pas", 
                "Erreur de mot de passe", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Convertir CIN en entier
            int cin = Integer.parseInt(cinStr);
            
            // Créer l'objet utilisateur
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setCin(cin);
            utilisateur.setNom(nom);
            utilisateur.setPrenom(prenom);
            utilisateur.setTel(Integer.parseInt(telephone));
            utilisateur.setEmail(email);
            utilisateur.setMotDePasse(motDePasse);
            utilisateur.setTypeUtilisateur("client"); // Par défaut, le rôle est CLIENT
            
            // Enregistrer l'utilisateur dans la base de données
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
            boolean success = utilisateurDAO.inscrire(utilisateur);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Compte créé avec succès ! Vous pouvez maintenant vous connecter.", 
                    "Inscription réussie", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Revenir à l'écran de connexion
                loginframe loginFrame = new loginframe();
                loginFrame.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Erreur lors de la création du compte. Veuillez réessayer.", 
                    "Erreur d'inscription", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Le CIN doit être un nombre entier", 
                "Format invalide", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
