package GUI;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import network.*;
import java.io.IOException;


public class startClient {


    @FXML
    private TextField TField;

    @FXML
    void launchClient (){
        String ip = TField.getText();
        Client client = new Client(ip);
        client.setupConnection();
        client.setupStreams();

        try {
            Stage stage = (Stage) TField.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("placeShips.fxml"));
            Parent root1 = loader.load();
            placeShips c = loader.getController();
            c.player = client;
            c.startThread1();


            stage.setScene(new Scene(root1));
            stage.setTitle("preparation phase");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
