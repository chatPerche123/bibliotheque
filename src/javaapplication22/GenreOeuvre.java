
package javaapplication22;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class GenreOeuvre {
    //variables membres
    protected int id_genreOeuvre;
    protected String nom_Genre;
    protected static Map<Integer, GenreOeuvre> mesGenres = new HashMap<Integer, GenreOeuvre>();
    //utilitaires
    protected static String charset = "ISO-8859-1";
    Scanner sc = new Scanner(System.in, charset);
   

    //Constructeur(s)
    public GenreOeuvre() {      
    }
    public GenreOeuvre(String genre) throws SQLException {
        this.nom_Genre = genre;
        //Ajout genre BDD
        dataGateway.ajouterGenre(this); 
        //Ajout genre liste
        GenreOeuvre.mesGenres.put(id_genreOeuvre, this); 
    }
    
    //Sous-menu de gestion des genres
    protected static void gestionGenre() throws ParseException, SQLException {
        Scanner sc = new Scanner(System.in, charset);
        String reponse;
        
        while(true) {
            System.out.println("\t**Gestion des genres**");
            System.out.println("1) Ajout genre: ");
            System.out.println("2) Supprimer genre: ");
            System.out.println("3) Modifier genre: ");
            System.out.println("0) <== ");
            reponse = sc.next();
            
            switch(reponse) {
                case "0": 
                    //quitter
                    break;
                case "1":
                    GenreOeuvre.creationGenre();
                    break;
                case "2":
                    //supression genre
                    GenreOeuvre.suppressionGenre();
                    break;
                case "3":
                    //Modifier infos genres
                    GenreOeuvre genre = new GenreOeuvre();
                    genre.modifierInfoGenre();
                    break;
                default:
                    break;
            }
            
            if(reponse.equals("0")) {
                break;
            }
        }
        
    }
    
    //initialise suppresion genre
    protected static void suppressionGenre() {
        Scanner sc = new Scanner(System.in, charset);
        
        System.out.println("\tVeiller à fournir les informations ci-dessous:");
        while(true) {
            System.out.println("Nom du genre à supprimer: ");   
            String nom_Genre = sc.nextLine();
            //supprime genre
            GenreOeuvre.supprimerGenre(nom_Genre);
            
            System.out.println("Voulez-vous supprimer un autre genre ? (oui/non)");
            String rep = sc.nextLine().toLowerCase();
            
            if(rep.equals("non")) {
                break;
            }     
        }  
    }
    //supprime genre
    protected static void supprimerGenre(String genre) {   
        System.out.println("SupprimerCentre(): "+genre);
        boolean supressionEtat = false;
        for (Map.Entry<Integer, GenreOeuvre> entry : GenreOeuvre.mesGenres.entrySet()) {
            if(genre.equals(entry.getValue().nom_Genre)) {
                //supprime genre programme (local)
                GenreOeuvre.mesGenres.remove(entry.getKey());
                System.out.println("Supression du genre "+genre);
                supressionEtat = true;
                //Supprime genre BDD
                dataGateway.supprimerGenre(entry.getValue());
                break;
            }
        } 
        if(!supressionEtat) {
            System.out.println("Supression impossible ("+genre+" n'existe pas)");
        }
    } 
    
    //Vérifie existance genre
    protected boolean genreExiste(String nom_Genre) {        
        for (Map.Entry<Integer, GenreOeuvre> entry : GenreOeuvre.mesGenres.entrySet()) {
            if(nom_Genre.equals(entry.getValue().nom_Genre)) {
                System.out.println("Le genre "+nom_Genre+" existe.");
                return true;
            }
        }
        
        System.out.println("L'éditeur "+nom_Genre+" n'existe pas.");  
        return false;
    }
    //initialise modification info genre
    protected void modifierInfoGenre() {
        sc = new Scanner(System.in, charset);
        
        System.out.println("\tVeiller à fournir les informations ci-dessous:");
        while(true) {
            System.out.println("Nom du genre à modifier: ");   
            String nom_Genre = sc.nextLine(); 
            
            if(this.genreExiste(nom_Genre)) {
                System.out.println("Voulez-vous changer le nom de ce genre ? (oui/non)");
                String rep = sc.nextLine().toLowerCase();
                
                if(rep.equals("oui")) {
                    this.modifierNomGenre(nom_Genre);  
                }
            }
            
            System.out.println("Voulez-vous modifier un autre genre ? (oui/non)");
            String rep = sc.next().toLowerCase();
               
            if(rep.equals("non")) {
                break;
            }     
        }    
    }
    
    protected void modifierNomGenre(String nom_Genre) {
        for(Map.Entry<Integer, GenreOeuvre> entry : GenreOeuvre.mesGenres.entrySet()) {
            if(entry.getValue().nom_Genre.equals(nom_Genre)) {
                System.out.println("Nouveau nom: ");
                entry.getValue().nom_Genre = sc.nextLine();  
                System.out.println("Modification ok");
                
                dataGateway.modifierGenre(entry.getValue());
            }
        }
    }
    //Vérifie existance genre
    protected static boolean genreExiste_Oeuvre(String genreOeuvre) {        
        for (Map.Entry<Integer, GenreOeuvre> entry : GenreOeuvre.mesGenres.entrySet()) {
            if(genreOeuvre.equals(entry.getValue().nom_Genre)) {
                System.out.println("Le genre "+genreOeuvre+" existe.");
                
                return true;
            }
        }
        
        System.out.println("Le genre "+genreOeuvre+" n'existe pas.");  
        return false;
    }
    //liste des genres disponibles dans le programme
    protected static void listeGenreDispo() {
        if(GenreOeuvre.mesGenres.isEmpty()) {
            System.out.println("Liste vide\n");
        
        }else {
            System.out.println("Liste des genres disponibles: ");
            for(Map.Entry<Integer, GenreOeuvre> entry : GenreOeuvre.mesGenres.entrySet()) {
                System.out.println("    => "+entry.getValue().nom_Genre);
            }    
        }
    }
    
    protected static void creationGenre() throws SQLException {
        String charset = "ISO-8859-1";
        Scanner sc = new Scanner(System.in, charset);
        String reponse;
        String genre;

        while(true) {
            System.out.println("Nom du genre: ");
            genre = sc.nextLine();
            
            if(!GenreOeuvre.genreExiste_Oeuvre(genre)) {
                System.out.println("Ajout nouveau genre");
                GenreOeuvre nouveauGenre = new GenreOeuvre(genre);     
            }
            
            System.out.println("Voulez-vous ajouter un autre nouveau genre ?(oui/non)");
            reponse = sc.nextLine().toLowerCase();
            
            if(reponse.equals("non")) {         
                break;
            }    
        }
        
    }
    //GETTERS && SETTERS
    protected static GenreOeuvre getGenreOeuvre(String genreOeuvre) {
        for (Map.Entry<Integer, GenreOeuvre> entry : GenreOeuvre.mesGenres.entrySet()) {
            if(genreOeuvre.equals(entry.getValue().nom_Genre)) {
                return entry.getValue();
            }
        }
        
        return null;
    }
    protected static GenreOeuvre getGenre(int id) {
        for (Map.Entry<Integer, GenreOeuvre> entry : GenreOeuvre.mesGenres.entrySet()) {
            if(id == entry.getValue().id_genreOeuvre) {
                //System.out.println("Ok");
                return entry.getValue();
            }
        }
        System.out.println("Pas ok");
        return null;    
    }
    
    protected static int getIdGenre(String genreOeuvre) {
        for (Map.Entry<Integer, GenreOeuvre> entry : GenreOeuvre.mesGenres.entrySet()) {
            if(genreOeuvre.equals(entry.getValue().nom_Genre)) {
                System.out.println(genreOeuvre+" "+entry.getValue().nom_Genre+" "+entry.getValue().id_genreOeuvre);
                return entry.getValue().id_genreOeuvre;
            }
        } 
        System.out.println("Pas trouvé");
        return 0;
    }
            
}