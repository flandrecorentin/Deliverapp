package fr.deliverapp.controller;

import fr.deliverapp.view.Window;

/**
 * État initial au lancement de l'application
 *
 * @author Hexanome H4124
 */
public class InitialState implements State {

    /**
     * Créer une nouvelle tournée à partir de zéro
     *
     * @param c la controlleur de l'application
     * @param w la fenêtre
     */
    public void creerTournee(Controller c, Window w) {
        w.setChoixCarteWindow();
        c.setCurrentState(c.selectionCarteState);
    }

    /**
     * Choix d'importer une tournée déja existante à partir d'une sauvegarde
     *
     * @param c la controlleur de l'application
     * @param w la fenêtre
     */
    public void importerTournee(Controller c, Window w) {
        w.setChoixImporterWindow();
        c.setCurrentState(Controller.importerListeLivraisonState);
    }

}
