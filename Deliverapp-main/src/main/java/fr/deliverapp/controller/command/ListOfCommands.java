package fr.deliverapp.controller.command;

import java.util.LinkedList;

/**
 * Liste des commandes effectuée par l'utilisateur (permet l'undo/redo)
 *
 * @author Hexanome H4124
 */
public class ListOfCommands {
    /**
     * La liste des commandes
     */
    private LinkedList<Command> l;
    /**
     * L'indice de la dernière commande effectuée dans la liste
     */
    private int i;

    /**
     * Contructeur de ListOfCommande
     * Initialise la liste chainée de commande (i = -1)
     */
    public ListOfCommands() {
        i = -1;
        l = new LinkedList<Command>();
    }

    /**
     * Méthode qui permet l'ajout d'une commande à la liste
     *
     * @param c la commande à rajouter
     */
    public void add(Command c) {
        i++;

        if(l.size() > i){
            l.remove(l.get(i));
        }

        l.add(i, c);

        c.doCommand();
    }

    /**
     * Méthode qui permet d'annuler la dernière commande effectuée
     */
    public void undo() {
        if (i >= 0) {
            l.get(i).undoCommand();
            i--;
        }
    }

    /**
     * Méthode qui permet de revenir avant une annulation de commande
     */
    public void redo() {
        if (i == l.size() - 1)
            return;
        i++;
        l.get(i).doCommand();

    }

    public LinkedList<Command> getL() {
        return l;
    }

    public void setL(LinkedList<Command> l) {
        this.l = l;
    }
}