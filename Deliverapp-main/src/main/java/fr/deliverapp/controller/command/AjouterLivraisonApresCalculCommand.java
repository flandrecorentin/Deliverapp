package fr.deliverapp.controller.command;

import fr.deliverapp.model.objects.Livraison;
import fr.deliverapp.model.objects.Livreur;
import fr.deliverapp.model.objects.Plan;

/**
 * Commande qui ajoute une livraison après la calcul d'une tournée
 *
 * @author Hexanome H4124
 */
public class AjouterLivraisonApresCalculCommand implements Command{

    /**
     * Le Livreur assigné à cette livraison
     */
    private Livreur livreur;

    /**
     * Le Plan utilisé
     */
    private Plan plan;

    /**
     * La Livraison à ajouter
     */
    private Livraison livraisonAjoutee;

    /**
     * La Livraison préexistante dans la Tournée qui sera effectuée juste
     * après la livraison à ajouter
     */
    private Livraison livraisonPrecedente;

    /**
     * Constructeur de la commande,
     * @param plan le plan utilisé
     * @param livraisonAjoutee la livraison à ajouter
     * @param livraisonPrecedente la livraison préexistante dans la Tournée
     * @param livreur le livreur à qui on affecte la livraison
     */
    public AjouterLivraisonApresCalculCommand(Plan plan, Livraison livraisonAjoutee, Livraison livraisonPrecedente, Livreur livreur){
        this.livreur = livreur;
        this.livraisonAjoutee = livraisonAjoutee;
        this.livraisonPrecedente = livraisonPrecedente;
        this.plan = plan;
    }

    /**
     * Effectue la commande, ajoute la livraison à la tournée juste avant
     * <code>this.livraisonPrecedente</code>
     */
    @Override
    public void doCommand() {
        livreur.getTournee().ajouterLivraison(plan, livraisonAjoutee, livraisonPrecedente);
        livreur.ajouterLivraison(livraisonAjoutee);
        livraisonAjoutee.getMarkers().getKey().setVisible(false);
        livraisonAjoutee.getMarkers().getValue().setVisible(true);
    }

    /**
     * Annule la commande, retire la Livraison <code>this
     * .livraisonAjoutee</code> de la Tournée
     */
    @Override
    public void undoCommand() {
        livreur.getTournee().supprimerLivraison(plan, livraisonAjoutee);
        livreur.supprimerLivraison(livraisonAjoutee);
        if (livraisonAjoutee.getMarkers().getKey().getVisible()) {
            livraisonAjoutee.getMarkers().getKey().setVisible(false);
            livraisonAjoutee.getMarkers().getValue().setVisible(true);
        } else {
            livraisonAjoutee.getMarkers().getKey().setVisible(true);
        }
    }
}
