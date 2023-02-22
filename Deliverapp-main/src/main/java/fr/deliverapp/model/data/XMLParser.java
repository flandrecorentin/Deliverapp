package fr.deliverapp.model.data;

import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.Marker;
import fr.deliverapp.controller.Controller;
import fr.deliverapp.model.objects.*;
import fr.deliverapp.view.Window;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Permet le traitement des cartes et livraisons en format XML
 * Classe singleton
 *
 * @author Hexanome H4124
 */
public class XMLParser {
    /**
     * HashMap des intersections <id, Intersection>
     */
    private HashMap<Long, Intersection> intersections;

    /**
     * Liste des routes praticables sur la carte donnée
     */
    private List <Segment> segments;

    /**
     * Entrepot de la carte
     */
    private Entrepot entrepot;

    /**
     * Instance de la classe
     */
    private static XMLParser instance = null;

    /**
     * Constructeur de la classe
     */
    public XMLParser(){}

    /**
     * Méthode retournant l'unique instance de XMLParser
     * Appelle le constructeur si l'instance n'existe pas encore sinon
     * retourne l'instance déja créée
     * @return instance l'instance unique de la classe
     */
    public static XMLParser getInstance(){
        if(instance == null) instance = new XMLParser();
        return instance;
    }

    /**
     * Charge la carte à partir d'un fichier XML
     * @param cheminFichierCarte le chemin du fichier XML de la carte
     * @return le Plan traité à partir du fichier donné
     */
    public Plan chargerCarte(String cheminFichierCarte){
        Plan plan = null;
        try {
            traiterCarteXML(cheminFichierCarte);
            List<Intersection> listeIntersections = new ArrayList<>(intersections.values());
            plan = new Plan(entrepot,segments, listeIntersections);
        }
        catch(Exception e) {
            //e.printStackTrace();
        }
        return plan;
    }

    /**
     * Méthode qui traite une carte XML pour en tirer les informations
     * (intersections et segments)
     *
     * @param cheminFichierCarte le chemin du fichier de la carte à traiter
     */
    private void traiterCarteXML(String cheminFichierCarte){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Récupère la fichier
            Document document = builder.parse(new File(cheminFichierCarte));

            // Normalise le fichier au format XML
            document.getDocumentElement().normalize();

            // Récupère tous les élements via leur tag
            NodeList intersectionInputList = document.getElementsByTagName("intersection");
            NodeList segmentInputList = document.getElementsByTagName("segment");
            NodeList warehouseInputList = document.getElementsByTagName("warehouse");

            intersections = HashMap.newHashMap(intersectionInputList.getLength());
            segments = new ArrayList<Segment>();

            // Informations sur l'entrepot
            long warehouseId = 0; //default = 0 but not to be used as 0
            for (int i = 0; i < warehouseInputList.getLength(); ++i) {
                Node warehouseNode = warehouseInputList.item(i);
                if (warehouseNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element warehouseElement = (Element) warehouseNode;
                    warehouseId=Long.parseLong(warehouseElement.getAttribute("address"));
                }
            }

            // Chargement des intersections
            Intersection inter;
            double latitude,longitude;
            long id;
            for (int i = 0; i < intersectionInputList.getLength(); ++i) {
                Node intersection = intersectionInputList.item(i);
                if (intersection.getNodeType() == Node.ELEMENT_NODE) {
                    Element intersectionElement = (Element) intersection;
                    latitude=Double.parseDouble(intersectionElement.getAttribute("latitude"));
                    longitude=Double.parseDouble(intersectionElement.getAttribute("longitude"));
                    id=Long.parseLong(intersectionElement.getAttribute("id"));
                    inter=new Intersection(/*id,*/latitude,longitude);
                    intersections.put(id,inter);
                }
            }

            // Chargement des segments
            long idOrigin,idDestination;
            Intersection origin, destination;
            double distance;
            String nom;
            for (int i = 0; i < segmentInputList.getLength(); ++i) {
                Node segment = segmentInputList.item(i);
                if (segment.getNodeType() == Node.ELEMENT_NODE) {
                    Element segmentElement = (Element) segment;
                    idOrigin = Long.parseLong(segmentElement.getAttribute("origin"));
                    idDestination = Long.parseLong(segmentElement.getAttribute("destination"));
                    distance = Double.parseDouble(segmentElement.getAttribute("length"));
                    nom = segmentElement.getAttribute("name");

                    origin = intersections.get(idOrigin);
                    destination = intersections.get(idDestination);

                    segments.add(new Segment(nom,distance,origin,destination));
                }
            }

            entrepot = new Entrepot(intersections.get(warehouseId));

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Méthode qui permet de sauvergarder une liste de livraison dans un
     * fichier XML
     * @param livraisons la liste des livraisons à sauvegarder
     *                         sauvergarde
     * @param nomFichier le nom du fichier de la sauvergarde
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public void sauvegarderLivraisons(List <Livraison> livraisons,
                                      String nomFichier) throws ParserConfigurationException, TransformerException {

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element livraisonsElement=doc.createElement("livraisons");
        Element livraisonElement;
        doc.appendChild(livraisonsElement);
        for(Livraison livraison : livraisons){
            livraisonElement=doc.createElement("livraison");
            livraisonElement.setAttribute("latitude",String.valueOf(livraison.getPointLivraison().getLat()));
            livraisonElement.setAttribute("longitude",String.valueOf(livraison.getPointLivraison().getLon()));
            livraisonElement.setAttribute("livreur",String.valueOf(livraison.getLivreur().getNom()));
            livraisonElement.setAttribute("horaire",String.valueOf(livraison.getHoraire()));
            livraisonsElement.appendChild(livraisonElement);
        }

        // Ecrit dans un fichier
        try (FileOutputStream output =
                     new FileOutputStream(nomFichier)) {
            sauvergarderDocumentDansFlux(doc, output);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Méthode qui écrit un document dans un flux
     * @param doc le document à enregistrer
     * @param output le flux dans lequel enregistrer
     * @throws TransformerException
     */
    private void sauvergarderDocumentDansFlux(Document doc,
                                              OutputStream output)
            throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);

    }

    /**
     * Méthode qui restaure la liste de livraison à partir d'un fichier
     * @param cheminFichier le chemin de la liste de livraison sauvegardée
     * @param c le controlleur
     * @param w la fenêtre
     * @return la liste des livraisons recupérée
     */
    public List<Livraison> restaurerLivraisons(String cheminFichier, Controller c, Window w){

        List<Livraison> livraisons=new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(cheminFichier));
            document.getDocumentElement().normalize();

            // Get all elements
            Node livraisonsNode = document.getDocumentElement();
            NodeList livraisonNodeList = livraisonsNode.getChildNodes();
            double lon,lat;
            int horaire;
            Livreur livreur;
            Livraison livraison;
            Plan plan=null;
            if(Controller.getCheminCarte()!=null){
                plan=this.chargerCarte(Controller.getCheminCarte());
            }
            for (int i = 0; i < livraisonNodeList.getLength(); i++) {
                Node livraisonNode = livraisonNodeList.item(i);
                if (livraisonNode.getNodeType() == Node.ELEMENT_NODE) {
                    if(livraisonNode.getNodeName().equals("livraison")){
                        Element livraisonElement = (Element) livraisonNode;
                        lon=Double.parseDouble(livraisonElement.getAttribute("longitude"));
                        lat=Double.parseDouble(livraisonElement.getAttribute("latitude"));
                        horaire=Integer.parseInt(livraisonElement.getAttribute("horaire"));
                        livreur=new Livreur(livraisonElement.getAttribute("livreur"));
                        Intersection intersection=new Intersection(lat,lon);
                        Pair<Marker,Marker> markers=null;
                        if(plan != null && plan.getEntrepot().getLocalisation() != null){
                            livraison=new Livraison(plan, intersection,horaire,livreur,markers);
                            livraisons.add(livraison);
                        }
                    }
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return livraisons;
    }

    /**
     * Méthode qui permet de générer la feuille de route pour une tournée donnée
     * @param tournee la tournée dont on veut générer la feuille de route
     */
    public void genererFeuilleDeRoute(Tournee tournee, String chemin){
        double lon,lat;
        int longueur=tournee.getListeLivraison().size();
        SimpleDateFormat formater=new SimpleDateFormat("H:mm");

        try {
            FileWriter fileWriter = new FileWriter(chemin);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println("\t\t\t\tDeliverapp");
            printWriter.println();
            printWriter.println("\tDate: "+new Date());
            printWriter.println();
            printWriter.println("\tLivreur: "+tournee.getLivreur().getNom());
            printWriter.println();
            printWriter.println("\t***\t08:00 entrepot:");

            for(int i=0;i<longueur+1;i++){
                for(int j=0;j<tournee.getListeTrajet().get(i).getListeSegment().size();j++){
                    printWriter.println("\t\t\t"+tournee.getListeTrajet().get(i).getListeSegment().get(j).getNom()+"("+ tournee.getListeTrajet().get(i).getListeSegment().get(j).getDistance()+")");
                }
                if(i<longueur){
                    printWriter.println("\t\t\ttemps d'attente:"+tournee.getListeLivraison().get(i).getKey().getNbMinutesAttente()+"min");
                }
                printWriter.println();

                if(i<longueur) {
                    printWriter.println("\t***\t" + formater.format(tournee.getListeLivraison().get(i).getValue()) + " livraison " + (i + 1) + ":");
                    lon = tournee.getListeLivraison().get(i).getKey().getPointLivraison().getLon();
                    lat = tournee.getListeLivraison().get(i).getKey().getPointLivraison().getLat();
                    printWriter.println("\t\t(latitude: " + lat + " longitude: " + lon + ")");
                    printWriter.println("\t\t\tPause 5 min");
                }
            }
            printWriter.println("\t***\tentrepot:");
            printWriter.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}

