package fr.deliverapp.controller.command;

/**
 * Permet d'obtenir l'inverse d'une commande pour l'annuler
 *
 * @author Hexanome H4124
 */
public class ReverseCommand implements Command {

    /**
     * La commande dont on veut l'inverse
     */
    private Command commande;

    /**
     * Constructeur
     *
     * @param commande la commande dont on veut l'inverse
     */
    public ReverseCommand(Command commande) {
        this.commande = commande;
    }

    /**
     * Méthode qui effectue l'inverse de la commande (son "undo")
     */
    public void doCommand() {
        commande.undoCommand();
    }

    /**
     * Méthode qui annule l'inverse de la commande (son "do")
     */
    public void undoCommand() {
        commande.doCommand();
    }
}