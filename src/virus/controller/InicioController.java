/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virus.controller;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import virus.model.CartaDto;
import virus.model.JugadorDto;
import virus.util.AppContext;
import virus.util.FlowController;
import virus.util.Hilo;

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

    }

    @FXML
    private void Salir(MouseEvent event) {
        FlowController.getInstance().goView("Menu");
    }

    @FXML
    private void Jugar(MouseEvent event) {
        if (!txtIP.getText().isEmpty() && !txtJugador.getText().isEmpty() && !txtServidor.getText().isEmpty()) {
            enviarTexto(txtJugador.getText(), txtIP.getText(), txtServidor.getText());
            
        }
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
            enviarObjetos(nombre, IP_Jugador, IP_Servidor);
            //Cerramos la conexión
        } catch (UnknownHostException e) {
            System.out.println("El host no existe o no está activo.");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void enviarObjetos(String nombre, String IP_Jugador, String IP_Servidor) {
        JugadorDto jugador = new JugadorDto(nombre, false, false, IP_Jugador, new ArrayList<CartaDto>(), new ArrayList<CartaDto>(), "");
        AppContext.getInstance().set("JugadorDto", jugador);
        try {
            // need host and port, we want to connect to the ServerSocket at port 7777
            Socket socket = new Socket(IP_Servidor, 44440);
            System.out.println("Connected Object!");
            // get the output stream from the socket.
            OutputStream outputStream = socket.getOutputStream();
            InputStream respuesta = new DataInputStream(socket.getInputStream());
            // create an object output stream from the output stream so we can send an object through it
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            ObjectInputStream objectInputStream = new ObjectInputStream(respuesta);
            //ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            // make a bunch of messages to send.
            System.out.println("Sending messages to the ServerSocket");
            objectOutputStream.writeObject(jugador);
            ArrayList<CartaDto> cartas = (ArrayList<CartaDto>) objectInputStream.readObject();
            jugador.setMazo(cartas);
            System.out.println("Closing socket and terminating program.");
            socket.close();
            Hilo hilo = new Hilo();
            hilo.start();
            FlowController.getInstance().goViewInWindowTransparent("VistaCargando");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
