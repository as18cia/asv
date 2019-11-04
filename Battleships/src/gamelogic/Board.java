package gamelogic;
import GUI.gameUI;
import GUI.placeShips;
import gamelogic.Enums.Type;
import gamelogic.Enums.State;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;


public class Board implements Serializable {
    // A List with all the ships.
    private ArrayList<Ship> fleet;


    public Board() {
        this.fleet = new ArrayList<Ship>();
    }


    public ArrayList<Ship> getFleet() {
        return this.fleet;
    }




    public boolean deployable (Type type, ArrayList<Coordinates> coordinates){

        if(coordinates.get(0).getX() - coordinates.get(1).getX() == 0){
            for(Coordinates sc : coordinates){
                if(sc.getX() != coordinates.get(0).getX()) return false;
            }

            int min = 10;
            int counter = 0;
            Boolean b;

            // finds the first position from the left.
            for(Coordinates sc : coordinates){
                if(sc.getY() < min) min = sc.getY();
            }


            for(int i = 0; i < 10; i++){
                b = false;
                for(Coordinates sc : coordinates){
                    if(sc.getY() == min + i) {
                        counter++;
                        b = true;
                    }
                }
                if(coordinates.size() == counter) return true;
                if(!b) return false;
            }

        }else if(coordinates.get(0).getY() - coordinates.get(1).getY() == 0){
            for(Coordinates sc : coordinates){
                if(sc.getY() != coordinates.get(0).getY()) return false;
            }

            int min = 10; int counter = 0; Boolean b;
            for(Coordinates sc : coordinates){
                if(sc.getX() < min) min = sc.getX();
            }

            for(int i = 0; i < 10; i++){
                b = false;
                for(Coordinates sc : coordinates){
                    if(sc.getX() == min + i) {
                        counter++;
                        b = true;
                    }
                }
                if(coordinates.size() == counter) return true;
                if(!b) return false;
            }
        }

        return false;
    }



    public void setShip(Type type, ArrayList<Coordinates> coordinates){
        Ship s = new Ship(type);
        for(int i = 0; i < s.getPosition().length; i++){
            Pos p = new Pos(coordinates.get(i).getX(), coordinates.get(i).getY(), State.Intact);
            s.setPosition(i, p);
        }
        fleet.add(s);
    }

    public Ship createShip(Type type, ArrayList<Coordinates> coordinates){
        Ship s = new Ship(type);
        for(int i = 0; i < s.getPosition().length; i++){
            Pos p = new Pos(coordinates.get(i).getX(), coordinates.get(i).getY(), State.Intact);
            s.setPosition(i, p);
        }
        return s;
    }

    public boolean Random_deployable (ArrayList<Coordinates> coordinates){
        for(Coordinates c: coordinates){
            if(c.getX() < 0 || c.getY() < 0 || c.getX() > 9 || c.getY() >9) return false;
            for(Ship s: this.getFleet()){
                for(Pos p: s.getPosition()){
                    if(c.getX() == p.getX() && c.getY() == p.getY()) return false;
                }
            }
        }
        return true;
    }

    public ArrayList<Coordinates> generateCoordinates(int x, int y, Type type, int i){
        ArrayList<Coordinates>  coordinates = new ArrayList<>();
        int n = 0,  m = 0;
        if(i == 1) n = 1;
        if(i == -1) m = 1;

        for(int a = x, b = y; a < x + 9 && b < y + 9; a+=n, b+=m ){
            if(coordinates.size() == 2 && type == Type.DESTROYER) return coordinates;
            if(coordinates.size() == 3 && type == Type.CRUISER) return coordinates;
            if(coordinates.size() == 4 && type == Type.BATTLESHIP) return coordinates;
            if(coordinates.size() == 5 && type == Type.CARRIER) return coordinates;
            coordinates.add(new Coordinates(a, b));

        }
        return coordinates;
    }



    public void RandomFleet(placeShips C){
        C.DS.setStyle("-fx-font-weight: normal;");
        do {
            int size  = fleet.size();
            if(size < 4){
                RandomShip(Type.DESTROYER);
                C.DS.setText("DESTROYER " + (size + 1) + "/4");
            }else if (size < 7){
                RandomShip(Type.CRUISER);
                C.CR.setText("CRUISER " + (size -4 + 1) + "/3");
            }else if (size < 9){
                RandomShip(Type.BATTLESHIP);
                C.BS.setText("BATTLESHIP " + (size -7 + 1) + "/2");
            }else if (size == 9){
                RandomShip(Type.CARRIER);
                C.CA.setText("CARRIER " + (size -9 + 1) + "/1");

            }

        } while (fleet.size() != 10);
    }

    public void RandomShip (Type type){

        Ship ship = new Ship(type);
        ship.setState(State.Sunk);
        Random r = new Random();

        do {
            int randY = r.nextInt(10);
            int randX = r.nextInt(10);

            if(randX < 5){
                ArrayList<Coordinates> coordinates = this.generateCoordinates(randX, randY, type, 1);

                if(this.Random_deployable(coordinates)) {
                    ship = this.createShip(type, coordinates);
                    this.getFleet().add(ship);
                }
            }else{
                ArrayList<Coordinates> coordinates = this.generateCoordinates(randX, randY, type, -1);
                if(this.Random_deployable(coordinates)) {
                    ship = this.createShip(type, coordinates);
                    this.getFleet().add(ship);
                }
            }

        } while (ship.getState() != State.Afloat);

    }

}
