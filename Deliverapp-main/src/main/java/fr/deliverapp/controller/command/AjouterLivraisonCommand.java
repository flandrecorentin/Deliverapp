package fr.deliverapp.controller.command;

import fr.deliverapp.model.objects.Livraison;
import fr.deliverapp.model.objects.Livreur;

/**
 * Commande qui ajoute une livraison à un livreur
 *
 * @author Hexanome H4124
 */
public class AjouterLivraisonCommand implements Command {

    private Livreur livreur;
    private Livraison livraison;

    /**
     * Construsteur de la commande
     *
     * @param livraison la livraison a affecter
     * @param livreur   le livreur à qui on affcte la livraison
     */
    public AjouterLivraisonCommand(Livraison livraison, Livreur livreur) {
        this.livreur = livreur;
        this.livraison = livraison;
    }

    /**
     * Méthode qui ajoute la livraison de la liste de livraison du livreur
     */
    @Override
    public void doCommand() {
        livreur.ajouterLivraison(livraison);
        livraison.getMarkers().getKey().setVisible(false);
        livraison.getMarkers().getValue().setVisible(true);
    }

    /**
     * Méthode qui retire la livraison de la liste de livraison du livreur
     */
    @Override
    public void undoCommand() {
        livreur.supprimerLivraison(livraison);
        if(livraison.getMarkers().getKey().getVisible()){
            livraison.getMarkers().getKey().setVisible(false);
            livraison.getMarkers().getValue().setVisible(true);
        }else{
            livraison.getMarkers().getKey().setVisible(true);
            livraison.getMarkers().getValue().setVisible(false);
        }
    }
}
