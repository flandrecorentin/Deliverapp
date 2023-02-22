package fr.deliverapp.model.package_diagram;

import java.util.ArrayList;
import java.util.Collection;

// TODO : doc à revoir

/**
 * Représente la liste de tous les élements graphiques à mettre à jour quand
 * les données sont modifiées
 *
 * @author Hexanome H4124
 */
public class  Observable {

    /**
     * Liste des élements graphiques observateurs (les observers)
     */
    private Collection<Observer> obs;

    /**
     * Constructeur
     * Initialise une liste vide d'Observer
     */
    public Observable() {
        obs = new ArrayList<Observer>();
    }

    /**
     * Ajoute un observer à la liste
     *
     * @param o
     */
    public void addObserver(Observer o) {
        if (!obs.contains(o)) obs.add(o);
    }

    /**
     * Notifie les observeurs d'un changement de donnée
     *
     * @param arg
     */
    public void notifyObservers(Object arg) {
        for (Observer o : obs)
            o.update(this, arg);
    }
}
