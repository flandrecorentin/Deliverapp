package fr.deliverapp.controller;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.Marker;
import fr.deliverapp.model.data.XMLParser;
import fr.deliverapp.model.objects.Intersection;
import fr.deliverapp.model.objects.Livraison;
import fr.deliverapp.model.objects.Livreur;
import fr.deliverapp.model.objects.Plan;
import fr.deliverapp.view.Window;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.util.List;

/**
 * Etat après l'action d'importer une liste de livraison
 *
 * @author Hexanome H4124
 */
public class ImporterListeLivraisonState implements State{

    private Plan plan;
    private List<Livraison> livraisons;

    /**
     * Selection de la carte par l'utilisateur
     * Affichage d'une interface de choix de fichier
     *
     * @param c la controlleur de l'application
     * @param w la fenêtre
     */
    public void selectionnerCarte(Controller c, Window w, boolean fromApp){
        try{
            final FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(w.getPrimaryStage());
            XMLParser parser = XMLParser.getInstance();
            Text falseMap=(Text)w.getPrimaryStage().getScene().lookup("#falseMap");
            if(file != null ){
                String path=file.getAbsolutePath();
                String extension=path.substring(path.lastIndexOf("."));
                int sizeWindow = path.split("\\\\").length-1;
                int sizeMac = path.split("/").length-1;
                String filename = "";
                if(sizeWindow>=0) {
                    filename = path.split("\\\\")[sizeWindow];
                    System.err.println(path.split("\\\\")[sizeWindow]);
                }else if(sizeMac>=0){
                    filename = path.split("/")[sizeMac];
                    System.err.println(path.split("/")[sizeMac]);
                }else{
                    plan=null;
                    Text errorText=(Text)w.getPrimaryStage().getScene().lookup("#errorText");
                    errorText.setText("le fichier choisi est invalide");
                    errorText.setVisible(true);
                    System.out.println("Fichier invalide pour le chargement de la carte");

                    Button validerBtn = (Button) w.getPrimaryStage().getScene().lookup("#validerListeLivraison");
                    validerBtn.setVisible(false);
                }
                if(extension.equals(".xml") && !filename.equals("")){
                    plan = parser.chargerCarte(path);
                    if(plan ==null || plan.getEntrepot().getLocalisation()==null){
                        Text errorText=(Text)w.getPrimaryStage().getScene().lookup("#errorText");
                        errorText.setText("le fichier choisi est invalide");
                        errorText.setVisible(true);
                        falseMap.setVisible(false);
                        Controller.setCheminCarte(null);
                        System.out.println("Chemin invalide.");

                        Text filePathMap = (Text) w.getPrimaryStage().getScene().lookup("#filePathMap");
                        filePathMap.setText(filename);
                        filePathMap.setVisible(true);

                        Button validerBtn = (Button) w.getPrimaryStage().getScene().lookup("#validerListeLivraison");
                        validerBtn.setVisible(false);
                    }
                    else{
                        Text errorText=(Text)w.getPrimaryStage().getScene().lookup("#errorText");
                        if(livraisons != null && !livraisons.isEmpty()){
                            errorText.setText("NB:fichier de la carte importé avec succés il faut réimporter le fichier des livraisons");
                            errorText.setVisible(true);
                            falseMap.setVisible(false);
                            Button validerBtn = (Button) w.getPrimaryStage().getScene().lookup("#validerListeLivraison");
                            validerBtn.setVisible(false);
                            livraisons.clear();
                        }
                        else{
                            errorText.setVisible(false);
                        }
                        System.out.println(file.getAbsolutePath());

                        Text filePathMap = (Text) w.getPrimaryStage().getScene().lookup("#filePathMap");
                        filePathMap.setText(filename);
                        filePathMap.setVisible(true);

                        Controller.setCheminCarte(file.getAbsolutePath());
                    }
                }
                else {
                    plan=null;
                    Text errorText=(Text)w.getPrimaryStage().getScene().lookup("#errorText");
                    errorText.setText("le fichier choisi est invalide");
                    errorText.setVisible(true);
                    falseMap.setVisible(false);
                    Controller.setCheminCarte(null);
                    System.out.println("Fichier invalide pour le chargement de la carte");

                    Text filePathMap = (Text) w.getPrimaryStage().getScene().lookup("#filePathMap");
                    filePathMap.setText(filename);
                    filePathMap.setVisible(true);

                    Button validerBtn = (Button) w.getPrimaryStage().getScene().lookup("#validerListeLivraison");
                    validerBtn.setVisible(false);
                }
            }
            else {
                Text errorText=(Text)w.getPrimaryStage().getScene().lookup("#errorText");
                errorText.setText("le fichier choisi est invalide");
                errorText.setVisible(true);
                falseMap.setVisible(false);
                Controller.setCheminCarte(null);
                System.out.println("Fichier invalide pour le chargement de la carte");

                Button validerBtn = (Button) w.getPrimaryStage().getScene().lookup("#validerListeLivraison");
                validerBtn.setVisible(false);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Importe une liste de livraison sauvergadée dans un fichier XML
     *
     * @param c le controlleur
     * @param w la fenêtre
     */
    public void selectionnerListeLivraison(Controller c, Window w){
        plan=null;
        System.out.println(Controller.getCheminCarte());
        if(Controller.getCheminCarte()!=null){
            plan=XMLParser.getInstance().chargerCarte(Controller.getCheminCarte());
        }
        System.err.println(plan);
        try{
            final FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(w.getPrimaryStage());
            XMLParser parser=XMLParser.getInstance();
            Text falseMap=(Text)w.getPrimaryStage().getScene().lookup("#falseMap");
            if(file != null ){
                String path=file.getAbsolutePath();
                String extension=path.substring(path.lastIndexOf("."));
                int sizeWindow = path.split("\\\\").length-1;
                int sizeMac = path.split("/").length-1;
                String filename = "";
                if(sizeWindow>=0) {
                    filename = path.split("\\\\")[sizeWindow];
                    System.err.println(path.split("\\\\")[sizeWindow]);
                }else if(sizeMac>=0){
                    filename = path.split("/")[sizeMac];
                    System.err.println(path.split("/")[sizeMac]);
                }else{
                    plan=null;
                    Text errorText=(Text)w.getPrimaryStage().getScene().lookup("#errorText");
                    errorText.setText("le fichier choisi est invalide");
                    errorText.setVisible(true);
                    System.out.println("Fichier invalide pour le chargement de la carte");

                    Button validerBtn = (Button) w.getPrimaryStage().getScene().lookup("#validerListeLivraison");
                    validerBtn.setVisible(false);
                }
                if(extension.equals(".xml")){
                    livraisons=parser.restaurerLivraisons(path,c,w);
                    System.out.println("liv"+livraisons);
                    if(livraisons.isEmpty()){
                        if(plan ==null || plan.getEntrepot().getLocalisation()==null){
                                falseMap=(Text)w.getPrimaryStage().getScene().lookup("#falseMap");
                                falseMap.setText("charger la carte avant d'importer la liste des livraisons");
                                falseMap.setVisible(true);
                                Text errorText=(Text)w.getPrimaryStage().getScene().lookup("#errorText");
                                errorText.setVisible(false);
                                livraisons.clear();
                                System.out.println("iciiii");
                                return;
                        }
                        Text errorText=(Text)w.getPrimaryStage().getScene().lookup("#errorText");
                        errorText.setText("le fichier choisi est invalide");
                        falseMap.setVisible(false);
                        errorText.setVisible(true);

                        Text filePathList = (Text) w.getPrimaryStage().getScene().lookup("#filePathList");
                        filePathList.setText(filename);
                        filePathList.setVisible(true);

                        Button validerBtn = (Button) w.getPrimaryStage().getScene().lookup("#validerListeLivraison");
                        validerBtn.setVisible(false);
                    }
                    else{
                        Text errorText=(Text)w.getPrimaryStage().getScene().lookup("#errorText");
                        errorText.setVisible(false);
                        System.out.println(file.getAbsolutePath());
                        if(plan != null && plan.getEntrepot().getLocalisation()!=null){
                            for(Livraison livraison:livraisons){
                                boolean verif=false;
                                for(Intersection intersection : plan.getListeIntersection()){
                                    if(livraison.getPointLivraison().getLat()==intersection.getLat() && livraison.getPointLivraison().getLon()==intersection.getLon()){
                                        verif=true;
                                    }
                                }
                                if(verif==false){
                                    falseMap=(Text)w.getPrimaryStage().getScene().lookup("#falseMap");
                                    falseMap.setText("la liste des livraisons ne correspond pas à la carte choisie");
                                    falseMap.setVisible(true);
                                    errorText.setVisible(false);
                                    livraisons.clear();
                                    return;
                                }
                            }
                        }
                        falseMap.setVisible(false);
                        Controller.setCheminLivraison(file.getAbsolutePath());

                        Text filePathList = (Text) w.getPrimaryStage().getScene().lookup("#filePathList");
                        filePathList.setText(filename);
                        filePathList.setVisible(true);

                        Button validerBtn = (Button) w.getPrimaryStage().getScene().lookup("#validerListeLivraison");
                        validerBtn.setVisible(true);
                    }
                }
                else {
                    Text errorText=(Text)w.getPrimaryStage().getScene().lookup("#errorText");
                    errorText.setText("le fichier choisi est invalide");
                    errorText.setVisible(true);
                    falseMap.setVisible(false);
                    if(livraisons!=null){
                        livraisons.clear();
                    }
                    System.out.println("Fichier invalide pour le chargement de la carte");

                    Text filePathList = (Text) w.getPrimaryStage().getScene().lookup("#filePathList");
                    filePathList.setText(filename);
                    filePathList.setVisible(true);

                    Button validerBtn = (Button) w.getPrimaryStage().getScene().lookup("#validerListeLivraison");
                    validerBtn.setVisible(false);
                }
            }
            else {
                Text errorText=(Text)w.getPrimaryStage().getScene().lookup("#errorText");
                errorText.setText("le fichier choisi est invalide");
                errorText.setVisible(true);
                falseMap.setVisible(false);
                if(livraisons!=null){
                    livraisons.clear();
                }
                System.out.println("Fichier invalide pour le chargement de la carte");

                Button validerBtn = (Button) w.getPrimaryStage().getScene().lookup("#validerListeLivraison");
                validerBtn.setVisible(false);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Valide l'importation de la liste des livraisons importée
     *
     * @param c le controlleur
     * @param w la fenêtre
     * @param tableLivraison le tableau d'affichage des livraisons
     */
    public void validerListeLivraison(Controller c, Window w, TableView tableLivraison){
        plan =null;
        Text falseMap=(Text)w.getPrimaryStage().getScene().lookup("#falseMap");
        if(Controller.getCheminCarte()!=null){
            plan=XMLParser.getInstance().chargerCarte(Controller.getCheminCarte());
        }
        if((plan == null || plan.getEntrepot().getLocalisation()==null) && (livraisons==null ||livraisons.isEmpty()) ){
            Text errorText=(Text)w.getPrimaryStage().getScene().lookup("#errorText");
            errorText.setText("deux fichiers manquent:les données de la carte et la liste de livraisons");
            errorText.setVisible(true);
            falseMap.setVisible(false);
        }
        else if(livraisons == null || livraisons.isEmpty()){
            Text errorText=(Text)w.getPrimaryStage().getScene().lookup("#errorText");
            errorText.setText("un fichier contenant les livraisons doit etre importé");
            errorText.setVisible(true);
            falseMap.setVisible(false);
        }
        else if (plan == null || plan.getEntrepot().getLocalisation()==null){
            Text errorText=(Text)w.getPrimaryStage().getScene().lookup("#errorText");
            errorText.setText("un fichier contenant les données de la carte doit etre importé");
            errorText.setVisible(true);
            falseMap.setVisible(false);
        }
        else{
            Text errorText=(Text)w.getPrimaryStage().getScene().lookup("#errorText");
            errorText.setVisible(false);
            falseMap.setVisible(false);
            for(Livraison livraison:livraisons){
                for(Livreur livreur :w.getLivreurs()){
                    if(livreur.getNom().equals(livraison.getLivreur().getNom())){
                        livraison.setLivreur(livreur);
                        livreur.getListeLivraison().add(livraison);
                    }
                }
                w.getLivraisons().add(livraison);
            }


            c.setCurrentState(c.principalState);
            w.setPagePrincipale("import");
        }
    }


}
