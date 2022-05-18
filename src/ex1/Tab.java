package ex1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class Tab {
    public static int[][] createTab(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner sc = new Scanner(file);
        String ligne = sc.nextLine();
        int[][] tab = new int[Integer.parseInt(ligne.split(" ")[0])][Integer.parseInt(ligne.split(" ")[1])];
        int i = 0;
        while(sc.hasNextLine()) {
            ligne = sc.nextLine();
            var tmp = ligne.split(" ");
            for(int j = 0; j < tmp.length; j++) {
                tab[i][j] = Integer.parseInt(tmp[j]);
            }
            i++;
        }
        return tab;
    }

    public static Graph tabToGraph(int[][] tab){
        int index=0;
        Graph g = new Graph();
        for(int x=0; x<tab.length; x++){
            for(int y=0; y<tab[x].length; y++){
                index++;
                if(tab[x][y] != 0){
                    Sommet s = new Sommet(index, new Position(x, y), tab[x][y]);
                    g.addSommet(s);
                }
            }
        }
        for(Sommet s1: g.getGraph().keySet()) {
            for(Sommet s2: g.getGraph().keySet()){
                if (!s1.equals(s2) && s1.distanceTo(s2) != -1) g.getGraph().get(s1).add(new Arete(s1.getIndex(), s2.getIndex(), s1.distanceTo(s2)));
            }
        }
        return g;
    }

    public static void main(String[] args) throws FileNotFoundException {
        var tab = createTab("resources/examples/reseau_5_10_2.txt");
        Graph g = tabToGraph(tab);
        System.out.println(g);
        for(int[] t : tab)
            System.out.println(Arrays.toString(t));
    }
}
