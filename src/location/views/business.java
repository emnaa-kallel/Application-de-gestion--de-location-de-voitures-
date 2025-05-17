/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package location.views;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import location.dao.ClientDAO;
import location.dao.LocationDAO;
import location.dao.PaiementDAO;
import location.dao.PenaliteDAO;
import location.dao.PersonnelDAO;
import location.dao.UtilisateurDAO;
import location.dao.VoitureDAO;
import location.models.Client;
import location.models.Location;
import location.models.Paiement;
import location.models.Penalite;
import location.models.Personnel;
import location.models.Utilisateur;
import location.models.Voiture;

/**
 *
 * @author tawfik
 */
public class business {
    
    //client
    public void reserver(Voiture voiture, int cin, Date debut, Date fin, String mode_paiement ){
        
        var client = new ClientDAO().trouverParCIN(cin);
        var personnel = new PersonnelDAO().listerTous().get(0);
        voiture.setDisponibilite("louee");
        modifier(voiture);   
        var location = new Location();
        location.setClient(client);
        location.setClientCin(client.getCin());
        location.setVoiture(voiture);
        location.setDateDebut(debut);
        location.setDateFin(fin);
        location.setStatut("en_cours");
        location.setModePaiement(mode_paiement);
       
        location.setVoitureId(voiture.getImmatriculation());
        new LocationDAO().ajouter(location);
        
        //payer(location, mode_paiement);
        
    }
    
    public void modifier(Voiture voiture){
        var d = new VoitureDAO();
        d.modifier(voiture);
    }
    
    public void annuler(Voiture voiture){
        voiture.setDisponibilite("disponible");
        modifier(voiture);   
    }
    
    public void payer(Location location, String modePaiement){
        var paiement = new Paiement();
        paiement.setIdLocation(location.getId());
        paiement.setLocation(location);
          var localDate = LocalDate.now();
          Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        paiement.setDatePaiement(date );
        paiement.setModePaiement(modePaiement);
        //paiement.setMontant(location.getPrixTotal());
        paiement.setStatutPaiement("effectue");
        var d = new PaiementDAO();
       
        d.ajouter(paiement);
    }
    
    
    public void  modifierCompte(Utilisateur utilisateur, Client client){
        var du = new UtilisateurDAO();
        du.mettreAJour(utilisateur);
        var dc = new ClientDAO();
        dc.modifier(client);
    } 
    
    //Admin
    
    public void GererVoiture(Voiture voiture){
        modifier(voiture);
    }
    
    
    public void GererEmploye(Utilisateur utilisateur){
        modifierCompte(utilisateur, null);
    }
    
    public void Penalite(Penalite penalite){
        var d = new PenaliteDAO();
        d.ajouter(penalite);
        
    }
    
        public double getMontantPenalite(Penalite penalite){
            var loc = new LocationDAO().trouverParId(penalite.getIdLocation());
            
            
            var date1 =  loc.getDateFin();
                        var date2 =  loc.getDateRetour();

                        if(date1==null || date2 == null) return -1;
                            
                        var sb = date2.getTime() - date1.getTime();
                        if(sb<0) return 0;
    long daysBetween = TimeUnit.DAYS.convert(sb, TimeUnit.MILLISECONDS);

         if(daysBetween > 0){
             var voiture = new VoitureDAO().trouverParId(loc.getVoitureId());
                     var pjr = voiture.getPrixJour();

             return pjr * 1.2 * daysBetween;
         }
         return 0;
        }
    
    public void ModifierCompteAll(Utilisateur utilisateur, Personnel personnel){
        var du = new UtilisateurDAO();
        du.mettreAJour(utilisateur);
        var dc = new PersonnelDAO();
        dc.modifier(personnel);
    }
    
    //employer
    // comme admin  mais pas de gereremployee et pas de penalite
    
    
    public static void main(String args[]) {
        
    }
}
