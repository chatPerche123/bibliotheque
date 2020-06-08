/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication22;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.sql.*;


/**
 *
 * @author Fabien
 */
public class Auteur {
    //VARIABLES MEMBRES
    protected int id_Auteur;
    protected String nom_Auteur;
    protected String prenom_Auteur;
    //Différents auteurs existants
    protected static Map<Integer, Auteur> mesAuteurs = new HashMap<Integer, Auteur>();
    Scanner sc = new Scanner(System.in);
    protected static String charset = "ISO-8859-1";

    
    //constructeurs
    public Auteur() {
    }
    public Auteur(String nom_Auteur, String prenom_Auteur) throws SQLException {                    
        this.nom_Auteur = nom_Auteur;
        this.prenom_Auteur = prenom_Auteur;
        
        dataGateway.ajouterAuteur(this);
        
        Auteur.mesAuteurs.put(this.id_Auteur, this);
    } 
    
    
    //Sous-menu gestion des auteurs
    protected void gestionAuteur() throws ParseException, SQLException {
        String reponse;
        
        while(true) {
            System.out.println("\t**Gestion des auteurs**");
            System.out.println("1) Ajout auteurs: ");
            System.out.println("2) Supprimer auteurs: ");
            System.out.println("3) Modifier infos auteurs: ");
            System.out.println("0) <== ");
            reponse = sc.next();
            
            switch(reponse) {
                case "0": 
                    //quitter
                    break;
                case "1":
                    this.creationAuteur();
                    break;
                case "2":
                    this.suppressionAuteur();
                    break;
                case "3":
                    this.modifierInfoAuteur();
                    break;
                default:
                    break;
            }
            
            if(reponse.equals("0")) {
                break;
            }
        }
    }
    
    
    protected void suppressionAuteur() throws SQLException {
        sc = new Scanner(System.in, charset);
        
        System.out.println("\tVeiller à fournir les informations ci-dessous:");
        while(true) {
            System.out.println("Nom de l'auteur à supprimer: ");   
            String nom_Auteur = sc.nextLine();
            
            System.out.println("Prenom de l'auteur à supprimer: ");
            String prenom_Auteur = sc.nextLine();
            
            System.out.println("ID de l'auteur à supprimer: ");
            int id_Auteur = Integer.parseInt(sc.nextLine());
            
            this.supprimerAuteur(nom_Auteur, prenom_Auteur, id_Auteur);
            
            System.out.println("Voulez-vous supprimer un autre auteur ? (oui/autre)");
            String rep = sc.nextLine().toLowerCase();
            
            if(!rep.equals("oui")) {
                break;
            }     
        }  
    }
    protected void supprimerAuteur(String auteur, String prenom, int id) throws SQLException {   
        boolean supressionEtat = false;
        for(Map.Entry<Integer, Auteur> entry : Auteur.mesAuteurs.entrySet()) {
            if(auteur.equals(entry.getValue().nom_Auteur) && prenom.equals(entry.getValue().prenom_Auteur) && id == entry.getValue().id_Auteur) {    
                //supprime auteur dans la BDD
                dataGateway.supprimerAuteur(entry.getValue());
                
                //supprime auteur dans le programme (Map)
                Auteur.mesAuteurs.remove(entry.getKey());
                System.out.println("Supression de l'auteur "+auteur);
                supressionEtat = true; 
                break;
            }
        } 
        //Auteur non trouvé
        if(!supressionEtat) {
            System.out.println("Supression impossible ("+auteur+" n'existe pas)");
        }
    }  
    
    //Initialise (verificationAuteur()) la création d'un auteur
    protected void creationAuteur() throws SQLException {
        sc = new Scanner(System.in, charset);
        
        System.out.println("\tVeiller à fournir les informations ci-dessous:");
        while(true) {
            this.verificationAuteur();   
            break;        
        } 
    }
    
    //Crée un auteur
    //Il peut avoir le nom et prénom qu'un auteur existant
    protected void verificationAuteur() throws SQLException {
        sc = new Scanner(System.in, charset);
        String reponse, auteurNom, prenomAuteur;
        
        while(true) {
            System.out.println("3) Nom auteur à ajouter: ");
            auteurNom = sc.nextLine();
            
            System.out.println("4) Prénom auteur à ajouter: ");
            prenomAuteur = sc.nextLine();
            
            //création auteur
            Auteur auteur = new Auteur(auteurNom, prenomAuteur);
            
            System.out.println("Voulez-vous rajouter un autre auteur ? (oui/autre)");
            reponse = sc.next().toLowerCase();
            
            if(!reponse.equals("oui")) {
                break;           
            }
            sc.nextLine();
        }
    }
   
    //Vérification de l'existance de l'auteur dans le programme (Map)
    protected static boolean auteurExiste_Auteur(String nom_Auteur, String prenom_Auteur, int id_Auteur) {        
        for (Map.Entry<Integer, Auteur> entry : Auteur.mesAuteurs.entrySet()) {
            if(nom_Auteur.equals(entry.getValue().nom_Auteur) && prenom_Auteur.equals(entry.getValue().prenom_Auteur) && 
                    id_Auteur == entry.getValue().id_Auteur) {
                System.out.println("L'auteur "+id_Auteur+" "+nom_Auteur+"-"+prenom_Auteur+" existe.");
                return true;
            }
        }
        
        System.out.println("L'auteur "+nom_Auteur+" n'existe pas.");  
        return false;
    }
    
    protected void modifierInfoAuteur() {
        sc = new Scanner(System.in, charset);
        
        System.out.println("\tVeiller à fournir les informations ci-dessous:");
        while(true) {
            System.out.println("Nom de l'auteur à modifier: ");   
            String nom_Auteur = sc.nextLine(); 
            
            System.out.println("Prénom de l'auteur à modifier: ");
            String prenom_Auteur = sc.nextLine();
            
            System.out.println("ID de l'auteur à modifier: ");
            int id_Auteur = Integer.parseInt(sc.nextLine());
            
            if(Auteur.auteurExiste_Auteur(nom_Auteur, prenom_Auteur, id_Auteur)) {
                System.out.println("Voulez-vous changer des informations (nom/prénom) sur cet auteur ? (oui/autre)");
                String rep = sc.nextLine().toLowerCase();
                
                if(!rep.equals("oui")) {
                    this.modifierAuteur(id_Auteur);  
                    
                }
            }
            
            System.out.println("Voulez-vous modifier un autre auteur ? (oui/autre)");
            String rep = sc.nextLine().toLowerCase();
               
            if(!rep.equals("non")) {
                break;
            }     
        }    
    }

    protected void modifierAuteur(int id_Auteur) {
        for(Map.Entry<Integer, Auteur> entry : Auteur.mesAuteurs.entrySet()) {
            if(entry.getValue().id_Auteur == id_Auteur) {
                System.out.println("Nouveau nom: ");
                entry.getValue().nom_Auteur = sc.nextLine();  
                
                System.out.println("Nouveau prénom: ");
                entry.getValue().prenom_Auteur = sc.nextLine();
                
                //Modifier auteur BDD
                dataGateway.modifierAuteur(entry.getValue());
                
                System.out.println("Modification ok");   
            }
        }
   }
     
    //Liste les différents auteurs dans le programme
    protected static void listeAuteur() {
        if(Auteur.mesAuteurs.isEmpty()) {
            System.out.println("Liste vide\n");
        }else {
            String result = "";

            for(Map.Entry<Integer, Auteur> entry : Auteur.mesAuteurs.entrySet()) {
                result += entry.getValue().id_Auteur+" | "+ entry.getValue().nom_Auteur+" "+entry.getValue().prenom_Auteur+"\n";
            }
            
            System.out.println("Liste des auteurs enregistrés: \nID| Nom\n"+result);
        }
    }
    
    //Setters && Getters
    protected static Auteur getAuteurOeuvre(String auteurOeuvre) {
        for (Map.Entry<Integer, Auteur> entry : Auteur.mesAuteurs.entrySet()) {
            if(auteurOeuvre.equals(entry.getValue().nom_Auteur)) {
                return entry.getValue();
            }
        }
        
        return null;
    }
    protected static Auteur getAuteur(int id) {
        for (Map.Entry<Integer, Auteur> entry : Auteur.mesAuteurs.entrySet()) {
            if(id == entry.getValue().id_Auteur) {
                return entry.getValue();
            }
        }
        System.out.println("Pas ok");
        return null;    
    }
    protected void setAuteurNom(String nom_Auteur) {
        this.nom_Auteur = nom_Auteur;
    }
    protected String getAuteurNom() {  
        return this.nom_Auteur;
    }
    
}
