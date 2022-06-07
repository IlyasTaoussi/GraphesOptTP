package ex1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Sommet implements Comparable<Sommet> {
    private int index;
    private Position position;
    private int val;
    private double cost;
    private double heuristic;
    private Sommet parent = null;

    public Sommet() { }

    public Sommet(int index, Position position, int val){
        this.index = index;
        this.position = position;
        this.val = val;
        this.cost = 0;
    }

    public Sommet(int index){
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Sommet(int index, int x, int y, int val) {
        this.index = index;
        position = new Position(x, y);
        this.val = val;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sommet sommet = (Sommet) o;
        if(val == 0 || position == null) return index == sommet.index;
        return index == sommet.index && val == sommet.val && position.equals(sommet.position);
    }

    public double distanceTo(Sommet s2){
        if(Math.abs(position.getX() - s2.getPosition().getX()) == 1 && Math.abs(position.getY() - s2.getPosition().getY()) == 1){
            return Math.sqrt(2);
        }
        else if((Math.abs(position.getX() - s2.getPosition().getX()) == 1 && position.getY() == s2.getPosition().getY())
                || (Math.abs(position.getY() - s2.getPosition().getY()) == 1 && position.getX() == s2.getPosition().getX())){
            return 1;
        }
        return -1;
    }

    public double euclideanDistance(Sommet s2) {
        return Math.sqrt(Math.pow(s2.position.getX()-this.position.getX(), 2) + Math.pow(s2.position.getY() - this.position.getY(), 2));
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(double heuristic) {
        this.heuristic = heuristic;
    }

    @Override
    public String toString() {
        return "Sommet{" +
                "index=" + index +
                ", position=" + position +
                ", val=" + val +
                '}';
    }

    @Override
    public int compareTo(Sommet o) {
        return Double.compare(this.heuristic, o.heuristic);
    }

    public void setParent(Sommet sommet) {
        this.parent = sommet;
    }

    public Sommet getParent() {
        return this.parent;
    }
}
