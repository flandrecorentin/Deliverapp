package fr.deliverapp.model.objects;

import fr.deliverapp.model.package_diagram.Observable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Représente le graphe de toutes les livraisons.
 * Il permet de calculer dans quel ordre il faut effectuer les livraisons.
 */
public class GraphTournee extends Observable implements Graph {

    private int nbVertices;
    private int[][] cost;

    /**
     * Constructeur du graphe permettant de calculer dans quel ordre effectuer
     * les livraisons.
     * Il calcule aussi le plus court chemin entre deux points.
     *
     * @param plan            La map chargée.
     * @param listeLivraisons La liste de livraisons dont il faut calculer l'ordre. L'intersection de chaque Livraison doit se trouver dans la liste de livraisons du parametre plan.
     * @param entrepot        L'entrepot duquel doit partir et revenir le livreur.
     */
    public GraphTournee(Plan plan, LinkedList<Livraison> listeLivraisons, Entrepot entrepot) {
        nbVertices = listeLivraisons.size() + 1;


        cost = new int[nbVertices][nbVertices];
        for (int i = 0; i < nbVertices; i++) {
            for (int j = 0; j < nbVertices; j++) {
                cost[i][j] = -1;
            }
        }

        LinkedList<Integer> horairesExistantes = new LinkedList<Integer>();
        for (Livraison l : listeLivraisons) {
            if (!horairesExistantes.contains(Integer.valueOf(l.getHoraire()))) {
                horairesExistantes.add(l.getHoraire());
            }
        }
        Collections.sort(horairesExistantes);

        List<Intersection> i = plan.getListeIntersection();

        for(Livraison livraison : listeLivraisons){
            for(Intersection intersection : i ){
                if (livraison.getPointLivraison().getLat()==intersection.getLat() && livraison.getPointLivraison().getLon()==intersection.getLon()) {
                    livraison.setPointLivraison(intersection);
                }
            }
        }

        int numeroEntrepot = i.indexOf(plan.getEntrepot().getLocalisation());
        System.out.println("--------------------------------------------------");
        System.out.println("COUT DES ARCS : ");
        for (Livraison l1 : listeLivraisons) {
            if (l1.getHoraire() == horairesExistantes.get(0)) {
                cost[0][listeLivraisons.indexOf(l1) + 1] = (int) plan.calculPlusCourtChemin(i.indexOf(entrepot.getLocalisation()), i.indexOf(l1.getPointLivraison()));
                System.out.println("1, " + (i.indexOf(l1.getPointLivraison()) + 1) + " : " + cost[0][listeLivraisons.indexOf(l1) + 1]);

            }

            for (Livraison l2 : listeLivraisons) {
                if (l2.getHoraire() == l1.getHoraire() || ((horairesExistantes.indexOf(l1.getHoraire()) != (horairesExistantes.size() - 1)) && (l2.getHoraire() == horairesExistantes.get(horairesExistantes.indexOf(l1.getHoraire()) + 1)))) {
                    if (l1 != l2) {
                        cost[listeLivraisons.indexOf(l1) + 1][listeLivraisons.indexOf(l2) + 1] = (int) plan.calculPlusCourtChemin(i.indexOf(l1.getPointLivraison()), i.indexOf(l2.getPointLivraison()));
                        System.out.println((i.indexOf(l1.getPointLivraison()) + 1) + ", " + (i.indexOf(l2.getPointLivraison()) + 1) + " : " + cost[listeLivraisons.indexOf(l1) + 1][listeLivraisons.indexOf(l2) + 1]);

                    }
                }
            }


            if (l1.getHoraire() == horairesExistantes.get(horairesExistantes.size() - 1)) {
                cost[listeLivraisons.indexOf(l1) + 1][0] = (int) plan.calculPlusCourtChemin(i.indexOf(l1.getPointLivraison()), i.indexOf(entrepot.getLocalisation()));
                System.out.println(+(i.indexOf(l1.getPointLivraison()) + 1) + ", 1" + " : " + cost[listeLivraisons.indexOf(l1) + 1][0]);

            }
        }
        System.out.println("--------------------------------------------------");
    }

    /**
     * @return
     */
    @Override
    public int getNbVertices() {
        return nbVertices;
    }

    /**
     * Donne la distance minimum d'un trajet entre deux intersections de livraisons.
     * Si le trajet ne se trouve pas dans le graphe, renvoie -1.
     *
     * @param i Numero de la livraison de départ du trajet dans la liste de livraisons donnée au constructeur de GraphTournée.
     * @param j Numero de la livraison d'arrivée du trajet.
     * @return La distance en mètre du trajet minimum entre les deux intersections de livraisons demandées.
     */
    @Override
    public int getCost(int i, int j) {
        if (i < 0 || i >= nbVertices || j < 0 || j >= nbVertices)
            return -1;
        return cost[i][j];
    }

    /**
     * Vérifie l'existance d'un arc dans le GraphTournée entre deux intersections de livraisons.
     *
     * @param i Numero de la livraison de départ du trajet dans la liste de livraisons donnée au constructeur de GraphTournée.
     * @param j Numero de la livraison d'arrivée du trajet.
     * @return True si l'arc existe, False sinon.
     */
    @Override
    public boolean isArc(int i, int j) {
        if (getCost(i, j) == -1) {
            return false;
        } else {
            return true;
        }
    }

}
