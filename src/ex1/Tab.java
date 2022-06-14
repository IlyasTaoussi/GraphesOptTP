package ex1;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Ilyas Taoussi, Birkan Yildiz
 * @version 1.0
 */

public class Tab {
    public static int[][] createTab(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner sc = new Scanner(file);
        String ligne = sc.nextLine();
        int[][] tab = new int[Integer.parseInt(ligne.split("\s")[0])][Integer.parseInt(ligne.split("\s")[1])];
        int i = 0;
        while (sc.hasNextLine()) {
            ligne = sc.nextLine();
            var tmp = ligne.split("\s");
            for (int j = 0; j < tmp.length; j++) {
                tab[i][j] = Integer.parseInt(tmp[j]);
            }
            i++;
        }
        return tab;
    }

    public static Graph tabToGraph(int[][] tab) {
        int index = 0;
        Graph g = new Graph();
        for (int x = 0; x < tab.length; x++) {
            for (int y = 0; y < tab[x].length; y++) {
                if (tab[x][y] != 0) {
                    Sommet s = new Sommet(index, new Position(x, y), tab[x][y]);
                    g.addSommet(s);
                }
                index++;
            }
        }
        g.setMaxIndex(index);
        for (Sommet s1 : g.getGraph().keySet()) {
            for (Sommet s2 : g.getGraph().keySet()) {
                if (!s1.equals(s2) && s1.distanceTo(s2) != -1) g.addArete(s1.getIndex(), s2.getIndex());
            }
        }
        return g;
    }

    public static void solveModel(Graph graph, String path) throws IloException, IOException {
        int n = graph.getMaxIndex();
        int s = graph.getSourceIndex();
        int t = graph.getTargetIndex();

        System.out.println("s = " + s + "\nt = " + t + "\nn = " + n);
        IloCplex model = new IloCplex();
        IloNumVar[][] x = new IloNumVar[n][];
        double[][] c = new double[n][n];

        for (int i = 0; i < n; i++) {
            x[i] = model.boolVarArray(n);
        }

        for (Sommet sommet : graph.getGraph().keySet()) {
            var aretes = graph.getGraph().get(sommet);
            for (Arete arete : aretes) {
                c[arete.getSommet1()][arete.getSommet2()] = arete.getCost();
            }
        }

        IloLinearNumExpr obj = model.linearNumExpr();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (c[i][j] != 0) {
                    obj.addTerm(c[i][j], x[i][j]);
                }
            }
        }
        model.addMinimize(obj);

        for (int i = 0; i < n; i++) {
            IloLinearNumExpr constraint = model.linearNumExpr();
            for (int j = 0; j < n; j++) {
                if (c[i][j] != 0) {
                    constraint.addTerm(1.0, x[i][j]);
                    constraint.addTerm(-1.0, x[j][i]);
                }
            }
            if (i == s) model.addEq(constraint, 1.0);
            else if (i == t) model.addEq(constraint, -1.0);
            else model.addEq(constraint, 0.0);
        }

        boolean isSolved = model.solve();
        if (isSolved) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            StringBuilder sb = new StringBuilder();
            double objValue = model.getObjValue();
            sb.append("Obj_val (min Distance) = ")
                    .append(objValue).append("\n")
                    .append("Chemin :").append("\n")
                    .append("[");
            int i = s;
            while (i != t) {
                for (int j = 0; j < n; j++) {
                    if (c[i][j] != 0) {
                        var val = model.getValue(x[i][j]);
                        if (val != 0) {
                            if (j == t)
                                sb.append(i).append(" ").append(graph.getSommet(i).getPosition()).append(" -> ").append(j).append(graph.getSommet(j).getPosition()).append("]");
                            else sb.append(i).append(graph.getSommet(i).getPosition()).append(" -> ");
                            i = j;
                            break;
                        }
                    }
                }
            }
            writer.append(sb.toString());
            writer.flush();
            writer.close();
        } else {
            System.out.println("No Solution");
        }
    }

    public static void main(String[] args) {
        String path = "resources/examples/reseau_50_50_1.txt";

        try {
            String sol_path = "resources/output/sol_" + path.split("/")[2];

            var tab = createTab(path);
            Graph g = tabToGraph(tab);

            solveModel(g, sol_path);
        } catch (IloException | IOException ex) {
            ex.printStackTrace();
        }
        try {
            String sol_path = "resources/output/solAEtoile_" + path.split("/")[2];
            BufferedWriter writer = new BufferedWriter(new FileWriter(sol_path));
            var tab = createTab(path);
            Graph g = tabToGraph(tab);
            AEtoile a = new AEtoile();
            Sommet dep = g.getSommet(g.getSourceIndex());
            Sommet arr = g.getSommet(g.getTargetIndex());
            long startTime = System.currentTimeMillis();
            ArrayList<Sommet> solution = (ArrayList<Sommet>) a.shortestWay(g, dep, arr);
            long endTime = System.currentTimeMillis();
            System.out.println((endTime - startTime));

            StringBuilder sb = new StringBuilder();
            sb.append("Obj_val (min Distance) = ")
                    .append(solution.get(0).getCost()).append("\n")
                    .append("Chemin :").append("\n")
                    .append("[");
            for(int i = solution.size()-1; i>=0; i--) {
                Sommet s = solution.get(i);
                if(i == 0)
                    sb.append(s.getIndex()).append(" ").append(s.getPosition()).append("]");
                else sb.append(s.getIndex()).append(s.getPosition()).append(" -> ");
            }
            writer.write(sb.toString());
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
