package fr.deliverapp.controller.command;

/**
 * Interface Command
 *
 * @author Hexanome H4124
 */
public interface Command {
    /**
     * Méthode qui effectue la commande
     */
    void doCommand();

    /**
     * Méthode qui annule la commande
     */
    void undoCommand();
}