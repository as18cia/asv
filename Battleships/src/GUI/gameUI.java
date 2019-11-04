package GUI;

import gamelogic.Coordinates;
import gamelogic.*;
import gamelogic.Enums.State;
import gamelogic.Enums.Type;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import network.*;
import java.io.IOException;
import java.util.ArrayList;



@SuppressWarnings("ALL")
public class gameUI {
    Board board;
    Player player;
    boolean myTurn;
    public int SS = 0;
    public Thread thread;



    @FXML
    public Circle Circle;

    @FXML
    public Button SendButton;

    @FXML
    public TextArea textS;

    @FXML
    public TextArea textW;

    @FXML
    private GridPane EGrid;

    @FXML
    private GridPane SGrid;

    @FXML
    public Button fire;

    @FXML
    public Label TopLabel;

    @FXML
    private Button leave;




    @FXML
    void Leave (ActionEvent event){

        if (thread.isAlive()) {
            try {
                if(player instanceof Client) ((Client) player).output.writeObject(true);
                if(player instanceof Server) ((Server) player).output.writeObject(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
            thread.stop();
            closeConnection ();
        }else{
            closeConnection ();
        }

    }

    public void closeConnection (){
        if(player instanceof Client) {
            ((Client) player).CloseConnection();
        }
        if(player instanceof Server) {
            ((Server) player).CloseConnection();
        }
        ((Stage) leave.getScene().getWindow()).close();
    }



    @FXML
    void sendMessage(ActionEvent event) {
        String message = textW.getText();
        //checks if the string is not empty
        if(message.trim().length() == 0) {
            textW.setText("");
            return;
        }
        message = message.trim();
        if(player instanceof Server){
            ((Server) player).sendMessage(message);
            textW.setText("");
            textS.appendText(("<You>:  " + message + "\n"));
        }else if (player instanceof Client){
            ((Client) player).sendMessage(message);
            textW.setText("");
            textS.appendText(("<You>:  " + message + "\n"));
        }
    }


    @FXML
    void Pos_press(ActionEvent event){
        int x = 0;
        ArrayList<ToggleButton> T = getTButtons();
        for(ToggleButton tb: T){
            if(tb.isSelected()) x++;
        }
        if(x == 5) fire.setDisable(false);
        if(x < 5)fire.setDisable(true);
        if(x > 5) {
            ToggleButton tb =  (ToggleButton)event.getSource();
            tb.setSelected(false);
        }
    }
    // return an array of with the Coordinates of the selected buttons
    @FXML
    void Hit_press (ActionEvent event){
        if(!myTurn) {
            textS.appendText("<> its not your turn\n");
            return;
        }
        ArrayList<Coordinates> s = new ArrayList<>();
        ArrayList<ToggleButton> T = getTButtons();
        for(ToggleButton tb: T){
            if(tb.isSelected()) {
                s.add(new Coordinates(GridPane.getRowIndex(tb), GridPane.getColumnIndex(tb)));
            }
        }

        try {
            if(player instanceof Server){
                ((Server)player).output.writeObject(s);
                ((Server)player).output.flush();
            }else if(player instanceof Client){
                ((Client)player).output.writeObject(s);
                ((Client)player).output.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    ArrayList<ToggleButton> getTButtons(){
        ArrayList<ToggleButton> T = new ArrayList<>();
        ObservableList<Node> children = EGrid.getChildren();
        for(Node node: children) {
            if (node instanceof ToggleButton) {
                ToggleButton tb = (ToggleButton) node;
                T.add(tb);
            }
        }
         return T;
    }


    void UpdateSGrid (ArrayList<Pos> pos){
        ObservableList<Node> children = SGrid.getChildren();
        for(Node node: children) {
            if (node instanceof ToggleButton) {
                ToggleButton tb = (ToggleButton) node;
                for(Pos p : pos){
                    if(p.getX() == GridPane.getRowIndex(node) && p.getY() == GridPane.getColumnIndex(node)){
                        if(p.getState() == State.Intact)  tb.setStyle("-fx-background-color: #909090;");
                        if(p.getState() == State.Hit || p.getState() == State.Sunk ) tb.setStyle("-fx-background-color: #FF2A2A;");
                    }
                }
            }
        }
    }


    void UpdateSGrid() {
        ObservableList<Node> children = SGrid.getChildren();
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


    public void read_report(ArrayList<Pos> p){
        for(Pos pos: p){
            if(pos.getState() == State.Sunk) SS++ ;

            UpdateEGrid(pos.getX(), pos.getY(), pos.getState());


        }
    }

    void UpdateEGrid (int a, int b, State state){
        ObservableList<Node> children = EGrid.getChildren();
        for(Node node: children){
            if(node instanceof ToggleButton){
                ToggleButton tb = (ToggleButton) node;
                if(GridPane.getRowIndex(node) == a && GridPane.getColumnIndex(node) == b){
                    tb.setSelected(false);
                    fire.setDisable(true);
                    switch(state){
                        case Hit:
                            tb.setDisable(true);
                            tb.setStyle("-fx-background-color: Lime;");
                            break;
                        case Sunk:
                            tb.setDisable(true);
                            tb.setStyle("-fx-background-color: red;");
                            break;
                        case Intact:
                            tb.setDisable(true);
                            tb.setStyle("-fx-background-color: DimGrey;");
                            break;
                    }
                }
            }
        }
    }

    public void ShowEnemyBoard (ArrayList<Ship> ships){
        ObservableList<Node> children = EGrid.getChildren();
        for(Node node: children){
            if(node instanceof ToggleButton){
                ToggleButton tb = (ToggleButton) node;
                if(!tb.isDisabled()){
                    for(Ship s: ships){
                        for(Pos p: s.getPosition()){
                            if(GridPane.getRowIndex(node) == p.getX() && GridPane.getColumnIndex(node) == p.getY()){
                                tb.setDisable(true);
                                if(s.getType() == Type.DESTROYER) tb.setStyle("-fx-background-color: #039aff;");
                                if(s.getType() == Type.CRUISER) tb.setStyle("-fx-background-color: #72E0E3;");
                                if(s.getType() == Type.BATTLESHIP) tb.setStyle("-fx-background-color: #388E8E;");
                                if(s.getType() == Type.CARRIER) tb.setStyle("-fx-background-color: #1A98DE;");
                            }else{
                                tb.setDisable(true);
                            }
                        }
                    }
                }

            }
        }
    }

    public void UpdateLabel(int i){
        fire.setVisible(false);
        if(i == 1) {
            TopLabel.setText("You Won");
            TopLabel.setStyle("-fx-text-fill: green;");
        }
        if (i == -1) {
            TopLabel.setText("You Lost");
            TopLabel.setStyle("-fx-text-fill: Red;");
        }
        TopLabel.setVisible(true);
    }

    void disableGridE(){
        ObservableList<Node> children = EGrid.getChildren();
        for(Node node: children){
            if(node instanceof ToggleButton){
                ToggleButton tb = (ToggleButton) node;
                if(!tb.isDisabled()){
                    tb.setDisable(true);
                }
            }
        }
    }

    void updateCircle(){
        Platform.runLater(()->{
            if (!myTurn) Circle.setFill(Color.RED);
            if (myTurn) Circle.setFill(Color.LIME);
        });
    }



}