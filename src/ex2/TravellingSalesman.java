package ex2;

import ex1.Arete;
import ex1.Graph;
import ex1.Sommet;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class TravellingSalesman {

    public static Graph createGraph(String path) throws FileNotFoundException {
        Graph graph = new Graph();

        File file = new File(path);
        Scanner sc = new Scanner(file);
        String ligne = sc.nextLine();
        int nb_sommets = Integer.parseInt(ligne.split(" ")[0]);

        for(int i = 0; i < nb_sommets; i++){
            graph.getGraph().put(new Sommet(i), new ArrayList<>());
        }
        graph.setMaxIndex(nb_sommets);

        while(sc.hasNextLine()) {
            ligne = sc.nextLine();
            var tmp = ligne.split(" ");
            graph.addArete(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]), Double.parseDouble(tmp[2]));
            graph.addArete(Integer.parseInt(tmp[1]), Integer.parseInt(tmp[0]), Double.parseDouble(tmp[2]));
        }
        return graph;
    }

    public static void solveModel(Graph graph) throws IloException {
        int n = graph.getMaxIndex();
        System.out.println(n);
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
                if(j != i) obj.addTerm(c[i][j], x[i][j]);
            }
        }
        model.addMinimize(obj);

        for(int j = 0; j < n; j++){
            IloLinearNumExpr constraint = model.linearNumExpr();
            for(int i = 0; i < n; i++){
                if(i != j) constraint.addTerm(1.0, x[i][j]);
            }
            model.addEq(constraint, 1.0);
        }

        for(int i = 0; i < n; i++){
            IloLinearNumExpr constraint = model.linearNumExpr();
            for(int j = 0; j < n; j++){
                if(j != i) constraint.addTerm(1.0, x[i][j]);
            }
            model.addEq(constraint, 1.0);
        }

        boolean isSolved = model.solve();
        if(isSolved){
            double objValue = model.getObjValue();
            System.out.println("Obj_val = " + objValue);
            for(int i = 0; i < n; i++){
                for(int j = 0; j < n; j++){
                    if(j != i) {
                        var val = model.getValue(x[i][j]);
                        System.out.println("x[" + i + "]["+ j + "]= " + val);
                    }
                }
            }
        } else{
            System.out.println("No Solution");
        }
    }

    public static void main(String[] args) throws FileNotFoundException{
        try{
            Graph graph = createGraph("resources/examples/ex2.txt");
            System.out.println(graph);
            solveModel(graph);
        } catch(IloException | FileNotFoundException ex){
            ex.printStackTrace();
        }

    }
}
