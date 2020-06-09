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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
    String nombreGanador = "";
    private Boolean inmune = false;
    public static String estado = "";

    @Override
    public void run() {
        while (!mensajeRecibido.equals("partidaFinalizada")) {
            try {
                serverSocket = new ServerSocket(44440);
                socket = serverSocket.accept();
                entrada = new DataInputStream(socket.getInputStream());
                mensajeRecibido = entrada.readUTF();
                if (null != mensajeRecibido) {
                    switch (mensajeRecibido) {
                        case "cartaDesechada": {
                            DataInputStream respuesta2 = new DataInputStream(socket.getInputStream());
                            ObjectInputStream objectInputStream = new ObjectInputStream(respuesta2);
                            CartaDto carta = (CartaDto) objectInputStream.readObject();
                            partidaDto.getDesechadas().add(carta);
                            Platform.runLater(() -> {
                                imageView.setImage(new Image("virus/resources/" + carta.getImagen()));
                            });
                            //IniciarHilo();
                            break;
                        }
                        case "cambioTurno":
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
                            break;
                        case "movimientoJugador": {
                            DataInputStream input;
                            input = new DataInputStream(socket.getInputStream());
                            String padre = input.readUTF();
                            String hijo = input.readUTF();
                            String IPJugador = input.readUTF();
                            DataInputStream respuesta2 = new DataInputStream(socket.getInputStream());
                            ObjectInputStream objectInputStream = new ObjectInputStream(respuesta2);
                            CartaDto carta = (CartaDto) objectInputStream.readObject();
                            JugadorDto jugadorAux = partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPJugador)).findAny().get();
                            System.out.println("ESTADO 1 " + estado);
                            switch (hijo) {
                                case "0":
                                    System.out.println("ESTADO 2 SWITCH 1.0" + estado);
                                    CambioEstado(carta, jugadorAux.getCartas1());
                                    System.out.println("ESTADO 2 SWITCH 1.1" + estado);
                                    jugadorAux.getCartas1().add(carta);
                                    break;
                                case "1":
                                    System.out.println("ESTADO 2 SWITCH 2.0" + estado);
                                    CambioEstado(carta, jugadorAux.getCartas2());
                                    System.out.println("ESTADO 2 SWITCH 2.1" + estado);
                                    jugadorAux.getCartas2().add(carta);
                                    break;
                                case "2":
                                    System.out.println("ESTADO 2 SWITCH 3.0" + estado);
                                    CambioEstado(carta, jugadorAux.getCartas3());
                                    System.out.println("ESTADO 2 SWITCH 3.1" + estado);
                                    jugadorAux.getCartas3().add(carta);
                                    break;
                                case "3":
                                    System.out.println("ESTADO 2 SWITCH 4.0" + estado);
                                    CambioEstado(carta, jugadorAux.getCartas4());
                                    System.out.println("ESTADO 2 SWITCH 4.1" + estado);
                                    jugadorAux.getCartas4().add(carta);
                                    break;
                                case "4":
                                    System.out.println("ESTADO 2 SWITCH 5.0" + estado);
                                    CambioEstado(carta, jugadorAux.getCartas5());
                                    System.out.println("ESTADO 2 SWITCH 5.1" + estado);
                                    jugadorAux.getCartas5().add(carta);
                                    break;
                                default:
                                    break;
                            }
                            System.out.println("ESTADO 3" + estado);
                            /*
                            Preguntamos si ya termino el juegoS
                             */
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
                            //Introduce las cartas jugadas en las vistas de los usuarios

                            anchorPane.getChildren().forEach((t) -> {
                                if (t.getId() != null && t.getId().equals(padre)) {
                                    int i = Integer.valueOf(hijo);
                                    System.out.println("ESTADO XDD" + estado);
                                    System.out.println("ESTADO XDD2.00" + estado);
                                    Pane pane = ((Pane) ((HBox) t).getChildren().get(i));
                                    if (inmune) {
                                        pane.setRotate(90.0);
                                        pane.setLayoutX(pane.getLayoutX() + 25.0);
                                        inmune = false;
                                    }

                                    ImageView imageAux = new ImageView("virus/resources/" + carta.getImagen());
                                    //imageAux.setRotate(90.0);
                                    imageAux.setFitHeight(81);
                                    imageAux.setFitWidth(58);
                                    imageAux.setTranslateX(0);
                                    imageAux.setTranslateY(0);
                                    switch (hijo) {
                                        case "0":
                                            imageAux.setLayoutY(pane.getLayoutY() + (jugadorAux.getCartas1().size() - 1) * 25);
                                            break;
                                        case "1":
                                            imageAux.setLayoutY(pane.getLayoutY() + (jugadorAux.getCartas2().size() - 1) * 25);
                                            break;
                                        case "2":
                                            imageAux.setLayoutY(pane.getLayoutY() + (jugadorAux.getCartas3().size() - 1) * 25);
                                            break;
                                        case "3":
                                            imageAux.setLayoutY(pane.getLayoutY() + (jugadorAux.getCartas4().size() - 1) * 25);
                                            break;
                                        case "4":
                                            imageAux.setLayoutY(pane.getLayoutY() + (jugadorAux.getCartas5().size() - 1) * 25);
                                            break;
                                        default:
                                            break;
                                    }
                                    Platform.runLater(() -> {
                                        pane.getChildren().add(imageAux);
                                    });
                                    System.out.println("ESTADO 5" + estado);
                                    if (estado.equals("Curado") || estado.equals("Inmunizado")) {
                                        System.out.println("ESTADO 6" + estado);
                                        System.out.println("PLOKKKKKKKKKKKKKKKKKKKKKKKK");
                                        new Mensaje().show(Alert.AlertType.INFORMATION, "Información de Juego", "Órgano curado");

                                        HiloEstado hilo = new HiloEstado();
                                        hilo.correrHilo(pane);
                                    }
                                    System.out.println("ESTADO 7" + estado);

                                }
                            });
                            System.out.println("ESTADO 8" + estado);
                            /*anchorPane.getChildren().forEach(t -> {
                                if (t.getId() != null && t.getId().equals(padre)) {
                                    int i = Integer.valueOf(hijo);
                                     System.out.println("PLOKKKKKKKKKKKKKKKKKKKKKKKK AASASASS");
                                    Platform.runLater(() -> {
                                        Pane pane = ((Pane) ((HBox) t).getChildren().get(i));
                                        System.out.println("ESTADO :  "+ estado);
                                        if (estado.equals("Curado") || estado.equals("Inmunizado")) {
                                            System.out.println("PLOKKKKKKKKKKKKKKKKKKKKKKKK");
                                            new Mensaje().show(Alert.AlertType.INFORMATION,"Información de Juego", "Órgano curado");
                                            
                                            HiloEstado hilo = new HiloEstado();
                                            hilo.correrHilo(pane);
                                        }else{
                                            System.out.println("XDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
                                        }
                                    });
                                }
                            });*/

                            estado = "";

                            if (cont == 4) {
                                findePartida = true;
                                mensajeRecibido = "partidaFinalizada";
                                nombreGanador = jugadorAux.getNombre();
                            } else if (jugadorAux.getIP().equals(jugadorDto.getIP())) {
                                jugadorAux.setMazo(jugadorDto.getMazo());
                                jugadorAux.setTurno(jugadorDto.getTurno());
                                jugadorDto = jugadorAux;
                                AppContext.getInstance().set("JugadorDto", jugadorAux);
                            }
                            /*else {
                                jugadorDto.setTurno(false);
                                AppContext.getInstance().set("JugadorDto", jugadorDto);
                            }*/
                            break;
                        }

                        case "partidaFinalizada":
                            break;
                        default:
                            break;
                    }
                }
                serverSocket.close();

            } catch (IOException IO) {
                System.out.println(IO.getMessage());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(JuegoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Platform.runLater(() -> {
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "¡VICTORIA!", this.imageView.getScene().getWindow(), nombreGanador + " ha ganado el juego");
            FlowController.getInstance().goView("Inicio");
        });
        //Cierra todos los procesos que queden pendientes
    }

    public void CambioEstado(CartaDto carta, ArrayList<CartaDto> mazo) {
        if ((carta.getTipoCarta().equals("Medicina") || carta.getTipoCarta().equals("Medicina_Comodin")) && mazo.stream().filter(x -> x.getTipoCarta().equals("Medicina")
                || x.getTipoCarta().equals("Medicina_Comodin")).count() == 0 && mazo.stream().filter(x -> x.getTipoCarta().equals("Virus")
                || x.getTipoCarta().equals("Virus_Comodin")).count() == 0) {//Vacunar
            estado = "Vacunado";
            mazo.get(0).setEstado("Vacunado");
            System.out.println("estado del organo ahora es Vacunado");
            /*
             *cambia el estado de la carta a vacunado y ahora se necesitan 2 virus para infectar el organo
             */
        } else if ((carta.getTipoCarta().equals("Medicina") || carta.getTipoCarta().equals("Medicina_Comodin")) && mazo.stream().filter(x -> x.getTipoCarta().equals("Virus")
                || x.getTipoCarta().equals("Virus_Comodin")).count() == 1) {//Curar
            mazo.get(0).setEstado("Curado");
            ArrayList removidas = (ArrayList<CartaDto>) mazo.stream().
                    filter(x -> x.getTipoCarta().equals("Medicina") || x.getTipoCarta().
                    equals("Medicina_Comodin") || x.getTipoCarta().equals("Virus") || x.getTipoCarta().equals("Virus_Comodin")).collect(Collectors.toList());
            estado = "Curado";
            mazo.removeAll(removidas);
            System.out.println(mazo.size());
            System.out.println("estado del organo ahora es Curado");
            /*
             *hay un virus en el organo, entonces una vez que ponemos la medicina, se mandan ambas cartas a la pila de descarte
             */
        } else if ((carta.getTipoCarta().equals("Medicina") || carta.getTipoCarta().equals("Medicina_Comodin")) && mazo.stream().filter(x -> x.getTipoCarta().equals("Medicina")
                || x.getTipoCarta().equals("Medicina_Comodin")).count() == 1) {//Inmunizar
            mazo.get(0).setEstado("Inmunizado");
            inmune = true;
            estado = "Inmunizado";
            System.out.println("estado del organo ahora es Inmunizado");

            ArrayList removidas = (ArrayList<CartaDto>) mazo.stream().
                    filter(x -> x.getTipoCarta().equals("Medicina") || x.getTipoCarta().
                    equals("Medicina_Comodin")).collect(Collectors.toList());
            mazo.removeAll(removidas);

            /*si ya el órgano cuenta con una medicina, esta segunda medicina logrará
             *proteger para siempre contra el ataque de cualquier virus y no podrá ser destruido ni
             *afectado por cartas de tratamiento. Cuando el órgano se inmuniza las cartas de medicina
             *se giran 90 grados sobre el órgano para indicar que está inmune.
             */
        } else if ((carta.getTipoCarta().equals("Virus_Comodin") || carta.getTipoCarta().equals("Virus")) && mazo.stream().filter(x -> x.getTipoCarta().equals("Virus")
                || x.getTipoCarta().equals("Virus_Comodin")).count() == 0 && mazo.stream().filter(x -> x.getTipoCarta().equals("Medicina")
                || x.getTipoCarta().equals("Medicina_Comodin")).count() == 0) {//Infectar
            mazo.get(0).setEstado("Infectado");
            estado = "Infectado";
            System.out.println("estado del organo ahora es Infectado");
            //se cambia el estado del organo a infectado y
        } else if ((carta.getTipoCarta().equals("Virus_Comodin") || carta.getTipoCarta().equals("Virus")) && mazo.stream().filter(x -> x.getTipoCarta().equals("Virus")
                || x.getTipoCarta().equals("Virus_Comodin")).count() == 1) {//Extirpar
            mazo.get(0).setEstado("Extirpado");
            estado = "Extirpado";
            System.out.println("estado del organo ahora es Extripado");
            /*si un segundo virus es colocado sobre un órgano ya infectado, este órgano
             *será destruido y las tres cartas (el órgano y los 2 virus) serán enviadas a la pila de
             *descarte.
             */
        } else if ((carta.getTipoCarta().equals("Virus_Comodin") || carta.getTipoCarta().equals("Virus")) && mazo.stream().filter(x -> x.getTipoCarta().equals("Medicina")
                || x.getTipoCarta().equals("Medicina_Comodin")).count() == 1) {//Destruir vacuna
            mazo.get(0).setEstado("Sana");
            estado = "Sana";
            System.out.println("estado del organo ahora es Sana después de destruir");
            /*
             *si sobre un órgano se encuentra una carta de medicina y se le aplica
             *un virus del mismo color, ambas cartas (la medicina y el virus) serán enviadas a la pila
             *de descarte
             */
        } else {
            //estado = "";
        }
    }

    public void IniciarHilo() {
        Hilo_Peticiones hilo = new Hilo_Peticiones(partidaDto, imageView, jugadorDto, turno, anchorPane);
        hilo.start();
    }
}
