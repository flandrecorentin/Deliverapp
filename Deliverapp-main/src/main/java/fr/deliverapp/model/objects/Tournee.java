package fr.deliverapp.model.objects;

import fr.deliverapp.model.package_diagram.Observable;
import javafx.util.Pair;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Représente la Tournée calculée d'un livreur
 *
 * @author Hexanome H4124
 */
public class Tournee extends Observable {
    /**
     * Le livreur qui va effectuer cette Tournee
     */
    private Livreur livreur;

    /**
     * La liste des livraison de la tournée
     */
    private List<Pair<Livraison, Date>> listeLivraison;

    /**
     * La liste des trajets à suivre pour effectuer cette Tournée
     */
    private List<Trajet> listeTrajet;

    /**
     * Constructeur de Tournée à utiliser en cas de calcul de trajet déjà effectué
     *
     * @param livreur        Le livreur assigné à la Tournée.
     * @param listeLivraison La liste de Paires de <Livraison, Date> à effectuer, triés dans l'ordre.
     * @param listeTrajet    La liste de Trajet à effectuer par le livreur.
     */
    public Tournee(Livreur livreur, List<Pair<Livraison, Date>> listeLivraison, List<Trajet> listeTrajet) {
        this.livreur = livreur;
        this.listeLivraison = listeLivraison;
        this.listeTrajet = listeTrajet;
    }


    /**
     * Constructeur de Tournée qui calcule le bon ordre de livraisons à effectuer, leur horaire, et l'ensemble de segment à traverser pour y arriver.
     *
     * @param plan            La map chargée.
     * @param listeLivraisons Liste des livraisons dont doit être composée la Tournée. L'intersection de chaque Livraison doit se trouver dans la liste d'intersections du paramètre plan.
     * @param livreur         Livreur à qui on assigne la tournée.
     */
    public Tournee(Plan plan, LinkedList<Livraison> listeLivraisons, Livreur livreur) {
        //Initialisations
        this.listeTrajet = new LinkedList<Trajet>();
        this.livreur = livreur;
        this.listeLivraison = new LinkedList<Pair<Livraison, Date>>();

        //Calculs
        Graph g = new GraphTournee(plan, listeLivraisons, plan.getEntrepot());
        TSP tsp = new TSP1();
        tsp.searchSolution(30000, g);


        List<Intersection> listeInter = plan.getListeIntersection();
        List<Segment> listeSeg = plan.getListeSegment();


        double dernierTemps = 0.0;
        int dernierNumero = listeInter.indexOf(plan.getEntrepot().getLocalisation());
        int dernierNumeroPourGraph = -1;


        // Première itération : départ de l'entrepot
        int numero = (int) tsp.getSolution(1) - 1;
        Livraison liv = listeLivraisons.get(numero);

        double distance = g.getCost(dernierNumeroPourGraph + 1, numero + 1);
        System.err.println("dist entre entrepot et " + (numero + 1) + " : " + distance);
        double temps = distance / 15.0 / 1000.0;
        double tempsArrive = temps + dernierTemps;
        double tempsTotal = tempsArrive + 5.0 / 60.0;

        if (tempsTotal + 8.0 < liv.getHoraire()) {
            liv.setNbMinutesAttente((int)((liv.getHoraire()-(tempsTotal+8.0))*60));
            tempsTotal = liv.getHoraire() - 8.0;
        }

        dernierTemps = tempsTotal;
        int heure = (int) tempsTotal;
        int minutes = (int) ((tempsTotal - (int) tempsTotal) * 60);
        Date horairePassage = new Date();
        horairePassage.setHours(8 + heure);
        horairePassage.setMinutes(minutes);
        horairePassage.setSeconds(0);

        System.err.println("Temps depuis départ  " + heure + "h : " + minutes);

        Pair<Livraison, Date> pair = new Pair<>(liv, horairePassage);

        this.listeLivraison.add(pair);


        LinkedList<Segment> listeSegTrajet = new LinkedList<>();
        int numeroInter = listeInter.indexOf(listeLivraisons.get(numero).getPointLivraison());
        int dernierNumeroInter = listeInter.indexOf(plan.getEntrepot().getLocalisation());
        int[] pred = plan.calculPlusCourtCheminAvecPredecesseurs(dernierNumeroInter, numeroInter);
        //System.err.println("Calcul : "+(dernierNumeroInter)+" "+(numeroInter));
        int arrivee = numeroInter;
        int depart = numeroInter;
        LinkedList<Integer> listeInt = new LinkedList<Integer>();
        listeInt.add(arrivee);
        while (depart != dernierNumeroInter) {
            depart = pred[arrivee];
            listeInt.add(0, depart);
            arrivee = depart;
        }
        for (int j = 0; j < listeInt.size() - 1; j++) {
            for (Segment s : listeSeg) {
                if (listeInter.indexOf(s.getOrigine()) == listeInt.get(j) && listeInter.indexOf(s.getDestination()) == listeInt.get(j + 1)) {
                    listeSegTrajet.add(s);
                }
            }
        }
        System.err.print("Rues parcourues : ");
        for (Segment s : listeSegTrajet) {
            System.err.print(s.getNom() + " ");
        }
        System.err.println();
        System.err.println();

        Trajet t = new Trajet(listeSegTrajet);
        this.listeTrajet.add(t);


        dernierNumero = numero;
        dernierNumeroPourGraph = numero;


        //Itérations intermédiaires : entre les livraisons
        for (int i = 2; i < listeLivraisons.size() + 1; i++) {

            numero = (int) tsp.getSolution(i) - 1;
            liv = listeLivraisons.get(numero);

            distance = g.getCost(dernierNumeroPourGraph + 1, numero + 1);
            System.err.println("dist entre " + (dernierNumero + 1) + " et " + (numero + 1) + " : " + distance);
            temps = distance / 15.0 / 1000.0;
            tempsArrive = temps + dernierTemps;
            tempsTotal = tempsArrive + 5.0 / 60.0;

            if (tempsTotal + 8.0 < liv.getHoraire()) {
                liv.setNbMinutesAttente((int)((liv.getHoraire()-(tempsTotal+8.0))*60));
                tempsTotal = liv.getHoraire() - 8.0;
            }

            dernierTemps = tempsTotal;
            heure = (int) tempsTotal;
            minutes = (int) ((tempsTotal - (int) tempsTotal) * 60);
            horairePassage = new Date();
            horairePassage.setHours(8 + heure);
            horairePassage.setMinutes(minutes);
            horairePassage.setSeconds(0);

            System.err.println("Temps depuis départ  " + heure + "h : " + minutes);

            pair = new Pair<>(liv, horairePassage);

            this.listeLivraison.add(pair);


            listeSegTrajet = new LinkedList<>();
            numeroInter = listeInter.indexOf(listeLivraisons.get(numero).getPointLivraison());
            dernierNumeroInter = listeInter.indexOf(listeLivraisons.get(dernierNumero).getPointLivraison());
            pred = plan.calculPlusCourtCheminAvecPredecesseurs(dernierNumeroInter, numeroInter);
            if(pred == null){
                this.listeLivraison=null;
                this.listeTrajet=null;
                this.livreur=null;
                return;
            }
            //System.err.println("Calcul : "+(dernierNumeroInter)+" "+(numeroInter));
            arrivee = numeroInter;
            depart = numeroInter;
            listeInt = new LinkedList<Integer>();
            listeInt.add(arrivee);
            while (depart != dernierNumeroInter) {
                depart = pred[arrivee];
                listeInt.add(0, depart);
                arrivee = depart;
            }
            for (int j = 0; j < listeInt.size() - 1; j++) {
                for (Segment s : listeSeg) {
                    if (listeInter.indexOf(s.getOrigine()) == listeInt.get(j) && listeInter.indexOf(s.getDestination()) == listeInt.get(j + 1)) {
                        listeSegTrajet.add(s);
                    }
                }
            }
            System.err.print("Rues parcourues : ");
            for (Segment s : listeSegTrajet) {
                System.err.print(s.getNom() + " ");
            }
            System.err.println();
            System.err.println();

            t = new Trajet(listeSegTrajet);
            this.listeTrajet.add(t);


            dernierNumero = numero;
            dernierNumeroPourGraph = numero;
        }

        //Itération finale : retour à l'entrepot

        listeSegTrajet = new LinkedList<>();
        numeroInter = listeInter.indexOf(plan.getEntrepot().getLocalisation());
        dernierNumeroInter = listeInter.indexOf(listeLivraisons.get(tsp.getSolution(listeLivraison.size()) - 1).getPointLivraison());
        pred = plan.calculPlusCourtCheminAvecPredecesseurs(dernierNumeroInter, numeroInter);
        System.err.println("Retour : " + dernierNumeroInter + " " + listeInter.indexOf(plan.getEntrepot().getLocalisation()));
        for (int j = 0; j < pred.length; j++) {
            //System.err.println(pred[j]);
        }
        arrivee = numeroInter;
        depart = arrivee;
        listeInt = new LinkedList<Integer>();
        listeInt.add(arrivee);
        while (depart != dernierNumeroInter) {
            depart = pred[arrivee];
            listeInt.add(0, depart);
            arrivee = depart;
        }

        for (int j = 0; j < listeInt.size() - 1; j++) {
            for (Segment s : listeSeg) {
                if (listeInter.indexOf(s.getOrigine()) == listeInt.get(j) && listeInter.indexOf(s.getDestination()) == listeInt.get(j + 1)) {
                    listeSegTrajet.add(s);
                }
            }
        }
        System.err.print("Rues parcourues : ");
        for (Segment s : listeSegTrajet) {
            System.err.print(s.getNom() + " ");
        }
        System.err.println();
        System.err.println();

        t = new Trajet(listeSegTrajet);
        this.listeTrajet.add(t);
    }

    /**
     * Renvoie la liste des trajets de la Tournée
     *
     * @return <code>this.listeTrajet</code>
     */
    public List<Trajet> getListeTrajet() {
        return listeTrajet;
    }

    /**
     * Modifie la liste des trajets d'une Tournee
     *
     * @param listeTrajet la liste des trajets
     */
    public void setListeTrajet(List<Trajet> listeTrajet) {
        this.listeTrajet = listeTrajet;
    }

    /**
     * Renvoie le livreur assignée à cette Tournee
     *
     * @return <code>this.livreur</code>
     */
    public Livreur getLivreur() {
        return livreur;
    }

    /**
     * Modifie le livreur qui va effectuer cette Tournee
     *
     * @param livreur le nouveau livreur a qui on assigne la Tournee
     */
    public void setLivreur(Livreur livreur) {
        this.livreur = livreur;
    }

    /**
     * Renvoie le la liste des livraison de cette Tournee
     *
     * @return <code>this.listeLivraison</code>
     */
    public List<Pair<Livraison, Date>> getListeLivraison() {
        return listeLivraison;
    }

    /**
     * Modifie la liste des livraisons pour une Tournee
     *
     * @param listeLivraison la nouvelle liste de livraison
     */
    public void setListeLivraison(List<Pair<Livraison, Date>> listeLivraison) {
        this.listeLivraison = listeLivraison;
    }

    /**
     * Supprime une livraison d'une tournée
     *
     * @param plan                le plan
     * @param livraisonASupprimer la livraison a supprimer de la Tournee
     */
    public void supprimerLivraison(Plan plan, Livraison livraisonASupprimer) {
        System.err.println("----- Début supprimerLivraison()");


        // récupération de la liste d'intersection du plan
        List<Intersection> intersectionsDuPlan = plan.getListeIntersection();



        //Test d'existence de livraisonASupprimer dans la tournee
        boolean precedenteExiste = false;
        int indexOfLivraisonASupprimer = -1;
        for (Pair<Livraison, Date> pair : listeLivraison) {
            if (pair.getKey() == livraisonASupprimer) {
                precedenteExiste = true;
                indexOfLivraisonASupprimer = listeLivraison.indexOf(pair);
            }
        }
        if (!precedenteExiste) {
            System.err.println("La livraison a supprimer sélectionnée n'existe pas dans la Tournée");
            return;
        }

        System.err.println("indexOfLivraisonASupprimer: " + indexOfLivraisonASupprimer);

        // trajetPrecedent -> trajetASupprimer -> trajetApres
        // Calcul du nouveau trajet: trajetPrecedent -> trajetApres

        // si ni première livraison et ni dernière livraison
        Trajet recalculTrajet = new Trajet();
        int[] idIntersectionsDuTrajet;
        double longueurRecalculTrajet;
            if (indexOfLivraisonASupprimer != 0 && indexOfLivraisonASupprimer != listeLivraison.size() - 1) {
                idIntersectionsDuTrajet = plan.calculPlusCourtCheminAvecPredecesseurs(intersectionsDuPlan.indexOf(listeLivraison.get(indexOfLivraisonASupprimer - 1).getKey().getPointLivraison()), intersectionsDuPlan.indexOf(listeLivraison.get(indexOfLivraisonASupprimer + 1).getKey().getPointLivraison()));
                longueurRecalculTrajet = plan.calculPlusCourtChemin(intersectionsDuPlan.indexOf(listeLivraison.get(indexOfLivraisonASupprimer - 1).getKey().getPointLivraison()), intersectionsDuPlan.indexOf(listeLivraison.get(indexOfLivraisonASupprimer + 1).getKey().getPointLivraison()));
                int idIntersection = intersectionsDuPlan.indexOf(listeLivraison.get(indexOfLivraisonASupprimer + 1).getKey().getPointLivraison());
                while (idIntersection != intersectionsDuPlan.indexOf(listeLivraison.get(indexOfLivraisonASupprimer - 1).getKey().getPointLivraison())) {
                    int idIntersectionPrecedente = idIntersectionsDuTrajet[idIntersection];
                    Segment segment = plan.trouveSegmentDansListeSegments(idIntersectionPrecedente, idIntersection);
                    recalculTrajet.ajouterSegmentAListeSegmentALaFin(segment);
                    idIntersection = idIntersectionPrecedente;
                }
            } else if (indexOfLivraisonASupprimer == 0) {
              /*  idIntersectionsDuTrajet = plan.calculPlusCourtCheminAvecPredecesseurs(intersectionsDuPlan.indexOf(plan.getEntrepot().getLocalisation()), intersectionsDuPlan.indexOf(listeLivraison.get(indexOfLivraisonASupprimer + 1).getKey().getPointLivraison()));
                longueurRecalculTrajet = plan.calculPlusCourtChemin(intersectionsDuPlan.indexOf(plan.getEntrepot().getLocalisation()), intersectionsDuPlan.indexOf(listeLivraison.get(indexOfLivraisonASupprimer + 1).getKey().getPointLivraison()));
                int idIntersection = intersectionsDuPlan.indexOf(listeLivraison.get(indexOfLivraisonASupprimer + 1).getKey().getPointLivraison());
                while (idIntersection != intersectionsDuPlan.indexOf(plan.getEntrepot().getLocalisation())) {
                    int idIntersectionPrecedente = idIntersectionsDuTrajet[idIntersection];
                    Segment segment = plan.trouveSegmentDansListeSegments(idIntersectionPrecedente, idIntersection);
                    recalculTrajet.ajouterSegmentAListeSegmentALaFin(segment);
                    idIntersection = idIntersectionPrecedente;
                } */
            } else if (indexOfLivraisonASupprimer == listeLivraison.size() - 1) {
                idIntersectionsDuTrajet = plan.calculPlusCourtCheminAvecPredecesseurs(intersectionsDuPlan.indexOf(listeLivraison.get(indexOfLivraisonASupprimer - 1).getKey().getPointLivraison()), intersectionsDuPlan.indexOf(plan.getEntrepot().getLocalisation()));
                longueurRecalculTrajet = plan.calculPlusCourtChemin(intersectionsDuPlan.indexOf(listeLivraison.get(indexOfLivraisonASupprimer - 1).getKey().getPointLivraison()), intersectionsDuPlan.indexOf(plan.getEntrepot().getLocalisation()));
                int idIntersection = intersectionsDuPlan.indexOf(plan.getEntrepot().getLocalisation());
                while (idIntersection != intersectionsDuPlan.indexOf(listeLivraison.get(indexOfLivraisonASupprimer - 1).getKey().getPointLivraison())) {
                    int idIntersectionPrecedente = idIntersectionsDuTrajet[idIntersection];
                    Segment segment = plan.trouveSegmentDansListeSegments(idIntersectionPrecedente, idIntersection);
                    recalculTrajet.ajouterSegmentAListeSegmentALaFin(segment);
                    idIntersection = idIntersectionPrecedente;
                }
            } else {
                System.err.println("Grosse erreur AIE AIE AIE si tu vois ca bon courage pour débug mon pote");
                return;
            }


            // enlever livraisonASupprimer de listeLivraison
            // enlever trajet: trajetPrecedent -> trajetASupprimer
            // enlever trajet: trajetASupprimer -> trajetApres
            // ajouter trajet: trajetPrecedent -> trajetApres
            this.listeLivraison.remove(indexOfLivraisonASupprimer);
            this.listeTrajet.remove(indexOfLivraisonASupprimer);
            //if (indexOfLivraisonASupprimer != listeLivraison.size())
            this.listeTrajet.remove(indexOfLivraisonASupprimer);
            this.listeTrajet.add(indexOfLivraisonASupprimer, recalculTrajet);


            // changement des horaires pour les horaires à partir de la livraison supprimée.
            for (Pair<Livraison, Date> pair : listeLivraison) {
                // si c'est une livraison qui a lieu après le changement et si ce n'est pas la dernière livraison
                int index = listeLivraison.indexOf(pair);
                System.err.println("index" + index);


                // si première livraison
                if (index == 0 && indexOfLivraisonASupprimer == 0) {
                    System.err.println("PREMIERE LIVRAISON");
                    double nouvelleDistance = plan.calculPlusCourtChemin(intersectionsDuPlan.indexOf(plan.getEntrepot().getLocalisation()), intersectionsDuPlan.indexOf(listeLivraison.get(index).getKey().getPointLivraison()));
                    double secondesARajouter = (nouvelleDistance * 3600.0) / 15000.0;
                    System.err.println("nouvelleDistance " + nouvelleDistance);
                    System.err.println("secondesARajouter " + secondesARajouter);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(new Date(pair.getValue().getYear(), pair.getValue().getMonth(), pair.getValue().getDay(), 8, 0, 0));
                    System.err.println(cal.getTime());
                    cal.add(Calendar.SECOND, (int) secondesARajouter + 60 * 5);
                    System.err.println(cal.getTime());
                    if (listeLivraison.get(index).getKey().getHoraire() <= cal.getTime().getHours()) {
                        listeLivraison.get(index).getValue().setTime(cal.getTime().getTime());
                    } else {
                        System.err.println("L'horaire ne colle pas");
                        Date nouvelleDate = new Date(pair.getValue().getYear(), pair.getValue().getMonth(), pair.getValue().getDay(), listeLivraison.get(index).getKey().getHoraire(), 0, 0);
                        listeLivraison.get(index).getValue().setTime(nouvelleDate.getTime());
                    }
                }


                if (index >= indexOfLivraisonASupprimer - 1 && index != listeLivraison.size() - 1) {
                    System.err.println("intersection actuelle " + intersectionsDuPlan.indexOf(listeLivraison.get(index).getKey().getPointLivraison()));
                    System.err.println("intersection prochaine " + intersectionsDuPlan.indexOf(listeLivraison.get(index + 1).getKey().getPointLivraison()));
                    double nouvelleDistance = plan.calculPlusCourtChemin(intersectionsDuPlan.indexOf(listeLivraison.get(index).getKey().getPointLivraison()), intersectionsDuPlan.indexOf(listeLivraison.get(index + 1).getKey().getPointLivraison()));
                    double secondesARajouter = (nouvelleDistance * 3600.0) / 15000.0;
                    System.err.println("nouvelleDistance " + nouvelleDistance);
                    System.err.println("secondesARajouter " + secondesARajouter);
                    System.err.println("Avant: " + listeLivraison.get(index + 1).getValue());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(pair.getValue());
                    System.err.println("Avant cal: " + cal.getTime());
                    cal.add(Calendar.SECOND, (int) secondesARajouter + 60 * 5);
                    System.err.println("Apres cal: " + cal.getTime());
                    // si l'horaire d'arrivé potentiel colle bien pour la nouvelle heure
                    if (listeLivraison.get(index + 1).getKey().getHoraire() <= cal.getTime().getHours()) {
                        listeLivraison.get(index + 1).getValue().setTime(cal.getTime().getTime());
                    }
                    // si l'horaire d'arrivée potentiel ne colle pas
                    else {
                        System.err.println("L'horaire ne colle pas");
                        Date nouvelleDate = new Date(pair.getValue().getYear(), pair.getValue().getMonth(), pair.getValue().getDay(), listeLivraison.get(index + 1).getKey().getHoraire(), 0, 0);
                        listeLivraison.get(index + 1).getValue().setTime(nouvelleDate.getTime());
                    }

                    System.err.println("Après: " + listeLivraison.get(index + 1).getValue());
                }

            }

        notifyObservers(livraisonASupprimer);
        System.err.println("----- Fin supprimerLivraison()");
    }


    /**
     * Effectue l'ajout d'une livraison après le calcul de la tournée, en recalculant les horaires des livraisons suivantes.
     *
     * @param plan                La map chargée.
     * @param livraisonAjoutee    La livraison à ajouter. Son intersection doit se trouver dans la liste d'intersection du paramètre plan.
     * @param livraisonPrecedente La livraison après laquelle on veut ajouter une livraison. Elle doit déjà se trouver dans la tournée.
     */
    public void ajouterLivraison(Plan plan, Livraison livraisonAjoutee, Livraison livraisonPrecedente) {

        //Test d'existence de livraisonPrecedente
        boolean precedenteExiste = false;
        for (Pair<Livraison, Date> pair : listeLivraison) {
            if (pair.getKey() == livraisonPrecedente) {
                precedenteExiste = true;
            }
        }
        if (!precedenteExiste) {
            System.err.println("La livraison précédente sélectionnée n'existe pas dans la Tournée");
            return;
        }

        int indexLivraisonPrecedenteTournee = 0;
        Pair<Livraison, Date> livraisonPrecedenteTournee = new Pair<>(livraisonPrecedente, new Date());
        for (Pair<Livraison, Date> pair : listeLivraison) {
            if (pair.getKey() == livraisonPrecedente) {
                indexLivraisonPrecedenteTournee = listeLivraison.indexOf(pair);
                livraisonPrecedenteTournee = pair;
            }
        }

        int numeroLivraisonPrecedente = plan.getListeIntersection().indexOf(livraisonPrecedente.getPointLivraison());
        System.err.println("numPrec : " + numeroLivraisonPrecedente);
        int numeroLivraisonAjoutee = plan.getListeIntersection().indexOf(livraisonAjoutee.getPointLivraison());
        System.err.println("numAj : " + numeroLivraisonAjoutee);

        boolean aSuivant = !(listeLivraison.size() == (indexLivraisonPrecedenteTournee + 1));

        List<Segment> segPlan = plan.getListeSegment();
        List<Intersection> interPlan = plan.getListeIntersection();

        double distance1 = plan.calculPlusCourtChemin(numeroLivraisonPrecedente, numeroLivraisonAjoutee);
        int[] pred1 = plan.calculPlusCourtCheminAvecPredecesseurs(numeroLivraisonPrecedente, numeroLivraisonAjoutee);


        int arrivee = numeroLivraisonAjoutee;
        int depart = numeroLivraisonAjoutee;
        LinkedList<Integer> chemin1 = new LinkedList<Integer>();
        chemin1.add(arrivee);
        while (depart != numeroLivraisonPrecedente) {
            depart = pred1[arrivee];
            chemin1.add(0, depart);
            arrivee = depart;
        }


        LinkedList<Intersection> cheminIntersections1 = new LinkedList<>();
        for (Integer i : chemin1) {
            cheminIntersections1.add(interPlan.get(i));
        }

        LinkedList<Segment> listeSeg1 = new LinkedList<>();
        System.err.println("TAILLES : " + chemin1.size());
        System.err.println("LONGUEUR : " + distance1);

        for (int j = 0; j < cheminIntersections1.size() - 1; j++) {
            for (Segment s : segPlan) {
                if (s.getOrigine() == cheminIntersections1.get(j) && s.getDestination() == cheminIntersections1.get(j + 1)) {
                    listeSeg1.add(s);
                }
            }
        }


        Trajet trajet1 = new Trajet(listeSeg1);

        listeTrajet.remove(indexLivraisonPrecedenteTournee+1);
        listeTrajet.add(indexLivraisonPrecedenteTournee+1, trajet1);

        double temps1 = distance1 / 15.0 / 1000.0;
        double tempsTotal1 = temps1 + 5.0 / 60.0;
        Date date1 = new Date();
        date1.setTime(livraisonPrecedenteTournee.getValue().getTime() + (long) (tempsTotal1 * 60 * 60 * 1000));

        System.err.println(" - dist "+distance1);
        System.err.println(" - temps "+temps1);
        System.err.println(" - date "+date1);

        if (date1.getHours() < livraisonAjoutee.getHoraire()) {
            livraisonAjoutee.setNbMinutesAttente(60-date1.getMinutes()+(60*(date1.getHours()-livraisonAjoutee.getHoraire())));
            date1.setHours(livraisonAjoutee.getHoraire());
            date1.setMinutes(0);
            date1.setSeconds(0);
        }

        listeLivraison.add(indexLivraisonPrecedenteTournee + 1, new Pair<Livraison, Date>(livraisonAjoutee, date1));

        if (aSuivant) {

            int numeroLivraisonSuivante = plan.getListeIntersection().indexOf(listeLivraison.get(indexLivraisonPrecedenteTournee+2).getKey().getPointLivraison());
            System.err.println("numSuiv : "+numeroLivraisonSuivante);

            double distance2 = plan.calculPlusCourtChemin(numeroLivraisonAjoutee, numeroLivraisonSuivante);
            int[] pred2 = plan.calculPlusCourtCheminAvecPredecesseurs(numeroLivraisonAjoutee, numeroLivraisonSuivante);

            arrivee = numeroLivraisonSuivante;
            depart = numeroLivraisonSuivante;
            LinkedList<Integer> chemin2 = new LinkedList<Integer>();
            chemin2.add(arrivee);
            while (depart != numeroLivraisonAjoutee) {
                depart = pred2[arrivee];
                chemin2.add(0, depart);
                arrivee = depart;
            }


            LinkedList<Intersection> cheminIntersections2 = new LinkedList<>();
            for (Integer i : chemin2) {
                cheminIntersections2.add(interPlan.get(i));
            }
            LinkedList<Segment> listeSeg2 = new LinkedList<>();
            for (int j = 0; j < cheminIntersections2.size() - 1; j++) {
                for (Segment s : segPlan) {
                    if (s.getOrigine() == cheminIntersections2.get(j) && s.getDestination() == cheminIntersections2.get(j + 1)) {
                        listeSeg2.add(s);
                    }
                }
            }

            System.err.println("FIN BORDEL");

            Trajet trajet2 = new Trajet(listeSeg2);

            listeTrajet.add(indexLivraisonPrecedenteTournee + 2, trajet2);

            double temps2 = distance2 / 15.0 / 1000.0;
            double tempsTotal2 = temps2 + 5.0 / 60.0;
            Date date2 = new Date();
            date2.setTime(date1.getTime() + (long) (tempsTotal2 * 60 * 60 * 1000));

            Livraison livraisonSuivante = listeLivraison.get(indexLivraisonPrecedenteTournee + 2).getKey();
            if (date2.getHours() < livraisonSuivante.getHoraire()) {
                livraisonSuivante.setNbMinutesAttente(60-date2.getMinutes()+(60*(date2.getHours()-livraisonSuivante.getHoraire())));
                date2.setHours(livraisonSuivante.getHoraire());
                date2.setMinutes(0);
                date2.setSeconds(0);
            }

            Livraison lTemp = listeLivraison.get(indexLivraisonPrecedenteTournee + 2).getKey();
            listeLivraison.remove(indexLivraisonPrecedenteTournee + 2);
            listeLivraison.add(indexLivraisonPrecedenteTournee + 2, new Pair<>(lTemp, date2));

            if (listeLivraison.size() == indexLivraisonPrecedenteTournee + 3) {
                return;
            }

            int taille = listeLivraison.size();


            for (int i = indexLivraisonPrecedenteTournee + 3; i < taille; i++) {
                Livraison l = listeLivraison.get(i).getKey();
                System.err.println("VALEUR LIVRAISON L : " + l.getPointLivraison().getId());
                System.err.println("NUM L : " + interPlan.indexOf(l.getPointLivraison()));
                Livraison lPrec = listeLivraison.get(i - 1).getKey();
                System.err.println("VALEUR LIVRAISON LPREC : " + lPrec.getPointLivraison().getId());
                System.err.println("NUM LPREC : " + interPlan.indexOf(lPrec.getPointLivraison()));
                Date dPrec = listeLivraison.get(i - 1).getValue();
                double distance = plan.calculPlusCourtChemin(interPlan.indexOf(l.getPointLivraison()), interPlan.indexOf(lPrec.getPointLivraison()));

                double temps = distance / 15.0 / 1000.0;
                double tempsTotal = temps + 5.0 / 60.0;
                Date date = new Date();
                date.setTime(dPrec.getTime() + (long) (tempsTotal * 60 * 60 * 1000));

                if (date.getHours() < l.getHoraire()) {
                    l.setNbMinutesAttente(60-date.getMinutes()+(60*(date.getHours()-1-l.getHoraire())));
                    date.setHours(l.getHoraire());
                    date.setMinutes(0);
                    date.setSeconds(0);
                }

                listeLivraison.remove(i);
                listeLivraison.add(i, new Pair<>(l, date));
            }
        }else{
            int numeroEntrepot = plan.getListeIntersection().indexOf(plan.getEntrepot().getLocalisation());
            System.err.println("numSuiv : "+numeroEntrepot);

            double distance2 = plan.calculPlusCourtChemin(numeroLivraisonAjoutee, numeroEntrepot);
            int[] pred2 = plan.calculPlusCourtCheminAvecPredecesseurs(numeroLivraisonAjoutee, numeroEntrepot);

            arrivee = numeroEntrepot;
            depart = numeroEntrepot;
            LinkedList<Integer> chemin2 = new LinkedList<Integer>();
            chemin2.add(arrivee);
            while(depart != numeroLivraisonAjoutee){
                depart = pred2[arrivee];
                chemin2.add(0,depart);
                arrivee = depart;
            }


            LinkedList<Intersection> cheminIntersections2 = new LinkedList<>();
            for (Integer i : chemin2) {
                cheminIntersections2.add(interPlan.get(i));
            }
            LinkedList<Segment> listeSeg2 = new LinkedList<>();
            for (int j = 0; j < cheminIntersections2.size() - 1; j++) {
                for (Segment s : segPlan) {
                    if (s.getOrigine() == cheminIntersections2.get(j) && s.getDestination() == cheminIntersections2.get(j + 1)) {
                        listeSeg2.add(s);
                    }
                }
            }

            System.err.println("FIN BORDEL");

            Trajet trajet2 = new Trajet(listeSeg2);

            listeTrajet.add(indexLivraisonPrecedenteTournee + 2, trajet2);

        }
        notifyObservers(livraisonAjoutee);
    }


     /**
     * Permet de trouver quelles livraisons ne pourront pas être effectuées en temps et en heure.
     *
     * @return La liste de livraisons de la tournée qui ne sont pas valides.
     */
    public List<Livraison> calculLivraisonsNonValides(){
        List livraisonsNonValides = new LinkedList();

        for(Pair<Livraison,Date> pair : listeLivraison){
            Livraison l = pair.getKey();
            Date d = pair.getValue();
            if(d.getHours() > l.getHoraire()){
                livraisonsNonValides.add(l);
            }
        }
        return livraisonsNonValides;
    }





}
