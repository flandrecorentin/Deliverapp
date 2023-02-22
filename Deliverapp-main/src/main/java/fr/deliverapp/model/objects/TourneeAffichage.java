package fr.deliverapp.model.objects;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.CoordinateLine;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.Marker;
import fr.deliverapp.controller.Utils;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Représente une tournée affichable (liste de routes et de point à suivre)
 *
 * @author Hexanome H4124
 */
public class TourneeAffichage {

    private Tournee tournee;
    private Color color;
    private ArrayList<CoordinateLine> listeLignes = new ArrayList<CoordinateLine>();
    private ArrayList<Marker> listeFleches = new ArrayList<Marker>();

    public TourneeAffichage(Tournee tourneeInput, Color colorInput) {
        tournee = tourneeInput;
        color = colorInput;

        for(Trajet trajet: tournee.getListeTrajet()) {
            for (Segment s : trajet.getListeSegment()) {
                Intersection origin = s.getOrigine();
                Intersection end = s.getDestination();
                CoordinateLine result = Utils.createColoredRoute(color, origin, end);
                listeLignes.add(result);

                double lonMiddle = (origin.getLon() + end.getLon()) / 2.0;
                double latMiddle = (origin.getLat() + end.getLat()) / 2.0;
                int bearingAngle = (int) (Utils.angleFromCoordinate2(
                        origin.getLat(), origin.getLon(), end.getLat(), end.getLon()));
                Marker orientation = new Marker(getClass().getResource("/images/right-arrow.png"), 0, 0)
                        .setPosition(new Coordinate(latMiddle, lonMiddle)).setRotation(bearingAngle).setVisible(true);
                listeFleches.add(orientation);
            }
        }
    }

    public void afficherTourneeSurMapView(MapView mapView) {
        for(CoordinateLine cl : listeLignes) {
            cl.setVisible(true);
            mapView.addCoordinateLine(cl);
        }
        for(Marker m : listeFleches) {
            m.setVisible(true);
            mapView.addMarker(m);
        }
    }

    public void desafficherTourneeSurMapView(MapView mapView) {
        for(CoordinateLine cl : listeLignes) {
            cl.setVisible(false);
            mapView.removeCoordinateLine(cl);
        }
        for(Marker m : listeFleches) {
            m.setVisible(false);
            mapView.removeMarker(m);
        }
    }

}
