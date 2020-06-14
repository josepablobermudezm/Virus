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
    private ImageView imgDesechadas;
    private JugadorDto jugadorDto;
    private Label turno;
    private AnchorPane anchorPane;
    public static Boolean findePartida = false;
    private ArrayList<ImageView> mazoImg;

    public Hilo_Peticiones(PartidaDto partida, ImageView image, JugadorDto jugador, Label label, AnchorPane anchorPane, ArrayList<ImageView> mazoImg) {
        super();
        partidaDto = partida;
        imgDesechadas = image;
        jugadorDto = jugador;
        turno = label;
        this.anchorPane = anchorPane;
        this.mazoImg = mazoImg;
    }

    DataInputStream entrada;
    DataOutputStream salida;
    Socket socket;
    ServerSocket serverSocket;
    String mensajeRecibido = "";

    public static String estado = "";
    public static Pane paneAux1 = null;
    public static Pane paneAux2 = null;
    private static int valor = 0;
    private static HBox jug1 = null;
    private static HBox jug2 = null;
    private static String IPJ2;

    @Override
    public void run() {

        while (!mensajeRecibido.equals("partidaFinalizada")) {
            try {
                serverSocket = new ServerSocket(44440);
                socket = serverSocket.accept();
                entrada = new DataInputStream(socket.getInputStream());
                salida = new DataOutputStream(socket.getOutputStream());
                mensajeRecibido = entrada.readUTF();
                if (null != mensajeRecibido) {
                    switch (mensajeRecibido) {
                        case "cartaDesechada": {
                            DataInputStream respuesta2 = new DataInputStream(socket.getInputStream());
                            ObjectInputStream objectInputStream = new ObjectInputStream(respuesta2);
                            CartaDto carta = (CartaDto) objectInputStream.readObject();

                            if (carta.getTipoCarta().equals("Guante")) {
                                JugadorDto jugador = partidaDto.getJugadores().stream().
                                        filter(x -> x.getTurno()).findAny().get();
                                partidaDto.getJugadores().stream().forEach(x -> {
                                    if (!x.getIP().equals(jugador.getIP())) {

                                        if (jugadorDto.getIP().equals(x.getIP())) {
                                            jugadorDto.getMazo().clear();
                                            partidaDto.getDesechadas().addAll(x.getMazo());
                                            x.getMazo().clear();
                                            mazoImg.stream().forEach((t) -> {
                                                Platform.runLater(() -> {
                                                    t.setImage(null);
                                                });
                                            });
                                        }

                                    }

                                });
                                Platform.runLater(() -> {
                                    new Mensaje().show(Alert.AlertType.INFORMATION, "Información de Juego", "Tratamiento Guante de Látex aplicado");
                                });
                            } else if (carta.getTipoCarta().equals("Ladron")) {

                            }

                            partidaDto.getDesechadas().add(carta);
                            Platform.runLater(() -> {
                                imgDesechadas.setImage(new Image("virus/resources/" + carta.getImagen()));
                            });
                            break;
                        }
                        case "cambioTurno":
                            String IP = entrada.readUTF();
                            /*
                            *Si nuestro jugador es el que se ha recibido desde el servidor es porque
                            *es el turno del mismo
                             */
                            partidaDto.getJugadores().stream().forEach((t) -> {
                                if (!t.getIP().equals(IP)) {
                                    t.setTurno(false);
                                } else {
                                    t.setTurno(true);
                                }
                            });

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
                                    CambioEstado(jugadorAux.getCartas1());
                                    break;
                                case "1":
                                    jugadorAux.getCartas2().add(carta);
                                    CambioEstado(jugadorAux.getCartas2());
                                    break;
                                case "2":
                                    jugadorAux.getCartas3().add(carta);
                                    CambioEstado(jugadorAux.getCartas3());
                                    break;
                                case "3":
                                    jugadorAux.getCartas4().add(carta);
                                    CambioEstado(jugadorAux.getCartas4());
                                    break;
                                case "4":
                                    jugadorAux.getCartas5().add(carta);
                                    CambioEstado(jugadorAux.getCartas5());
                                    break;
                                default:
                                    break;
                            }
                            /*
                            Preguntamos si ya termino el juegoS
                             */
                            int cont = 0;
                            if ((!jugadorAux.getCartas1().isEmpty()) ? jugadorAux.getCartas1().get(0).getEstado().equals("Sano")
                                    || jugadorAux.getCartas1().get(0).getEstado().equals("Inmunizado")
                                    || jugadorAux.getCartas1().get(0).getEstado().equals("Vacunado") : false) {
                                cont++;
                            }
                            if ((!jugadorAux.getCartas2().isEmpty()) ? jugadorAux.getCartas2().get(0).getEstado().equals("Sano")
                                    || jugadorAux.getCartas2().get(0).getEstado().equals("Inmunizado")
                                    || jugadorAux.getCartas2().get(0).getEstado().equals("Vacunado") : false) {
                                cont++;
                            }
                            if ((!jugadorAux.getCartas3().isEmpty()) ? jugadorAux.getCartas3().get(0).getEstado().equals("Sano")
                                    || jugadorAux.getCartas3().get(0).getEstado().equals("Inmunizado")
                                    || jugadorAux.getCartas3().get(0).getEstado().equals("Vacunado") : false) {
                                cont++;
                            }
                            if ((!jugadorAux.getCartas4().isEmpty()) ? jugadorAux.getCartas4().get(0).getEstado().equals("Sano")
                                    || jugadorAux.getCartas4().get(0).getEstado().equals("Inmunizado")
                                    || jugadorAux.getCartas4().get(0).getEstado().equals("Vacunado") : false) {
                                cont++;
                            }
                            if ((!jugadorAux.getCartas5().isEmpty()) ? jugadorAux.getCartas5().get(0).getEstado().equals("Sano")
                                    || jugadorAux.getCartas5().get(0).getEstado().equals("Inmunizado")
                                    || jugadorAux.getCartas5().get(0).getEstado().equals("Vacunado") : false) {
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
                                                    pane.getChildren().get(pane.getChildren().size() - 1).setRotate(90);

                                                    pane.getChildren().get(pane.getChildren().size() - 2).setRotate(90);
                                                    new Mensaje().show(Alert.AlertType.INFORMATION, "Información de Juego", "Inmunizado");
                                                });
                                                break;
                                            case "Extirpado":
                                                Platform.runLater(() -> {
                                                    ImageView auxImg = (ImageView) pane.getChildren().get(pane.getChildren().size() - 1);
                                                    imgDesechadas.setImage(auxImg.getImage());
                                                    pane.getChildren().remove(pane.getChildren().size() - 1);
                                                    pane.getChildren().remove(pane.getChildren().size() - 1);
                                                    pane.getChildren().remove(pane.getChildren().size() - 1);
                                                    new Mensaje().show(Alert.AlertType.INFORMATION, "Información de Juego", "Extirpado");
                                                });
                                                break;
                                            case "Sano":
                                                Platform.runLater(() -> {
                                                    ImageView auxImg = (ImageView) pane.getChildren().get(pane.getChildren().size() - 1);
                                                    imgDesechadas.setImage(auxImg.getImage());
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
                                //findePartida = true;
                                mensajeRecibido = "partidaFinalizada";
                                jugadorDto = jugadorAux;
                                /*salida.writeUTF("partidaFinalizada");
                                entrada.readUTF();*/
                                //nombreGanador = jugadorAux.getNombre();

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

                        case "mazoTerminado":
                            Platform.runLater(() -> {
                                new Mensaje().show(Alert.AlertType.INFORMATION, "Información de juego", "Barajando pila de Descarte");
                                imgDesechadas.setImage(null);
                            });
                            partidaDto.getDesechadas().clear();

                            break;
                        case "Ladron":
                            DataInputStream input;
                            input = new DataInputStream(socket.getInputStream());
                            String padre = input.readUTF();
                            String hijo = input.readUTF();
                            String IPJugador = input.readUTF();
                            ArrayList<CartaDto> cartas = new ArrayList();
                            String IPActual = partidaDto.getJugadores().stream().filter(x -> x.getTurno()).findAny().get().getIP();
                            switch (padre) {
                                case "hvox":
                                    cartas = cartasRival(partidaDto.getJugadores().get(0), Integer.valueOf(hijo));
                                    break;
                                case "hvox2":
                                    cartas = cartasRival(partidaDto.getJugadores().get(1), Integer.valueOf(hijo));
                                    break;
                                case "hvox3":
                                    cartas = cartasRival(partidaDto.getJugadores().get(2), Integer.valueOf(hijo));
                                    break;
                                case "hvox4":
                                    cartas = cartasRival(partidaDto.getJugadores().get(3), Integer.valueOf(hijo));
                                    break;
                                case "hvox5":
                                    cartas = cartasRival(partidaDto.getJugadores().get(4), Integer.valueOf(hijo));
                                    break;
                                case "hbox6":
                                    cartas = cartasRival(partidaDto.getJugadores().get(5), Integer.valueOf(hijo));
                                    break;
                                default:
                                    break;
                            }

                            if (partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPActual)).findAny().get().getCartas1().isEmpty()) {
                                partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPActual)).findAny().get().setCartas1(cartas);
                                valor = 0;
                            } else if (partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPActual)).findAny().get().getCartas2().isEmpty()) {
                                partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPActual)).findAny().get().setCartas2(cartas);
                                valor = 1;
                            } else if (partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPActual)).findAny().get().getCartas3().isEmpty()) {
                                partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPActual)).findAny().get().setCartas3(cartas);
                                valor = 2;
                            } else if (partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPActual)).findAny().get().getCartas4().isEmpty()) {
                                partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPActual)).findAny().get().setCartas4(cartas);
                                valor = 3;
                            } else if (partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPActual)).findAny().get().getCartas5().isEmpty()) {
                                partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPActual)).findAny().get().setCartas5(cartas);
                                valor = 4;
                            }

                            if (partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPActual)).findAny().get().getIP().equals(jugadorDto.getIP())) {
                                jugadorDto = partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPActual)).findAny().get();
                                AppContext.getInstance().set("JugadorDto", jugadorDto);
                            }

                            int variableU = partidaDto.getJugadores().indexOf(partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPJugador)).findAny().get());
                            String idHBox = hBoxJugador(variableU);

                            //aquí agregamos el órgano visualmente
                            anchorPane.getChildren().forEach((t) -> {
                                if (t.getId() != null && t.getId().equals(padre)) {
                                    int i = Integer.valueOf(hijo);
                                    paneAux2 = ((Pane) ((HBox) t).getChildren().get(i));
                                } else if (t.getId() != null && t.getId().equals(idHBox)) {
                                    int i = valor;
                                    paneAux1 = ((Pane) ((HBox) t).getChildren().get(i));
                                }
                            });

                            Platform.runLater(() -> {

                                ArrayList<Node> nodos = new ArrayList();
                                paneAux2.getChildren().stream().forEach((t) -> {
                                    nodos.add(t);
                                });
                                paneAux2.getChildren().clear();
                                paneAux1.getChildren().addAll(nodos);
                            });

                            /*
                            Preguntamos si ya termino el juegoS
                             */
                            int cont = 0;
                            JugadorDto jugadorAux = partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPActual)).findAny().get();
                            if ((!jugadorAux.getCartas1().isEmpty()) ? jugadorAux.getCartas1().get(0).getEstado().equals("Sano")
                                    || jugadorAux.getCartas1().get(0).getEstado().equals("Inmunizado")
                                    || jugadorAux.getCartas1().get(0).getEstado().equals("Vacunado") : false) {
                                cont++;
                            }
                            if ((!jugadorAux.getCartas2().isEmpty()) ? jugadorAux.getCartas2().get(0).getEstado().equals("Sano")
                                    || jugadorAux.getCartas2().get(0).getEstado().equals("Inmunizado")
                                    || jugadorAux.getCartas2().get(0).getEstado().equals("Vacunado") : false) {
                                cont++;
                            }
                            if ((!jugadorAux.getCartas3().isEmpty()) ? jugadorAux.getCartas3().get(0).getEstado().equals("Sano")
                                    || jugadorAux.getCartas3().get(0).getEstado().equals("Inmunizado")
                                    || jugadorAux.getCartas3().get(0).getEstado().equals("Vacunado") : false) {
                                cont++;
                            }
                            if ((!jugadorAux.getCartas4().isEmpty()) ? jugadorAux.getCartas4().get(0).getEstado().equals("Sano")
                                    || jugadorAux.getCartas4().get(0).getEstado().equals("Inmunizado")
                                    || jugadorAux.getCartas4().get(0).getEstado().equals("Vacunado") : false) {
                                cont++;
                            }
                            if ((!jugadorAux.getCartas5().isEmpty()) ? jugadorAux.getCartas5().get(0).getEstado().equals("Sano")
                                    || jugadorAux.getCartas5().get(0).getEstado().equals("Inmunizado")
                                    || jugadorAux.getCartas5().get(0).getEstado().equals("Vacunado") : false) {
                                cont++;
                            }

                            if (cont == 4) {
                                mensajeRecibido = "partidaFinalizada";
                                jugadorDto = jugadorAux;
                            }

                            break;
                        case "errorMedico":
                            DataInputStream entrada;
                            entrada = new DataInputStream(socket.getInputStream());
                            String hBoxRival = entrada.readUTF();

                            JugadorDto jugador = partidaDto.getJugadores().stream().filter(x -> x.getTurno()).findAny().get();
                            int i = partidaDto.getJugadores().indexOf(jugador);

                            String hBoxJugActual = "";
                            switch (i) {
                                case 0:
                                    hBoxJugActual = "hvox";
                                    break;
                                case 1:
                                    hBoxJugActual = "hvox2";
                                    break;
                                case 2:
                                    hBoxJugActual = "hvox3";
                                    break;
                                case 3:
                                    hBoxJugActual = "hvox4";
                                    break;
                                case 4:
                                    hBoxJugActual = "hvox5";
                                    break;
                                case 5:
                                    hBoxJugActual = "hbox6";
                                    break;
                                default:
                                    break;
                            }
                            String IPJ1 = jugador.getIP();
                            switch (hBoxRival) {
                                case "hvox":
                                    IPJ2 = partidaDto.getJugadores().get(0).getIP();
                                    intercambioJuego(partidaDto.getJugadores().stream().filter(x -> x.getTurno()).findAny().get(), 0);
                                    break;
                                case "hvox2":
                                    IPJ2 = partidaDto.getJugadores().get(1).getIP();
                                    intercambioJuego(partidaDto.getJugadores().stream().filter(x -> x.getTurno()).findAny().get(), 1);
                                    break;
                                case "hvox3":
                                    IPJ2 = partidaDto.getJugadores().get(2).getIP();
                                    intercambioJuego(partidaDto.getJugadores().stream().filter(x -> x.getTurno()).findAny().get(), 2);
                                    break;
                                case "hvox4":
                                    IPJ2 = partidaDto.getJugadores().get(3).getIP();
                                    intercambioJuego(partidaDto.getJugadores().stream().filter(x -> x.getTurno()).findAny().get(), 3);
                                    break;
                                case "hvox5":
                                    IPJ2 = partidaDto.getJugadores().get(4).getIP();
                                    intercambioJuego(partidaDto.getJugadores().stream().filter(x -> x.getTurno()).findAny().get(), 4);
                                    break;
                                case "hbox6":
                                    IPJ2 = partidaDto.getJugadores().get(5).getIP();
                                    intercambioJuego(partidaDto.getJugadores().stream().filter(x -> x.getTurno()).findAny().get(), 5);
                                    break;
                                default:
                                    break;
                            }

                            if (partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPJ1)).findAny().get().getIP().equals(jugadorDto.getIP())) {
                                jugadorDto = partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPJ1)).findAny().get();

                                System.out.println("JUGADOR QUE HIZO LA JUGADA");

                                AppContext.getInstance().set("JugadorDto", jugadorDto);
                            } else if (partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPJ2)).findAny().get().getIP().equals(jugadorDto.getIP())) {
                                jugadorDto = partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPJ2)).findAny().get();
                                AppContext.getInstance().set("JugadorDto", jugadorDto);
                                System.out.println("JUGADOR QUE SE LA APLICARON");
                            }

                            //aquí se se seleccionan los hbox que van a ser intercambiados
                            for (Node t : anchorPane.getChildren()) {
                                if (t.getId() != null && t.getId().equals(hBoxRival)) {
                                    jug2 = ((HBox) t);
                                } else if (t.getId() != null && t.getId().equals(hBoxJugActual)) {
                                    jug1 = ((HBox) t);
                                }
                            }

                            Platform.runLater(() -> {

                                ArrayList<Node> nodosJ1 = new ArrayList();
                                ArrayList<Node> nodosJ2 = new ArrayList();
                                jug1.getChildren().stream().forEach((t) -> {
                                    nodosJ1.add(t);
                                });
                                jug2.getChildren().stream().forEach((t) -> {
                                    nodosJ2.add(t);
                                });
                                jug1.getChildren().clear();
                                jug1.getChildren().addAll(nodosJ2);
                                jug2.getChildren().clear();
                                jug2.getChildren().addAll(nodosJ1);
                            });
                            break;
                        case "Transplante":
                            DataInputStream input2;
                            input2 = new DataInputStream(socket.getInputStream());
                            String padre1 = input2.readUTF();
                            String hijo1 = input2.readUTF();
                            String padre2 = input2.readUTF();
                            String hijo2 = input2.readUTF();
                            ArrayList<CartaDto> cartas1 = new ArrayList();
                            ArrayList<CartaDto> cartas2 = new ArrayList();
                            JugadorDto JugadorActual = partidaDto.getJugadores().stream().filter(x -> x.getTurno()).findAny().get();
                            Integer jugador2 = hBoxJugador(padre2);
                            Integer jugador1 = hBoxJugador(padre1);
                            Integer hijo1Aux = Integer.valueOf(hijo1);
                            Integer hijo2Aux = Integer.valueOf(hijo2);
                            
                            
                            cartas1 = cartas(partidaDto.getJugadores().get(jugador1), hijo1Aux);
                            System.out.println("CARTA JUGADOR 1 "+ cartas1.get(0).getTipoCarta()+" INDICE "+ jugador1+ " HIJO "+ hijo1Aux);
                            cartas2 = cartas(partidaDto.getJugadores().get(jugador2), hijo2Aux);
                            System.out.println("CARTA JUGADOR 2 "+ cartas2.get(0).getTipoCarta()+ " INDICE "+ jugador2+ " HIJO "+ hijo2Aux);

                            switch (hijo1Aux) {
                                case 0:
                                    partidaDto.getJugadores().get(jugador1).getCartas1().clear();
                                    partidaDto.getJugadores().get(jugador1).getCartas1().addAll(cartas2);
                                    break;
                                case 1:
                                    partidaDto.getJugadores().get(jugador1).getCartas2().clear();
                                    partidaDto.getJugadores().get(jugador1).getCartas2().addAll(cartas2);
                                    break;
                                case 2:
                                    partidaDto.getJugadores().get(jugador1).getCartas3().clear();
                                    partidaDto.getJugadores().get(jugador1).getCartas3().addAll(cartas2);
                                    break;
                                case 3:
                                    partidaDto.getJugadores().get(jugador1).getCartas4().clear();
                                    partidaDto.getJugadores().get(jugador1).getCartas4().addAll(cartas2);
                                    break;
                                case 4:
                                    partidaDto.getJugadores().get(jugador1).getCartas5().clear();
                                    partidaDto.getJugadores().get(jugador1).getCartas5().addAll(cartas2);
                                    break;
                            }
                            
                            switch (hijo2Aux) {
                                case 0:
                                    partidaDto.getJugadores().get(jugador2).getCartas1().clear();
                                    partidaDto.getJugadores().get(jugador2).getCartas1().addAll(cartas1);
                                    break;
                                case 1:
                                    partidaDto.getJugadores().get(jugador2).getCartas2().clear();
                                    partidaDto.getJugadores().get(jugador2).getCartas2().addAll(cartas1);
                                    break;
                                case 2:
                                    partidaDto.getJugadores().get(jugador2).getCartas3().clear();
                                    partidaDto.getJugadores().get(jugador2).getCartas3().addAll(cartas1);
                                    break;
                                case 3:
                                    partidaDto.getJugadores().get(jugador2).getCartas4().clear();
                                    partidaDto.getJugadores().get(jugador2).getCartas4().addAll(cartas1);
                                    break;
                                case 4:
                                    partidaDto.getJugadores().get(jugador2).getCartas5().clear();
                                    partidaDto.getJugadores().get(jugador2).getCartas5().addAll(cartas1);
                                    break;
                            }
                            
                            System.out.println("JUGADOR QUE HIZO EL MOVIENTO");
                            System.out.println("LISTA 1");
                            partidaDto.getJugadores().get(jugador1).getCartas1().stream().forEach((t) -> {
                                System.out.println(t.getTipoCarta());
                            });
                            System.out.println("LISTA 2");
                            partidaDto.getJugadores().get(jugador1).getCartas2().stream().forEach((t) -> {
                                System.out.println(t.getTipoCarta());
                            });
                            System.out.println("LISTA 3");
                            partidaDto.getJugadores().get(jugador1).getCartas3().stream().forEach((t) -> {
                                System.out.println(t.getTipoCarta());
                            });
                            System.out.println("LISTA 4");
                            partidaDto.getJugadores().get(jugador1).getCartas4().stream().forEach((t) -> {
                                System.out.println(t.getTipoCarta());
                            });
                            System.out.println("LISTA 5");
                            partidaDto.getJugadores().get(jugador1).getCartas5().stream().forEach((t) -> {
                                System.out.println(t.getTipoCarta());
                            });
                            
                            System.out.println("JUGADOR QUE RECIBIO");
                            System.out.println("LISTA 1");
                            partidaDto.getJugadores().get(jugador2).getCartas1().stream().forEach((t) -> {
                                System.out.println(t.getTipoCarta());
                            });
                            System.out.println("LISTA 2");
                            partidaDto.getJugadores().get(jugador2).getCartas2().stream().forEach((t) -> {
                                System.out.println(t.getTipoCarta());
                            });
                            System.out.println("LISTA 3");
                            partidaDto.getJugadores().get(jugador2).getCartas3().stream().forEach((t) -> {
                                System.out.println(t.getTipoCarta());
                            });
                            System.out.println("LISTA 4");
                            partidaDto.getJugadores().get(jugador2).getCartas4().stream().forEach((t) -> {
                                System.out.println(t.getTipoCarta());
                            });
                            System.out.println("LISTA 5");
                            partidaDto.getJugadores().get(jugador2).getCartas5().stream().forEach((t) -> {
                                System.out.println(t.getTipoCarta());
                            });
                            
                            
                            if (partidaDto.getJugadores().get(jugador1).getIP().equals(jugadorDto.getIP())) {
                                jugadorDto = partidaDto.getJugadores().get(jugador1);

                                System.out.println("JUGADOR QUE HIZO LA JUGADA");

                                AppContext.getInstance().set("JugadorDto", jugadorDto);
                            } else if (partidaDto.getJugadores().get(jugador2).getIP().equals(jugadorDto.getIP())) {
                                jugadorDto = partidaDto.getJugadores().get(jugador2);
                                AppContext.getInstance().set("JugadorDto", jugadorDto);
                                System.out.println("JUGADOR QUE SE LA APLICARON");
                            }

                            anchorPane.getChildren().forEach((t) -> {
                                if (t.getId() != null && t.getId().equals(padre1)) {
                                    int i2 = hijo1Aux;
                                    paneAux2 = ((Pane) ((HBox) t).getChildren().get(i2));
                                } else if (t.getId() != null && t.getId().equals(padre2)) {
                                    int i3 = hijo2Aux;
                                    paneAux1 = ((Pane) ((HBox) t).getChildren().get(i3));
                                }
                            });

                            Platform.runLater(() -> {
                                ArrayList<Node> nodes2 = new ArrayList();
                                paneAux2.getChildren().stream().forEach((t) -> {
                                    nodes2.add(t);
                                });
                                ArrayList<Node> nodes1 = new ArrayList();
                                paneAux1.getChildren().stream().forEach((t) -> {
                                    nodes1.add(t);
                                });

                                paneAux2.getChildren().clear();
                                paneAux1.getChildren().clear();

                                paneAux1.getChildren().addAll(nodes2);
                                paneAux2.getChildren().addAll(nodes1);
                            });

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

        final String nombreGanador = jugadorDto.getNombre();

        Platform.runLater(() -> {
            new Mensaje().showModal(Alert.AlertType.INFORMATION, "¡VICTORIA!", this.imgDesechadas.getScene().getWindow(), nombreGanador + " ha ganado el juego");
            FlowController.getInstance().goView("Inicio");
        });

        //Cierra todos los procesos que queden pendientes
    }

    private void intercambioJuego(JugadorDto jugador, int i) {
        ArrayList<CartaDto> cartasJ1 = new ArrayList();
        ArrayList<CartaDto> cartasJ2 = new ArrayList();
        //Creo dos listas auxiliares para almacenar las cartas
        cartasJ1.addAll(jugador.getCartas1());
        cartasJ2.addAll(partidaDto.getJugadores().get(i).getCartas1());
        //Limpio la listas de las cartas de los jugadores
        jugador.getCartas1().clear();
        partidaDto.getJugadores().get(i).getCartas1().clear();
        //Annado las nuevas cartas a cada jugador
        jugador.getCartas1().addAll(cartasJ2);
        partidaDto.getJugadores().get(i).getCartas1().addAll(cartasJ1);
        //Segunda Lista
        //Limpiamos las listas auxiliares
        cartasJ1.clear();
        cartasJ2.clear();
        //Se annade las nuevas cartas
        cartasJ1.addAll(jugador.getCartas2());
        cartasJ2.addAll(partidaDto.getJugadores().get(i).getCartas2());
        //Limpio la listas de las cartas de los jugadores
        jugador.getCartas2().clear();
        partidaDto.getJugadores().get(i).getCartas2().clear();
        //Annado las nuevas cartas a cada jugador
        jugador.getCartas2().addAll(cartasJ2);
        partidaDto.getJugadores().get(i).getCartas2().addAll(cartasJ1);
        //Tercer Lista
        //Limpiamos las listas auxiliares
        cartasJ1.clear();
        cartasJ2.clear();
        //Se annade las nuevas cartas
        cartasJ1.addAll(jugador.getCartas3());
        cartasJ2.addAll(partidaDto.getJugadores().get(i).getCartas3());
        //Limpio la listas de las cartas de los jugadores
        jugador.getCartas3().clear();
        partidaDto.getJugadores().get(i).getCartas3().clear();
        //Annado las nuevas cartas a cada jugador
        jugador.getCartas3().addAll(cartasJ2);
        partidaDto.getJugadores().get(i).getCartas3().addAll(cartasJ1);
        //Cuarta Lista
        //Limpiamos las listas auxiliares
        cartasJ1.clear();
        cartasJ2.clear();
        //Se annade las nuevas cartas
        cartasJ1.addAll(jugador.getCartas4());
        cartasJ2.addAll(partidaDto.getJugadores().get(i).getCartas4());
        //Limpio la listas de las cartas de los jugadores
        jugador.getCartas4().clear();
        partidaDto.getJugadores().get(i).getCartas4().clear();
        //Annado las nuevas cartas a cada jugador
        jugador.getCartas4().addAll(cartasJ2);
        partidaDto.getJugadores().get(i).getCartas4().addAll(cartasJ1);
        //Quinta Lista
        //Limpiamos las listas auxiliares
        cartasJ1.clear();
        cartasJ2.clear();
        //Se annade las nuevas cartas
        cartasJ1.addAll(jugador.getCartas5());
        cartasJ2.addAll(partidaDto.getJugadores().get(i).getCartas5());
        //Limpio la listas de las cartas de los jugadores
        jugador.getCartas5().clear();
        partidaDto.getJugadores().get(i).getCartas5().clear();
        //Annado las nuevas cartas a cada jugador
        jugador.getCartas5().addAll(cartasJ2);
        partidaDto.getJugadores().get(i).getCartas5().addAll(cartasJ1);
    }

    //retornamos el hBox del jugador
    public String hBoxJugador(int variableU) {
        String variable = "";
        switch (variableU) {
            case 0:
                variable = "hvox";
                break;
            case 1:
                variable = "hvox2";
                break;
            case 2:
                variable = "hvox3";
                break;
            case 3:
                variable = "hvox4";
                break;
            case 4:
                variable = "hvox5";
                break;
            case 5:
                variable = "hbox6";
                break;
        }
        return variable;
    }

    public Integer hBoxJugador(String variableU) {
        Integer variable = null;
        switch (variableU) {
            case "hvox":
                variable = 0;
                break;
            case "hvox2":
                variable = 1;
                break;
            case "hvox3":
                variable = 2;
                break;
            case "hvox4":
                variable = 3;
                break;
            case "hvox5":
                variable = 4;
                break;
            case "hbox6":
                variable = 5;
                break;
        }
        return variable;
    }

    private ArrayList<CartaDto> cartas(JugadorDto rival, Integer indice) {
        switch (indice) {
            case 0:
                return rival.getCartas1();
            case 1:
                return rival.getCartas2();
            case 2:
                return rival.getCartas3();
            case 3:
                return rival.getCartas4();
            case 4:
                return rival.getCartas5();
            default:
                return null;
        }
    }

    private ArrayList<CartaDto> cartasRival(JugadorDto rival, Integer indice) {
        switch (indice) {
            case 0:
                ArrayList<CartaDto> auxList = new ArrayList();
                rival.getCartas1().stream().forEach((t) -> {
                    auxList.add(t);
                });
                rival.getCartas1().clear();
                return auxList;
            case 1:
                ArrayList<CartaDto> auxList2 = new ArrayList();
                rival.getCartas2().stream().forEach((t) -> {
                    auxList2.add(t);
                });
                rival.getCartas2().clear();
                return auxList2;
            case 2:
                ArrayList<CartaDto> auxList3 = new ArrayList();
                rival.getCartas3().stream().forEach((t) -> {
                    auxList3.add(t);
                });
                rival.getCartas3().clear();
                return auxList3;
            case 3:
                ArrayList<CartaDto> auxList4 = new ArrayList();
                rival.getCartas4().stream().forEach((t) -> {
                    auxList4.add(t);
                });
                rival.getCartas4().clear();
                return auxList4;
            case 4:
                ArrayList<CartaDto> auxList5 = new ArrayList();
                rival.getCartas5().stream().forEach((t) -> {
                    auxList5.add(t);
                });
                rival.getCartas5().clear();
                return auxList5;
            default:
                return null;
        }
    }

    public void CambioEstado(ArrayList<CartaDto> mazo) {
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
                t.setEstado("Sano");
            });

            partidaDto.getDesechadas().addAll(removidas);

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
            mazo.clear();
            estado = "Extirpado";

            /*si un segundo virus es colocado sobre un órgano ya infectado, este órgano
             *será destruido y las tres cartas (el órgano y los 2 virus) serán enviadas a la pila de
             *descarte.
             */
 /*
             *si sobre un órgano se encuentra una carta de medicina y se le aplica
             *un virus del mismo color, ambas cartas (la medicina y el virus) serán enviadas a la pila
             *de descarte*/
        }
    }

    public void IniciarHilo() {
        Hilo_Peticiones hilo = new Hilo_Peticiones(partidaDto, imgDesechadas, jugadorDto, turno, anchorPane, mazoImg);
        hilo.start();
    }
}
