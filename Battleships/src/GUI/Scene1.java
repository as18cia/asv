package GUI;


import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.*;

import java.io.IOException;
import java.net.InetAddress;

public class Scene1 {

    @FXML
    private Button host;


    @FXML
    public void launchServer(){
        try {
            Stage stage = (Stage) host.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("Waiting.fxml"));
            for (Node n :root.getChildrenUnmodifiable()) {
                if(n instanceof AnchorPane){
                    for(Node m: ((AnchorPane) n).getChildren()){
                        if(m instanceof Label){
                            ((Label) m).setText(InetAddress.getLocalHost().getHostAddress());
                        }
                    }
                }
            }
            stage.setScene(new Scene(root));
            stage.show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Server server = new Server();
                    server.WaitForConnection();
                    server.setupStream();


                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("placeShips.fxml"));
                                Parent root1 = loader.load();
                                placeShips C2 = loader.getController();
                                C2.player = server;
                                C2.startThread1();
                                stage.setScene(new Scene(root1));
                                stage.setTitle("preparation phase");
                                stage.show();


                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }).start();





        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void launchClient(){
        try {
            Stage stage = (Stage) host.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("startClient.fxml"));
            stage.setScene(new Scene(root, 650, 400));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
