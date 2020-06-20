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
import java.util.function.Consumer;
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
    public static JugadorDto jugadorActual;
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
    private boolean modoTransplante = false;
    private boolean errorMedico = false;
    private static boolean modoTratamiento = false;
    private static boolean modoContagio = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        jugadorActual = (JugadorDto) AppContext.getInstance().get("JugadorDto");
        carta1 = jugadorActual.getMazo().get(0);
        carta2 = jugadorActual.getMazo().get(1);
        carta3 = jugadorActual.getMazo().get(2);

        user.setText(jugadorActual.getNombre());

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

        if (jugadores.get(0).getIP().equals(jugadorActual.getIP())) {
            jugadorActual.setTurno(true);
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
        peticiones = new Hilo_Peticiones(partida, imgDesechada, jugadorActual, lbl_JTurno, fondo_juego, mazoImg);
        peticiones.start();

        desechadas = imgDesechada;
    }

    public String hijo1 = "";
    public String padre1 = "";
    public String hijo2 = "";
    public String padre2 = "";
    private Pane auxPane1 = null;
    private Pane auxPane2 = null;
    /*
    * EVENTOS Dinamicos de MOUSE
     */
    EventHandler<MouseEvent> movimiento = event -> {
        if (!findePartida) {
            jugadorActual = (JugadorDto) AppContext.getInstance().get("JugadorDto");
            if (jugadorActual.getTurno()) {
                if (!ladron && !errorMedico && !modoTransplante && !modoContagio) {
                    if (!modoTratamiento) {
                        if (cartaAux != null) {
                            JugadorDto jugadorAux = partida.getJugadores().stream().
                                    filter(x -> x.getIP().equals(jugadorActual.getIP())).findAny().get();
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
                    } else {
                        Mensaje msj = new Mensaje();
                        msj.show(Alert.AlertType.WARNING, "Error con carta", "No puedes realizar este movimiento");
                    }
                } else {//Movimiento de ladron o Error Medico o transplante
                    if (!modoTransplante && !modoContagio) {
                        paneAuxiliar = (Pane) event.getSource();
                        String padre = paneAuxiliar.getParent().getId();
                        String hijoAux = hijo(padre);
                        Integer iHijo = Integer.valueOf(hijoAux);
                        switch (padre) {
                            case "hvox":
                                if (ladron) {
                                    moveLadron(padre, hijoAux, cartasRival(partida.getJugadores().get(0), iHijo));
                                } else if (errorMedico) {
                                    errorMedicoSocket("errorMedico", padre);
                                }
                                break;
                            case "hvox2":
                                if (ladron) {
                                    moveLadron(padre, hijoAux, cartasRival(partida.getJugadores().get(1), iHijo));
                                } else if (errorMedico) {
                                    errorMedicoSocket("errorMedico", padre);
                                }
                                break;
                            case "hvox3":
                                if (ladron) {
                                    moveLadron(padre, hijoAux, cartasRival(partida.getJugadores().get(2), iHijo));
                                } else if (errorMedico) {
                                    errorMedicoSocket("errorMedico", padre);
                                }
                                break;
                            case "hvox4":
                                if (ladron) {
                                    moveLadron(padre, hijoAux, cartasRival(partida.getJugadores().get(3), iHijo));
                                } else if (errorMedico) {
                                    errorMedicoSocket("errorMedico", padre);
                                }
                                break;
                            case "hvox5":
                                if (ladron) {
                                    moveLadron(padre, hijoAux, cartasRival(partida.getJugadores().get(4), iHijo));
                                } else if (errorMedico) {
                                    errorMedicoSocket("errorMedico", padre);
                                }
                                break;
                            case "hbox6":
                                if (ladron) {
                                    moveLadron(padre, hijoAux, cartasRival(partida.getJugadores().get(5), iHijo));
                                } else if (errorMedico) {
                                    errorMedicoSocket("errorMedico", padre);
                                }
                                break;
                        }
                    } else if (modoTransplante) {//transplante
                        if (hijo1.isEmpty() && padre1.isEmpty()) {

                            paneAuxiliar = (Pane) event.getSource();
                            //paneAuxiliar.getStyleClass().add("hVoxActivo2");
                            auxPane1 = paneAuxiliar;
                            padre1 = paneAuxiliar.getParent().getId();
                            hijo1 = hijo(padre1);
                            auxPane1.getChildren().stream().forEach(x -> {
                                x.getStyleClass().clear();
                                x.getStyleClass().add("hVoxActivo2");
                            });
                        } else {

                            paneAuxiliar = (Pane) event.getSource();
                            //paneAuxiliar.getStyleClass().add("hVoxActivo2");
                            auxPane2 = paneAuxiliar;
                            padre2 = paneAuxiliar.getParent().getId();
                            hijo2 = hijo(padre2);
                            JugadorDto jugadorAux = (partida.getJugadores().stream().filter(x -> x.getTurno()).findAny().get());
                            String hBoxJp = padreBox(partida.getJugadores().indexOf(jugadorAux));
                            if (padre2.equals(padre1)) {
                                padre1 = "";
                                padre2 = "";
                                hijo1 = "";
                                hijo2 = "";
                                auxPane2.getChildren().stream().forEach(x -> {
                                    x.getStyleClass().clear();
                                    x.getStyleClass().add("hVoxActivo2");
                                });

                                /*auxPane1.getStyleClass().add("hVoxActivo");
                                auxPane2.getStyleClass().add("hVoxActivo");*/
                                new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No puede seleccionar al mismo jugador dos veces, seleccione  de nuevo");
                            } else if ((padre1.equals(hBoxJp) || padre2.equals(hBoxJp))) {
                                Integer IndiceJugador2 = 0;
                                Integer hijo1Aux = 0;
                                Integer hijo2Aux = 0;
                                String padreAux1 = "";
                                String padreAux2 = "";
                                String hijoAux1 = "";
                                String hijoAux2 = "";

                                if (padre1.equals(hBoxJp)) {
                                    padreAux1 = padre1;
                                    padreAux2 = padre2;
                                    hijoAux1 = hijo1;
                                    hijoAux2 = hijo2;
                                    IndiceJugador2 = padreBox(padre2);
                                    hijo1Aux = Integer.valueOf(hijo1);
                                    hijo2Aux = Integer.valueOf(hijo2);
                                } else if (padre2.equals(hBoxJp)) {
                                    padreAux1 = padre2;
                                    padreAux2 = padre1;
                                    hijoAux1 = hijo2;
                                    hijoAux2 = hijo1;
                                    IndiceJugador2 = padreBox(padre1);
                                    hijo1Aux = Integer.valueOf(hijo2);
                                    hijo2Aux = Integer.valueOf(hijo1);
                                }

                                JugadorDto jugadorAux2 = partida.getJugadores().get(IndiceJugador2);
                                ArrayList<CartaDto> listaJug1 = cartasRival(jugadorAux, hijo1Aux);
                                ArrayList<CartaDto> listaJug2 = cartasRival(jugadorAux2, hijo2Aux);
                                if (!listaJug1.isEmpty() && !listaJug2.isEmpty()) {
                                    CartaDto aux1 = listaJug1.get(0);
                                    CartaDto aux2 = listaJug2.get(0);

                                    if (aux1.getTipoCarta().equals(aux2.getTipoCarta()) && !aux1.getEstado().equals("Inmunizado") && !aux2.getEstado().equals("Inmunizado")) {
                                        padre1 = "";
                                        padre2 = "";
                                        hijo1 = "";
                                        hijo2 = "";
                                        modoTransplante = false;

                                        auxPane1.getChildren().stream().forEach(x -> {
                                            x.getStyleClass().clear();
                                            //x.getStyleClass().add("hVoxActivo");
                                        });
                                        auxPane2.getChildren().stream().forEach(x -> {
                                            x.getStyleClass().clear();
                                            //  x.getStyleClass().add("hVoxActivo");
                                        });

                                        movimientoTransplanteSocket(padreAux1, hijoAux1, padreAux2, hijoAux2);
                                    } else {
                                        Boolean existeJp = false;
                                        if (!aux2.getEstado().equals("Inmunizado")
                                                && (!jugadorAux.getCartas1().isEmpty() //Organo en los campos vacios
                                                ? !aux2.getTipoCarta().equals(jugadorAux.getCartas1().get(0).getTipoCarta())
                                                : true)
                                                && (!jugadorAux.getCartas2().isEmpty()
                                                ? !aux2.getTipoCarta().equals(jugadorAux.getCartas2().get(0).getTipoCarta())
                                                : true)
                                                && (!jugadorAux.getCartas3().isEmpty()
                                                ? !aux2.getTipoCarta().equals(jugadorAux.getCartas3().get(0).getTipoCarta())
                                                : true)
                                                && (!jugadorAux.getCartas4().isEmpty()
                                                ? !aux2.getTipoCarta().equals(jugadorAux.getCartas4().get(0).getTipoCarta())
                                                : true)
                                                && (!jugadorAux.getCartas5().isEmpty()
                                                ? !aux2.getTipoCarta().equals(jugadorAux.getCartas5().get(0).getTipoCarta())
                                                : true)) {
                                            existeJp = true;
                                        } else if (aux2.getTipoCarta().equals("Organo_Comodin")) {
                                            existeJp = true;
                                        }

                                        Boolean existeJn = false;
                                        if (!aux1.getEstado().equals("Inmunizado")
                                                && (!jugadorAux2.getCartas1().isEmpty() //Organo en los campos vacios
                                                ? !aux1.getTipoCarta().equals(jugadorAux2.getCartas1().get(0).getTipoCarta())
                                                : true)
                                                && (!jugadorAux2.getCartas2().isEmpty()
                                                ? !aux1.getTipoCarta().equals(jugadorAux2.getCartas2().get(0).getTipoCarta())
                                                : true)
                                                && (!jugadorAux2.getCartas3().isEmpty()
                                                ? !aux1.getTipoCarta().equals(jugadorAux2.getCartas3().get(0).getTipoCarta())
                                                : true)
                                                && (!jugadorAux2.getCartas4().isEmpty()
                                                ? !aux1.getTipoCarta().equals(jugadorAux2.getCartas4().get(0).getTipoCarta())
                                                : true)
                                                && (!jugadorAux2.getCartas5().isEmpty()
                                                ? !aux1.getTipoCarta().equals(jugadorAux2.getCartas5().get(0).getTipoCarta())
                                                : true)) {
                                            existeJn = true;
                                        } else if (aux1.getTipoCarta().equals("Organo_Comodin")) {
                                            existeJn = true;
                                        }

                                        if (!existeJn || !existeJp) {
                                            padre1 = "";
                                            padre2 = "";
                                            hijo1 = "";
                                            hijo2 = "";
                                            auxPane1.getChildren().stream().forEach(x -> {
                                                x.getStyleClass().clear();
                                                //x.getStyleClass().add("hVoxActivo");
                                            });
                                            auxPane2.getChildren().stream().forEach(x -> {
                                                x.getStyleClass().clear();
                                                //  x.getStyleClass().add("hVoxActivo");
                                            });
                                            new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "Estos órganos no pueden ser intercambiados");
                                        } else {
                                            padre1 = "";
                                            padre2 = "";
                                            hijo1 = "";
                                            hijo2 = "";
                                            modoTransplante = false;
                                            auxPane1.getChildren().stream().forEach(x -> {
                                                x.getStyleClass().clear();
                                                //x.getStyleClass().add("hVoxActivo");
                                            });
                                            auxPane2.getChildren().stream().forEach(x -> {
                                                x.getStyleClass().clear();
                                                //  x.getStyleClass().add("hVoxActivo");
                                            });
                                            movimientoTransplanteSocket(padreAux1, hijoAux1, padreAux2, hijoAux2);

                                        }
                                    }
                                } else {
                                    padre1 = "";
                                    padre2 = "";
                                    hijo1 = "";
                                    hijo2 = "";
                                    auxPane1.getChildren().stream().forEach(x -> {
                                        x.getStyleClass().clear();
                                        //x.getStyleClass().add("hVoxActivo");
                                    });
                                    auxPane2.getChildren().stream().forEach(x -> {
                                        x.getStyleClass().clear();
                                        //  x.getStyleClass().add("hVoxActivo");
                                    });
                                    new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No puede seleccionar un mazo vacío, seleccione de nuevo");
                                }
                            } else {
                                new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No se seleccionó su propio mazo");
                            }
                        }
                    } else if (modoContagio) {
                        if (hijo1.isEmpty() && padre1.isEmpty()) {
                            paneAuxiliar = (Pane) event.getSource();
                            //paneAuxiliar.getStyleClass().add("hVoxActivo2");
                            auxPane1 = paneAuxiliar;
                            padre1 = paneAuxiliar.getParent().getId();
                            auxPane1.getChildren().stream().forEach(x -> {
                                x.getStyleClass().clear();
                                x.getStyleClass().add("hVoxActivo2");
                            });
                            hijo1 = hijo(padre1);
                        } else {
                            paneAuxiliar = (Pane) event.getSource();
                            // paneAuxiliar.getStyleClass().add("hVoxActivo2");
                            auxPane2 = paneAuxiliar;
                            padre2 = paneAuxiliar.getParent().getId();
                            hijo2 = hijo(padre2);
                            JugadorDto jugadorAux = (partida.getJugadores().stream().filter(x -> x.getTurno()).findAny().get());
                            String hBoxJp = padreBox(partida.getJugadores().indexOf(jugadorAux));
                            if (padre2.equals(padre1)) {
                                padre1 = "";
                                padre2 = "";
                                hijo1 = "";
                                hijo2 = "";
                                auxPane2.getChildren().stream().forEach(x -> {
                                    x.getStyleClass().clear();
                                    x.getStyleClass().add("hVoxActivo2");
                                });
                                new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No puede seleccionar al mismo jugador dos veces, seleccione  de nuevo");
                            } else if ((padre1.equals(hBoxJp))) {
                                String padreAux1 = padre1;
                                String padreAux2 = padre2;
                                String hijoAux1 = hijo1;
                                String hijoAux2 = hijo2;
                                Integer IndiceJugador2 = padreBox(padre2);
                                Integer hijo1Aux = Integer.valueOf(hijo1);
                                Integer hijo2Aux = Integer.valueOf(hijo2);

                                JugadorDto jugadorAux2 = partida.getJugadores().get(IndiceJugador2);
                                ArrayList<CartaDto> listaJug1 = cartasRival(jugadorAux, hijo1Aux);
                                ArrayList<CartaDto> listaJug2 = cartasRival(jugadorAux2, hijo2Aux);
                                if (!listaJug1.isEmpty() && !listaJug2.isEmpty()) {
                                    CartaDto aux1;
                                    CartaDto aux2;
                                    if (listaJug1.size() > 1) {

                                        aux1 = listaJug1.get(1);
                                        aux2 = listaJug2.get(0);

                                        if ((aux2.getTipoCarta().equals("Organo_Comodin") || (aux1.getTipoCarta().equals("Virus_Comodin")))
                                                || (aux1.getColor().equals(aux2.getColor()) && aux1.getTipoCarta().equals("Virus"))
                                                && aux2.getEstado().equals("Sano")) {
                                            padre1 = "";
                                            padre2 = "";
                                            hijo1 = "";
                                            hijo2 = "";
                                            //modoContagio = false;
                                            auxPane1.getChildren().stream().forEach(x -> {
                                                x.getStyleClass().clear();
                                                //x.getStyleClass().add("hVoxActivo2");
                                            });
                                            auxPane2.getChildren().stream().forEach(x -> {
                                                x.getStyleClass().clear();
                                                //x.getStyleClass().add("hVoxActivo2");
                                            });
                                            movimientoContagioSocket(padreAux1, hijoAux1, padreAux2, hijoAux2);
                                            movimientoContagio();
                                        } else {

                                            padre1 = "";
                                            padre2 = "";
                                            hijo1 = "";
                                            hijo2 = "";
                                            auxPane1.getChildren().stream().forEach(x -> {
                                                x.getStyleClass().clear();
                                                //x.getStyleClass().add("hVoxActivo2");
                                            });
                                            auxPane2.getChildren().stream().forEach(x -> {
                                                x.getStyleClass().clear();
                                                //x.getStyleClass().add("hVoxActivo2");
                                            });
                                            new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No puede seleccionar estas cartas.");
                                        }
                                    } else {
                                        padre1 = "";
                                        padre2 = "";
                                        hijo1 = "";
                                        hijo2 = "";
                                        auxPane1.getChildren().stream().forEach(x -> {
                                            x.getStyleClass().clear();
                                            //x.getStyleClass().add("hVoxActivo2");
                                        });
                                        auxPane2.getChildren().stream().forEach(x -> {
                                            x.getStyleClass().clear();
                                            //x.getStyleClass().add("hVoxActivo2");
                                        });
                                        new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No puede seleccionar un mazo vacío, no posee virus");
                                    }
                                } else {
                                    padre1 = "";
                                    padre2 = "";
                                    hijo1 = "";
                                    hijo2 = "";
                                    auxPane1.getChildren().stream().forEach(x -> {
                                        x.getStyleClass().clear();
                                        //x.getStyleClass().add("hVoxActivo2");
                                    });
                                    auxPane2.getChildren().stream().forEach(x -> {
                                        x.getStyleClass().clear();
                                        //x.getStyleClass().add("hVoxActivo2");
                                    });
                                    new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No puede seleccionar un mazo vacío, seleccione de nuevo");
                                }
                            } else {
                                padre1 = "";
                                padre2 = "";
                                hijo1 = "";
                                hijo2 = "";
                                auxPane1.getChildren().stream().forEach(x -> {
                                    x.getStyleClass().clear();
                                    //x.getStyleClass().add("hVoxActivo2");
                                });
                                auxPane2.getChildren().stream().forEach(x -> {
                                    x.getStyleClass().clear();
                                    //x.getStyleClass().add("hVoxActivo2");
                                });
                                new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "Debes de seleccionar primero el virus");
                            }
                        }
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

    public Integer padreBox(String box) {
        switch (box) {
            case "hvox":
                return 0;
            case "hvox2":
                return 1;
            case "hvox3":
                return 2;
            case "hvox4":
                return 3;
            case "hvox5":
                return 4;
            case "hbox6":
                return 5;
            default:
                return null;
        }
    }

    public String padreBox(int i) {
        switch (i) {
            case 0:
                return "hvox";
            case 1:
                return "hvox2";
            case 2:
                return "hvox3";
            case 3:
                return "hvox4";
            case 4:
                return "hvox5";
            case 5:
                return "hbox6";
            default:
                return "";
        }
    }

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
                if (jugadorActual.getCartas1().isEmpty() && jugadorActual.getCartas2().isEmpty() && jugadorActual.getCartas3().isEmpty() && jugadorActual.getCartas4().isEmpty() && jugadorActual.getCartas5().isEmpty()) {
                    enviarCartaLadronSocket("Ladron", padre, hijo, jugadorActual.getIP());
                } else if ((!jugadorActual.getCartas1().isEmpty() //Organo en los campos vacios
                        //Organo en los campos vacios
                        ? !cartas.get(0).getTipoCarta().equals(jugadorActual.getCartas1().get(0).getTipoCarta())
                        : true)
                        && (!jugadorActual.getCartas2().isEmpty()
                        ? !cartas.get(0).getTipoCarta().equals(jugadorActual.getCartas2().get(0).getTipoCarta())
                        : true)
                        && (!jugadorActual.getCartas3().isEmpty()
                        ? !cartas.get(0).getTipoCarta().equals(jugadorActual.getCartas3().get(0).getTipoCarta())
                        : true)
                        && (!jugadorActual.getCartas4().isEmpty()
                        ? !cartas.get(0).getTipoCarta().equals(jugadorActual.getCartas4().get(0).getTipoCarta())
                        : true)
                        && (!jugadorActual.getCartas5().isEmpty()
                        ? !cartas.get(0).getTipoCarta().equals(jugadorActual.getCartas5().get(0).getTipoCarta())
                        : true)) {
                    enviarCartaLadronSocket("Ladron", padre, hijo, jugadorActual.getIP());
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
            jugadorActual = (JugadorDto) AppContext.getInstance().get("JugadorDto");

            if (jugadorActual.getTurno() && !ladron) {
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

        enviarCartaJuegoSocket("movimientoJugador", padre, hijo, jugadorActual.getIP());
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
                                    || cartaAux.getTipoCarta().equals("Organo_Comodin")) && jugadorActual.getCartas1().isEmpty() && jugadorActual.getCartas2().isEmpty() && jugadorActual.getCartas3().isEmpty() && jugadorActual.getCartas4().isEmpty() && jugadorActual.getCartas5().isEmpty()) {
                                movimiento(padre, hijo);
                            } else if ((cartaAux.getTipoCarta().equals("Corazon") || cartaAux.getTipoCarta().equals("Estomago")
                                    || cartaAux.getTipoCarta().equals("Cerebro") || cartaAux.getTipoCarta().equals("Hueso")
                                    || cartaAux.getTipoCarta().equals("Organo_Comodin")) && (!jugadorActual.getCartas1().isEmpty() //Organo en los campos vacios
                                    //Organo en los campos vacios
                                    ? !cartaAux.getTipoCarta().equals(jugadorActual.getCartas1().get(0).getTipoCarta())
                                    : true)
                                    && (!jugadorActual.getCartas2().isEmpty()
                                    ? !cartaAux.getTipoCarta().equals(jugadorActual.getCartas2().get(0).getTipoCarta())
                                    : true)
                                    && (!jugadorActual.getCartas3().isEmpty()
                                    ? !cartaAux.getTipoCarta().equals(jugadorActual.getCartas3().get(0).getTipoCarta())
                                    : true)
                                    && (!jugadorActual.getCartas4().isEmpty()
                                    ? !cartaAux.getTipoCarta().equals(jugadorActual.getCartas4().get(0).getTipoCarta())
                                    : true)
                                    && (!jugadorActual.getCartas5().isEmpty()
                                    ? !cartaAux.getTipoCarta().equals(jugadorActual.getCartas5().get(0).getTipoCarta())
                                    : true)) {
                                //Verifica que el organo que se desea poner no este lleno con un organo 
                                switch (hijo) {
                                    case "0":
                                        if (jugadorActual.getCartas1().isEmpty()) {
                                            ////movimientoInmune(hijo, padre, "propio", "");
                                            movimiento(padre, hijo);
                                        } else {
                                            new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se puede poner otro órgano en este campo");
                                        }
                                        break;
                                    case "1":
                                        if (jugadorActual.getCartas2().isEmpty()) {
                                            ////movimientoInmune(hijo, padre, "propio", "");
                                            movimiento(padre, hijo);
                                        } else {
                                            new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se puede poner otro órgano en este campo");
                                        }
                                        break;
                                    case "2":
                                        if (jugadorActual.getCartas3().isEmpty()) {
                                            ////movimientoInmune(hijo, padre, "propio", "");
                                            movimiento(padre, hijo);
                                        } else {
                                            new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se puede poner otro órgano en este campo");
                                        }
                                        break;
                                    case "3":
                                        if (jugadorActual.getCartas4().isEmpty()) {
                                            //movimientoInmune(hijo, padre, "propio", "");
                                            movimiento(padre, hijo);
                                        } else {
                                            new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se puede poner otro órgano en este campo");
                                        }
                                        break;
                                    case "4":
                                        if (jugadorActual.getCartas5().isEmpty()) {
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

                                        if (!jugadorActual.getCartas1().isEmpty()
                                                && !jugadorActual.getCartas1().get(0).getEstado().equals("Inmunizado")
                                                && (jugadorActual.getCartas1().get(0).getColor().equals(cartaAux.getColor())
                                                || cartaAux.getTipoCarta().equals("Medicina_Comodin")
                                                || jugadorActual.getCartas1().get(0).getTipoCarta().equals("Organo_Comodin"))) {
                                            if (!cartaAux.getTipoCarta().equals("Corazon") && !cartaAux.getTipoCarta().equals("Estomago")
                                                    && !cartaAux.getTipoCarta().equals("Cerebro") && !cartaAux.getTipoCarta().equals("Hueso")
                                                    && !cartaAux.getTipoCarta().equals("Organo_Comodin")) {
                                                noVirus(padre, hijo);
                                            } else {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner un organo encima de otro órgano");
                                            }
                                        } else {
                                            if (jugadorActual.getCartas1().isEmpty()) {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "Movimiento no permitido");
                                            } else if (!jugadorActual.getCartas1().isEmpty() && !jugadorActual.getCartas1().get(0).getColor().equals(cartaAux.getColor())) {
                                                if (virus(cartaAux)) {
                                                    new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se puede poner un virus en tu propio mazo");
                                                } else {
                                                    if (jugadorActual.getCartas1().get(0).getEstado().equals("Inmunizado")) {
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
                                        if (!jugadorActual.getCartas2().isEmpty()
                                                && !jugadorActual.getCartas2().get(0).getEstado().equals("Inmunizado")
                                                && (jugadorActual.getCartas2().get(0).getColor().equals(cartaAux.getColor()) || cartaAux.getTipoCarta().equals("Medicina_Comodin") || jugadorActual.getCartas2().get(0).getTipoCarta().equals("Organo_Comodin"))) {
                                            if (!cartaAux.getTipoCarta().equals("Corazon") && !cartaAux.getTipoCarta().equals("Estomago")
                                                    && !cartaAux.getTipoCarta().equals("Cerebro") && !cartaAux.getTipoCarta().equals("Hueso")
                                                    && !cartaAux.getTipoCarta().equals("Organo_Comodin")) {
                                                noVirus(padre, hijo);
                                            } else {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner un organo encima de otro órgano");
                                            }
                                        } else {
                                            if (jugadorActual.getCartas2().isEmpty()) {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "Movimiento no permitido");
                                            } else if (!jugadorActual.getCartas2().isEmpty() && !jugadorActual.getCartas2().get(0).getColor().equals(cartaAux.getColor())) {
                                                if (virus(cartaAux)) {
                                                    new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se puede poner un virus en tu propio mazo");
                                                } else {
                                                    if (jugadorActual.getCartas2().get(0).getEstado().equals("Inmunizado")) {
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
                                        if (!jugadorActual.getCartas3().isEmpty()
                                                && !jugadorActual.getCartas3().get(0).getEstado().equals("Inmunizado")
                                                && (jugadorActual.getCartas3().get(0).getColor().equals(cartaAux.getColor()) || cartaAux.getTipoCarta().equals("Medicina_Comodin") || jugadorActual.getCartas3().get(0).getTipoCarta().equals("Organo_Comodin"))) {
                                            if (!cartaAux.getTipoCarta().equals("Corazon") && !cartaAux.getTipoCarta().equals("Estomago")
                                                    && !cartaAux.getTipoCarta().equals("Cerebro") && !cartaAux.getTipoCarta().equals("Hueso")
                                                    && !cartaAux.getTipoCarta().equals("Organo_Comodin")) {
                                                noVirus(padre, hijo);
                                            } else {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner un organo encima de otro órgano");
                                            }
                                        } else {
                                            if (jugadorActual.getCartas3().isEmpty()) {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "Movimiento no permitido");
                                            } else if (!jugadorActual.getCartas3().isEmpty() && !jugadorActual.getCartas3().get(0).getColor().equals(cartaAux.getColor())) {
                                                if (virus(cartaAux)) {
                                                    new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se puede poner un virus en tu propio mazo");
                                                } else {
                                                    if (jugadorActual.getCartas3().get(0).getEstado().equals("Inmunizado")) {
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
                                        if (!jugadorActual.getCartas4().isEmpty()
                                                && !jugadorActual.getCartas4().get(0).getEstado().equals("Inmunizado")
                                                && (jugadorActual.getCartas4().get(0).getColor().equals(cartaAux.getColor()) || cartaAux.getTipoCarta().equals("Medicina_Comodin") || jugadorActual.getCartas4().get(0).getTipoCarta().equals("Organo_Comodin"))) {
                                            if (!cartaAux.getTipoCarta().equals("Corazon") && !cartaAux.getTipoCarta().equals("Estomago")
                                                    && !cartaAux.getTipoCarta().equals("Cerebro") && !cartaAux.getTipoCarta().equals("Hueso")
                                                    && !cartaAux.getTipoCarta().equals("Organo_Comodin")) {
                                                noVirus(padre, hijo);
                                            } else {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner un organo encima de otro órgano");
                                            }
                                        } else {
                                            if (jugadorActual.getCartas4().isEmpty()) {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "Movimiento no permitido");
                                            } else if (!jugadorActual.getCartas4().isEmpty() && !jugadorActual.getCartas4().get(0).getColor().equals(cartaAux.getColor())) {
                                                if (virus(cartaAux)) {
                                                    new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se puede poner un virus en tu propio mazo");
                                                } else {
                                                    if (jugadorActual.getCartas4().get(0).getEstado().equals("Inmunizado")) {
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
                                        if (!jugadorActual.getCartas5().isEmpty()
                                                && !jugadorActual.getCartas5().get(0).getEstado().equals("Inmunizado")
                                                && (jugadorActual.getCartas5().get(0).getColor().equals(cartaAux.getColor()) || cartaAux.getTipoCarta().equals("Medicina_Comodin") || jugadorActual.getCartas5().get(0).getTipoCarta().equals("Organo_Comodin"))) {
                                            if (!cartaAux.getTipoCarta().equals("Corazon") && !cartaAux.getTipoCarta().equals("Estomago")
                                                    && !cartaAux.getTipoCarta().equals("Cerebro") && !cartaAux.getTipoCarta().equals("Hueso")
                                                    && !cartaAux.getTipoCarta().equals("Organo_Comodin")) {
                                                noVirus(padre, hijo);
                                            } else {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se pueden poner un organo encima de otro órgano");
                                            }
                                        } else {
                                            if (jugadorActual.getCartas5().isEmpty()) {
                                                new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "Movimiento no permitido");
                                            } else if (!jugadorActual.getCartas5().isEmpty() && !jugadorActual.getCartas5().get(0).getColor().equals(cartaAux.getColor())) {
                                                if (virus(cartaAux)) {
                                                    new Mensaje().show(Alert.AlertType.WARNING, "Información de juego", "No se puede poner un virus en tu propio mazo");
                                                } else {
                                                    if (jugadorActual.getCartas5().get(0).getEstado().equals("Inmunizado")) {
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
                                    break;*/ /*default:
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

            jugadorActual.getMazo().add(carta);

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
                                if (jugadorActual.getTurno()) {
                                    if (cartaAux != null) {
                                        switch (cartaAux.getTipoCarta()) {
                                            case "Transplante":
                                                if (!modoDesechar) {
                                                    modoTratamiento = true;
                                                    movimientoTransplante();
                                                    desecharCarta("desecharCarta");
                                                } else {
                                                    new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No puedes realizar esta acción, porque ya desechaste cartas");
                                                }
                                                break;
                                            case "Ladron":
                                                if (!modoDesechar) {
                                                    modoTratamiento = true;
                                                    movientoLadron();
                                                    desecharCarta("desecharCarta");
                                                } else {
                                                    new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No puedes realizar esta acción, porque ya desechaste cartas");
                                                }
                                                break;
                                            case "Contagio":
                                                if (!modoDesechar) {
                                                    modoTratamiento = true;
                                                    movimientoContagio();
                                                    desecharCarta("desecharCarta");
                                                } else {
                                                    new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No puedes realizar esta acción, porque ya desechaste cartas");
                                                }
                                                break;
                                            case "Guante":
                                                if (!modoDesechar) {
                                                    modoTratamiento = true;
                                                    desecharCarta("desecharCarta");
                                                } else {
                                                    new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No puedes realizar esta acción, porque ya desechaste cartas");
                                                }
                                                break;
                                            case "Error":
                                                if (!modoDesechar) {
                                                    modoTratamiento = true;
                                                    errorMedicoMetodo();
                                                    desecharCarta("desecharCarta");
                                                } else {
                                                    new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No puedes realizar esta acción, porque ya desechaste cartas");
                                                }

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

    public void movimientoTransplante() {
        modoTransplante = true;
        if (jugadorActual.getCartas1().isEmpty() && jugadorActual.getCartas2().isEmpty() && jugadorActual.getCartas3().isEmpty() && jugadorActual.getCartas4().isEmpty() && jugadorActual.getCartas5().isEmpty()) {
            modoTransplante = false;
            new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "Esta carta no tendrá efecto porque no tienes campos disponibles");
        } else if (partida.getJugadores().stream().filter(x -> !x.getIP().equals(jugadorActual.getIP())).
                allMatch(x -> x.getCartas1().isEmpty()
                && x.getCartas2().isEmpty() && x.getCartas3().isEmpty() && x.getCartas4().isEmpty()
                && x.getCartas5().isEmpty())) {
            modoTransplante = false;
            new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No existen campos de adversarios para ser intercambiados");
        } else {

            Boolean moveTratamiento = partida.getJugadores().stream().filter(x -> !x.getIP().equals(jugadorActual.getIP())).
                    allMatch(x -> (!x.getCartas1().isEmpty() //Organo en los campos vacios
                    ? x.getCartas1().get(0).getEstado().equals("Inmunizado")
                    : true)
                    && (!x.getCartas2().isEmpty()
                    ? x.getCartas2().get(0).getEstado().equals("Inmunizado")
                    : true)
                    && (!x.getCartas3().isEmpty()
                    ? x.getCartas3().get(0).getEstado().equals("Inmunizado")
                    : true)
                    && (!x.getCartas4().isEmpty()
                    ? x.getCartas4().get(0).getEstado().equals("Inmunizado")
                    : true)
                    && (!x.getCartas5().isEmpty()
                    ? x.getCartas5().get(0).getEstado().equals("Inmunizado")
                    : true));

            Boolean movePropio = (!jugadorActual.getCartas1().isEmpty() //Organo en los campos vacios
                    ? jugadorActual.getCartas1().get(0).getEstado().equals("Inmunizado")
                    : true)
                    && (!jugadorActual.getCartas2().isEmpty()
                    ? jugadorActual.getCartas2().get(0).getEstado().equals("Inmunizado")
                    : true)
                    && (!jugadorActual.getCartas3().isEmpty()
                    ? jugadorActual.getCartas3().get(0).getEstado().equals("Inmunizado")
                    : true)
                    && (!jugadorActual.getCartas4().isEmpty()
                    ? jugadorActual.getCartas4().get(0).getEstado().equals("Inmunizado")
                    : true)
                    && (!jugadorActual.getCartas5().isEmpty()
                    ? jugadorActual.getCartas5().get(0).getEstado().equals("Inmunizado")
                    : true);

            ArrayList<JugadorDto> jugadores = (ArrayList<JugadorDto>) partida.getJugadores().stream().filter(x -> !x.getIP().equals(jugadorActual.getIP())).collect(Collectors.toList());

            ArrayList<CartaDto> cartasJug = new ArrayList();
            jugadores.stream().map((jug) -> {
                if (!jug.getCartas1().isEmpty() && !jug.getCartas1().get(0).getEstado().equals("Inmunizado")) {
                    cartasJug.add(jug.getCartas1().get(0));
                }
                return jug;
            }).map((jug) -> {
                if (!jug.getCartas2().isEmpty() && !jug.getCartas2().get(0).getEstado().equals("Inmunizado")) {
                    cartasJug.add(jug.getCartas2().get(0));
                }
                return jug;
            }).map((jug) -> {
                if (!jug.getCartas3().isEmpty() && !jug.getCartas3().get(0).getEstado().equals("Inmunizado")) {
                    cartasJug.add(jug.getCartas3().get(0));
                }
                return jug;
            }).map((jug) -> {
                if (!jug.getCartas5().isEmpty() && !jug.getCartas5().get(0).getEstado().equals("Inmunizado")) {
                    cartasJug.add(jug.getCartas5().get(0));
                }
                return jug;
            }).filter((jug) -> (!jug.getCartas4().isEmpty() && !jug.getCartas4().get(0).getEstado().equals("Inmunizado"))).forEachOrdered((jug) -> {
                cartasJug.add(jug.getCartas4().get(0));
            });

            Boolean existe = false;
            for (CartaDto carta : cartasJug) {
                if ((!jugadorActual.getCartas1().isEmpty() //Organo en los campos vacios
                        ? carta.getTipoCarta().equals(jugadorActual.getCartas1().get(0).getTipoCarta()) && !jugadorActual.getCartas1().get(0).getTipoCarta().equals("Inmunizado")
                        : true)
                        || (!jugadorActual.getCartas2().isEmpty()
                        ? carta.getTipoCarta().equals(jugadorActual.getCartas2().get(0).getTipoCarta()) && !jugadorActual.getCartas2().get(0).getTipoCarta().equals("Inmunizado")
                        : true)
                        || (!jugadorActual.getCartas3().isEmpty()
                        ? carta.getTipoCarta().equals(jugadorActual.getCartas3().get(0).getTipoCarta()) && !jugadorActual.getCartas3().get(0).getTipoCarta().equals("Inmunizado")
                        : true)
                        || (!jugadorActual.getCartas4().isEmpty()
                        ? carta.getTipoCarta().equals(jugadorActual.getCartas4().get(0).getTipoCarta()) && !jugadorActual.getCartas4().get(0).getTipoCarta().equals("Inmunizado")
                        : true)
                        || (!jugadorActual.getCartas5().isEmpty()
                        ? carta.getTipoCarta().equals(jugadorActual.getCartas5().get(0).getTipoCarta()) && !jugadorActual.getCartas5().get(0).getTipoCarta().equals("Inmunizado")
                        : true)) {
                    existe = true;
                } else if ((carta.getTipoCarta().equals("Organo_Comodin") && !carta.getEstado().equals("Inmunizado"))
                        || (!jugadorActual.getCartas1().isEmpty() //Organo en los campos vacios
                        //Organo en los campos vacios
                        ? jugadorActual.getCartas1().get(0).getTipoCarta().equals("Organo_Comodin") && !jugadorActual.getCartas1().get(0).getTipoCarta().equals("Inmunizado")
                        : true)
                        || (!jugadorActual.getCartas2().isEmpty()
                        ? jugadorActual.getCartas2().get(0).getTipoCarta().equals("Organo_Comodin") && !jugadorActual.getCartas2().get(0).getTipoCarta().equals("Inmunizado")
                        : true)
                        || (!jugadorActual.getCartas3().isEmpty()
                        ? jugadorActual.getCartas3().get(0).getTipoCarta().equals("Organo_Comodin") && !jugadorActual.getCartas3().get(0).getTipoCarta().equals("Inmunizado")
                        : true)
                        || (!jugadorActual.getCartas4().isEmpty()
                        ? jugadorActual.getCartas4().get(0).getTipoCarta().equals("Organo_Comodin") && !jugadorActual.getCartas4().get(0).getTipoCarta().equals("Inmunizado")
                        : true)
                        || (!jugadorActual.getCartas5().isEmpty()
                        ? jugadorActual.getCartas5().get(0).getTipoCarta().equals("Organo_Comodin") && !jugadorActual.getCartas5().get(0).getTipoCarta().equals("Inmunizado")
                        : true)) {
                    existe = true;
                }
            }

            if (!existe) {
                Mensaje ms = new Mensaje();
                ms.show(Alert.AlertType.WARNING, "Información de Juego", "No existe movimiento por lo que la carta no tendrá efecto.");
                modoTransplante = false;
            } else if (moveTratamiento) {
                Mensaje ms = new Mensaje();
                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Las cartas de tus adversarios están inmunizadas por lo que la carta no tendrá efecto.");
                modoTransplante = false;
            } else if (movePropio) {
                Mensaje ms = new Mensaje();
                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Tus cartas están inmunizadas por lo que la carta no tendrá efecto.");
                modoTransplante = false;
            } else {
                Mensaje ms = new Mensaje();
                ms.show(Alert.AlertType.INFORMATION, "Información de Juego", "Estás en modo de Transplante, selecciona un órgano de tu mazo y otro de un jugador para realizar el transplante.");
            }
        }
    }

    public void errorMedicoMetodo() {
        errorMedico = true;

        if (partida.getJugadores().stream().filter(x -> !x.getIP().equals(jugadorActual.getIP())).
                allMatch(x -> (x.getCartas1().isEmpty()
                && x.getCartas2().isEmpty() && x.getCartas3().isEmpty() && x.getCartas4().isEmpty()
                && x.getCartas5().isEmpty()))) {
            errorMedico = false;
            new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No existen campos de adversarios para ser intercambiados");
        } else {
            new Mensaje().show(Alert.AlertType.INFORMATION, "Información de Juego", "Error Médico, selecciona el mazo de un jugador");
        }

    }

    private void movimientoContagio() {
        modoContagio = true;
        if (jugadorActual.getCartas1().isEmpty() && jugadorActual.getCartas2().isEmpty() && jugadorActual.getCartas3().isEmpty() && jugadorActual.getCartas4().isEmpty() && jugadorActual.getCartas5().isEmpty()) {
            modoContagio = false;
            new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "Esta carta no tendrá efecto porque no tienes campos disponibles");
        } else {
            if (partida.getJugadores().stream().filter(x -> !x.getIP().equals(jugadorActual.getIP())).
                    allMatch(x -> x.getCartas1().isEmpty()
                    && x.getCartas2().isEmpty() && x.getCartas3().isEmpty() && x.getCartas4().isEmpty()
                    && x.getCartas5().isEmpty())) {
                modoContagio = false;
                new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No existen campos de adversarios para ser contagiados");
            } else {
                ArrayList<JugadorDto> jugadores = (ArrayList<JugadorDto>) partida.getJugadores().stream().filter(x -> !x.getIP().equals(jugadorActual.getIP())).collect(Collectors.toList());

                ArrayList<CartaDto> cartasJug = new ArrayList();
                jugadores.stream().map((jug) -> {
                    if (!jug.getCartas1().isEmpty() && !jug.getCartas1().get(0).getEstado().equals("Inmunizado")) {
                        cartasJug.add(jug.getCartas1().get(0));
                    }
                    return jug;
                }).map((jug) -> {
                    if (!jug.getCartas2().isEmpty() && !jug.getCartas2().get(0).getEstado().equals("Inmunizado")) {
                        cartasJug.add(jug.getCartas2().get(0));
                    }
                    return jug;
                }).map((jug) -> {
                    if (!jug.getCartas3().isEmpty() && !jug.getCartas3().get(0).getEstado().equals("Inmunizado")) {
                        cartasJug.add(jug.getCartas3().get(0));
                    }
                    return jug;
                }).map((jug) -> {
                    if (!jug.getCartas5().isEmpty() && !jug.getCartas5().get(0).getEstado().equals("Inmunizado")) {
                        cartasJug.add(jug.getCartas5().get(0));
                    }
                    return jug;
                }).filter((jug) -> (!jug.getCartas4().isEmpty() && !jug.getCartas4().get(0).getEstado().equals("Inmunizado"))).forEachOrdered((jug) -> {
                    cartasJug.add(jug.getCartas4().get(0));
                });
                Boolean existe2 = false;
                if ((!jugadorActual.getCartas1().isEmpty() //Organo en los campos vacios
                        ? jugadorActual.getCartas1().get(0).getEstado().equals("Infectado") : false)
                        || (!jugadorActual.getCartas2().isEmpty()
                        ? jugadorActual.getCartas2().get(0).getEstado().equals("Infectado") : false)
                        || (!jugadorActual.getCartas3().isEmpty()
                        ? jugadorActual.getCartas3().get(0).getEstado().equals("Infectado") : false)
                        || (!jugadorActual.getCartas4().isEmpty()
                        ? jugadorActual.getCartas4().get(0).getEstado().equals("Infectado") : false)
                        || (!jugadorActual.getCartas5().isEmpty()
                        ? jugadorActual.getCartas5().get(0).getEstado().equals("Infectado") : false)) {
                    existe2 = true;
                }

                Boolean existe = false;
                if (existe2) {
                    for (CartaDto carta : cartasJug) {
                        if (!carta.getEstado().equals("Inmunizado") && !carta.getEstado().equals("Infectado") && !carta.getEstado().equals("Vacunado")
                                && (!jugadorActual.getCartas1().isEmpty() //Organo en los campos vacios
                                ? carta.getEstado().equals("Sano") && (jugadorActual.getCartas1().size() > 1 ? jugadorActual.getCartas1().get(1).getColor().equals(carta.getColor()) : false)
                                : true)
                                || (!jugadorActual.getCartas2().isEmpty()
                                ? carta.getEstado().equals("Sano") && (jugadorActual.getCartas2().size() > 1 ? jugadorActual.getCartas2().get(1).getColor().equals(carta.getColor()) : false)
                                : true)
                                || (!jugadorActual.getCartas3().isEmpty()
                                ? carta.getEstado().equals("Sano") && (jugadorActual.getCartas3().size() > 1 ? jugadorActual.getCartas3().get(1).getColor().equals(carta.getColor()) : false)
                                : true)
                                || (!jugadorActual.getCartas4().isEmpty()
                                ? carta.getEstado().equals("Sano") && (jugadorActual.getCartas4().size() > 1 ? jugadorActual.getCartas4().get(1).getColor().equals(carta.getColor()) : false)
                                : true)
                                || (!jugadorActual.getCartas5().isEmpty()
                                ? carta.getEstado().equals("Sano") && (jugadorActual.getCartas5().size() > 1 ? jugadorActual.getCartas5().get(1).getColor().equals(carta.getColor()) : false)
                                : true)) {
                            existe = true;
                        } else if ((carta.getTipoCarta().equals("Organo_Comodin") && carta.getEstado().equals("Sano")) || (!jugadorActual.getCartas1().isEmpty() //Organo en los campos vacios
                                ? (jugadorActual.getCartas1().size() > 1 ? jugadorActual.getCartas1().get(1).getTipoCarta().equals("Virus_Comodin") : false)
                                : true)
                                || (!jugadorActual.getCartas2().isEmpty() //Organo en los campos vacios
                                ? (jugadorActual.getCartas2().size() > 1 ? jugadorActual.getCartas2().get(1).getTipoCarta().equals("Virus_Comodin") : false)
                                : true)
                                || (!jugadorActual.getCartas3().isEmpty() //Organo en los campos vacios
                                ? (jugadorActual.getCartas3().size() > 1 ? jugadorActual.getCartas3().get(1).getTipoCarta().equals("Virus_Comodin") : false)
                                : true)
                                || (!jugadorActual.getCartas4().isEmpty() //Organo en los campos vacios
                                ? (jugadorActual.getCartas4().size() > 1 ? jugadorActual.getCartas4().get(1).getTipoCarta().equals("Virus_Comodin") : false)
                                : true)
                                || (!jugadorActual.getCartas5().isEmpty() //Organo en los campos vacios
                                ? (jugadorActual.getCartas5().size() > 1 ? jugadorActual.getCartas5().get(1).getTipoCarta().equals("Virus_Comodin") : false)
                                : true)) {
                            existe = true;
                        }
                    }
                }

                if (!existe) {
                    modoContagio = false;
                    new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No existen campos de adversarios para ser contagiados");
                } else {
                    Mensaje ms = new Mensaje();
                    ms.show(Alert.AlertType.INFORMATION, "Información de Juego", "Estás en modo contagio, contagia los órganos de otros jugadores");
                }
            }

        }
    }

    private void movientoLadron() {
        ladron = true;
        if (!jugadorActual.getCartas1().isEmpty() && !jugadorActual.getCartas2().isEmpty() && !jugadorActual.getCartas3().isEmpty() && !jugadorActual.getCartas4().isEmpty() && !jugadorActual.getCartas5().isEmpty()) {
            ladron = false;
            new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "Esta carta no tendrá efecto porque no tienes campos disponibles");
        } else {
            if (partida.getJugadores().stream().filter(x -> !x.getIP().equals(jugadorActual.getIP())).
                    allMatch(x -> x.getCartas1().isEmpty()
                    && x.getCartas2().isEmpty() && x.getCartas3().isEmpty() && x.getCartas4().isEmpty()
                    && x.getCartas5().isEmpty())) {
                ladron = false;
                new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No existen campos de adversarios para ser intercambiados");
            } else {
                ArrayList<JugadorDto> jugadores = (ArrayList<JugadorDto>) partida.getJugadores().stream().filter(x -> !x.getIP().equals(jugadorActual.getIP())).collect(Collectors.toList());

                ArrayList<CartaDto> cartasJug = new ArrayList();
                jugadores.stream().map((jug) -> {
                    if (!jug.getCartas1().isEmpty() && !jug.getCartas1().get(0).getEstado().equals("Inmunizado")) {
                        cartasJug.add(jug.getCartas1().get(0));
                    }
                    return jug;
                }).map((jug) -> {
                    if (!jug.getCartas2().isEmpty() && !jug.getCartas2().get(0).getEstado().equals("Inmunizado")) {
                        cartasJug.add(jug.getCartas2().get(0));
                    }
                    return jug;
                }).map((jug) -> {
                    if (!jug.getCartas3().isEmpty() && !jug.getCartas3().get(0).getEstado().equals("Inmunizado")) {
                        cartasJug.add(jug.getCartas3().get(0));
                    }
                    return jug;
                }).map((jug) -> {
                    if (!jug.getCartas5().isEmpty() && !jug.getCartas5().get(0).getEstado().equals("Inmunizado")) {
                        cartasJug.add(jug.getCartas5().get(0));
                    }
                    return jug;
                }).filter((jug) -> (!jug.getCartas4().isEmpty() && !jug.getCartas4().get(0).getEstado().equals("Inmunizado"))).forEachOrdered((jug) -> {
                    cartasJug.add(jug.getCartas4().get(0));
                });

                Boolean existe = false;
                for (CartaDto carta : cartasJug) {
                    if (!carta.getEstado().equals("Inmunizado")
                            && (!jugadorActual.getCartas1().isEmpty() //Organo en los campos vacios
                            //Organo en los campos vacios
                            ? !carta.getTipoCarta().equals(jugadorActual.getCartas1().get(0).getTipoCarta())
                            : true)
                            && (!jugadorActual.getCartas2().isEmpty()
                            ? !carta.getTipoCarta().equals(jugadorActual.getCartas2().get(0).getTipoCarta())
                            : true)
                            && (!jugadorActual.getCartas3().isEmpty()
                            ? !carta.getTipoCarta().equals(jugadorActual.getCartas3().get(0).getTipoCarta())
                            : true)
                            && (!jugadorActual.getCartas4().isEmpty()
                            ? !carta.getTipoCarta().equals(jugadorActual.getCartas4().get(0).getTipoCarta())
                            : true)
                            && (!jugadorActual.getCartas5().isEmpty()
                            ? !carta.getTipoCarta().equals(jugadorActual.getCartas5().get(0).getTipoCarta())
                            : true)) {
                        existe = true;
                    } else if (carta.getTipoCarta().equals("Organo_Comodin")) {
                        existe = true;
                    }
                }

                if (!existe) {
                    ladron = false;
                    new Mensaje().show(Alert.AlertType.WARNING, "Información de Juego", "No existen campos de adversarios para ser intercambiados");
                } else {
                    Mensaje ms = new Mensaje();
                    ms.show(Alert.AlertType.INFORMATION, "Información de Juego", "Estás en modo ladron, selecciona el órgano de otro jugador");
                }

            }

        }
    }

    public void desecharCarta(String Mensaje) {
        try {
            Socket socket = new Socket(jugadorActual.getIPS(), 44440);
            DataOutputStream mensaje = new DataOutputStream(socket.getOutputStream());
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            mensaje.writeUTF(Mensaje);
            mensajeRecibido = "";
            mensajeRecibido = entrada.readUTF();
            socket.close();

            Socket socket2 = new Socket(jugadorActual.getIPS(), 44440);
            OutputStream outputStream = socket2.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(cartaAux);
            socket2.close();
            if (cartaAux.getTipoCarta().equals("Error")) {
                cartaAux = jugadorActual.getMazo().stream().filter(x -> x.getTipoCarta().equals("Error")).findAny().get();
            } else if (cartaAux.getTipoCarta().equals("Ladron")) {
                cartaAux = jugadorActual.getMazo().stream().filter(x -> x.getTipoCarta().equals("Ladron")).findAny().get();
            }
            ;
            System.out.println("DESECHAR CARTA " + jugadorActual.getMazo().remove(cartaAux));//removemos la carta del mazo del  jugador 
            imageViewDesechada.setImage(null);
            cartaAux = null;
            vBox.getStyleClass().clear();
            vBox.getStyleClass().add("hVoxActivo");
            vBox2.getStyleClass().clear();
            vBox2.getStyleClass().add("hVoxActivo");
            vBox3.getStyleClass().clear();
            vBox3.getStyleClass().add("hVoxActivo");
            JugadorDto jugador = partida.getJugadores().stream().filter(x -> x.getIP().equals(jugadorActual.getIP())).findAny().get();
            jugador.setMazo(jugadorActual.getMazo());

            AppContext.getInstance().set("JugadorDto", jugadorActual);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void enviarCartaJuegoSocket(String Mensaje, String padre, String hijo, String IP) {
        try {
            Socket socket = new Socket(jugadorActual.getIPS(), 44440);
            DataOutputStream mensaje = new DataOutputStream(socket.getOutputStream());
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            System.out.println("Connected Text!");
            mensaje.writeUTF(Mensaje);
            String mensajeRecibido = "";
            mensajeRecibido = entrada.readUTF();
            System.out.println(mensajeRecibido);
            socket.close();
            Socket socket2 = new Socket(jugadorActual.getIPS(), 44440);
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
            System.out.println(jugadorActual.getMazo().remove(cartaAux));//removemos la carta del mazo del  jugador 
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
            Socket socket = new Socket(jugadorActual.getIPS(), 44440);
            DataOutputStream mensaje = new DataOutputStream(socket.getOutputStream());
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            System.out.println("Connected Text!");
            mensaje.writeUTF(Mensaje);
            String mensajeRecibido = "";
            mensajeRecibido = entrada.readUTF();
            System.out.println(mensajeRecibido);
            socket.close();

            Socket socket2 = new Socket(jugadorActual.getIPS(), 44440);
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

    public void movimientoTransplanteSocket(String padre1, String hijo1, String padre2, String hijo2) {
        try {
            Socket socket = new Socket(jugadorActual.getIPS(), 44440);
            DataOutputStream mensaje = new DataOutputStream(socket.getOutputStream());
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            System.out.println("Connected Text!");
            mensaje.writeUTF("Transplante");
            String mensajeRecibido = "";
            mensajeRecibido = entrada.readUTF();
            System.out.println(mensajeRecibido);
            socket.close();

            Socket socket2 = new Socket(jugadorActual.getIPS(), 44440);
            System.out.println("Connected Text!");
            DataOutputStream mensaje2 = new DataOutputStream(socket2.getOutputStream());
            mensaje2.writeUTF(padre1);
            mensaje2.writeUTF(hijo1);
            mensaje2.writeUTF(padre2);
            mensaje2.writeUTF(hijo2);
            System.out.println("Mensajes enviados");
            socket2.close();
            Mensaje ms = new Mensaje();
            ms.show(Alert.AlertType.INFORMATION, "Información de Juego", "Transplante realizado con éxito");

            modoTransplante = false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void movimientoContagioSocket(String padre1, String hijo1, String padre2, String hijo2) {
        try {
            Socket socket = new Socket(jugadorActual.getIPS(), 44440);
            DataOutputStream mensaje = new DataOutputStream(socket.getOutputStream());
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            System.out.println("Connected Text!");
            mensaje.writeUTF("Contagio");
            String mensajeRecibido = "";
            mensajeRecibido = entrada.readUTF();
            System.out.println(mensajeRecibido);
            socket.close();

            Socket socket2 = new Socket(jugadorActual.getIPS(), 44440);
            System.out.println("Connected Text!");
            DataOutputStream mensaje2 = new DataOutputStream(socket2.getOutputStream());
            mensaje2.writeUTF(padre1);
            mensaje2.writeUTF(hijo1);
            mensaje2.writeUTF(padre2);
            mensaje2.writeUTF(hijo2);
            System.out.println("Mensajes enviados");
            socket2.close();
            /*Mensaje ms = new Mensaje();
            ms.show(Alert.AlertType.INFORMATION, "Información de Juego", "Contagio realizado con éxito");*/
            JugadorDto jugador = partida.getJugadores().stream().filter(x -> x.getIP().equals(jugadorActual.getIP())).findAny().get();
            jugador.setMazo(jugadorActual.getMazo());
            jugadorActual = jugador;
            AppContext.getInstance().set("JugadorDto", jugadorActual);
            //modoContagio = false;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void errorMedicoSocket(String Mensaje, String padre) {
        try {
            Socket socket = new Socket(jugadorActual.getIPS(), 44440);
            DataOutputStream mensaje = new DataOutputStream(socket.getOutputStream());
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            System.out.println("Connected Text!");
            mensaje.writeUTF(Mensaje);
            String mensajeRecibido = "";
            mensajeRecibido = entrada.readUTF();
            System.out.println(mensajeRecibido);
            socket.close();

            Socket socket2 = new Socket(jugadorActual.getIPS(), 44440);
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
            if (jugadorActual.getTurno()) {
                if (jugadorActual.getMazo().size() == 3) {
                    try {
                        Socket socket = new Socket(jugadorActual.getIPS(), 44440);
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
                        jugadorActual.setTurno(false);
                        modoTransplante = false;
                        modoContagio = false;

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
            if (!modoTransplante) {
                if (!ladron) {
                    if (!errorMedico) {
                        if (jugadorActual.getTurno()) {
                            if (jugadorActual.getMazo().size() < 3 && (image9.getImage() == null || image8.getImage() == null
                                    || image7.getImage() == null)) {
                                recogioCarta = true;
                                ObtenerCarta(jugadorActual.getIPS());
                                if (jugadorActual.getMazo().size() == 3) {
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
                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Estás en modo transplante");
            }
        } else {
            Mensaje ms = new Mensaje();
            ms.show(Alert.AlertType.WARNING, "Información de Juego", "La partida ya ha finalizado");
        }
    }
}
