package ex1;

import java.util.ArrayList;
import java.util.Arrays;

public class Sommet {
    private int index;
    private Position position;
    private int val;

    public Sommet() { }

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

}
