package fr.deliverapp.model.objects;

import java.util.Collection;
import java.util.Iterator;

/**
 * Première version du TSP
 */
public class TSP1 extends TemplateTSP {
    @Override
    protected int bound(Integer currentVertex, Collection<Integer> unvisited) {
        return 0;
    }

    @Override
    protected Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graph g) {
        return new SeqIter(unvisited, currentVertex, g);
    }

}
