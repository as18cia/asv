
package gamelogic;

import gamelogic.Enums.Type;
import gamelogic.Enums.State;

import java.io.Serializable;


public class Ship implements Serializable {
    private Type type;
    private State state;
    private Pos[] position;




    public Ship(Type type){
        this.type = type;
        this.state = State.Afloat;
        switch(type){
            case CARRIER:
                this.position = new Pos[5];
                break;
            case BATTLESHIP:
                this.position = new Pos[4];
                break;
            case CRUISER:
                this.position = new Pos[3];
                break;
            case DESTROYER:
                this.position = new Pos[2];
                break;

        }
    }

    public Pos[] getPosition(){
        return position;
    }

    public void setPosition(int i, Pos p) {
        this.position[i] = p;
    }

    public State getState(){
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Type getType(){
        return type;
    }


}
