package fr.deliverapp.controller;


import fr.deliverapp.controller.command.AjouterLivraisonApresCalculCommand;
import fr.deliverapp.controller.command.ListOfCommands;
import fr.deliverapp.model.objects.*;
import fr.deliverapp.view.Window;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.Date;
import java.util.List;

/**
 * État quand la Tournée est calculée pour le livreur sélectionné
 *
 * @author Hexanome H4124
 */
public class TourneeCalculeeState implements State{


    /**
     * Permet de repasser à LivreurSelectionneState quand la tournée a été
     * calculée
     *
     * @param c                            le controlleur
     * @param w                            la fenêtre
     * @param VBoxCreationLivraison        la zone où les instructions d'ajout de
     *                                     livraison sont ajoutées
     * @param comboboxHoraire              la combobox permettant de choisir la plage horaire
     * @param texteSelectionPointLivraison le label de selection du point
     * @param texteSelectionPlageHoraire   le texte de sélection de l aplage
     *                                     horaire
     * @param buttonValiderAjoutLivraison  le bouton permettant de valider
     *                                     l'ajout
     * @param buttonAnnulerAjoutLivraison  le bouton permettant d'annuler
     *                                     l'ajout d'une livraison
     * @param labelInfos                   le texte affiché
     */
    public void revenirAvantCalculTournee(Controller c, Window w,
                                   VBox VBoxCreationLivraison, Text TitreVBoxAjoutLivraison,
                                   ComboBox comboboxHoraire,
                                   Label texteSelectionPointLivraison,  Label texteSelectionPlageHoraire,
                                   Button buttonValiderAjoutLivraison,  Button buttonAnnulerAjoutLivraison,
                                          Text labelInfos){

        VBoxCreationLivraison.setVisible(false);
        texteSelectionPlageHoraire.setVisible(true);
        texteSelectionPlageHoraire.setManaged(true);
        texteSelectionPlageHoraire.setText("Sélectionner une plage horaire");
        texteSelectionPointLivraison.setVisible(true);
        texteSelectionPointLivraison.setManaged(true);
        texteSelectionPointLivraison.setText("Sélectionner sur la carte un point de livraison");
        comboboxHoraire.setVisible(true);
        comboboxHoraire.setManaged(true);
        labelInfos.setText(" ");


        buttonValiderAjoutLivraison.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                c.validerAjoutLivraison();
            }
        });
        buttonAnnulerAjoutLivraison.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                c.annulerAjoutLivraison();
            }
        });

        c.desafficherTournee(w.getLivreurSelectionne());
        c.supprimerTourneeAffichage(w.getLivreurSelectionne());

        c.setCurrentState(c.livreurSelectionneState);
        c.getCurrentState().initialiserBoutonsState(c,w);
        c.majLabelNbLivraisons();
    }

    /**
     * Transition pour l'ajout et suppression de livraison après calcul
     *
     * @param c                            le controlleur
     * @param w                            la fenetre
     * @param VBoxCreationLivraison        zone affiché pour l'ajout d'une livraison
     * @param TitreVBoxAjoutLivraison      titre de la zone ajout de livraison
     * @param texteSelectionPointLivraison le label de selection du point
     * @param texteSelectionPlageHoraire   le texte de sélection de l aplage
     *                                     horaire
     * @param buttonValiderAjoutLivraison  le bouton permettant de valider
     *                                     l'ajout
     * @param buttonAnnulerAjoutLivraison  le bouton permettant d'annuler
     *                                     l'ajout d'une livraison
     */
    public void ajouterLivraisonApresCalcul(Controller c, Window w,
                                            VBox VBoxCreationLivraison,
                                            Text TitreVBoxAjoutLivraison,
                                            Label texteSelectionPointLivraison,
                                            Label texteSelectionPlageHoraire,
                                            Button buttonValiderAjoutLivraison,
                                            Button buttonAnnulerAjoutLivraison){
        TitreVBoxAjoutLivraison.setText("Ajouter une livraison après calcul de tournée pour " + w.getLivreurSelectionne().getNom());
        TitreVBoxAjoutLivraison.getStyleClass().add("header1");
        VBoxCreationLivraison.setVisible(true);
        VBoxCreationLivraison.setManaged(true);
        buttonValiderAjoutLivraison.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                c.validerAjoutApresCalcul();
            }
        });
        buttonAnnulerAjoutLivraison.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                c.annulerAjoutApresCalcul();
            }
        });
        c.setCurrentState(c.ajouterLivraisonApresCalculState);
        c.majLabelNbLivraisons();
    }

    /**
     * Supprime la livraison quand la tournée est déja calculée
     *
     * @param c                            le controlleur
     * @param w                            la fenêtre
     * @param VBoxCreationLivraison        la zone affichée lors de la création
     *                                     d'une livraison
     * @param TitreVBoxAjoutLivraison      le titre de la zone de création d'une
     *                                     livraison
     * @param comboboxHoraire              la combobox de la plage horaire
     * @param listeCommande                la liste des commandes effectuées
     * @param texteSelectionPointLivraison le texte qui indique comment
     *                                     sélectionner un point de livraison
     * @param texteSelectionPlageHoraire   le texte qui indique comment
     *                                     sélectionner une plage horaire
     * @param buttonValiderAjoutLivraison  bouton pour valider l'ajout de la
     *                                     livraison
     * @param buttonAnnulerAjoutLivraison  bouton pour annuler l'ajout de la
     *                                     livraison
     */
    public void supprimerLivraisonApresCalcul(Controller c, Window w, VBox VBoxCreationLivraison,
                                              Text TitreVBoxAjoutLivraison,
                                              ComboBox comboboxHoraire, ListOfCommands listeCommande,
                                              Label texteSelectionPointLivraison, Label texteSelectionPlageHoraire,
                                              Button buttonValiderAjoutLivraison, Button buttonAnnulerAjoutLivraison){

        VBoxCreationLivraison.setManaged(true);
        VBoxCreationLivraison.setVisible(true);
        comboboxHoraire.setVisible(false);
        texteSelectionPlageHoraire.setVisible(false);
        texteSelectionPointLivraison.setText("Veuillez choisir la livraison à supprimer sur la carte");
        TitreVBoxAjoutLivraison.setText("Suppression d'un livraison après le calcul d'une tournée");

        buttonValiderAjoutLivraison.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                c.confirmerSuppressionLivraisonApresCalcul();
            }
        });
        buttonAnnulerAjoutLivraison.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                c.annulerSuppressionApresCalcul();
            }
        });
        c.setCurrentState(c.supprimerLivraisonApresCalculState);
        c.majLabelNbLivraisons();
    }

    public void terminer(Controller c, Window w) {
        c.setCurrentState(c.livreurSelectionneState);
    }

    public void initialiserBoutonsState(Controller c, Window w) {
        boolean afficher = false;
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
        }else if(w.getPrimaryStage().getScene().lookup("#boutonGenerationFeuilleDeRoute") != null){
            w.getPrimaryStage().getScene().lookup("#boutonGenerationFeuilleDeRoute").setVisible(!afficher);
            w.getPrimaryStage().getScene().lookup("#boutonGenerationFeuilleDeRoute").setManaged(!afficher);
        }
        if(c.boutonRevenirAvantCalculTournee != null) {
            c.boutonRevenirAvantCalculTournee.setVisible(!afficher);
            c.boutonRevenirAvantCalculTournee.setManaged(!afficher);
        }
        if(c.boutonChangerCarte != null) {
            c.boutonChangerCarte.setVisible(afficher);
            c.boutonChangerCarte.setManaged(afficher);
        }

        c.labelInfos.setVisible(true);
        c.labelNombreLivraisonLivreur.setVisible(true);
        c.labelTournee.setText("Tournee de " + w.getLivreurSelectionne().getNom());

        c.BorderPaneInfoLivreur.setVisible(true);
        c.BorderPaneInfoLivreur.setManaged(true);

        c.desactiverBoutonsEditionSupp();

        List<Pair<Livraison, Date>> listeLivraisons = w.getLivreurSelectionne().getTournee().getListeLivraison();
        c.labelInfos.setText("Horaire de la première livraison : " + listeLivraisons.get(0).getValue().getHours() + "h" + listeLivraisons.get(0).getValue().getMinutes() + "min  " +
                "\n" + "Horaire de la dernière : " + listeLivraisons.get(listeLivraisons.size() - 1).getValue().getHours() + "h" + listeLivraisons.get(listeLivraisons.size() - 1).getValue().getMinutes() + "min  ");
        c.labelNombreLivraisonLivreur.setText(w.getLivreurSelectionne().getListeLivraison().size() + " livraison(s) au total");
    }

    public void majBoutonNbLivraisons(Controller c, Window w) {
        c.labelNombreLivraisonLivreur.setText(w.getLivreurSelectionne().getListeLivraison().size() + " livraison(s) au total");
    }

    public void undo(ListOfCommands listeCommande) {
        listeCommande.undo();
    }

    public void redo(ListOfCommands listeCommande) {
        listeCommande.redo();
    }
}
