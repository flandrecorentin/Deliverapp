package fr.deliverapp.model;

import org.junit.jupiter.api.Test;
import fr.deliverapp.model.objects.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlanTest {

    @Test
    void calculPlusCourtChemin() {
        Intersection a = new Intersection(12,0.1,0.1);
        Intersection b = new Intersection(23,0.1,0.1);
        Intersection c = new Intersection(34,0.1,0.1);
        Intersection d = new Intersection(45,0.1,0.1);
        Intersection e = new Intersection(56,0.1,0.1);
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

        List<Intersection> i = (List<Intersection>)plan.getListeIntersection();
        assert(i.indexOf(a)==0);
        assert(i.indexOf(b)==1);
        assert(i.indexOf(c)==2);
        assert(i.indexOf(d)==3);
        assert(i.indexOf(e)==4);

        assert(plan.calculPlusCourtChemin(i.indexOf(a),i.indexOf(b))==3.0);
        assert(plan.calculPlusCourtChemin(i.indexOf(a),i.indexOf(c))==7.0);
        assert(plan.calculPlusCourtChemin(i.indexOf(a),i.indexOf(d))==9.0);
        assert(plan.calculPlusCourtChemin(i.indexOf(a),i.indexOf(e))==4.0);
    }

}