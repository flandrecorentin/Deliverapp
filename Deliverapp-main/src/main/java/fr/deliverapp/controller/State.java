package fr.deliverapp.controller;

import com.sothawo.mapjfx.CoordinateLine;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;
import com.sothawo.mapjfx.Projection;
import fr.deliverapp.controller.command.ListOfCommands;
import fr.deliverapp.model.objects.Intersection;
import fr.deliverapp.model.objects.Livraison;
import fr.deliverapp.model.objects.Livreur;
import fr.deliverapp.view.Window;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.util.ArrayList;


/**
 * Listes des méthodes appelables par la Window
 * En fonction du State actuel du controller certaines méthodes seront
 * appelées ou non
 *
 * @author Hexanome H4124
 */
public interface State {
    /**
     * Créer une nouvelle tournée à partir de zéro
     *
     * @param c la controlleur de l'application
     * @param w la fenêtre
     */
    public default void creerTournee(Controller c, Window w) {
    }

    ;

    /**
     * Choix d'importer une tournée déja existante à partir d'une sauvegarde
     *
     * @param c la controlleur de l'application
     * @param w la fenêtre
     */
    public default void importerTournee(Controller c, Window w) {
    }

    ;

    /**
     * Selection de la carte par l'utilisateur
     * Affichage d'une interface de choix de fichier
     *
     * @param c la controlleur de l'application
     * @param w la fenêtre
     * @param fromApp est-ce qu'on arrive de la fenêtre déjà
     */
    public default void selectionnerCarte(Controller c, Window w, boolean fromApp) {
    }

    ;

    /**
     * Importation d'une nouvelle carte à partir d'un fichier XML
     *
     * @param c le controlleur de l'application
     * @param w la fenêtre
     */
    public default void changerCarte(Controller c, Window w) {
    }

    ;

    /**
     * Initialise la carte et ses contrôles
     *
     * @param projection la projection
     * @param mapView          le JFX MapView d'id mapView
     * @param labelZoom        le JFX Label d'id labelZoom (affiche le niveau de zoom)
     * @param labelEvent       le JFX Label d'id labelEvent
     * @param colonneLivreur    la colonne des noms des livreurs
     * @param colonneLivraison la colonne des infos livraisons
     * @param tableLivreur le tableau des livreurs
     * @param tableLivraison le tableau des livraisons
     * @param listeMarkers     la liste des intersections cliquables
     * @param listeLines       la liste des routes praticables
     * @param listeInter       la liste des intersections du plan
     * @param listeCommande    la liste des commandes (pour undo / redo)
     * @param c                le Controller
     * @param w                la Window
     */
    public default void initialiserCarteEtControles(Projection projection,
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
                                                    Controller c, Window w) {
    }

    ;

    /**
     * Intention d'ajout d'une livraison
     *
     * @param c                       le controlleur
     * @param w                       la fenêtre
     * @param VBoxCreationLivraison   la zone affichée pour les instructions
     *                                d'ajout
     * @param TitreVBoxAjoutLivraison le texte
     */
    public default void ajouterLivraison(Controller c, Window w, VBox VBoxCreationLivraison, Text TitreVBoxAjoutLivraison) {
    }

    ;

    /**
     * Met la livraison existante en attribut
     *
     * @param livraison la livraison précédente à passer d'un état à un autre
     */
    public default void setLivraisonApresCalculTournee(Livraison livraison) {
    }

    ;

    /**
     * Calcule la tournée pour le livreur sélectionné
     *
     * @param c          le controlleur
     * @param w          la fenêtre
     */
    public default void calculerTournee(Controller c, Window w) {
    }

    ;

    /**
     * Valide la tournée calculée pour le livreur sélectionné
     *
     * @param c le controlleur
     * @param w la fenêtre
     */
    public default void validerTournee(Controller c, Window w) {
    }

    /**
     * Mise à jour du label affichant le nombre de livraison du livreur sélectionné
     *
     * @param c le controlleur
     * @param w la fenêtre
     */
    public default void majLabelNbLivraisons(Controller c, Window w) {
    }

    ;

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
    public default void revenirAvantCalculTournee(Controller c, Window w,
                                                  VBox VBoxCreationLivraison, Text TitreVBoxAjoutLivraison,
                                                  ComboBox comboboxHoraire,
                                                  Label texteSelectionPointLivraison, Label texteSelectionPlageHoraire,
                                                  Button buttonValiderAjoutLivraison, Button buttonAnnulerAjoutLivraison,
                                                  Text labelInfos) {
    }

    ;

    /**
     * Permet de passer à l'état LivreurSelectionneState
     *
     * @param c                      le controlleur
     * @param w                      la fenêtre
     * @param boutonAjouterLivraison le bouton qui permet d'ajouter une
     *                               livraison
     * @param VBoxCreationLivraison  la zone affichée pour ajouter une livraison
     */
    public default void selectionnerLivreur(Controller c, Window w, Button boutonAjouterLivraison, VBox VBoxCreationLivraison) {
    }

    ;

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
    public default void deselectionnerLivreur(Controller c, Window w,
                                              TableColumn<Livraison, String> colonneSelectionLivraison,
                                              BorderPane BorderPaneInfoLivreur,
                                              Button boutonAjouterLivraison, Button boutonCalculerTournee) {
    }

    ;

    /**
     * Sauvegarde la liste des livraisons
     *
     * @param c le controlleur
     * @param w la fenêtre
     */
    public default void sauvegarderListeLivraison(Controller c, Window w) {
    }

    ;

    /**
     * Génère la feuille de route
     *
     * @param c le controlleur
     * @param w la fenêtre
     */
    public default void genererFeuilleRoute(Controller c, Window w) {
    }

    ;

    /**
     * Télécharge la feuille de route
     *
     * @param c le controlleur
     * @param w la fenêtre
     */
    public default void telechargerFeuilleRoute(Controller c, Window w) {
    }

    ;

    /**
     * Passe de l'état TournéeCalculée à l'état LivreurSelectionne
     *
     * @param c le controlleur
     * @param w la window
     */
    public default void terminer(Controller c, Window w) {
    }

    ;

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
    public default void validerAjoutLivraison(Controller c, Window w,
                                              ComboBox comboboxHoraire, ListOfCommands listeCommande, VBox VBoxCreationLivraison,
                                              Label texteSelectionPointLivraison, Label texteSelectionPlageHoraire) {
    }

    ;

    /**
     * Annule l'ajout d'une livraison, revient à LivreurSelectionneState
     *
     * @param c le controlleur
     * @param w la fenêtre
     */
    public default void annulerAjoutLivraison(Controller c, Window w, VBox VBoxCreationLivraison, ComboBox comboboxHoraire){};

    /**
     * Annule la dernière commande effectuée
     *
     * @param listeCommande
     */
    public default void undo(ListOfCommands listeCommande) {
    }

    ;

    /**
     * Effectue la dernière commande annulée
     *
     * @param listeCommande
     */
    public default void redo(ListOfCommands listeCommande) {
    }

    ;


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
    public default void ajouterLivraisonApresCalcul(Controller c, Window w,
                                                    VBox VBoxCreationLivraison,
                                                    Text TitreVBoxAjoutLivraison, Label texteSelectionPointLivraison,
                                                    Label texteSelectionPlageHoraire, Button buttonValiderAjoutLivraison,
                                                    Button buttonAnnulerAjoutLivraison) {
    }

    ;

    /**
     * Annule l'ajout d'une livraison après le calcul
     *
     * @param c                            le controlleur
     * @param w                            la fenêtre
     * @param comboboxHoraire              la combobox de la plage horaire
     * @param VBoxCreationLivraison        la zone affichée pour créer une livraison
     * @param texteSelectionPointLivraison le texte qui indique comment
     *                                     sélectionner un point de livraison
     * @param texteSelectionPlageHoraire   le texte qui indique comment
     *                                     sélectionner une plage horaire
     */

    public default void annulerAjoutApresCalcul(Controller c, Window w, VBox VBoxCreationLivraison,
                                                ComboBox comboboxHoraire,
                                                Label texteSelectionPointLivraison, Label texteSelectionPlageHoraire,
                                                Button buttonValiderAjoutLivraison, Button buttonAnnulerAjoutLivraison) {
    }

    ;

    /**
     * Valide l'ajout d'une livraison après le calcul d'une tournée
     *
     * @param c                            le controlleur
     * @param w                            la fenêtre
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
    public default void validerAjoutApresCalcul(Controller c, Window w,
                                                ComboBox comboboxHoraire, ListOfCommands listeCommande,
                                                Label texteSelectionPointLivraison, Label texteSelectionPlageHoraire,
                                                Button buttonValiderAjoutLivraison, Button buttonAnnulerAjoutLivraison) {
    }

    ;

    /**
     * Choix de la livraison avant laquelle on veut insérer une livraison
     * quand la tournée est déja calculée
     *
     * @param c                            le controlleur
     * @param w                            la fenêtre
     * @param VBoxAjouterLivraison         la zone affichée lors de l'ajout
     *                                     d'une
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
     * @param labelInfos                   le texte d'info affiché
     */
    public default void validerChoixPrecedentApresCalcul(Controller c, Window w, VBox VBoxAjouterLivraison,
                                                         ComboBox comboboxHoraire, ListOfCommands listeCommande,
                                                         Label texteSelectionPointLivraison, Label texteSelectionPlageHoraire,
                                                         Button buttonValiderAjoutLivraison, Button buttonAnnulerAjoutLivraison, Text labelInfos) {
    }

    ;

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
    public default void supprimerLivraisonApresCalcul(Controller c, Window w, VBox VBoxCreationLivraison,
                                                      Text TitreVBoxAjoutLivraison,
                                                      ComboBox comboboxHoraire, ListOfCommands listeCommande,
                                                      Label texteSelectionPointLivraison, Label texteSelectionPlageHoraire,
                                                      Button buttonValiderAjoutLivraison, Button buttonAnnulerAjoutLivraison) {
    }

    ;

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
    public default void annulerSuppressionApresCalcul(Controller c, Window w, VBox VBoxCreationLivraison,
                                                      ComboBox comboboxHoraire,
                                                      Label texteSelectionPointLivraison, Label texteSelectionPlageHoraire,
                                                      Button buttonValiderAjoutLivraison, Button buttonAnnulerAjoutLivraison) {
    }

    ;

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
    public default void confirmerSuppressionLivraisonApresCalcul(Controller c, Window w, VBox VBoxCreationLivraison,
                                                                 Text TitreVBoxAjoutLivraison,
                                                                 ComboBox comboboxHoraire, ListOfCommands listeCommande,
                                                                 Label texteSelectionPointLivraison, Label texteSelectionPlageHoraire,
                                                                 Button buttonValiderAjoutLivraison, Button buttonAnnulerAjoutLivraison, Text labelInfos) {
    }

    ;

    /**
     * Importe une liste de livraison sauvergadée dans un fichier XML
     *
     * @param c le controlleur
     * @param w la fenêtre
     */
    public default void selectionnerListeLivraison(Controller c, Window w) {
    }

    /**
     * Valide l'importation de la liste des livraisons importée
     *
     * @param c le controlleur
     * @param w la fenêtre
     * @param tableLivraison le tableau d'affichage des livraisons
     */
    public default void validerListeLivraison(Controller c, Window w, TableView tableLivraison) {
    }

    public default void initialiserBoutonsState(Controller c, Window w) {
    }


}
