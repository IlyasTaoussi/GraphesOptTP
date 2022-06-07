package ex1;

/**
 * @author Ilyas Taoussi, Birkan Yildiz
 * @version 1.0
 */
public class Arete {
    private int sommet1;
    private int sommet2;
    private double cost;

    public Arete(int sommet1, int sommet2, double cost) {
        this.sommet1 = sommet1;
        this.sommet2 = sommet2;
        this.cost = cost;
    }

    public int getSommet1() {
        return sommet1;
    }

    public void setSommet1(int sommet1) {
        this.sommet1 = sommet1;
    }

    public int getSommet2() {
        return sommet2;
    }

    public void setSommet2(int sommet2) {
        this.sommet2 = sommet2;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Arete{" +
                "s1=" + sommet1 +
                ", s2=" + sommet2 +
                ", cost=" + cost +
                '}';
    }
}
