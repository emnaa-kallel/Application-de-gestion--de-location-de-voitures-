/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package location.views;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import location.dao.VoitureDAO;
import location.models.Utilisateur;
import location.models.Voiture;


/**
 *
 * @author sahar
 */
public class ClientDashboard3 extends javax.swing.JFrame {

    /**
     * Creates new form ClientDashboard3
     */
    
    private VoitureDAO voitureDAO;
    private DefaultTableModel model;
    private Utilisateur utilisateur;

    public ClientDashboard3(Utilisateur user) {
        initComponents();
        this.voitureDAO = new VoitureDAO();
        this.utilisateur = user;

        // Créer un modèle basé sur les colonnes attendues
        String[] colonnes = {"Immatriculation", "Marque", "Modèle", "Année", "État", "Disponibilité", "Prix/Jour", "Carburant", "Transmission", "Action"};
        model = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 9; // Seule la colonne "Action" est éditable
            }
        };

        // Lier le modèle à la table jTable1
        jTable1.setModel(model);
        jTable1.setRowHeight(30);

        // Rendre la colonne d'action comme un bouton
        jTable1.getColumnModel().getColumn(9).setCellRenderer(new ButtonRenderer());
        jTable1.getColumnModel().getColumn(9).setCellEditor(new ButtonEditor(new JCheckBox()));

        // Charger les données
        chargerVoituresDisponibles();
    }

    private void chargerVoituresDisponibles() {
        model.setRowCount(0); // Vider la table

        List<Voiture> voitures = voitureDAO.listerTout();
        for (Voiture v : voitures) {
            String action = v.getDisponibilite().equalsIgnoreCase("Disponible") ? "Réserver" : null;

            model.addRow(new Object[]{
                v.getImmatriculation(),
                v.getMarque(),
                v.getModele(),
                v.getAnnee(),
                v.getEtat(),
                v.getDisponibilite(),
                v.getPrixJour() + " Dinars",
                v.getCarburant(),
                v.getTransmission(),
                action  // 10ème colonne : bouton ou rien
            });
        }
    }

    // Rendre le bouton dans la cellule
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            if (value != null) {
                setText(value.toString());
            } else {
                setText("");
            }
            return this;
        }
    }

    // Gérer l'action du bouton
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = jTable1.getSelectedRow();
                    if (selectedRow != -1) {
                        String dispo = (String) jTable1.getValueAt(selectedRow, 5);
                        if ("Disponible".equalsIgnoreCase(dispo)) {
                            String immatriculation = (String) jTable1.getValueAt(selectedRow, 0);
                            if (utilisateur != null) {
                                //new ReservationFrame(immatriculation, utilisateur).setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(null, "Veuillez vous connecter d'abord", "Erreur", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Cette voiture n'est pas disponible.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Veuillez sélectionner une voiture", "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            if (value != null) {
                button.setText(value.toString());
            }
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return button.getText();
        }
    }

    // Autres méthodes et initialisation de l'interface utilisateur...

    /*public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new ClientDashboard2(null).setVisible(true);
        });
    }*/
    public ClientDashboard3() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientDashboard3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientDashboard3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientDashboard3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientDashboard3.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientDashboard3().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify                     
    private javax.swing.JButton bDeconnexion;
    private javax.swing.JButton baffichertoutesvoitures;
    private javax.swing.JButton bexit;
    private javax.swing.JButton brechercher;
    private javax.swing.JButton bretour;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration                   
}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
