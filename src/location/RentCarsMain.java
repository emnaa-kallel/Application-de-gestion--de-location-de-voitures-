package location;

import location.views.HomeFrame;

/**
 * Point d'entrée principal de l'application de location de voitures
 * @author sahar
 */
public class RentCarsMain {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Lancer l'interface d'accueil
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomeFrame().setVisible(true);
            }
        });
    }
}
