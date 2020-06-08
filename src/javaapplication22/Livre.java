

package javaapplication22;

import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import static javaapplication22.GenreOeuvre.charset;


public class Livre {
    //propriétés
    protected int isbn_Livre;    
    
    protected ArrayList<GenreOeuvre> genreOeuvre_Livre = new ArrayList<>();
    protected static ArrayList<Livre> liste_Livre = new ArrayList<>();
    protected ArrayList<Auteur> listeAuteur = new ArrayList<>();
    Scanner sc = new Scanner(System.in, charset);
   
    protected String etat_Livre;
    protected LocalDate dateAchat_Livre;
    protected LocalDate date_publication_Livre;
    protected String titre_Livre;
    protected int quantite;  
    
    protected Editeur editeur;
    protected Emplacement emplacement;
    protected Centre centre;
     
    //constructeur
    public Livre() {  
        this.centre = new Centre();
        this.editeur = new Editeur();
        this.emplacement = new Emplacement();
    }
    
    //Sous-menu gestion livre
    protected void gestionLivre() throws ParseException, SQLException {
        sc = new Scanner(System.in, charset);
        String reponse;
        
        while(true) {
            System.out.println("\t**Gestion des livres**");
            System.out.println("1) Ajout livre: ");
            System.out.println("2) Supprimer livre: ");
            System.out.println("3) Modifier livre: ");
            System.out.println("0) <== ");
            reponse = sc.next();
            
            switch(reponse) {
                case "0": 
                    break;
                case "1":
                    //Création livre
                    this.creationLivre();
                    break;
                case "2":
                    //supprimer Livre
                    this.supprimerLivre();
                    break;
                case "3":
                    //modifier Livre
                    this.modifierLivre();
                    break;
                default:
                    System.out.println("Choix non-disponible");
                    break;
            }
            
            if(reponse.equals("0")) {
                break;
            }
        }
    }
    //Existance de l'ID livre
    protected boolean trouverISBN_Livre(int isbn_Livre) {
        for(Livre livre : Livre.liste_Livre) {
            if(livre.getISBN_Livre() == isbn_Livre) {   
                return true;
            }
        }
        
        return false;        
    }
    
    //Création livre
    protected void creationLivre() throws ParseException, SQLException{
        sc = new Scanner(System.in, charset);
        String titreLivre = "";
        int quantite = 0;    
        
        System.out.println("\tVeiller à fournir les informations ci-dessous:");
        while(true) {
            
            Livre livre = new Livre();  
            livre.verificationISBN();
            
            System.out.println("2) Titre: ");
            titreLivre = sc.next();

            livre.verificationGenre();   
            livre.verificationEditeur();            
            livre.verificationAuteur();           
            livre.verificationDatePublication();           
            livre.verificationDateAchat();           
            livre.verificationEmplacement();           
            quantite = livre.quantiteLivre();
            
            sc.nextLine();

            livre.verificationEtat();
            System.out.println("L ETAT DU LIVRE "+livre.etat_Livre);
            livre.verificationCentre(livre);
            
            System.out.println("Voulez-vous ajouter un autre livre ? (oui/non)");
            String reponse = sc.nextLine().toLowerCase();
            
            livre.setQuantite_Livre(quantite);
            livre.setTitre_Livre(titreLivre);  
            
            //ajout livre à la liste des livres
            Livre.liste_Livre.add(livre);
            //Ajout livre BDD
            dataGateway.ajouterLivre(livre);
            
            if(reponse.equals("non")) {   
                break;
            }   
        }
    }
    
    protected void supprimerLivre() throws SQLException {
        sc = new Scanner(System.in, charset);
        int isbn = 0;
        
        System.out.println("ID du livre: ");
        isbn = Integer.parseInt(sc.nextLine());
        
        //livre existe/non
        boolean bool = this.trouverISBN_Livre(isbn);
        
        //si livre existe alors suppression de ce livre dans liste livres
        if(bool) {
            for(Livre livre : Livre.liste_Livre) {
                if(livre.isbn_Livre == isbn) {
                    //supp livre BDD
                    dataGateway.supprimerLivre(isbn);
                    //supp livre local
                    Livre.liste_Livre.remove(livre);
                }
            }
        }
        
    }
    //Infos générales sur un livre à partir de son id
    protected void infoLivreID(int id_Livre) {
        String str = "////////////////\nINFO GENERALE: \n";
        str += "(ID| Titre| Date publication| Date achat| Etat| Centre| Editeur| Emplacement)\n";
        for(Livre livre : Livre.liste_Livre) {
            if(livre.isbn_Livre == id_Livre) {
                str += livre.isbn_Livre+" | "+livre.titre_Livre+" | "+
                            livre.date_publication_Livre+" | "+livre.dateAchat_Livre
                    +" | "+livre.etat_Livre+" | "+livre.centre.nom_Centre+" | "+livre.editeur.nom_Editeur
                    +" | "+livre.emplacement.nom_Emplacement+" \nAUTEUR(S): \n";    
                for(Auteur auteur : livre.listeAuteur) { 
                    str += auteur.nom_Auteur+"-"+auteur.prenom_Auteur+" ";                    

                }
                str += "\nGENRE(s) \n";
                for(GenreOeuvre genre: livre.genreOeuvre_Livre) {
                    str += genre.nom_Genre+" ";
                }
                str += "\n////////////////\n";
            }
        }
        System.out.println(str);
    }
    
    //Sous-menu modification info livre (C EST LONG)
    protected void modifierLivre() throws SQLException {

        while(true) {
            sc = new Scanner(System.in, charset);
            System.out.println("MODIFICATION LIVRE");

            System.out.println("1) Modifier le titre ");
            System.out.println("2) Modifier la date de publication ");
            System.out.println("3) Modifier la date d'achat ");
            System.out.println("4) Modifier l'état d'un livre ");
            System.out.println("5) Modifier le centre d'un livre ");
            System.out.println("6) Modifier l'éditeur d'un livre ");
            System.out.println("7) Modifier l'emplacement d'un livre ");
            System.out.println("8) Modifier l'auteur d'un livre ");
            System.out.println("9) Modifier les genres d'un livre");
            System.out.println("0 <== Quitter ");
            String choix = sc.nextLine();
            int isbn_Livre = 0;

            if(!choix.equals("0")) {
                System.out.println("ID du livre: ");
                isbn_Livre = Integer.parseInt(sc.nextLine());
            }
            System.out.println("L'ID de votre livre est "+isbn_Livre);
            this.infoLivreID(isbn_Livre);
            boolean bool = this.trouverISBN_Livre(isbn_Livre);

            if(bool) {
                for(Livre livre : Livre.liste_Livre) {
                    if(livre.isbn_Livre == isbn_Livre) {
                        switch(choix) {
                            case("1"):
                                //Modifier le titre
                                System.out.println("Nouveau titre: ");
                                String titre_Livre = sc.nextLine();

                                livre.titre_Livre = titre_Livre;
                                dataGateway.modifierTitreLivre(livre);  
                                break;
                            case("2"):
                                //Modifier la date de publication
                                while(true) {
                                    System.out.println("Nouvelle date de publication: (JJ/MM/AAAA)");
                                    String date = sc.nextLine();
                                    
                                    try {
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                        livre.date_publication_Livre = LocalDate.parse(date, formatter);
                                        dataGateway.modifierDatePublicationLivre(livre);
                                        break;
                                
                                    }catch(DateTimeParseException e) {
                                        System.out.println("Mauvais format");
                                    }    
                                }   
                                break;  
                            case("3"):
                                //Modifier la date d'achat
                                while(true) {
                                    System.out.println("Nouvelle date d'achat (JJ/MM/AAAA): ");   
                                    String date = sc.nextLine();
                                    
                                    try {
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                                        livre.dateAchat_Livre = LocalDate.parse(date, formatter);
                                        dataGateway.modifierDateAchatLivre(livre);
                                        break;
                                    }catch(DateTimeParseException e) {
                                        System.out.println("Mauvais format");
                                    }
                                }
                                break;  
                            case("4"):
                                //Modifier l'état d'un livre
                                System.out.println("Nouvelle état du livre ");
                                
                                livre.verificationEtat();
                                dataGateway.modifierEtatLivre(livre);
                                break;
                            case("5"):
                                //Modifier le centre d'un livre
                                this.listeCentre();
                                System.out.println("Nouveau centre livre: ");
                                
                                livre.verificationCentre(livre);
                                dataGateway.modifierCentreLivre(livre);
                                break;
                            case("6"):
                                //Modifier l'éditeur d'un livre
                                this.listeEditeur();
                                System.out.println("Nouvel éditeur: ");
                                
                                
                                livre.verificationEditeur();
                                dataGateway.modifierEditeurLivre(livre);
                                break;  
                            case("7"):
                                //Modifier l'emplacement d'un livre
                                this.listeEmplacement();
                                System.out.println("Nouvel emplacement du livre ");
                                
                                livre.verificationEmplacement();
                                dataGateway.modifierEmplacementLivre(livre);
                                break;     
                            case("8"):
                                //Modifier auteur livre
                                while(true) {
                                    System.out.println("\t1) Modifier un auteur déjà associé au livre: ");
                                    System.out.println("\t2) Supprimer un auteur déjà associé au livre: ");
                                    System.out.println("\t3) Ajouter un auteur au livre: ");
                                    System.out.println("\t0) <== Quitter");
                                    String changerAuteur = sc.nextLine();
                                    
                                    switch(changerAuteur) {
                                        case("1"):  
                                            //Info sur l'auteur actuel à modifier
                                            System.out.println("ID de l'auteur à modifier: ");
                                            int id_Auteur = Integer.parseInt(sc.nextLine());
                                            System.out.println("Nom de l'auteur à modifier: ");
                                            String nom_Auteur = sc.nextLine();
                                            System.out.println("Prenom de l'auteur à modifier: ");
                                            String prenom_Auteur = sc.nextLine();

                                            //vérifie si l'auteur est bien associé au livre
                                            if(this.listeAuteurLivre2(isbn_Livre, id_Auteur, prenom_Auteur, nom_Auteur) == true) {
                                                //prise des nouvelles informations sur le nouvel auteur
                                                System.out.println("ID du nouvelle auteur: ");
                                                int id_nAuteur = Integer.parseInt(sc.nextLine());
                                                System.out.println("Nouveau nom: ");
                                                nom_Auteur = sc.nextLine();
                                                System.out.println("Nouveau prénom: ");
                                                prenom_Auteur = sc.nextLine(); 
                                                
                                                //Si le nouvelle auteur n'est pas déjà associé au livre
                                                if(this.listeAuteurLivre2(isbn_Livre, id_nAuteur, prenom_Auteur, nom_Auteur) == false ) {
                                                    //Si l'auteur existe dans la bdd
                                                    if(Auteur.auteurExiste_Auteur(nom_Auteur, prenom_Auteur, id_nAuteur)) {
                                                        System.out.println("L'auteur n'était pas déjà associé à ce livre, ajout ok.");
                                                        this.modifierNomPrenomAuteur(isbn_Livre, id_Auteur,id_nAuteur, nom_Auteur, prenom_Auteur);
                                                        dataGateway.modifierNomPrenomAuteurLivre(isbn_Livre,id_nAuteur, id_Auteur);    
                                                    
                                                    }else {
                                                        System.out.println("L'auteur n'existe pas dans la bdd");
                                                    }
                                                
                                                }else {
                                                    System.out.println("L'auteur est déjà associé à ce livre, ajout réfusé.");
                                                }
                                            }else {
                                                System.out.println("L'auteur n'existe pas dans la base de données");
                                            }
                                            //Montrer les auteurs de l'id d'un livre

                                            break;
                                        case("2"):
                                            //Info sur l'auteur actuel à modifier
                                            System.out.println("ID de l'auteur à supprimer: ");
                                            id_Auteur = Integer.parseInt(sc.nextLine());
                                            System.out.println("Nom de l'auteur à supprimer: ");
                                            nom_Auteur = sc.nextLine();
                                            System.out.println("Prenom de l'auteur à supprimer: ");
                                            prenom_Auteur = sc.nextLine();

                                            //vérifie si l'auteur est bien associé au livre
                                            if(this.listeAuteurLivre2(isbn_Livre, id_Auteur, prenom_Auteur, nom_Auteur) == true) {
                                                this.supprimerAuteurLivre(isbn_Livre, id_Auteur);
                                                dataGateway.supprimerAuteurLivre(isbn_Livre, id_Auteur);       
                                            }else {
                                                System.out.println("L'auteur n'est pas associé à ce livre");
                                            }
                                            break;
                                        case("3"):
                                            System.out.println("ID de l'auteur à ajouter: ");
                                            id_Auteur = Integer.parseInt(sc.nextLine());
                                            System.out.println("Nom de l'auteur à ajouter: ");
                                            nom_Auteur = sc.nextLine();
                                            System.out.println("Prenom de l'auteur à ajouter: ");
                                            prenom_Auteur = sc.nextLine();    
                                            
                                            //si l'auteur n'est pas déjà associé
                                            if(this.listeAuteurLivre2(isbn_Livre, id_Auteur, prenom_Auteur, nom_Auteur) == false){
                                                //si l'auteur existe dans la bdd
                                                if(Auteur.auteurExiste_Auteur(nom_Auteur, prenom_Auteur, id_Auteur)) {
                                                   this.ajouterAuteurLivre(isbn_Livre, id_Auteur, nom_Auteur, prenom_Auteur);
                                                   dataGateway.ajouterAuteurLivre(isbn_Livre, id_Auteur);
                                                }else {
                                                    System.out.println("L'auteur n'existe pas dans la BDD.");
                                                }
                                            }else {
                                                System.out.println("L'auteur est déjà associé au livre.");
                                            }
                                                    
                                            break;
                                        case("0"):
                                            
                                            break;
                                        default:

                                            break;
                                    }
                                    
                                    if(changerAuteur.equals("0")) {
                                        break;
                                    }
                                    
                                }
                                break;
                                
                            case("9"):
                                //Modifier genre livre
                                while(true) {
                                    System.out.println("\t1) Modifier un genre déjà associé au livre: ");
                                    System.out.println("\t2) Supprimer un genre déjà associé au livre: ");
                                    System.out.println("\t3) Ajouter un genre au livre: ");
                                    System.out.println("\t0) <== Quitter");
                                    String changerGenre = sc.nextLine();
                                    
                                    switch(changerGenre) {
                                        case("1"):
                                            //Modifier
                                            //Info sur l'auteur actuel à modifier
                                            System.out.println("Nom du genre à modifier: ");
                                            String nom_Genre = sc.nextLine();    
                                            
                                            
                                            //vérifier si le genre est associé au livre
                                            if(this.genreExiste_Livre(nom_Genre, isbn_Livre)) {
                                                GenreOeuvre.listeGenreDispo();
                                                System.out.println("Nouveau genre: ");
                                                String nouveau_Genre = sc.nextLine();

                                                //vérifier si le nouveau genre existe dans la base de données
                                                if(GenreOeuvre.genreExiste_Oeuvre(nouveau_Genre)) {
                                                    //vérifie si le nouveau genre n'est pas déjà associé à au livre
                                                    if(this.genreExiste_Livre(nouveau_Genre, isbn_Livre) == false) {
                                                        //modifier genre du livre
                                                        dataGateway.modifierGenre_Livre(isbn_Livre, GenreOeuvre.getIdGenre(nouveau_Genre), GenreOeuvre.getIdGenre(nom_Genre));
                                                        this.modifierGenre_Livre(nom_Genre, isbn_Livre, nouveau_Genre, GenreOeuvre.getIdGenre(nouveau_Genre));
                                                    }
                                                }
                                                    

                                                }
                                                else {
                                                    System.out.println("Le genre n'est pas associé au livre");
                                                }
 
                                            break;
                                        case("2"):
                                            //Supprimer
                                            //Info sur l'auteur actuel à modifier
                                            System.out.println("Nom du genre à supprimer: ");
                                            nom_Genre = sc.nextLine();

                                            //vérifie si le genre est bien associé au livre
                                            if(this.listeGenreLivre(isbn_Livre, nom_Genre)) {
                                                dataGateway.supprimerGenreLivre(isbn_Livre, GenreOeuvre.getIdGenre(nom_Genre)); 
                                                this.supprimerGenreLivre(isbn_Livre, nom_Genre);                                                     
                                            }else {
                                                System.out.println("Le genre n'est pas associé à ce livre.");
                                            }
                                            break;
                                        case("3"):
                                            //Ajouter
                                            System.out.println("Nom du genre à ajouter: ");
                                            String nouveau_Genre = sc.nextLine();
                                            
                                            //si le nouveau genre existe
                                            //si il n'est pas déjà associé au livre
                                            //ajout du nouveau genre au livre
                                            
                                            if(GenreOeuvre.genreExiste_Oeuvre(nouveau_Genre)) {
                                                if(this.genreExiste_Livre(nouveau_Genre, isbn_Livre) == false)   {
                                                    int id = GenreOeuvre.getIdGenre(nouveau_Genre);
                                                    dataGateway.ajoutGenreLivre(isbn_Livre, GenreOeuvre.getIdGenre(nouveau_Genre));   
                                                    this.ajouterGenreLivre(isbn_Livre, id, nouveau_Genre);
                                                }else {
                                                    System.out.println("Le genre est déjà associé au livre.");
                                                }                                     
                                            }else {
                                                System.out.println("Le genre n'existe pas dans la BDD.");
                                            }
                                            break;
                                        case("0"):
                                            //Quitter
                                            
                                            break;
                                    }
                                    
                                    if(changerGenre.equals("0")) {
                                        break;
                                    }
                                }
                            break;                                    
                            case("0"):
                                //<== Quitter
                                
                                break; 
                            default:
                                //Default

                            break;               
                        }
                    }
                }
            }
            
            if(choix.equals("0")) {
                break;
            }
        }
    }
       
    protected void ajouterAuteurLivre(int isbn_Livre, int id_Auteur, String nom_Auteur, String prenom_Auteur) {
        for(Livre livre : Livre.liste_Livre) {
            if(livre.isbn_Livre == isbn_Livre) {
                Auteur auteur = new Auteur();
                auteur.id_Auteur = id_Auteur;
                auteur.nom_Auteur = nom_Auteur;
                auteur.prenom_Auteur = prenom_Auteur;
                livre.listeAuteur.add(auteur);
            }
        }
    }
    public void ajouterGenreLivre(int isbn_Livre, int id_nouveauGenre, String nom_NouveauGenre) {
       for(Livre livre : Livre.liste_Livre) {
            if(livre.isbn_Livre == isbn_Livre) {
                GenreOeuvre genre = new GenreOeuvre();
                genre.id_genreOeuvre = id_nouveauGenre;
                genre.nom_Genre = nom_NouveauGenre;

                livre.genreOeuvre_Livre.add(genre);
            }
        }  
    }
    //LISTES// 
    protected void listeEditeur() {
        String str = "Editeurs disponibles: \n";
        for (Map.Entry<Integer, Editeur> entry : Editeur.mesEditeurs.entrySet()) {
            str += entry.getValue().nom_Editeur+" | ";
        }
        System.out.println(str);
    }
    protected void listeEmplacement() {
        String str = "Emplacements disponibles: \n";
        for (Map.Entry<Integer, Emplacement> entry : Emplacement.mesEmplacements.entrySet()) {
            str += entry.getValue().nom_Emplacement+" | ";
        }
        System.out.println(str);
    }
    protected void listeCentre() {
        String str = "Centres disponibles: \n";
        for (Map.Entry<Integer, Centre> entry : Centre.mesCentres.entrySet()) {
            str += entry.getValue().nom_Centre+" | ";
        }
        System.out.println(str);
    }
    //VERSION NON BOOL
    protected void listeAuteurLivre(int isbn_Livre) {
        for(Livre livre : Livre.liste_Livre) {
            if(livre.isbn_Livre == isbn_Livre) {
                for(Auteur auteur : livre.listeAuteur) {
                    System.out.println("ID "+auteur.id_Auteur+"| Nom "+auteur.nom_Auteur+"| Prenom "+auteur.prenom_Auteur);
                }
            }
        }
    }  
    protected boolean listeGenreLivre(int isbn_Livre, String nom_Genre) {
        for(Livre livre : Livre.liste_Livre) {
            if(livre.isbn_Livre == isbn_Livre) {
                for(GenreOeuvre genre : livre.genreOeuvre_Livre) {
                    if(genre.nom_Genre.equals(nom_Genre)) {
                        return true;
                    } 
                }
            }
        } 
        return false;
    }
    //VERSION BOOL
    protected boolean listeAuteurLivre2(int isbn_Livre, int id_Auteur, String prenom_Auteur, String nom_Auteur) {
        for(Livre livre : Livre.liste_Livre) {
            if(livre.isbn_Livre == isbn_Livre) {
                for(Auteur auteur : livre.listeAuteur) {
                    if(auteur.id_Auteur == id_Auteur && auteur.prenom_Auteur.equals(prenom_Auteur) && auteur.nom_Auteur.equals(nom_Auteur)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected void modifierNomPrenomAuteur(int isbn, int id_Auteur, int id_nAuteur, String nom, String prenom) {
        for(Livre livre : Livre.liste_Livre) {
            if(livre.isbn_Livre == isbn) {
                for(Auteur auteur : livre.listeAuteur) {
                    if(auteur.id_Auteur == id_Auteur) {
                        //System.out.println("Vérification que le nouvelle auteur existe");
                        auteur.nom_Auteur = nom;
                        auteur.prenom_Auteur = prenom;
                        auteur.id_Auteur = id_nAuteur;
                    }
                }
            }
        }
    }
    
    protected void supprimerAuteurLivre(int isbn_Livre, int id_Auteur) {
        for(Livre livre : Livre.liste_Livre) {
            if(livre.isbn_Livre == isbn_Livre) {
                for(Auteur auteur : livre.listeAuteur) {
                    if(auteur.id_Auteur == id_Auteur) {
                        System.out.println("Suppression de l'auteur "+auteur.id_Auteur+" "+auteur.nom_Auteur+"-"+auteur.prenom_Auteur);
                        livre.listeAuteur.remove(auteur);
                        break;
                    }
                }
            }
        } 
    }
    protected void supprimerGenreLivre(int isbn_Livre, String nom_Genre) {
        for(Livre livre : Livre.liste_Livre) {
            if(livre.isbn_Livre == isbn_Livre) {
                for(GenreOeuvre genre : livre.genreOeuvre_Livre) {
                    if(genre.nom_Genre.equals(nom_Genre)) {
                        livre.genreOeuvre_Livre.remove(genre);
                        break;
                    }
                }
            }
        } 
    }
    //Vérifie que la quantité d'un livre est un chiffre
    protected int quantiteLivre() {
        Scanner sc = new Scanner(System.in, charset);
        System.out.println("4) Quantité: ");

        try {
            quantite = sc.nextInt();           
            return quantite;

        }catch(InputMismatchException  e) {
            System.out.println("Un nombre svp");
            this.quantiteLivre();
        }    
        return 0;
    }
    //Vérifier que l'état est conforme
    protected void verificationEtat() {
        String etatLivre[] = {"Très abimé", "Abimé", "Correct", "Bon", "Neuf"};
        while(true){
            System.out.println("5) Etat du livre (Très abimé/Abimé/Correct/Bon/Neuf): ");
            String rep = sc.nextLine();
            
            for(int k = 0; k < etatLivre.length; k++) {
                if(rep.equals(etatLivre[k])) {
                    this.etat_Livre = rep;
                    k = etatLivre.length+1;
                
                }else {
                    this.etat_Livre = "";
                }
            }
            
            if(this.etat_Livre != "") {
                break;
            }
        }
    }
    
    protected void verificationISBN() {
        Scanner sc = new Scanner(System.in, charset);
        int isbnLivre = 0;
        
        while(true) {
            System.out.println("1) ISBN du livre: ");
            isbnLivre = Integer.parseInt(sc.next());
            sc.nextLine();
            //si isbn existe alors
            boolean ajoutISBNBool = this.trouverISBN_Livre(isbnLivre);

            if(ajoutISBNBool) {
                this.setISBN_Livre(isbnLivre);          
                break; 
            }
            
        } 
    }
    
    protected void verificationGenre() {
        sc = new Scanner(System.in, charset);
        String reponse, genreLivre;
        while(true) {
            System.out.println("3) Genre du livre: ");
            genreLivre = sc.nextLine();
            //sc.nextLine();
            boolean ajoutGenreBool = this.ajoutGenreLivre(genreLivre);
            
            if(ajoutGenreBool) {
                System.out.println("Ajout possible, le genre existe");
                this.setGenreOeuvre(GenreOeuvre.getGenreOeuvre(genreLivre));
            }else {
                System.out.println("Ajout impossible, le genre n'existe pas");
            }
            
            System.out.println("Voulez-vous donner/rajouter un autre genre (oui/non) ? ");
            reponse = sc.next().toLowerCase();
            
            if(reponse.equals("non")) {
                
                break;           
            }
            sc.nextLine();
        }
    }
    
    protected void verificationEditeur() {
        sc = new Scanner(System.in, charset);
        String reponse, editeurLivre;
        while(true) {
            System.out.println("4) Editeur du livre: ");
            editeurLivre = sc.nextLine();

            boolean ajoutEditeurBool = this.ajoutEditeurLivre(editeurLivre);

            if(ajoutEditeurBool) {
                System.out.println("Ajout possible, l'éditeur existe");

            }else {
                System.out.println("Ajout impossible, l'éditeur n'existe pas");
            }
            
            System.out.println("Voulez-vous changer le nom de l'éditeur ? (oui/non)");
            reponse = sc.next().toLowerCase();
            
            if(reponse.equals("non")) {
                break;           
            }
            sc.nextLine();
        }
    }
    
    protected void verificationAuteur() {
        sc = new Scanner(System.in, charset);
        String reponse, auteurLivre, prenomAuteur;
        int id_Auteur;
        while(true) {
            System.out.println("4) Id auteur: ");
            id_Auteur = Integer.parseInt(sc.nextLine());
            System.out.println("5) Nom auteur du livre: ");
            auteurLivre = sc.nextLine();
            System.out.println("6) Prénom auteur du livre");
            prenomAuteur = sc.nextLine();
            //sc.nextLine();

            boolean ajoutGenreBool = this.ajoutAuteurLivre(auteurLivre, prenomAuteur, id_Auteur);

            if(ajoutGenreBool) {
                System.out.println("Ajout possible, l'auteur existe");
                this.setAuteurOeuvre(Auteur.getAuteurOeuvre(auteurLivre));
            }else {
                System.out.println("Ajout impossible, le auteur n'existe pas");
            }
            
            System.out.println("Voulez-vous donner/rajouter un autre auteur ? (oui/non)");
            reponse = sc.next().toLowerCase();
            
            if(reponse.equals("non")) {
                break;           
            }
            sc.nextLine();
        }
    }
    
    protected void verificationDatePublication() {
        sc = new Scanner(System.in, charset);
        boolean verifBool = false;
        while(!verifBool) {
            System.out.println("Date publication (JJ/MM/AAAA): ");
            String str = sc.nextLine();
            
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                this.date_publication_Livre = LocalDate.parse(str, formatter);

                String str2 = this.date_publication_Livre.format(formatter);            
                System.out.println("Votre date: "+str2);
                verifBool = true;
                
            } catch(DateTimeParseException e) {
                System.out.println("Mauvais format");
            }
        }  
    }
    
    protected void verificationDateAchat() {
        sc = new Scanner(System.in, charset);
        boolean verifBool = false;
        while(!verifBool) {
            System.out.println("Date achat (JJ/MM/AAAA): ");
            String str = sc.nextLine();
            
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                this.dateAchat_Livre = LocalDate.parse(str, formatter);

                String str2 = this.dateAchat_Livre.format(formatter);            
                System.out.println("Votre date: "+str2);
                verifBool = true;
                
            } catch(DateTimeParseException e) {
                System.out.println("Mauvais format");
            }
        }  
    }

    protected void verificationEmplacement() throws SQLException {
        Scanner sc = new Scanner(System.in, charset);
        Scanner sc2 = new Scanner(System.in, charset);
        String reponse, emplacementLivre;
        while(true) {
            System.out.println("3) Emplacement du livre: ");
            emplacementLivre = sc.nextLine();
            //sc.nextLine();
            boolean ajoutGenreBool = this.ajoutEmplacementLivre(emplacementLivre);
            
            if(ajoutGenreBool) {
                System.out.println("Ajout possible, l'emplacement existe");
                //this.setEmplacementOeuvre(Emplacement.getEmplacementOeuvre(emplacementLivre));
            }else {
                System.out.println("Ajout impossible, l'emplacement n'existe pas");
            }
            
            System.out.println("Voulez-vous donner/rajouter un autre emplacement (oui/non) ? ");
            reponse = sc2.nextLine().toLowerCase();
            
            
            if(reponse.equals("non")) {
                
                break;           
            }
        }
    }
    
    protected void verificationCentre(Livre livre) {
        sc = new Scanner(System.in, charset);
        String reponse, centreLivre;
        while(true) {
            System.out.println("3) Centre qui possède ce livre: ");
            centreLivre = sc.nextLine();
            //sc.nextLine();
            boolean ajoutGenreBool = this.ajoutCentreLivre(centreLivre, livre);
            
            if(ajoutGenreBool) {
                System.out.println("Ajout possible, le centre existe");
                //this.setEmplacementOeuvre(Emplacement.getEmplacementOeuvre(emplacementLivre));
            }else {
                System.out.println("Ajout impossible, le centre n'existe pas");
            }
            
            System.out.println("Voulez-vous donner un autre centre valide (oui/non) ? ");
            reponse = sc.nextLine().toLowerCase();
            
            if(reponse.equals("non")) {
                
                break;           
            }

        }
    }
    
    
    protected boolean genreExiste(String genreLivre) {   
        //vérifie si le genre existe dans la liste des genres disponibles
        boolean genreLivreExiste = GenreOeuvre.genreExiste_Oeuvre(genreLivre);
        
        if(genreLivreExiste) {           
            for(GenreOeuvre genre : this.genreOeuvre_Livre) {
                System.out.println("(genreExiste)"+ genre);
                
                //true ET livre possède déjà ce genre: ajout impossible
                if(genre.nom_Genre.equals(genreLivre)) {
                    return false;
                }
            }
            //true ET livre ne possède pas déjà ce genre: ajout ok
            return true;
        }
        
        return false;
    }
   protected boolean editeurExiste(String editeurLivre) {   
        //vérifie si l'éditeur existe dans la liste des éditeurs disponibles
        boolean editeurLivreExiste = Editeur.editeurExiste_Editeur(editeurLivre);
        
        if(editeurLivreExiste) {  
           this.editeur = new Editeur();
            
           
           editeur.setEditeurNom(editeurLivre);
           editeur.setEditeurId(editeurLivre);
           
           return true;
        }
        
        return false;
    }
    protected boolean auteurExiste(String auteurLivre, String prenomAuteur, int id_Auteur) {   
        //vérifie si le genre existe dans la liste des genres disponibles
        boolean auteurLivreExiste = Auteur.auteurExiste_Auteur(auteurLivre, prenomAuteur, id_Auteur);
        
        if(auteurLivreExiste) {           
            for(Auteur auteur : this.listeAuteur) {
                System.out.println("(auteurExiste)"+ auteur);
                
                //true ET livre possède déjà cet auteur: ajout impossible
                if(auteur.nom_Auteur.equals(auteurLivre) && auteur.prenom_Auteur.equals(prenomAuteur) && auteur.id_Auteur == id_Auteur) {
                    return false;
                }
            }
            //true ET livre ne possède pas déjà ce genre: ajout ok
            return true;
        }
        
        return false;
    }
   protected boolean emplacementExiste(String emplacementLivre) {   
        //vérifie si l'éditeur existe dans la liste des éditeurs disponibles
        boolean emplacementLivreExiste = Emplacement.emplacementExiste_Emplacement(emplacementLivre);
        
        if(emplacementLivreExiste) {  
           this.emplacement = new Emplacement();
            
           
           emplacement.setEmplacementNom(emplacementLivre);
           emplacement.setEmplacementId(emplacementLivre);
           return true;
        }
        
        return false;
    }
   protected boolean centreExiste(String centreLivre, Livre livre) {   
        //vérifie si centre existe dans la liste des centres disponibles
        Centre centreLivreExiste = Centre.centreExiste_Centre(centreLivre);
        
        if(centreLivreExiste != null) { 
           System.out.println("CENTRE EXISTE");
           this.centre = centreLivreExiste;
           this.centre.listeLivre_Centre.add(livre);
           System.out.println(this.centre.nom_Centre+" "+this.centre.listeLivre_Centre.get(0));
           
           return true;
        }
        
        return false;
    }  
    
    
    protected boolean ajoutGenreLivre(String genreLivre) {
        boolean ajoutPossible = this.genreExiste(genreLivre);
        
        System.out.println(ajoutPossible);
        if(ajoutPossible) {
            System.out.println("Le genre n'est pas répertorié pour ce livre, donc ok");
            
            return true;
        }else {
            System.out.println("Ajout impossible car il existe déjà pour ce livre");
            return false;
        }
    }
    protected boolean ajoutEditeurLivre(String editeurLivre) {
        boolean ajoutPossible = this.editeurExiste(editeurLivre);
        
        System.out.println(ajoutPossible);
        if(ajoutPossible) {
            System.out.println("L'éditeur n'est pas répertorié pour ce livre, donc ok");
            
            return true;
        }else {
            System.out.println("Ajout impossible car il existe déjà pour ce livre");
            
            this.editeur = new Editeur();
            editeur.nom_Editeur = "vide";
            editeur.id_Editeur = 0;
            
            return false;
        }
    }
    protected boolean ajoutAuteurLivre(String auteurLivre, String prenomAuteur, int id_Auteur) {
        boolean ajoutPossible = Auteur.auteurExiste_Auteur(auteurLivre, prenomAuteur, id_Auteur);
        
        System.out.println(ajoutPossible);
        if(ajoutPossible) {
            System.out.println("L'auteur n'est pas répertorié pour ce livre, donc ok");
            
            return true;
        }else {
            System.out.println("Ajout impossible car il existe déjà pour ce livre");
            return false;
        }
    }
    protected boolean ajoutEmplacementLivre(String emplacementLivre) throws SQLException {
        boolean ajoutPossible = this.emplacementExiste(emplacementLivre);
        
        System.out.println(ajoutPossible);
        if(ajoutPossible) {
            System.out.println("L'emplacement n'est pas répertorié pour ce livre, donc ok");
            
            return true;
        }else {
            System.out.println("Ajout impossible car il existe déjà pour cet emplacement");
            
            //création emplacement
            this.emplacement = new Emplacement();
            this.emplacement.nom_Emplacement = "vide";
            this.emplacement.id_Emplacement = 0;
            
            return false;
        }
    }
    protected boolean ajoutCentreLivre(String centreLivre, Livre livre) {
        boolean ajoutPossible = this.centreExiste(centreLivre, livre);
        
        System.out.println(ajoutPossible);
        if(ajoutPossible) {
            System.out.println("Le centre n'est pas répertorié pour ce livre, donc ok");
            
            return true;
        }else {
            System.out.println("Ajout impossible car il existe déjà pour ce livre");
            
            //création centre
            this.centre = new Centre();
            centre.nom_Centre = "vide";
            centre.adresse_Centre = "vide";
            centre.id_Centre = 0;
            return false;
        }
    }
    
    //Vérifie si un genre est présent ou non pour un livre
    protected boolean genreExiste_Livre(String genreLivre, int isbn_Livre) {
        for(Livre livre : Livre.liste_Livre) {
            if(livre.isbn_Livre == isbn_Livre) {
                for(GenreOeuvre genre : livre.genreOeuvre_Livre) {
                    if(genre.nom_Genre.equals(genreLivre)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    protected boolean modifierGenre_Livre(String genreLivre, int isbn_Livre, String nouveauGenre, int nouvelID) {
        for(Livre livre : Livre.liste_Livre) {
            if(livre.isbn_Livre == isbn_Livre) {
                for(GenreOeuvre genre : livre.genreOeuvre_Livre) {
                    if(genre.nom_Genre.equals(genreLivre)) {
                        genre.id_genreOeuvre = nouvelID;
                        genre.nom_Genre = nouveauGenre;
                    }
                }
            }
        }
        return false;
    }
     
    protected String afficherAuteur_Livre() {
        String str2 = "(";
        for(Auteur auteur : this.listeAuteur) {
            str2 += auteur.nom_Auteur+"-"+auteur.prenom_Auteur+" ";
        }    
        if(str2.equals("(")) {
            str2 = " vide";
        }
        
        return str2+")";
    } 
    protected String afficherGenre_Livre() {
        String str = "(  ";
        for(GenreOeuvre genre : this.genreOeuvre_Livre) {
            str += genre.nom_Genre+"  ";
        }
        if(str.equals("(")) {
            str = " vide";
        }
        
        return str+")";
    }
    
    //Liste des livres
    protected static void listeLivre() {
        if(Livre.liste_Livre.isEmpty()) {
            System.out.println("Liste vide\n");
        }else {
            int indice = 0;
            System.out.println("Liste des livres: ");
            System.out.println("n°  | Titre | Date publi | Genre(s)    | Quantité | Editeur | Auteur(s) | Emplacement | Centre");
            for(Livre livre : Livre.liste_Livre) {
                indice++;

                System.out.println(indice+"-   "+livre.isbn_Livre+"    | "+livre.titre_Livre+" | "
                        +livre.date_publication_Livre+" | "+livre.afficherGenre_Livre()+        
                        +livre.quantite+"      | "+livre.editeur.getEditeurNom()+ "    | "+livre.afficherAuteur_Livre()
                        +"        | "+livre.emplacement.getEmplacementNom()+"    | "+livre.centre.getCentreNom()+"/"+livre.centre.getCentreAdresse());
            }
        }
    }
    //GETTERS && SETTERS
    protected int getISBN_Livre() {
        return this.isbn_Livre;
    }
    protected void setISBN_Livre(int isbn) {
        this.isbn_Livre = isbn;
    }
    protected String getNom_Livre() {
        return this.titre_Livre;
    }
    
    protected void setTitre_Livre(String titre) {
        this.titre_Livre = titre;
    }
    protected void setGenreOeuvre(GenreOeuvre genreLivre) {
        System.out.println("Ajout OK");
        this.genreOeuvre_Livre.add(genreLivre);
    }
    protected void setAuteurOeuvre(Auteur auteurLivre) {
        System.out.println("Ajout OK");
        this.listeAuteur.add(auteurLivre);
    }
    protected void setQuantite_Livre(int quantite) {
        this.quantite = quantite;
             
    }
    protected void setEtatLivre(String etatLivre) {
        this.etat_Livre = etatLivre;
    }
    protected static String getTitreLivre(int isbn_Livre) {
        for(Livre livre : Livre.liste_Livre) {
            if(livre.isbn_Livre == isbn_Livre) {
                return livre.titre_Livre;               
            }
        }   
        return "";
    }
    protected static int getQuantiteLivre(int isbn_Livre) {
        for(Livre livre : Livre.liste_Livre) {
            if(livre.isbn_Livre == isbn_Livre) {
                return livre.quantite;               
            }
        }   
        return 0;
    }
    protected static void setQuantite(int isbn_Livre) {
        for(Livre livre : Livre.liste_Livre) {
            if(livre.isbn_Livre == isbn_Livre) {
                livre.quantite--;               
            }
        }   
    }

}
