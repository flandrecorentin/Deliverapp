package fr.deliverapp.model.data;

import fr.deliverapp.model.objects.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.System.currentTimeMillis;
import static org.junit.jupiter.api.Assertions.*;

class XMLParserTest {

    @Test
    void chargerCarte() {

        XMLParser xml = XMLParser.getInstance();
        long t1 = currentTimeMillis();
        Plan mediumPlan = xml.chargerCarte("src/main/resources/xmlmaps/mediumMap.xml");
        long t2 = currentTimeMillis();
        System.err.println("Temps de chargement de la mediumMap : "+(t2-t1)+"ms");

        Entrepot e = mediumPlan.getEntrepot();
        List<Intersection> listInter = mediumPlan.getListeIntersection();
        List<Segment> listSeg = mediumPlan.getListeSegment();
        assert(listInter.contains(e.getLocalisation()));

        assert(listInter.size() == 1448);
        assert(listSeg.size() == 3097);

        Segment segTest = listSeg.get(1);
        Intersection interTest = listInter.get(1);

        assert(interTest.getLat() == 45.75978 && interTest.getLon() == 4.875795);
        assert(segTest.getNom().equals("Avenue Lacassagne") && segTest.getOrigine().getLat() == 45.750404 && segTest.getDestination().getLat() == 45.74979 && segTest.getDistance() == 118.890465);


    }
}