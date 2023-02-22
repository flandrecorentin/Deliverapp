package fr.deliverapp.controller;

import com.sothawo.mapjfx.Marker;
import fr.deliverapp.controller.command.AjouterLivraisonApresCalculCommand;
import fr.deliverapp.controller.command.ListOfCommands;
import fr.deliverapp.controller.command.ReverseCommand;
import fr.deliverapp.model.objects.Intersection;
import fr.deliverapp.model.objects.Livraison;
import fr.deliverapp.view.Window;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.Date;
import java.util.List;

/**
 * État quand une livraison est supprimée après le calcul d'une Tournée
 *
 * @author Hexanome H4124
 */
public class SupprimerLivraisonApresCalculState implements State{

    /**
     * Confirmer la suppression d'une des livraisons après le calcul de la
     * tournée
     *
     * @param c                            le controlleur
     * @param w                            la fenêtre
     * @param VBoxCreationLivraison        la zone affichée lors de l'ajout
     *                                     d'une livraison
     * @param TitreVBoxAjoutLivraison
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
     * @param labelInfos                   le texte d'info affiché
     */
    public void confirmerSuppressionLivraisonApresCalcul(Controller c, Window w, VBox VBoxCreationLivraison,
                                                         Text TitreVBoxAjoutLivraison,
                                                         ComboBox comboboxHoraire, ListOfCommands listeCommande,
                                                         Label texteSelectionPointLivraison, Label texteSelectionPlageHoraire,
                                                         Button buttonValiderAjoutLivraison, Button buttonAnnulerAjoutLivraison, Text labelInfos){


        if(w.getLivreurSelectionne().getTournee().getListeLivraison().size() == 1){

            texteSelectionPointLivraison.setText("La livraison n'est pas suppressible car elle est unique, veuillez annuler le calcul de tournée.");
            texteSelectionPointLivraison.setStyle("-fx-text-fill: red;");
            return;
        }

        Marker m = null;
        if(w.getMarkerSelectionne() != null) m= w.getMarkerSelectionne().getKey();
        if (m == null) {
            texteSelectionPointLivraison.setStyle("-fx-text-fill:red; -fx-font-weight: bold;");
            return;
        }

        texteSelectionPointLivraison.setStyle("-fx-text-fill:black; -fx-font-weight: medium;");
        Boolean valide = false;
        List<Pair<Livraison, Date>> listeLivraisons = w.getLivreurSelectionne().getTournee().getListeLivraison();
        for(Pair<Livraison,Date> pair : listeLivraisons){
            if(pair.getKey().getMarkers().getKey() == m || pair.getKey().getMarkers().getValue() == m){
                c.desafficherTournee(w.getLivreurSelectionne());
                listeCommande.add(new ReverseCommand(new AjouterLivraisonApresCalculCommand(w.getPlan(), pair.getKey(), new Livraison(new Intersection(0.0,0.0),8), w.getLivreurSelectionne())));
                c.ajouterTourneeAffichage(w.getLivreurSelectionne());
                w.supprimerLivraisonsSelectionnees(pair.getKey());
                c.getMapView().removeMarker(pair.getKey().getMarkers().getValue());
                c.afficherTournee(w.getLivreurSelectionne());
                valide = true;
                texteSelectionPointLivraison.setStyle("-fx-text-fill:black; -fx-font-weight: medium;");
                break;
            }
        }

        if(!valide){
            texteSelectionPointLivraison.setText("Veuillez choisir un ancien point de livraison");
            texteSelectionPointLivraison.setStyle("-fx-text-fill:red; -fx-font-weight: bold;");
            return;
        }

        comboboxHoraire.setManaged(true);
        comboboxHoraire.setVisible(true);
        texteSelectionPointLivraison.setText("Selectionner sur la carte le point de livraison");
        texteSelectionPlageHoraire.setVisible(true);

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

        VBoxCreationLivraison.setManaged(false);
        VBoxCreationLivraison.setVisible(false);

        List<Pair<Livraison, Date>> l = w.getLivreurSelectionne().getTournee().getListeLivraison();
        labelInfos.setText("Horaire de la première livraison : "+l.get(0).getValue().getHours()+"h"+l.get(0).getValue().getMinutes()+"min  "+
                "\n"+"Horaire de la dernière livraison : "+l.get(l.size()-1).getValue().getHours()+"h"+l.get(l.size()-1).getValue().getMinutes()+"min  ");


        c.setCurrentState(c.tourneeCalculeeState);
        c.setMarkerSelectionne(null);
        w.setMarkerSelectionne(null);

        Text labelNbLiv = (Text)w.getPrimaryStage().getScene().lookup("#labelNombreLivraisonLivreur");
        labelNbLiv.setText(w.getLivreurSelectionne().getTournee().getListeLivraison().size()+" livraison(s) au total");



    };

    /**
     * Annuler la suppression après le calcul
     *
     * @param c                            le controlleur
     * @param w                            la fenêtre
     * @param VBoxCreationLivraison        la zone affichée lors de la création
     *                                     d'une livraison
     * @param comboboxHoraire              la combobox de la plage horaire
     * @param texteSelectionPointLivraison le texte qui indique comment
     *                                     sélectionner un point de livraison
     * @param texteSelectionPlageHoraire   le texte qui indique comment
     *                                     sélectionner une plage horaire
     * @param buttonValiderAjoutLivraison  bouton pour valider l'ajout de la
     *                                     livraison
     * @param buttonAnnulerAjoutLivraison  bouton pour annuler l'ajout de la
     *                                     livraison
     */
    public void annulerSuppressionApresCalcul(Controller c, Window w, VBox VBoxCreationLivraison,
                                              ComboBox comboboxHoraire,
                                              Label texteSelectionPointLivraison, Label texteSelectionPlageHoraire,
                                              Button buttonValiderAjoutLivraison, Button buttonAnnulerAjoutLivraison){



        comboboxHoraire.setManaged(true);
        comboboxHoraire.setVisible(true);
        texteSelectionPointLivraison.setText("Selectionner sur la carte le point de livraison");
        texteSelectionPlageHoraire.setVisible(true);

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

        VBoxCreationLivraison.setManaged(false);
        VBoxCreationLivraison.setVisible(false);

        c.setCurrentState(c.tourneeCalculeeState);

    };


}
