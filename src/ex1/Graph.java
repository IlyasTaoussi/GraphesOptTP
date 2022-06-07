package ex1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * @author Ilyas Taoussi, Birkan Yildiz
 * @version 1.0
 */
public class Graph {

    private HashMap<Sommet, ArrayList<Arete>> graph;
    private int maxIndex;

    public Graph() {
        this.graph = new HashMap<>();
    }

    public HashMap<Sommet, ArrayList<Arete>> getGraph() {
        return graph;
    }

    public void setGraph(HashMap<Sommet, ArrayList<Arete>> graph) {
        this.graph = graph;
    }

    public int getMaxIndex() {
        return maxIndex;
    }

    public void setMaxIndex(int maxIndex) {
        this.maxIndex = maxIndex;
    }

    public int getSourceIndex() {
        for (Sommet s : graph.keySet()) {
            if (s.getVal() == 2) return s.getIndex();
        }
        return -1;
    }

    public int getTargetIndex() {
        for (Sommet s : graph.keySet()) {
            if (s.getVal() == 3) return s.getIndex();
        }
        return -1;
    }

    public void addArete(int index1, int index2) {
        Sommet s1 = getSommet(index1);
        Sommet s2 = getSommet(index2);
        addArete(s1, s2, s1.distanceTo(s2));
    }

    public void addArete(int index1, int index2, double cost) {
        Sommet s1 = getSommet(index1);
        Arete arete = new Arete(s1.getIndex(), index2, cost);
        graph.get(s1).add(arete);
    }

    public void addArete(Sommet s1, Sommet s2, double cost) {
        Arete arete = new Arete(s1.getIndex(), s2.getIndex(), cost);
        graph.get(s1).add(arete);
    }

    public void delArete(Sommet s1, Sommet s2) {
        var list = graph.get(s1);
        for (Arete arete : list) {
            if (arete.getSommet2() == s2.getIndex()) {
                list.remove(arete);
                break;
            }
        }
    }

    public Sommet getSommet(int index) {
        var sommets = graph.keySet();
        for (Iterator<Sommet> it = sommets.iterator(); it.hasNext(); ) {
            Sommet sommet = it.next();
            if (sommet.getIndex() == index) return sommet;
        }
        return null;
    }

    public void addSommet(Sommet s) {
        if (!graph.containsKey(s)) {
            graph.put(s, new ArrayList<>());
        }
    }


    public void delSommet(Sommet s) {
        if (graph.containsKey(s)) {
            graph.remove(s);
        }
        var sommets = graph.keySet();
        for (Iterator<Sommet> it = sommets.iterator(); it.hasNext(); ) {
            graph.get(it.next()).remove(s.getIndex());
        }
    }

    public ArrayList<Sommet> getNeighborsOf(Sommet sommet) {
        ArrayList<Sommet> neighbors = new ArrayList<>();
        var sommets = graph.keySet();
        for (Iterator<Sommet> it = sommets.iterator(); it.hasNext(); ) {
            Sommet s = it.next();
            if (sommet.distanceTo(s) != -1) neighbors.add(s);
        }
        return neighbors;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        var sommets = graph.keySet();
        for (Iterator<Sommet> it = sommets.iterator(); it.hasNext(); ) {
            Sommet s = it.next();
            sb.append(s).append(" ").append(graph.get(s)).append("\n");
        }

        return sb.toString();
    }
}
