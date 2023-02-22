package fr.deliverapp.controller;

import fr.deliverapp.model.data.XMLParser;
import fr.deliverapp.model.objects.Plan;
import fr.deliverapp.view.Window;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;


/**
 * État quand l'utilisateur doit sélectionner un fichier XML carte
 *
 * @author Hexanome H4124
 */
public class SelectionCarteState implements State {

    /**
     * Selection de la carte par l'utilisateur
     * Affichage d'une interface de choix de fichier
     *
     * @param c la controlleur de l'application
     * @param w la fenêtre
     */
    public void selectionnerCarte(Controller c, Window w, boolean fromApp){
        Plan plan;
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(w.getPrimaryStage());
        Text errorText=(Text)w.getPrimaryStage().getScene().lookup("#errorText");
        XMLParser parser = XMLParser.getInstance();
        try{
            if(file != null ){
                String path=file.getAbsolutePath();
                String extension=path.substring(path.lastIndexOf("."));
                if(extension.equals(".xml")){
                    plan=null;
                    try{
                        plan = parser.chargerCarte(path);
                    }catch(Exception e){
                        plan = null;
                    }
                    if(plan==null || plan.getEntrepot().getLocalisation()==null){
                        errorText.setText("Le fichier choisi est invalide");
                        errorText.setVisible(true);
                        System.out.println("Fichier invalide pour le chargement de la carte");
                        if(fromApp == true) {
                            c.setCurrentState(c.principalState);
                        }
                    }
                    else{
                        errorText.setVisible(false);
                        System.out.println(file.getAbsolutePath());
                        c.setCurrentState(c.principalState);
                        Controller.setCheminCarte(file.getAbsolutePath());
                        w.setPagePrincipale("changement");
                    }
                }
                else {
                    errorText=(Text)w.getPrimaryStage().getScene().lookup("#errorText");
                    errorText.setText("Le fichier choisi est invalide");
                    errorText.setVisible(true);
                    System.out.println("Fichier invalide pour le chargement de la carte");
                    if(fromApp == true) {
                        c.setCurrentState(c.principalState);
                    }
                }
            }
            else {
                errorText=(Text)w.getPrimaryStage().getScene().lookup("#errorText");
                errorText.setVisible(true);
                System.out.println("Fichier invalide pour le chargement de la carte");
                if(fromApp == true) {
                    c.setCurrentState(c.principalState);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
