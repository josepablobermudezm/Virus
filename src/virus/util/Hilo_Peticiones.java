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
                            switch (hijo) {
                                case "0":
                                    jugadorAux.getCartas1().add(carta);
                                    CambioEstado(carta, jugadorAux.getCartas1());
                                    break;
                                case "1":
                                    jugadorAux.getCartas2().add(carta);
                                    CambioEstado(carta, jugadorAux.getCartas2());
                                    break;
                                case "2":
                                    jugadorAux.getCartas3().add(carta);
                                    CambioEstado(carta, jugadorAux.getCartas3());
                                    break;
                                case "3":
                                    jugadorAux.getCartas4().add(carta);
                                    CambioEstado(carta, jugadorAux.getCartas4());
                                    break;
                                case "4":
                                    jugadorAux.getCartas5().add(carta);
                                    CambioEstado(carta, jugadorAux.getCartas5());
                                    break;
                                default:
                                    break;
                            }
                            /*
                            Preguntamos si ya termino el juegoS
                             */
                            int cont = 0;
                            if ((!jugadorAux.getCartas1().isEmpty()) ? jugadorAux.getCartas1().get(0).getEstado().equals("Sano") : false) {
                                cont++;
                            }
                            if ((!jugadorAux.getCartas2().isEmpty()) ? jugadorAux.getCartas2().get(0).getEstado().equals("Sano") : false) {
                                cont++;
                            }
                            if ((!jugadorAux.getCartas3().isEmpty()) ? jugadorAux.getCartas3().get(0).getEstado().equals("Sano") : false) {
                                cont++;
                            }
                            if ((!jugadorAux.getCartas4().isEmpty()) ? jugadorAux.getCartas4().get(0).getEstado().equals("Sano") : false) {
                                cont++;
                            }
                            if ((!jugadorAux.getCartas5().isEmpty()) ? jugadorAux.getCartas5().get(0).getEstado().equals("Sano") : false) {
                                cont++;
                            }
                            //Introduce las cartas jugadas en las vistas de los usuarios

                            anchorPane.getChildren().forEach((t) -> {
                                if (t.getId() != null && t.getId().equals(padre)) {
                                    int i = Integer.valueOf(hijo);
                                    Pane pane = ((Pane) ((HBox) t).getChildren().get(i));

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

                                    if (estado.equals("Sano") || estado.equals("Inmunizado") || estado.equals("Extirpado")) {
                                        switch (estado) {
                                            case "Inmunizado":
                                                Platform.runLater(() -> {
                                                    pane.setRotate(90.0);
                                                    pane.setLayoutX(pane.getLayoutX() + 25.0);
                                                    pane.getChildren().remove(pane.getChildren().size() - 1);
                                                    pane.getChildren().remove(pane.getChildren().size() - 1);
                                                    new Mensaje().show(Alert.AlertType.INFORMATION, "Información de Juego", "Inmunizado");
                                                });
                                                break;
                                            case "Extirpado":
                                                Platform.runLater(() -> {
                                                    pane.getChildren().remove(pane.getChildren().size() - 1);
                                                    pane.getChildren().remove(pane.getChildren().size() - 1);
                                                    pane.getChildren().remove(pane.getChildren().size() - 1);
                                                    new Mensaje().show(Alert.AlertType.INFORMATION, "Información de Juego", "Extirpado");
                                                });
                                                break;
                                            case "Sano":
                                                Platform.runLater(() -> {
                                                    pane.getChildren().remove(pane.getChildren().size() - 1);
                                                    pane.getChildren().remove(pane.getChildren().size() - 1);
                                                    new Mensaje().show(Alert.AlertType.INFORMATION, "Información de Juego", "Sano");
                                                });
                                                break;
                                        }

                                    }

                                }
                            });

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
        if (mazo.stream().filter(x -> x.getTipoCarta().equals("Medicina")
                || x.getTipoCarta().equals("Medicina_Comodin")).count() == 1
                && mazo.stream().filter(x -> x.getTipoCarta().equals("Virus")
                || x.getTipoCarta().equals("Virus_Comodin")).count() == 0) {//Vacunar
            estado = "Vacunado";
            mazo.get(0).setEstado("Vacunado");
            /*
             *cambia el estado de la carta a vacunado y ahora se necesitan 2 virus para infectar el organo
             */
        } else if (mazo.stream().filter(x -> x.getTipoCarta().equals("Medicina")
                || x.getTipoCarta().equals("Medicina_Comodin")).count() == 1
                && mazo.stream().filter(x -> x.getTipoCarta().equals("Virus")
                || x.getTipoCarta().equals("Virus_Comodin")).count() == 1) {//Curar
            mazo.get(0).setEstado("Sano");

            ArrayList<CartaDto> removidas = (ArrayList<CartaDto>) mazo.stream().
                    filter(x -> x.getTipoCarta().equals("Medicina") || x.getTipoCarta().
                    equals("Medicina_Comodin") || x.getTipoCarta().equals("Virus")
                    || x.getTipoCarta().equals("Virus_Comodin")).collect(Collectors.toList());
            estado = "Sano";
            removidas.stream().forEach((t) -> {
                System.out.println("TIPO " + t.getTipoCarta());
            });
            mazo.removeAll(removidas);
            /*
             *hay un virus en el organo, entonces una vez que ponemos la medicina, se mandan ambas cartas a la pila de descarte
             */
        } else if (mazo.stream().filter(x -> x.getTipoCarta().equals("Medicina")
                || x.getTipoCarta().equals("Medicina_Comodin")).count() == 2
                && mazo.stream().filter(x -> x.getTipoCarta().equals("Virus")
                || x.getTipoCarta().equals("Virus_Comodin")).count() == 0) {//Inmunizar
            mazo.get(0).setEstado("Inmunizado");
            estado = "Inmunizado";

            ArrayList<CartaDto> removidas = (ArrayList<CartaDto>) mazo.stream().
                    filter(x -> x.getTipoCarta().equals("Medicina") || x.getTipoCarta().
                    equals("Medicina_Comodin")).collect(Collectors.toList());
            removidas.stream().forEach((t) -> {
                System.out.println("TIPO " + t.getTipoCarta());
            });
            mazo.removeAll(removidas);

            /*si ya el órgano cuenta con una medicina, esta segunda medicina logrará
             *proteger para siempre contra el ataque de cualquier virus y no podrá ser destruido ni
             *afectado por cartas de tratamiento. Cuando el órgano se inmuniza las cartas de medicina
             *se giran 90 grados sobre el órgano para indicar que está inmune.
             */
        } else if (mazo.stream().filter(x -> x.getTipoCarta().equals("Virus")
                || x.getTipoCarta().equals("Virus_Comodin")).count() == 1
                && mazo.stream().filter(x -> x.getTipoCarta().equals("Medicina")
                || x.getTipoCarta().equals("Medicina_Comodin")).count() == 0) {//Infectar
            mazo.get(0).setEstado("Infectado");
            estado = "Infectado";
            //se cambia el estado del organo a infectado y
        } else if (mazo.stream().filter(x -> x.getTipoCarta().equals("Medicina")
                || x.getTipoCarta().equals("Medicina_Comodin")).count() == 0
                && mazo.stream().filter(x -> x.getTipoCarta().equals("Virus")
                || x.getTipoCarta().equals("Virus_Comodin")).count() == 2) {//Extirpar
            mazo.get(0).setEstado("Extirpado");
            estado = "Extirpado";

            ArrayList<CartaDto> removidas = (ArrayList<CartaDto>) mazo.stream().
                    filter(x -> x.getTipoCarta().equals("Virus") || x.getTipoCarta().equals("Virus_Comodin")).
                    collect(Collectors.toList());
            removidas.stream().forEach((t) -> {
                System.out.println("TIPO " + t.getTipoCarta());
            });
            mazo.removeAll(removidas);

            /*si un segundo virus es colocado sobre un órgano ya infectado, este órgano
             *será destruido y las tres cartas (el órgano y los 2 virus) serán enviadas a la pila de
             *descarte.
             */
        } /*else if ((carta.getTipoCarta().equals("Virus_Comodin") || carta.getTipoCarta().equals("Virus")) && mazo.stream().filter(x -> x.getTipoCarta().equals("Medicina")
                || x.getTipoCarta().equals("Medicina_Comodin")).count() == 1) {//Destruir vacuna
            mazo.get(0).setEstado("Sano");
            estado = "Sano";

            ArrayList <CartaDto> removidas = (ArrayList<CartaDto>) mazo.stream().
                    filter(x -> x.getTipoCarta().equals("Medicina") || x.getTipoCarta().
                    equals("Medicina_Comodin") || x.getTipoCarta().equals("Virus") || x.getTipoCarta().equals("Virus_Comodin")).collect(Collectors.toList());
            removidas.stream().forEach(t -> {
                System.out.println("TIPO :"+t.getTipoCarta());
            });
            mazo.removeAll(removidas);
            /*
             *si sobre un órgano se encuentra una carta de medicina y se le aplica
             *un virus del mismo color, ambas cartas (la medicina y el virus) serán enviadas a la pila
             *de descarte
             
        } */ else {
            //estado = "";
        }
    }

    public void IniciarHilo() {
        Hilo_Peticiones hilo = new Hilo_Peticiones(partidaDto, imageView, jugadorDto, turno, anchorPane);
        hilo.start();
    }
}
