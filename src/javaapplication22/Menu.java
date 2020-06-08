
package javaapplication22;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;


public class Menu {
    //utilitaires
    protected static String charset = "ISO-8859-1";
    static Scanner sc = new Scanner(System.in, charset);
    
    
    //Menu application
    public static void monMenu() throws ParseException, SQLException {
        
        sc = new Scanner(System.in, charset);
        System.out.println("Bienvenue\n"); 
        
        //Connexion à la base de données
        dataGateway.conn = dataGateway.getConnexion();
        //Récupérer les données de la BDD et remplir les objets de l'application
        dataGateway.set_all_data();
        
        while(true) {
            System.out.println("-Administrateur: ");
            System.out.println("\t1) Gestion des livres: ");
            System.out.println("\t2) Gestion des genres: ");
            System.out.println("\t3) Gestion des emplacements: ");
            System.out.println("\t4) Gestion des centres: ");  
            System.out.println("\t5) Afficher listes: ");
            System.out.println("\t6) Gestions des éditeurs: ");
            System.out.println("\t7) Gestions des auteurs: ");
            System.out.println("\t8) Gestions des abonnées: ");
            System.out.println("\t9) Gestion des emprunts: ");
            System.out.println("\t0) [x]<== Quitter l'application");
            System.out.println("   Choissisez: ");
            String reponse = sc.next();
            
            switch(reponse) {              
                case "1":
                    Livre livre = new Livre();
                    livre.gestionLivre();
                    break;
                case "2":
                    GenreOeuvre.gestionGenre();
                    break;
                case "3":
                    Emplacement emplacement = new Emplacement();
                    emplacement.gestionEmplacement();
                    break;
                case "4":
                    Centre centre = new Centre(); 
                    centre.gestionCentre();
                    break;
                case "5":
                    Menu.afficherListes();
                    break;
                case "6":
                    Editeur editeur = new Editeur();
                    editeur.gestionEditeur();
                    break;
                case "7":
                    Auteur auteur = new Auteur();
                    auteur.gestionAuteur();
                    break;
                case "8":
                    Abonne abonne = new Abonne();
                    abonne.gestionAbonne();
                    break;
                case "9":
                    Emprunt emprunt = new Emprunt();
                    emprunt.gestionEmprunt();
                    break;
                default:
                    //System.out.println("default");
                    break;    
            }
 
            if(reponse.equals("0")) {
                break;
            }
        }
    }
    
    //Permet d'afficher différentes listes de l'application
    public static void afficherListes() throws ParseException {
        while(true) {
            System.out.println("Les différentes listes disponibles: ");
            System.out.println("\t1) Les bibliothèques");
            System.out.println("\t2) Les livres");
            System.out.println("\t3) Les emprunts");
            System.out.println("\t4) Les genres");
            System.out.println("\t5) Les centres");
            System.out.println("\t6) Les éditeurs");
            System.out.println("\t7) Les auteurs");
            System.out.println("\t8) Les emplacements");
            System.out.println("\t9) La liste des livres par centre");
            System.out.println("\t10) La liste des abonnés");
            System.out.println("\t11) La liste des abonnés par centre");
            System.out.println("\t12) La liste des emprunts d'un abonné");
            System.out.println("\t13) La liste des emprunts par centre");
            System.out.println("\t0) <== ");
            System.out.println("   Choissisez: ");
            String reponse = sc.nextLine();

            switch(reponse) {
                    case "0":
                        break;
                    case "1":
                        break;
                    case "2":
                        System.out.println("La liste des livres: ");
                        Livre.listeLivre();
                        break;
                    case "3":
                        System.out.println("La liste des emprunts: ");
                        Emprunt.listeEmprunt();
                        break;
                    case "4":
                        System.out.println("La liste des genres:");
                        GenreOeuvre.listeGenreDispo();
                        break; 
                    case "5":
                        System.out.println("La liste des centres: ");
                        Centre centre = new Centre();
                        centre.listeCentreDispo();
                        break;
                    case "6":
                        System.out.println("La liste des éditeurs: ");
                        Editeur.listeEditeurs();
                        break;
                    case "7":
                        System.out.println("La liste des auteurs: ");
                        Auteur.listeAuteur();
                        break;    
                    case "8":
                        System.out.println("La liste des emplacements: ");
                        Emplacement.listeEmplacement();
                        break;   
                    case "9":
                        System.out.println("Votre centre: ");
                        String rep = sc.nextLine();

                        System.out.println("La liste des livres par centre: ");
                        Centre.listeLivreCentre(rep);
                        break;
                    case "10":
                        System.out.println("La liste des abonnés: ");
                        Abonne.listeAbonne();
                        break; 
                    case "11":
                        System.out.println("ID de votre centre: ");
                        int id_Centre = Integer.parseInt(sc.nextLine());
                        
                        if(Centre.centreExiste(id_Centre)) {
                            Centre.listeAbonneCentre(id_Centre);
                            
                        }else {
                            System.out.println("Centre n'existe pas.");
                        }
                        break;
                    case "12":
                        System.out.println("ID abonné: ");
                        Emprunt.listeEmpruntAbonne(Integer.parseInt(sc.nextLine()));
                        break;
                                               
                    case "13":
                        System.out.println("ID de votre centre: ");
                        id_Centre = Integer.parseInt(sc.nextLine());
                        
                        if(Centre.centreExiste(id_Centre)) {
                            Emprunt.listeEmpruntCentre(id_Centre);
                        }else {
                            System.out.println("Centre n'existe pas.");
                        }
                        break;     
                        
                    default:
                        System.out.println("default");
                        break;
            }
            
            if(reponse.equals("0")) {
                break;
            }
        }
    }        
}
