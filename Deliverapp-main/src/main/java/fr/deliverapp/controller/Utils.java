package fr.deliverapp.controller;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.CoordinateLine;
import fr.deliverapp.model.objects.Intersection;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Méthodes utiles
 *
 * @author Hexanome H4124
 */
public class Utils {

    /**
     * Crée une route colorée, à partir d'intersections que l'on peut
     * ajouter à la carte graphique
     *
     * @param color    la couleur de la route désirée
     * @param intersec les intersections composant la route
     * @return la route graphique coloriée
     */
    public static CoordinateLine createColoredRoute(Color color,
                                                    Intersection... intersec) {
        List<Coordinate> coord = new ArrayList<Coordinate>();
        for (Intersection i : intersec) {
            coord.add(new Coordinate(i.getLat(), i.getLon()));
        }
        CoordinateLine toAdd = new CoordinateLine(coord);
        toAdd.setColor(color);
        toAdd.setVisible(true);
        return toAdd;
    }

    /**
     * Retourne un angle entre deux points via leurs latitudes et longitudes
     *
     * @param lat1  latitude du 1er point
     * @param long1 longitude du 2eme point
     * @param lat2  latitude du 2eme point
     * @param long2 longitude du 2eme point
     * @return l'angle en degré entre les deux points
     */
    public static double angleFromCoordinate2(double lat1, double long1,
                                              double lat2, double long2) {
        double R = 6371;
        double x1 = R * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(long1));
        double y1 = R * Math.cos(Math.toRadians(lat1)) * Math.sin(Math.toRadians(long1));

        double x2 = R * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(long2));
        double y2 = R * Math.cos(Math.toRadians(lat2)) * Math.sin(Math.toRadians(long2));

        double brng = Math.atan2(x2 - x1, y2 - y1);

        brng = Math.toDegrees(brng);
//        brng = (brng + 360) % 360;
//        brng = 360 - brng; // count degrees counter-clockwise - remove to make clockwise

        return brng;
    }
}
