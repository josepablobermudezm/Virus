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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import virus.controller.JuegoController;
import static virus.controller.JuegoController.jugador;
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
    public static Boolean findePartida = false;

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
    String mensajeRecibido = "";

    @Override
    public void run() {
        System.out.println("HILO NO SE ESTÁ CERAAAAAAAAAAAAAAAAAAAANDO...............................");
        while (!mensajeRecibido.equals("partidaFinalizada")) {
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
                    //IniciarHilo();
                } else if ("cambioTurno".equals(mensajeRecibido)) {
                    String IP = entrada.readUTF();
                    /*
                 *    Si nuestro jugador es el que se ha recibido desde el servidor es porque 
                 *   es el turno del mismo
                     */
                    if (jugadorDto.getIP().equals(IP)) {
                        jugadorDto.setTurno(true);
                        Platform.runLater(() -> {
                            new Mensaje().show(Alert.AlertType.INFORMATION, "Información de juego", "Es tu turno");
                        });
                    } else {
                        jugadorDto.setTurno(false);
                    }

                    String nombre = partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IP)).
                            findAny().get().getNombre();

                    Platform.runLater(() -> {
                        turno.setText(nombre);
                    });

                    System.out.println("Cambio de Turno: " + IP);
                    //IniciarHilo();
                } else if ("movimientoJugador".equals(mensajeRecibido)) {
                    DataInputStream input;
                    input = new DataInputStream(socket.getInputStream());

                    String padre = input.readUTF();
                    String hijo = input.readUTF();
                    String IPJugador = input.readUTF();

                    DataInputStream respuesta2 = new DataInputStream(socket.getInputStream());
                    ObjectInputStream objectInputStream = new ObjectInputStream(respuesta2);
                    CartaDto carta = (CartaDto) objectInputStream.readObject();

                    JugadorDto jugadorAux = partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPJugador)).findAny().get();

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
                            jugadorAux.getCartas5().add(carta);
                            break;
                        default:
                            break;
                    }
                    int cont = 0;
                    if ((!jugadorAux.getCartas1().isEmpty()) ? jugadorAux.getCartas1().get(0).getEstado().equals("Sana") : false) {
                        cont++;
                    }
                    if ((!jugadorAux.getCartas2().isEmpty()) ? jugadorAux.getCartas2().get(0).getEstado().equals("Sana") : false) {
                        cont++;
                    }
                    if ((!jugadorAux.getCartas3().isEmpty()) ? jugadorAux.getCartas3().get(0).getEstado().equals("Sana") : false) {
                        cont++;
                    }
                    if ((!jugadorAux.getCartas4().isEmpty()) ? jugadorAux.getCartas4().get(0).getEstado().equals("Sana") : false) {
                        cont++;
                    }
                    if ((!jugadorAux.getCartas5().isEmpty()) ? jugadorAux.getCartas5().get(0).getEstado().equals("Sana") : false) {
                        cont++;
                    }
                    if (cont == 4) {
                        findePartida = true;
                        mensajeRecibido = "partidaFinalizada";
                        System.out.println(jugadorAux.getNombre() + " Haz ganado el juego");
                        Platform.runLater(() -> {
                            new Mensaje().showModal(Alert.AlertType.INFORMATION, "¡VICTORIA!", this.imageView.getScene().getWindow(), jugadorAux.getNombre() + " Ha ganado el juego");
                        });
                    } else if (jugadorAux.getIP().equals(jugadorDto.getIP())) {
                        jugadorAux.setMazo(jugadorDto.getMazo());
                        jugadorAux.setTurno(jugadorDto.getTurno());
                        AppContext.getInstance().set("JugadorDto", jugadorAux);
                    }
                    System.out.println(cont + " ..........................................Contador para ganar el juego ");
                    //pregunta que si el jugador es el mismo que encontramos, el que hizo el movimiento, entonces actualizamos las cartas

                    anchorPane.getChildren().forEach((t) -> {
                        if (t.getId() != null && t.getId().equals(padre)) {
                            int i = Integer.valueOf(hijo);
                            Platform.runLater(() -> {
                                ((ImageView) ((Pane) ((HBox) t).getChildren().get(i)).getChildren().get(0)).
                                        setImage(new Image("virus/resources/" + carta.getImagen()));
                            });
                        }
                    });
                    //IniciarHilo();
                } else if ("partidaFinalizada".equals(mensajeRecibido)) {
                    System.out.println("Partida Finalizada");
                }
                serverSocket.close();

            } catch (IOException IO) {
                System.out.println(IO.getMessage());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(JuegoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("..................................................................................................AAAA");
    }

    public void IniciarHilo() {
        Hilo_Peticiones hilo = new Hilo_Peticiones(partidaDto, imageView, jugadorDto, turno, anchorPane);
        hilo.start();
    }
}
