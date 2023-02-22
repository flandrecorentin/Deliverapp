package fr.deliverapp.model.objects;

import fr.deliverapp.model.package_diagram.Observable;

/**
 * Objet Entrepot, point de départ des tournées des livreurs
 *
 * @author Hexanome H4124
 */
public class Entrepot extends Observable {

    /**
     * L'intersection où se trouve l'entrepot
     */
    private Intersection localisation;

    /**
     * Constructeur de l'entrepot
     *
     * @param localisation l'intersection où se trouve l'entrepot
     */
    public Entrepot(Intersection localisation) {
        this.localisation = localisation;
    }

    /**
     * Méthode qui retourne l'intersection de l'entrepot
     *
     * @return localisation, l'intersection
     */
    public Intersection getLocalisation() {
        return localisation;
    }

    /**
     * Méthode qui permet de changer l'intersction de l'entrepot
     *
     * @param localisation la nouvelle intersection de l'entrepot
     */
    public void setLocalisation(Intersection localisation) {
        this.localisation = localisation;
    }
}
