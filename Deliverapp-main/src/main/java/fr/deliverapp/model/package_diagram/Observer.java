package fr.deliverapp.model.package_diagram;

/**
 * Interface Observer
 *
 * @author Hexanome H4124
 */
public interface Observer {
    /**
     * Méthode qui permet de mettre à jour l'affichage d'une donnée observée
     *
     * @param observed la donnée observée
     * @param arg
     */
    public void update(Observable observed, Object arg);
}
