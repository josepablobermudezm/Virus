/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virus.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import virus.model.CartaDto;
import virus.model.JugadorDto;
import virus.util.FlowController;

/**
 * FXML Controller class
 *
 * @author Jose Pablo Bermudez
 */
public class InicioController extends Controller implements Initializable {

    @FXML
    private ImageView omg;
    @FXML
    private TextField txtIP;
    @FXML
    private TextField txtServidor;
    @FXML
    private TextField txtJugador;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image imgLogo;
        try {
            imgLogo = new Image("virus/resources/fondo2.png");
            omg.setImage(imgLogo);
        } catch (Exception e) {
        }
    }    

    @Override
    public void initialize() {
        if(!txtIP.getText().isEmpty() && !txtJugador.getText().isEmpty() && !txtServidor.getText().isEmpty()){
            enviarTexto(txtJugador.getText(), txtIP.getText(), txtServidor.getText());
        }
    }

    @FXML
    private void Salir(MouseEvent event) {
        FlowController.getInstance().goView("Menu");
    }

    @FXML
    private void Jugar(MouseEvent event) {
        
    }
    
    public static void enviarTexto(String nombre, String IP_Jugador, String IP_Servidor) {
        Socket socket;
        DataOutputStream mensaje;
        DataInputStream respuesta;
        try {
            socket = new Socket(IP_Servidor, 44440);
            mensaje = new DataOutputStream(socket.getOutputStream());
            respuesta = new DataInputStream(socket.getInputStream());
            System.out.println("Connected Text!");
            //Enviamos un mensaje
            mensaje.writeUTF("jugador");

            String mensajeRecibido = respuesta.readUTF();//Leemos respuesta
            System.out.println(mensajeRecibido);
            socket.close();
            enviarObjetos(nombre,IP_Jugador,IP_Servidor);
            //Cerramos la conexión
        } catch (UnknownHostException e) {
            System.out.println("El host no existe o no está activo.");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void enviarObjetos(String nombre, String IP_Jugador, String IP_Servidor) {
        try {
            // need host and port, we want to connect to the ServerSocket at port 7777
            Socket socket = new Socket(IP_Servidor, 44440);
            System.out.println("Connected Object!");

            // get the output stream from the socket.
            OutputStream outputStream = socket.getOutputStream();
            // create an object output stream from the output stream so we can send an object through it
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            // make a bunch of messages to send.
            System.out.println("Sending messages to the ServerSocket");
            objectOutputStream.writeObject(new JugadorDto(nombre, false, false, IP_Jugador, new ArrayList<CartaDto>(), new ArrayList<CartaDto>(),""));

            System.out.println("Closing socket and terminating program.");
            socket.close();
        } catch (IOException e) {
            System.out.println("Error enviando objetos");
        }
    }
    
}
