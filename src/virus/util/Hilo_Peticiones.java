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
                            ArrayList<CartaDto> cartaAux = new ArrayList();
                            switch (padre) {
                                case "hvox":
                                    cartaAux = cartasRival(partidaDto.getJugadores().get(0), Integer.valueOf(hijo));
                                    break;
                                case "hvox2":
                                    cartaAux = cartasRival(partidaDto.getJugadores().get(1), Integer.valueOf(hijo));
                                    break;
                                case "hvox3":
                                    cartaAux = cartasRival(partidaDto.getJugadores().get(2), Integer.valueOf(hijo));
                                    break;
                                case "hvox4":
                                    cartaAux = cartasRival(partidaDto.getJugadores().get(3), Integer.valueOf(hijo));
                                    break;
                                case "hvox5":
                                    cartaAux = cartasRival(partidaDto.getJugadores().get(4), Integer.valueOf(hijo));
                                    break;
                                case "hbox6":
                                    cartaAux = cartasRival(partidaDto.getJugadores().get(5), Integer.valueOf(hijo));
                                    break;
                                default:
                                    break;
                            }
                            int valor = 0;
                            if (partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPJugador)).findAny().get().getCartas1().isEmpty()) {
                                partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPJugador)).findAny().get().setCartas1(cartaAux);
                                valor = 0;
                            } else if (partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPJugador)).findAny().get().getCartas2().isEmpty()) {
                                partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPJugador)).findAny().get().setCartas2(cartaAux);
                                valor = 1;
                            } else if (partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPJugador)).findAny().get().getCartas3().isEmpty()) {
                                partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPJugador)).findAny().get().setCartas3(cartaAux);
                                valor = 2;
                            } else if (partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPJugador)).findAny().get().getCartas4().isEmpty()) {
                                partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPJugador)).findAny().get().setCartas4(cartaAux);
                                valor = 3;
                            } else if (partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPJugador)).findAny().get().getCartas5().isEmpty()) {
                                partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPJugador)).findAny().get().setCartas5(cartaAux);
                                valor = 4;
                            }

                            if (partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPJugador)).findAny().get().getIP().equals(jugadorDto.getIP())) {
                                jugadorDto = partidaDto.getJugadores().stream().filter(x -> x.getIP().equals(IPJugador)).findAny().get();
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
                                    int i = variableU;
                                    paneAux1 = ((Pane) ((HBox) t).getChildren().get(i));
                                }
                            });
                            Platform.runLater(() -> {
                                ArrayList<Node> nodos = (ArrayList<Node>) paneAux2.getChildren();
                                paneAux2.getChildren().clear();
                                paneAux1.getChildren().addAll(nodos);
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

    private ArrayList<CartaDto> cartasRival(JugadorDto rival, Integer indice) {
        switch (indice) {
            case 0:
                ArrayList<CartaDto> auxList = rival.getCartas1();
                rival.getCartas1().clear();
                return auxList;
            case 1:
                ArrayList<CartaDto> auxList2 = rival.getCartas2();
                rival.getCartas2().clear();
                return auxList2;
            case 2:
                ArrayList<CartaDto> auxList3 = rival.getCartas3();
                rival.getCartas3().clear();
                return auxList3;
            case 3:
                ArrayList<CartaDto> auxList4 = rival.getCartas4();
                rival.getCartas4().clear();
                return auxList4;
            case 4:
                ArrayList<CartaDto> auxList5 = rival.getCartas5();
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

            /* ArrayList<CartaDto> removidas = (ArrayList<CartaDto>) mazo.stream().
                    filter(x -> x.getTipoCarta().equals("Medicina") || x.getTipoCarta().
                    equals("Medicina_Comodin")).collect(Collectors.toList());
            removidas.stream().forEach((t) -> {
                System.out.println("TIPO " + t.getTipoCarta());
            });
            removidas.stream().forEach((t) -> {
                t.setEstado("Sano");
            });
            
            partidaDto.getDesechadas().addAll(removidas);
            mazo.removeAll(removidas);*/

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
        Hilo_Peticiones hilo = new Hilo_Peticiones(partidaDto, imgDesechadas, jugadorDto, turno, anchorPane, mazoImg);
        hilo.start();
    }
}
