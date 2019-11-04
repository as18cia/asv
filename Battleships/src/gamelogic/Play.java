package gamelogic;

import gamelogic.Enums.*;

import java.util.ArrayList;




public class Play {


    public static ArrayList<Pos> create_report(ArrayList<Coordinates> strike, Board b){

        ArrayList<Pos> p = new ArrayList<>();

        for(int i = 0; i < strike.size(); i++){
            p.add(new Pos(strike.get(i).getX(), strike.get(i).getY(), State.Intact));
        }

        for(Pos pos: p){
            for(Ship sh: b.getFleet()){
                if(pos.getState() == State.Hit || pos.getState() == State.Sunk) continue;
                if(sh.getState() == State.Sunk) continue;
                boolean sunk = true;

                for(Pos n: sh.getPosition()){
                    if(pos.getX() == n.getX() && pos.getY() == n.getY()){
                        pos.setState(State.Hit);
                        n.setState(State.Hit);
                    }
                    sunk = sunk && n.getState() == State.Hit;
                }
                if(sunk) {
                    pos.setState(State.Sunk);
                    sh.setState(State.Sunk);
                }
            }
        }
        return p;
    }



}
