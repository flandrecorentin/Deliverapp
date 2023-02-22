package fr.deliverapp.model.objects;

import fr.deliverapp.model.package_diagram.Observable;

/**
 * Représente un point de livraison possible sur la carte
 */
public class Intersection extends Observable {

    /**
     * L'id de l'intersection
     */
    private long id;

    /**
     * La latitude de l'intersection
     */
    private double lat;

    /**
     * La longitude de l'intersection
     */
    private double lon;

    /**
     * Constructeur
     *
     * @param id  l'id de l'intersection
     * @param lat latitude
     * @param lon logitude
     */
    public Intersection(long id, double lat, double lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * Constructeur
     *
     * @param lat latitude
     * @param lon logitude
     */
    public Intersection(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
        this.id = id;
    }

    /**
     * Méthode qui permet de savoir si deux intersections ont la même
     * position
     *
     * @param i
     * @return <code>true</code> si les coordonnées sont les mêmes;
     * <code>false</code> sinon
     */
    public boolean sameCoordinates(Intersection i) {
        return (lat == i.lat && lon == i.lon);
    }

    /**
     * Méthode qui renvoie l'id d'une intersection
     *
     * @return <code>this.id</code>
     */
    public long getId() {
        return id;
    }

    /**
     * Méthode qui affecte un id à une intersection
     *
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Méthode qui renvoie la latitude d'une intersection
     *
     * @return <code>this.lat</code>
     */
    public double getLat() {
        return lat;
    }

    /**
     * Méthode qui affecte la position d'une intersection (latitude)
     *
     * @param lat
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * Méthode qui renvoie la longitude d'une intersection
     *
     * @return <code>this.long</code>
     */
    public double getLon() {
        return lon;
    }

    /**
     * Méthode qui affecte la position d'une intersection (longitude)
     *
     * @param lon
     */
    public void setLon(double lon) {
        this.lon = lon;
    }
}
