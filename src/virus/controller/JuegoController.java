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
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
    public VBox vBox;
    public VBox vBox2;
    public VBox vBox3;
    private ArrayList<ImageView> mazoImg = new ArrayList();
    private boolean ladron = false;
    private boolean errorMedico = false;
    private static boolean modoTratamiento = false;

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
                hvox.setVisible(true);
                hvox2.setVisible(true);
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
                hvox.setVisible(true);
                hvox2.setVisible(true);
                hvox3.setVisible(true);

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
                hvox.setVisible(true);
                hvox2.setVisible(true);
                hvox3.setVisible(true);
                hvox4.setVisible(true);
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
                hvox.setVisible(true);
                hvox2.setVisible(true);
                hvox3.setVisible(true);
                hvox4.setVisible(true);
                hvox5.setVisible(true);
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
                hvox.setVisible(true);
                hvox2.setVisible(true);
                hvox3.setVisible(true);
                hvox4.setVisible(true);
                hbox6.setVisible(true);
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

        vBox = new VBox();
        image7 = new ImageView("virus/resources/" + carta1.getImagen());
        image7.setId("carta1");
        vBox.getStyleClass().clear();
        vBox.getStyleClass().add("hVoxActivo");
        image7.setFitHeight(81);
        image7.setFitWidth(58);
        vBox.setLayoutX(535);
        vBox.setLayoutY(510);
        image7.setOnMouseClicked(seleccionarCarta);
        vBox.getChildren().add(image7);

        vBox2 = new VBox();
        image8 = new ImageView("virus/resources/" + carta2.getImagen());
        image8.setId("carta2");
        vBox2.getStyleClass().clear();
        vBox2.getStyleClass().add("hVoxActivo");
        image8.setFitHeight(81);
        image8.setFitWidth(58);
        vBox2.setLayoutX(vBox.getLayoutX() + 102.5);
        vBox2.setLayoutY(510);
        image8.setOnMouseClicked(seleccionarCarta);
        vBox2.getChildren().add(image8);

        vBox3 = new VBox();
        image9 = new ImageView("virus/resources/" + carta3.getImagen());
        image9.setId("carta3");
        vBox3.getStyleClass().clear();
        vBox3.getStyleClass().add("hVoxActivo");
        image9.setFitHeight(81);
        image9.setFitWidth(58);
        vBox3.setLayoutX(vBox2.getLayoutX() + 102.5);
        vBox3.setLayoutY(510);
        image9.setOnMouseClicked(seleccionarCarta);
        vBox3.getChildren().add(image9);
        /*
        Agrega imagenes del mazo del usuario
         */
        mazoImg.add(image7);
        mazoImg.add(image8);
        mazoImg.add(image9);

        fondo_juego.getChildren().add(vBox);
        fondo_juego.getChildren().add(vBox2);
        fondo_juego.getChildren().add(vBox3);

        //Introduce los jugadores a la partida
        partida.setJugadores(jugadores);
        peticiones = new Hilo_Peticiones(partida, imgDesechada, jugador, lbl_JTurno, fondo_juego, mazoImg);
        peticiones.start();

        desechadas = imgDesechada;
    }

    /*
    * EVENTOS Dinamicos de MOUSE
     */
    EventHandler<MouseEvent> movimiento = event -> {
        if (!findePartida) {
            jugador = (JugadorDto) AppContext.getInstance().get("JugadorDto");
            if (jugador.getTurno()) {
                if (!ladron && !errorMedico) {
                    if (cartaAux != null) {
                        JugadorDto jugadorAux = partida.getJugadores().stream().
                                filter(x -> x.getIP().equals(jugador.getIP())).findAny().get();
                        int i = partida.getJugadores().indexOf(jugadorAux);

                        paneAuxiliar = (Pane) event.getSource();
                        String padre = paneAuxiliar.getParent().getId();
                        switch (i) {
                            case 0:
                                if (padre.equals("hvox")) {//cuando es su propio movimiento
                                    movimiento(padre);
                                } else {
                                    movimientoAdvXJug(padre);//cuando es un movimiento hacia el enemigo
                                }
                                break;
                            case 1:
                                if (padre.equals("hvox2")) {
                                    movimiento(padre);
                                } else {
                                    movimientoAdvXJug(padre);
                                }
                                break;

                            case 2:
                                if (padre.equals("hvox3")) {
                                    movimiento(padre);
                                } else {
                                    movimientoAdvXJug(padre);
                                }
                                break;
                            case 3:
                                if (padre.equals("hvox4")) {
                                    movimiento(padre);
                                } else {
                                    movimientoAdvXJug(padre);
                                }
                                break;
                            case 4:
                                if (padre.equals("hvox5")) {
                                    movimiento(padre);
                                } else {
                                    movimientoAdvXJug(padre);
                                }
                                break;
                            case 5:
                                if (padre.equals("hbox6")) {
                                    movimiento(padre);
                                } else {
                                    movimientoAdvXJug(padre);
                                }
                                break;
                        }
                    } else {
                        Mensaje msj = new Mensaje();
                        msj.show(Alert.AlertType.WARNING, "Error con carta", "No has seleccionado la carta");
                    }
                } else {//Movimiento de ladron o Error Medico
                    paneAuxiliar = (Pane) event.getSource();
                    String padre = paneAuxiliar.getParent().getId();
                    String hijoAux = hijo(padre);
                    Integer iHijo = Integer.valueOf(hijoAux);
                    switch (padre) {
                        case "hvox":
                            if (ladron) {
                                moveLadron(padre, hijoAux, cartasRival(partida.getJugadores().get(0), iHijo));
                            } else if (errorMedico) {
                                enviarCartaErrorMedicoSocket("errorMedico", padre);
                            }
                            break;
                        case "hvox2":
                            if (ladron) {
                                moveLadron(padre, hijoAux, cartasRival(partida.getJugadores().get(1), iHijo));
                            } else if (errorMedico) {
                                enviarCartaErrorMedicoSocket("errorMedico", padre);
                            }
                            break;
                        case "hvox3":
                            if (ladron) {
                                moveLadron(padre, hijoAux, cartasRival(partida.getJugadores().get(2), iHijo));
                            } else if (errorMedico) {
                                enviarCartaErrorMedicoSocket("errorMedico", padre);
                            }
                            break;
                        case "hvox4":
                            if (ladron) {
                                moveLadron(padre, hijoAux, cartasRival(partida.getJugadores().get(3), iHijo));
                            } else if (errorMedico) {
                                enviarCartaErrorMedicoSocket("errorMedico", padre);
                            }
                            break;
                        case "hvox5":
                            if (ladron) {
                                moveLadron(padre, hijoAux, cartasRival(partida.getJugadores().get(4), iHijo));
                            } else if (errorMedico) {
                                enviarCartaErrorMedicoSocket("errorMedico", padre);
                            }
                            break;
                        case "hbox6":
                            if (ladron) {
                                moveLadron(padre, hijoAux, cartasRival(partida.getJugadores().get(5), iHijo));
                            } else if (errorMedico) {
                                enviarCartaErrorMedicoSocket("errorMedico", padre);
                            }
                            break;
                    }
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

    public int indexJugador(String variableU) {
        int variable = 0;
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

    void moveLadron(String padre, String hijo, ArrayList<CartaDto> cartas) {
        if (!cartas.isEmpty()) {
            if (!cartas.get(0).getEstado().equals("Inmunizado")) {
                if (jugador.getCartas1().isEmpty() && jugador.getCartas2().isEmpty() && jugador.getCartas3().isEmpty() && jugador.getCartas4().isEmpty() && jugador.getCartas5().isEmpty()) {
                    enviarCartaLadronSocket("Ladron", padre, hijo, jugador.getIP());
                } else if ((!jugador.getCartas1().isEmpty() //Organo en los campos vacios
                        ? !cartas.get(0).getTipoCarta().equals(jugador.getCartas1().get(0).getTipoCarta())
                        : true)
                        && (!jugador.getCartas2().isEmpty()
                        ? !cartas.get(0).getTipoCarta().equals(jugador.getCartas2().get(0).getTipoCarta())
                        : true)
                        && (!jugador.getCartas3().isEmpty()
                        ? !cartas.get(0).getTipoCarta().equals(jugador.getCartas3().get(0).getTipoCarta())
                        : true)
                        && (!jugador.getCartas4().isEmpty()
                        ? !cartas.get(0).getTipoCarta().equals(jugador.getCartas4().get(0).getTipoCarta())
                        : true)
                        && (!jugador.getCartas5().isEmpty()
                        ? !cartas.get(0).getTipoCarta().equals(jugador.getCartas5().get(0).getTipoCarta())
                        : true)) {
                    enviarCartaLadronSocket("Ladron", padre, hijo, jugador.getIP());
                } else {
                    new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No puedes seleccionar esta carta");
                }
            } else {
                new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No puedes robar órganos inmunizados");
            }

        } else {
            new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "Esta pila no posee cartas en ella.");
        }
    }

    EventHandler<MouseEvent> seleccionarCarta = event -> {
        if (!findePartida) {
            jugador = (JugadorDto) AppContext.getInstance().get("JugadorDto");
            if (jugador.getTurno() && !ladron) {
                imageViewDesechada = ((ImageView) event.getSource());
                switch (((ImageView) event.getSource()).getId()) {
                    case "carta3":
                        cartaAux = carta3;
                        if (imageViewDesechada.getImage() != null) {
                            vBox.getStyleClass().clear();
                            vBox.getStyleClass().add("hVoxActivo");
                            vBox2.getStyleClass().clear();
                            vBox2.getStyleClass().add("hVoxActivo");
                            vBox3.getStyleClass().clear();
                            vBox3.getStyleClass().add("hVoxActivo2");
                        }
                        break;
                    case "carta2":
                        cartaAux = carta2;
                        if (imageViewDesechada.getImage() != null) {
                            vBox.getStyleClass().clear();
                            vBox.getStyleClass().add("hVoxActivo");
                            vBox2.getStyleClass().clear();
                            vBox2.getStyleClass().add("hVoxActivo2");
                            vBox3.getStyleClass().clear();
                            vBox3.getStyleClass().add("hVoxActivo");
                        }
                        break;
                    default:
                        cartaAux = carta1;
                        if (imageViewDesechada.getImage() != null) {
                            vBox.getStyleClass().clear();
                            vBox.getStyleClass().add("hVoxActivo2");
                            vBox2.getStyleClass().clear();
                            vBox2.getStyleClass().add("hVoxActivo");
                            vBox3.getStyleClass().clear();
                            vBox3.getStyleClass().add("hVoxActivo");
                        }
                        break;
                }
            } else {
                if (ladron) {
                    Mensaje ms = new Mensaje();
                    ms.show(Alert.AlertType.WARNING, "Información de Juego", "Estás en modo ladron");
                } else {
                    Mensaje ms = new Mensaje();
                    ms.show(Alert.AlertType.WARNING, "Información de Juego", "Espera a tu turno");
                }

            }
        } else {
            Mensaje ms = new Mensaje();
            ms.show(Alert.AlertType.WARNING, "Información de Juego", "La partida ya ha finalizado");
        }
    };

    String hijo = "";
    VBox boxVacio = null;

    private void movimientoAdvXJug(String padre) {
        if (cartaAux.getTipoCarta().equals("Virus")
                || cartaAux.getTipoCarta().equals("Virus_Comodin") && !modoTratamiento) {
            movimientoContrario(padre);
        } else {
            if (modoTratamiento) {
                new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No puedes realizar esta acción, porque ya hiciste tu movimiento");
            } else {
                Mensaje ms = new Mensaje();
                ms.show(Alert.AlertType.WARNING, "Información de Juego", "No puedes poner tu carta en este lugar.");
            }

        }
    }

    private void movimiento(String padre, String hijo) {

        enviarCartaJuegoSocket("movimientoJugador", padre, hijo, jugador.getIP());
        modoOrgano = true;
        unSoloOrgano = true;
    }

    private void noVirus(String padre, String hijo) {
        if (!cartaAux.getTipoCarta().equals("Virus") && !cartaAux.getTipoCarta().equals("Virus_Comodin")) {
            //movimientoInmune(hijo, padre, "propio", "");
            movimiento(padre, hijo);
        } else {
            new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No puedes poner un virus en tus propias cartas");
        }
    }

    private Boolean virus(CartaDto carta) {
        return carta.getTipoCarta().equals("Virus") || carta.getTipoCarta().equals("Virus_Comodin");
    }

    /*
    *Retorna el valor del indice del pane hijo
     */
    private String hijo(String padre) {
        String i = "";
        for (Node t : fondo_juego.getChildren()) {
            if (t.getId() != null && t.getId().equals(padre)) {
                for (Node v : ((HBox) t).getChildren()) {
                    if (v.equals(paneAuxiliar)) {
                        i = String.valueOf(((HBox) t).getChildren().indexOf(v));
                    }
                }
            }
        }
        return i;
    }

    private ArrayList<CartaDto> cartasRival(JugadorDto rival, Integer indice) {
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

    private void movimientoContrario(String padre) {
        if (!findePartida) {
            if (!unSoloOrgano) {
                if (!modoDesechar) {
                    hijo = "";
                    hijo = hijo(padre);
                    Integer iHijo = Integer.valueOf(hijo);
                    switch (padre) {
                        case "hvox":
                            ArrayList<CartaDto> jug1C = cartasRival(partida.getJugadores().get(0), iHijo);
                            if (!jug1C.isEmpty()) {
                                if (!jug1C.get(0).getEstado().equals("Inmunizado")
                                        && (jug1C.get(0).getColor().equals(cartaAux.getColor())
                                        || jug1C.get(0).getTipoCarta().equals("Organo_Comodin") || cartaAux.getTipoCarta().equals("Virus_Comodin"))) {
                                    String IP2 = partida.getJugadores().get(0).getIP();
                                    //movimientoInmune(hijo, padre, "", IP2);
                                    enviarCartaJuegoSocket("movimientoJugador", padre, hijo, IP2);
                                } else {
                                    if (jug1C.get(0).getEstado().equals("Inmunizado")) {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "El órgano está inmunizado");
                                    } else if (!jug1C.get(0).getColor().equals(cartaAux.getColor())) {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner cartas de distinto color");
                                    } else {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "Movimiento no permitido");
                                    }
                                }
                            } else {
                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No puedes poner tu carta en este lugar");
                            }
                            break;
                        case "hvox2":
                            ArrayList<CartaDto> jug2C = cartasRival(partida.getJugadores().get(1), iHijo);
                            if (!jug2C.isEmpty()) {
                                if (!jug2C.get(0).getEstado().equals("Inmunizado") && (jug2C.get(0).getColor().equals(cartaAux.getColor())
                                        || jug2C.get(0).getTipoCarta().equals("Organo_Comodin") || cartaAux.getTipoCarta().equals("Virus_Comodin"))) {
                                    String IP2 = partida.getJugadores().get(1).getIP();
                                    //movimientoInmune(hijo, padre, "", IP2);
                                    enviarCartaJuegoSocket("movimientoJugador", padre, hijo, IP2);
                                } else {
                                    if (jug2C.get(0).getEstado().equals("Inmunizado")) {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "El órgano está inmunizado");
                                    } else if (!jug2C.get(0).getColor().equals(cartaAux.getColor())) {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner cartas de distinto color");
                                    } else {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "Movimiento no permitido");
                                    }
                                }
                            } else {
                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No puedes poner tu carta en este lugar");
                            }
                            break;
                        case "hvox3":
                            ArrayList<CartaDto> jug3C = cartasRival(partida.getJugadores().get(2), iHijo);
                            if (!jug3C.isEmpty()) {
                                if (!jug3C.get(0).getEstado().equals("Inmunizado") && (jug3C.get(0).getColor().equals(cartaAux.getColor())
                                        || jug3C.get(0).getTipoCarta().equals("Organo_Comodin") || cartaAux.getTipoCarta().equals("Virus_Comodin"))) {
                                    String IP2 = partida.getJugadores().get(2).getIP();
                                    //movimientoInmune(hijo, padre, "", IP2);
                                    enviarCartaJuegoSocket("movimientoJugador", padre, hijo, IP2);
                                } else {
                                    if (jug3C.get(0).getEstado().equals("Inmunizado")) {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "El órgano está inmunizado");
                                    } else if (!jug3C.get(0).getColor().equals(cartaAux.getColor())) {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner cartas de distinto color");
                                    } else {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "Movimiento no permitido");

                                    }
                                }
                            } else {
                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No puedes poner tu carta en este lugar");
                            }
                            break;
                        case "hvox4":
                            ArrayList<CartaDto> jug4C = cartasRival(partida.getJugadores().get(3), iHijo);
                            if (!jug4C.isEmpty()) {
                                if (!jug4C.get(0).getEstado().equals("Inmunizado") && (jug4C.get(0).getColor().equals(cartaAux.getColor())
                                        || jug4C.get(0).getTipoCarta().equals("Organo_Comodin") || cartaAux.getTipoCarta().equals("Virus_Comodin"))) {
                                    String IP2 = partida.getJugadores().get(3).getIP();
                                    //movimientoInmune(hijo, padre, "", IP2);
                                    enviarCartaJuegoSocket("movimientoJugador", padre, hijo, IP2);
                                } else {
                                    if (jug4C.get(0).getEstado().equals("Inmunizado")) {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "El órgano está inmunizado");
                                    } else if (!jug4C.get(0).getColor().equals(cartaAux.getColor())) {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner cartas de distinto color");
                                    } else {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "Movimiento no permitido");
                                    }
                                }
                            } else {
                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No puedes poner tu carta en este lugar");
                            }
                            break;
                        case "hvox5":
                            ArrayList<CartaDto> jug5C = cartasRival(partida.getJugadores().get(4), iHijo);
                            if (!jug5C.isEmpty()) {
                                if (!jug5C.get(0).getEstado().equals("Inmunizado") && (jug5C.get(0).getColor().equals(cartaAux.getColor())
                                        || jug5C.get(0).getTipoCarta().equals("Organo_Comodin") || cartaAux.getTipoCarta().equals("Virus_Comodin"))) {
                                    String IP2 = partida.getJugadores().get(4).getIP();
                                    //movimientoInmune(hijo, padre, "", IP2);
                                    enviarCartaJuegoSocket("movimientoJugador", padre, hijo, IP2);
                                } else {
                                    if (jug5C.get(0).getEstado().equals("Inmunizado")) {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "El órgano está inmunizado");
                                    } else if (!jug5C.get(0).getColor().equals(cartaAux.getColor())) {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner cartas de distinto color");
                                    } else {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "Movimiento no permitido");
                                    }
                                }
                            } else {
                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No puedes poner tu carta en este lugar");
                            }
                            break;
                        case "hbox6":
                            ArrayList<CartaDto> jug6C = cartasRival(partida.getJugadores().get(5), iHijo);
                            if (!jug6C.isEmpty()) {
                                if (!jug6C.get(0).getEstado().equals("Inmunizado") && jug6C.get(0).getColor().equals(cartaAux.getColor())
                                        || jug6C.get(0).getTipoCarta().equals("Organo_Comodin")) {
                                    String IP2 = partida.getJugadores().get(5).getIP();
                                    //movimientoInmune(hijo, padre, "", IP2);
                                    enviarCartaJuegoSocket("movimientoJugador", padre, hijo, IP2);
                                } else {
                                    if (jug6C.get(0).getEstado().equals("Inmunizado")) {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "El órgano está inmunizado");
                                    } else if (!jug6C.get(0).getColor().equals(cartaAux.getColor())) {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner cartas de distinto color");
                                    } else {
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "Movimiento no permitido");
                                    }
                                }
                            } else {
                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No puedes poner tu carta en este lugar");
                            }
                            break;
                        default:
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

    private void movimiento(String padre) {
        if (!findePartida) {
            if (!unSoloOrgano) {
                if (!modoTratamiento) {
                    if (!modoDesechar) {
                        if (paneAuxiliar != null) {
                            hijo = "";
                            hijo = hijo(padre);
                            //si es el primer movimiento
                            if ((cartaAux.getTipoCarta().equals("Corazon") || cartaAux.getTipoCarta().equals("Estomago")
                                    || cartaAux.getTipoCarta().equals("Cerebro") || cartaAux.getTipoCarta().equals("Hueso")
                                    || cartaAux.getTipoCarta().equals("Organo_Comodin")) && jugador.getCartas1().isEmpty() && jugador.getCartas2().isEmpty() && jugador.getCartas3().isEmpty() && jugador.getCartas4().isEmpty() && jugador.getCartas5().isEmpty()) {
                                movimiento(padre, hijo);
                            } else if ((cartaAux.getTipoCarta().equals("Corazon") || cartaAux.getTipoCarta().equals("Estomago")
                                    || cartaAux.getTipoCarta().equals("Cerebro") || cartaAux.getTipoCarta().equals("Hueso")
                                    || cartaAux.getTipoCarta().equals("Organo_Comodin")) && (!jugador.getCartas1().isEmpty() //Organo en los campos vacios
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
                                            ////movimientoInmune(hijo, padre, "propio", "");
                                            movimiento(padre, hijo);
                                        } else {
                                            new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se puede poner otro órgano en este campo");
                                        }
                                        break;
                                    case "1":
                                        if (jugador.getCartas2().isEmpty()) {
                                            ////movimientoInmune(hijo, padre, "propio", "");
                                            movimiento(padre, hijo);
                                        } else {
                                            new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se puede poner otro órgano en este campo");
                                        }
                                        break;
                                    case "2":
                                        if (jugador.getCartas3().isEmpty()) {
                                            ////movimientoInmune(hijo, padre, "propio", "");
                                            movimiento(padre, hijo);
                                        } else {
                                            new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se puede poner otro órgano en este campo");
                                        }
                                        break;
                                    case "3":
                                        if (jugador.getCartas4().isEmpty()) {
                                            //movimientoInmune(hijo, padre, "propio", "");
                                            movimiento(padre, hijo);
                                        } else {
                                            new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se puede poner otro órgano en este campo");
                                        }
                                        break;
                                    case "4":
                                        if (jugador.getCartas5().isEmpty()) {
                                            //movimientoInmune(hijo, padre, "propio", "");
                                            movimiento(padre, hijo);
                                        } else {
                                            new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se puede poner otro órgano en este campo");
                                        }
                                        break;
                                }
                            } else {//Cualquier otro movimiento
                                switch (hijo) {
                                    case "0":

                                        if (!jugador.getCartas1().isEmpty()
                                                && !jugador.getCartas1().get(0).getEstado().equals("Inmunizado")
                                                && (jugador.getCartas1().get(0).getColor().equals(cartaAux.getColor())
                                                || cartaAux.getTipoCarta().equals("Medicina_Comodin")
                                                || jugador.getCartas1().get(0).getTipoCarta().equals("Organo_Comodin"))) {
                                            if (!cartaAux.getTipoCarta().equals("Corazon") && !cartaAux.getTipoCarta().equals("Estomago")
                                                    && !cartaAux.getTipoCarta().equals("Cerebro") && !cartaAux.getTipoCarta().equals("Hueso")
                                                    && !cartaAux.getTipoCarta().equals("Organo_Comodin")) {
                                                noVirus(padre, hijo);
                                            } else {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner un organo encima de otro órgano");
                                            }
                                        } else {
                                            if (jugador.getCartas1().isEmpty()) {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "Movimiento no permitido");
                                            } else if (!jugador.getCartas1().isEmpty() && !jugador.getCartas1().get(0).getColor().equals(cartaAux.getColor())) {
                                                if (virus(cartaAux)) {
                                                    new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se puede poner un virus en tu propio mazo");
                                                } else {
                                                    if (jugador.getCartas1().get(0).getEstado().equals("Inmunizado")) {
                                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "El órgano está inmunizado");
                                                    } else {
                                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner cartas de distinto color");

                                                    }
                                                }
                                            } else {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "Movimiento no permitido");
                                            }
                                        }
                                        break;
                                    case "1":
                                        if (!jugador.getCartas2().isEmpty()
                                                && !jugador.getCartas2().get(0).getEstado().equals("Inmunizado")
                                                && (jugador.getCartas2().get(0).getColor().equals(cartaAux.getColor()) || cartaAux.getTipoCarta().equals("Medicina_Comodin") || jugador.getCartas2().get(0).getTipoCarta().equals("Organo_Comodin"))) {
                                            if (!cartaAux.getTipoCarta().equals("Corazon") && !cartaAux.getTipoCarta().equals("Estomago")
                                                    && !cartaAux.getTipoCarta().equals("Cerebro") && !cartaAux.getTipoCarta().equals("Hueso")
                                                    && !cartaAux.getTipoCarta().equals("Organo_Comodin")) {
                                                noVirus(padre, hijo);
                                            } else {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner un organo encima de otro órgano");
                                            }
                                        } else {
                                            if (jugador.getCartas2().isEmpty()) {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "Movimiento no permitido");
                                            } else if (!jugador.getCartas2().isEmpty() && !jugador.getCartas2().get(0).getColor().equals(cartaAux.getColor())) {
                                                if (virus(cartaAux)) {
                                                    new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se puede poner un virus en tu propio mazo");
                                                } else {
                                                    if (jugador.getCartas2().get(0).getEstado().equals("Inmunizado")) {
                                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "El órgano está inmunizado");
                                                    } else {
                                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner cartas de distinto color");
                                                    }
                                                }
                                            } else {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "Movimiento no permitido");
                                            }
                                        }
                                        break;
                                    case "2":
                                        if (!jugador.getCartas3().isEmpty()
                                                && !jugador.getCartas3().get(0).getEstado().equals("Inmunizado")
                                                && (jugador.getCartas3().get(0).getColor().equals(cartaAux.getColor()) || cartaAux.getTipoCarta().equals("Medicina_Comodin") || jugador.getCartas3().get(0).getTipoCarta().equals("Organo_Comodin"))) {
                                            if (!cartaAux.getTipoCarta().equals("Corazon") && !cartaAux.getTipoCarta().equals("Estomago")
                                                    && !cartaAux.getTipoCarta().equals("Cerebro") && !cartaAux.getTipoCarta().equals("Hueso")
                                                    && !cartaAux.getTipoCarta().equals("Organo_Comodin")) {
                                                noVirus(padre, hijo);
                                            } else {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner un organo encima de otro órgano");
                                            }
                                        } else {
                                            if (jugador.getCartas3().isEmpty()) {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "Movimiento no permitido");
                                            } else if (!jugador.getCartas3().isEmpty() && !jugador.getCartas3().get(0).getColor().equals(cartaAux.getColor())) {
                                                if (virus(cartaAux)) {
                                                    new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se puede poner un virus en tu propio mazo");
                                                } else {
                                                    if (jugador.getCartas3().get(0).getEstado().equals("Inmunizado")) {
                                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "El órgano está inmunizado");
                                                    } else {
                                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner cartas de distinto color");

                                                    }
                                                }
                                            } else {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "Movimiento no permitido");
                                            }
                                        }
                                        break;

                                    case "3":
                                        if (!jugador.getCartas4().isEmpty()
                                                && !jugador.getCartas4().get(0).getEstado().equals("Inmunizado")
                                                && (jugador.getCartas4().get(0).getColor().equals(cartaAux.getColor()) || cartaAux.getTipoCarta().equals("Medicina_Comodin") || jugador.getCartas4().get(0).getTipoCarta().equals("Organo_Comodin"))) {
                                            if (!cartaAux.getTipoCarta().equals("Corazon") && !cartaAux.getTipoCarta().equals("Estomago")
                                                    && !cartaAux.getTipoCarta().equals("Cerebro") && !cartaAux.getTipoCarta().equals("Hueso")
                                                    && !cartaAux.getTipoCarta().equals("Organo_Comodin")) {
                                                noVirus(padre, hijo);
                                            } else {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner un organo encima de otro órgano");
                                            }
                                        } else {
                                            if (jugador.getCartas4().isEmpty()) {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "Movimiento no permitido");
                                            } else if (!jugador.getCartas4().isEmpty() && !jugador.getCartas4().get(0).getColor().equals(cartaAux.getColor())) {
                                                if (virus(cartaAux)) {
                                                    new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se puede poner un virus en tu propio mazo");
                                                } else {
                                                    if (jugador.getCartas4().get(0).getEstado().equals("Inmunizado")) {
                                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "El órgano está inmunizado");
                                                    } else {
                                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner cartas de distinto color");
                                                    }
                                                }
                                            } else {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "Movimiento no permitido");
                                            }
                                        }
                                        break;

                                    case "4":
                                        if (!jugador.getCartas5().isEmpty()
                                                && !jugador.getCartas5().get(0).getEstado().equals("Inmunizado")
                                                && (jugador.getCartas5().get(0).getColor().equals(cartaAux.getColor()) || cartaAux.getTipoCarta().equals("Medicina_Comodin") || jugador.getCartas5().get(0).getTipoCarta().equals("Organo_Comodin"))) {
                                            if (!cartaAux.getTipoCarta().equals("Corazon") && !cartaAux.getTipoCarta().equals("Estomago")
                                                    && !cartaAux.getTipoCarta().equals("Cerebro") && !cartaAux.getTipoCarta().equals("Hueso")
                                                    && !cartaAux.getTipoCarta().equals("Organo_Comodin")) {
                                                noVirus(padre, hijo);
                                            } else {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner un organo encima de otro órgano");
                                            }
                                        } else {
                                            if (jugador.getCartas5().isEmpty()) {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "Movimiento no permitido");
                                            } else if (!jugador.getCartas5().isEmpty() && !jugador.getCartas5().get(0).getColor().equals(cartaAux.getColor())) {
                                                if (virus(cartaAux)) {
                                                    new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se puede poner un virus en tu propio mazo");
                                                } else {
                                                    if (jugador.getCartas5().get(0).getEstado().equals("Inmunizado")) {
                                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "El órgano está inmunizado");
                                                    } else {
                                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner cartas de distinto color");

                                                    }
                                                }
                                            } else {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "Movimiento no permitido");
                                            }
                                        }
                                        break;
                                    /*default:
                                    Mensaje ms = new Mensaje();
                                    ms.show(Alert.AlertType.WARNING, "Información de Juego", "Ya hay un tipo de organo en este mazo....");
                                    break;*/
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
                    new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No puedes realizar esta acción, porque ya hiciste tu movimiento");
                }
            } else {
                Mensaje msj = new Mensaje();
                msj.show(Alert.AlertType.WARNING, "Información de Juego", "No puedes agregar un órgano si has agregado uno previamente");//.l.
            }

        } else {
            Mensaje msj = new Mensaje();
            msj.show(Alert.AlertType.WARNING, "Información de Juego", "La partida ya ha finalizado");
        }
    }

    @Override
    public void initialize() {

    }

    public static void ObtenerCarta(String IP_Servidor) {
        try {
            Socket socket = new Socket(IP_Servidor, 44440);
            DataOutputStream mensaje = new DataOutputStream(socket.getOutputStream());
            //DataInputStream respuesta = new DataInputStream(socket.getInputStream());
            //Enviamos un mensaje
            mensaje.writeUTF("pedirCartas");
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            entrada.readUTF();
            //Cerramos la conexión
            socket.close();
            Socket socket2 = new Socket(IP_Servidor, 44440);
            DataInputStream respuesta2 = new DataInputStream(socket2.getInputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(respuesta2);
            CartaDto carta = (CartaDto) objectInputStream.readObject();
            if (carta == null) {
                OutputStream outputstream = socket2.getOutputStream();
                ObjectOutputStream objectoutputstream = new ObjectOutputStream(outputstream);
                objectoutputstream.writeObject(partida.getDesechadas());
                carta = (CartaDto) objectInputStream.readObject();

                socket = new Socket(IP_Servidor, 44440);
                mensaje = new DataOutputStream(socket.getOutputStream());
                DataInputStream respuesta = new DataInputStream(socket.getInputStream());
                //Enviamos un mensaje
                mensaje.writeUTF("mazoTerminado");
                respuesta.readUTF();
                //Cerramos la conexión
                socket.close();

                /*Platform.runLater(() -> {
                    desechadas.setImage(null);
                });*/
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
            System.out.println(e);
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    @FXML
    private void CartaDesechada(MouseEvent event) {
        if (!findePartida) {
            if (!ladron) {
                if (!errorMedico) {
                    if (!modoOrgano) {
                        if (!modoTratamiento) {
                            if (!recogioCarta) {
                                if (jugador.getTurno()) {
                                    if (cartaAux != null) {
                                        switch (cartaAux.getTipoCarta()) {
                                            case "Transplante":
                                                if (!modoDesechar) {
                                                    modoTratamiento = true;
                                                    desecharCarta("desecharCarta");
                                                } else {
                                                    new Mensaje().show(Alert.AlertType.INFORMATION, "Información de Juego", "No puedes realizar esta acción, porque ya hiciste desechaste cartas");
                                                }
                                                break;
                                            case "Ladron":
                                                if (!modoDesechar) {
                                                    modoTratamiento = true;
                                                    movientoLadron();
                                                    desecharCarta("desecharCarta");
                                                } else {
                                                    new Mensaje().show(Alert.AlertType.INFORMATION, "Información de Juego", "No puedes realizar esta acción, porque ya hiciste desechaste cartas");
                                                }
                                                break;
                                            case "Contagio":
                                                if (!modoDesechar) {
                                                    modoTratamiento = true;
                                                    desecharCarta("desecharCarta");
                                                } else {
                                                    new Mensaje().show(Alert.AlertType.INFORMATION, "Información de Juego", "No puedes realizar esta acción, porque ya hiciste desechaste cartas");
                                                }
                                                break;
                                            case "Guante":
                                                if (!modoDesechar) {
                                                    modoTratamiento = true;
                                                    desecharCarta("desecharCarta");
                                                } else {
                                                    new Mensaje().show(Alert.AlertType.INFORMATION, "Información de Juego", "No puedes realizar esta acción, porque ya hiciste desechaste cartas");
                                                }
                                                break;
                                            case "Error":
                                                if (!modoDesechar) {
                                                    modoTratamiento = true;
                                                    errorMedico = true;
                                                    if (partida.getJugadores().stream().filter(x -> !x.getIP().equals(jugador.getIP())).
                                                            allMatch(x -> (x.getCartas1().isEmpty()
                                                            && x.getCartas2().isEmpty() && x.getCartas3().isEmpty() && x.getCartas4().isEmpty()
                                                            && x.getCartas5().isEmpty()))) {
                                                        errorMedico = false;
                                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No existen campos de adversarios para ser intercambiados");
                                                    } else {
                                                        new Mensaje().show(Alert.AlertType.INFORMATION, "Información de Juego", "Error Médico");
                                                    }
                                                } else {
                                                    new Mensaje().show(Alert.AlertType.INFORMATION, "Información de Juego", "No puedes realizar esta acción, porque ya hiciste desechaste cartas");
                                                }
                                                desecharCarta("desecharCarta");
                                                break;
                                            default:
                                                modoDesechar = true;
                                                desecharCarta("desecharCarta");
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
                                ms.show(Alert.AlertType.WARNING, "Información de Juego", "No puedes desechar una carta en este momento");
                            }
                        } else {
                            new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No puedes realizar esta acción, porque ya hiciste tu movimiento");
                        }
                    } else {
                        Mensaje ms = new Mensaje();
                        ms.show(Alert.AlertType.WARNING, "Información de Juego", "No puedes desechar una carta si ya botaste un organo");
                    }
                } else {
                    Mensaje ms = new Mensaje();
                    ms.show(Alert.AlertType.WARNING, "Información de Juego", "Estás en error médico");
                }
            } else {
                Mensaje ms = new Mensaje();
                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Estás en modo ladron");
            }
        } else {
            Mensaje ms = new Mensaje();
            ms.show(Alert.AlertType.WARNING, "Información de Juego", "La partida ya ha finalizado");
        }
    }

    private void movientoLadron() {
        ladron = true;
        if (!jugador.getCartas1().isEmpty() && !jugador.getCartas2().isEmpty() && !jugador.getCartas3().isEmpty() && !jugador.getCartas4().isEmpty() && !jugador.getCartas5().isEmpty()) {
            ladron = false;
            new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "Esta carta no tendrá efecto porque no tienes campos disponibles");
        } else {
            if (partida.getJugadores().stream().filter(x -> !x.getIP().equals(jugador.getIP())).
                    allMatch(x -> (x.getCartas1().isEmpty()
                    && x.getCartas2().isEmpty() && x.getCartas3().isEmpty() && x.getCartas4().isEmpty()
                    && x.getCartas5().isEmpty()))) {
                ladron = false;
                new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No existen campos de adversarios para ser intercambiados");
            } else {
                ArrayList<JugadorDto> jugadores = (ArrayList<JugadorDto>) partida.getJugadores().stream().filter(x -> !x.getIP().equals(jugador.getIP())).collect(Collectors.toList());

                ArrayList<CartaDto> cartasJug = new ArrayList();
                jugadores.stream().map((jug) -> {
                    if (!jug.getCartas1().isEmpty()) {
                        cartasJug.add(jug.getCartas1().get(0));
                    }
                    return jug;
                }).map((jug) -> {
                    if (!jug.getCartas2().isEmpty()) {
                        cartasJug.add(jug.getCartas2().get(0));
                    }
                    return jug;
                }).map((jug) -> {
                    if (!jug.getCartas3().isEmpty()) {
                        cartasJug.add(jug.getCartas3().get(0));
                    }
                    return jug;
                }).map((jug) -> {
                    if (!jug.getCartas5().isEmpty()) {
                        cartasJug.add(jug.getCartas5().get(0));
                    }
                    return jug;
                }).filter((jug) -> (!jug.getCartas4().isEmpty())).forEachOrdered((jug) -> {
                    cartasJug.add(jug.getCartas4().get(0));
                });

                Boolean existe = false;
                for (CartaDto carta : cartasJug) {
                    if (!existe) {
                        if (carta.getTipoCarta().equals("Organo_Comodin")) {
                            existe = true;
                        } else {
                            if (!jugador.getCartas1().isEmpty() && !jugador.getCartas1().get(0).getColor().equals(carta.getColor())) {
                                existe = true;
                            }
                            if (!jugador.getCartas2().isEmpty() && !jugador.getCartas2().get(0).getColor().equals(carta.getColor())) {
                                existe = true;
                            }
                            if (!jugador.getCartas3().isEmpty() && !jugador.getCartas3().get(0).getColor().equals(carta.getColor())) {
                                existe = true;
                            }
                            if (!jugador.getCartas4().isEmpty() && !jugador.getCartas4().get(0).getColor().equals(carta.getColor())) {
                                existe = true;
                            }
                            if (!jugador.getCartas5().isEmpty() && !jugador.getCartas5().get(0).getColor().equals(carta.getColor())) {
                                existe = true;
                            }
                        }
                    }
                }

                if (!existe) {
                    ladron = false;
                    new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No existen campos de adversarios para ser intercambiados");
                } else {
                    Mensaje ms = new Mensaje();
                    ms.show(Alert.AlertType.INFORMATION, "Información de Juego", "Estás en modo ladron");
                }

            }

        }
    }

    public void desecharCarta(String Mensaje) {
        try {
            Socket socket = new Socket(jugador.getIPS(), 44440);
            DataOutputStream mensaje = new DataOutputStream(socket.getOutputStream());
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            mensaje.writeUTF(Mensaje);
            String mensajeRecibido = "";
            mensajeRecibido = entrada.readUTF();
            socket.close();

            Socket socket2 = new Socket(jugador.getIPS(), 44440);
            OutputStream outputStream = socket2.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(cartaAux);
            socket2.close();
            jugador.getMazo().remove(cartaAux);//removemos la carta del mazo del  jugador 
            imageViewDesechada.setImage(null);
            cartaAux = null;
            vBox.getStyleClass().clear();
            vBox.getStyleClass().add("hVoxActivo");
            vBox2.getStyleClass().clear();
            vBox2.getStyleClass().add("hVoxActivo");
            vBox3.getStyleClass().clear();
            vBox3.getStyleClass().add("hVoxActivo");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void enviarCartaJuegoSocket(String Mensaje, String padre, String hijo, String IP) {
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
            //Envia la IP del jugador destino que ha hecho este jugador
            mensaje2.writeUTF(IP);
            System.out.println("Closing socket and terminating program.");
            socket2.close();
            imageViewDesechada.setImage(null);
            System.out.println(jugador.getMazo().remove(cartaAux));//removemos la carta del mazo del  jugador 
            vBox.getStyleClass().clear();
            vBox.getStyleClass().add("hVoxActivo");
            vBox2.getStyleClass().clear();
            vBox2.getStyleClass().add("hVoxActivo");
            vBox3.getStyleClass().clear();
            vBox3.getStyleClass().add("hVoxActivo");
            cartaAux = null;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    //Envía las cartas a los jugadores
    private void enviarCartaLadronSocket(String Mensaje, String padre, String hijo, String IP) {
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
            System.out.println("Connected Text!");
            DataOutputStream mensaje2 = new DataOutputStream(socket2.getOutputStream());
            mensaje2.writeUTF(padre);
            mensaje2.writeUTF(hijo);
            mensaje2.writeUTF(IP);
            System.out.println("Mensjaes enviados");
            socket2.close();

            ladron = false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void enviarCartaErrorMedicoSocket(String Mensaje, String padre) {
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
            System.out.println("Connected Text!");
            DataOutputStream mensaje2 = new DataOutputStream(socket2.getOutputStream());
            mensaje2.writeUTF(padre);
            System.out.println("Mensjaes enviados");
            socket2.close();

            errorMedico = false;
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
                        ladron = false;
                        errorMedico = false;
                        modoTratamiento = false;
                        jugador.setTurno(false);

                        vBox.getStyleClass().clear();
                        vBox.getStyleClass().add("hVoxActivo");
                        vBox2.getStyleClass().clear();
                        vBox2.getStyleClass().add("hVoxActivo");
                        vBox3.getStyleClass().clear();
                        vBox3.getStyleClass().add("hVoxActivo");
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
            if (!ladron) {
                if (!errorMedico) {
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
                    ms.show(Alert.AlertType.WARNING, "Información de Juego", "Estás en error médico");
                }
            } else {
                Mensaje ms = new Mensaje();
                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Estás en modo ladron");
            }
        } else {
            Mensaje ms = new Mensaje();
            ms.show(Alert.AlertType.WARNING, "Información de Juego", "La partida ya ha finalizado");
        }
    }
}
