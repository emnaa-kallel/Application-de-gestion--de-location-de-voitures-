package location.views;

import location.dao.UtilisateurDAO;
import location.models.Utilisateur;
import javax.swing.*;
import java.awt.*;

public class AuthentificationFrame extends JFrame {

    public AuthentificationFrame() {
        setTitle("Authentification");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Créer les composants
        JLabel loginLabel = new JLabel("Login:");
        JTextField loginField = new JTextField(20);

        JLabel cinLabel = new JLabel("CIN:");
        JTextField cinField = new JTextField(20);

        JButton loginButton = new JButton("Se connecter");

        // Action du bouton de connexion
        loginButton.addActionListener(e -> {
            String login = loginField.getText().trim();
            String cinText = cinField.getText().trim();
            int CIN = -1;

            try {
                CIN = Integer.parseInt(cinText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Le CIN doit être un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (login.isEmpty() || cinText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            UtilisateurDAO utilisateurDAO = new UtilisateurDAO();
            Utilisateur utilisateur = utilisateurDAO.authentifier2(login, CIN);

            if (utilisateur != null) {
                String typeUtilisateur = utilisateur.getTypeUtilisateur();
                if ("client".equals(typeUtilisateur)) {
                    new ClientDashboard2(utilisateur).setVisible(true); // Interface Client
                } else if ("personnel".equals(typeUtilisateur)) {
                    String role = utilisateur.getRole();
                    if ("admin".equals(role)) {
                        // new AdminInterfaceFrame().setVisible(true); // Interface Admin
                    } else {
                        // new EmployeInterfaceFrame().setVisible(true); // Interface Employé
                    }
                }
                dispose(); // Ferme la fenêtre d'authentification
            } else {
                JOptionPane.showMessageDialog(this, "Login ou CIN incorrect", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Utilisation du GroupLayout pour organiser les composants de façon ordonnée
        JPanel panel = new JPanel();
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        // Alignement horizontal pour les composants
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addComponent(loginLabel)
                        .addComponent(cinLabel))
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(loginField)
                        .addComponent(cinField))
                )
                .addGroup(layout.createSequentialGroup()
                    .addComponent(loginButton)
                )
        );

        // Alignement vertical pour les composants
        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(loginLabel)
                    .addComponent(loginField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(cinLabel)
                    .addComponent(cinField))
                .addComponent(loginButton)
        );

        add(panel);
    }

    public static void main(String[] args) {
        new AuthentificationFrame().setVisible(true);
    }
}
