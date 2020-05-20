/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virus.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static virus.controller.InicioController.enviarObjetos;
import virus.controller.JuegoController;
import virus.model.CartaDto;
import virus.model.JugadorDto;
import virus.model.PartidaDto;

/**
 *
 * @author Jose Pablo Bermudez
 */
public class Hilo_Peticiones extends Thread {

    public PartidaDto partidaDto;
    public ImageView imageView;

    public Hilo_Peticiones(PartidaDto partida, ImageView image) {
        super();
        partidaDto = partida;
        imageView = image;
    }

    DataInputStream entrada;
    DataOutputStream salida;
    Socket socket;
    ServerSocket serverSocket;
    String mensajeRecibido;

    @Override
    public void run() {
        while (true) {
            Platform.runLater(() -> {
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
                    }
                    serverSocket.close();
                } catch (IOException IO) {
                    System.out.println(IO.getMessage());
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(JuegoController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }
}
