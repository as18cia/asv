package network;

import gamelogic.Pos;
import gamelogic.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;


public class Server extends Player {

   /* public ObjectOutputStream output;
    public  ObjectInputStream input;*/
    private ServerSocket server;
    private Socket connection;
    public boolean connected = true;

    public Server(){
        try {
            server = new ServerSocket(2912);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void WaitForConnection (){
        try {
            connection = server.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setupStream (){
        try {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*public void chatting(){
        String message;
        while (true) {
            try {
                message = (String) input.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }*/

    Object readObject1(){
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


    public void sendMessage(final String message){
        try {
            output.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CloseConnection(){
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
















}
