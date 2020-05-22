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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private AnchorPane anchorPane;

    public Hilo_Peticiones(PartidaDto partida, ImageView image, JugadorDto jugador, Label label, AnchorPane anchorPane) {
        super();
        partidaDto = partida;
        imageView = image;
        jugadorDto = jugador;
        turno = label;
        this.anchorPane = anchorPane;
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
                 *    Si nuestro jugador es el que se ha recibido desde el servidor es porque 
                 *   es el turno del mismo
                 */
                if (jugadorDto.getIP().equals(IP)) {
                    jugadorDto.setTurno(true);
                } else {
                    jugadorDto.setTurno(false);
                }

                String nombre = partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IP)).
                        findAny().get().getNombre();

                Platform.runLater(() -> {
                    turno.setText(nombre);
                });

                System.out.println("Cambio de Turno: " + IP);
            } else if ("movimientoJugador".equals(mensajeRecibido)) {
                DataInputStream input;
                input = new DataInputStream(socket.getInputStream());

                String padre = input.readUTF();
                String hijo = input.readUTF();
                String IPJugador = input.readUTF();

                DataInputStream respuesta2 = new DataInputStream(socket.getInputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(respuesta2);
                CartaDto carta = (CartaDto) objectInputStream.readObject();

                partidaDto.getJugadores().stream().forEach(x->System.out.print(x.getIP() + " IP Jugadores"));
                System.out.println(IPJugador + " ip recibida");
                
                JugadorDto jugadorAux = partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPJugador)).findAny().get();
                
                System.out.println(jugadorAux.getIP() + " jugadorAux ip");

                switch (hijo) {
                    case "0":
                        jugadorAux.getCartas1().add(carta);
                        break;
                    case "1":
                        jugadorAux.getCartas2().add(carta);
                        break;
                    case "2":
                        jugadorAux.getCartas3().add(carta);
                        break;
                    case "3":
                        jugadorAux.getCartas4().add(carta);
                        break;
                    case "4":
                        //jugadorAux.getCartas5().add(carta);
                        break;
                    default:
                        break;
                }
                //pregunta que si el jugador es el mismo que encontramos, el que hizo el movimiento, entonces actualizamos las cartas
                if(jugadorAux.getIP().equals(jugadorDto.getIP())){
                    AppContext.getInstance().set("JugadorDto", jugadorAux);
                }

                anchorPane.getChildren().forEach((t) -> {
                    if (t.getId() != null && t.getId().equals(padre)) {
                        int i = Integer.valueOf(hijo);
                        Platform.runLater(() -> {
                            ((ImageView) ((VBox) ((HBox) t).getChildren().get(i)).getChildren().get(0)).
                                    setImage(new Image("virus/resources/" + carta.getImagen()));
                        });
                    }
                });
            }
            serverSocket.close();
            Hilo_Peticiones hilo = new Hilo_Peticiones(partidaDto, imageView, jugadorDto, turno, anchorPane);
            hilo.start();
        } catch (IOException IO) {
            System.out.println(IO.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JuegoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
