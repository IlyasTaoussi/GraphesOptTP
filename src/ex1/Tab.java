package ex1;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
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
                if(tab[x][y] != 0){
                    Sommet s = new Sommet(index, new Position(x, y), tab[x][y]);
                    g.addSommet(s);
                }
                index++;
            }
        }
        g.setMaxIndex(index);
        for(Sommet s1: g.getGraph().keySet()) {
            for(Sommet s2: g.getGraph().keySet()){
                if (!s1.equals(s2) && s1.distanceTo(s2) != -1) g.addArete(s1.getIndex(), s2.getIndex());
            }
        }
        return g;
    }

    public static void solveModel(Graph graph) throws IloException {
        int n = graph.getMaxIndex();
        int s = graph.getSourceIndex();
        int t = graph.getTargetIndex();

        System.out.println("s = " + s + "\nt = " + t + "\nn = " + n);
        IloCplex model = new IloCplex();
        IloNumVar[][] x = new IloNumVar[n][];
        double[][] c = new double[n][n];

        for(int i = 0; i < n; i++){
            x[i] = model.boolVarArray(n);
        }

        for(Sommet sommet: graph.getGraph().keySet()){
            var aretes = graph.getGraph().get(sommet);
            for(Arete arete : aretes){
                c[arete.getSommet1()][arete.getSommet2()] = arete.getCost();
            }
        }

        IloLinearNumExpr obj = model.linearNumExpr();
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++) {
                if(c[i][j] != 0) {
                    obj.addTerm(c[i][j], x[i][j]);
                }
            }
        }
        model.addMinimize(obj);

        for(int i = 0; i < n; i++){
            IloLinearNumExpr constraint = model.linearNumExpr();
            for(int j = 0; j < n; j++){
                if(c[i][j] != 0) {
                    constraint.addTerm(1.0, x[i][j]);
                    constraint.addTerm(-1.0, x[j][i]);
                }
            }
            if(i == s) model.addEq(constraint, 1.0);
            else if(i == t) model.addEq(constraint, -1.0);
            else model.addEq(constraint, 0.0);
        }

        boolean isSolved = model.solve();
        if(isSolved){
            double objValue = model.getObjValue();
            System.out.println("Obj_val = " + objValue);
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                    if(c[i][j] != 0) {
                        var val = model.getValue(x[i][j]);
                        if(val != 0)System.out.println("x[" + (i+1) + "]["+ (j+1) + "]= " + val);
                    }
                }
            }
        } else{
          System.out.println("No Solution");
        }
    }

    public static void main(String[] args) {
        try{
            var tab = createTab("resources/examples/reseau_5_10_1.txt");
            Graph g = tabToGraph(tab);
            for(int[] t : tab)
                System.out.println(Arrays.toString(t));
            solveModel(g);
        }catch (IloException | FileNotFoundException ex){
            ex.printStackTrace();
        }

    }
}
