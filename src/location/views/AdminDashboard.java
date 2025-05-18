/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package location.views;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import location.models.Personal;
import location.models.Utilisateur;


import java.sql.*;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import location.dao.ClientDAO;
import location.dao.UtilisateurDAO;
import location.dao.PersonnelDAO;
import location.dao.VoitureDAO;
import location.utils.DatabaseConnection;

/**
 *
 * @author emnak
 */

public class AdminDashboard extends javax.swing.JFrame {


    public AdminDashboard(Utilisateur u) {
        initComponents();
        jTableVoiture.getColumn("Action").setCellRenderer(new ButtonRenderer());
        jTableVoiture.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), jTableVoiture, "table1"));
        jTableVoiture.setRowHeight(35);

        jTable2.getColumn("Action").setCellRenderer(new ButtonRenderer());
        jTable2.getColumn("Action").setCellEditor(new ButtonEditor(new JCheckBox(), jTable2,"table2"));
        jTable2.setRowHeight(35);
        
        
        jTable3.getColumn("Action").setCellRenderer(new MultiButtonRenderer());
        jTable3.getColumn("Action").setCellEditor(new MultiButtonEditor(new JCheckBox(), jTable3, "table3"));
        jTable3.setRowHeight(35);
        jTable3.getColumn("Action").setPreferredWidth(600); 
        jTable3.setRowHeight(40);
        jTable3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // une seule ligne à la fois
        jTable3.setRowSelectionAllowed(true);                          // permettre la sélection de lignes
        jTable3.setColumnSelectionAllowed(false);                      // empêcher sélection des colonnes seules

        
        jTable4.getColumn("Action").setCellRenderer(new ButtonRenderer2());
        jTable4.getColumn("Action").setCellEditor(new ButtonEditor2(new JCheckBox(), jTable4,"table4"));
        jTable4.setRowHeight(35);
        
        this.loadInfos(u);
        
        jButton3ActionPerformed(null);
        jButton5ActionPerformed(null);
        jButton7ActionPerformed(null);
        jButton9ActionPerformed(null);
        jButton11ActionPerformed(null);
        
        
        
        
        

        

    }
    private void loadInfos(Utilisateur u){
        var cin = u.getCin();
        this.txt0.setText(Integer.toString(cin));
        this.txt1.setText(u.getNom());
        this.txt2.setText(u.getPrenom());
        this.txt3.setText(Integer.toString(u.getTel()));
        this.txt4.setText(u.getEmail());
        this.txt5.setText(u.getPassword());
        
    }

    private int getCin() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    // Rendu du bouton dans la cellule
class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setText("Modifier");
        setPreferredSize(new Dimension(100, 30));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}

class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private int currentRow;
    private JTable table;
    private String tableId; // Pour savoir quelle table a déclenché l'action

    public ButtonEditor(JCheckBox checkBox, JTable table, String tableId) {
    super(checkBox);
    this.table = table;
    this.tableId = tableId;

     button = new JButton("Modifier");
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(100, 30));

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();

                // Ouvre les frames selon l’ID de la table
                switch (tableId) {
                    case "table1":
                        var  mat = jTableVoiture.getValueAt(jTableVoiture.getSelectedRow(),0).toString();
                        var v = new VoitureDAO().trouverParImmatriculation(mat);
                        new voiture(v).setVisible(true);
                        break;
                    case "table2":
                        new employe().setVisible(true);
                        break;
                    
                    
                    
                    default:
                        JOptionPane.showMessageDialog(button, "Table inconnue : " + tableId);
                        break;
                }
            }
        });
    }
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        currentRow = row;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return "Modifier";
    }
}
//table4
class ButtonRenderer2 extends JButton implements TableCellRenderer {
    public ButtonRenderer2() {
        setText("Valider");
        setPreferredSize(new Dimension(100, 30));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}

class ButtonEditor2 extends DefaultCellEditor {
    protected JButton button;
    private int currentRow;
    private JTable table;
    private String tableId;

    public ButtonEditor2(JCheckBox checkBox, JTable table, String tableId) {
        super(checkBox);
        this.table = table;
        this.tableId = tableId;

        button = new JButton("Valider");
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(100, 30));

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();

                int selectedRow = table.getSelectedRow(); 
                if (selectedRow != -1) {
                    String datef = table.getValueAt(selectedRow, 4).toString();
                    String idloc = table.getValueAt(selectedRow, 0).toString();
                    String imm = table.getValueAt(selectedRow, 2).toString();

                    switch (tableId) {
                        case "table4":
                            new validerlocation(datef, idloc, imm).setVisible(true);
                            break;
                        default:
                            JOptionPane.showMessageDialog(button, "Table inconnue : " + tableId);
                            break;
                    }
                } else {
                    JOptionPane.showMessageDialog(button, "Veuillez sélectionner une ligne !");
                }
            }
        }); 
    } 

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        currentRow = row;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return "Valider";
    }
       
}



class MultiButtonRenderer extends JPanel implements TableCellRenderer {
    private JButton btnPenalite;
    private JButton btnLocation;
    private JButton btnSupprimer;

    public MultiButtonRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        setOpaque(true);
        

        btnPenalite = createRendererButton("Historique pénalité");
        btnLocation = createRendererButton("Historique location");
        btnSupprimer = createRendererButton("Supprimer");

        add(btnPenalite);
        add(btnLocation);
        add(btnSupprimer);
    }

    private JButton createRendererButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(140, 30));
        button.setFocusable(false);         // Empêche la sélection
        button.setRolloverEnabled(false);   // Pas d'effet hover
        button.setBorderPainted(false);     // Look plus simple
        return button;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}

class MultiButtonEditor extends DefaultCellEditor {
    private JPanel panel;
    private JButton btnPenalite;
    private JButton btnLocation;
    private JButton btnSupprimer;
    private JTable table;
    private String tableId;

    public MultiButtonEditor(JCheckBox checkBox, JTable table, String tableId) {
        super(checkBox);
        this.table = table;
        this.tableId = tableId;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));

        btnPenalite = new JButton("Historique pénalité");
        btnPenalite.setPreferredSize(new Dimension(80, 30));
        
        btnLocation = new JButton("Historique location");
        btnLocation.setPreferredSize(new Dimension(80, 30));
        
        btnSupprimer = new JButton("Supprimer");
        btnSupprimer.setPreferredSize(new Dimension(80, 30));
        

        panel.add(btnPenalite);
        panel.add(btnLocation);
        panel.add(btnSupprimer);

        // Action : Historique pénalité
        btnPenalite.addActionListener(e -> {
            int selectedRow = jTable3.getSelectedRow();
            if (selectedRow != -1) {
                    String cin = jTable3.getValueAt(selectedRow, 0).toString(); // colonne 0 = CIN
                    new historiquepenalite(cin).setVisible(true);
            }
            
            
            
        });

        // Action : Historique location
        btnLocation.addActionListener(e -> {
            int selectedRow = jTable3.getSelectedRow();
            if (selectedRow != -1) {
                    String cin = jTable3.getValueAt(selectedRow, 0).toString(); // colonne 0 = CIN
                    new historiquelocation(cin).setVisible(true);
            }
            
            
        });

        // Action : Supprimer
        btnSupprimer.addActionListener(e -> {
    try {
         jTable3.requestFocusInWindow();
        int selectedRow = jTable3.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(AdminDashboard.this, "Veuillez sélectionner une ligne à supprimer !");
            return;
        }

        int cin = Integer.parseInt(jTable3.getValueAt(selectedRow, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(
                AdminDashboard.this,
                "Êtes-vous sûr de vouloir supprimer ce client (CIN: " + cin + ") ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return; // annuler suppression
        }

        ClientDAO dao = new ClientDAO();
        boolean success = dao.supprimer(cin);

        if (success) {
            JOptionPane.showMessageDialog(AdminDashboard.this, "Client supprimé avec succès !");
            // Rafraîchir la table (exemple simple, adapter selon ton modèle de données)
            ((DefaultTableModel) jTable3.getModel()).removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(AdminDashboard.this, "Échec de la suppression du Client.");
        }

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(AdminDashboard.this, "Erreur de format : CIN invalide !");
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(AdminDashboard.this, "Erreur : " + ex.getMessage());
    }
});

    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
    }




    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        txt1 = new javax.swing.JTextField();
        txt2 = new javax.swing.JTextField();
        txt3 = new javax.swing.JTextField();
        txt4 = new javax.swing.JTextField();
        txt5 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txt0 = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableVoiture = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jButton11 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel1.setText("Bienvenu Admin ,");

        jLabel3.setText("Nom :");

        jLabel4.setText("Prénom :");

        jLabel5.setText("Téléphone :");

        jLabel6.setText("Email :");

        jLabel7.setText("Nouveau mot de passe :");

        jButton1.setText("Mettre à jour mon compte");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("CIN");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(61, 61, 61)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt3, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                            .addComponent(txt2, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                            .addComponent(txt4, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                            .addComponent(txt5)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 910, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 273, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(53, 53, 53)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt1, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                            .addComponent(txt0))))
                .addContainerGap(151, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt0, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt4, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt5, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addComponent(jButton1)
                .addGap(66, 66, 66))
        );

        jTabbedPane1.addTab("Mon Compte", jPanel1);

        jButton2.setText("Ajouter une voiture");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Rafraichir");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTableVoiture.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Immatriculatio", "Marque", "Modèle", "Année", "Etat", "Disponibilité", "Prix_jour", "Carburant", "Transmission", "date_debut_assurance", "date_expiration_assurance", "id_assurance", "Action"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTableVoiture);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1125, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(69, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Gestion Voitures", jPanel2);

        jButton4.setText("Ajouter un employé");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Rafraichir");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "CIN", "Nom", "Prénom", "Tel", "Email", "Action"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1010, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(115, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(65, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Gestion Employés", jPanel3);

        jButton7.setText("Rafraichir");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "CIN", "Nom", "Prénom", "Tel", "Email", "Adresse", "Date de naissance", "Permis de conduire", "Date expiration permis", "Action"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1125, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(328, 328, 328)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jButton7)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(74, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Gestion Clients", jPanel4);

        jButton9.setText("Rafraichir");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Id_Location", "CIN_Client", "Immatriculation", "Date Début", "Date fin", "date_retour", "mode_paiement", "statut", "Action"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable4);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1119, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(142, 142, 142)
                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jButton9)
                .addGap(37, 37, 37)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(56, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Gestion Locations", jPanel5);

        jButton11.setText("Rafraichir");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Id pénalité", "Id location", "mode_paiement", "etat_paiement"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable6.setEnabled(false);
        jScrollPane6.setViewportView(jTable6);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 1001, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(101, 101, 101)
                        .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(124, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jButton11)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(78, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Consultation Pénalités", jPanel7);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jTabbedPane1)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        new voitureajout().setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        new employeajout().setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed


            String sql = "SELECT id_penalite, id_location, mode_paiement,etat_paiement FROM Penalite";

    try ( Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
        // Connexion à Oracle
       
        ResultSet rs = ps.executeQuery();

        // Modèle pour JTable
        DefaultTableModel model = (DefaultTableModel) jTable6.getModel();
        model.setRowCount(0); // Nettoyer le tableau avant d'ajouter de nouvelles lignes

        // Remplir JTable
        while (rs.next()) {
            Object[] row = new Object[4];
            for (int i = 0; i < 4; i++) {
                row[i] = rs.getObject(i + 1);
            }
            model.addRow(row);
        }

        // Fermer connexions
        rs.close();
        ps.close();
        conn.close();

    } catch (Exception ex) {
        ex.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Erreur de connexion ou de chargement des données.");
    }
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        String url = "jdbc:oracle:thin:@localhost:1521:xe"; 
    String user = "C##emnaa";
    String password = "123456";
        String sql = "SELECT immatriculation, marque, modele, annee, etat, disponibilite, prix_jour, carburant, transmission,date_debut_assurance, date_expiration_assurance, id_assurance FROM Voiture";

     try ( Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
        // Requête SQL
       
        ResultSet rs = ps.executeQuery();

        // Modèle pour JTable
        DefaultTableModel model = (DefaultTableModel) jTableVoiture.getModel();
        model.setRowCount(0); // Nettoyer le tableau avant d'ajouter de nouvelles lignes

        // Remplir JTable
        while (rs.next()) {
            Object[] row = new Object[12]; // 5 colonnes
            for (int i = 0; i < 12; i++) {
                row[i] = rs.getObject(i + 1);
            }
            model.addRow(row);
        }

        // Fermer connexions
        rs.close();
        ps.close();
        conn.close();

    } catch (Exception ex) {
        ex.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Erreur de connexion ou de chargement des données.");
    }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
         String url = "jdbc:oracle:thin:@localhost:1521:xe"; // À adapter à ton cas
    String user = "C##emnaa";
    String password = "123456";
String sql = "SELECT p.CIN, u.nom, u.prenom, u.tel, u.email " +
             "FROM Utilisateur u " +
             "JOIN Personnel p ON u.CIN = p.CIN " +
             "WHERE p.role = 'employe'";

    try ( Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
        
        // Requête SQL
        
        ResultSet rs = ps.executeQuery();

        // Modèle pour JTable
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0); // Nettoyer le tableau avant d'ajouter de nouvelles lignes

        // Remplir JTable
        while (rs.next()) {
            Object[] row = new Object[5]; // 5 colonnes
            for (int i = 0; i < 5; i++) {
                row[i] = rs.getObject(i + 1);
            }
            model.addRow(row);
        }

        // Fermer connexions
        rs.close();
        ps.close();
        conn.close();

    } catch (Exception ex) {
        ex.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Erreur de connexion ou de chargement des données.");
    }
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
            String url = "jdbc:oracle:thin:@localhost:1521:xe"; // Ton URL de connexion à Oracle
    String user = "C##emnaa"; // Ton utilisateur Oracle
    String password = "123456"; // Ton mot de passe Oracle

    String sql =  "SELECT c.CIN, nom, prenom, tel, email, adresse, date_naissance, permis_conduire, date_expiration_permis " +
    "FROM Utilisateur u " +
    "JOIN Client c ON u.CIN = c.CIN " + 
    "WHERE u.type_utilisateur = 'client'";
    // Utilisation de try-with-resources pour fermer automatiquement les ressources
    try ( Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {

        ResultSet rs = ps.executeQuery();
        // Modèle pour JTable
        DefaultTableModel model = (DefaultTableModel) jTable3.getModel();
        model.setRowCount(0); // Nettoyer le tableau avant d'ajouter de nouvelles lignes

        // Remplir JTable
        while (rs.next()) {
            Object[] row = new Object[9]; // On a 9 colonnes à remplir
            row[0] = rs.getObject("CIN");
            row[1] = rs.getObject("nom");
            row[2] = rs.getObject("prenom");
            row[3] = rs.getObject("tel");
            row[4] = rs.getObject("email");
            row[5] = rs.getObject("adresse");
            row[6] = rs.getObject("date_naissance");
            row[7] = rs.getObject("permis_conduire");
            row[8] = rs.getObject("date_expiration_permis");
            
            model.addRow(row);
        }

    } catch (Exception ex) {
        ex.printStackTrace();  // Affichage de l'exception pour le débogage
        javax.swing.JOptionPane.showMessageDialog(this, 
            "Erreur de connexion ou de chargement des données : " + ex.getMessage()
        );
    }
        
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        String url = "jdbc:oracle:thin:@localhost:1521:xe"; // À adapter à ton cas
    String user = "C##emnaa";
    String password = "123456";
        String sql = "SELECT id_location, CIN_client, immatriculation, date_debut, date_fin ,date_retour,mode_paiement,statut FROM Location";

    try ( Connection conn = DatabaseConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)) {
        
        // Requête SQL

        ResultSet rs = ps.executeQuery();

        // Modèle pour JTable
        DefaultTableModel model = (DefaultTableModel) jTable4.getModel();
        model.setRowCount(0); // Nettoyer le tableau avant d'ajouter de nouvelles lignes

        // Remplir JTable
        while (rs.next()) {
            Object[] row = new Object[8]; // 5 colonnes
            for (int i = 0; i < 8; i++) {
                row[i] = rs.getObject(i + 1);
            }
            model.addRow(row);
        }

        // Fermer connexions
        rs.close();
        ps.close();
        conn.close();

    } catch (Exception ex) {
        ex.printStackTrace();
        javax.swing.JOptionPane.showMessageDialog(this, "Erreur de connexion ou de chargement des données.");
    }
        
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
          try {
        
        int cin = Integer.parseInt(txt0.getText());
        String nom = txt1.getText();
        String prenom = txt2.getText();
        int tel = Integer.parseInt(txt3.getText());
        String email = txt4.getText();
        String mdp = txt5.getText();
        
        Utilisateur personnel = new Utilisateur();
        personnel.setCin(cin);
        personnel.setNom(nom);
        personnel.setPrenom(prenom);
        personnel.setTel(tel);
        personnel.setEmail(email);
        personnel.setMotDePasse(mdp);

        UtilisateurDAO dao = new UtilisateurDAO();
        boolean success = dao.modifcompte(personnel);

        if (success) {
            JOptionPane.showMessageDialog(this, "Compte modifié avec succès !");
        } else {
            JOptionPane.showMessageDialog(this, "Échec de la modification du compte.");
        }

    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Erreur de format : CIN ou salaire invalide !");
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
    }

       
     
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                var admin = new UtilisateurDAO().listerTous().stream().filter(l -> new PersonnelDAO().estAdmin(l.getCin())).findFirst().get();
                new AdminDashboard(admin).setVisible(true);
            }
        });
    }

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTableVoiture;
    private javax.swing.JTextField txt0;
    private javax.swing.JTextField txt1;
    private javax.swing.JTextField txt2;
    private javax.swing.JTextField txt3;
    private javax.swing.JTextField txt4;
    private javax.swing.JTextField txt5;
    // End of variables declaration//GEN-END:variables
}
