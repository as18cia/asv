
package gamelogic;

import gamelogic.Enums.State;

import java.io.Serializable;


public class Pos implements Serializable {


    private int x;
    private int y;
    private State state;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Pos(int a, int b, State state) {
        this.x = a;
        this.y = b;
        this.state = state;
    }

}
