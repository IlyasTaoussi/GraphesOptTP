package ex2;

import ex1.Arete;
import ex1.Graph;
import ex1.Sommet;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Ilyas Taoussi, Birkan Yildiz
 * @version 1.0
 */

public class TravellingSalesman {

    public static Graph createGraph(String path) throws FileNotFoundException {
        Graph graph = new Graph();

        File file = new File(path);
        Scanner sc = new Scanner(file);
        String ligne = sc.nextLine();
        int nb_sommets = Integer.parseInt(ligne.split(" ")[0]);

        for (int i = 0; i < nb_sommets; i++) {
            graph.getGraph().put(new Sommet(i), new ArrayList<>());
        }
        graph.setMaxIndex(nb_sommets);

        while (sc.hasNextLine()) {
            ligne = sc.nextLine();
            var tmp = ligne.split(" ");
            graph.addArete(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]), Double.parseDouble(tmp[2]));
            graph.addArete(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[0]), Double.parseDouble(tmp[2]));
        }
        return graph;
    }

    public static double solveModel(Graph graph, int n) throws IloException {
        IloCplex model = new IloCplex();
        IloNumVar[][] x = new IloNumVar[n][];
        double[][] c = new double[n][n];
        IloNumVar[] u = model.numVarArray(n, 0, Double.MAX_VALUE);

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
                if (j != i && c[i][j] != 0) obj.addTerm(c[i][j], x[i][j]);
            }
        }
        model.addMinimize(obj);

        for (int j = 0; j < n; j++) {
            IloLinearNumExpr constraint = model.linearNumExpr();
            for (int i = 0; i < n; i++) {
                if (i != j && c[i][j] != 0) constraint.addTerm(1.0, x[i][j]);
            }
            model.addEq(constraint, 1.0);
        }

        for (int i = 0; i < n; i++) {
            IloLinearNumExpr constraint = model.linearNumExpr();
            for (int j = 0; j < n; j++) {
                if (j != i && c[i][j] != 0) constraint.addTerm(1.0, x[i][j]);
            }
            model.addEq(constraint, 1.0);
        }

        for (int i = 1; i < n; i++) {
            for (int j = 1; j < n; j++) {
                if (j != i && c[i][j] != 0) {
                    IloLinearNumExpr constraint = model.linearNumExpr();
                    constraint.addTerm(1.0, u[i]);
                    constraint.addTerm(-1.0, u[j]);
                    constraint.addTerm(n-1, x[i][j]);
                    model.addLe(constraint, n-2);
                }
            }

        }

        boolean isSolved = model.solve();
        if (isSolved) {
            double objValue = model.getObjValue();
            System.out.println("Obj_val = " + objValue);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (j != i && c[i][j] != 0) {
                        var val = model.getValue(x[i][j]);
                        if(val != 0) System.out.println("x[" + i + "][" + j + "]= " + val);
                    }
                }
            }
            return objValue;
        } else {
            System.out.println("No Solution");
            return -1;
        }
    }

    public static Graph generateGraph(int n, double p){
        Graph graph = new Graph();
        for(int i = 0; i < n; i++){
            graph.addSommet(new Sommet(i));
        }

        Random random = new Random();
        double rnd;
        double cost;
        for(int i = 0; i < n-1; i++){
            for(int j = i; j < n; j++){
                rnd = Math.random();
                cost = random.nextDouble(10, 50);
                if(rnd <= p) {
                    graph.addArete(i, j, cost);
                    graph.addArete(j, i, cost);
                }
            }
        }
        return graph;
    }

    public static double enumerateCycles(Graph graph, int n, boolean[] v, int start, int current, int count, double cost, double obj){
        Arete arete = graph.getArete(current, start);
        if(count == n && arete != null){
            obj = Math.min(obj, cost + arete.getCost());
            return obj;
        }

        for(int i = 0; i < n; i++){
            arete = graph.getArete(current, i);
            if(!v[i] && arete != null){
                v[i] = true;
                obj = enumerateCycles(graph, n, v, start, i, count + 1, cost + arete.getCost(), obj);
                v[i] = false;
            }
        }
        return obj;
    }

    public static void main(String[] args) {
        try {
            /*
            Graph graph = createGraph("resources/examples/ex2.txt");
            solveModel(graph, n);
             */
            BufferedWriter writer = new BufferedWriter(new FileWriter("resources/output/simul_results.txt"));
            StringBuilder sb = new StringBuilder();

            int[] N = {5, 10, 11, 12, 15, 20, 50, 100, 500};
            double p = 1;

            int start = 0;
            boolean[] v ;
            double obj;

            double begin;
            double end;
            for(int i = 0; i < N.length; i++){
                Graph graph = generateGraph(N[i], p);
                v = new boolean[N[i]];
                v[start] = true;
                obj = Double.MAX_VALUE;
                sb.append("### N = ").append(N[i]).append("\n");

                if(i <= 3){
                    begin = (double) System.currentTimeMillis();
                    obj = enumerateCycles(graph, N[i], v, start, start, 1, 0, obj);
                    end = (double) System.currentTimeMillis();
                    sb.append("Obj_Enum = ").append(obj).append(" / ").append("Time = ").append((end - begin)).append(" ms").append("\n");
                }

                begin = System.currentTimeMillis();
                obj = solveModel(graph, N[i]);
                end = System.currentTimeMillis();
                sb.append("Obj_CPLEX = ").append(obj).append(" / ").append("Time = ").append((end - begin)).append(" ms").append("\n\n");
            }
            writer.append(sb.toString());
            writer.flush();
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
