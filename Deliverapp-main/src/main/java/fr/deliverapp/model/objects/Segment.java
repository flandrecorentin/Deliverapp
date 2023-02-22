package fr.deliverapp.model.objects;

import fr.deliverapp.model.package_diagram.Observable;

/**
 * Représente un chemin praticable entre deux Intersections A et B
 *
 * @author Hexanome H4124
 */
public class Segment extends Observable {

    /**
     * Le nom de la rue
     */
    private String nom;

    /**
     * La longueur du segment
     */
    private double distance;

    /**
     * L'intersection d'origine A
     */
    private Intersection origine;

    /**
     * L'intersection de la destination B
     */
    private Intersection destination;

    /**
     * Constructeur d'un Segment
     *
     * @param nom         le nom de la rue praticable
     * @param distance    la longueur de ce segment
     * @param origine     le point d'origine A
     * @param destination le point de destination B
     */
    public Segment(String nom, double distance, Intersection origine, Intersection destination) {
        this.nom = nom;
        this.distance = distance;
        this.origine = origine;
        this.destination = destination;
    }

    /**
     * Renvoie le nom du Segment
     *
     * @return <code>this.nom</code>
     */
    public String getNom() {
        return nom;
    }

    /**
     * Modifie le nom d'un Segment
     *
     * @param nom le nom du Segment
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Renvoie la longueur du Segment
     *
     * @return <code>this.longueur</code>
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Modifie la longueur d'un segment
     *
     * @param distance la nouvelle longueur
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * Renvoie le point d'origine (intersection) du Segment
     *
     * @return <code>this.origine</code>
     */
    public Intersection getOrigine() {
        return origine;
    }

    /**
     * Modifie le point d'origine du Segment
     *
     * @param origine le nouveau point d'origine
     */
    public void setOrigine(Intersection origine) {
        this.origine = origine;
    }

    /**
     * Renvoie le point destination (intersection) du Segment
     *
     * @return <code>this.destination</code>
     */
    public Intersection getDestination() {
        return destination;
    }

    /**
     * Modifie le point de destination du Segment
     *
     * @param destination le nouveau point d'origine
     */
    public void setDestination(Intersection destination) {
        this.destination = destination;
    }
}
