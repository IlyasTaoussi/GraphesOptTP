package ex1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AEtoile {
    public int compareHeuristic(int heuristicA, int heuristicB) {
        if(heuristicA < heuristicB) return 1;
        if(heuristicA == heuristicB) return 0;
        else return -1;
    }

    public List<Sommet> shortestWay(Graphe g, Sommet depart, Sommet arriver) {
        List<Sommet> closedList = new ArrayList<>();
        List<Sommet> openList = new ArrayList<>();
        Map<Sommet, ArrayList<Sommet>> map = new HashMap<>();

        openList.add(depart);
        while(!openList.isEmpty()) {
            Sommet sommet = openList.get(0);
            openList.remove(0);
            if(sommet.getPosition().equals(arriver.getPosition())) {
                var path = contruireArbre(sommet, map);
                return path;
            }
            for(Sommet s : sommet.getNeighbors()) {
                if(!(closedList.contains(s) || )) {
                    s.setCost(sommet.getCost()+1);
                    s.setHeuristic(s.getCost() + getDistance(s.getPosition(), arriver.getPosition()));
                    openList.add(s);
                }
            }
            closedList.add(sommet);
        }
        return null;
    }

    private List<Sommet> contruireArbre(Sommet sommet, Map<Sommet, ArrayList<Sommet>> map) {
        // TODO
        return null;
    }
}
