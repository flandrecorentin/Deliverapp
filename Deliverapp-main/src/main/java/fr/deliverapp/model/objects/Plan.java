package fr.deliverapp.model.objects;

import fr.deliverapp.model.package_diagram.Observable;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Représente le Plan crée à partir d'un fichier XML représentant une carte
 * Contient la liste des intersections, des segments ainsi que l'entrepot
 *
 * @author Hexanome H4124
 */
public class Plan extends Observable {

    /**
     * La liste des segments praticables sur le plan
     */
    private List<Segment> listeSegment;

    /**
     * La liste des points de livraison possibles sur le plan
     */
    private List<Intersection> listeIntersection;

    /**
     * L'entrepot du Plan, point de départ de toute Tournée
     */
    private Entrepot entrepot;

    /**
     * Matrice des coûts des segments
     */
    private double[][] coutsSegments;


    /*public Plan(Entrepot entrepot,List<Segment> listeSegment, HashMap<Long, Intersection> intersections) {
        this.entrepot=entrepot;

        this.listeSegment = listeSegment;
        this.mapIntersection = intersections;
        coutsSegments = new double[intersections.size()][intersections.size()];
        for(Segment s : listeSegment){
            //coutsSegments[s.getOrigine().getIdCalcul()][s.getDestination().getIdCalcul()] = s.getDistance();
            coutsSegments[s.getOrigine().getIdCalcul()][s.getDestination().getIdCalcul()] = s.getDistance();
        }
    }
    public Plan(Entrepot entrepot,List<Segment> listeSegment, List<Intersection> intersections) {
        this.entrepot=entrepot;

        this.listeSegment = listeSegment;
        for(Intersection i : intersections){
            System.err.println(i.getId()+" "+i.getIdCalcul());
            this.mapIntersection.put(i.getId(), i);
        }
        coutsSegments = new double[intersections.size()][intersections.size()];
        for(Segment s : listeSegment){
            coutsSegments[s.getOrigine().getIdCalcul()][s.getDestination().getIdCalcul()] = s.getDistance();
        }
    }*/

    /**
     * Constructeur du Plan
     *
     * @param entrepot      l'entrepot
     * @param listeSegment  la liste des segments
     * @param intersections la liste des intersections
     */
    public Plan(Entrepot entrepot, List<Segment> listeSegment, List<Intersection> intersections) {
        this.entrepot = entrepot;
        this.listeSegment = listeSegment;
        this.listeIntersection = intersections;
        coutsSegments = new double[intersections.size()][intersections.size()];
        for (Segment s : listeSegment) {
            coutsSegments[listeIntersection.indexOf(s.getOrigine())][listeIntersection.indexOf(s.getDestination())] = s.getDistance();
        }
    }

    /**
     * Constructeur vide de Plan
     */
    public Plan() {
    }

    /**
     * Renvoie la liste de segments du Plan
     *
     * @return <code>this.listeSegment</code>
     */
    public List<Segment> getListeSegment() {
        return listeSegment;
    }

    /**
     * Affecte la liste de segments à un Plan
     *
     * @param listeSegment la liste de segments
     */
    public void setListeSegment(List<Segment> listeSegment) {
        this.listeSegment = listeSegment;
    }

    /**
     * Renvoie la liste d'intersections du Plan
     *
     * @return <code>this.listeIntersection</code>
     */
    public List<Intersection> getListeIntersection() {
        return listeIntersection;
    }

    /**
     * Affecte une liste d'intersections à un plan
     *
     * @param listeIntersections la liste d'intersections
     */
    public void setListeIntersection(LinkedList<Intersection> listeIntersections) {
        this.listeIntersection = listeIntersection;
    }

    /**
     * Méthode qui calcule le plus court chemin entre un point de départ et
     * un point d'arrivée
     *
     * @param idOrigine le point de départ
     * @param idArrivee le point d'arrivée
     * @return le coût du chemin si celui-ci est possible; <code>Double
     * .MAX_VALUE</code>
     * sinon
     */
    public double calculPlusCourtChemin(int idOrigine, int idArrivee) {
        double[] d = new double[listeIntersection.size()];
        // int[] predecesseurs = new int[listeIntersection.size()];
        LinkedList<Integer> Q = new LinkedList<Integer>();
        Arrays.fill(d, Double.MAX_VALUE);

        d[idOrigine] = 0;
        Q.add(idOrigine);

        while (Q.size() != 0) {
            double minimum = Double.MAX_VALUE;
            int si = 0;
            for (Integer q : Q) {
                if (minimum > d[q]) {
                    minimum = d[q];
                    si = q;
                }
            }
            for (int sj = 0; sj < listeIntersection.size(); sj++) {
                if (estSuccesseur(si, sj)) {
                    if (d[sj] == Double.MAX_VALUE || Q.contains(sj)) {
                        d[sj] = Math.min(d[si] + coutsSegments[si][sj], d[sj]);
                        if (!Q.contains(sj)) {
                            Q.add(sj);
                        }
                    }
                }
            }
            Q.remove(Integer.valueOf(si));
            if (d[idArrivee] != Double.MAX_VALUE && !Q.contains(idArrivee)) {
                return d[idArrivee];
            }
        }
        System.err.println("Erreur : chemin non trouvé");
        return Double.MAX_VALUE;
    }

    /**
     * Méthode qui calcule le plus court chemin entre un point de départ et
     * un point d'arrivée en prenant en compte les prédécesseurs
     *
     * @param idOrigine le point de départ
     * @param idArrivee le point d'arrivée
     * @return le coût du chemin si celui-ci est possible; <code>null</code>
     * sinon
     */
    public int[] calculPlusCourtCheminAvecPredecesseurs(int idOrigine, int idArrivee) {
        double[] d = new double[listeIntersection.size()];
        int[] predecesseurs = new int[listeIntersection.size()];
        LinkedList<Integer> Q = new LinkedList<Integer>();
        Arrays.fill(d, Double.MAX_VALUE);

        d[idOrigine] = 0;
        Q.add(idOrigine);

        while (Q.size() != 0) {
            double minimum = Double.MAX_VALUE;
            int si = 0;
            for (Integer q : Q) {
                if (minimum > d[q]) {
                    minimum = d[q];
                    si = q;
                }
            }
            for (int sj = 0; sj < listeIntersection.size(); sj++) {
                if (estSuccesseur(si, sj)) {
                    if (d[sj] == Double.MAX_VALUE || Q.contains(sj)) {

                        if (d[si] + coutsSegments[si][sj] >= d[sj]) {

                        } else {
                            d[sj] = d[si] + coutsSegments[si][sj];
                            predecesseurs[sj] = si;
                        }
                        //d[sj]= Math.min(d[si]+coutsSegments[si][sj],d[sj]);

                        if ((!Q.contains(sj)) && (d[sj] < d[idArrivee])) {
                            //if( ( !Q.contains(sj) ) ){
                            Q.add(sj);
                        }
                    }
                }
            }
            Q.remove(Integer.valueOf(si));
            if (d[idArrivee] != Double.MAX_VALUE && !Q.contains(idArrivee)) {
                return predecesseurs;
            }
        }
        System.err.println("Erreur : chemin non trouvé");
        return null;
    }

    /**
     * Méthode qui renvoie si une intersection est successeur d'une autre
     *
     * @param si le point i
     * @param sj le point j
     * @return true si i est successeur de j
     */
    private boolean estSuccesseur(int si, int sj) {
        return coutsSegments[si][sj] > 0;
    }

    /**
     * Renvoie l'entrepot du Plan
     *
     * @return <code>this.entrepot</code>
     */
    public Entrepot getEntrepot() {
        return this.entrepot;
    }

    /**
     * Affecte l'entrepot au Plan
     *
     * @param entrepot l'entrepot
     */
    public void setEntrepot(Entrepot entrepot) {
        this.entrepot = entrepot;
    }

    /**
     * Cherche un segment qui permet d'aller d'un point d'origine à un point
     * de destination
     *
     * @param idIntersectionOrigine     l'intersection d'origine
     * @param idIntersectionDestination l'intersection de destination
     * @return le Segment si celui-ci existe; null sinon
     */
    public Segment trouveSegmentDansListeSegments(int idIntersectionOrigine, int idIntersectionDestination) {
        for (Segment s : this.listeSegment) {
            if (listeIntersection.indexOf(s.getOrigine()) == idIntersectionOrigine && listeIntersection.indexOf(s.getDestination()) == idIntersectionDestination) {
                return s;
            }
        }
        return null;
    }
}
