package ex1;

import javax.print.attribute.standard.PDLOverrideSupported;
import java.util.*;

public class AEtoile {

    public List<Sommet> shortestWay(Graph g, Sommet depart, Sommet arriver) {
        List<Sommet> closedList = new ArrayList<>();
        PriorityQueue<Sommet> openList = new PriorityQueue<>();
        Map<Sommet, ArrayList<Sommet>> map = new HashMap<>();

        depart.setCost(0);
        openList.add(depart);
        while(!openList.isEmpty()) {
            Sommet sommet = openList.peek();
            openList.remove(sommet);
            map.put(sommet, new ArrayList<>());
            if(sommet.getPosition().equals(arriver.getPosition())) {
                var path = createTree(sommet, map);
                return path;
            }
            for(Sommet s : g.getNeighborsOf(sommet)) {
                if(!(closedList.contains(s) && !openList.contains(s))) {
                    if(s.getCost() == 0)
                        s.setCost(sommet.getCost()+s.distanceTo(sommet));
                    s.setHeuristic(s.getCost() + s.euclideanDistance(arriver));
                    openList.add(s);
                    if(s.getParent() == null)
                        s.setParent(sommet);
                }
            }
            closedList.add(sommet);
        }
        return null;
    }

    private List<Sommet> createTree(Sommet sommet, Map<Sommet, ArrayList<Sommet>> map) {
        ArrayList<Sommet> path = new ArrayList<>();
        path.add(sommet);
        while(sommet.getParent() != null) {
            sommet = sommet.getParent();
            path.add(sommet);
        }
        return path;
    }
}
