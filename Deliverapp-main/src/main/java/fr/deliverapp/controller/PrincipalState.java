package fr.deliverapp.controller;

import com.sothawo.mapjfx.*;
import com.sothawo.mapjfx.event.MapLabelEvent;
import com.sothawo.mapjfx.event.MapViewEvent;
import com.sothawo.mapjfx.event.MarkerEvent;
import fr.deliverapp.controller.command.AjouterLivraisonCommand;
import fr.deliverapp.controller.command.ListOfCommands;
import fr.deliverapp.controller.command.ModifierLivraisonCommand;
import fr.deliverapp.controller.command.ReverseCommand;
import fr.deliverapp.model.data.XMLParser;
import fr.deliverapp.model.objects.*;
import fr.deliverapp.view.Window;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Etat quand la carte est choisie par l'utilisateur ou qu'une tournée a été
 * importée
 *
 * @author Hexanome H4124
 */
public class PrincipalState implements State {

    /**
     * Le zoom par défaut de la carte graphique
     */
    private static final int ZOOM_DEFAULT = 16;
    /**
     * Markers visibles ou non
     */
    private boolean markersVisible = false;
    /**
     * Le Marker de l'entrepôt
     */
    private Marker markerEntrepot;

    /**
     * Initialise la carte et ses contrôles
     *
     * @param projection
     * @param mapView          le JFX MapView d'id mapView
     * @param labelZoom        le JFX Label d'id labelZoom (affiche le niveau de zoom)
     * @param labelEvent       le JFX Label d'id labelEvent
     * @param colonneLivreur
     * @param colonneLivraison
     * @param tableLivreur
     * @param tableLivraison
     * @param listeMarkers     la liste des intersections cliquables
     * @param listeLines       la liste des routes praticables
     * @param listeInter       la liste des intersections du plan
     * @param listeCommande    la liste des commandes (pour undo / redo)
     * @param c                le Controller
     * @param w                la Window
     */
    public void initialiserCarteEtControles(Projection projection,
                                            MapView mapView,
                                            Label labelZoom,
                                            Label labelEvent,
                                            TableColumn<Livreur, String> colonneLivreur,
                                            TableColumn<Livraison, String> colonneLivraison,
                                            TableView<Livreur> tableLivreur,
                                            TableView<Livraison> tableLivraison,
                                            ArrayList<Marker> listeMarkers,
                                            ArrayList<CoordinateLine> listeLines,
                                            ArrayList<Intersection> listeInter,
                                            BorderPane BorderPaneInfoLivreur,
                                            Text labelTournee,
                                            Text labelNombreLivraisonLivreur,
                                            Button boutonAjouterLivraison,
                                            Button buttonValiderTournee,
                                            ListOfCommands listeCommande,
                                            Controller c, Window w ){
        markersVisible = false;

        // Initialisation des données dans les TableView
        tableLivreur.setItems(w.getLivreurs());
        tableLivraison.setItems(w.getLivraisons());

        // TODO : à remettre mais Controller pas content car au début il ne
        //  trouve pas ces élements dans la page...

        colonneLivreur.setCellValueFactory(cellData -> cellData.getValue().getStringProperty());
        colonneLivraison.setCellValueFactory(cellData -> cellData.getValue().getStringProperty());
        colonneLivraison.prefWidthProperty().bind(tableLivraison.widthProperty().multiply(0.45));
        colonneLivraison.setStyle(" -fx-text-fill: black;");
        colonneLivreur.setStyle(" -fx-text-fill: black;");

        // Ajout des boutons de séléction de livreurs / suppression de livraison
        addButtonModificationLivraison(w,c, tableLivraison, listeCommande, mapView);
        addButtonSelectionLivreur(w, tableLivreur, c, BorderPaneInfoLivreur, labelTournee, labelNombreLivraisonLivreur,
                boutonAjouterLivraison, buttonValiderTournee);
        addButtonSupressionLivraison(w,c,tableLivraison, listeCommande);

        // set the custom css file for the MapView
        mapView.setCustomMapviewCssURL(getClass().getResource("/css/custom_mapview.css"));

        // Appelle le parser pour récupérer la carte
        XMLParser parser = XMLParser.getInstance();
        Plan map = parser.chargerCarte(c.getCheminCarte());
        w.setPlan(map);

        // Gestion de la zone d'affichage de la map, mis à jour dans le if(map != null)
        Intersection minIntersection = map.getListeIntersection().get(0);
        Intersection maxIntersection = map.getListeIntersection().get(0);

        Extent mapArea = Extent.forCoordinates(
                new Coordinate(minIntersection.getLat(),minIntersection.getLon()),
                new Coordinate(maxIntersection.getLat(),maxIntersection.getLon())
        );

        // Ajout du Marker entrepot
        listeInter.clear();listeMarkers.clear();
        if(map != null) {
            for (Intersection i : map.getListeIntersection()) {
                listeInter.add(i);
                double lat = i.getLat();
                double lon = i.getLon();
                Marker marker=new Marker(getClass().getResource("/images/circle.png"), -12, -12)
                        .setPosition(new Coordinate(lat, lon))
                        .setVisible(false);
                listeMarkers.add(marker);
                for(Livraison livraison : w.getLivraisons()) {
                    if (livraison.getPointLivraison().getLat()==i.getLat() && livraison.getPointLivraison().getLon()==i.getLon()) {
                        livraison.setPointLivraison(i);
                        String image = "";
                        if (livraison.getLivreur().getNom().equals("Bob")) {
                            image = "/images/circle_red.png";
                        } else {
                            image = "/images/circle_blue.png";
                        }
                        Marker markerSelectionne = new Marker(getClass().getResource(image), -12, -12)
                                .setPosition(new Coordinate(lat, lon))
                                .setVisible(false);

                        Pair<Marker,Marker> pair = new Pair<Marker,Marker>(marker, markerSelectionne);
                        livraison.setMarkers(pair);

                    }
                }


                        // Détermination du coin min et coin max de la map pour paramétrer l'extent
                if(i.getLat() < minIntersection.getLat() &&
                        i.getLon() < minIntersection.getLon()) {
                    minIntersection = i;
                }

                if(i.getLat() > maxIntersection.getLat() &&
                        i.getLon() > maxIntersection.getLon()) {
                    maxIntersection = i;
                }
            }

            mapArea = Extent.forCoordinates(
                    new Coordinate(minIntersection.getLat() - 0.05,minIntersection.getLon() - 0.05),
                    new Coordinate(maxIntersection.getLat() + 0.05,maxIntersection.getLon() + 0.05)
            );
            listeLines.clear();
            for(Segment s : map.getListeSegment()) {
                Coordinate origin = new Coordinate(s.getOrigine().getLat(),s.getOrigine().getLon());
                Coordinate dest = new Coordinate(s.getDestination().getLat(),s.getDestination().getLon());

                listeLines.add(new CoordinateLine(origin, dest).setColor(Color.GRAY).setVisible(true));
            }

            markerEntrepot =
                    new Marker(getClass().getResource("/images/entrepot.png"), -25
                            , -25)
                            .setPosition(new Coordinate(map.getEntrepot().getLocalisation().getLat(), map.getEntrepot().getLocalisation().getLon()))
                            .setVisible(true);
        }




        labelZoom.textProperty().bind(Bindings.format("zoom: %.0f", mapView.zoomProperty()));

        // watch the MapView's initialized property to finish initialization
        Extent finalMapArea = mapArea;
        mapView.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                afterMapIsInitialized(mapView, map, listeMarkers,
                        listeLines, finalMapArea,w);
            }
        });


        // Ajoute la feuille de style
        tableLivraison.setRowFactory(table -> {
            TableRow<Livraison> row = new TableRow<Livraison>(){
                @Override
                protected void updateItem(Livraison livraison, boolean empty){
                    boolean existenceNonValides = true;
                    boolean existenceTournee = true;
                    boolean existenceLivreur = true;
                    if(w.getLivreurSelectionne()==null){
                        existenceLivreur= false;
                    }
                    if(existenceLivreur && w.getLivreurSelectionne().getTournee()==null){
                        existenceTournee = false;
                    }
                    if(existenceLivreur && existenceTournee && w.getLivreurSelectionne().getTournee().calculLivraisonsNonValides().size()==0){
                        existenceNonValides = false;
                    }
                    super.updateItem(livraison, empty);
                    if(super.getItem() != null){
                        if(w.getLivraisonsSelectionnees().contains(super.getItem())) {
                            // BOB ou PATRICK
                            if (super.getItem().getLivreur().getNom().equals("Bob")) {
                                super.setStyle("-fx-background-color: pink; -fx-border-radius: 45; -fx-background-radius: 45;  -fx-border-color: black; -fx-border-width: 1;");
                                if(existenceLivreur && existenceTournee && existenceNonValides && w.getLivreurSelectionne().getTournee().calculLivraisonsNonValides().contains(super.getItem())){
                                    super.setStyle("-fx-background-color: pink; -fx-border-radius: 45; -fx-background-radius: 45;  -fx-border-color: red; -fx-border-width: 3;");
                                }
                            } else if(super.getItem().getLivreur().getNom().equals("Patrick")) {
                                super.setStyle("-fx-background-color: #339CFF; -fx-border-radius: 45; -fx-background-radius: 45;  -fx-border-color: black; -fx-border-width: 1;");
                                if(existenceLivreur && existenceTournee && existenceNonValides && w.getLivreurSelectionne().getTournee().calculLivraisonsNonValides().contains(super.getItem())){
                                    super.setStyle("-fx-background-color: #339CFF; -fx-border-radius: 45; -fx-background-radius: 45;  -fx-border-color: red; -fx-border-width: 3;");
                                }
                            }
                        }else{
                            super.setStyle("-fx-background-color: #e3e3e3; -fx-border-radius: 45; -fx-background-radius: 45; -fx-border-color: black; -fx-border-width: 1;");
                            if(existenceLivreur && existenceTournee && existenceNonValides && w.getLivreurSelectionne().getTournee().calculLivraisonsNonValides().contains(super.getItem())){
                                super.setStyle("-fx-background-color: #e3e3e3; -fx-border-radius: 45; -fx-background-radius: 45;  -fx-border-color: red; -fx-border-width: 3;");
                            }
                        }
                    }else{
                        super.setStyle("");
                    }
                }
            };
            row.setOnMouseClicked(evt -> {
                boolean existenceNonValides = true;
                boolean existenceTournee = true;
                boolean existenceLivreur = true;
                if(w.getLivreurSelectionne()==null){
                    existenceLivreur= false;
                }
                if(existenceLivreur && w.getLivreurSelectionne().getTournee()==null){
                    existenceTournee = false;
                }
                if(existenceLivreur && existenceTournee && w.getLivreurSelectionne().getTournee().calculLivraisonsNonValides().size()==0){
                    existenceNonValides = false;
                }
                if(!row.isEmpty() && row.getItem() != null) {
                    if (w.getLivraisonsSelectionnees().contains(row.getItem())) {
                        w.getLivraisonsSelectionnees().remove(row.getItem());
                        row.setStyle("-fx-background-color: #e3e3e3; -fx-border-radius: 45; -fx-background-radius: 45; -fx-border-color: black; -fx-border-width: 1;");
                        if(existenceLivreur && existenceTournee && existenceNonValides && w.getLivreurSelectionne().getTournee().calculLivraisonsNonValides().contains(row.getItem())){
                            row.setStyle("-fx-background-color: #e3e3e3; -fx-border-radius: 45; -fx-background-radius: 45;  -fx-border-color: red; -fx-border-width: 3;");
                        }
                        row.getItem().getMarkers().getKey().setVisible(true);
                        row.getItem().getMarkers().getValue().setVisible(false);
                    } else {
                        w.ajouteLivraisonsSelectionnees(row.getItem());
                        if(row.getItem().getLivreur().getNom().equals("Bob")) {
                            row.setStyle("-fx-background-color: pink; -fx-border-radius: 45; -fx-background-radius: 45;  -fx-border-color: black; -fx-border-width: 1;");
                            if(existenceLivreur && existenceTournee && existenceNonValides && w.getLivreurSelectionne().getTournee().calculLivraisonsNonValides().contains(row.getItem())){
                                row.setStyle("-fx-background-color: pink; -fx-border-radius: 45; -fx-background-radius: 45;  -fx-border-color: red; -fx-border-width: 3;");
                            }
                        }else if(row.getItem().getLivreur().getNom().equals("Patrick")){
                            row.setStyle("-fx-background-color: #339CFF; -fx-border-radius: 45; -fx-background-radius: 45;  -fx-border-color: black; -fx-border-width: 1;");
                            if(existenceLivreur && existenceTournee && existenceNonValides && w.getLivreurSelectionne().getTournee().calculLivraisonsNonValides().contains(row.getItem())){
                                row.setStyle("-fx-background-color: #339CFF; -fx-border-radius: 45; -fx-background-radius: 45;  -fx-border-color: red; -fx-border-width: 3;");
                            }
                        }
                        row.getItem().getMarkers().getKey().setVisible(false);
                        row.getItem().getMarkers().getValue().setVisible(true);
                        mapView.addMarker(row.getItem().getMarkers().getValue());
                        mapView.setCenter(row.getItem().getMarkers().getValue().getPosition());
                    }
                }
            });


            mapView.addEventHandler(MarkerEvent.MARKER_CLICKED, event -> {
                event.consume();
                boolean existenceNonValides = true;
                boolean existenceTournee = true;
                boolean existenceLivreur = true;
                if(w.getLivreurSelectionne()==null){
                    existenceLivreur= false;
                }
                if(existenceLivreur && w.getLivreurSelectionne().getTournee()==null){
                    existenceTournee = false;
                }
                if(existenceLivreur && existenceTournee && w.getLivreurSelectionne().getTournee().calculLivraisonsNonValides().size()==0){
                    existenceNonValides = false;
                }
                if (!row.isEmpty() && row.getItem() != null &&
                        (row.getItem().getMarkers().getKey().getId().equals(event.getMarker().getId()) || row.getItem().getMarkers().getValue().getId().equals(event.getMarker().getId()))) {
                    w.setMarkerVisible(event.getMarker());
                    if (w.getLivraisonsSelectionnees().contains(row.getItem())) {
                        w.getLivraisonsSelectionnees().remove(row.getItem());
                        row.setStyle("-fx-background-color: #e3e3e3; -fx-border-radius: 45; -fx-background-radius: 45; -fx-border-color: black; -fx-border-width: 1;");
                        if(existenceLivreur && existenceTournee && existenceNonValides && w.getLivreurSelectionne().getTournee().calculLivraisonsNonValides().contains(row.getItem())){
                            row.setStyle("-fx-background-color: #e3e3e3; -fx-border-radius: 45; -fx-background-radius: 45;  -fx-border-color: red; -fx-border-width: 3;");
                        }
                        row.getItem().getMarkers().getKey().setVisible(true);
                        row.getItem().getMarkers().getValue().setVisible(false);
                    } else {
                        w.ajouteLivraisonsSelectionnees(row.getItem());
                        if (row.getItem().getLivreur().getNom().equals("Bob")) {
                            row.setStyle("-fx-background-color: pink; -fx-border-radius: 45; -fx-background-radius: 45;  -fx-border-color: black; -fx-border-width: 1;");
                            if(existenceLivreur && existenceTournee && existenceNonValides && w.getLivreurSelectionne().getTournee().calculLivraisonsNonValides().contains(row.getItem())){
                                row.setStyle("-fx-background-color: pink; -fx-border-radius: 45; -fx-background-radius: 45;  -fx-border-color: red; -fx-border-width: 3;");
                            }
                        } else if(row.getItem().getLivreur().getNom().equals("Patrick")) {
                            row.setStyle("-fx-background-color: #339CFF; -fx-border-radius: 45; -fx-background-radius: 45;  -fx-border-color: black; -fx-border-width: 1;");
                            if(existenceLivreur && existenceTournee && existenceNonValides && w.getLivreurSelectionne().getTournee().calculLivraisonsNonValides().contains(row.getItem())){
                                row.setStyle("-fx-background-color: #339CFF; -fx-border-radius: 45; -fx-background-radius: 45;  -fx-border-color: red; -fx-border-width: 3;");
                            }
                        }
                        row.getItem().getMarkers().getKey().setVisible(false);
                        row.getItem().getMarkers().getValue().setVisible(true);
                    }
                }
            });


            return row;
        });

        setupEventHandlers(c, w, mapView, labelEvent, listeInter, listeMarkers);

        // finally initialize the map view
        mapView.initialize(Configuration.builder()
                .projection(projection)
                .showZoomControls(false)
                .build());
    }
    /**
     * Crée les events listeners pour la map graphique
     *
     * @param controller   le Controller
     * @param mapView      la mapView
     * @param labelEvent   texte affiché lors d'un event
     * @param listeInter   la liste des intersections
     * @param listeMarkers la liste des Markers
     */
    private void setupEventHandlers(Controller controller, Window w, MapView mapView,
                                    Label labelEvent,
                                    ArrayList<Intersection> listeInter,
                                    ArrayList<Marker> listeMarkers) {
        // add an event handler for singleclicks, set the click marker to the new position when it's visible
        mapView.addEventHandler(MapViewEvent.MAP_CLICKED, event -> {
            event.consume();
            final Coordinate newPosition = event.getCoordinate().normalize();
            labelEvent.setText("Event: map clicked at: " + newPosition);
        });

        mapView.addEventHandler(MapViewEvent.MAP_RIGHTCLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: map right clicked at: " + event.getCoordinate());
        });
        mapView.addEventHandler(MarkerEvent.MARKER_CLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: marker clicked: " + event.getMarker().getId() + " " + event.getMarker().getPosition());
            w.setMarkerSelectionne(event.getMarker());
            controller.setMarkerSelectionne(event.getMarker().getPosition());
        });
        mapView.addEventHandler(MarkerEvent.MARKER_RIGHTCLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: marker right clicked: " + event.getMarker().getId());
        });
        mapView.addEventHandler(MapLabelEvent.MAPLABEL_CLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: label clicked: " + event.getMapLabel().getText());
        });
        mapView.addEventHandler(MapLabelEvent.MAPLABEL_RIGHTCLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: label right clicked: " + event.getMapLabel().getText());
        });


        mapView.addEventHandler(MapViewEvent.ANY, event -> {
//            logger.debug("pointer moved to " + event.getCoordinate());
            double zoom = mapView.zoomProperty().get();
            if(zoom >= 16 && !markersVisible) {
                makeMarkersVisible(true, listeMarkers);
            }
            else if (zoom < 16 && markersVisible) {
                makeMarkersVisible(false, listeMarkers);
            }
        });
    }

    /**
     * Initialise la map
     *
     * @param mapView      la carte graphique
     * @param plan         l'objet Plan
     * @param listeMarkers la liste des intersection cliquables
     * @param listeLines   la liste des routes praticables
     */
    private void afterMapIsInitialized(MapView mapView,
                                       Plan plan,
                                       ArrayList <Marker> listeMarkers,
                                       ArrayList<CoordinateLine> listeLines,
                                       Extent extent,
                                       Window w) {
        // start at the harbour with default zoom
        mapView.setZoom(ZOOM_DEFAULT);

        mapView.setCenter(new Coordinate(plan.getListeIntersection().get(0).getLat(), plan.getListeIntersection().get(0).getLon()));

        for(Marker m : listeMarkers) {
            mapView.addMarker(m);
        }

        for(Livraison l : w.getLivraisons()) {
            mapView.addMarker(l.getMarkers().getValue());
        }


        for(CoordinateLine cl : listeLines) {
            mapView.addCoordinateLine(cl);
        }

        for(Livraison livraison:w.getLivraisons()){
            mapView.addMarker(livraison.getMarkers().getValue());
        }


        mapView.addMarker(markerEntrepot);
        mapView.setCenter(markerEntrepot.getPosition());
        // Désactivé pour l'instant
//        mapView.constrainExtent(extent);


    }

    /**
     * Rend les markers visibles ou invisibles
     *
     * @param in           true : affiche, false : enlève
     * @param listeMarkers la liste des Marker
     */
    private void makeMarkersVisible(boolean in, ArrayList <Marker> listeMarkers) {
        markersVisible = in;
        for(Marker m : listeMarkers) {
            m.setVisible(in);
        }
    }

    private void addButtonSelectionLivreur(Window window,
                                           TableView<Livreur> tableLivreur,
                                           Controller controller,
                                           BorderPane BorderPaneInfoLivreur,
                                           Text labelTournee, Text labelNombreLivraisonLivreur,
                                           Button boutonAjouterLivraison, Button buttonValiderTournee) {
        tableLivreur.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        TableColumn<Livreur, String> colonneSelectionLivreur = new TableColumn<>("");
        colonneSelectionLivreur.setCellValueFactory(new PropertyValueFactory<>(""));
//        colonneSelectionLivreur.setResizable(false);
        ToggleGroup group = new ToggleGroup();

        Callback<TableColumn<Livreur, String>, TableCell<Livreur, String>> cellFactory = new Callback<TableColumn<Livreur, String>, TableCell<Livreur, String>>() {
            @Override
            public TableCell<Livreur, String> call(final TableColumn<Livreur, String> param) {
                final TableCell<Livreur, String> cell = new TableCell<Livreur, String>() {

                    private final ToggleButton btn =
                            new ToggleButton("Sélectionner");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        btn.getStyleClass().add("buttonDefault");
                        btn.setToggleGroup(group);
                        try {
                            controller.ajouterHashMapBoutonSelection(window.getLivreurs().get(getTableRow().getIndex()),btn);
                        }
                        catch(Exception e) {

                        }

                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction((ActionEvent event) -> {
                                if(!btn.isSelected()) {
                                    if(controller.getCurrentState().equals(controller.livreurSelectionneState) ||
                                            controller.getCurrentState().equals(controller.tourneeCalculeeState) ) {
                                        controller.terminer();
                                        controller.deselectionnerLivreur();
                                    }
                                }else if(btn.isSelected()){
                                    if(!controller.getCurrentState().equals(controller.ajouterLivraisonState)) {

                                        window.setLivreurSelectionne(window.getLivreurs().get(getTableRow().getIndex()));
                                        controller.selectionnerLivreur();
//                                        initialiserAffichageLivreur(BorderPaneInfoLivreur, labelTournee,
//                                                labelNombreLivraisonLivreur, boutonAjouterLivraison, buttonValiderTournee, window);
                                        System.out.println(window.getLivreurs().get(getTableRow().getIndex()).getNom());
                                    }
                                }
                            });
                            setGraphic(btn);
                            setText(null);

                        }
                    }
                };
                return cell;
            }
        };
        colonneSelectionLivreur.setCellFactory(cellFactory);
        tableLivreur.getColumns().add(colonneSelectionLivreur);
    }

    private void initialiserAffichageLivreur(BorderPane BorderPaneInfoLivreur, Text labelTournee,
                                             Text labelNombreLivraisonLivreur, Button boutonAjouterLivraison, Button boutonCalculerTournee,
                                             Window w, Controller controller) {
        System.out.println("test");
        labelTournee.setText("Tournee de " + w.getLivreurSelectionne().getNom());
        labelNombreLivraisonLivreur.setText(w.getLivreurSelectionne().getListeLivraison().size() + " livraison(s) au total");
        labelNombreLivraisonLivreur.getStyleClass().add("header2");
        //buttonValiderTournee.setText("Valider la tournée de " + w.getLivreurSelectionne().getNom());
        //buttonValiderTournee.getStyleClass().add("buttonDefault");
        if(controller.getCurrentState() != controller.tourneeCalculeeState) {
            boutonAjouterLivraison.setVisible(true);
            boutonAjouterLivraison.setManaged(true);
        }
        BorderPaneInfoLivreur.setVisible(true);
        BorderPaneInfoLivreur.setManaged(true);
        boutonCalculerTournee.setVisible(true);
        boutonCalculerTournee.setManaged(true);
    }


    private void addButtonSupressionLivraison(Window window,
                                              Controller controller,
                                              TableView<Livraison> tableLivraison, ListOfCommands listeCommande) {
        TableColumn<Livraison, String> colonneSelectionLivraison =
                new TableColumn<>("");
        colonneSelectionLivraison.setCellValueFactory(new PropertyValueFactory<>(""));

        Callback<TableColumn<Livraison, String>, TableCell<Livraison, String>> cellFactory = new Callback<TableColumn<Livraison, String>, TableCell<Livraison, String>>() {
            @Override
            public TableCell<Livraison, String> call(final TableColumn<Livraison, String> param) {
                final TableCell<Livraison, String> cell = new TableCell<Livraison, String>() {

                    private final ToggleButton btn =
                            new ToggleButton();


                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        btn.getStyleClass().add("deleteButton");
//                        try {
//                            controller.ajouterHashmapLivreurBoutonEditionSupp(window.getLivraisons().get(getTableRow().getIndex()).getLivreur(),btn);
//                        }
//                        catch(Exception e) {
//
//                        }
                        if (empty || controller.getCurrentState() ==
                                controller.tourneeCalculeeState) {
                            setGraphic(null);
                            setText(null);
                        } else {

                            btn.setOnAction((ActionEvent event) -> {
                                Livraison livraison = getTableRow().getItem();
                                listeCommande.add(
                                        new ReverseCommand(
                                                new AjouterLivraisonCommand(livraison, livraison.getLivreur())));
                                livraison.getMarkers().getKey().setVisible(true);
                                livraison.getMarkers().getValue().setVisible(false);
                                Text labelInfos = (Text)window.getPrimaryStage().getScene().lookup("#labelNombreLivraisonLivreur");
                                labelInfos.setText(window.getLivreurSelectionne().getListeLivraison().size()+" livraison(s) au total");
                            });
                            setGraphic(btn);
                            setText(null);

                        }
                    }
                };
                cell.prefWidthProperty().bind(tableLivraison.widthProperty().multiply(0.20));
                return cell;
            }
        };
        colonneSelectionLivraison.setCellFactory(cellFactory);
        tableLivraison.getColumns().add(colonneSelectionLivraison);
    }

    private void addButtonModificationLivraison(Window window, Controller controller,
                                              TableView<Livraison> tableLivraison, ListOfCommands listeCommande, MapView mapView) {
        TableColumn<Livraison, String> colonneSelectionLivraison =
                new TableColumn<>("");
        colonneSelectionLivraison.setCellValueFactory(new PropertyValueFactory<>(""));

        Callback<TableColumn<Livraison, String>, TableCell<Livraison, String>> cellFactory = new Callback<TableColumn<Livraison, String>, TableCell<Livraison, String>>() {
            @Override
            public TableCell<Livraison, String> call(final TableColumn<Livraison, String> param) {
                final TableCell<Livraison, String> cell = new TableCell<Livraison, String>() {

                    private final ToggleButton btn =
                            new ToggleButton();


                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        btn.getStyleClass().add("editButton");

//                        try {
//                            controller.ajouterHashmapLivreurBoutonEditionSupp(window.getLivraisons().get(getTableRow().getIndex()).getLivreur(),btn);
//                        }
//                        catch(Exception e) {

//                        }
                        if (empty || controller.getCurrentState() ==
                                controller.tourneeCalculeeState) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction((ActionEvent event) -> {
                                Livraison livraison = getTableRow().getItem();
                                    listeCommande.add(
                                                    new ModifierLivraisonCommand(livraison, window, mapView));
                            });
                            setGraphic(btn);
                            setText(null);

                        }
                    }
                };
                cell.prefWidthProperty().bind(tableLivraison.widthProperty().multiply(0.20));
                return cell;
            }
        };
        colonneSelectionLivraison.setCellFactory(cellFactory);
        tableLivraison.getColumns().add(colonneSelectionLivraison);
    }

    /**
     * Permet de passer à l'état LivreurSelectionneState
     *
     * @param c                      le controlleur
     * @param w                      la fenêtre
     * @param boutonAjouterLivraison le bouton qui permet d'ajouter une
     *                               livraison
     * @param VBoxCreationLivraison  la zone affichée pour ajouter une livraison
     */
    public void selectionnerLivreur(Controller c, Window w, Button boutonAjouterLivraison, VBox VBoxCreationLivraison) {
        if(!c.tourneeDejaCalculee(w.getLivreurSelectionne())) {
            c.setCurrentState(c.livreurSelectionneState);
        }
        else {
            c.setCurrentState(c.tourneeCalculeeState);
        }
        c.afficherTournee(w.getLivreurSelectionne());
        c.desactiverBoutonsAutresLivreurs(w.getLivreurSelectionne());

        for(Livreur l : w.getLivreurs()){
            if(l.getNom() != w.getLivreurSelectionne().getNom()){
                if(c.tourneeDejaCalculee(l)){
                    c.desafficherTournee(l);
                }
            }
        }
    }

    /**
     * Selection de la carte par l'utilisateur
     * Affichage d'une interface de choix de fichier
     *
     * @param c la controlleur de l'application
     * @param w la fenêtre
     */
    public void changerCarte(Controller c, Window w) {
        c.setCurrentState(c.selectionCarteState);
    }

    /**
     * Annule la dernière commande effectuée
     *
     * @param listeCommande
     */
    public void undo(ListOfCommands listeCommande) {
        listeCommande.undo();
    }

    /**
     * Effectue la dernière commande annulée
     *
     * @param listeCommande
     */
    public void redo(ListOfCommands listeCommande) {
        listeCommande.redo();
    }

    public void initialiserBoutonsState(Controller c, Window w) {
        boolean afficher = true;

        if(c.boutonAjouterLivraison != null) {
            c.boutonAjouterLivraison.setVisible(!afficher);
            c.boutonAjouterLivraison.setManaged(!afficher);
        }
        if(c.boutonCalculerTournee != null) {
            c.boutonCalculerTournee.setVisible(!afficher);
            c.boutonCalculerTournee.setManaged(!afficher);
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

        if(w.getPrimaryStage().getScene().lookup("#textGreenFeuilleRoute") != null){
            w.getPrimaryStage().getScene().lookup("#textGreenFeuilleRoute").setVisible(!afficher);
            w.getPrimaryStage().getScene().lookup("#textGreenFeuilleRoute").setManaged(!afficher);
        }

        if(c.boutonRevenirAvantCalculTournee != null) {
            c.boutonRevenirAvantCalculTournee.setVisible(!afficher);
            c.boutonRevenirAvantCalculTournee.setManaged(!afficher);
        }
        if(c.boutonChangerCarte != null) {
            c.boutonChangerCarte.setVisible(afficher);
            c.boutonChangerCarte.setManaged(afficher);
        }

        c.BorderPaneInfoLivreur.setVisible(!afficher);
        c.BorderPaneInfoLivreur.setManaged(!afficher);

        c.labelInfos.setVisible(false);
        c.labelNombreLivraisonLivreur.setVisible(false);

        c.desactiverBoutonsEditionSupp();
    }
}
