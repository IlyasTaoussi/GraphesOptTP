package ex1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Graph {

    private HashMap<Sommet, ArrayList<Arete>> graph;

    public Graph() {
        this.graph = new HashMap<>();
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

    /*
    public void delSommet(Sommet s){
        if(graph.containsKey(s)){
            graph.remove(s);
        }
        var sommets = graph.keySet();
        for(Iterator<Sommet> it = sommets.iterator(); it.hasNext(); ) {
            graph.get(it.next())
        }
    }
    */

}
