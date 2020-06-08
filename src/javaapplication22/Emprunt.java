/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication22;

import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Fabien
 */
public class Emprunt {
    //utilitaires
    protected static String charset = "ISO-8859-1";
    Scanner sc = new Scanner(System.in, charset);
    //variables membres
    protected int id_Emprunt;
    protected int id_Abonne;
    protected int id_Centre;
    protected int isbn_Livre;
    protected LocalDate dateEmprunt;
    protected LocalDate dateRetours;
    //+15j
    protected LocalDate dateLimite;
    //liste des emprunts
    protected static ArrayList<Emprunt> liste_Emprunt = new ArrayList<>();
    
    //constructeur(s)
    public Emprunt() {   
    }
    public Emprunt(int id_Abonne, int id_Centre, int isbn_Livre, LocalDate dateEmprunt, LocalDate dateRetours) {  
        this.id_Emprunt = 0;
        this.id_Abonne = id_Abonne;
        this.id_Centre = id_Centre;
        this.isbn_Livre = isbn_Livre;
        this.dateEmprunt = dateEmprunt;
        this.dateRetours = dateRetours;
        //date de l'emprunt + 15j
        this.dateLimite = dateEmprunt.plusDays(15);
    }
    
    //Sous-menu gestion des emprunts
    protected void gestionEmprunt() throws ParseException, SQLException {
        sc = new Scanner(System.in, charset);
        String reponse;
        
        while(true) {
            System.out.println("\t**Gestion des Emprunts**");
            System.out.println("1) Générer un emprunt: ");
            System.out.println("2) Supprimer emprunt: ");
            System.out.println("3) Liste des emprunts en retard: ");
            System.out.println("4) Rendre emprunt: ");
            System.out.println("0) <== ");
            reponse = sc.next();
            
            switch(reponse) {
                case "0": 
                    //quitter
                    break;
                case "1":
                    //Création emprunt
                    this.creationEmprunt();
                    break;
                case "2":
                    //supprimer emprunt
                    this.supprimerEmprunt();
                    break;
                case "3":
                    //liste des emprunts en retard
                    this.listeEmpruntEnRetard();
                    break;
                case "4":
                    //terminer un emprunt
                    this.rendreEmprunt();
                default:
                    break;
            }
            
            if(reponse.equals("0")) {
                break;
            }
        }
    }
    
    protected void rendreEmprunt() throws SQLException {
        sc = new Scanner(System.in, charset);
        //affiche la liste des emprunts pour plus de commodité
        Emprunt.listeEmprunt();
        System.out.println("L'emprunt suivant sera terminé.");
        System.out.println("ID de l'emprunt: ");
        int id_Emprunt = Integer.parseInt(sc.nextLine());
        
        //Si l'emprunt existe
        if(Emprunt.verifierEmpruntExistID(id_Emprunt)) {
            //ajout date retours (null => nouvelle valeur)
            Emprunt.terminerEmpruntID(id_Emprunt);
        }
    }
    
    //LISTES//
    protected void listeEmpruntEnRetard() {
        System.out.println("Liste des emprunts en retard: ");
        String listeEmprunt = "";
        
        for(Emprunt emprunt : Emprunt.liste_Emprunt) {
            int var = emprunt.dateLimite.compareTo(LocalDate.now());
            //System.out.println("Jours de retard: "+var);
            //pour tester, il faut modifier la date d'emprunt pour avoir +15j par rapport à la date d'aujourd'hui
            if(emprunt.dateRetours == null && var < 0) {
                listeEmprunt = "ID emprunt: "+emprunt.id_Emprunt+" isbn libre: "+emprunt.isbn_Livre+" titre livre"+Livre.getTitreLivre(var)
                        +" ID abonne"+emprunt.id_Abonne+" Info abonne: "+Abonne.getInfoAbonne(emprunt.id_Abonne)+" Jours de retard: "+var+"\n";
            }
        }
        System.out.println(listeEmprunt);
    }
    protected static void listeEmpruntCentre(int id_Centre) {
        for(Emprunt emprunt : Emprunt.liste_Emprunt) {
            if(emprunt.id_Centre == id_Centre) {
                System.out.println("ID abonné: "+emprunt.id_Abonne+"("+Abonne.getInfoAbonne(emprunt.id_Abonne)+") "
                        + "ID centre: "+emprunt.id_Centre+"("+Centre.getInfoCentre(emprunt.id_Centre)+")"
                        + " ID emprunt "+emprunt.id_Emprunt+" ISBN: "+emprunt.isbn_Livre+" ("+Livre.getTitreLivre(emprunt.isbn_Livre)+")");
            
            }
        }
    }
    protected static void listeEmpruntAbonne(int id_Abonne) {
        System.out.println("SIZE "+Emprunt.liste_Emprunt.size());
        for(Emprunt emprunt : Emprunt.liste_Emprunt) {
            if(emprunt.id_Abonne == id_Abonne) {
                System.out.println("ID abonné: "+emprunt.id_Abonne+"("+Abonne.getInfoAbonne(emprunt.id_Abonne)+") "
                        + "ID centre: "+emprunt.id_Centre+"("+Centre.getInfoCentre(emprunt.id_Centre)+")"
                        + " ID emprunt "+emprunt.id_Emprunt+" ISBN: "+emprunt.isbn_Livre+" ("+Livre.getTitreLivre(emprunt.isbn_Livre)+")");
            
            }
        }
    }
    protected static void listeEmprunt() {
        System.out.println("SIZE "+Emprunt.liste_Emprunt.size());
        for(Emprunt emprunt : Emprunt.liste_Emprunt) {
            System.out.println("ID abonne("+emprunt.id_Abonne+" "+Abonne.getInfoAbonne(emprunt.id_Abonne)+")"
                    + " ID centre("+emprunt.id_Centre+" "+Centre.getInfoCentre(emprunt.id_Centre)+")"
                    + " ID emprunt("+emprunt.id_Emprunt+") ISBN("+emprunt.isbn_Livre+" "+Livre.getTitreLivre(emprunt.isbn_Livre)+")");
              
        }
    }
    
    //Vérifie existance emprunt avec 3 paramètres
    protected static boolean verifierEmpruntExist(int id_Abonne, int id_Centre, int isbn_Livre) {
        for(Emprunt emprunt : Emprunt.liste_Emprunt) {
            if(emprunt.id_Abonne == id_Abonne && emprunt.id_Centre == id_Centre && emprunt.isbn_Livre == isbn_Livre) {
                System.out.println("L'emprunt existe");
                return true;
            }
        }
        System.out.println("L'emprunt n'existe pas déjà");
        return false;
    }
    //Vérifie existance emprunt avec 1 paramètre
    protected static boolean verifierEmpruntExistID(int id_Emprunt) {
        for(Emprunt emprunt : Emprunt.liste_Emprunt) {
            if(emprunt.id_Emprunt == id_Emprunt) {
                System.out.println("L'emprunt existe");
                return true;
            }
        }
        System.out.println("L'emprunt n'existe pas déjà");
        return false;
    }
    //Trouve et applique une date de retours à un emprunt
    protected static void terminerEmpruntID(int id_Emprunt) throws SQLException {
        for(Emprunt emprunt : Emprunt.liste_Emprunt) {
            if(emprunt.id_Emprunt == id_Emprunt) {
                //La date de retours est celle de maintenant
                emprunt.dateRetours = LocalDate.now();
                //modifie date retours dans BDD
                dataGateway.terminerEmprunt(emprunt);
            }
        }
    }
    //Vérifie les délais des emprunts d'un abonné
    protected static boolean verifierEmpruntDelait(int id_Abonne) {
        int var = 0;
        for(Emprunt emprunt : Emprunt.liste_Emprunt) {
            if(emprunt.id_Abonne == id_Abonne) {
                System.out.println("La date limite: "+emprunt.dateLimite);
                System.out.println("La date d'aujourd'hui: "+LocalDate.now());
                System.out.println("La date de l'emprunt: "+emprunt.dateEmprunt);
                //Pour tester  var = emprunt.dateLimite.compareTo(LocalDate.now().plusDays(16));
                var = emprunt.dateLimite.compareTo(LocalDate.now());
                //si ma date limite est plus avancée de temps de jours que la date d'aujourd'hui alors il y a de la marge
                if(emprunt.dateRetours != null || var > 0 ){
                    System.out.println("Il reste encore "+var+" jours");
                    return true;
                }
                System.out.println("L'emprunter est en retard dans la remise d'un livre. Impossible d'en emprunter un nouveau");
                System.out.println("Il est en retard de "+var);
                return false;
            }
        }
        
        System.out.println("Encore aucun emprunt");
        return true;   
    }
    //supprime un emprunt
    protected static void supprimerEmprunt(int id_Emprunt) {
        for(Emprunt emprunt : Emprunt.liste_Emprunt) {
            if(emprunt.id_Emprunt == id_Emprunt) {
                System.out.println("suppression emprunt");
                Emprunt.liste_Emprunt.remove(emprunt);
                break;
            }
        }
    }
    //initialise la suppression d'un emprunt
    protected void supprimerEmprunt() {
        sc = new Scanner(System.in, charset);
        
        System.out.println("\tVeiller à fournir les informations ci-dessous:");
        while(true) {
            //Afficher la liste des emprunts
            Emprunt.listeEmprunt();
            
            System.out.println("ID de l'emprunt: ");
            int id_Emprunt = Integer.parseInt(sc.nextLine());
            
            boolean bool = Emprunt.verifierEmpruntExistID(id_Emprunt);
            
            if(bool) {
                Emprunt.supprimerEmprunt(id_Emprunt);
            }
            
            System.out.println("Voulez-vous supprimer un autre emprunt ? (oui/non)");
            String rep = sc.nextLine().toLowerCase();

            if(rep.equals("non")) {
                break;
            }   
        }
    }
    //Création d'un emprunt
    protected void creationEmprunt() {
        sc = new Scanner(System.in, charset);
        
        System.out.println("\tVeiller à fournir les informations ci-dessous:");
        while(true) {
            System.out.println("ID de l'abonné empruntant: ");
            int id_Abonne = Integer.parseInt(sc.nextLine());
            System.out.println("ID du centre de l'emprunt: ");
            int id_Centre = Integer.parseInt(sc.nextLine());
                        
            
            //vérifier si l'abonné est bien inscrit à ce centre
            //vérifier si l'abonné et qu'il a un nb d'empruntPossible >0
            boolean bool = Centre.abonneCentreExisteBool(id_Abonne, id_Centre);
            
            if(bool) {
                System.out.println("ID du Livre a emprunter: ");
                int isbn_Livre = Integer.parseInt(sc.nextLine());
                
                //vérifier si le livre est présent au centre 
                //vérifier qu'il est disponible en quantité suffisante
                bool = Centre.livreCentreExisteBool(isbn_Livre, id_Centre);
                if(bool) {
                    //vérifier si l'emprunt n'existe pas déjà
                    bool = Emprunt.verifierEmpruntExist(id_Abonne, id_Centre, isbn_Livre);   
                    if(bool == false) {
                        //faire l'emprunt
                        
                        //vérifier si l'emprunteur ne doit pas déjà rendre un livre
                        bool = Emprunt.verifierEmpruntDelait(id_Abonne);
                        if(bool) {
                            LocalDate dateEmprunt  = LocalDate.now();
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                            String text = dateEmprunt.format(formatter);
                            System.out.println("Date emprunt (JJ/MM/AAAA): "+text);
                            dateEmprunt = LocalDate.parse(text, formatter);
                            
                            //Null par défaut tant que non rendu
                            LocalDate dateRetours = null;

                            System.out.println("RAPPEL: \nVous avez 15j pour rendre un livre");

                            Emprunt emprunt = new Emprunt(id_Abonne, id_Centre, isbn_Livre, dateEmprunt, dateRetours);
                            System.out.println("ID_Abonne "+emprunt.id_Abonne);
                            System.out.println("id_Centre "+emprunt.id_Centre);
                            System.out.println("id_Livre "+emprunt.isbn_Livre);
                            System.out.println("dateEmprunt "+emprunt.dateEmprunt);
                            System.out.println("dateRetours "+emprunt.dateRetours); 

                            //Ajout emprunt BDD
                            dataGateway.ajouterEmprunt(emprunt);
                            //Ajout emprunt programme
                            Emprunt.liste_Emprunt.add(emprunt);
                        }
                    }      
                }
            }
            
            System.out.println("Voulez-vous Ajouter un autre emprunt ? (oui/non)");
            String rep = sc.nextLine().toLowerCase();
            
            if(rep.equals("non")) {
                break;
            }     
        }  
    }    
}