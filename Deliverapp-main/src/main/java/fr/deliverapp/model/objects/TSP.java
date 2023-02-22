package fr.deliverapp.model.objects;

/**
 * Interface pour la résolution du TSP
 */
public interface TSP {
    /**
     * Recherche du circuit hamiltonien au cout minimal <code>g</code> en maximum <code>timeLimit</code> millisecondes
     * (retourne le meilleur trajet avant la fin du temps)
     * Attention : le trajet commence à l'intersection 0
     * @param timeLimit : Limite de temps de calcul
     * @param g : Le graph à calculer
     */
    public void searchSolution(int timeLimit, Graph g);

    /**
     * @param i : numero de vertex
     * @return La ième vertex par laquelle on passe
     * (-1 si <code>searcheSolution</code> n'a pas encore été appelé, ou si i < 0 ou si i >= g.getNbSommets())
     */
    public Integer getSolution(int i);

    /**
     * @return le cout total de la solution <code>searchSolution</code>
     * (-1 si <code>searcheSolution</code> n'a pas encore été appelé).
     */
    public int getSolutionCost();

}
