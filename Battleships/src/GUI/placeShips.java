package GUI;

import gamelogic.Board;


import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import gamelogic.*;
import gamelogic.Enums.Type;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import network.*;
import java.io.IOException;
import java.util.ArrayList;



public class placeShips{


    Board board = new Board();
    int size = 0;
    Player player;
    Boolean P1_Ready = false;
    Boolean P2_Ready = false;

    @FXML
    private Button PlayButton;
    @FXML
    private Button RndButton;
    @FXML
    private Button RemoveButton;
    @FXML
    private Button NextButton, BackButton;
    @FXML
    private GridPane GridF;
    @FXML
    public Label DS, CR, BS, CA;
    @FXML
    private Button Skip;
    @FXML
    private Label LabelW;




    @FXML
    void RandomP(ActionEvent event) {
        if(board.getFleet().size() != 0) return;
        board.RandomFleet(this);
        UpdateGridF();
        UpdateSize();

        NextButton.setVisible(false);
        BackButton.setVisible(false);
        RndButton.setVisible(false);
        PlayButton.setVisible(true);
        RemoveButton.setVisible(true);

    }


    @FXML
    void RemoveShips (){
        board.getFleet().clear();
        UpdateSize();
        RestartGridF();

        DS.setText("DESTROYER 0/4");
        CR.setText("CRUISER 0/3");
        BS.setText("BATTLESHIP 0/2");
        CA.setText("CARRIER 0/1");
        DS.setStyle("-fx-font-weight: bold;");


        NextButton.setVisible(true);
        NextButton.setDisable(true);
        BackButton.setVisible(true);
        BackButton.setDisable(true);
        RndButton.setVisible(true);
        PlayButton.setVisible(false);
        RemoveButton.setVisible(false);
    }


    @FXML
    void setShips (ActionEvent event) {
        if(size < 4) NextF(2, event);
        if(size < 7 && size >= 4) NextF(3, event);
        if(size < 9 && size >= 7) NextF(4, event);
        if(size < 10 && size >= 9) NextF(5, event);
    }



    @FXML
    void play (ActionEvent event){

    }
    @FXML
    void Next_press (ActionEvent event){
        if(size < 4) {
            ArrayList<Coordinates> Coord = getCoordinates();
            if (board.deployable(Type.DESTROYER, Coord)) {
                board.setShip(Type.DESTROYER, Coord);
                DS.setText("DESTROYER " + (size + 1) + "/4");
                re_set(true);
                LabelUpdate();
                BackButton.setDisable(false);
            } else {
                re_set(false);
            }
        }else if (size < 7){
            ArrayList<Coordinates> Coord = getCoordinates();
            if(board.deployable(Type.CRUISER, Coord)){
                board.setShip(Type.CRUISER, Coord);
                CR.setText("CRUISER " + (size -4 + 1) + "/3");
                re_set(true);
                LabelUpdate();
            }else{
                re_set(false);
            }
        }else if (size < 9){
            ArrayList<Coordinates> Coord = getCoordinates();
            if(board.deployable(Type.BATTLESHIP, Coord)){
                board.setShip(Type.BATTLESHIP, Coord);
                BS.setText("BATTLESHIP " + (size -7 + 1) + "/2");
                re_set(true);
                LabelUpdate();
            }else{
                re_set(false);
            }
        }else if (size < 10){ ///////////
            ArrayList<Coordinates> Coord = getCoordinates();
            if(board.deployable(Type.CARRIER, Coord)){
                board.setShip(Type.CARRIER, Coord);
                CA.setText("CARRIER " + (size -9 + 1) + "/1");
                re_set(true);
                LabelUpdate();
            }else{
                re_set(false);
            }
        }else if(size == 10){

            NextButton.setVisible(false);
            BackButton.setVisible(false);
            RndButton.setVisible(false);
            PlayButton.setVisible(true);
            RemoveButton.setVisible(true);
        }

        UpdateSize();
        if(size != 10) NextButton.setDisable(true); //
    }

    public void startThread1(){
       new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    player.input.readObject();

                    do {
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } while (!P1_Ready);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            launchGame();
                        }
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }



    @FXML
    void StartGame(){
        PlayButton.setVisible(false);
        RemoveButton.setVisible(false);
        LabelW.setVisible(true);

        P1_Ready = true;
        try {
            player.output.writeObject("#start#");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void Back_press(){
        Pos[] pos = board.getFleet().get(size -1).getPosition();
        ObservableList<Node> children = GridF.getChildren();
        for(Node node : children){
            if(node instanceof ToggleButton){
                ToggleButton tb = (ToggleButton) node;

                for(Pos p: pos){
                    if(p.getX() == GridPane.getRowIndex(node) && p.getY() == GridPane.getColumnIndex(node)) {
                        tb.setDisable(false);
                        tb.setStyle("");
                    }
                }
            }

        }
        board.getFleet().remove(size - 1);
        UpdateSize();

        if(size < 4){
            DS.setText("DESTROYER " + (size) + "/4");
        }else if(size < 7){
            CR.setText("CRUISER " + (size -4) + "/3");
        }else if(size < 9){
            BS.setText("BATTLESHIP " + (size -7) + "/2");
        }else if(size < 10){
            CA.setText("CARRIER " + (size -9) + "/1");
        }
        LabelUpdate2();
        if(board.getFleet().size() == 0) BackButton.setDisable(true);

    }



    //b-true : a ship can be placed on the selected position -> unselect then Disabled the selected buttons, set color: green
    //b-false : a ship cannot be placed ->  unselect selected buttons.

    void re_set(Boolean b){
        ObservableList<Node> children = GridF.getChildren();
        for (Node node : children) {
            if(node instanceof ToggleButton) {
                ToggleButton tb = (ToggleButton) node;
                if (b && tb.isSelected()) {
                    tb.setSelected(false);
                    tb.setDisable(true);
                    if(size < 4) {
                        tb.setStyle("-fx-background-color: #039aff;");
                    }else if(size < 7){
                        tb.setStyle("-fx-background-color: #72E0E3;");
                    }else if (size < 9){
                        tb.setStyle("-fx-background-color: #388E8E;");
                    }else if (size == 9){
                        tb.setStyle("-fx-background-color: #1A98DE;");
                    }


                } else {
                    tb.setSelected(false);
                }
            }
        }
    }


     void LabelUpdate(){
        if (size == 3){
            DS.setStyle("-fx-font-weight: normal;");
            CR.setStyle("-fx-font-weight: Bold;");
        }else if(size == 6){
            CR.setStyle("-fx-font-weight: normal;");
            BS.setStyle("-fx-font-weight: Bold;");
        }else if(size == 8){
            BS.setStyle("-fx-font-weight: normal;");
            CA.setStyle("-fx-font-weight: Bold;");
        }
    }

    // for the Back button
    void LabelUpdate2(){
        if (board.getFleet().size() == 3){
            DS.setStyle("-fx-font-weight: Bold;");
            CR.setStyle("-fx-font-weight: normal;");
        }else if(board.getFleet().size() == 6){
            CR.setStyle("-fx-font-weight: Bold;");
            BS.setStyle("-fx-font-weight: normal;");
        }else if(board.getFleet().size() == 8){
            BS.setStyle("-fx-font-weight: Bold;");
            CA.setStyle("-fx-font-weight: normal;");
        }
    }

        //Group? object
    // Enable or Disable the Next button depending of the Ship size.
    void NextF (int x, ActionEvent event){
        int c = 0;
        ObservableList<Node> children = GridF.getChildren();
        for (Node node : children) {
            if(node instanceof ToggleButton){
                ToggleButton tb = (ToggleButton) node;
                if(tb.isSelected()) c++;
            }

        }
        if(c < x) NextButton.setDisable(true);
        if(c == x) NextButton.setDisable(false);
        if(c > x) {
            ToggleButton tb =  (ToggleButton)event.getSource();
            tb.setSelected(false);
        }
    }


    void RestartGridF(){
        ObservableList<Node> children = GridF.getChildren();
        for(Node node: children){
            if(node instanceof ToggleButton){
                ToggleButton tb = (ToggleButton) node;
                tb.setDisable(false);
                tb.setStyle("");
            }
        }
    }



    void UpdateGridF() {
        ObservableList<Node> children = GridF.getChildren();
        for(Node node: children){
            if(node instanceof ToggleButton){
                ToggleButton tb = (ToggleButton) node;
                tb.setDisable(true);
                for(Ship s: board.getFleet()){
                    for (Pos p : s.getPosition()) {
                        if(p.getX() == GridPane.getRowIndex(node) && p.getY() == GridPane.getColumnIndex(node)){
                            if(s.getType() == Type.DESTROYER) tb.setStyle("-fx-background-color: #039aff;");
                            if(s.getType() == Type.CRUISER) tb.setStyle("-fx-background-color: #72E0E3;");
                            if(s.getType() == Type.BATTLESHIP) tb.setStyle("-fx-background-color: #388E8E;");
                            if(s.getType() == Type.CARRIER) tb.setStyle("-fx-background-color: #1A98DE;");
                        }
                    }
                }
            }
        }
    }


    // return the Coordinates of the selected Buttons
    ArrayList<Coordinates> getCoordinates(){
        ArrayList<Coordinates> coordinates = new ArrayList<>();
        int x, y;
        ObservableList<Node> children = GridF.getChildren();

        for (Node node : children) {
            if(node instanceof ToggleButton){
                ToggleButton tb = (ToggleButton) node;
                if(tb.isSelected()){
                    x = GridPane.getRowIndex(node);
                    y = GridPane.getColumnIndex(node);
                    coordinates.add(new Coordinates(x, y));
                }
            }
        }

        return coordinates;
    }


    void UpdateSize (){
        this.size = this.board.getFleet().size();
    }

    void readMessage (String s, gameUI C){
        C.textS.appendText("<Player 2>: " + s + "\n");
    }

    public void launchGame(){
        try {

            Stage stage = (Stage) NextButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("gameUI.fxml"));
            Parent root = loader.load();
            gameUI C2 = loader.getController();
            C2.board = board;
            C2.player = player;
            C2.UpdateSGrid();
            stage.setScene(new Scene(root));
            stage.setTitle("Battleships");
            stage.show();


            C2.thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    if(player instanceof Server){
                        Object strike;
                        Object report;
                        while(true && !C2.thread.isInterrupted()){
                            try {

                                C2.myTurn = true;
                                C2.updateCircle();

                                do{
                                    report = (((Server) player).input.readObject());
                                    if(report instanceof String) readMessage((String) report, C2);
                                } while (report instanceof String);

                                if(report instanceof Boolean) break;

                                if(report instanceof Board){
                                    Platform.runLater(()->C2.UpdateLabel(-1));
                                    C2.ShowEnemyBoard(((Board) report).getFleet());
                                    C2.Circle.setVisible(false);
                                }else{
                                    C2.read_report((ArrayList<Pos>) report);
                                }

                                if(C2.SS == 10) {
                                    Platform.runLater(()->C2.UpdateLabel(1));
                                    ((Server) player).output.writeObject(C2.board);
                                    C2.disableGridE();
                                    C2.Circle.setVisible(false);
                                }


                                C2.myTurn = false;
                                C2.updateCircle();

                                do{
                                    strike = (((Server) player).input.readObject());
                                    if(strike instanceof String) readMessage((String) strike, C2);
                                } while (strike instanceof String);

                                if(strike instanceof Boolean) break;

                                ArrayList<Pos> strikeReport = Play.create_report((ArrayList<Coordinates>) strike, C2.board);
                                ((Server) player).output.writeObject(strikeReport);
                                C2.UpdateSGrid (strikeReport);



                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }


                    }else if (player instanceof Client){
                        Object strike;
                        Object report;
                        while (true && !C2.thread.isInterrupted()){

                            try {

                                C2.myTurn = false;
                                C2.updateCircle();

                                do{
                                    strike = (((Client) player).input.readObject());
                                    if(strike instanceof String) readMessage((String) strike, C2);
                                } while (strike instanceof String);

                                if(strike instanceof Boolean) break;

                                ArrayList<Pos> strikeReport = Play.create_report((ArrayList<Coordinates>) strike, C2.board);
                                ((Client) player).output.writeObject(strikeReport);
                                C2.UpdateSGrid(strikeReport);


                                C2.myTurn = true;
                                C2.updateCircle();


                                do{
                                    report = (((Client) player).input.readObject());
                                    if(report instanceof String) readMessage((String)report, C2);
                                } while (report instanceof String);

                                if(report instanceof Boolean) break;

                                if(report instanceof Board){
                                    Platform.runLater(()->C2.UpdateLabel(-1));
                                    C2.TopLabel.setVisible(true);
                                    C2.ShowEnemyBoard(((Board) report).getFleet());
                                    C2.Circle.setVisible(false);
                                }else{
                                    C2.read_report((ArrayList<Pos>) report);
                                }


                                if(C2.SS == 10) {
                                    Platform.runLater(()->C2.UpdateLabel(1));
                                    C2.TopLabel.setVisible(true);
                                    ((Client) player).output.writeObject(C2.board);
                                    C2.disableGridE();
                                    C2.Circle.setVisible(false);
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                    Platform.runLater(()->{
                        C2.textS.appendText("<>: Player 2 left");
                        C2.SendButton.setText("-_-");
                        C2.SendButton.setDisable(true);
                        C2.fire.setVisible(false);
                    });

                }

            });

            C2.thread.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

