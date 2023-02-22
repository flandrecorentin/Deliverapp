package fr.deliverapp.model.objects;

import fr.deliverapp.model.package_diagram.Observable;

import java.util.LinkedList;
import java.util.List;

/**
 * Représente un trajet entre deux intersections via une liste de segments
 * praticables
 */
public class Trajet extends Observable {

    /**
     * La liste des Segments à suivre pour effectuer le Trajet
     */
    private List<Segment> listeSegment;

    /**
     * Constructeur de Trajet à partir d'une liste de Segment
     *
     * @param listeSegment la liste des Segments à suivre pour effectuer le
     *                     Trajet
     */
    public Trajet(List<Segment> listeSegment) {
        this.listeSegment = listeSegment;
    }

    /**
     * Constructeur de Trajet vide
     * Initialise une liste de Segments vide
     */
    public Trajet() {
        this.listeSegment = new LinkedList<>();
    }

    /**
     * Renvoie la liste de segments composant le Trajet
     *
     * @return <code>this.listeSegment</code>
     */
    public List<Segment> getListeSegment() {
        return listeSegment;
    }

    /**
     * Modifie la liste de segments du Trajet
     *
     * @param listeSegment la nouvelle liste de segments
     */
    public void setListeSegment(List<Segment> listeSegment) {
        this.listeSegment = listeSegment;
    }

    /**
     * Ajoute un segment supplémentaire à la fin de la liste
     *
     * @param segmentARajouter le segment à rajouter à la fin
     */
    public void ajouterSegmentAListeSegmentALaFin(Segment segmentARajouter) {
        listeSegment.add(segmentARajouter);
    }

    /**
     * Ajoute un segment supplémentaire au début de la liste
     *
     * @param segmentARajouter le segment à rajouter au début
     */
    public void ajouterSegmentAListeSegmentAuDebut(Segment segmentARajouter) {
        this.listeSegment.add(0, segmentARajouter);
    }
}
