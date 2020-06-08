/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication22;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Map;
import static javaapplication22.dataGateway.conn;

/**
 *
 * @author Fabien
 */
public class dataGateway {
    static Connection conn = null;
    protected static final String USERNAME = "root";
    protected static final String PASSWORD = "";
    protected static final String CONN_STRING = "jdbc:mysql://localhost:3306/bibliotheque";
    protected static String charset = "ISO-8859-1";
    
    //établir connexion avec la BDD
    public static Connection getConnexion() {
        try{
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            System.out.println("CONNECTED !"); 
            return conn;
        }catch(SQLException e) {
            System.err.println(e);
        }
        return null;
    }
    
    
    public static void ajouterAuteur(Auteur auteur) throws SQLException {
        try{
            String insert = "INSERT INTO auteur (nom_Auteur,prenom_Auteur) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setString(1, auteur.nom_Auteur);
            statement.setString(2, auteur.prenom_Auteur);
            statement.executeUpdate();
            
            String query = "SELECT id_Auteur FROM auteur";
            ResultSet rs = statement.executeQuery(query);
            
            while(rs.next()) {
                auteur.id_Auteur = rs.getInt("id_Auteur");
            } 
            
        }catch(SQLException e) {
            System.err.println(e);
        } 
    }
    public static void supprimerAuteur(Auteur auteur) {
        try{
            String insert = "DELETE FROM auteur WHERE id_Auteur = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setInt(1, auteur.id_Auteur);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }    
    }
    public static void modifierAuteur(Auteur auteur) {
        try{
            String insert = "UPDATE auteur SET nom_Auteur = ?, prenom_Auteur = ? WHERE id_Auteur = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setString(1, auteur.nom_Auteur);
            statement.setString(2, auteur.prenom_Auteur);
            statement.setInt(3, auteur.id_Auteur);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }  
    }
        public static void ajouterEditeur(Editeur editeur) throws SQLException {
        try{
            String insert = "INSERT INTO editeur (nom_Editeur) VALUES (?)";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setString(1, editeur.nom_Editeur);
            statement.executeUpdate();
            
            String query = "SELECT id_Editeur FROM editeur";
            ResultSet rs = statement.executeQuery(query);
            
            while(rs.next()) {
                editeur.id_Editeur = rs.getInt("id_Editeur");
            } 
            
        }catch(SQLException e) {
            System.err.println(e);
        } 
    }
    public static void supprimerEditeur(Editeur editeur) {
        try{
            String insert = "DELETE FROM editeur WHERE id_Editeur = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setInt(1, editeur.id_Editeur);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }    
    }
    public static void modifierEditeur(Editeur editeur) {
        try{
            String insert = "UPDATE editeur SET nom_Editeur = ? WHERE id_Editeur = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setString(1, editeur.nom_Editeur);
            statement.setInt(2, editeur.id_Editeur);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }  
    }
     
    public static void ajouterEmplacement(Emplacement emplacement) throws SQLException {
        try{
            String insert = "INSERT INTO emplacement (nom_Emplacement) VALUES (?)";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setString(1, emplacement.nom_Emplacement);
            statement.executeUpdate();
            
            String query = "SELECT id_Emplacement FROM emplacement";
            ResultSet rs = statement.executeQuery(query);
            
            while(rs.next()) {
                emplacement.id_Emplacement = rs.getInt("id_Emplacement");
            } 
            
        }catch(SQLException e) {
            System.err.println(e);
        } 
    }
    public static void supprimerEmplacement(Emplacement emplacement) {
        try{
            String insert = "DELETE FROM emplacement WHERE id_Emplacement = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setInt(1, emplacement.id_Emplacement);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }    
    }
    public static void modifierEmplacement(Emplacement emplacement) {
        try{
            String insert = "UPDATE emplacement SET nom_Emplacement = ? WHERE id_Emplacement = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setString(1, emplacement.nom_Emplacement);
            statement.setInt(2, emplacement.id_Emplacement);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }  
    }

    public static void ajouterCentre(Centre centre) throws SQLException {
        try{
            String insert = "INSERT INTO centre (nom_Centre, adresse_Centre) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setString(1, centre.nom_Centre);
            statement.setString(2, centre.adresse_Centre);
            statement.executeUpdate();
            
            String query = "SELECT id_Centre FROM centre";
            ResultSet rs = statement.executeQuery(query);
            
            while(rs.next()) {
                centre.id_Centre = rs.getInt("id_Centre");
            } 
            
        }catch(SQLException e) {
            System.err.println(e);
        } 
    }
    public static void supprimerCentre(Centre centre) {
        try{
            String insert = "DELETE FROM centre WHERE id_Centre = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setInt(1, centre.id_Centre);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }    
    }
    public static void modifierNomCentre(Centre centre) {
        try{
            String insert = "UPDATE centre SET nom_Centre = ? WHERE id_Centre = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setString(1, centre.nom_Centre);
            statement.setInt(2, centre.id_Centre);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }  
    }   
    public static void modifierAdresseCentre(Centre centre) {
        try{
            String insert = "UPDATE centre SET adresse_Centre = ? WHERE id_Centre = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setString(1, centre.adresse_Centre);
            statement.setInt(2, centre.id_Centre);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }  
    }  
    
    
    public static void ajouterGenre(GenreOeuvre genre) throws SQLException {
        try{
            String insert = "INSERT INTO genre (nom_Genre) VALUES (?)";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setString(1, genre.nom_Genre);
            statement.executeUpdate();
            
            String query = "SELECT id_Genre FROM genre";
            ResultSet rs = statement.executeQuery(query);
            
            while(rs.next()) {
                genre.id_genreOeuvre = rs.getInt("id_Genre");
            } 
            
        }catch(SQLException e) {
            System.err.println(e);
        } 
    }
    public static void supprimerGenre(GenreOeuvre genre) {
        try{
            String insert = "DELETE FROM genre WHERE id_Genre = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setInt(1, genre.id_genreOeuvre);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }    
    }
    public static void modifierGenre(GenreOeuvre genre) {
        try{
            String insert = "UPDATE genre SET nom_Genre = ? WHERE id_Genre = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setString(1, genre.nom_Genre);
            statement.setInt(2, genre.id_genreOeuvre);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }  
    }  

    public static void ajouterAbonne(Abonne abonne, Centre centre) throws SQLException {
        try{
            String insert = "INSERT INTO abonne (nom_Abonne, prenom_Abonne, "
                    + "email_Abonne, tel_Abonne, codePostal_Abonne, adresse_Abonne, "
                    + "ville_Abonne, nbEmpruntPossible_Abonne, caution_Abonne) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setString(1, abonne.nom_Abonne);
            statement.setString(2, abonne.prenom_Abonne);
            statement.setString(3, abonne.email_Abonne);
            statement.setString(4, abonne.tel_Abonne);
            statement.setInt(5, abonne.codePostal_Abonne);
            statement.setString(6, abonne.adresse_Abonne);
            statement.setString(7, abonne.ville_Abonne);
            statement.setInt(8, abonne.nbEmpruntPossible);
            statement.setInt(9, abonne.cautionAbonne);
            
            statement.executeUpdate();
            
            //Récupère l'ID généré par la BDD 
            String query = "SELECT id_Abonne FROM abonne";
            ResultSet rs = statement.executeQuery(query);
            
            while(rs.next()) {
                abonne.id_Abonne = rs.getInt("id_Abonne");
            } 
            
            dataGateway.ajouterAbonneCentre(abonne, centre);
            
        }catch(SQLException e) {
            System.err.println(e);
        } 
    }
    
    public static void ajouterAbonneCentre(Abonne abonne, Centre centre) throws SQLException {
        try {
            String insert = "INSERT INTO abonnes_centres (id_Abonne, id_Centre) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setInt(1, abonne.id_Abonne);
            statement.setInt(2, centre.id_Centre);
            statement.executeUpdate();
            //ajout en local
            centre.listeAbonne_Centre.add(abonne);
            
        }catch(SQLException e) {
            System.err.println(e);
        } 
    }
    
    public static void supprimerAbonne(Abonne abonne) {
        try{
            String insert = "DELETE FROM abonne WHERE id_Abonne = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            System.out.println("DELETE "+abonne.id_Abonne+" "+abonne.nom_Abonne);
            statement.setInt(1, abonne.id_Abonne);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }    
    }
    public static void modifierNomAbonne(Abonne abonne) {
        try{
            String insert = "UPDATE abonne SET nom_Abonne = ? WHERE id_Abonne = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setString(1, abonne.nom_Abonne);
            statement.setInt(2, abonne.id_Abonne);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }  
    }  
    public static void modifierPrenomAbonne(Abonne abonne) {
        try{
            String insert = "UPDATE abonne SET prenom_Abonne = ? WHERE id_Abonne = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setString(1, abonne.prenom_Abonne);
            statement.setInt(2, abonne.id_Abonne);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }  
    } 
    public static void modifierEmailAbonne(Abonne abonne) {
        try{
            String insert = "UPDATE abonne SET email_Abonne = ? WHERE id_Abonne = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setString(1, abonne.email_Abonne);
            statement.setInt(2, abonne.id_Abonne);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }  
    } 
    public static void modifierTelAbonne(Abonne abonne) {
        try{
            String insert = "UPDATE abonne SET tel_Abonne = ? WHERE id_Abonne = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setString(1, abonne.tel_Abonne);
            statement.setInt(2, abonne.id_Abonne);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }  
    } 
    public static void modifierCodePostalAbonne(Abonne abonne) {
        try{
            String insert = "UPDATE abonne SET codePostal_Abonne = ? WHERE id_Abonne = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setInt(1, abonne.codePostal_Abonne);
            statement.setInt(2, abonne.id_Abonne);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }  
    } 
    public static void modifierAdresseAbonne(Abonne abonne) {
        try{
            String insert = "UPDATE abonne SET adresse_Abonne = ? WHERE id_Abonne = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setString(1, abonne.adresse_Abonne);
            statement.setInt(2, abonne.id_Abonne);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }  
    } 
    public static void modifierVilleAbonne(Abonne abonne) {
        try{
            String insert = "UPDATE abonne SET ville_Abonne = ? WHERE id_Abonne = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setString(1, abonne.ville_Abonne);
            statement.setInt(2, abonne.id_Abonne);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }  
    } 
   
    //Ajout livre table Livre, livres_auteurs et livres_genres
    public static void ajouterLivre(Livre livre) throws SQLException {
        try{
            String insert = "INSERT INTO livre (isbn_Livre, date_publication_Livre, "
                    + "etat_Livre, dateAchat_Livre, titre_Livre, id_Centre, id_Editeur, "
                    + " id_Emplacement, quantite) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            
            statement.setInt(1, livre.isbn_Livre);    
            Date date = Date.valueOf(livre.date_publication_Livre);
            statement.setDate(2, date);
            statement.setString(3, livre.etat_Livre);  
            date = Date.valueOf(livre.dateAchat_Livre);
            statement.setDate(4, date);
            statement.setString(5, livre.titre_Livre); 
            statement.setInt(6, livre.centre.id_Centre); 
            statement.setInt(7, livre.editeur.id_Editeur);  
            statement.setInt(8, livre.emplacement.id_Emplacement); 
            statement.setInt(9, livre.quantite); 
            
            statement.executeUpdate();
            insert = "INSERT INTO livres_auteurs (isbn_Livre, id_Auteur) VALUES (?, ?)";
            statement = conn.prepareStatement(insert);

            System.out.println("Taille listeAuteur "+livre.listeAuteur.size());
            System.out.println("ISBN DU livre "+livre.isbn_Livre);
            for(Auteur auteur : livre.listeAuteur){

                statement.setInt(1, livre.isbn_Livre);
                statement.setInt(2, auteur.id_Auteur);
                statement.executeUpdate();
            }


            
            insert = "INSERT INTO livres_genres (isbn_Livre, id_Genre) VALUES (?, ?)";
            statement = conn.prepareStatement(insert);
            
            for(GenreOeuvre genre : livre.genreOeuvre_Livre){
                statement.setInt(1, livre.isbn_Livre);
                statement.setInt(2, genre.id_genreOeuvre);
                statement.executeUpdate();
            }  
            
        }catch(SQLException e) {
            System.err.println(e);
        } 
    }
    //supprime livre dans tables livre, livres_auteurs et livres_genres
    public static void supprimerLivre(int isbn_Livre) throws SQLException {
        try {
            String query = "DELETE FROM livre WHERE isbn_Livre = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, isbn_Livre);
            preparedStmt.execute();
            
            query = "DELETE FROM livres_auteurs WHERE isbn_Livre = ?";
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, isbn_Livre);
            preparedStmt.execute();
            
            query = "DELETE FROM livres_genres WHERE isbn_Livre = ?";
            preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1, isbn_Livre);  
            preparedStmt.execute();
            
        }catch(SQLException e) {
            System.err.println(e);
        } 
    }
    public static void modifierTitreLivre(Livre livre) {
        try{
            String insert = "UPDATE livre SET titre_Livre = ? WHERE isbn_Livre = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setString(1, livre.titre_Livre);
            statement.setInt(2, livre.isbn_Livre);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }  
    }
    public static void modifierDatePublicationLivre(Livre livre) {
        System.out.println(livre.date_publication_Livre);
        try{
            String insert = "UPDATE livre SET date_publication_Livre = ? WHERE isbn_Livre = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            Date date = Date.valueOf(livre.date_publication_Livre);
            statement.setDate(1, date);
            statement.setInt(2, livre.isbn_Livre);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }  
    }
    public static void modifierDateAchatLivre(Livre livre) {
        System.out.println(livre.date_publication_Livre);
        try{
            String insert = "UPDATE livre SET dateAchat_Livre = ? WHERE isbn_Livre = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            Date date = Date.valueOf(livre.dateAchat_Livre);
            statement.setDate(1, date);
            statement.setInt(2, livre.isbn_Livre);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }  
    }
    public static void modifierEtatLivre(Livre livre) {
        try{
            String insert = "UPDATE livre SET etat_Livre = ? WHERE isbn_Livre = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setString(1, livre.etat_Livre);
            statement.setInt(2, livre.isbn_Livre);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }  
    }
    public static void modifierCentreLivre(Livre livre) {
        try{
            String insert = "UPDATE livre SET id_Centre = ? WHERE isbn_Livre = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setInt(1, livre.centre.id_Centre);
            statement.setInt(2, livre.isbn_Livre);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }  
    }
    
    public static void modifierEditeurLivre(Livre livre) {
        try{
            String insert = "UPDATE livre SET id_Editeur = ? WHERE isbn_Livre = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setInt(1, livre.editeur.id_Editeur);
            statement.setInt(2, livre.isbn_Livre);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }  
    }
    
    public static void modifierEmplacementLivre(Livre livre) {
        try{
            String insert = "UPDATE livre SET id_Emplacement = ? WHERE isbn_Livre = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setInt(1, livre.emplacement.id_Emplacement);
            statement.setInt(2, livre.isbn_Livre);
            
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }  
    } 
    
    public static void modifierNomPrenomAuteurLivre(int isbn_Livre, int id_nAuteur, int id_Auteur) {
        try {
            String insert = "UPDATE livres_auteurs SET id_Auteur = ? WHERE isbn_Livre = ? AND id_Auteur = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setInt(1, id_nAuteur);
            statement.setInt(2, isbn_Livre);
            statement.setInt(3, id_Auteur);
            
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }  
    }
    public static void supprimerAuteurLivre(int isbn_Livre, int id_Auteur) {
        try {
            String insert = "DELETE FROM livres_auteurs WHERE isbn_Livre = ? AND id_Auteur = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setInt(1, isbn_Livre);
            statement.setInt(2, id_Auteur);
            
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        } 
    }
            
    public static void ajouterAuteurLivre(int isbn_Livre, int id_Auteur) {
        try {
            String insert = "INSERT INTO livres_auteurs (isbn_Livre, id_Auteur) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setInt(1, isbn_Livre);
            statement.setInt(2, id_Auteur);
            
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }   
    }

    public static void modifierGenre_Livre(int isbn_Livre, int id_nouveauGenre, int id_ancienGenre) {
        try {
            String insert = "UPDATE livres_genres SET id_Genre = ? WHERE isbn_Livre = ? AND id_Genre = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setInt(1, id_nouveauGenre);
            statement.setInt(2, isbn_Livre);
            statement.setInt(3, id_ancienGenre);
            
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }   
    }
    public static void supprimerGenreLivre(int isbn_Livre, int id_Genre) {
        try {
            String insert = "DELETE FROM livres_genres WHERE isbn_Livre = ? AND id_Genre = ?";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setInt(1, isbn_Livre);
            statement.setInt(2, id_Genre);
            
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        } 
    }
    public static void ajoutGenreLivre(int isbn_Livre, int id_Genre) {
        try {
            String insert = "INSERT INTO livres_genres (isbn_Livre, id_Genre) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setInt(1, isbn_Livre);
            statement.setInt(2, id_Genre);
            
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }   
    }

    public static void terminerEmprunt(Emprunt emprunt) throws SQLException {
         String insert = "UPDATE Emprunt SET dateRetours = ? WHERE id_Emprunt = ?";
         PreparedStatement statement = conn.prepareStatement(insert);
         
         Date date = Date.valueOf(emprunt.dateRetours);
         statement.setDate(1, date);
         statement.setInt(2, emprunt.id_Emprunt);
         statement.executeUpdate();     
    }    
    public static void ajouterEmprunt(Emprunt emprunt) {
        try {
            
            String insert = "INSERT INTO emprunt (id_Abonne, id_Centre, isbn_Livre, dateEmprunt, dateRetours, dateLimite) VALUES (?, ?, ?, ? ,?, ?)";
            PreparedStatement statement = conn.prepareStatement(insert);
            
            statement.setInt(1, emprunt.id_Abonne);
            statement.setInt(2, emprunt.id_Centre);
            statement.setInt(3, emprunt.isbn_Livre);
            Date date = Date.valueOf(emprunt.dateEmprunt);
            statement.setDate(4, date);
            //date = Date.valueOf(emprunt.dateRetours);
            statement.setDate(5, null);
            date = Date.valueOf(emprunt.dateLimite);
            statement.setDate(6, date);
            
            statement.executeUpdate();
            
            
            insert = "SELECT * FROM emprunt WHERE id_Abonne = ? AND id_Centre = ? AND isbn_Livre = ?";
            PreparedStatement ps = conn.prepareStatement(insert);
            ps.setInt(1, emprunt.id_Abonne);
            ps.setInt(2,  emprunt.id_Centre);
            ps.setInt(3,  emprunt.isbn_Livre);
            
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                emprunt.id_Emprunt = rs.getInt("id_Emprunt");
            }
            
            insert = "UPDATE livre SET quantite = ? WHERE isbn_Livre = ?";
            statement = conn.prepareStatement(insert);      
            int var = Livre.getQuantiteLivre(emprunt.isbn_Livre);
            var = var - 1;     
            System.out.println("Ma quantité de livre -1: "+var); 
            
            Livre.setQuantite(emprunt.isbn_Livre);
            
            statement.setInt(1, var);
            statement.setInt(2, emprunt.isbn_Livre);
            statement.executeUpdate();
            
            insert = "UPDATE abonne SET nbEmpruntPossible_Abonne = ? WHERE id_Abonne = ?";
            statement = conn.prepareStatement(insert); 
            var = Abonne.getNbEmpruntPossbleLivre(emprunt.id_Abonne);
            var = var - 1;
            System.out.println("Ma quantité de nombre d'emprunt possible -1: "+var);  
            
            Abonne.setNbEmpruntPossible(emprunt.id_Abonne);
            
            statement.setInt(1, var);
            statement.setInt(2, emprunt.id_Abonne);
            statement.executeUpdate();
            
        }catch(SQLException e) {
            System.err.println(e);
        }    
    }
    //Initialise toutes les informations dans la BDD vers le programme local
    public static void set_all_data() throws SQLException {
        try{  
            Statement statement = conn.createStatement();
            String query = "SELECT * FROM auteur";

            ResultSet rs = statement.executeQuery(query);
            
            while(rs.next()) {
                Auteur auteur = new Auteur();
                auteur.id_Auteur = rs.getInt("id_Auteur");
                auteur.nom_Auteur = rs.getString("nom_Auteur");
                auteur.prenom_Auteur = rs.getString("prenom_Auteur");

                Auteur.mesAuteurs.put(rs.getInt("id_Auteur"), auteur);
            }
            
            query = "SELECT * FROM editeur";
            rs = statement.executeQuery(query);
            
            while(rs.next()) {
                Editeur editeur = new Editeur();
                editeur.id_Editeur = rs.getInt("id_Editeur");
                editeur.nom_Editeur = rs.getString("nom_Editeur");

                Editeur.mesEditeurs.put(rs.getInt("id_Editeur"), editeur);
            }   

            query = "SELECT * FROM centre";
            rs = statement.executeQuery(query);
            
            while(rs.next()) {
                Centre centre = new Centre();
      
                centre.id_Centre = rs.getInt("id_Centre");
                centre.nom_Centre = rs.getString("nom_Centre");
                centre.adresse_Centre = rs.getString("adresse_Centre");

                Centre.mesCentres.put(rs.getInt("id_Centre"), centre);
            }   
            
            query = "SELECT * FROM Emplacement";
            rs = statement.executeQuery(query);
            
            while(rs.next()) {
                Emplacement emplacement = new Emplacement();
                emplacement.id_Emplacement = rs.getInt("id_Emplacement");
                emplacement.nom_Emplacement = rs.getString("nom_Emplacement");

                Emplacement.mesEmplacements.put(rs.getInt("id_Emplacement"), emplacement);
            }  
            
            query = "SELECT * FROM Genre";
            rs = statement.executeQuery(query);
            
            while(rs.next()) {
                GenreOeuvre genre = new GenreOeuvre();

                genre.id_genreOeuvre = rs.getInt("id_Genre");
                genre.nom_Genre = rs.getString("nom_Genre");

                GenreOeuvre.mesGenres.put(rs.getInt("id_Genre"), genre);
            } 
            
            query = "SELECT * FROM Abonne";
            rs = statement.executeQuery(query);
            
            while(rs.next()) {
                Abonne abonne = new Abonne();

                abonne.id_Abonne = rs.getInt("id_Abonne");
                abonne.nom_Abonne = rs.getString("nom_Abonne");
                abonne.prenom_Abonne = rs.getString("prenom_Abonne");
                abonne.email_Abonne = rs.getString("email_Abonne");
                abonne.tel_Abonne = rs.getString("tel_Abonne");
                abonne.codePostal_Abonne = rs.getInt("codePostal_Abonne");
                abonne.adresse_Abonne = rs.getString("adresse_Abonne");
                abonne.ville_Abonne = rs.getString("ville_Abonne");
                abonne.nbEmpruntPossible = rs.getInt("nbEmpruntPossible_Abonne");
                abonne.cautionAbonne = rs.getInt("caution_Abonne");  
                
                Abonne.mesAbonnes.put(rs.getInt("id_Abonne"), abonne);
            } 
            
            query = "SELECT * FROM Livre";
            rs = statement.executeQuery(query);
           
            while(rs.next()) {  
                Livre livre = new Livre();
                
                livre.isbn_Livre = rs.getInt("isbn_Livre");   
                livre.date_publication_Livre = rs.getDate("date_publication_Livre").toLocalDate();
                livre.etat_Livre = rs.getString("etat_Livre");
                livre.dateAchat_Livre = rs.getDate("dateAchat_Livre").toLocalDate(); 
                livre.titre_Livre = rs.getString("titre_Livre");                 
                livre.centre.id_Centre = rs.getInt("id_Centre");    
                livre.editeur.id_Editeur = rs.getInt("id_Editeur");                
                livre.emplacement.id_Emplacement = rs.getInt("id_Emplacement");  
                livre.quantite = rs.getInt("quantite");
          
                Livre.liste_Livre.add(livre);
                
                Centre centre = Centre.centreExisteId_Centre(rs.getInt("id_Centre"));
                centre.listeLivre_Centre.add(livre);
            }
            query = "SELECT * FROM livres_auteurs";
            rs = statement.executeQuery(query);
            Auteur auteur = new Auteur();
            while(rs.next()) {
                for(Livre livre : Livre.liste_Livre) {
                    if(livre.getISBN_Livre() == rs.getInt("isbn_Livre")) {
                        auteur = Auteur.getAuteur(rs.getInt("id_Auteur"));
                        livre.listeAuteur.add(auteur);
                    }
                }
            }
            
            query = "SELECT * FROM livres_genres";
            rs = statement.executeQuery(query);
            GenreOeuvre genre = new GenreOeuvre();
            while(rs.next()) {
                for(Livre livre : Livre.liste_Livre) {
                    if(livre.getISBN_Livre() == rs.getInt("isbn_Livre")) {
                        genre = GenreOeuvre.getGenre(rs.getInt("id_Genre"));
                        livre.genreOeuvre_Livre.add(genre);
                    }
                }
            }
            
            query ="SELECT DISTINCT emplacement.nom_emplacement, livre.isbn_Livre, centre.nom_Centre, centre.adresse_Centre, editeur.nom_Editeur FROM livre INNER JOIN centre ON livre.id_Centre = centre.id_Centre INNER JOIN editeur ON livre.id_Editeur = editeur.id_Editeur INNER JOIN Emplacement ON livre.id_Emplacement = Emplacement.id_Emplacement";
            rs = statement.executeQuery(query);
            while(rs.next()) {
                for(Livre livre : Livre.liste_Livre) {
                    if(livre.getISBN_Livre() == rs.getInt("isbn_Livre")) {
                        livre.editeur.nom_Editeur = rs.getString("nom_Editeur");
                        livre.centre.nom_Centre = rs.getString("nom_Centre");
                        livre.centre.adresse_Centre = rs.getString("adresse_Centre");
                        livre.emplacement.nom_Emplacement = rs.getString("nom_Emplacement");
                        
                        Centre centre = new Centre();
                        centre.listeLivre_Centre.add(livre);
                    }
                }
            } 
            
            query = "SELECT * FROM abonne INNER JOIN abonnes_centres WHERE abonne.id_Abonne = abonnes_centres.id_Abonne";
            rs = statement.executeQuery(query);
            Centre centre = new Centre();
            while(rs.next()) {
                for(Map.Entry<Integer, Centre> entry : Centre.mesCentres.entrySet()) {
                    if(entry.getValue().id_Centre == rs.getInt("id_Centre")) {
                        Abonne abonne = new Abonne();
                        abonne.id_Abonne = rs.getInt("id_Abonne");
                        abonne.adresse_Abonne = rs.getString("adresse_Abonne");
                        abonne.cautionAbonne = rs.getInt("caution_Abonne");
                        abonne.codePostal_Abonne = rs.getInt("codePostal_Abonne");
                        abonne.email_Abonne = rs.getString("email_Abonne");
                        abonne.nbEmpruntPossible = rs.getInt("nbEmpruntPossible_Abonne");
                        abonne.ville_Abonne = rs.getString("ville_Abonne");
                        abonne.nom_Abonne = rs.getString("nom_Abonne");
                        abonne.prenom_Abonne = rs.getString("prenom_Abonne");
                        abonne.tel_Abonne = rs.getString("tel_Abonne");
                        
                  
                        entry.getValue().listeAbonne_Centre.add(abonne);  
                    }
                }
            }

            query = "SELECT * FROM emprunt";
            rs = statement.executeQuery(query);
            while(rs.next()) {
                //si dateRetours est null dans BDD alors la gestion est différente dans le code
                if(rs.getDate("dateRetours") == null) {
                    LocalDate date = null;
                    Emprunt emprunt = new Emprunt(rs.getInt("id_Abonne"), rs.getInt("id_Centre"), rs.getInt("isbn_Livre"), rs.getDate("dateEmprunt").toLocalDate(), date);
                    emprunt.id_Emprunt = rs.getInt("id_Emprunt");
                    Emprunt.liste_Emprunt.add(emprunt);
                }else {
                      Emprunt emprunt = new Emprunt(rs.getInt("id_Abonne"), rs.getInt("id_Centre"), rs.getInt("isbn_Livre"), rs.getDate("dateEmprunt").toLocalDate(), rs.getDate("dateRetours").toLocalDate());
                      emprunt.id_Emprunt = rs.getInt("id_Emprunt");
                      Emprunt.liste_Emprunt.add(emprunt);
                }
            }
   
            rs.close();
        }catch (Exception e) {
            System.out.println(e);
        }
    } 
}    