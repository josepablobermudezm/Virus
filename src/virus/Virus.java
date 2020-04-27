/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import virus.util.FlowController;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import virus.model.CartaDto;

/**
 *
 * @author Jose Pablo Bermudez
 */
public class Virus extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        // need host and port, we want to connect to the ServerSocket at port 7777
        Socket socket = new Socket("25.101.202.236", 44440);
        System.out.println("Connected!");

        // get the output stream from the socket.
        OutputStream outputStream = socket.getOutputStream();
        // create an object output stream from the output stream so we can send an object through it
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        // make a bunch of messages to send.
        List<CartaDto> cartas = new ArrayList<>();
        cartas.add(new CartaDto("Virus","Roja", "XD.png","Desechada"));
        cartas.add(new CartaDto("Organo","Roja", "HOLA.png","Mazo"));
        cartas.add(new CartaDto("Medicina","Verde", "ASDF.png","Jugada"));
        cartas.add(new CartaDto("Tratamiento","Azul", "PLOK.png","Desechada"));

        System.out.println("Sending messages to the ServerSocket");
        objectOutputStream.writeObject(cartas);

        System.out.println("Closing socket and terminating program.");
        socket.close();
        
       
         // don't need to specify a hostname, it will be the current machine
        
         
         /*ServerSocket ss = new ServerSocket(44440);
        System.out.println("ServerSocket awaiting connections...");
        Socket socket = ss.accept(); // blocking call, this will wait until a connection is attempted on this port.
        System.out.println("Connection from " + socket + "!");

        // get the input stream from the connected socket
        InputStream inputStream = socket.getInputStream();
        // create a DataInputStream so we can read data from it.
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        // read the list of messages from the socket
        List<CartaDto> listOfMessages = (List<CartaDto>) objectInputStream.readObject();
        System.out.println("Received [" + listOfMessages.size() + "] messages from: " + socket);
        // print out the text of every message
        System.out.println("All messages:");
        listOfMessages.forEach((msg)-> System.out.println(msg.getColor()));

        System.out.println("Closing sockets.");
        ss.close();
        socket.close();
        */
        FlowController.getInstance().InitializeFlow(stage,null);
        FlowController.getInstance().goMain();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
