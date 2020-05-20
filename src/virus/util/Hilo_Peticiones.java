/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virus.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import virus.controller.JuegoController;
import virus.model.CartaDto;
import virus.model.JugadorDto;
import virus.model.PartidaDto;

/**
 *
 * @author Jose Pablo Bermudez
 */
public class Hilo_Peticiones extends Thread {

    private PartidaDto partidaDto;
    private ImageView imageView;
    private JugadorDto jugadorDto;
    private Label turno;

    public Hilo_Peticiones(PartidaDto partida, ImageView image, JugadorDto jugador, Label label) {
        super();
        partidaDto = partida;
        imageView = image;
        jugadorDto = jugador;
        turno = label;
    }

    DataInputStream entrada;
    DataOutputStream salida;
    Socket socket;
    ServerSocket serverSocket;
    String mensajeRecibido;

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(44440);
            System.out.println("Esperando una conexión...");
            socket = serverSocket.accept();
            System.out.println("Un cliente se ha conectado...");
            entrada = new DataInputStream(socket.getInputStream());
            System.out.println("Confirmando conexion al cliente....");
            mensajeRecibido = entrada.readUTF();
            System.out.println(mensajeRecibido);
            if ("cartaDesechada".equals(mensajeRecibido)) {
                DataInputStream respuesta2 = new DataInputStream(socket.getInputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(respuesta2);
                CartaDto carta = (CartaDto) objectInputStream.readObject();
                partidaDto.getDesechadas().add(carta);
                Platform.runLater(() -> {
                    imageView.setImage(new Image("virus/resources/" + carta.getImagen()));
                });
            } else if ("cambioTurno".equals(mensajeRecibido)) {
                String IP = entrada.readUTF();
                /*
                Si nuestro jugador es el que se ha recibido desde el servidor es porque 
                es el turno del mismo
                 */
                if (jugadorDto.getIP().equals(IP)) {
                    jugadorDto.setTurno(true);
                }
                
                String nombre = partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IP)).
                        findAny().get().getNombre();
                
                Platform.runLater(() -> {
                     turno.setText(nombre);
                });
               
                System.out.println("Cambio de Turno: "+nombre);
            }
            serverSocket.close();
            Hilo_Peticiones hilo = new Hilo_Peticiones(partidaDto, imageView, jugadorDto, turno);
            hilo.start();
        } catch (IOException IO) {
            System.out.println(IO.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JuegoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
