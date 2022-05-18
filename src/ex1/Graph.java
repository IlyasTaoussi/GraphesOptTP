package ex1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Graph {

    private HashMap<Sommet, ArrayList<Arete>> graph;

    public Graph() {
        this.graph = new HashMap<>();
    }

    public HashMap<Sommet, ArrayList<Arete>> getGraph() {
        return graph;
    }

    public void setGraph(HashMap<Sommet, ArrayList<Arete>> graph) {
        this.graph = graph;
    }

    public void addArete(Sommet s1, Sommet s2){
        Arete arete = new Arete(s1.getIndex(), s2.getIndex(), s1.distanceTo(s2));
        graph.get(s1).add(arete);
    }

    public void delArete(Sommet s1, Sommet s2){
        var list = graph.get(s1);
        for(Arete arete: list){
            if(arete.getSommet2() == s2.getIndex()){
                list.remove(arete);
                break;
            }
        }
    }

    public void addSommet(Sommet s){
        if(!graph.containsKey(s)){
            graph.put(s, new ArrayList<>());
        }
    }


    public void delSommet(Sommet s){
        if(graph.containsKey(s)){
            graph.remove(s);
        }
        var sommets = graph.keySet();
        for(Iterator<Sommet> it = sommets.iterator(); it.hasNext(); ) {
            graph.get(it.next()).remove(s.getIndex());
        }
    }

    public ArrayList<Sommet> getNeighborsOf(Sommet sommet) {
        ArrayList<Sommet> neighbors = new ArrayList<>();
        var sommets = graph.keySet();
        for(Iterator<Sommet> it = sommets.iterator(); it.hasNext(); ) {
            Sommet s = it.next();
            if(sommet.distanceTo(s) != -1) neighbors.add(s);
        }
        return neighbors;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        var sommets = graph.keySet();
        for(Iterator<Sommet> it = sommets.iterator(); it.hasNext(); ) {
            Sommet s = it.next();
            sb.append(s).append(" ").append(graph.get(s)).append("\n");
        }

        return sb.toString();
    }
}
