/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virus.controller;

import com.jfoenix.controls.JFXButton;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import static virus.controller.InicioController.enviarObjetos;
import virus.model.CartaDto;
import virus.model.JugadorDto;
import virus.model.PartidaDto;
import virus.util.AppContext;
import virus.util.FlowController;
import virus.util.Hilo;
import virus.util.Hilo_Peticiones;
import static virus.util.Hilo_Peticiones.findePartida;
import virus.util.Mensaje;

/**
 * FXML Controller class
 *
 * @author Jose Pablo Bermudez
 */
public class JuegoController extends Controller implements Initializable {

    @FXML
    private Label user;
    @FXML
    private Label user2;
    @FXML
    private Label user3;
    @FXML
    private Label user4;
    @FXML
    private Label user5;
    @FXML
    private Label user6;
    @FXML
    private HBox hvox;
    @FXML
    private HBox hvox2;
    @FXML
    private HBox hvox3;
    @FXML
    private HBox hvox4;
    @FXML
    private HBox hvox5;
    @FXML
    private HBox hbox6;
    public static JugadorDto jugador;
    public static CartaDto carta1;
    public static CartaDto carta2;
    public static CartaDto carta3;
    public CartaDto cartaAux;
    @FXML
    private ImageView imgDesechada;
    private static ImageView desechadas;
    private static PartidaDto partida = new PartidaDto();
    boolean finalizado = false;
    Timer timer = new Timer();
    int tic = 1;
    DataInputStream entrada;
    DataOutputStream salida;
    Socket socket;
    ServerSocket serverSocket;
    String mensajeRecibido;
    ImageView imageViewDesechada = null;
    @FXML
    private AnchorPane fondo_juego;
    @FXML
    private HBox H_turno;
    @FXML
    private Label lbl_t_Turno;
    @FXML
    private Label lbl_JTurno;
    @FXML
    private ImageView CartaBocaAbajoimg;
    public static ImageView image7;
    public static ImageView image8;
    public static ImageView image9;
    public static Boolean recogioCarta = false;
    public static Pane paneAuxiliar;
    public Boolean modoDesechar = false;
    public Boolean modoOrgano = false;
    public Boolean unSoloOrgano = false;
    public static Hilo_Peticiones peticiones;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        jugador = (JugadorDto) AppContext.getInstance().get("JugadorDto");
        carta1 = jugador.getMazo().get(0);
        carta2 = jugador.getMazo().get(1);
        carta3 = jugador.getMazo().get(2);

        user.setText(jugador.getNombre());

        ArrayList<JugadorDto> jugadores = (ArrayList<JugadorDto>) AppContext.getInstance().get("Jugadores");

        switch (jugadores.size()) {
            case 2:
                hvox.getChildren().forEach(x -> {
                    x.setOnMouseReleased(movimiento);
                    x.getStyleClass().clear();
                    x.getStyleClass().add("hVoxActivo");

                });
                hvox2.getChildren().forEach(x -> {
                    x.setOnMouseReleased(movimiento);
                    x.getStyleClass().clear();
                    x.getStyleClass().add("hVoxActivo");
                });
                break;
            case 3:
                hvox.getChildren().forEach(x -> {
                    x.setOnMouseReleased(movimiento);
                    x.getStyleClass().clear();
                    x.getStyleClass().add("hVoxActivo");
                });
                hvox2.getChildren().forEach(x -> {
                    x.setOnMouseReleased(movimiento);
                    x.getStyleClass().clear();
                    x.getStyleClass().add("hVoxActivo");
                });
                hvox3.getChildren().forEach(x -> {
                    x.setOnMouseReleased(movimiento);
                    x.getStyleClass().clear();
                    x.getStyleClass().add("hVoxActivo");
                });
                break;
            case 4:
                hvox.getChildren().forEach(x -> {
                    x.setOnMouseReleased(movimiento);
                    x.getStyleClass().clear();
                    x.getStyleClass().add("hVoxActivo");
                });
                hvox2.getChildren().forEach(x -> {
                    x.setOnMouseReleased(movimiento);
                    x.getStyleClass().clear();
                    x.getStyleClass().add("hVoxActivo");
                });
                hvox3.getChildren().forEach(x -> {
                    x.setOnMouseReleased(movimiento);
                    x.getStyleClass().clear();
                    x.getStyleClass().add("hVoxActivo");
                });
                hvox4.getChildren().forEach(x -> {
                    x.setOnMouseReleased(movimiento);
                    x.getStyleClass().clear();
                    x.getStyleClass().add("hVoxActivo");
                });
                break;
            case 5:
                hvox.getChildren().forEach(x -> {
                    x.setOnMouseReleased(movimiento);
                    x.getStyleClass().clear();
                    x.getStyleClass().add("hVoxActivo");
                });
                hvox2.getChildren().forEach(x -> {
                    x.setOnMouseReleased(movimiento);
                    x.getStyleClass().clear();
                    x.getStyleClass().add("hVoxActivo");
                });
                hvox3.getChildren().forEach(x -> {
                    x.setOnMouseReleased(movimiento);
                    x.getStyleClass().clear();
                    x.getStyleClass().add("hVoxActivo");
                });
                hvox4.getChildren().forEach(x -> {
                    x.setOnMouseReleased(movimiento);
                    x.getStyleClass().clear();
                    x.getStyleClass().add("hVoxActivo");
                });
                hvox5.getChildren().forEach(x -> {
                    x.setOnMouseReleased(movimiento);
                    x.getStyleClass().clear();
                    x.getStyleClass().add("hVoxActivo");
                });
                break;
            case 6:
                hvox.getChildren().forEach(x -> {
                    x.setOnMouseReleased(movimiento);
                    x.getStyleClass().clear();
                    x.getStyleClass().add("hVoxActivo");
                });
                hvox2.getChildren().forEach(x -> {
                    x.setOnMouseReleased(movimiento);
                    x.getStyleClass().clear();
                    x.getStyleClass().add("hVoxActivo");
                });
                hvox3.getChildren().forEach(x -> {
                    x.setOnMouseReleased(movimiento);
                    x.getStyleClass().clear();
                    x.getStyleClass().add("hVoxActivo");
                });
                hvox4.getChildren().forEach(x -> {
                    x.setOnMouseReleased(movimiento);
                    x.getStyleClass().clear();
                    x.getStyleClass().add("hVoxActivo");
                });
                hvox5.getChildren().forEach(x -> {
                    x.setOnMouseReleased(movimiento);
                    x.getStyleClass().clear();
                    x.getStyleClass().add("hVoxActivo");
                });
                hbox6.getChildren().forEach(x -> {
                    x.setOnMouseReleased(movimiento);
                    x.getStyleClass().clear();
                    x.getStyleClass().add("hVoxActivo");
                });
                break;
            default:
                break;
        }

        if (jugadores.get(0).getIP().equals(jugador.getIP())) {
            jugador.setTurno(true);
        }

        lbl_JTurno.setText((jugadores.stream().filter(x -> x.getTurno()).findAny().get()).getNombre());

        ArrayList<Label> nombres = new ArrayList();

        nombres.add(user);
        nombres.add(user2);
        nombres.add(user3);
        nombres.add(user4);
        nombres.add(user5);
        nombres.add(user6);

        for (int i = 0; i < jugadores.size(); i++) {
            nombres.get(i).setText(jugadores.get(i).getNombre());
        }
        VBox vBox = new VBox();
        image7 = new ImageView("virus/resources/" + carta1.getImagen());
        image7.setId("carta1");
        vBox.getStyleClass().clear();
        vBox.getStyleClass().add("hVoxActivo");
        image7.setFitHeight(107.25);
        image7.setFitWidth(74.75);
        vBox.setLayoutX(400);
        vBox.setLayoutY(450);
        image7.setOnMouseClicked(cartaAdesechar);
        vBox.getChildren().add(image7);

        VBox vBox2 = new VBox();
        image8 = new ImageView("virus/resources/" + carta2.getImagen());
        image8.setId("carta2");
        vBox2.getStyleClass().clear();
        vBox2.getStyleClass().add("hVoxActivo");
        image8.setFitHeight(107.25);
        image8.setFitWidth(74.75);
        vBox2.setLayoutX(vBox.getLayoutX() + 102.5);
        vBox2.setLayoutY(450);
        image8.setOnMouseClicked(cartaAdesechar);
        vBox2.getChildren().add(image8);

        VBox vBox3 = new VBox();
        image9 = new ImageView("virus/resources/" + carta3.getImagen());
        image9.setId("carta3");
        vBox3.getStyleClass().clear();
        vBox3.getStyleClass().add("hVoxActivo");
        image9.setFitHeight(107.25);
        image9.setFitWidth(74.75);
        vBox3.setLayoutX(vBox2.getLayoutX() + 102.5);
        vBox3.setLayoutY(450);
        image9.setOnMouseClicked(cartaAdesechar);
        vBox3.getChildren().add(image9);

        fondo_juego.getChildren().add(vBox);
        fondo_juego.getChildren().add(vBox2);
        fondo_juego.getChildren().add(vBox3);

        //Introduce los jugadores a la partida
        partida.setJugadores(jugadores);
        peticiones = new Hilo_Peticiones(partida, imgDesechada, jugador, lbl_JTurno, fondo_juego);
        peticiones.start();

        desechadas = imgDesechada;
    }
    String hijo = "";
    Boolean vacio = true;
    VBox boxVacio = null;

    private void movimiento(String padre, String hijo) {
        enviarCartaJuegoSocket("movimientoJugador", padre, hijo);
        modoOrgano = true;
        unSoloOrgano = true;
    }

    private void noVirus(String padre, String hijo) {
        if (!cartaAux.getTipoCarta().equals("Virus")) {
            movimiento(padre, hijo);
        } else {
            new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No puedes poner un virus en tus propias cartas");
        }
    }

    private void movimiento(String padre) {
        if (!findePartida) {
            if (!unSoloOrgano) {
                if (!modoDesechar) {
                    if (paneAuxiliar != null) {
                        hijo = "";
                        vacio = true;

                        fondo_juego.getChildren().forEach((t) -> {
                            if (t.getId() != null && t.getId().equals(padre)) {
                                ((HBox) t).getChildren().forEach((v) -> {
                                    if (v.equals(paneAuxiliar)) {
                                        hijo = String.valueOf(((HBox) t).getChildren().indexOf(v));
                                    }
                                });
                            }
                        });
                        //si es el primer movimiento
                        if (cartaAux.getTipoCarta().equals("Corazon") || cartaAux.getTipoCarta().equals("Estomago")
                                || cartaAux.getTipoCarta().equals("Cerebro") || cartaAux.getTipoCarta().equals("Hueso")
                                || cartaAux.getTipoCarta().equals("Organo_Comodin") && jugador.getCartas1().isEmpty() && jugador.getCartas2().isEmpty() && jugador.getCartas3().isEmpty() && jugador.getCartas4().isEmpty() && jugador.getCartas5().isEmpty()) {
                            movimiento(padre, hijo);
                        } else if (cartaAux.getTipoCarta().equals("Corazon") || cartaAux.getTipoCarta().equals("Estomago")
                                || cartaAux.getTipoCarta().equals("Cerebro") || cartaAux.getTipoCarta().equals("Hueso")
                                || cartaAux.getTipoCarta().equals("Organo_Comodin") && (!jugador.getCartas1().isEmpty() //Organo en los campos vacios
                                ? !cartaAux.getTipoCarta().equals(jugador.getCartas1().get(0).getTipoCarta())
                                : true)
                                && (!jugador.getCartas2().isEmpty()
                                ? !cartaAux.getTipoCarta().equals(jugador.getCartas2().get(0).getTipoCarta())
                                : true)
                                && (!jugador.getCartas3().isEmpty()
                                ? !cartaAux.getTipoCarta().equals(jugador.getCartas3().get(0).getTipoCarta())
                                : true)
                                && (!jugador.getCartas4().isEmpty()
                                ? !cartaAux.getTipoCarta().equals(jugador.getCartas4().get(0).getTipoCarta())
                                : true)
                                && (!jugador.getCartas5().isEmpty()
                                ? !cartaAux.getTipoCarta().equals(jugador.getCartas5().get(0).getTipoCarta())
                                : true)) {
                            //Verifica que el organo que se desea poner no este lleno con un organo 
                            switch (hijo) {
                                case "0":
                                    if (jugador.getCartas1().isEmpty()) {
                                        movimiento(padre, hijo);
                                    } else {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pude poner otro órgano en este campo");
                                    }
                                    break;
                                case "1":
                                    if (jugador.getCartas2().isEmpty()) {
                                        movimiento(padre, hijo);
                                    } else {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pude poner otro órgano en este campo");
                                    }
                                    break;
                                case "2":
                                    if (jugador.getCartas3().isEmpty()) {
                                        movimiento(padre, hijo);
                                    } else {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pude poner otro órgano en este campo");
                                    }
                                    break;
                                case "3":
                                    if (jugador.getCartas4().isEmpty()) {
                                        movimiento(padre, hijo);
                                    } else {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pude poner otro órgano en este campo");
                                    }
                                    break;
                                case "4":
                                    if (jugador.getCartas5().isEmpty()) {
                                        movimiento(padre, hijo);
                                    } else {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pude poner otro órgano en este campo");
                                    }
                                    break;
                            }
                        } else {//Cualquier otro movimiento
                            switch (hijo) {
                                case "0":
                                    if (jugador.getCartas1().get(0).getColor().equals(cartaAux.getColor()) || jugador.getCartas1().get(0).getTipoCarta().equals("Organo_Comodin")) {
                                        noVirus(padre, hijo);
                                    } else {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner cartas de distinto color");
                                    }
                                    break;
                                case "1":
                                    if (jugador.getCartas2().get(0).getColor().equals(cartaAux.getColor()) || jugador.getCartas2().get(0).getTipoCarta().equals("Organo_Comodin")) {
                                        noVirus(padre, hijo);
                                    } else {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner cartas de distinto color");
                                    }
                                    break;
                                case "2":
                                    if (jugador.getCartas3().get(0).getColor().equals(cartaAux.getColor())|| jugador.getCartas3().get(0).getTipoCarta().equals("Organo_Comodin")) {
                                        noVirus(padre, hijo);
                                    } else {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner cartas de distinto color");
                                    }
                                    break;
                                case "3":
                                    if (jugador.getCartas4().get(0).getColor().equals(cartaAux.getColor()) || jugador.getCartas4().get(0).getTipoCarta().equals("Organo_Comodin")) {
                                        noVirus(padre, hijo);
                                    } else {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner cartas de distinto color");
                                    }
                                    break;
                                case "4":
                                    if (jugador.getCartas5().get(0).getColor().equals(cartaAux.getColor()) || jugador.getCartas5().get(0).getTipoCarta().equals("Organo_Comodin")) {
                                        noVirus(padre, hijo);
                                    } else {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner cartas de distinto color");
                                    }
                                default:
                                    Mensaje ms = new Mensaje();
                                    ms.show(Alert.AlertType.WARNING, "Información de Juego", "Ya hay un tipo de organo en este mazo");
                                    break;
                            }
                        }
                    } else {
                        Mensaje ms = new Mensaje();
                        ms.show(Alert.AlertType.WARNING, "Información de Juego", "No puedes agregar un órgano en este lugar.");
                    }
                } else {
                    Mensaje ms = new Mensaje();
                    ms.show(Alert.AlertType.WARNING, "Información de Juego", "No puedes agregar un órgano si ya botaste cartas");
                }
            } else {
                Mensaje msj = new Mensaje();
                msj.show(Alert.AlertType.WARNING, "Información de Juego", "No puedes agregar un órgano si has agregado uno previamente");//.l.
            }
        } else {
            Mensaje msj = new Mensaje();
            msj.show(Alert.AlertType.WARNING, "Información de Juego", "La partida ya ha finalizado");//.l.
        }
    }

    EventHandler<MouseEvent> movimiento = event -> {
        if (!findePartida) {
            jugador = (JugadorDto) AppContext.getInstance().get("JugadorDto");
            if (jugador.getTurno()) {
                if (cartaAux != null) {
                    JugadorDto jugadorAux = partida.getJugadores().stream().
                            filter(x -> x.getIP().equals(jugador.getIP())).findAny().get();
                    int i = partida.getJugadores().indexOf(jugadorAux);

                    paneAuxiliar = (Pane) event.getSource();
                    String padre = paneAuxiliar.getParent().getId();
                    switch (i) {
                        case 0:
                            if (padre.equals("hvox")) {
                                movimiento(padre);
                            } else {
                                Mensaje ms = new Mensaje();
                                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Esta no es tu zona de juego.");
                            }
                            break;
                        case 1:
                            if (padre.equals("hvox2")) {
                                movimiento(padre);
                            } else {
                                Mensaje ms = new Mensaje();
                                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Esta no es tu zona de juego.");
                            }
                            break;

                        case 2:
                            if (padre.equals("hvox3")) {
                                movimiento(padre);
                            } else {
                                Mensaje ms = new Mensaje();
                                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Esta no es tu zona de juego.");
                            }
                            break;
                        case 3:
                            if (padre.equals("hvox4")) {
                                movimiento(padre);
                            } else {
                                Mensaje ms = new Mensaje();
                                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Esta no es tu zona de juego.");
                            }
                            break;
                        case 4:
                            if (padre.equals("hvox5")) {
                                movimiento(padre);
                            } else {
                                Mensaje ms = new Mensaje();
                                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Esta no es tu zona de juego.");
                            }
                            break;
                        case 5:
                            if (padre.equals("hbox6")) {
                                movimiento(padre);
                            } else {
                                Mensaje ms = new Mensaje();
                                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Esta no es tu zona de juego.");
                            }
                            break;
                    }
                } else {
                    Mensaje msj = new Mensaje();
                    msj.show(Alert.AlertType.WARNING, "Error con carta", "No has seleccionado la carta");
                }
            } else {
                Mensaje ms = new Mensaje();
                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Espera a tu turno");
            }
        } else {
            Mensaje ms = new Mensaje();
            ms.show(Alert.AlertType.WARNING, "Información de Juego", "La partida ya ha finalizado");
        }
    };

    EventHandler<MouseEvent> cartaAdesechar = event -> {
        if (!findePartida) {
            jugador = (JugadorDto) AppContext.getInstance().get("JugadorDto");
            if (jugador.getTurno()) {
                imageViewDesechada = ((ImageView) event.getSource());
                switch (((ImageView) event.getSource()).getId()) {
                    case "carta3":
                        cartaAux = carta3;
                        break;
                    case "carta2":
                        cartaAux = carta2;
                        break;
                    default:
                        //carta 1
                        cartaAux = carta1;
                        break;
                }
            } else {
                Mensaje ms = new Mensaje();
                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Espera a tu turno");
            }
        } else {
            Mensaje ms = new Mensaje();
            ms.show(Alert.AlertType.WARNING, "Información de Juego", "La partida ya ha finalizado");
        }
    };

    @FXML
    private void Salir(MouseEvent event) {
        FlowController.getInstance().goView("Menu");
    }

    @Override
    public void initialize() {

    }

    public static void ObtenerCarta(String IP_Servidor) {
        try {
            Socket socket = new Socket(IP_Servidor, 44440);
            DataOutputStream mensaje = new DataOutputStream(socket.getOutputStream());
            //DataInputStream respuesta = new DataInputStream(socket.getInputStream());
            System.out.println("Connected Text!");
            //Enviamos un mensaje
            mensaje.writeUTF("pedirCartas");
            String mensajeRecibido = "";
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            mensajeRecibido = entrada.readUTF();
            System.out.println(mensajeRecibido);
            //Cerramos la conexión
            socket.close();
            Socket socket2 = new Socket(IP_Servidor, 44440);
            System.out.println("Connected Text!");
            DataInputStream respuesta2 = new DataInputStream(socket2.getInputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(respuesta2);
            CartaDto carta = (CartaDto) objectInputStream.readObject();
            if (carta == null) {
                OutputStream outputstream = socket2.getOutputStream();
                ObjectOutputStream objectoutputstream = new ObjectOutputStream(outputstream);
                partida.getDesechadas().stream().forEach(x -> System.out.print(x.getTipoCarta()));
                objectoutputstream.writeObject(partida.getDesechadas());
                partida.getDesechadas().clear();
                carta = (CartaDto) objectInputStream.readObject();
                Platform.runLater(() -> {
                    desechadas.setImage(null);
                });
            }
            if (image7.getImage() == null) {
                image7.setImage(new Image("virus/resources/" + carta.getImagen()));
                carta1 = carta;
            } else if (image8.getImage() == null) {
                image8.setImage(new Image("virus/resources/" + carta.getImagen()));
                carta2 = carta;
            } else if (image9.getImage() == null) {
                image9.setImage(new Image("virus/resources/" + carta.getImagen()));
                carta3 = carta;
            }

            jugador.getMazo().add(carta);

            //Cerramos la conexión
            socket2.close();
        } catch (UnknownHostException e) {
            System.out.println("El host no existe o no está activo.");
        } catch (IOException e) {
            System.out.println(e + "serás?");
        } catch (ClassNotFoundException e) {
            System.out.println(e + "o tú serás?");
        }
    }

    @FXML
    private void CartaDesechada(MouseEvent event) {
        if (!findePartida) {
            if (!modoOrgano) {
                if (!recogioCarta) {
                    if (jugador.getTurno()) {
                        if (cartaAux != null) {
                            desecharCarta("desecharCarta");
                            modoDesechar = true;
                        } else {
                            Mensaje msj = new Mensaje();
                            msj.show(Alert.AlertType.WARNING, "Error con carta", "No has seleccionado la carta");
                        }
                    } else {
                        Mensaje ms = new Mensaje();
                        ms.show(Alert.AlertType.WARNING, "Información de Juego", "Espera a tu turno");
                    }
                } else {
                    Mensaje ms = new Mensaje();
                    ms.show(Alert.AlertType.WARNING, "Información de Juego", "No puedes desechar una carta en este momento");
                }
            } else {
                Mensaje ms = new Mensaje();
                ms.show(Alert.AlertType.WARNING, "Información de Juego", "No puedes desechar una carta si ya botaste un organo");
            }
        } else {
            Mensaje ms = new Mensaje();
            ms.show(Alert.AlertType.WARNING, "Información de Juego", "La partida ya ha finalizado");
        }
    }

    public void desecharCarta(String Mensaje) {
        try {
            Socket socket = new Socket(jugador.getIPS(), 44440);
            DataOutputStream mensaje = new DataOutputStream(socket.getOutputStream());
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            System.out.println("Connected Text!");
            mensaje.writeUTF(Mensaje);
            String mensajeRecibido = "";
            mensajeRecibido = entrada.readUTF();
            System.out.println(mensajeRecibido);
            socket.close();
            Socket socket2 = new Socket(jugador.getIPS(), 44440);
            OutputStream outputStream = socket2.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            System.out.println("Sending messages to the ServerSocket");
            objectOutputStream.writeObject(cartaAux);
            System.out.println("Closing socket and terminating program.");
            socket2.close();
            System.out.println(jugador.getMazo().remove(cartaAux));//removemos la carta del mazo del  jugador 
            imageViewDesechada.setImage(null);
            cartaAux = null;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //Envía las cartas a los jugadores
    public void enviarCartaJuegoSocket(String Mensaje, String padre, String hijo) {
        try {
            Socket socket = new Socket(jugador.getIPS(), 44440);
            DataOutputStream mensaje = new DataOutputStream(socket.getOutputStream());
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            System.out.println("Connected Text!");
            mensaje.writeUTF(Mensaje);
            String mensajeRecibido = "";
            mensajeRecibido = entrada.readUTF();
            System.out.println(mensajeRecibido);
            socket.close();
            Socket socket2 = new Socket(jugador.getIPS(), 44440);
            OutputStream outputStream = socket2.getOutputStream();
            DataOutputStream mensaje2 = new DataOutputStream(socket2.getOutputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            System.out.println("Sending messages to the ServerSocket");
            objectOutputStream.writeObject(cartaAux);
            mensaje2.writeUTF(padre);
            mensaje2.writeUTF(hijo);
            mensaje2.writeUTF(jugador.getIP());
            System.out.println("Closing socket and terminating program.");
            socket2.close();
            imageViewDesechada.setImage(null);
            System.out.println(jugador.getMazo().remove(cartaAux));//removemos la carta del mazo del  jugador 

            //AppContext.getInstance().set("JugadorDto", jugador);
            cartaAux = null;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cambiarTurnoAux() {
        if (!findePartida) {
            if (jugador.getTurno()) {
                if (jugador.getMazo().size() == 3) {
                    try {
                        Socket socket = new Socket(jugador.getIPS(), 44440);
                        DataOutputStream mensaje = new DataOutputStream(socket.getOutputStream());
                        DataInputStream entrada = new DataInputStream(socket.getInputStream());
                        System.out.println("Connected Text!");
                        mensaje.writeUTF("cambioTurno");
                        mensajeRecibido = entrada.readUTF();

                        socket.close();
                        
                        /*
                        Reinicio de variables
                        */
                        recogioCarta = false;
                        modoDesechar = false;
                        modoOrgano = false;
                        unSoloOrgano = false;
                        cartaAux = null;

                        jugador.setTurno(false);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                    Mensaje ms = new Mensaje();
                    ms.show(Alert.AlertType.WARNING, "Información de Juego", "El mazo no está completo");
                }
            } else {
                Mensaje ms = new Mensaje();
                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Espera a tu tuno");
            }
        } else {
            Mensaje ms = new Mensaje();
            ms.show(Alert.AlertType.WARNING, "Información de Juego", "La partida ya ha finalizado");
        }
    }

    @FXML
    private void CartadeMazo(MouseEvent event) {
        if (!findePartida) {
            cartaAux = null;
            if (jugador.getTurno()) {
                if (jugador.getMazo().size() < 3 && (image9.getImage() == null || image8.getImage() == null
                        || image7.getImage() == null)) {
                    recogioCarta = true;
                    ObtenerCarta(jugador.getIPS());
                    if (jugador.getMazo().size() == 3) {
                        cambiarTurnoAux();
                        Mensaje ms = new Mensaje();
                        ms.show(Alert.AlertType.INFORMATION, "Información de Juego", "Cambio de turno");
                    }
                } else {
                    Mensaje ms = new Mensaje();
                    ms.show(Alert.AlertType.WARNING, "Información de Juego", "Usted ya tiene su mazo completo");
                }
            } else {
                Mensaje ms = new Mensaje();
                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Espera a tu tuno");
            }
        } else {
            Mensaje ms = new Mensaje();
            ms.show(Alert.AlertType.WARNING, "Información de Juego", "La partida ya ha finalizado");
        }
    }
}
