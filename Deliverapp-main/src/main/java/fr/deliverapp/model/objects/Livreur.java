package fr.deliverapp.model.objects;

import fr.deliverapp.model.package_diagram.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.Random;

/**
 * Représente un livreur, sa liste de livraison et sa tournée
 *
 * @author Hexanome H4124
 */
public class Livreur extends Observable {

    /**
     * Le nom du livreur
     */
    private String nom;

    /**
     * La propriété affichable de l'objet Livreur
     */
    private StringProperty stringProperty;

    /**
     * la liste de livraison assignées à ce livreur
     */
    private LinkedList<Livraison> listeLivraison;

    /**
     * La tournée de ce livreur
     */
    private Tournee tournee;

    /**
     * La couleur d'affichage de ce livreur
     */
    private Color couleur;

    /**
     * Le nombre de livraisons hors horaire
     */
    private int horsHoraire;

    /**
     * Constructeur d'un livreur à partir de son nom
     * Crée une liste de livraison vide
     *
     * @param nom le nom du livreur
     */
    public Livreur(String nom) {
        this.nom = nom;
        this.tournee = null;
        this.stringProperty = new SimpleStringProperty(nom);
        this.listeLivraison = new LinkedList<>();

        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();

        this.couleur = new Color(r, g, b, 0.5);
        this.horsHoraire = 0;
    }
    public Livreur(String nom, Color couleur) {
        this.nom = nom;
        this.stringProperty = new SimpleStringProperty(nom);
        this.listeLivraison = new LinkedList<>();
        this.couleur = couleur;
        this.horsHoraire = 0;
    }


    /**
     * Méthode qui renvoie le nom du livreur
     *
     * @return <code>this.nom</code>
     */
    public String getNom() {
        return nom;
    }

    /**
     * Méthode qui set le nom du livreur
     *
     * @param nom le nom du livreur
     */
    public void setNom(String nom) {
        this.nom = nom;
        this.stringProperty.set(nom);
    }

    /**
     * Méthode qui retourne la couleur du livreur
     *
     * @return <code>this.couleur</code>
     */
    public Color getCouleur() {
        return couleur;
    }

    /**
     * Méthode qui set la couleur du livreur
     *
     * @param couleur la couleur du livreur
     */
    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }

    /**
     * Méthode qui retourne la propriété affichable du livreur
     *
     * @return <code>this.stringProperty</code>
     */
    public StringProperty getStringProperty() {
        return stringProperty;
    }

    public int getHorsHoraire() {
        return horsHoraire;
    }

    public void setHorsHoraire(int horsHoraire) {
        this.horsHoraire = horsHoraire;
    }

    /**
     * Méthide qui renvoie la liste d elivraison assignées à ce livreur
     *
     * @return <code>this.listeLivraison</code> une liste chainée
     */
    public LinkedList<Livraison> getListeLivraison() {
        return listeLivraison;
    }

    /**
     * Méthode qui permet de modifier la liste de livvraison du livreur pour
     * une autre liste chainée
     *
     * @param listeLivraison la nouvelle liste de livraisons
     */
    public void setListeLivraison(LinkedList<Livraison> listeLivraison) {
        this.listeLivraison = listeLivraison;
    }

    /**
     * Méthode qui permet d'ajouter une livraison à la liste de livraison du
     * livreur
     *
     * @param livraison la livraison à ajouter
     */
    public void ajouterLivraison(Livraison livraison) {
        // if(!(listeLivraison == null))
        listeLivraison.add(livraison);
        notifyObservers(livraison);
    }

    /**
     * Méthode qui permet de retirer une livraison à la liste de livraison du
     * livreur
     *
     * @param livraison la livraison à retirer
     */
    public void supprimerLivraison(Livraison livraison) {
        listeLivraison.remove(livraison);
        notifyObservers(livraison);
    }

    /**
     * Méthode qui retourne la tournée d'un livreur
     *
     * @return <code>this.tournee</code>
     */
    public Tournee getTournee() {
        return this.tournee;
    }

    /**
     * Méthode qui affecte une tournée calculée à un livreur
     *
     * @param t la tournée calculée
     */
    public void setTournee(Tournee t) {
        this.tournee = t;
    }
}
