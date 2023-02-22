package fr.deliverapp.model.objects;

import fr.deliverapp.model.data.XMLParser;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.*;

import static java.lang.System.currentTimeMillis;
import static org.junit.jupiter.api.Assertions.*;

class TourneeTest {

    @Test
    void algoAvecMap5Points() {
        ArrayList<Intersection> listInt = new ArrayList<Intersection>();
        Intersection a = new Intersection(0.1,0.1);
        Intersection b = new Intersection(1.1,1.1);
        Intersection c = new Intersection(2.1,2.1);
        Intersection d = new Intersection(3.1,3.1);
        Intersection j = new Intersection(4.1,4.1);
        Intersection e = new Intersection(5.1,5.1);
        HashMap<Long, Intersection> mapInter = new HashMap<Long, Intersection>();
        mapInter.put(Long.valueOf(100),a);
        mapInter.put(Long.valueOf(101),b);
        mapInter.put(Long.valueOf(102),c);
        mapInter.put(Long.valueOf(103),d);
        mapInter.put(Long.valueOf(104),j);
        mapInter.put(Long.valueOf(105),e);
        listInt.add(a);
        listInt.add(b);
        listInt.add(c);
        listInt.add(d);
        listInt.add(j);
        listInt.add(e);

        Segment ab = new Segment("ab", 3000, a, b);
        Segment ae = new Segment("ae", 5000, a, e);
        Segment bc = new Segment("bc", 6000, b, c);
        Segment be = new Segment("be", 1000, b, e);
        Segment cd = new Segment("cd", 2000, c, d);
        Segment da = new Segment("da", 3000, d, a);
        Segment dc = new Segment("dc", 7000, d, c);
        Segment eb = new Segment("eb", 1000, e, b);
        Segment ec = new Segment("ec", 3000, e, c);
        Segment ed = new Segment("ed", 6000, e, d);
        Segment aj = new Segment("aj", 1000, a, j);
        Segment jb = new Segment("jb", 1000, j, b);
        List<Segment> listSeg = new LinkedList<Segment>();
        listSeg.add(ab);
        listSeg.add(ae);
        listSeg.add(bc);
        listSeg.add(be);
        listSeg.add(cd);
        listSeg.add(da);
        listSeg.add(dc);
        listSeg.add(eb);
        listSeg.add(ec);
        listSeg.add(ed);
        listSeg.add(aj);
        listSeg.add(jb);

        Plan plan =  new Plan(new Entrepot(a), listSeg, listInt);
        Livreur livreur = new Livreur("John");


        Livraison l3=new Livraison(plan, d,8,livreur,null);
        Livraison l4=new Livraison(plan, e,10,livreur,null);
        Livraison l2=new Livraison(plan, c,11,livreur,null);
        Livraison l1=new Livraison(plan, b,10,livreur,null);

        /*Livraison l3 = new Livraison(d, 8);
        Livraison l4 = new Livraison(e, 10);
        Livraison l2 = new Livraison(c, 11);
        Livraison l1 = new Livraison(b, 10);


        l1.setPlan(plan);
        l2.setPlan(plan);
        l3.setPlan(plan);
        l4.setPlan(plan);
        l1.setStringProperty(l1.getAdresse(), 10, livreur);
        l1.setLivreur(livreur);
        l2.setStringProperty(l2.getAdresse(), 11, livreur);
        l2.setLivreur(livreur);
        l3.setStringProperty(l3.getAdresse(), 8, livreur);
        l3.setLivreur(livreur);
        l4.setStringProperty(l4.getAdresse(), 8, livreur);
        l4.setLivreur(livreur);*/


        LinkedList<Livraison> listeLivraisons = new LinkedList<Livraison>();
        listeLivraisons.add(l1);
        listeLivraisons.add(l2);
        listeLivraisons.add(l3);
        listeLivraisons.add(l4);

        List<Intersection> i = plan.getListeIntersection();



        Tournee t = new Tournee(plan,listeLivraisons,livreur);

        List<Pair<Livraison, Date>> listeLivraisonDate = t.getListeLivraison();

        Date date1 = listeLivraisonDate.get(1).getValue();


        assert(t.getListeLivraison().get(2).getValue().getHours() == 10);
        assert(t.getListeLivraison().get(2).getValue().getMinutes() == 9);
        assert(t.getListeTrajet().get(3).getListeSegment().get(0).getNom() == "ec");
    }

    @Test
    void algoAvecSmallMap() {
        XMLParser xml = XMLParser.getInstance();
        long t1 = currentTimeMillis();
        Plan smallPlan = xml.chargerCarte("src/main/resources/xmlmaps/smallMap.xml");
        long t2 = currentTimeMillis();
        System.err.println("Temps de chargement de la smallMap : "+(t2-t1)+"ms");
        List<Intersection> listeIntersections = smallPlan.getListeIntersection();
        Livreur livreur = new Livreur("John");
        LinkedList<Livraison> listeLivraisons = new LinkedList<Livraison>();
        Intersection i1 = listeIntersections.get(0);
        Intersection i2 = listeIntersections.get(0);
        for(Intersection in : listeIntersections){
            if(in.getId()==25175791){
                i1 = in;
            }
            if(in.getId()==25175778){
//                i2 = in;

            }
        }

        Livraison l1 = new Livraison(i1,8);
        Livraison l2 = new Livraison(i2,9);

        l1.setPlan(smallPlan);
        l2.setPlan(smallPlan);
        l1.setStringProperty(l1.getAdresse(), 8, livreur);
        l2.setStringProperty(l2.getAdresse(), 9, livreur);
        l1.setLivreur(livreur);
        l2.setLivreur(livreur);

        listeLivraisons.add(l1);
        listeLivraisons.add(l2);


        t1 = currentTimeMillis();
        Tournee t = new Tournee(smallPlan,listeLivraisons,livreur);
        t2 = currentTimeMillis();
        System.err.println("Temps de calcul : "+(t2-t1)+"ms");

        List<Trajet> trajets = t.getListeTrajet();
        Trajet trajet1 = trajets.get(1);
        List<Segment> lSeg = trajet1.getListeSegment();


        Pair<Livraison, Date> dernLiv = t.getListeLivraison().get(t.getListeLivraison().size()-1);
        assertEquals(0, dernLiv.getValue().getMinutes());
        assertEquals(4.873133, dernLiv.getKey().getPointLivraison().getLon());

    }@Test
    void algoAvecMediumMap() {
        XMLParser xml = XMLParser.getInstance();
        long t1 = currentTimeMillis();
        Plan mediumPlan = xml.chargerCarte("src/main/resources/xmlmaps/mediumMap.xml");
        long t2 = currentTimeMillis();
        System.err.println("Temps de chargement de la mediumMap : "+(t2-t1)+"ms");
        List<Intersection> listeIntersections = mediumPlan.getListeIntersection();

        LinkedList<Livraison> listeLivraisons = new LinkedList<Livraison>();

        Livreur livreur = new Livreur("John");

        Livraison l1 = new Livraison(listeIntersections.get(5),8);
        Livraison l2 = new Livraison(listeIntersections.get(203),8);

        l1.setPlan(mediumPlan);
        l2.setPlan(mediumPlan);
        l1.setStringProperty(l1.getAdresse(), 8, livreur);
        l2.setStringProperty(l2.getAdresse(), 8, livreur);
        l1.setLivreur(livreur);
        l2.setLivreur(livreur);

        listeLivraisons.add(l1);
        listeLivraisons.add(l2);



        t1 = currentTimeMillis();
        Tournee t = new Tournee(mediumPlan,listeLivraisons,livreur);
        t2 = currentTimeMillis();
        System.err.println("Temps de calcul : "+(t2-t1)+"ms");


        Pair<Livraison, Date> dernLiv = t.getListeLivraison().get(t.getListeLivraison().size()-1);
        System.err.println(dernLiv.getValue().getMinutes());
        assertEquals(17, dernLiv.getValue().getMinutes());
        assertEquals(4.863642, dernLiv.getKey().getPointLivraison().getLon());

    }@Test
    void algoAvecLargeMap() {
        XMLParser xml = XMLParser.getInstance();
        long t1 = currentTimeMillis();
        Plan smallPlan = xml.chargerCarte("src/main/resources/xmlmaps/largeMap.xml");
        long t2 = currentTimeMillis();
        System.err.println("Temps de chargement de la largeMap : "+(t2-t1)+"ms");
        List<Intersection> listeIntersections = smallPlan.getListeIntersection();

        LinkedList<Livraison> listeLivraisons = new LinkedList<Livraison>();
        Livreur livreur = new Livreur("John");

        Livraison l1 = new Livraison(listeIntersections.get(5),8);
        Livraison l2 = new Livraison(listeIntersections.get(203),8);
        Livraison l3 = new Livraison(listeIntersections.get(204),8);
        Livraison l4 = new Livraison(listeIntersections.get(205),8);

        l1.setPlan(smallPlan);
        l1.setStringProperty(l1.getAdresse(), 8,livreur);
        l1.setLivreur(livreur);
        l2.setPlan(smallPlan);
        l2.setStringProperty(l2.getAdresse(), 8,livreur);
        l2.setLivreur(livreur);
        l3.setPlan(smallPlan);
        l3.setStringProperty(l3.getAdresse(), 8,livreur);
        l3.setLivreur(livreur);
        l4.setPlan(smallPlan);
        l4.setStringProperty(l4.getAdresse(), 8,livreur);
        l4.setLivreur(livreur);

        listeLivraisons.add(l1);
        listeLivraisons.add(l2);
        listeLivraisons.add(l3);
        listeLivraisons.add(l4);



        t1 = currentTimeMillis();
        Tournee t = new Tournee(smallPlan,listeLivraisons,livreur);
        t2 = currentTimeMillis();
        System.err.println("Temps de calcul : "+(t2-t1)+"ms");

        Pair<Livraison, Date> dernLiv = t.getListeLivraison().get(t.getListeLivraison().size()-1);
        assertEquals(5,dernLiv.getValue().getMinutes());
        assertEquals(4.8454523,dernLiv.getKey().getPointLivraison().getLon());
    }

    @Test
    void ajouterLivraison() {
        XMLParser xml = XMLParser.getInstance();
        long t1 = currentTimeMillis();
        Plan largePlan = xml.chargerCarte("src/main/resources/xmlmaps/largeMap.xml");
        long t2 = currentTimeMillis();
        System.err.println("Temps de chargement de la largeMap : "+(t2-t1)+"ms");
        List<Intersection> listeIntersections = largePlan.getListeIntersection();

        LinkedList<Livraison> listeLivraisons = new LinkedList<Livraison>();

        Livreur livreur = new Livreur("John");

        Livraison l1 = new Livraison(listeIntersections.get(5),8);
        Livraison l2 = new Livraison(listeIntersections.get(208),8);
        Livraison l3 = new Livraison(listeIntersections.get(210),8);
        Livraison lAjout = new Livraison(listeIntersections.get(224),8);

        listeLivraisons.add(l1);
        listeLivraisons.add(l2);
        listeLivraisons.add(l3);

        l1.setPlan(largePlan);
        l2.setPlan(largePlan);
        l3.setPlan(largePlan);
        l1.setStringProperty(l1.getAdresse(), 8, livreur);
        l2.setStringProperty(l2.getAdresse(), 8, livreur);
        l3.setStringProperty(l3.getAdresse(), 8, livreur);
        l1.setLivreur(livreur);
        l2.setLivreur(livreur);
        l3.setLivreur(livreur);

        t1 = currentTimeMillis();
        Tournee t = new Tournee(largePlan,listeLivraisons,livreur);
        t2 = currentTimeMillis();
        System.err.println("Temps de calcul : "+(t2-t1)+"ms");

        List<Pair<Livraison, Date>> listeLiv = t.getListeLivraison();
        for(Pair<Livraison,Date> p : listeLiv){
            System.err.println("POINT DE LIVRAISON : "+listeIntersections.indexOf(p.getKey().getPointLivraison()));
            System.err.println("   HORAIRE : "+p.getValue().getHours()+":"+p.getValue().getMinutes());
        }
        System.err.println("------------------AJOUT-------------");

        t.ajouterLivraison(largePlan, lAjout, l2);

        listeLiv = t.getListeLivraison();
        for(Pair<Livraison,Date> p : listeLiv){
            System.err.println("POINT DE LIVRAISON : "+listeIntersections.indexOf(p.getKey().getPointLivraison()));
            System.err.println("   HORAIRE : "+p.getValue().getHours()+":"+p.getValue().getMinutes());
        }


        for(Trajet traj : t.getListeTrajet()){
            for(Segment seg : traj.getListeSegment()){
                System.err.print(seg.getNom());
            }
            System.err.println();
        }
        assertEquals(224, largePlan.getListeIntersection().indexOf(t.getListeLivraison().get(1).getKey().getPointLivraison()));
        assertEquals(39, t.getListeLivraison().get(1).getValue().getMinutes());
        assertEquals(53, t.getListeLivraison().get(2).getValue().getMinutes());
        assertEquals(11, t.getListeLivraison().get(3).getValue().getMinutes());
    }


    @Test
    void testSuppressionLivraisonHorraireQuiSeSuivent(){
        XMLParser xml = XMLParser.getInstance();
        long t1 = currentTimeMillis();
        Plan largePlan = xml.chargerCarte("src/main/resources/xmlmaps/largeMap.xml");
        long t2 = currentTimeMillis();
        System.err.println("Temps de chargement de la largeMap : "+(t2-t1)+"ms");
        List<Intersection> listeIntersections = largePlan.getListeIntersection();

//        LinkedList<Livraison> listesLivraisonsVeritables = new LinkedList<Livraison>();
        LinkedList<Livraison> listeLivraisons = new LinkedList<Livraison>();

        Livreur livreur = new Livreur("John");

        Livraison l1 = new Livraison(listeIntersections.get(5),8);
        Livraison l2 = new Livraison(listeIntersections.get(208),8);
        Livraison l3 = new Livraison(listeIntersections.get(210),8);
        Livraison l4 = new Livraison(listeIntersections.get(224),8);

        l1.setPlan(largePlan);
        l2.setPlan(largePlan);
        l3.setPlan(largePlan);
        l4.setPlan(largePlan);
        l1.setStringProperty(l1.getAdresse(), 8, livreur);
        l2.setStringProperty(l2.getAdresse(), 8, livreur);
        l3.setStringProperty(l3.getAdresse(), 8, livreur);
        l4.setStringProperty(l4.getAdresse(), 8, livreur);
        l1.setLivreur(livreur);
        l2.setLivreur(livreur);
        l3.setLivreur(livreur);
        l4.setLivreur(livreur);

        listeLivraisons.add(l1);
        listeLivraisons.add(l2);
        listeLivraisons.add(l3);
        listeLivraisons.add(l4);

//        listesLivraisonsVeritables.add(l1);
//        listesLivraisonsVeritables.add(l2);
//        listesLivraisonsVeritables.add(l3);

        t1 = currentTimeMillis();
        Tournee t = new Tournee(largePlan,listeLivraisons,livreur);
//        Tournee tt = new Tournee(largePlan,listesLivraisonsVeritables,livreur);
        t2 = currentTimeMillis();
        System.err.println("Temps de calcul : "+(t2-t1)+"ms");

        List<Pair<Livraison, Date>> listeLiv = t.getListeLivraison();
        for(Pair<Livraison,Date> p : listeLiv){
            System.err.println("POINT DE LIVRAISON : "+listeIntersections.indexOf(p.getKey().getPointLivraison()));
            System.err.println("   HORAIRE : "+p.getValue().getHours()+":"+p.getValue().getMinutes());
        }
        System.err.println("------------------SUPPRESSION-------------");

        t.supprimerLivraison(largePlan, l4);  // supprimer livraison quelconque
//        t.supprimerLivraison(largePlan, l3); // supprimer premiere quelconque
//        t.supprimerLivraison(largePlan, l2); // supprimer derniere quelconque

        listeLiv = t.getListeLivraison();
        for(Pair<Livraison,Date> p : listeLiv){
            System.err.println("POINT DE LIVRAISON : "+listeIntersections.indexOf(p.getKey().getPointLivraison()));
            System.err.println("   HORAIRE : "+p.getValue().getHours()+":"+p.getValue().getMinutes());
        }

        for(Trajet traj : t.getListeTrajet()){
            for(Segment seg : traj.getListeSegment()){
                System.err.print(seg.getNom());
            }
            System.err.println();
        }

        assertEquals(5, largePlan.getListeIntersection().indexOf(t.getListeLivraison().get(1).getKey().getPointLivraison()));
        assertEquals(52, t.getListeLivraison().get(t.getListeLivraison().size()-1).getValue().getMinutes());


    }

    @Test
    void testSuppressionLivraisonHorraireQuiNeSeSuiventPas(){
        XMLParser xml = XMLParser.getInstance();
        long t1 = currentTimeMillis();
        Plan largePlan = xml.chargerCarte("src/main/resources/xmlmaps/largeMap.xml");
        long t2 = currentTimeMillis();
        System.err.println("Temps de chargement de la largeMap : "+(t2-t1)+"ms");
        List<Intersection> listeIntersections = largePlan.getListeIntersection();

//        LinkedList<Livraison> listesLivraisonsVeritables = new LinkedList<Livraison>();
        LinkedList<Livraison> listeLivraisons = new LinkedList<Livraison>();

        Livreur livreur = new Livreur("John");

        Livraison l1 = new Livraison(listeIntersections.get(5),8);
        Livraison l2 = new Livraison(listeIntersections.get(208),9);
        Livraison l3 = new Livraison(listeIntersections.get(210),8);
        Livraison l4 = new Livraison(listeIntersections.get(224),8);

        l1.setPlan(largePlan);
        l2.setPlan(largePlan);
        l3.setPlan(largePlan);
        l4.setPlan(largePlan);
        l1.setStringProperty(l1.getAdresse(), 8, livreur);
        l2.setStringProperty(l2.getAdresse(), 9, livreur);
        l3.setStringProperty(l3.getAdresse(), 8, livreur);
        l4.setStringProperty(l4.getAdresse(), 8, livreur);
        l1.setLivreur(livreur);
        l2.setLivreur(livreur);
        l3.setLivreur(livreur);
        l4.setLivreur(livreur);

        listeLivraisons.add(l1);
        listeLivraisons.add(l2);
        listeLivraisons.add(l3);
        listeLivraisons.add(l4);


        t1 = currentTimeMillis();
        Tournee t = new Tournee(largePlan,listeLivraisons,livreur);
        t2 = currentTimeMillis();
        System.err.println("Temps de calcul : "+(t2-t1)+"ms");

        List<Pair<Livraison, Date>> listeLiv = t.getListeLivraison();
        for(Pair<Livraison,Date> p : listeLiv){
            System.err.println("POINT DE LIVRAISON : "+listeIntersections.indexOf(p.getKey().getPointLivraison()));
            System.err.println("   HORAIRE : "+p.getValue().getHours()+":"+p.getValue().getMinutes());
        }
        System.err.println("------------------SUPPRESSION-------------");

        t.supprimerLivraison(largePlan, l4);

        listeLiv = t.getListeLivraison();
        for(Pair<Livraison,Date> p : listeLiv){
            System.err.println("POINT DE LIVRAISON : "+listeIntersections.indexOf(p.getKey().getPointLivraison()));
            System.err.println("   HORAIRE : "+p.getValue().getHours()+":"+p.getValue().getMinutes());
        }

        for(Trajet traj : t.getListeTrajet()){
            for(Segment seg : traj.getListeSegment()){
                System.err.print(seg.getNom());
            }
            System.err.println();
        }
        // Les asserts /!\ pour verifier que les tests passent tous


    }

    @Test
    void testCalculLivraisonsNonValides(){
        XMLParser xml = XMLParser.getInstance();
        long t1 = currentTimeMillis();
        Plan largePlan = xml.chargerCarte("src/main/resources/xmlmaps/largeMap.xml");
        long t2 = currentTimeMillis();
        List<Intersection> listeIntersections = largePlan.getListeIntersection();
        LinkedList<Livraison> listeLivraisons = new LinkedList<Livraison>();
        Livreur livreur = new Livreur("John");
        Livraison l1 = new Livraison(listeIntersections.get(5),8);
        Livraison l2 = new Livraison(listeIntersections.get(208),9);
        Livraison l3 = new Livraison(listeIntersections.get(210),8);
        Livraison l4 = new Livraison(listeIntersections.get(224),8);

        l1.setPlan(largePlan);
        l2.setPlan(largePlan);
        l3.setPlan(largePlan);
        l4.setPlan(largePlan);
        l1.setStringProperty(l1.getAdresse(), 8, livreur);
        l2.setStringProperty(l2.getAdresse(), 9, livreur);
        l3.setStringProperty(l3.getAdresse(), 8, livreur);
        l4.setStringProperty(l4.getAdresse(), 8, livreur);
        l1.setLivreur(livreur);
        l2.setLivreur(livreur);
        l3.setLivreur(livreur);
        l4.setLivreur(livreur);

        listeLivraisons.add(l1);
        listeLivraisons.add(l2);
        listeLivraisons.add(l3);
        listeLivraisons.add(l4);
        Tournee t = new Tournee(largePlan,listeLivraisons,livreur);

        List<Livraison> livraisonsNonValides = t.calculLivraisonsNonValides();
        System.err.println("Assert: |null|"+livraisonsNonValides+"|");
        assertEquals(livraisonsNonValides.size() , 0);


        LinkedList<Livraison> listeLivraisons2 = new LinkedList<Livraison>();
        Livraison l21 = new Livraison(listeIntersections.get(5),8);
        System.err.println("test: "+ l21);
        listeLivraisons2.add(new Livraison(largePlan, listeIntersections.get(6),8, livreur, null)); //5-10?
        listeLivraisons2.add(new Livraison(largePlan, listeIntersections.get(7),8, livreur, null)); //5-10?
        listeLivraisons2.add(new Livraison(largePlan, listeIntersections.get(8),8, livreur, null)); //5-10?
        listeLivraisons2.add(new Livraison(largePlan, listeIntersections.get(9),8, livreur, null)); //5-10?
        listeLivraisons2.add(new Livraison(largePlan, listeIntersections.get(10),8, livreur, null)); //5-10?
        listeLivraisons2.add(new Livraison(largePlan, listeIntersections.get(11),8, livreur, null)); //5-10?
        listeLivraisons2.add(new Livraison(largePlan, listeIntersections.get(12),8, livreur, null)); //5-10?


        t = new Tournee(largePlan,listeLivraisons2,livreur);

        livraisonsNonValides = t.calculLivraisonsNonValides();
        System.err.println("Assert: |null|"+livraisonsNonValides+"|");
        assertEquals(3, livraisonsNonValides.size());

    }

}