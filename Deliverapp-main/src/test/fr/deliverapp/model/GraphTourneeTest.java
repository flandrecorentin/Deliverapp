package fr.deliverapp.model;
import fr.deliverapp.model.objects.*;

import fr.deliverapp.model.objects.GraphTournee;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

class GraphTourneeTest {

    @Test
    void getCost() {
        Intersection a = new Intersection(12,1.1,0.1);
        Intersection b = new Intersection(23,2.1,0.1);
        Intersection c = new Intersection(34,3.1,0.1);
        Intersection d = new Intersection(45,4.1,0.1);
        Intersection e = new Intersection(56,5.1,0.1);
        HashMap<Long, Intersection> mapInter = new HashMap<Long, Intersection>();
        mapInter.put(Long.valueOf(100),a);
        mapInter.put(Long.valueOf(101),b);
        mapInter.put(Long.valueOf(102),c);
        mapInter.put(Long.valueOf(103),d);
        mapInter.put(Long.valueOf(104),e);

        Segment ab = new Segment("ab", 3, a, b);
        Segment ae = new Segment("ae", 5, a, e);
        Segment bc = new Segment("bc", 6, b, c);
        Segment be = new Segment("be", 1, b, e);
        Segment cd = new Segment("cd", 2, c, d);
        Segment da = new Segment("da", 3, d, a);
        Segment dc = new Segment("dc", 7, d, c);
        Segment eb = new Segment("eb", 1, e, b);
        Segment ec = new Segment("ec", 3, e, c);
        Segment ed = new Segment("ed", 6, e, d);
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

        Plan plan =  new Plan(new Entrepot(a), listSeg, new ArrayList<>(mapInter.values()));
        Livreur livreur = new Livreur("John");

        Livraison l3 = new Livraison(d, 8);
        Livraison l4 = new Livraison(e, 10);
        Livraison l2 = new Livraison(c, 11);
        Livraison l1 = new Livraison(b, 10);
        l1.setPlan(plan);
        l2.setPlan(plan);
        l3.setPlan(plan);
        l4.setPlan(plan);
        l1.setStringProperty(l1.getAdresse(),10,livreur);
        l2.setStringProperty(l2.getAdresse(),11,livreur);
        l3.setStringProperty(l3.getAdresse(),8,livreur);
        l4.setStringProperty(l4.getAdresse(),10,livreur);
        l1.setLivreur(livreur);
        l2.setLivreur(livreur);
        l3.setLivreur(livreur);
        l4.setLivreur(livreur);

        LinkedList<Livraison> listeLivraisons = new LinkedList<Livraison>();
        listeLivraisons.add(l1);
        listeLivraisons.add(l2);
        listeLivraisons.add(l3);
        listeLivraisons.add(l4);

        List<Intersection> i = (List<Intersection>)plan.getListeIntersection();

        GraphTournee graph = new GraphTournee(plan, listeLivraisons,plan.getEntrepot());


 //ca c'est vraiment pas bien de commenter les asserts attention
        assert(graph.isArc(0,2) == false);
        assert(graph.isArc(0,3) == true);
        assert(graph.isArc(0,4) == false);
        assert(graph.isArc(1,0) == false);
        assert(graph.isArc(1,2) == true);
        assert(graph.isArc(1,3) == false);
        assert(graph.isArc(1,4) == true);
        assert(graph.isArc(2,0) == true);
        assert(graph.isArc(2,1) == false);
        assert(graph.isArc(2,3) == false);
        assert(graph.isArc(2,5) == false);
        assert(graph.isArc(3,0) == false);
        assert(graph.isArc(3,1) == true);
        assert(graph.isArc(3,2) == false);
        assert(graph.isArc(3,4) == true);
        assert(graph.isArc(4,0) == false);
        assert(graph.isArc(4,1) == true);
        assert(graph.isArc(4,2) == true);
        assert(graph.isArc(4,3) == false);


    }
}