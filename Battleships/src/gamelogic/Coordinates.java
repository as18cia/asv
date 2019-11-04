
package gamelogic;


import java.io.Serializable;

public class Coordinates implements Serializable {


    private int x;
    private int y;


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
