package fr.deliverapp.model.objects;

/**
 * Interface Graph
 *
 * @author Hexanome H4124
 */
public interface Graph {
    /**
     * @return le nombre de sommets de <code>this</code>
     */
    public abstract int getNbVertices();

    /**
     * Méthode qui retourne le cout d'un trajet de i vers j
     *
     * @param i le point de départ
     * @param j le point d'arrivée
     * @return le coût de (i,j) si (i,j) est un trajet possible; -1 sinon
     */
    public abstract int getCost(int i, int j);

    /**
     * Méthode qui permet de savoir si (i,j) est un trajet possible
     *
     * @param i le point de départ
     * @param j le point d'arrivée
     * @return true si <code>(i,j)</code> est un trajet possible; false sinon
     */
    public abstract boolean isArc(int i, int j);
}
