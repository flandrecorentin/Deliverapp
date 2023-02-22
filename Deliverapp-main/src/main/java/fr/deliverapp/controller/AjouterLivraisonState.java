package fr.deliverapp.controller;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.event.MapViewEvent;
import fr.deliverapp.controller.command.AjouterLivraisonCommand;
import fr.deliverapp.controller.command.ListOfCommands;
import fr.deliverapp.model.objects.Intersection;
import fr.deliverapp.model.objects.Livraison;
import fr.deliverapp.view.Window;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.List;

/**
 * Etat quand le livreur est sélectionné et que l'utilisateur souhaite lui
 * ajouter une livraison
 *
 * @author Hexanome H4124
 */
public class AjouterLivraisonState implements State {

    /**
     * Valide l'ajout d'une livraison
     *
     * @param c                            le controlleur
     * @param w                            la fenêtre
     * @param comboboxHoraire              la combobox de la plage horaire
     * @param listeCommande                la liste des commandes effectuées
     * @param VBoxCreationLivraison        la zone affichée pour créer une livraison
     * @param texteSelectionPointLivraison le texte qui indique comment
     *                                     sélectionner un point de livraison
     * @param texteSelectionPlageHoraire   le texte qui indique comment
     *                                     sélectionner une plage horaire
     */
    public void validerAjoutLivraison(Controller c, Window w,
                                      ComboBox comboboxHoraire,
                                      ListOfCommands listeCommande,
                                      VBox VBoxCreationLivraison, Label texteSelectionPointLivraison,
                                      Label texteSelectionPlageHoraire) {

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

        listeCommande.add(new AjouterLivraisonCommand(livraison, w.getLivreurSelectionne()));
        Button btn = (Button) w.getPrimaryStage().getScene().lookup("#boutonSauvegarderLivraisons");
        Label label = (Label) w.getPrimaryStage().getScene().lookup("#textGreen");
        label.setVisible(false);
        btn.setVisible(true);


        System.out.println("Nouvelle livraison" + pointLivraison.getLon() +
                "; " + pointLivraison.getLat() + " à " + horaire + "h et " +
                "assigné par " + w.getLivreurSelectionne().getNom());

            VBoxCreationLivraison.setVisible(false);
            VBoxCreationLivraison.setManaged(false);
            comboboxHoraire.getSelectionModel().clearSelection();
            c.setMarkerSelectionne(null);
            c.setCurrentState(c.livreurSelectionneState);
            Button boutonSauvegarderLivraisons=(Button) w.getPrimaryStage().getScene().lookup("#boutonSauvegarderLivraisons");
            boutonSauvegarderLivraisons.setStyle("-fx-border-color: black");
            Text text=(Text)w.getPrimaryStage().getScene().lookup("#successText");
            text.setManaged(false);
            text.setVisible(false);
            c.getMapView().addEventHandler(MapViewEvent.ANY, event -> {
                for(Livraison l : w.getLivraisons()){
                    if(l.getMarkers().getValue().getVisible()){
                        l.getMarkers().getKey().setVisible(false);
                    }
                }
            });
    }

    /**
     * Annule l'ajout d'une livraison, revient à LivreurSelectionneState
     *
     * @param c                     le controlleur
     * @param w                     la fenêtre
     * @param comboboxHoraire       la combobox de selection de la plage horaire
     * @param VBoxCreationLivraison la zone d'affichage de création de
     *                              livraison
     */
    public void annulerAjoutLivraison(Controller c, Window w, VBox VBoxCreationLivraison, ComboBox comboboxHoraire) {
        comboboxHoraire.getSelectionModel().clearSelection();
        VBoxCreationLivraison.setVisible(false);
        VBoxCreationLivraison.setManaged(false);
        c.setMarkerSelectionne(null);
        c.setCurrentState(c.livreurSelectionneState);
    }
}
