/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virus;

import javafx.application.Application;
import javafx.stage.Stage;
import virus.util.FlowController;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import virus.model.CartaDto;
import virus.model.JugadorDto;

/**
 *
 * @author Jose Pablo Bermudez
 */
public class Virus extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // need host and port, we want to connect to the ServerSocket at port 7777
        
        /*Socket socket = new Socket("25.101.202.236", 44440);
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
        socket.close();*/
        
       
         // don't need to specify a hostname, it will be the current machine

        FlowController.getInstance().InitializeFlow(stage, null);
        FlowController.getInstance().goMain();
        enviarTexto();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    public static void enviarTexto() {
        Boolean exito = false;
        Socket socket;
        DataOutputStream mensaje;
        DataInputStream respuesta;
        try {
            socket = new Socket("25.3.190.217", 44440);
            mensaje = new DataOutputStream(socket.getOutputStream());
            respuesta = new DataInputStream(socket.getInputStream());
            System.out.println("Connected Text!");
            //Enviamos un mensaje
            //mensaje.writeUTF("Hola CARLOOOS!!");
            mensaje.writeUTF("jugador");

            String mensajeRecibido = respuesta.readUTF();//Leemos respuesta
            System.out.println(mensajeRecibido);
            socket.close();
            enviarObjetos();
            //Cerramos la conexión
            exito = true;
        } catch (UnknownHostException e) {
            System.out.println("El host no existe o no está activo.");
        } catch (IOException e) {
            System.out.println(e);
        }

        /*if (exito) {
            enviarObjetos();
        }*/
    }

    public static void enviarObjetos() {
        try {
            // need host and port, we want to connect to the ServerSocket at port 7777
            Socket socket = new Socket("25.3.190.217", 44440);
            System.out.println("Connected Object!");

            // get the output stream from the socket.
            OutputStream outputStream = socket.getOutputStream();
            // create an object output stream from the output stream so we can send an object through it
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

            // make a bunch of messages to send.
            /*List<JugadorDto> jugadores = new ArrayList<>();
        //jugadores.add();
         jugadores.add(new CartaDto("Organo","Roja", "HOLA.png","Mazo"));
        jugadores.add(new CartaDto("Medicina","Verde", "ASDF.png","Jugada"));
        jugadores.add(new CartaDto("Tratamiento","Azul", "PLOK.png","Desechada"));*/
            System.out.println("Sending messages to the ServerSocket");
            objectOutputStream.writeObject(new JugadorDto("Carlos", true, false, "25.101.202.236", new ArrayList<CartaDto>(), new ArrayList<CartaDto>()));

            System.out.println("Closing socket and terminating program.");
            socket.close();
        } catch (IOException e) {
            System.out.println("Error enviando objetos");
        }

    }
}
