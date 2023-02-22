package fr.deliverapp.controller.command;

import com.sothawo.mapjfx.MapView;
import fr.deliverapp.model.objects.Livraison;
import fr.deliverapp.model.objects.Livreur;
import fr.deliverapp.view.Window;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.Map;


/**
 * Commande qui modifie le livreur d'une livraison
 */
public class ModifierLivraisonCommand implements Command{

    /**
     * L'ancien livreur
     */
    private Livreur ancien_livreur;

    /**
     * Le nouveau livreur
     */
    private Livreur nouveau_livreur;

    /**
     * La livraison
     */
    private Livraison livraison;

    /**
     * Conctructeur de la commande
     * @param livraison la livraison
     * @param window la fenêtre
     * @param mapView la carte en graphique
     */
    public ModifierLivraisonCommand(Livraison livraison, Window window, MapView mapView){
        Label titrePopup = new Label("A quel livreur voulez-vous attribuer la livraison ?");
        BorderPane border = new BorderPane();
        border.setTop(titrePopup);
        ComboBox listeLivreur = new ComboBox();
        titrePopup.setStyle("-fx-font-size:16px; -fx-font-weight: bold;");
        listeLivreur.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 30; " +
                "-fx-background-insets: 0,1,2,3,0; " +
                "-fx-border-color: #000000 ; " +
                "-fx-border-radius: 30 ; " +
                "-fx-text-fill: #000000;" +
                "-fx-font-weight: bold; " +
                "-fx-font-family: Poppins; " +
                "-fx-padding: 10 20 10 20; " +
                "-fx-cursor:hand;");
        titrePopup.setAlignment(Pos.CENTER);
        Button validerModification = new Button("Valider");
        validerModification.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 30; " +
                "-fx-background-insets: 0,1,2,3,0; " +
                "-fx-border-color: #000000 ; " +
                "-fx-border-radius: 30 ; " +
                "-fx-text-fill: #000000;" +
                "-fx-font-weight: bold; " +
                "-fx-font-family: Poppins; " +
                "-fx-padding: 10 20 10 20; " +
                "-fx-cursor:hand;");
        validerModification.setAlignment(Pos.CENTER);
        listeLivreur.setPromptText("Selectionner un livreur");
        ObservableList<String> listelivreurString = FXCollections.observableArrayList();
        for(Livreur livreur : window.getLivreurs()){
            listelivreurString.add(livreur.getNom());
        }
        listeLivreur.setItems(listelivreurString);
        border.setCenter(listeLivreur);
        border.setBottom(validerModification);
        Scene ancien_scene = window.getPrimaryStage().getScene();
        border.setStyle("-fx-background-color : white; -fx-border-color:black");
        final Stage dialog = new Stage();
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(window.getPrimaryStage());
        Scene dialogScene = new Scene(border, 400, 250);
        dialog.setScene(dialogScene);
        dialog.show();
        final String[] nomNouveauLivreur = {""};
        EventHandler<ActionEvent> event =
                new EventHandler<ActionEvent>() {

                    public void handle(ActionEvent e)
                    {
                        if (listeLivreur.getSelectionModel().getSelectedItem()!=null) {
                            nomNouveauLivreur[0] = listeLivreur.getSelectionModel().getSelectedItem().toString();
                            System.out.println("nouveau livreur " + nomNouveauLivreur[0]);
                            modifierLivreur(nomNouveauLivreur[0], livraison, window, mapView);
                            doCommand();
                            window.getPrimaryStage().setScene(ancien_scene);
                            dialog.hide();
                        } else{
                          Label erreur = new Label("Veuillez sélectionner un livreur");
                          erreur.setStyle("-fx-text-fill:red;");
                          VBox VBoxerreur = new VBox();
                          VBoxerreur.getChildren().add(validerModification);
                          VBoxerreur.getChildren().add(erreur);
                          border.setBottom(VBoxerreur);
                        }

                    }
                };
        validerModification.setOnAction(event);


    }

    /**
     * Modifie le livreur de la livraison
     * @param nomNouveauLivreur le nom du nouveau livreur
     * @param livraison la livraison
     * @param window la fenêtre
     * @param mapView la carte graphique
     */
    private void modifierLivreur(String nomNouveauLivreur, Livraison livraison, Window window, MapView mapView){
        if(nomNouveauLivreur == window.getLivreurs().get(0).getNom()){
            this.nouveau_livreur = window.getLivreurs().get(0);
            livraison.changerCouleurMarker("red", mapView);
        } else{
            this.nouveau_livreur = window.getLivreurs().get(1);
            livraison.changerCouleurMarker("blue", mapView);
        }
        this.livraison = livraison;
        this.ancien_livreur = livraison.getLivreur();
    }

    /**
     * Effectue la commande
     */
    @Override
    public void doCommand() {
        if(ancien_livreur !=null) {
            ancien_livreur.supprimerLivraison(livraison);
            livraison.setLivreur(nouveau_livreur);
            nouveau_livreur.ajouterLivraison(livraison);
            System.err.println("Nouveau_Livreur : " + nouveau_livreur.getNom() + "\n Ancien_Livreur : " + ancien_livreur.getNom() + "\n Livraison : " + livraison.getLivreur().getNom());
        }
    }

    /**
     * Annule la commande
     */
    @Override
    public void undoCommand() {
        nouveau_livreur.supprimerLivraison(livraison);
        livraison.setLivreur(ancien_livreur);
        ancien_livreur.ajouterLivraison(livraison);    }
}
