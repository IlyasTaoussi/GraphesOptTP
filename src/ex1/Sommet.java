package ex1;

/**
 * @author Ilyas Taoussi, Birkan Yildiz
 * @version 1.0
 */

public class Sommet {
    private int index;
    private Position position;
    private int val;

    public Sommet() {
    }

    public Sommet(int index, Position position, int val) {
        this.index = index;
        this.position = position;
        this.val = val;
    }

    public Sommet(int index) {
        this.index = index;
    }

    public Sommet(int index, int x, int y, int val) {
        this.index = index;
        position = new Position(x, y);
        this.val = val;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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
        if (val == 0 || position == null) return index == sommet.index;
        return index == sommet.index && val == sommet.val && position.equals(sommet.position);
    }

    public double distanceTo(Sommet s2) {
        if (Math.abs(position.getX() - s2.getPosition().getX()) == 1 && Math.abs(position.getY() - s2.getPosition().getY()) == 1) {
            return Math.sqrt(2);
        } else if ((Math.abs(position.getX() - s2.getPosition().getX()) == 1 && position.getY() == s2.getPosition().getY())
                || (Math.abs(position.getY() - s2.getPosition().getY()) == 1 && position.getX() == s2.getPosition().getX())) {
            return 1;
        }
        return -1;
    }

    @Override
    public String toString() {
        return "Sommet{" +
                "index=" + index +
                ", position=" + position +
                ", val=" + val +
                '}';
    }
}
