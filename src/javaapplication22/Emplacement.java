/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication22;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Fabien
 */
public class Emplacement {
    //variables membres
    protected int id_Emplacement;
    protected String nom_Emplacement;
    //liste des emplacements dispos
    protected static Map<Integer, Emplacement> mesEmplacements = new HashMap<Integer, Emplacement>();
    //utilitaires
    Scanner sc = new Scanner(System.in);
    protected static String charset = "ISO-8859-1";
    
    //Constructeur(s)
    public Emplacement(){
        
    }
    public Emplacement(String nom_Emplacement) throws SQLException {       
        this.nom_Emplacement = nom_Emplacement;
        //ajout emplacement BDD
        dataGateway.ajouterEmplacement(this);
        //ajout emplacement programme
        Emplacement.mesEmplacements.put(id_Emplacement, this);       
        System.out.println("Ajout emplacement "+this.nom_Emplacement);
    }
    
    protected void gestionEmplacement() throws ParseException, SQLException {
        sc = new Scanner(System.in, charset);
        String reponse = "";
        
        while(true) {
            System.out.println("\t**Gestion des emplacements**");
            System.out.println("1) Ajout emplacement: ");
            System.out.println("2) Supprimer emplacement: ");
            System.out.println("3) Modifier infos emplacement: ");
            System.out.println("0) <== ");
            reponse = sc.next();
            
            switch(reponse) {
                case "0": 
                    //quitter
                    break;
                case "1":
                    //création emplacement
                    this.creationEmplacement();
                    break;
                case "2":
                    //supression genre
                    this.suppressionEmplacement();
                    break;
                case "3":
                    //modifier info d'un emplacement
                    this.modifierInfoEmplacement();
                    break;
                default:
                    break;
            }
            
            if(reponse.equals("0")) {
                break;
            }
        }
    }
    //initialise la suppresion d'un emplacement
    protected void suppressionEmplacement() {
        sc = new Scanner(System.in, charset);
        
        System.out.println("\tVeiller à fournir les informations ci-dessous:");
        while(true) {
            System.out.println("Nom de l'emplacement à supprimer: ");   
            String nom_Emplacement = sc.nextLine();
            
            System.out.println("ID de l'emplacement à supprimer: ");
            int id_Emplacement = Integer.parseInt(sc.nextLine());
            
            //supprime un emplacement
            this.supprimerEmplacement(nom_Emplacement, id_Emplacement);
            
            System.out.println("Voulez-vous supprimer un autre emplacement ? (oui/non)");
            String rep = sc.nextLine().toLowerCase();
            
            if(rep.equals("non")) {
                break;
            }     
        }  
    }
    //supprime un emplacement à partir du nom et de l'ID
    protected void supprimerEmplacement(String emplacement, int id_Emplacement) {   
        System.out.println("SupprimerEmplacement(): "+emplacement);
        boolean supressionEtat = false;
        for (Map.Entry<Integer, Emplacement> entry : Emplacement.mesEmplacements.entrySet()) {
            if(emplacement.equals(entry.getValue().nom_Emplacement) && id_Emplacement == entry.getValue().id_Emplacement) {
                System.out.println("Emplacement "+emplacement+" égal "+entry.getValue().nom_Emplacement);
                //supprime emplacement programme
                Emplacement.mesEmplacements.remove(entry.getKey());
                System.out.println("Supression de l'emplacement "+emplacement);
                supressionEtat = true;
                
                //supprime emplacement BDD
                dataGateway.supprimerEmplacement(entry.getValue());
                break;
            }
        } 
        if(!supressionEtat) {
            System.out.println("Supression impossible ("+emplacement+" n'existe pas)");
        }
    }  
    //initialise création d'un emplacement
    protected void creationEmplacement() throws SQLException {
        sc = new Scanner(System.in, charset);
        
        System.out.println("\tVeiller à fournir les informations ci-dessous:");
        while(true) {
            this.verificationEmplacement();   
            break;        
        } 
    }
    //vérifie la validité et la création d'un emplacement
    protected void verificationEmplacement() throws SQLException {
        sc = new Scanner(System.in, charset);
        String reponse, emplacementNom;
        
        while(true) {
            System.out.println("3) Emplacement à ajouter: ");
            emplacementNom = sc.nextLine();
            
            boolean editeurBool = this.ajoutEmplacement(emplacementNom);
            
            if(editeurBool) {
                System.out.println("Création emplacement a réussi");                

                System.out.println("Nom emplacement: "+emplacementNom);
                Emplacement emplacement = new Emplacement(emplacementNom);
            }else {
                System.out.println("La création de l'emplacement a échouée");
            }
            
            System.out.println("Voulez-vous rajouter un autre emplacement ? (oui/non)");
            reponse = sc.next();
            
            if(reponse.equals("non")) {
                break;           
            }
            sc.nextLine();
        }
    }
    //Initialise l'ajout d'un emplacement en vérifiant d'abord s'il existe
    protected boolean ajoutEmplacement(String emplacement) {
        boolean ajoutPossible = Emplacement.emplacementExiste_Emplacement(emplacement);
        
        System.out.println(ajoutPossible);
        if(ajoutPossible == false) {
            System.out.println("L'emplacement n'éxiste pas encore: ajout ok");   
            return true;
            
        }else {
            System.out.println("Ajout impossible car l'emplacement existe déjà");
            return false;
        }
    } 
    
    //Vérifie l'existance de l'emplacement
    protected static boolean emplacementExiste_Emplacement(String nom_Emplacement) {        
        for (Map.Entry<Integer, Emplacement> entry :  Emplacement.mesEmplacements.entrySet()) {
            if(nom_Emplacement.equals(entry.getValue().nom_Emplacement)) {
                System.out.println("L'emplacement "+nom_Emplacement+" existe.");
                return true;
            }
        }
        
        System.out.println("L'emplacement "+nom_Emplacement+" n'existe pas.");  
        return false;
    }
    
    //Gestionnaire modification emplacement
    protected void modifierInfoEmplacement() {
        sc = new Scanner(System.in, charset);
        
        System.out.println("\tVeiller à fournir les informations ci-dessous:");
        while(true) {
            System.out.println("Nom de l'emplacement à modifier: ");   
            String nom_Emplacement = sc.nextLine(); 
            
            
            if(this.emplacementExiste_Emplacement(nom_Emplacement)) {
                System.out.println("Voulez-vous changer le nom de cet emplacement ? (oui/non)");
                String rep = sc.nextLine();
                
                if(rep.equals("oui")) {
                    this.modifierNomEmplacement(nom_Emplacement);  
                }
            }
            
            System.out.println("Voulez-vous modifier un autre emplacement ? (oui/non)");
            String rep = sc.next();
               
            if(rep.equals("non")) {
                break;
            }     
        }    
    }
    protected void modifierNomEmplacement(String nom_Emplacement) {
        for(Map.Entry<Integer, Emplacement> entry : Emplacement.mesEmplacements.entrySet()) {
            if(entry.getValue().nom_Emplacement.equals(nom_Emplacement)) {
                System.out.println("Nouveau nom: ");
                entry.getValue().nom_Emplacement = sc.nextLine();  
                System.out.println("Modification ok");
                
                dataGateway.modifierEmplacement(entry.getValue());
            }
        }
    }
     
    //Liste des emplacements existants
    protected static void listeEmplacement() {
        if( Emplacement.mesEmplacements.isEmpty()) {
            System.out.println("Liste vide\n");
        }else {           
            String result = "";
            for(Map.Entry<Integer, Emplacement> entry : Emplacement.mesEmplacements.entrySet()) {
                result += entry.getValue().id_Emplacement+" | "+ entry.getValue().nom_Emplacement+"\n";
            }
            System.out.println("ID| Nom\n"+result);
        }
    }
    //GETTERS && SETTERS
    protected static Emplacement getEmplacementOeuvre(String EmplacementOeuvre) {
        for (Map.Entry<Integer, Emplacement> entry : Emplacement.mesEmplacements.entrySet()) {
            if(EmplacementOeuvre.equals(entry.getValue().nom_Emplacement)) {
                return entry.getValue();
            }
        }
        
        return null;
    }   
    protected void setEmplacementNom(String nom_Emplacement) {
        this.nom_Emplacement = nom_Emplacement;
    }
    protected void setEmplacementId(String nom_Emplacement) {
        this.id_Emplacement = Emplacement.emplacementGetInfo_Emplacement(nom_Emplacement).id_Emplacement;
    }
    protected static Emplacement emplacementGetInfo_Emplacement(String nom_Editeur) {        
        for (Map.Entry<Integer, Emplacement> entry : Emplacement.mesEmplacements.entrySet()) {
            if(nom_Editeur.equals(entry.getValue().nom_Emplacement)) {
                return entry.getValue();
            }
        }
        return null;
    }
    protected String getEmplacementNom() {
        if(this.nom_Emplacement.equals("")) {
            return "vide";
        }
        
        return this.nom_Emplacement;
    }
}