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
public class Editeur {
    //variables membres
    protected int id_Editeur;
    protected String nom_Editeur;
    //liste des éditeurs dispos
    protected static Map<Integer, Editeur> mesEditeurs = new HashMap<Integer, Editeur>();
    //utilitaire
    Scanner sc = new Scanner(System.in);
    protected static String charset = "ISO-8859-1";

    //Constructeur(s)
    public Editeur() {
    }
    public Editeur(String nom_Editeur) throws SQLException {
        //permet de commencer au même chiffre que l'id de la base de données
        this.nom_Editeur = nom_Editeur;
        //ajout éditeur BDD
        dataGateway.ajouterEditeur(this);   
        //ajout éditeur programme (Map)
        Editeur.mesEditeurs.put(id_Editeur, this); 
    }
    //Sous-menu gestion éditeur
    protected void gestionEditeur() throws ParseException, SQLException {
        Scanner sc = new Scanner(System.in, charset);
        String reponse;
        
        while(true) {
            System.out.println("\t**Gestion des éditeurs**");
            System.out.println("1) Ajout éditeurs: ");
            System.out.println("2) Supprimer éditeurs: ");
            System.out.println("3) Modifier infos éditeurs: ");
            System.out.println("0) <== ");
            reponse = sc.next();
            
            switch(reponse) {
                case "0": 
                    //quitter
                    break;
                case "1":
                    this.creationEditeur();
                    break;
                case "2":
                    this.suppressionEditeur();
                    break;
                case "3":
                    this.modifierInfoEditeur();
                    break;
                default:
                    break;
            }
            
            if(reponse.equals("0")) {
                break;
            }
        }
    }
    
    
    protected void suppressionEditeur() {
        sc = new Scanner(System.in, charset);
        
        System.out.println("\tVeiller à fournir les informations ci-dessous:");
        while(true) {
            System.out.println("Nom de l'éditeur à supprimer: ");   
            String nom_Editeur = sc.nextLine();
            
            System.out.println("ID de l'éditeur à supprimer: ");
            int id_Editeur = Integer.parseInt(sc.nextLine());
            
            this.supprimerEditeur(nom_Editeur, id_Editeur);
            
            System.out.println("Voulez-vous supprimer un autre Editeur ? (oui/non)");
            String rep = sc.nextLine().toLowerCase();
            
            if(rep.equals("non")) {
                break;
            }     
        }  
    }
    protected void supprimerEditeur(String editeur, int id_Editeur) {   
        boolean supressionEtat = false;
        for (Map.Entry<Integer, Editeur> entry : Editeur.mesEditeurs.entrySet()) {
            if(editeur.equals(entry.getValue().nom_Editeur) && id_Editeur == entry.getValue().id_Editeur) {
                //Supprime un éditeur dans le programme
                Editeur.mesEditeurs.remove(entry.getKey());
                System.out.println("Supression de l'éditeur "+editeur);
                supressionEtat = true;
                //Supprime un editeur dans la BDD
                dataGateway.supprimerEditeur(entry.getValue());
                break;
            }
        } 
        if(!supressionEtat) {
            System.out.println("Supression impossible ("+editeur+" n'existe pas)");
        }
    }  
    //Initialise la création d'un éditeur
    protected void creationEditeur() throws SQLException {
        sc = new Scanner(System.in, charset);
        
        System.out.println("\tVeiller à fournir les informations ci-dessous:");
        while(true) {
            this.verificationEditeur();   
            break;        
        } 
    }
    //Gestion vérification puis création éditeur
    protected void verificationEditeur() throws SQLException {
        sc = new Scanner(System.in, charset);
        String reponse, editeurNom;
        
        while(true) {
            System.out.println("3) Editeur à ajouter: ");
            editeurNom = sc.nextLine();
            
            boolean editeurBool = this.ajoutEditeur(editeurNom);
            
            if(editeurBool) {
                System.out.println("Création éditeur ok");                
                //sc.nextLine();
                System.out.println("Nom éditeur: "+editeurNom);
                Editeur editeur = new Editeur(editeurNom);
            }else {
                System.out.println("Création de l'éditeur pas ok");
            }
            
            System.out.println("Voulez-vous rajouter un autre éditeur ? (oui/non)");
            reponse = sc.next().toLowerCase();
            
            if(reponse.equals("non")) {
                break;           
            }
            sc.nextLine();
        }
    }
    //Initialise l'ajout d'un éditeur
    protected boolean ajoutEditeur(String editeur) {
        boolean ajoutPossible = this.editeurExiste_Editeur(editeur);
        
        System.out.println(ajoutPossible);
        if(ajoutPossible == false) {
            System.out.println("L'éditeur n'éxiste pas encore: ajout ok");   
            return true;
            
        }else {
            System.out.println("Ajout impossible car l'éditeur existe déjà");
            return false;
        }
    } 
    //(bool) vérification existance editeur à partir du nom
    protected static boolean editeurExiste_Editeur(String nom_Editeur) {        
        for (Map.Entry<Integer, Editeur> entry : Editeur.mesEditeurs.entrySet()) {
            if(nom_Editeur.equals(entry.getValue().nom_Editeur)) {
                System.out.println("L'éditeur "+nom_Editeur+" existe.");
                return true;
            }
        }
        
        System.out.println("L'éditeur "+nom_Editeur+" n'existe pas.");  
        return false;
    }
    //Gestion modification info éditeur
    protected void modifierInfoEditeur() {
        sc = new Scanner(System.in, charset);
        
        System.out.println("\tVeiller à fournir les informations ci-dessous:");
        while(true) {
            System.out.println("Nom de l'éditeur à modifier: ");   
            String nom_Editeur = sc.nextLine(); 
            
            System.out.println("ID de l'éditeur à modifier: ");
            int id_Editeur = Integer.parseInt(sc.nextLine());
            
            if(this.editeurExiste(nom_Editeur, id_Editeur)) {
                System.out.println("Voulez-vous changer le nom de cet éditeur ? (oui/non)");
                String rep = sc.nextLine().toLowerCase();
                
                if(rep.equals("oui")) {
                    this.modifierNomEditeur(id_Editeur);  
                }
            }
            
            System.out.println("Voulez-vous modifier un autre éditeur ? (oui/non)");
            String rep = sc.nextLine().toLowerCase();
               
            if(rep.equals("non")) {
                break;
            }     
        }    
    }
    //(BOOL) vérifie l'existance d'un éditeur dans la liste des éditeurs avec nom + id
    protected boolean editeurExiste(String nom_Editeur, int id_Editeur) {        
        for (Map.Entry<Integer, Editeur> entry : Editeur.mesEditeurs.entrySet()) {
            if(nom_Editeur.equals(entry.getValue().nom_Editeur) && id_Editeur ==  entry.getValue().id_Editeur) {
                System.out.println("L'éditeur "+nom_Editeur+" existe.");
                return true;
            }
        }
        
        System.out.println("L'éditeur "+nom_Editeur+" n'existe pas.");  
        return false;
    }
    
    protected void modifierNomEditeur(int id_Editeur) {
        for(Map.Entry<Integer, Editeur> entry : Editeur.mesEditeurs.entrySet()) {
            if(entry.getValue().id_Editeur == id_Editeur) {
                System.out.println("Nouveau nom: ");
                entry.getValue().nom_Editeur = sc.nextLine();  
                System.out.println("Modification ok");
                //Modifier nom éditeur BDD
                dataGateway.modifierEditeur(entry.getValue());
            }
        }
    }
     
    //Liste des éditeurs
    protected static void listeEditeurs() {
        if(Editeur.mesEditeurs.isEmpty()) {
            System.out.println("Liste vide\n");
        }else {
            String result = "";
            for (Map.Entry<Integer, Editeur> entry : Editeur.mesEditeurs.entrySet()) {
               result += entry.getValue().id_Editeur+" | "+ entry.getValue().nom_Editeur+"\n";
            }
            System.out.println("ID| Nom\n"+result);
        }
    }
    
    //GETTERS && SETTERS
    protected void setEditeurNom(String nom_Editeur) {
        this.nom_Editeur = nom_Editeur;
    }
    protected void setEditeurId(String nom_Editeur) {
        this.id_Editeur = Editeur.editeurGetInfo_Editeur(nom_Editeur).id_Editeur;
    }
    protected String getEditeurNom() {
        if(this.nom_Editeur.equals("")) {
            return "vide";
        }
        
        return this.nom_Editeur;
    }
    
    protected static Editeur editeurGetInfo_Editeur(String nom_Editeur) {        
        for (Map.Entry<Integer, Editeur> entry : Editeur.mesEditeurs.entrySet()) {
            if(nom_Editeur.equals(entry.getValue().nom_Editeur)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
