package network;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import gamelogic.Pos;
import gamelogic.*;



public class Client extends Player{

    /*public ObjectOutputStream output;
    public ObjectInputStream input;*/
    private Socket connection;
    private String hostIp;
    public boolean connected = true;

   public Client(String ip){
        this.hostIp = ip;
   }


    public void setupConnection(){
        try {
            connection = new Socket(hostIp, 2912);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setupStreams (){
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    Object readObject(){
        Object o = new Object();
        try {
            return input.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return o;
    }


    public void sendCoordinates (Coordinates[] Coordinates){
        try {
            output.writeObject(Coordinates);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendReport(Pos[] positions){
        try {
            output.writeObject(positions);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message){
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void CloseConnection() {
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
