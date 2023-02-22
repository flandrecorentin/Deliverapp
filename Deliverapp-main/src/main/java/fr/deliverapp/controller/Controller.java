package fr.deliverapp.controller;

import com.sothawo.mapjfx.*;
import fr.deliverapp.controller.command.ListOfCommands;
import fr.deliverapp.model.data.XMLParser;
import fr.deliverapp.model.objects.Intersection;
import fr.deliverapp.model.objects.Livraison;
import fr.deliverapp.model.objects.Livreur;
import fr.deliverapp.model.objects.TourneeAffichage;
import fr.deliverapp.view.Window;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * Le controlleur général appelée par la Window
 *
 * @author Hexanome H4124
 */
public class Controller {

    /**
     * Liste des différents états
     **/
    protected static final State initialState = new InitialState();
    protected static final State livreurSelectionneState =
            new LivreurSelectionneState();
    protected static final State ajouterLivraisonState =
            new AjouterLivraisonState();
    protected static final State principalState = new PrincipalState();
    protected static final State tourneeCalculeeState = new TourneeCalculeeState();
    protected static final State selectionCarteState =
            new SelectionCarteState();
    protected static final State importerListeLivraisonState = new ImporterListeLivraisonState();

    protected static final State ajouterLivraisonApresCalculState = new AjouterLivraisonApresCalculState();
    protected static final State choisirLivraisonPrecedenteState = new ChoisirLivraisonPrecedenteState();
    protected static final State supprimerLivraisonApresCalculState = new SupprimerLivraisonApresCalculState();

    /**
     * La fenêtre associée au controlleur
     */
    private static Window window;

    /**
     * Etat actuel du controlleur
     */
    private static State currentState = initialState;

    /**
     * Le chemin de la carte (fichier XML)
     */
    private static String cheminCarte;
    private static String cheminLivraison;
    /**
     * Label du zoom
     */
    @FXML
    protected Label labelZoom;
    /**
     * Label du dernier évènement émis
     **/
    @FXML
    protected Label labelEvent;
    /**
     * La table où sont affichés les Livreurs
     */
    @FXML
    protected TableView<Livreur> tableLivreur;
    /**
     * La colonne dans la table des Livreurs
     */
    @FXML
    protected TableColumn<Livreur, String> colonneLivreur;
    /**
     * La table où sont affichés les Livraisons
     */
    @FXML
    protected TableView<Livraison> tableLivraison;
    /**
     * La colonne dans la table des Livraisons
     */
    @FXML
    protected TableColumn<Livraison, String> colonneLivraison;
    /**
     * Le bouton pour ajouter des livraisons
     */
    @FXML
    protected Button boutonAjouterLivraison;
    /**
     * Le bouton pour calculer une Tournée
     */
    @FXML
    protected Button boutonCalculerTournee;
    /**
     * Le bouton pour ajouter des livraisons après le calcul de tournée
     */
    @FXML
    protected Button boutonAjouterLivraisonApresCalcul;
    /**
     * Le bouton pour supprimer des livraisons après calcul de tournée
     */
    @FXML
    protected Button boutonSupprimerLivraisonApresCalcul;
    /**
     * La ComboBox qui permet de choisir la plage horaire lors de l'ajout
     * d'une livraison
     */
    @FXML
    protected ComboBox comboboxHoraire;
    /**
     * La zone affichée lors d'un ajout de livraison
     */
    @FXML
    protected VBox VBoxCreationLivraison;
    /**
     * Label d'infos sur la tournée
     */
    @FXML
    protected Text labelInfos;
    /**
     * La zone affichée d'infos du livreur
     */
    @FXML
    protected BorderPane BorderPaneInfoLivreur;
    /**
     * Le label de la tournée (Tournée de nom du livreur)
     */
    @FXML
    protected Text labelTournee;
    /**
     * Le label qui indique le nombre de livraison (X livraisons)
     */
    @FXML
    protected Text labelNombreLivraisonLivreur;
    /**
     * Le bouton pour valider l'ajout d'une livraison
     */
    @FXML
    protected Button buttonValiderAjoutLivraison;
    /**
     * Le bouton pour annuler l'ajout d'une livraison
     */
    @FXML
    protected Button buttonAnnulerAjoutLivraison;
    /**
     * Le bouton pour générer
     */
    @FXML
    protected Button boutonGenerationFeuilleDeRoute;
    /**
     * Le bouton pour revenir au state avant le calcul d'une tournée (retour
     * à LivreurSelectionneState)
     */
    @FXML
    protected Button boutonRevenirAvantCalculTournee;
    /**
     * Le bouton permettant de changer la carte
     */
    @FXML
    protected Button boutonChangerCarte;
    /**
     * Le titre de la zone d'ajout de livraison
     */
    @FXML
    protected Text TitreVBoxAjoutLivraison;
    /**
     * Le label d'instruction pour choisir la plage horaire
     */
    @FXML
    protected Label texteSelectionPlageHoraire;
    /**
     * Le label d'instruction pour selectionner le point de livraison
     */
    @FXML
    protected Label texteSelectionPointLivraison;
    /**
     * Coordonnées du Marker sélectionné par l'utilisateur
     */
    private Coordinate markerSelectionne;
    /**
     * La carte graphique JFX
     **/
    @FXML
    private MapView mapView;
    /**
     * La tournée affichée (liste de Segments graphiques)
     */
    private ArrayList<CoordinateLine> tourneeAffichee = new ArrayList<CoordinateLine>();
    /**
     * La liste des Markers (Intersection sur la Map)
     */
    private ArrayList<Marker> listeMarkers = new ArrayList<>();

    /**
     * La liste des Intersections
     */
    private ArrayList<Intersection> listeInter = new ArrayList<>();

    /**
     * La liste des CoordinatesLines (Segment sur la Map)
     */
    private ArrayList<CoordinateLine> listeLines = new ArrayList<>();

    /**
     * La liste des commandes faite par l'utilisateur
     */
    private ListOfCommands listeCommandes;

    /**
     * Liste des tournées calculées pour chaque livreur
     */
    private HashMap<Livreur, TourneeAffichage> hashmapLivreurTourneeAffichage = new HashMap<Livreur, TourneeAffichage>();

    /**
     * Liste des boutons de sélection de livreur (pour les afficher /
     * retirer)
     */
    private HashMap<Livreur, List<ToggleButton>> hashmapLivreurBoutonSelection = new HashMap<Livreur, List<ToggleButton>>();

    /**
     * Constructeur du controlleur
     */
    public Controller() {
        this.listeCommandes = new ListOfCommands();
    }

    /**
     * Récupère le chemin absolu du fichier XML de la carte
     *
     * @return le chemin du fichier en absolu
     */
    public static String getCheminCarte() {
        return cheminCarte;
    }

    /**
     * Défini le chemin absolu du fichier XML de la carte
     *
     * @param cheminCarte le chemin
     */
    public static void setCheminCarte(String cheminCarte) {
        Controller.cheminCarte = cheminCarte;
    }

    /**
     * Défini le chemin du fichier des livraisons importées
     *
     * @param cheminLivraison le chemin
     */
    protected static void setCheminLivraison(String cheminLivraison) {
        Controller.cheminLivraison = cheminLivraison;
    }

    /**
     * Retourne l'état actuel du controller
     *
     * @return
     */
    public static State getCurrentState() {
        return currentState;
    }

    /**
     * Change the current state of the controller
     *
     * @param state the new current state
     */
    protected static void setCurrentState(State state) {
        Controller.currentState = state;
        System.out.println("New state : " + currentState);
    }

    /**
     * Renvoie la Map (carte graphique)
     *
     * @return <code>this.mapView</code>
     */
    public MapView getMapView() {
        return mapView;
    }

    /**
     * Renvoie la liste des Markers
     *
     * @return <code>this.listeMarkers</code>
     */
    public ArrayList<Marker> getListeMarkers() {
        return listeMarkers;
    }

    /**
     * Renvoie les coordonnées du Marker sélectionné
     *
     * @return <code>this.markerSelectionne</code>
     */
    public Coordinate getMarkerSelectionne() {
        return markerSelectionne;
    }

    /**
     * Modifie le marker sélectionné
     *
     * @param markerSelectionne les coordonnées du nouveau marker
     */
    public void setMarkerSelectionne(Coordinate markerSelectionne) {
        this.markerSelectionne = markerSelectionne;
    }

    /**
     * Met à jour le nombre de livraisons affichées sur le label
     */
    public void majLabelNbLivraisons() {
        currentState.majLabelNbLivraisons(this, window);
    }

    /**
     * Get the actual window
     */
    public Window getWindow() {
        return this.window;
    }

    /**
     * Attribue la fenêtre au controller
     *
     * @param window the window of the app
     */
    public void setWindow(Window window) {
        this.window = window;
    }

    /**
     * Méthode appelée par la View quand clic sur CREER_TOURNEE
     */
    public void creerTournee() {
        currentState.creerTournee(this, window);
    }

    /**
     * Méthode appelée par la View quand clic sur IMPORTER_TOURNEE
     */
    public void importerTournee() {
        currentState.importerTournee(this, window);
    }

    /**
     * Appelle la méthode sélectionner liste livraison de l'état actuel
     */
    public void selectionnerListeLivraison() {
        currentState.selectionnerListeLivraison(this, window);
    }

    /**
     * Appelle la méthode valider liste livraison de l'état actuel
     */
    public void validerListeLivraison() {
        tableLivraison = new TableView<>();
        currentState.validerListeLivraison(this, window, tableLivraison);
    }

    /**
     * Change la carte pendant l'utilisation de l'application
     */
    public void changerCarte() {
        currentState.changerCarte(this, window);

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Attention !");

        // Header Text: null
        alert.setHeaderText(null);
        alert.setContentText("Changer de carte implique la suppression de vos livraisons ajoutées. " +
                "Sauvegardez avant pour éviter de tout perdre.");

        alert.showAndWait();
        selectionnerCarteFromApp();
    }

    /**
     * Appelle la méthode sélectionner carte au lancement
     */
    public void selectionnerCarte() {
        currentState.selectionnerCarte(this, window, false);
    }

    /**
     * Appelle la méthode sélectionner carte de l'état actuel pendant le
     * fonctionnement de l'application
     */
    public void selectionnerCarteFromApp() {
        currentState.selectionnerCarte(this, window, true);
    }

    /**
     * Initialise la carte et ses contrôles
     *
     * @param p la projection
     */
    public void initialiserCarteEtControles(Projection p) {
        currentState.initialiserCarteEtControles(p, mapView, labelZoom,
                labelEvent, colonneLivreur, colonneLivraison,
                tableLivreur, tableLivraison, listeMarkers, listeLines,
                listeInter, BorderPaneInfoLivreur, labelTournee, labelNombreLivraisonLivreur,
                boutonAjouterLivraison, boutonCalculerTournee, listeCommandes, this,
                window);
    }

    /**
     * Méthode appelée par la View quand clic sur AJOUTER_LIVRAISON
     */
    public void ajouterLivraison() {
        currentState.ajouterLivraison(this, window, VBoxCreationLivraison, TitreVBoxAjoutLivraison);
    }


    /**
     * Méthode appelée par la View quand clic sur CALCULER_TOURNEE
     */
    public void calculerTournee() {
        currentState.calculerTournee(this, window);
    }

    /**
     * Méthode appelée par la View quand clic sur VALIDER_TOURNEE
     */
    public void validerTournee() {
        currentState.validerTournee(this, window);
    }

    /**
     * Méthode appelée par la View quand clic sur SELECTIONNER_LIVREUR
     */
    public void selectionnerLivreur() {
        currentState.selectionnerLivreur(this, window, boutonAjouterLivraison, VBoxCreationLivraison);
        currentState.initialiserBoutonsState(this, window);
    }

    /**
     * Méthode appelée par la View quand clic sur DESELECTIONNER_LIVREUR
     */
    public void deselectionnerLivreur() {
        currentState.deselectionnerLivreur(this, window, colonneLivraison, BorderPaneInfoLivreur,
                boutonAjouterLivraison, boutonCalculerTournee);
        currentState.initialiserBoutonsState(this, window);
    }

    /**
     * Méthode appelée par la View quand clic sur Retour avant le calcul de tournée
     */
    public void revenirAvantCalculTournee() {
        currentState.revenirAvantCalculTournee(this, window,
                VBoxCreationLivraison, TitreVBoxAjoutLivraison,
                comboboxHoraire,
                texteSelectionPointLivraison, texteSelectionPlageHoraire,
                buttonValiderAjoutLivraison, buttonAnnulerAjoutLivraison,
                labelInfos);
    }

    /**
     * Valide l'ajout d'une livraison
     */
    public void validerAjoutLivraison() {
        currentState.validerAjoutLivraison(this, window, comboboxHoraire,
                listeCommandes, VBoxCreationLivraison, texteSelectionPointLivraison, texteSelectionPlageHoraire);
    }

    /**
     * Annule l'ajout d'une livraison
     */
    public void annulerAjoutLivraison() {
        currentState.annulerAjoutLivraison(this, window, VBoxCreationLivraison, comboboxHoraire);
    }

    /**
     * Retour en arrière
     */
    public void undo() {
        currentState.undo(listeCommandes);
    }

    /**
     * Rétablir les retours en arrière
     */
    public void redo() {
        currentState.redo(listeCommandes);
    }

    /**
     * Transition pour l'ajout et suppression de livraison après calcul
     */
    public void ajouterLivraisonApresCalcul() {
        currentState.ajouterLivraisonApresCalcul(this, window, VBoxCreationLivraison,
                TitreVBoxAjoutLivraison, texteSelectionPointLivraison, texteSelectionPlageHoraire,
                buttonValiderAjoutLivraison, buttonAnnulerAjoutLivraison);
    }

    ;


    /**
     * Transition pour la validation d'un ajout de livraison après calcul
     */
    public void validerAjoutApresCalcul() {
        currentState.validerAjoutApresCalcul(this, window,
                comboboxHoraire, listeCommandes,
                texteSelectionPointLivraison, texteSelectionPlageHoraire,
                buttonValiderAjoutLivraison, buttonAnnulerAjoutLivraison);
    }

    ;

    /**
     * Transition pour valider le choix du précédent après le calcul
     */
    public void validerChoixPrecedentApresCalcul() {
        currentState.validerChoixPrecedentApresCalcul(this, window, VBoxCreationLivraison,
                comboboxHoraire, listeCommandes,
                texteSelectionPointLivraison, texteSelectionPlageHoraire,
                buttonValiderAjoutLivraison, buttonAnnulerAjoutLivraison, labelInfos);
    }

    ;

    /**
     * Transition pour annuler l'ajout après le calcul
     */
    public void annulerAjoutApresCalcul() {
        currentState.annulerAjoutApresCalcul(this, window,
                VBoxCreationLivraison,
                comboboxHoraire,
                texteSelectionPointLivraison, texteSelectionPlageHoraire,
                buttonValiderAjoutLivraison, buttonAnnulerAjoutLivraison);
    }

    ;

    /**
     * Supprimer une livraison après le calcul d'une tournée
     */
    public void supprimerLivraisonApresCalcul() {
        currentState.supprimerLivraisonApresCalcul(this, window, VBoxCreationLivraison,
                TitreVBoxAjoutLivraison,
                comboboxHoraire, listeCommandes,
                texteSelectionPointLivraison, texteSelectionPlageHoraire,
                buttonValiderAjoutLivraison, buttonAnnulerAjoutLivraison);
    }

    ;

    /**
     * Pour l'état TourneeState, retourne à LivreurSelectionneState
     */
    public void terminer() {
        currentState.terminer(this, window);
        setCurrentState(livreurSelectionneState);
        currentState.initialiserBoutonsState(this, window);
    }

    ;

    /**
     * Annule la suppression d'une livraison après le calcul d'une tournée
     */
    public void annulerSuppressionApresCalcul() {
        currentState.annulerSuppressionApresCalcul(this, window,
                VBoxCreationLivraison,
                comboboxHoraire,
                texteSelectionPointLivraison, texteSelectionPlageHoraire,
                buttonValiderAjoutLivraison, buttonAnnulerAjoutLivraison);
    }

    ;

    /**
     * Confirme la suppression d'une livraison après le calcul d'une tournée
     */
    public void confirmerSuppressionLivraisonApresCalcul() {
        currentState.confirmerSuppressionLivraisonApresCalcul(this, window, VBoxCreationLivraison,
                TitreVBoxAjoutLivraison,
                comboboxHoraire, listeCommandes,
                texteSelectionPointLivraison, texteSelectionPlageHoraire,
                buttonValiderAjoutLivraison, buttonAnnulerAjoutLivraison, labelInfos);
    }

    ;


    /**
     * Sauvergarde des livraisons dans un fichier XML
     */
    public void choisirCheminSauvergardeLivraisons() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        String folder;

        File f = directoryChooser.showDialog(window.getPrimaryStage());
        if (f != null) {
            folder = f.getAbsolutePath();
        } else {
            return;
        }
        File file;
        String nameFile = ""; //initialize File object and passing path as argument
        boolean result = false;
        int i = 0;
        try {
            while (!result) {
                nameFile = "\\sauvegardeLivraisons" + i + ".xml";
                file = new File(folder + nameFile);
                result = file.createNewFile();
                if (result) {
                    System.out.println("file created " + file.getCanonicalPath()); //returns the path string
                } else {
                    System.out.println("File already exist at location: " + file.getCanonicalPath());
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();    //prints exception if any
        }
        XMLParser parser = XMLParser.getInstance();
        try {
            parser.sauvegarderLivraisons(window.getLivraisons(), folder + nameFile);
            Label label = (Label) window.getPrimaryStage().getScene().lookup("#textGreen");
            Button btn = (Button) window.getPrimaryStage().getScene().lookup("#boutonSauvegarderLivraisons");
            label.setVisible(true);
            btn.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sauvergarde la feuille de route dans un fichier texte
     */
    public void sauvergarderFeuilleDeRoute() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        String folder;

        File f = directoryChooser.showDialog(window.getPrimaryStage());
        if (f != null) {
            folder = f.getAbsolutePath();
        } else {
            return;
        }
        File file;
        String nameFile = ""; //initialize File object and passing path as argument
        boolean result = false;
        int i = 0;
        try {
            while (!result) {
                nameFile = "\\sauvegardeFeuilleRoute" + i + ".txt";
                file = new File(folder + nameFile);
                result = file.createNewFile();
                if (result) {
                    System.out.println("file created " + file.getCanonicalPath()); //returns the path string
                } else {
                    System.out.println("File already exist at location: " + file.getCanonicalPath());
                    i++;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();    //prints exception if any
        }
        XMLParser parser = XMLParser.getInstance();
        try {
            parser.genererFeuilleDeRoute(window.getLivreurSelectionne().getTournee(), folder + nameFile);
            Label label = (Label) window.getPrimaryStage().getScene().lookup("#textGreenFeuilleRoute");
            label.setVisible(true);
            Button btn = (Button) window.getPrimaryStage().getScene().lookup("#boutonGenerationFeuilleDeRoute");
            btn.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Ajoute une tournée à la liste affichée
     *
     * @param livreur le livreur dont on souhaite afficher la tournée
     */
    public void ajouterTourneeAffichage(Livreur livreur) {
        // /!\ Coco pense que ce genre de methodes ne doit pas faire partie du controller (car sinon ca veut dire qu'on peut l'appeler quand on veut alors que normalement c'est uniquement quand on est dans certains états -> à ajouter au state correspondant)
        hashmapLivreurTourneeAffichage.put(livreur, new TourneeAffichage(livreur.getTournee(), livreur.getCouleur()));
    }

    /**
     * Retire une tournée de la liste affichée
     *
     * @param livreur le livreur dont on souahite retirer la tournée
     */
    public void supprimerTourneeAffichage(Livreur livreur) {
        if (hashmapLivreurTourneeAffichage.containsKey(livreur)) {
            hashmapLivreurTourneeAffichage.remove(livreur);
        }
    }

    /**
     * Affiche la tournée d'un livreur en particulier
     *
     * @param livreur le ivreur dont on souhaite ajouter la tournée
     */
    public void afficherTournee(Livreur livreur) {
        // si la tournée du livreur n'a jamais été affichée
        if (hashmapLivreurTourneeAffichage.containsKey(livreur)) {
            hashmapLivreurTourneeAffichage.get(livreur).afficherTourneeSurMapView(mapView);
        }
    }

    /**
     * Retire de l'affichage une tournée
     *
     * @param livreur le livreur dont on souhaite afficher la tournée
     */
    public void desafficherTournee(Livreur livreur) {
        if (hashmapLivreurTourneeAffichage.containsKey(livreur)) {
            hashmapLivreurTourneeAffichage.get(livreur).desafficherTourneeSurMapView(mapView);
        }
    }

    /**
     * Ajoute les boutons de selection de livreurs
     *
     * @param livreur le livreur
     * @param bouton  son bouton
     */
    public void ajouterHashMapBoutonSelection(Livreur livreur, ToggleButton bouton) {

        // vérification que le bouton n'a jamais été ajouté dans la hashmap
        Collection<List<ToggleButton>> listeTousBoutons = (hashmapLivreurBoutonSelection.values());
        for (Iterator iterator = listeTousBoutons.iterator(); iterator.hasNext(); ) {
            List<ToggleButton> listeTemp = (List<ToggleButton>) iterator.next();

            if (listeTemp.contains(bouton)) {
                return;
            }
        }
        // si c'est bon on vérifie ensuite si le livreur existe déjà
        if (!hashmapLivreurBoutonSelection.containsKey(livreur)) {
            hashmapLivreurBoutonSelection.put(livreur, new ArrayList<>());
        }

        hashmapLivreurBoutonSelection.get(livreur).add(bouton);
    }

    /**
     * Désactive les boutons de selection de tous les autres livreurs
     *
     * @param livreur le livreur unique dont on souhaite garder le bouton de
     *                déselection
     */
    public void desactiverBoutonsAutresLivreurs(Livreur livreur) {
        for (HashMap.Entry<Livreur, List<ToggleButton>> entry :
                hashmapLivreurBoutonSelection.entrySet()) {
            if (entry.getKey() != livreur) {
                for (ToggleButton tb : entry.getValue()) {
                    tb.setDisable(true);
                    tb.setVisible(false);
                }
            } else {
                for (ToggleButton tb : entry.getValue()) {
                    tb.setText("Déselectionner");
                }
            }
        }
    }

    /**
     * Affiche les boutons de sélection de tous les autres livreurs
     */
    public void activerBoutonsTousLivreurs() {
        for (HashMap.Entry<Livreur, List<ToggleButton>> entry :
                hashmapLivreurBoutonSelection.entrySet()) {
            for (ToggleButton tb : entry.getValue()) {
                tb.setDisable(false);
                tb.setVisible(true);
                tb.setText("Selectionner");
            }
        }
    }

    /**
     * Retire de l'affichage les boutons d'édition et de suppression de
     * livraisons
     */
    public void desactiverBoutonsEditionSupp() {
        tableLivraison.getColumns().get(1).setVisible(false);
        tableLivraison.getColumns().get(2).setVisible(false);
    }

    /**
     * Affiche les boutons d'édition et de suppression des livraisons
     */
    public void activerBoutonsEditionSupp() {
        tableLivraison.getColumns().get(1).setVisible(true);
        tableLivraison.getColumns().get(2).setVisible(true);
    }

    /**
     * Retourne si la tournée pour un livreur à déjà été calculée
     *
     * @param l le livreur pour qui on veut vérifier
     * @return true si oui; false sinon
     */
    public boolean tourneeDejaCalculee(Livreur l) {
        return hashmapLivreurTourneeAffichage.containsKey(l);
    }

}
