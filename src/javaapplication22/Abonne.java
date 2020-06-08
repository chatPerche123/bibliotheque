
package javaapplication22;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Définie un abonne
public class Abonne {
    //variables membres
    protected int id_Abonne;  
    protected String nom_Abonne;
    protected String prenom_Abonne;
    protected String email_Abonne;
    protected String tel_Abonne;
    protected String adresse_Abonne;
    protected String ville_Abonne;
    protected int codePostal_Abonne;
    protected int nbEmpruntPossible;
    protected int cautionAbonne; 
    //Liste des abonnés
    protected static Map<Integer, Abonne> mesAbonnes = new HashMap<Integer, Abonne>();  
    //utilitaires
    Scanner sc = new Scanner(System.in);
    protected static String charset = "ISO-8859-1";
    
    //Constructeurs
    public Abonne() {
        this.nom_Abonne = "vide";
        this.prenom_Abonne = "vide";
        this.email_Abonne = "vide";
        this.tel_Abonne = "vide";
        this.codePostal_Abonne = 0;
        this.adresse_Abonne = "vide";
        this.ville_Abonne = "vide";  
        this.cautionAbonne = 0;      
        this.nbEmpruntPossible = 0;
    }  
    public Abonne(String nom_Abonne, String prenom_Abonne, String email_Abonne, 
            String tel_Abonne, int codePostal_Abonne, String adresse_Abonne,
            String ville_Abonne, int nbEmpruntPossible, int cautionAbonne, Centre centre) throws SQLException {
 
        this.nom_Abonne = nom_Abonne;
        this.prenom_Abonne = prenom_Abonne;
        this.email_Abonne = email_Abonne;
        this.tel_Abonne = tel_Abonne;
        this.codePostal_Abonne = codePostal_Abonne;
        this.adresse_Abonne = adresse_Abonne;
        this.ville_Abonne = ville_Abonne;   
        this.nbEmpruntPossible = nbEmpruntPossible;
        this.cautionAbonne = cautionAbonne;
        
        //Ajout du nouvel abonné en BDD
        dataGateway.ajouterAbonne(this, centre); 
        //Ajout du nouvel abonné dans le programme
        Abonne.mesAbonnes.put(id_Abonne, this);
    }  
    public Abonne(String nom_Abonne) {
        this.nom_Abonne = nom_Abonne;   
        Abonne.mesAbonnes.put(id_Abonne, this);
    } 
    
    //Sous-menu gestion des abonnés
    protected void gestionAbonne() throws ParseException, SQLException {
        sc = new Scanner(System.in, charset);
        String reponse = "";
        
        while(true) {
            System.out.println("\t**Gestion des Abonnes**");
            System.out.println("1) Inscription Abonne: ");
            System.out.println("2) Supprimer Abonne: ");
            System.out.println("3) Modifier infos Abonne: ");
            System.out.println("0) <== ");
            reponse = sc.next();
            
            switch(reponse) {
                case "0": 
                    //quitter
                    break;
                case "1":
                    this.creationAbonne();
                    break;
                case "2":
                    this.suppressionAbonne();
                    break;
                case "3":
                    this.modifierInfoAbonne();
                    break;
            }
            
            if(reponse.equals("0")) {
                break;
            }
        }
    }
    
    //Gestionnaire de suppression d'un abonné
    protected void suppressionAbonne() {
        sc = new Scanner(System.in, charset);
        
        System.out.println("\tVeiller à fournir les informations ci-dessous:");
        while(true) {
            System.out.println("Nom de l'abonné à supprimer: ");   
            String nom_Abonne = sc.nextLine();
            
            System.out.println("Prénom de l'abonné à supprimer: ");
            String prenom_Abonne = sc.nextLine();
            
            System.out.println("ID de l'abonné à supprimer: ");
            int id_Abonne = Integer.parseInt(sc.nextLine());         
            
            //initialise suppression abonné programme et BDD
            this.supprimerAbonne(nom_Abonne, prenom_Abonne, id_Abonne);
            
            System.out.println("Voulez-vous supprimer un autre Abonne ? (oui/autre)");
            String rep = sc.nextLine().toLowerCase();
            
            if(!rep.equals("non")) {
                break;
            }     
        }  
    }
    //fonction de suppression d'un abonné
    protected void supprimerAbonne(String nom_Abonne, String prenom_Abonne, int id_Abonne) {   
        boolean supressionEtat = false;
        for (Map.Entry<Integer, Abonne> entry : Abonne.mesAbonnes.entrySet()) {
            if(nom_Abonne.equals(entry.getValue().nom_Abonne) && prenom_Abonne.equals(entry.getValue().prenom_Abonne) && id_Abonne == entry.getValue().id_Abonne ) {                
                //supprime abonné dans BDD
                dataGateway.supprimerAbonne(entry.getValue());
                //supprime abonné dans programme
                Abonne.mesAbonnes.remove(entry.getKey());
                System.out.println("Supression de l'Abonne "+nom_Abonne+" validée.");
                supressionEtat = true;
                break;
            }
        } 
        if(!supressionEtat) {
            System.out.println("Supression impossible ("+nom_Abonne+" n'existe pas).");
        }
    }  
    //Initialise la création d'un abonné
    protected void creationAbonne() throws SQLException {
       Scanner sc = new Scanner(System.in, charset);
        
        System.out.println("\tVeiller à fournir les informations ci-dessous:");
        while(true) {
            this.verificationAbonne();   
            break;        
        } 
    }
    //Fonction de vérification de conformité d'un n° téléphone
    public static boolean numeroValide(String str) {
        Pattern p = Pattern.compile("^(?:(?:\\+|00)33[\\s.-]{0,3}(?:\\(0\\)[\\s.-]{0,3})?|0)[1-9](?:(?:[\\s.-]?\\d{2}){4}|\\d{2}(?:[\\s.-]?\\d{3}){2})$");
        Matcher m = p.matcher(str);
        
        return (m.find() && m.group().equals(str));
    }
    
    //Création d'un abonné et sa conformité
    protected void verificationAbonne() throws SQLException {
        sc = new Scanner(System.in, charset);
        String reponse, abonneNom, prenom, tel, adresse, ville, email;
        int codePostal, caution, nbEmpruntPossible, cautionAbonne;
 
        while(true) {
            System.out.println("1) Nom abonne: ");
            abonneNom = sc.nextLine();
            
            System.out.println("2) Prénom abonné: ");
            prenom = sc.nextLine();
            
            while(true) {
                System.out.println("3) Téléphone: ");
                tel = sc.nextLine();
                //Vérification de la validité du téléphone
                if(Abonne.numeroValide(tel)){
                    System.out.println("Numéro valide");
                    break;
                }else {
                    System.out.println("Numéro invalide");
                }
           }
            //vérification de la validité d'un mail
            email = this.checkEmail();
         
            System.out.println("5) Adresse: ");
            adresse = sc.nextLine();
            
            System.out.println("6) Ville: ");
            ville = sc.nextLine();
            
            //Créer un objet centre que l'on associe à l'abonné
            Centre centre = this.centreAbonne();
            
            codePostal = this.codePostale();
            caution = this.montantCaution();
            
            nbEmpruntPossible = caution/5;
            Abonne abonne = new Abonne(abonneNom, prenom, email, tel, codePostal, adresse, ville, nbEmpruntPossible, caution, centre);

            System.out.println("Voulez-vous rajouter un autre Abonne ? (oui/non)");
            reponse = sc.next().toLowerCase();
            
            if(reponse.equals("non")) {
                break;           
            }
            //sc.nextLine();
        }
    }
    
    protected Centre centreAbonne() {
        System.out.println("ID du centre: ");
        int id_Centre = Integer.parseInt(sc.nextLine());
        
        while(true) {        
            //vérifier si le centre existe
            if(Centre.centreExiste(id_Centre)) {
                System.out.println("Le centre existe.");
                //ajouter le nouvelle abonne au centre dans le code
                for(Map.Entry<Integer, Centre> entry : Centre.mesCentres.entrySet()) {
                    if(entry.getValue().id_Centre == id_Centre) {
                        return entry.getValue();
                    }
                }
            
            }else {
                System.out.println("Le centre n'existe pas.");
            }
            
            //ajouter le nouvelle abonne dans la table Abonnes_Centres
            
        }
    }
    //vérification validité email
    protected String checkEmail() {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);

        while(true){
            String email;
            System.out.println("4) Email: ");
            email = sc.nextLine();
            Matcher matcher = pattern.matcher(email);

            if(matcher.matches() == true) {
                return email;

            }else {
                System.out.println("Mail non-conforme");
            }
        }

    }
    //Vérifie que le code postale est bien un chiffre
    protected int codePostale() {
            int codePostal;
            while(true) {
                System.out.println("7) Code postal: ");
                try{
                    codePostal = Integer.parseInt(sc.next());
                    return codePostal;
                }catch(NumberFormatException e) {
                    System.out.println("Pas un nombre");
                    this.codePostale();
                }
                
            }
    }
    //vérification validité caution
    protected int montantCaution() {
        int caution = 0;
            while(true) {
                System.out.println("Caution: ");
                //vérifier que la caution est un integer
                try {
                    caution = Integer.parseInt(sc.next());
                    
                    //Vérifie que la caution est dans l'interval 0-50 et est divisible par 5
                    if(caution%5==0 && caution <= 50) {
                        System.out.println("=> "+caution);
                        break;
                    }
                }catch(NumberFormatException e){
                    System.out.println("Pas un nombre");
                }
            }
            return caution;
    }
    //Initialise l'ajout d'un abonné
    protected boolean ajoutAbonne(String abonne) {
        boolean ajoutPossible = Abonne.abonneExiste_Abonne(abonne);
        
        System.out.println(ajoutPossible);
        if(ajoutPossible == false) {
            System.out.println("L'Abonne n'éxiste pas encore: ajout ok");   
            return true;
            
        }else {
            System.out.println("Ajout impossible car l'Abonne existe déjà");
            return false;
        }
    } 
    //Vérifie l'existance d'un abonné
    protected static boolean abonneExiste_Abonne(String nom_Abonne) {        
        for (Map.Entry<Integer, Abonne> entry : Abonne.mesAbonnes.entrySet()) {
            if(nom_Abonne.equals(entry.getValue().nom_Abonne)) {
                System.out.println("L'Abonne "+nom_Abonne+" existe.");
                return true;
            }
        }
        
        System.out.println("L'Abonne "+nom_Abonne+" n'existe pas.");  
        return false;
    }
    //Gestionnaire modification abonné
    protected void modifierInfoAbonne() {
        Scanner sc = new Scanner(System.in, charset);
        
        System.out.println("\tVeiller à fournir les informations ci-dessous:");
        while(true) {
            System.out.println("ID de l'Abonne à modifier: ");   
            int id_Abonne = Integer.parseInt(sc.nextLine()); 

            if(this.abonneExiste(id_Abonne)) {
                System.out.println("Voulez-vous changer le nom de cet Abonne ? (oui/non)");
                String rep = sc.nextLine().toLowerCase();
                
                if(rep.equals("oui")) {
                    this.modifierNomAbonne(id_Abonne);  
                }
                
                System.out.println("Voulez-vous changer le prenom de cet Abonne ? (oui/non)");
                rep = sc.nextLine().toLowerCase();
                
                if(rep.equals("oui")) {
                    this.modifierPrenomAbonne(id_Abonne);  
                }
                
                System.out.println("Voulez-vous changer le mail de cet Abonne ? (oui/non)");
                rep = sc.nextLine().toLowerCase();
                
                if(rep.equals("oui")) {
                    this.modifierEmailAbonne(id_Abonne);  
                }
                
                System.out.println("Voulez-vous changer le téléphone de cet Abonne ? (oui/non)");
                rep = sc.nextLine().toLowerCase();
                
                if(rep.equals("oui")) {
                    this.modifierTelAbonne(id_Abonne);  
                }
                
                System.out.println("Voulez-vous changer le code postal de cet Abonne ? (oui/non)");
                rep = sc.nextLine().toLowerCase();
                
                if(rep.equals("oui")) {
                    this.modifierCodePostalAbonne(id_Abonne);  
                }
                
                System.out.println("Voulez-vous changer l'adresse de cet Abonne ? (oui/non)");
                rep = sc.nextLine().toLowerCase();
                
                if(rep.equals("oui")) {
                    this.modifierAdresseAbonne(id_Abonne);  
                }
                
                System.out.println("Voulez-vous changer la ville de cet Abonne ? (oui/non)");
                rep = sc.nextLine().toLowerCase();
                
                if(rep.equals("oui")) {
                    this.modifierVilleAbonne(id_Abonne);  
                }
            }
            
            System.out.println("Voulez-vous modifier un autre Abonne ? (oui/non)");
            String rep = sc.next().toLowerCase();
               
            if(rep.equals("non")) {
                break;
            }     
        }    
    }
    //Vérifie que l'abonne existe avec son ID
    protected boolean abonneExiste(int id_Abonne) {        
        for (Map.Entry<Integer, Abonne> entry : Abonne.mesAbonnes.entrySet()) {
            if(id_Abonne == entry.getValue().id_Abonne) {
                System.out.println("L'Abonne "+entry.getValue().nom_Abonne+" existe.");
                return true;
            }
        }
        
        System.out.println("L'auteur "+id_Abonne+" n'existe pas.");  
        return false;
    }
    //MODIFICATIONS INFOS ABONNE//
    protected void modifierNomAbonne(int id_Abonne) {
        for(Map.Entry<Integer, Abonne> entry : Abonne.mesAbonnes.entrySet()) {
            if(entry.getValue().id_Abonne == id_Abonne) {
                System.out.println("Nouveau nom: ");
                entry.getValue().nom_Abonne = sc.nextLine();  
                System.out.println("Modification ok");
                
                //modifie dans la BDD le nom de l'abonné
                dataGateway.modifierNomAbonne(entry.getValue());
            }
        }
    }
    protected void modifierPrenomAbonne(int id_Abonne) {
        for(Map.Entry<Integer, Abonne> entry : Abonne.mesAbonnes.entrySet()) {
            if(entry.getValue().id_Abonne == id_Abonne) {
                System.out.println("Nouveau Prénom: ");
                entry.getValue().prenom_Abonne = sc.nextLine();  
                System.out.println("Modification ok");
                
                //modifie dans la BDD le prenom de l'abonné
                dataGateway.modifierPrenomAbonne(entry.getValue());
            }
        }
    }
    protected void modifierEmailAbonne(int id_Abonne) {
        for(Map.Entry<Integer, Abonne> entry : Abonne.mesAbonnes.entrySet()) {
            if(entry.getValue().id_Abonne == id_Abonne) {
                System.out.println("Nouveau Email: ");
                entry.getValue().email_Abonne = this.checkEmail();  
                System.out.println("Modification ok");
                
                //modifie dans la BDD l'email de l'abonné
                dataGateway.modifierEmailAbonne(entry.getValue());
            }
        }
    } 
    protected void modifierTelAbonne(int id_Abonne) {
        for(Map.Entry<Integer, Abonne> entry : Abonne.mesAbonnes.entrySet()) {
            if(entry.getValue().id_Abonne == id_Abonne) {
                System.out.println("Nouveau tel: ");
                entry.getValue().tel_Abonne = sc.nextLine();  
                System.out.println("Modification ok");
                
                //modifie dans la BDD le tél de l'abonné
                dataGateway.modifierTelAbonne(entry.getValue());
            }
        }
    }
    protected void modifierCodePostalAbonne(int id_Abonne) {
        for(Map.Entry<Integer, Abonne> entry : Abonne.mesAbonnes.entrySet()) {
            if(entry.getValue().id_Abonne == id_Abonne) {
                System.out.println("Nouveau code postal: ");
                entry.getValue().codePostal_Abonne = Integer.parseInt(sc.nextLine());  
                System.out.println("Modification ok");
                
                //modifie dans la BDD le CP de l'abonné
                dataGateway.modifierCodePostalAbonne(entry.getValue());
            }
        }
    }
    protected void modifierAdresseAbonne(int id_Abonne) {
        for(Map.Entry<Integer, Abonne> entry : Abonne.mesAbonnes.entrySet()) {
            if(entry.getValue().id_Abonne == id_Abonne) {
                System.out.println("Nouvelle adresse: ");
                entry.getValue().adresse_Abonne = sc.nextLine(); 
                System.out.println("Modification ok");
                
                //modifie dans la BDD l'adresse de l'abonné
                dataGateway.modifierAdresseAbonne(entry.getValue());
            }
        }
    }
    protected void modifierVilleAbonne(int id_Abonne) {
        for(Map.Entry<Integer, Abonne> entry : Abonne.mesAbonnes.entrySet()) {
            if(entry.getValue().id_Abonne == id_Abonne) {
                System.out.println("Nouvelle ville: ");
                entry.getValue().ville_Abonne = sc.nextLine(); 
                System.out.println("Modification ok");
                
                //modifie dans la BDD la ville de l'abonné
                dataGateway.modifierVilleAbonne(entry.getValue());
            }
        }
    }
    
    //La liste des abonnés
    protected static void listeAbonne() {
        if(Abonne.mesAbonnes.isEmpty()) {
            System.out.println("Liste vide\n");
        }else {
            String result = "";
            for (Map.Entry<Integer, Abonne> entry : Abonne.mesAbonnes.entrySet()) {
                result += entry.getValue().id_Abonne+" | "+ entry.getValue().nom_Abonne
                        +" | "+entry.getValue().prenom_Abonne+" | "+entry.getValue().email_Abonne
                 +" | "+entry.getValue().adresse_Abonne+" | "+entry.getValue().tel_Abonne
                +" | "+entry.getValue().ville_Abonne+" | "+entry.getValue().codePostal_Abonne
                +" | "+entry.getValue().cautionAbonne+" | "+entry.getValue().nbEmpruntPossible+"\n";
            }
            System.out.println("ID| Nom | Prenom | Email | Adresse | Tel | Ville | CodePostal | Caution | NbEmpruntPossible\n"+result);
        }
    }
    
    //GETTERS && SETTERS
    protected static Abonne getAbonneOeuvre(String abonneOeuvre) {
        for (Map.Entry<Integer, Abonne> entry : Abonne.mesAbonnes.entrySet()) {
            if(abonneOeuvre.equals(entry.getValue().nom_Abonne)) {
                return entry.getValue();
            }
        }
        
        return null;
    }
    
    protected void setAuteurNom(String nom_Abonne) {
        this.nom_Abonne = nom_Abonne;
    }
    protected String getAbonneNom() {  
        return this.nom_Abonne;
    }  
    protected static String getInfoAbonne(int id_Abonne) {
        for(Map.Entry<Integer, Abonne> entry : Abonne.mesAbonnes.entrySet()) {
            if(id_Abonne == entry.getValue().id_Abonne) {
                return entry.getValue().nom_Abonne+"-"+entry.getValue().prenom_Abonne;
            }
        } 
        return "";
    }
    protected static int getNbEmpruntPossbleLivre(int id_Abonne){
        for(Map.Entry<Integer, Abonne> entry : Abonne.mesAbonnes.entrySet()) {
            if(id_Abonne == entry.getValue().id_Abonne) {
                return entry.getValue().nbEmpruntPossible;
            }
        } 
        return 0;    
    }
    
    protected static void setNbEmpruntPossible(int id_Abonne) {
        for(Map.Entry<Integer, Abonne> entry : Abonne.mesAbonnes.entrySet()) {
            if(id_Abonne == entry.getValue().id_Abonne) {
                entry.getValue().nbEmpruntPossible--;
            }
        } 
    }
    
}
