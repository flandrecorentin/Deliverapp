/*
 Copyright 2015-2020 Peter-Josef Meisch (pj.meisch@sothawo.com)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package fr.deliverapp.view;

import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.Projection;
import fr.deliverapp.controller.Controller;
import fr.deliverapp.model.objects.Livraison;
import fr.deliverapp.model.objects.Livreur;
import fr.deliverapp.model.objects.Plan;
import fr.deliverapp.model.package_diagram.Observable;
import fr.deliverapp.model.package_diagram.Observer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Fenêtre de l'application Deliverapp
 * Basé sur le projet mapjfx-demo de @sothawo
 *
 * @author Hexanome H4124
 */
public class Window extends Application implements Observer {

    /**
     * Le logger utilisé pour débugger la map
     */
    private static final Logger logger = LoggerFactory.getLogger(Window.class);
    /**
     * La liste des livreurs affichés à l'écran
     */
    private static ObservableList<Livreur> livreurs = FXCollections.observableArrayList();
    /**
     * Le Stage affiché à l'écran
     */
    private Stage primaryStage;

    /**
     * Le controlleur de la fenêtre
     */
    private Controller controller;

    /**
     * Le plan affiché (intersections et segments cliquable / praticables)
     */
    private Plan plan;
    /**
     * La liste des livraisons affichées à l'écran
     */
    private ObservableList<Livraison> livraisons = FXCollections.observableArrayList();

    /**
     * Le livreur sélectionné par l'utilisateur
     * Au lancement de l'application il est initialisé à <code>null</code>
     */
    private Livreur livreurSelectionne = null;

    /**
     * Le Marker sélectionné par l'utilisateur (point de livraison)
     * Au lancement de l'application il est initialisé à <code>null</code>
     */
    private Pair<Marker, Marker> markerSelectionne = null;

    /**
     * La liste des livraisons selectionnées
     */
    private List<Livraison> livraisonsSelectionnees = new ArrayList<>();


    /**
     * Constructeur de Window
     * Initialise les variables, crée les livreurs et ajoute les observeurs
     */
    public Window() {
        this.controller = new Controller();
        this.plan = new Plan();
        plan.addObserver(this);

        // Ajout des livreurs
        livreurs.add(new Livreur("Bob", Color.RED));
        livreurs.add(new Livreur("Patrick", Color.BLUE));

        for (Livreur l : livreurs) {
            l.addObserver(this);
        }
    }

    /**
     * Méthode qui lance l'application Deliverapp
     *
     * @param args les arguments de la CLI
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Retourne la liste observable des livreurs
     *
     * @return <code>this.livreurs</code>
     */
    public ObservableList<Livreur> getLivreurs() {
        return livreurs;
    }

    /**
     * Modifie la liste observable des livreurs
     * @param livreurs
     */
    public void setLivreurs(ObservableList<Livreur> livreurs) {
        this.livreurs = livreurs;
    }

    /**
     * Retourne la liste observable des livraisons
     *
     * @return <code>this.livraisons</code>
     */
    public ObservableList<Livraison> getLivraisons() {
        return livraisons;
    }

    /**
     * Peuple la fenêtre au démarrage de l'application avec l'interface
     * d'accueil
     *
     * @param primaryStage le contenu de la fenetre
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        //logger.info("Starting Deliverapp");
        String fxmlFile = "/fxml/Accueil.fxml";
        //logger.debug("Loading fxml file {}", fxmlFile);
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent rootNode = fxmlLoader.load(getClass().getResourceAsStream(fxmlFile));
        //logger.trace("stage loaded");

        controller = fxmlLoader.getController();
        controller.setWindow(this);

        rootNode.getStylesheets().add(getClass().getResource("/css/test.css").toExternalForm());

        Scene scene = new Scene(rootNode);
        //logger.trace("scene created");

        primaryStage.setTitle("Deliverapp");
        primaryStage.getIcons().add(new Image(getClass().getResource("/images/Logo_128.png").toExternalForm()));
        primaryStage.setScene(scene);
        // logger.trace("showing scene");
        primaryStage.show();
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);

        this.primaryStage = primaryStage;

        //logger.debug("Deliverapp start method finished");
    }

    /**
     * Change le contenu de la fenêtre pour l'interface de choix de la carte
     */
    public void setChoixCarteWindow() {
        String fxmlFile = "/fxml/ChoixCarte.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent rootNode = null;
        try {
            rootNode = fxmlLoader.load(getClass().getResourceAsStream(fxmlFile));
            rootNode.getStylesheets().add(getClass().getResource("/css/test.css").toExternalForm());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(rootNode);
        primaryStage.setTitle("Deliverapp - choix de la carte");
        primaryStage.getIcons().add(new Image(getClass().getResource("/images/Logo_128.png").toExternalForm()));
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
    }

    /**
     * Change le contenu de la fenêtre pour l'interface d'importation de
     * livraisons
     */
    public void setChoixImporterWindow(){
        String fxmlFile = "/fxml/ImporterLivraison.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent rootNode = null;
        try {
            rootNode = fxmlLoader.load(getClass().getResourceAsStream(fxmlFile));
            rootNode.getStylesheets().add(getClass().getResource("/css/test.css").toExternalForm());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(rootNode);
        primaryStage.setTitle("Deliverapp - importer des livraisons");
        primaryStage.getIcons().add(new Image(getClass().getResource("/images/Logo_128.png").toExternalForm()));
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
    }

    /**
     * Change le contenu de la fenêtre pour l'interface principale (carte)
     */
    public void setPagePrincipale(String type) {
        String fxmlFile = "/fxml/PageMain.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent rootNode = null;
        try {
            rootNode = fxmlLoader.load(getClass().getResourceAsStream(fxmlFile));
            rootNode.getStylesheets().add(getClass().getResource("/css/test.css").toExternalForm());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // fxmlLoader.setController(controller);
        controller = fxmlLoader.getController();
        controller.setWindow(this);
        final Projection projection = getParameters().getUnnamed().contains("wgs84")
                ? Projection.WGS_84 : Projection.WEB_MERCATOR;

        controller.initialiserCarteEtControles(projection);

        // Suppression des livraisons existantes avant le changement de carte
        if(type.equals("changement")){
            for(Livreur livreur : livreurs) {
                if(!livreur.getListeLivraison().isEmpty()) {
                    int size = livreur.getListeLivraison().size();
                    for (int i = size-1; i>=0 ; i--){
                        livreur.supprimerLivraison(livreur.getListeLivraison().get(i));
                    }
                }
            }
        }

        rootNode.getStylesheets().add(getClass().getResource("/css/test.css").toExternalForm());
//      Font.loadFont(getClass().getResourceAsStream("/others/Poppins-Medium.ttf"), 10);
        Scene scene = new Scene(rootNode);
        primaryStage.setTitle("Deliverapp");
        primaryStage.getIcons().add(new Image(getClass().getResource("/images/Logo_128.png").toExternalForm()));
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.centerOnScreen();
        primaryStage.setResizable(false);
    }


    /**
     * Retourne le stage actuel de la fenêtre (contenu)
     *
     * @return <code>this.primaryStage</code>
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Retourne le livreur selectionnée par l'utilisateur
     *
     * @return <code>this.livreurSelectionne</code>
     */
    public Livreur getLivreurSelectionne() {
        return livreurSelectionne;
    }

    /**
     * Sélectionne un livreur et notifie le controlleur et les observeurs
     *
     * @param livreurSelectionne le livreur sélectionné
     */
    public void setLivreurSelectionne(Livreur livreurSelectionne) {
        this.livreurSelectionne = livreurSelectionne;
//        controller.selectionnerLivreur();
        this.update(null, null);
    }

    /**
     * Retourne le plan
     *
     * @return <code>this.plan</code>
     */
    public Plan getPlan() {
        return plan;
    }

    /**
     * Modifie le plan
     *
     * @param p le nouveau plan
     */
    public void setPlan(Plan p) {
        this.plan = p;
    }

    /**
     * Retourne les livraisons selectionnées
     *
     * @return <code>this.livraisonsSelectionnees</code>
     */
    public List<Livraison> getLivraisonsSelectionnees() {
        return livraisonsSelectionnees;
    }

    /**
     * Ajoute une livraison aux livraisons sélectionnées
     *
     * @param livraisonSelectionne la livraison à ajouter
     */
    public void ajouteLivraisonsSelectionnees(Livraison livraisonSelectionne) {
        this.livraisonsSelectionnees.add(livraisonSelectionne);
        System.err.println(livraisonsSelectionnees);
    }

    /**
     * Retire une livraison aux livraisons sélectionnées
     *
     * @param livraisonSelectionne la livraison à retirer
     */
    public void supprimerLivraisonsSelectionnees(Livraison livraisonSelectionne) {
        this.livraisonsSelectionnees.remove(livraisonSelectionne);
    }

    /**
     * Retourne le Marker sélectionné par l'utilisateur
     *
     * @return <code>this.markerSelectionne</code>
     */
    public Pair<Marker,Marker> getMarkerSelectionne() {
        return markerSelectionne;
    }

    /**
     * Modifie le marker sélectionné
     *
     * @param markerSelectionne le nouveau marker sélectionné
     */
    public void setMarkerSelectionne(Marker markerSelectionne) {
        this.markerSelectionne = new Pair(markerSelectionne, null);
    }

    /**
     * Rend un marker visible ou invisible
     * @param markerClicked le marker dont on veut ajouter
     */
    public void setMarkerVisible(Marker markerClicked) {
        for(Livraison livraison : livraisons){
            if(markerClicked.getPosition().equals(livraison.getMarkers().getKey().getPosition())){
                if(livraison.getMarkers().getKey().getVisible()){
                    livraison.getMarkers().getKey().setVisible(false);
                    livraison.getMarkers().getValue().setVisible(true);
                }else{
                    livraison.getMarkers().getKey().setVisible(true);
                    livraison.getMarkers().getValue().setVisible(false);
                }
            }
        }
    }

    /**
     * La méthode appelée lors de la modification du livreur sélectionné
     * Modifie la liste des livraisons observables
     *
     * @param o   la donnée observée
     * @param arg
     */

    @Override
    public void update(Observable o, Object arg) {
        livraisons.clear();
        if (livreurSelectionne == null) {
            for (Livreur leLivreur : livreurs) {
                for (Livraison livraison : leLivreur.getListeLivraison()) {
                    livraisons.add(livraison);
                }
            }
        } else {
            for (Livraison livraison : livreurSelectionne.getListeLivraison()) {
                livraisons.add(livraison);
            }
        }
    }
}
