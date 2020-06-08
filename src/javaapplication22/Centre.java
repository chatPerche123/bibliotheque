
package javaapplication22;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Centre {
    //VARIABLES MEMBRES
    //un centre possède des abonnés et des livres
    protected ArrayList<Livre> listeLivre_Centre;
    protected ArrayList<Abonne> listeAbonne_Centre;
    //Liste des centres existants
    protected static Map<Integer, Centre> mesCentres = new HashMap<Integer, Centre>();
    
    protected int id_Centre;
    protected String nom_Centre;
    protected String adresse_Centre;
    
    //utilitaires
    Scanner sc = new Scanner(System.in, charset);
    protected static String charset = "ISO-8859-1";
    
    //Constructeur(s)
    public Centre() {
        this.id_Centre = 0;
        this.nom_Centre = "";
        this.adresse_Centre = "";
        
        this.listeLivre_Centre = new ArrayList<>();
        this.listeAbonne_Centre = new ArrayList<>();
    }
    public Centre(String nom_Centre, String adresse_Centre) throws SQLException {
        this.nom_Centre = nom_Centre;
        this.adresse_Centre = adresse_Centre;

        //initialisation des listes
        this.listeLivre_Centre = new ArrayList<>();
        this.listeAbonne_Centre = new ArrayList<>();
        
        //Ajoute centre à la BDD
        dataGateway.ajouterCentre(this);
        //Ajoute centre à la liste dans le programme
        Centre.mesCentres.put(id_Centre, this);
    }
    
    //La liste des abonnés d'un centre
    protected static void listeAbonneCentre(int id_Centre) {
        boolean bool = false;
        for(Map.Entry<Integer, Centre> entry : Centre.mesCentres.entrySet()) {
            if(entry.getValue().id_Centre == id_Centre) {
                for(Abonne abonne : entry.getValue().listeAbonne_Centre) {
                    System.out.println(entry.getValue().nom_Centre+" : "+abonne.nom_Abonne+"-"+abonne.prenom_Abonne);
                    bool = true;
                }
            }
        }
        if(bool == false) {
            System.out.println("Le centre n'a pas d'abonné.");
        }
    }
    //Sous-menu gestion centre
    protected void gestionCentre() throws ParseException, SQLException {
        Scanner sc = new Scanner(System.in, charset);
        String reponse;
        
        while(true) {
            System.out.println("\t**Gestion des centres**");
            System.out.println("1) Ajout centre: ");
            System.out.println("2) Supprimer centre: ");
            System.out.println("3) Modifier infos centre: ");
            System.out.println("  ---------  ");
            System.out.println("0) <== ");
            reponse = sc.next();
            
            switch(reponse) {
                case "0": 
                    //quitter s-menu
                    break;
                case "1":
                    //création centre
                    this.creationCentre();
                    break;
                case "2":
                    //supression centre
                    this.suppressionCentre();
                    break;
                case "3":
                    //modifications infos centre
                    this.modifierInfoCentre();
                    break;
                default:
                    break;
            }
            
            if(reponse.equals("0")) {
                break;
            }
        }
    }
    //initialise la création d'un centre
    protected void creationCentre() throws SQLException {
        Scanner sc = new Scanner(System.in, charset);
        
        System.out.println("\tVeiller à fournir les informations ci-dessous:");
        while(true) {
            //vérifie info avant la création d'un centre
            this.verificationCentre();   
            break;        
        }
    }
    
    protected void suppressionCentre() {
        Scanner sc = new Scanner(System.in, charset);
        
        System.out.println("\tVeiller à fournir les informations ci-dessous:");
        while(true) {
            System.out.println("Nom du centre à supprimer: ");   
            String nom_Centre = sc.nextLine();
            
            this.supprimerCentre(nom_Centre);
            
            System.out.println("Voulez-vous supprimer un autre centre ? (oui/non)");
            String rep = sc.nextLine().toLowerCase();
            
            if(rep.equals("non")) {
                break;
            }     
        }  
    }
    //On vérifie si le centre existe et qu'un abonné spécifique s'y trouve et que celui-ci puisse emprunter
    protected static boolean abonneCentreExisteBool(int id_Abonne, int id_Centre) {
        String str = "Le centre n'existe pas";
        //Dans la liste des centres disponibles
        for(Map.Entry<Integer, Centre> entry : Centre.mesCentres.entrySet()) {
            //Si mon id correspond à celui d'un centre existant alors je poursuis 
            if(id_Centre == entry.getValue().id_Centre) {
                //Dans la liste des centres de l'abonne
                for(Abonne abonne : entry.getValue().listeAbonne_Centre) {
                    //si mon id abonne correspond à un dans la liste alors je poursuis
                    if(abonne.id_Abonne == id_Abonne) {
                        System.out.println("id abo ok");
                        //si son nombre d'emprunt est possible alors il pourra emprunter
                        if(abonne.nbEmpruntPossible > 0) {
                            System.out.println("nb emprunt ok");
                            return true;
                        }else {
                            System.out.println("Nombre d'emprunt possible trop bas");
                            return false;
                        }
                    }
                }
                System.out.println("L'abonné n'existe pas dans ce centre");
                return false;
            
            }
        }
        System.out.println("Le centre n'existe pas");
        return false;
    }
    //Vérifie que le centre existe et que celui-ci a bien un livre spécifique et en quantité suffisante
    protected static boolean livreCentreExisteBool(int isbn_Livre, int id_Centre) {
        String str = "";
        for(Map.Entry<Integer, Centre> entry : Centre.mesCentres.entrySet()) {
            if(id_Centre == entry.getValue().id_Centre) {
                for(Livre livre : entry.getValue().listeLivre_Centre) {
                    if(livre.isbn_Livre == isbn_Livre) {
                        if(livre.quantite > 0) {
                            System.out.println("Livre dans centre ok.");
                            return true;
                        }else {
                            System.out.println("Livre existe dans le centre mais en quantité insuffisante");
                            return false;
                        }
                        
                    }
                }

                System.out.println("Livre n'existe pas dans le centre");
                return false;
            }
        }
        System.out.println(str);
        return false;
    }
    //Gestion de modification info centre
    protected void modifierInfoCentre() {
        Scanner sc = new Scanner(System.in, charset);
        
        System.out.println("\tVeiller à fournir les informations ci-dessous:");
        while(true) {
            System.out.println("Nom du centre à modifier: ");   
            String nom_Centre = sc.nextLine(); 
            
            if(this.centreExiste(nom_Centre)) {
                System.out.println("Voulez-vous changer le nom de ce centre ? (oui/non)");
                String rep = sc.nextLine().toLowerCase();
                
                if(rep.equals("oui")) {
                    this.modifierNomCentre(nom_Centre);  
                }
                
                System.out.println("Voulez-vous changer l'adresse de ce centre ? (oui/non)");
                String rep2 = sc.nextLine().toLowerCase();
                
                if(rep2.equals("oui")) {
                    this.modifierAdresseCentre(nom_Centre);
                }

            }
            
            System.out.println("Voulez-vous modifier un autre centre ? (oui/non)");
            String rep = sc.next().toLowerCase();
    
            if(rep.equals("non")) {
                break;
            }     
        }    
    }
    //MODIFIER INFO CENTRE//
    protected void modifierNomCentre(String centre) {
        for(Map.Entry<Integer, Centre> entry : Centre.mesCentres.entrySet()) {
            if(entry.getValue().nom_Centre.equals(centre)) {
                System.out.println("Nouveau nom: ");
                entry.getValue().nom_Centre = sc.nextLine();  
                System.out.println("Modification ok");
                //Modifier nom centre BDD
                dataGateway.modifierNomCentre(entry.getValue());
            }
        }
    }
    protected void modifierAdresseCentre(String nom_Centre) {
        for(Map.Entry<Integer, Centre> entry : Centre.mesCentres.entrySet()) {
            if(entry.getValue().nom_Centre.equals(nom_Centre)) {
                System.out.println("Nouvelle adresse: ");
                entry.getValue().adresse_Centre = sc.nextLine();  
                System.out.println("Modification ok");
                //Modifier adresse centre BDD
                dataGateway.modifierAdresseCentre(entry.getValue());
            }
        }
    }
   
    //LISTES//
    protected void listeCentreDispo() {
        if(Centre.mesCentres.isEmpty()) {
            System.out.println("Liste vide\n");
        
        }else {
            System.out.println("Liste des centres disponible: ");
            for(Map.Entry<Integer, Centre> entry : Centre.mesCentres.entrySet()) {
                System.out.println("    => Centre: "+entry.getValue().nom_Centre + "  || Adresse: "+entry.getValue().adresse_Centre);
            }    
        }
    }
    protected static void listeLivreCentre(String nom_Centre) {
        //je vérifie tous mes centres
        for(Map.Entry<Integer, Centre> entry : Centre.mesCentres.entrySet()) {
            //si je trouve mon centre "nom_centre"
            System.out.println(entry.getValue().nom_Centre+" "+entry.getValue().id_Centre+" Votre  centre en parametre "+nom_Centre);
            if(entry.getValue().nom_Centre.equals(nom_Centre)) {
                System.out.println("nom centre egaux");
                System.out.println("Taille "+entry.getValue().listeLivre_Centre.size());
                for(Livre livre : entry.getValue().listeLivre_Centre) {
                    System.out.println(livre.getNom_Livre());      
                }
                break;
            }
            System.out.println("Le centre n'existe pas");
        }
        
        
    }
    //Validation et Création d'un centre
    protected void verificationCentre() throws SQLException {
        Scanner sc = new Scanner(System.in, charset);
        String reponse, centreNom, adresseCentre;
        
        while(true) {
            System.out.println("3) Centre à ajouter: ");
            centreNom = sc.nextLine();
            
            boolean centreBool = this.ajoutCentre(centreNom);
            
            if(centreBool) {
                System.out.println("Création du centre ok");                
                System.out.println("Adresse du centre: ");
                adresseCentre = sc.nextLine();
                //sc.nextLine();
                System.out.println("Nom centre: "+centreNom+" adresse centre: "+adresseCentre);
                Centre centre = new Centre(centreNom, adresseCentre);
            }else {
                System.out.println("Création du centre pas ok");
            }
            
            System.out.println("Voulez-vous rajouter un autre centre ? (oui/non)");
            reponse = sc.next().toLowerCase();
            
            if(reponse.equals("non")) {
                break;           
            }
            sc.nextLine();
        }
    } 
    //fait appel à la vérification de l'existance d'un centre (CentreExiste(nom_Centre))   
    protected boolean ajoutCentre(String centre) {
        boolean ajoutPossible = this.centreExiste(centre);
        
        System.out.println(ajoutPossible);
        if(ajoutPossible == false) {
            System.out.println("Le centre n'éxiste pas encore: ajout ok");   
            return true;
            
        }else {
            System.out.println("Ajout impossible car le centre existe déjà");
            return false;
        }
    }      
    //(Bool) Vérifie si le centre existe dans la liste des centres avec son nom
    protected boolean centreExiste(String nom_Centre) {        
        for (Map.Entry<Integer, Centre> entry : Centre.mesCentres.entrySet()) {
            if(nom_Centre.equals(entry.getValue().nom_Centre)) {
                return true;
            }
        }
        
        System.out.println("Le centre "+nom_Centre+" n'existe pas.");  
        return false;
    }
    //(Bool) Vérifie si le centre existe dans la liste des centres avec son ID
    protected static boolean centreExiste(int id_Centre) {        
        for (Map.Entry<Integer, Centre> entry : Centre.mesCentres.entrySet()) {
            if(id_Centre == entry.getValue().id_Centre) {
                return true;
            }
        }
        return false;
    }
    //Supprime un centre dans le programme et la BDD
    protected void supprimerCentre(String centre) {   
        System.out.println("SupprimerCentre(): "+centre);
        boolean supressionEtat = false;
        for (Map.Entry<Integer, Centre> entry : Centre.mesCentres.entrySet()) {
            if(centre.equals(entry.getValue().nom_Centre)) {
                //Supprime le centre dans la BDD
                dataGateway.supprimerCentre(entry.getValue());
                //Supprime le centre dans le programme (Map)
                Centre.mesCentres.remove(entry.getKey());
                System.out.println("Supression du centre "+centre);
                supressionEtat = true;
                break;
            }
        } 
        if(!supressionEtat) {
            System.out.println("Supression impossible ("+centre+" n'existe pas)");
        }
    }
    
    //Retourne un objet centre à partir de son nom avec une F.static
    protected static Centre centreExiste_Centre(String nom_Centre) {        
        for (Map.Entry<Integer, Centre> entry : Centre.mesCentres.entrySet()) {
            if(nom_Centre.equals(entry.getValue().nom_Centre)) {
                return entry.getValue();
            }
        }
        
        System.out.println("Le centre "+nom_Centre+" n'existe pas.");  
        return null;
    }
    //Retourne un objet centre à partir de son id avec une F.static
    protected static Centre centreExisteId_Centre(int id_Centre) {        
        for (Map.Entry<Integer, Centre> entry : Centre.mesCentres.entrySet()) {
            if(id_Centre == entry.getValue().id_Centre) {
                return entry.getValue();
            }
        }
        
        System.out.println("Le centre "+id_Centre+" n'existe pas.");  
        return null;
    }
    
    protected void ajoutLivre_Centre(Livre livre) {
        this.listeLivre_Centre.add(livre);
    }
    protected void ajoutAbonne_Centre(Abonne abonne) {
        this.listeAbonne_Centre.add(abonne);
    }
    
    //GETTERS && SETTERS
    protected void setCentreNom(String nom_Centre) {
        this.nom_Centre = nom_Centre;
    }
    protected String getCentreNom() {
        if(this.nom_Centre.equals("")) {
            return "vide";
        }
        
        return this.nom_Centre;
    } 
    protected String getCentreAdresse() {
        if(this.adresse_Centre.equals("")) {
            return "vide";
        }
        
        return this.adresse_Centre;
    } 
    protected static String getInfoCentre(int id_Centre) {
        for (Map.Entry<Integer, Centre> entry : Centre.mesCentres.entrySet()) {
            if(id_Centre == entry.getValue().id_Centre) {

                return entry.getValue().nom_Centre;
            }
        }
        return "";
    }
    
}