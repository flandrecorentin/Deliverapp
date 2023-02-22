package fr.deliverapp.controller;

import fr.deliverapp.controller.command.ListOfCommands;
import fr.deliverapp.model.objects.Livraison;
import fr.deliverapp.model.objects.Livreur;
import fr.deliverapp.model.objects.Tournee;
import fr.deliverapp.view.Window;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.Date;
import java.util.List;

/**
 * État suivant PrincipalState lorsque que l'on sélectionne un livreur
 *
 * @author Hexanome H4124
 */
public class LivreurSelectionneState implements State {
    /**
     * Désélectionne le livreur, revient à PrincipalState
     *
     * @param c                         le controlleur
     * @param w                         la fenêtre
     * @param colonneSelectionLivraison la colonne où on sélectionne la
     *                                  livraison
     * @param BorderPaneInfoLivreur     la zone des infos du livreur
     * @param boutonAjouterLivraison    le bouton pour ajouter une livraison
     */
    public void deselectionnerLivreur(Controller c, Window w, TableColumn<Livraison, String> colonneSelectionLivraison,
                                      BorderPane BorderPaneInfoLivreur, Button boutonAjouterLivraison, Button boutonCalculerTournee) {


        for(Livreur l : w.getLivreurs()){
            if (c.tourneeDejaCalculee(l) && l.getNom() != w.getLivreurSelectionne().getNom()){
                c.ajouterTourneeAffichage(l);
                c.afficherTournee(l);
            }
        }

        w.setLivreurSelectionne(null);
        c.setCurrentState(c.principalState);
        c.getCurrentState().initialiserBoutonsState(c,w);
        c.activerBoutonsTousLivreurs();
    }

    /**
     * Intention d'ajout d'une livraison
     *
     * @param c                       le controlleur
     * @param w                       la fenêtre
     * @param VBoxCreationLivraison   la zone affichée pour les instructions
     *                                d'ajout
     * @param TitreVBoxAjoutLivraison le texte
     */
    public void ajouterLivraison(Controller c, Window w, VBox VBoxCreationLivraison, Text TitreVBoxAjoutLivraison) {
        TitreVBoxAjoutLivraison.setText("Ajouter une livraison pour " + w.getLivreurSelectionne().getNom());
        TitreVBoxAjoutLivraison.getStyleClass().add("header1");
        VBoxCreationLivraison.setVisible(true);
        VBoxCreationLivraison.setManaged(true);
        c.setCurrentState(c.ajouterLivraisonState);
    }

    /**
     * Calcule la tournée pour le livreur sélectionné
     *
     * @param c          le controlleur
     * @param w          la fenêtre
     */
    public void calculerTournee(Controller c, Window w) {

        if(w.getLivreurSelectionne().getListeLivraison().size() == 0){
            return;
        }

        try {
            System.out.println(w.getLivreurSelectionne().getListeLivraison());
            //todo : etre sur que t doit etre dans le state
            Tournee t = new Tournee(w.getPlan(), w.getLivreurSelectionne().getListeLivraison(), w.getLivreurSelectionne());
            if (t.getListeTrajet() != null) {
                w.getLivreurSelectionne().setTournee(t);
                for(Livreur l : w.getLivreurs()){
                    if(l.getTournee()!=null){
                        c.desafficherTournee(l);
                    }
                }
                c.ajouterTourneeAffichage(w.getLivreurSelectionne());
                c.afficherTournee(w.getLivreurSelectionne());
                if(w.getLivreurSelectionne().getTournee().calculLivraisonsNonValides().size() != 0){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Attention !");

                    // Header Text: null
                    alert.setHeaderText(null);
                    alert.setContentText("Certaines livraisons ne peuvent pas être livrées à temps (elles seront entourées en rouge dans la vue textuelle).");

                    alert.showAndWait();
                }
                c.setCurrentState(c.tourneeCalculeeState);
                c.getCurrentState().initialiserBoutonsState(c,w);

            }
            else {
                c.labelInfos.setText("Tournée non calculable, due à une impasse à sens unique");
            }
        }
        catch (Exception e) {
            c.labelInfos.setText("Tournée non calculable, due à une impasse à sens unique");
        }
    }

    /**
     * Importation d'une nouvelle carte à partir d'un fichier XML
     *
     * @param c le controlleur de l'application
     * @param w la fenêtre
     */
    public void changerCarte(Controller c, Window w) {
        c.setCurrentState(c.selectionCarteState);
    }

    /**
     * Annule la dernière commande effectuée
     * @param listeCommande la liste des commandes effectuée
     */
    public void undo(ListOfCommands listeCommande) {
        listeCommande.undo();
    }

    /**
     * Effectue la dernière commande annulée
     * @param listeCommande la liste des commandes effectuée
     */
    public void redo(ListOfCommands listeCommande) {
        listeCommande.redo();
    }

    public void initialiserBoutonsState(Controller c, Window w) {
        boolean afficher = true;

        if(c.boutonAjouterLivraison != null) {
            c.boutonAjouterLivraison.setVisible(afficher);
            c.boutonAjouterLivraison.setManaged(afficher);
        }
        if(c.boutonCalculerTournee != null) {
            c.boutonCalculerTournee.setVisible(afficher);
            c.boutonCalculerTournee.setManaged(afficher);
        }
        if(c.boutonAjouterLivraisonApresCalcul != null) {
            c.boutonAjouterLivraisonApresCalcul.setVisible(!afficher);
            c.boutonAjouterLivraisonApresCalcul.setManaged(!afficher);
        }
        if(c.boutonSupprimerLivraisonApresCalcul != null) {
            c.boutonSupprimerLivraisonApresCalcul.setVisible(!afficher);
            c.boutonSupprimerLivraisonApresCalcul.setManaged(!afficher);
        }
        if(c.boutonGenerationFeuilleDeRoute != null) {
            c.boutonGenerationFeuilleDeRoute.setVisible(!afficher);
            c.boutonGenerationFeuilleDeRoute.setManaged(!afficher);
        }
        if(c.boutonRevenirAvantCalculTournee != null) {
            c.boutonRevenirAvantCalculTournee.setVisible(!afficher);
            c.boutonRevenirAvantCalculTournee.setManaged(!afficher);
        }
        if(c.boutonChangerCarte != null) {
            c.boutonChangerCarte.setVisible(!afficher);
            c.boutonChangerCarte.setManaged(!afficher);
        }

        c.activerBoutonsEditionSupp();

        c.labelInfos.setVisible(true);
        c.labelNombreLivraisonLivreur.setVisible(true);
        c.labelTournee.setText("Tournee de " + w.getLivreurSelectionne().getNom());

        c.BorderPaneInfoLivreur.setVisible(true);
        c.BorderPaneInfoLivreur.setManaged(true);

        if(w.getLivreurSelectionne().getListeLivraison() != null) {
            c.labelNombreLivraisonLivreur.setText(w.getLivreurSelectionne().getListeLivraison().size() + " livraison(s) au total");
        }
        else {
            c.labelNombreLivraisonLivreur.setText("0 livraison(s) au total");
        }

        if(afficher) {
            c.VBoxCreationLivraison.setVisible(false);
            c.texteSelectionPlageHoraire.setVisible(true);
            c.texteSelectionPlageHoraire.setManaged(true);
            c.texteSelectionPlageHoraire.setText("Sélectionner une plage horaire");
            c.texteSelectionPointLivraison.setVisible(true);
            c.texteSelectionPointLivraison.setManaged(true);
            c.texteSelectionPointLivraison.setText("Sélectionner sur la carte un point de livraison");
            c.comboboxHoraire.setVisible(true);
            c.comboboxHoraire.setManaged(true);
            c.labelInfos.setText(" ");


            c.buttonValiderAjoutLivraison.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    c.validerAjoutLivraison();
                    c.majLabelNbLivraisons();
                }
            });
            c.buttonAnnulerAjoutLivraison.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    c.annulerAjoutLivraison();
                }
            });
        }
    }

    public void majLabelNbLivraisons(Controller c, Window w) {
        c.labelNombreLivraisonLivreur.setText(w.getLivreurSelectionne().getListeLivraison().size() + " livraison(s) au total");
    }
}
