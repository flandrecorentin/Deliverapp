package fr.deliverapp.controller;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.Marker;
import fr.deliverapp.controller.command.ListOfCommands;
import fr.deliverapp.model.objects.Intersection;
import fr.deliverapp.model.objects.Livraison;
import fr.deliverapp.view.Window;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.List;

/**
 * État quand l'utilisateur choisit d'ajouter une livraison après le calcul
 * d'une tournée
 *
 * @author Hexanome H4124
 */
public class AjouterLivraisonApresCalculState implements State {

    /**
     * Valider ajout de la livraison
     *
     * @param c                            le controlleur de l'application
     * @param w                            la fenêtre
     * @param comboboxHoraire              la combobox permettant de choisir la plage horaire
     * @param listeCommande                la liste des commandes déjà effectuée par
     *                                     l'utilisateur
     * @param texteSelectionPointLivraison le label de selection du point
     * @param texteSelectionPlageHoraire   le texte de sélection de l aplage
     *                                     horaire
     * @param buttonValiderAjoutLivraison  le bouton permettant de valider
     *                                     l'ajout
     * @param buttonAnnulerAjoutLivraison  le bouton permettant d'annuler
     *                                     l'ajout d'une livraison
     */
    public void validerAjoutApresCalcul(Controller c, Window w,
                                        ComboBox comboboxHoraire, ListOfCommands listeCommande,
                                        Label texteSelectionPointLivraison, Label texteSelectionPlageHoraire,
                                        Button buttonValiderAjoutLivraison, Button buttonAnnulerAjoutLivraison) {
        boolean valide = true;
        if (c.getMarkerSelectionne() == null) {
            texteSelectionPointLivraison.setStyle("-fx-text-fill:red; -fx-font-weight: bold;");
            valide = false;
        }
        Intersection pointLivraison = null;
        if (valide) {
            texteSelectionPointLivraison.setStyle("-fx-text-fill:black; -fx-font-weight: medium;");
             pointLivraison =
                new Intersection(c.getMarkerSelectionne().getLatitude(),
                        c.getMarkerSelectionne().getLongitude());

        List<Intersection> temp = w.getPlan().getListeIntersection();
        for (Intersection i : temp) {
            if (i.sameCoordinates(pointLivraison)) {
                pointLivraison = i;
            }
         }
        }

        int horaire = -1;
        if (comboboxHoraire.getSelectionModel().getSelectedItem() != null) {
        String plageHoraire =
                comboboxHoraire.getSelectionModel().getSelectedItem().toString();
            switch (plageHoraire) {
                case "8h - 9h": {
                    horaire = 8;
                    break;
                }
                case "9h - 10h": {
                    horaire = 9;
                    break;
                }
                case "10h - 11h": {
                    horaire = 10;
                    break;
                }
                case "11h - 12h": {
                    horaire = 11;
                    break;
                }
                default: {
                    horaire = -1;
                }
             }
        }

        if (horaire == -1) {
            texteSelectionPlageHoraire.setStyle("-fx-text-fill:red; -fx-font-weight: bold;");
            return;
        }
        texteSelectionPlageHoraire.setStyle("-fx-text-fill:black; -fx-font-weight: medium;");
        if (!valide) return;

        String image = "";
        if (w.getLivreurSelectionne().getNom().equals("Bob")) {
            image = "/images/circle_red.png";
        } else {
            image = "/images/circle_blue.png";
        }

        Marker markerSelected = new Marker(getClass().getResource(image), -12, -12)
                .setPosition(new Coordinate(c.getMarkerSelectionne().getLatitude(), c.getMarkerSelectionne().getLongitude()))
                .setVisible(false);

        c.getMapView().addMarker(markerSelected);

        Livraison livraison = new Livraison(w.getPlan(), pointLivraison, horaire, w.getLivreurSelectionne(), new Pair<>(w.getMarkerSelectionne().getKey(), markerSelected));

        livraison.getMarkers().getKey().setVisible(false);
        livraison.getMarkers().getValue().setVisible(true);
        w.ajouteLivraisonsSelectionnees(livraison);

        System.out.println("Nouvelle livraison" + pointLivraison.getLon() +
                "; " + pointLivraison.getLat() + " à " + horaire + "h et " +
                "assigné par " + w.getLivreurSelectionne().getNom());

        comboboxHoraire.setVisible(false);
        comboboxHoraire.setManaged(false);
        texteSelectionPlageHoraire.setVisible(false);
        texteSelectionPointLivraison.setText("Veuillez sélectionner sur la carte le point de livraison déjà existant qui précédera votre nouveau point de livraison");
        buttonValiderAjoutLivraison.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                c.validerChoixPrecedentApresCalcul();
            }
        });
        buttonAnnulerAjoutLivraison.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                c.annulerAjoutApresCalcul();
            }
        });

        c.choisirLivraisonPrecedenteState.setLivraisonApresCalculTournee(livraison);
        c.setCurrentState(c.choisirLivraisonPrecedenteState);
        Button boutonSauvegarderLivraisons=(Button) w.getPrimaryStage().getScene().lookup("#boutonSauvegarderLivraisons");
        texteSelectionPlageHoraire.setStyle("-fx-text-fill:black; -fx-font-weight: medium;");
        boutonSauvegarderLivraisons.setStyle("-fx-text-fill:black; -fx-font-weight: medium;");
        Text text=(Text)w.getPrimaryStage().getScene().lookup("#successText");
        text.setManaged(false);
        text.setVisible(false);
        comboboxHoraire.getSelectionModel().clearSelection();
        c.setMarkerSelectionne(null);
        w.setMarkerSelectionne(null);


    }

    /**
     * @param c                            le controlleur de l'application
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
     */
    public void annulerAjoutApresCalcul(Controller c, Window w, VBox VBoxCreationLivraison,
                                        ComboBox comboboxHoraire,
                                        Label texteSelectionPointLivraison, Label texteSelectionPlageHoraire,
                                        Button buttonValiderAjoutLivraison, Button buttonAnnulerAjoutLivraison) {
        comboboxHoraire.setManaged(true);
        comboboxHoraire.setVisible(true);
        comboboxHoraire.getSelectionModel().clearSelection();
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

    }

    ;

}
