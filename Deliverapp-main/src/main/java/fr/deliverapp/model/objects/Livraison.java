package fr.deliverapp.model.objects;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.Marker;
import fr.deliverapp.model.package_diagram.Observable;
import com.sothawo.mapjfx.*;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Pair;

import java.awt.*;
import java.util.List;

/**
 * Représente une livraison entrée par l'utilisateur
 *
 * @author Hexanome H4124
 */
public class Livraison extends Observable {

    /**
     * Le point de livraison sur la plan
     */
    private Intersection pointLivraison;

    private Plan plan;

    /**
     * L'adresse de la livraison
     */
    private String adresse;

    /**
     * Nombre de minutes à attendre une fois arrivé sur place
     */
    private int nbMinutesAttente;

    /**
     * La plage horaire de la livraison (8 : 8h - 9h, 9: 9h - 10h...)
     */
    private int horaire;

    /**
     * La propriété affichable de cette livraison
     */
    private StringProperty stringProperty;

    /**
     * Le livreur assigné à cette livraison
     */
    private Livreur livreur;

    // TODO : c'est koi ?
    private Pair<Marker, Marker> markers;

    /**
     * Constructeur d'une livraison
     *
     * @param pointLivraison le point de livraison
     * @param horaire        la plage horaire de la livraison
     */
    public Livraison(Intersection pointLivraison, int horaire) {
        this.pointLivraison = pointLivraison;
        this.horaire = horaire;
        this.stringProperty = new SimpleStringProperty("\n de "+this.horaire+"h à "+(this.horaire+1)+"h \n");

        this.nbMinutesAttente = 0;
    }

    /**
     *
     * @param livraison
     * @param listMarkers
     * @param mapView
     */
    public Livraison(Livraison livraison, List<Marker> listMarkers, MapView mapView) {
        this.nbMinutesAttente = 0;
        final Marker[] markerSelect = new Marker[1];
        this.pointLivraison = livraison.pointLivraison;
        this.horaire = livraison.horaire;
        this.livreur = livraison.livreur;
        this.plan = livraison.plan;

        if(plan.getListeSegment() != null) {
            for (Segment segment : plan.getListeSegment()) {
                if (segment.getDestination().sameCoordinates(pointLivraison)) {
                    this.adresse = segment.getNom();
                    break;
                }
            }
        }

        this.stringProperty = new SimpleStringProperty(this.adresse+"\n de "+this.horaire+"h à "+(this.horaire+1)+"h \n"+this.getLivreur().getNom());

        String image = "";
        if(this.livreur.getNom().equals("Bob")){
            image = "/images/circle_red.png";
        }else{
            image = "/images/circle_blue.png";
        }

        String finalImage = image;
        listMarkers.forEach(marker -> {
            Intersection i = new Intersection(marker.getPosition().getLatitude(), marker.getPosition().getLongitude());
            if(this.pointLivraison.sameCoordinates(i)){
                markerSelect[0] = new Marker(getClass().getResource(finalImage), -12, -12)
                        .setPosition(new Coordinate(marker.getPosition().getLatitude(),marker.getPosition().getLongitude()))
                        .setVisible(false);
                mapView.addMarker(markerSelect[0]);
                this.markers = new Pair<>(marker, markerSelect[0]);
            }
        });
    }
    public Livraison(Plan plan, Intersection pointLivraison, int horaire, Livreur livreur, Pair<Marker,Marker> markers) {
        this.nbMinutesAttente = 0;
        this.pointLivraison = pointLivraison;
        this.horaire = horaire;
        this.livreur = livreur;
        this.markers = markers;
        this.plan = plan;

        if(plan.getListeSegment() != null) {
            for (Segment segment : plan.getListeSegment()) {
                if (segment.getDestination().sameCoordinates(pointLivraison)) {
                    this.adresse = segment.getNom();
                    break;
                }
            }
        }
        if(this.adresse == null){
            this.adresse = "Adresse non définie";
        }

        this.stringProperty = new SimpleStringProperty(this.adresse+"\n de "+this.horaire+"h à "+(this.horaire+1)+"h \n"+this.getLivreur().getNom());
    }

    /**
     *
     * @param plan
     * @param pointLivraison
     * @param horaire
     * @param livreur
     * @param listMarkers
     * @param mapView
     */
    public Livraison(Plan plan, Intersection pointLivraison, int horaire, Livreur livreur, List<Marker> listMarkers, MapView mapView) {
        final Marker[] markerSelect = new Marker[1];
        this.pointLivraison = pointLivraison;
        this.horaire = horaire;
        this.livreur = livreur;
        this.plan = plan;

        if(plan.getListeSegment() != null) {
            for (Segment segment : plan.getListeSegment()) {
                if (segment.getDestination().sameCoordinates(pointLivraison)) {
                    this.adresse = segment.getNom();
                    break;
                }
            }
        }
        if(this.adresse == null){
            this.adresse = "Adresse non définie";
        }

        this.stringProperty = new SimpleStringProperty(this.adresse+"\n de "+this.horaire+"h à "+(this.horaire+1)+"h \n"+this.getLivreur().getNom());

        String image = "";
        if(this.livreur.getNom().equals("Bob")){
            image = "/images/circle_red.png";
        }else{
            image = "/images/circle_blue.png";
        }

        String finalImage = image;
        listMarkers.forEach(marker -> {
            Intersection i = new Intersection(marker.getPosition().getLatitude(), marker.getPosition().getLongitude());
            if(this.pointLivraison.sameCoordinates(i)){
                markerSelect[0] = new Marker(getClass().getResource(finalImage), -12, -12)
                        .setPosition(new Coordinate(marker.getPosition().getLatitude(),marker.getPosition().getLongitude()))
                        .setVisible(false);
                mapView.addMarker(markerSelect[0]);
                this.markers = new Pair<>(marker, markerSelect[0]);
            }
        });
    }

    /**
     * Méthode qui retourne le texte décrivant une livraison
     *
     * @return le texte décrivant une livraison
     */
    public StringProperty getStringProperty() {
        return stringProperty;
    }

    public void changerCouleurMarker(String couleur, MapView mapView) {
        mapView.removeMarker(markers.getValue());
        Marker nouveauMarker = new Marker(getClass().getResource("/images/circle_"+couleur+".png"), -12, -12)
                .setPosition(new Coordinate(markers.getKey().getPosition().getLatitude(),markers.getKey().getPosition().getLongitude()))
                .setVisible(false);
        mapView.addMarker(nouveauMarker);
        this.markers = new Pair<>(this.markers.getKey(), nouveauMarker);
        this.markers.getKey().setVisible(false);
        this.markers.getValue().setVisible(true);
    }

    /**
     * Méthode qui retourne l'adresse d'une livraison
     *
     * @return une chaine de caractère, l'adresse
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * Méthode qui affacte une adresse à une livraison à partir de son point
     * de livraison
     *
     * @param pointLivraison le point de livraison
     */
    public void setAdresse(Intersection pointLivraison) {
        if(plan.getListeSegment() != null) {
            for (Segment segment : plan.getListeSegment()) {
                if (segment.getDestination().sameCoordinates(pointLivraison)) {
                    this.adresse = segment.getNom();
                    break;
                }
            }
        }
    }

    /**
     * Méthode qui crée le texte décrivant une livraison à partir des
     * informations qui la composent
     *
     * @param adresse son adresse
     * @param horaire sa plage horaire
     * @param livreur son livreur
     */
    public void setStringProperty(String adresse, int horaire, Livreur livreur) {
        this.stringProperty.set(adresse + "\n de " + horaire + "h à " + (horaire + 1) + "h \n" + livreur.getNom());
    }


    public StringProperty stringPropertyProperty() {
        return stringProperty;
    }

    /**
     * Méthode qui retourne le point de livraison
     *
     * @return <code>this.pointLivraison</code>
     */
    public Intersection getPointLivraison() {
        return pointLivraison;
    }

    /**
     * Méthode qui affecte un point de livraison
     *
     * @param pointLivraison
     */
    public void setPointLivraison(Intersection pointLivraison) {
        this.pointLivraison = pointLivraison;
        setAdresse(this.pointLivraison);
        setStringProperty(this.adresse, this.horaire, this.livreur);
    }

    /**
     * Méthode qui retourne la plage horaire d'une livraison
     *
     * @return <code>this.horaire</code>; 8 : "8h - 9h", 9 : "9h - 10h" ...
     */
    public int getHoraire() {
        return horaire;
    }

    /**
     * Méthode qui permet de modifier la plage horaire d'une livraison
     *
     * @param horaire la nouvelle plage horaire; 8 : "8h - 9h", 9 : "9h - 10h" ...
     */
    public void setHoraire(int horaire) {
        this.horaire = horaire;
        setStringProperty(this.adresse, this.horaire, this.livreur);
    }

    /**
     * Méthode qui permet d'obtenir le livreur assigné à la livraison
     *
     * @return le livreur
     */
    public Livreur getLivreur() {
        return livreur;
    }

    /**
     * Méthode qui permet d'assigner un livreur à une livraison
     *
     * @param livreur le livreur assigné
     */
    public void setLivreur(Livreur livreur) {
        this.livreur = livreur;
        setStringProperty(this.adresse, this.horaire, this.livreur);
    }

    // TODO : ???
    public Pair<Marker, Marker> getMarkers() {
        return markers;
    }

    // TODO : ???
    public void setMarkers(Pair<Marker, Marker> markers) {
        this.markers = markers;
    }

    public void setNbMinutesAttente(int nbMinutesAttente){
        this.nbMinutesAttente = nbMinutesAttente;
    }
    public int getNbMinutesAttente(){
        return this.nbMinutesAttente;
    }

    /**
     * Méthode qui ppermet d'afficher une livraison dans la console
     *
     * @return string
     */
    @Override
    public String toString() {
        return "Livraison{" +
                "pointLivraison=" + pointLivraison +
                ", horaire=" + horaire +
                ", livreur=" + livreur +
                '}';
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        boolean egalite = true;
        if(o instanceof  Livraison){
            Livraison oLivraison = (Livraison) o;
            if(oLivraison.getPointLivraison().getLat()!=this.getPointLivraison().getLat()){
                egalite = false;
            }
            if(oLivraison.getPointLivraison().getLon()!=this.getPointLivraison().getLon()){
                egalite = false;
            }
            if(oLivraison.getHoraire()!=this.getHoraire()){
                egalite = false;
            }
        }
        else{
            return false;
        }
        return egalite;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }
}
