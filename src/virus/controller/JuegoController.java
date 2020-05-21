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
    @FXML
    private VBox jug1_1;
    @FXML
    private VBox jug1_2;
    @FXML
    private VBox jug1_3;
    @FXML
    private VBox jug1_4;
    @FXML
    private VBox jug2_1;
    @FXML
    private VBox jug2_2;
    @FXML
    private VBox jug2_3;
    @FXML
    private VBox jug2_4;
    @FXML
    private VBox jug3_1;
    @FXML
    private VBox jug3_2;
    @FXML
    private VBox jug3_3;
    @FXML
    private VBox jug3_4;
    @FXML
    private VBox jug4_1;
    @FXML
    private VBox jug4_2;
    @FXML
    private VBox jug4_3;
    @FXML
    private VBox jug4_4;
    @FXML
    private VBox jug5_1;
    @FXML
    private VBox jug5_2;
    @FXML
    private VBox jug5_3;
    @FXML
    private VBox jug5_4;
    @FXML
    private VBox jug6_1;
    @FXML
    private VBox jug6_2;
    @FXML
    private VBox jug6_3;
    @FXML
    private VBox jug6_4;
    @FXML
    private Rectangle CartasDesechadas;
    @FXML
    private Rectangle MazoCartas;
    public static CartaDto carta1;
    public static CartaDto carta2;
    public static CartaDto carta3;
    public CartaDto cartaAux;
    @FXML
    private ImageView imgDesechada;
    private PartidaDto partida = new PartidaDto();
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
    public static VBox vboxAuxiliar;

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

        if (jugadores.size() == 1) {
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
        } else if (jugadores.size() == 3) {
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
        } else if (jugadores.size() == 4) {
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
        } else if (jugadores.size() == 5) {
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
        } else if (jugadores.size() == 6) {
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

        image7 = new ImageView("virus/resources/" + carta1.getImagen());
        image7.setId("carta1");
        image7.setFitHeight(107.25);
        image7.setFitWidth(74.75);
        image7.setLayoutX(400);
        image7.setLayoutY(450);
        image7.setOnMouseClicked(cartaAdesechar);

        image8 = new ImageView("virus/resources/" + carta2.getImagen());
        image8.setId("carta2");
        image8.setFitHeight(107.25);
        image8.setFitWidth(74.75);
        image8.setLayoutX(image7.getLayoutX() + 102.5);
        image8.setLayoutY(450);
        image8.setOnMouseClicked(cartaAdesechar);

        image9 = new ImageView("virus/resources/" + carta3.getImagen());
        image9.setId("carta3");
        image9.setFitHeight(107.25);
        image9.setFitWidth(74.75);
        image9.setLayoutX(image8.getLayoutX() + 102.5);
        image9.setLayoutY(450);
        image9.setOnMouseClicked(cartaAdesechar);

        fondo_juego.getChildren().add(image7);
        fondo_juego.getChildren().add(image8);
        fondo_juego.getChildren().add(image9);

        //Introduce los jugadores a la partida
        partida.setJugadores(jugadores);
        Hilo_Peticiones peticiones = new Hilo_Peticiones(partida, imgDesechada, jugador, lbl_JTurno, fondo_juego);
        peticiones.start();
    }
    String hijo = "";
    Boolean vacio = true;
    VBox boxVacio = null;

    private void primermovimiento(String padre) {
        
        if (vboxAuxiliar!=null && ((ImageView)vboxAuxiliar.getChildren().get(0)).getImage() == null) {
            hijo = "";
            vacio = true;

            fondo_juego.getChildren().forEach((t) -> {
                if (t.getId() != null && t.getId().equals(padre)) {
                    ((HBox) t).getChildren().forEach((v) -> {
                        if (v.equals(vboxAuxiliar)) {
                            hijo = String.valueOf(((HBox) t).getChildren().indexOf(v));
                        }
                    });
                }
            });

            enviarCartaJuegoSocket("movimientoJugador", padre, hijo);
        }else{
            
        }

    }

    EventHandler<MouseEvent> movimiento = event -> {
        if (jugador.getTurno()) {
            if (cartaAux != null) {
                if (cartaAux.getTipoCarta().equals("Corazon") || cartaAux.getTipoCarta().equals("Estomago")
                        || cartaAux.getTipoCarta().equals("Cerebro") || cartaAux.getTipoCarta().equals("Hueso") || 
                        cartaAux.getTipoCarta().equals("Organo_Comodin")) {
                    JugadorDto jugadorAux = partida.getJugadores().stream().
                            filter(x -> x.getIP().equals(jugador.getIP())).findAny().get();
                    int i = partida.getJugadores().indexOf(jugadorAux);
                    vboxAuxiliar = (VBox) event.getSource();
                    String padre = vboxAuxiliar.getParent().getId();
                    switch (i) {
                        case 0:
                            if (padre.equals("hvox")) {
                                primermovimiento(padre);
                            } else {
                                Mensaje ms = new Mensaje();
                                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Esta no es tu zona de juego.");
                            }
                            break;
                        case 1:
                            if (padre.equals("hvox2")) {
                                primermovimiento(padre);
                            } else {
                                Mensaje ms = new Mensaje();
                                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Esta no es tu zona de juego.");
                            }
                            break;

                        case 2:
                            if (padre.equals("hvox3")) {
                                primermovimiento(padre);
                            } else {
                                Mensaje ms = new Mensaje();
                                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Esta no es tu zona de juego.");
                            }
                            break;
                        case 3:
                            if (padre.equals("hvox4")) {
                                primermovimiento(padre);
                            } else {
                                Mensaje ms = new Mensaje();
                                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Esta no es tu zona de juego.");
                            }
                            break;
                        case 4:
                            if (padre.equals("hvox5")) {
                                primermovimiento(padre);
                            } else {
                                Mensaje ms = new Mensaje();
                                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Esta no es tu zona de juego.");
                            }
                            break;
                        case 5:
                            if (padre.equals("hbox6")) {
                                primermovimiento(padre);
                            } else {
                                Mensaje ms = new Mensaje();
                                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Esta no es tu zona de juego.");
                            }
                            break;
                    }
                }
            } else {
                Mensaje msj = new Mensaje();
                msj.show(Alert.AlertType.WARNING, "Error con carta", "No has seleccionado la carta");
            }
        } else {
            Mensaje ms = new Mensaje();
            ms.show(Alert.AlertType.WARNING, "Información de Juego", "No puedes realizar esta acción");
        }
    };

    EventHandler<MouseEvent> cartaAdesechar = event -> {
        if (jugador.getTurno()) {
            imageViewDesechada = ((ImageView) event.getSource());
            if (((ImageView) event.getSource()).getId().equals("carta3")) {
                cartaAux = carta3;
            } else if (((ImageView) event.getSource()).getId().equals("carta2")) {
                cartaAux = carta2;
            } else {//carta 1
                cartaAux = carta1;
            }
        } else {
            Mensaje ms = new Mensaje();
            ms.show(Alert.AlertType.WARNING, "Información de Juego", "No puedes realizar esta acción");
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
            DataOutputStream mensaje2 = new DataOutputStream(socket2.getOutputStream());
            //DataInputStream respuesta = new DataInputStream(socket2.getInputStream());
            System.out.println("Connected Text!");
            //mensaje2.writeUTF("EsperandoCarta...");

            DataInputStream respuesta2 = new DataInputStream(socket2.getInputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(respuesta2);

            CartaDto carta = (CartaDto) objectInputStream.readObject();
            jugador.getMazo().add(carta);
            if (image7.getImage() == null) {
                image7.setImage(new Image("virus/resources/" + carta.getImagen()));
                carta1 = carta;
            } else if (image8.getImage() == null) {
                image8.setImage(new Image("virus/resources/" + carta.getImagen()));
                carta2 = carta;
            } else {
                image9.setImage(new Image("virus/resources/" + carta.getImagen()));
                carta3 = carta;
            }
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
        if (!recogioCarta) {
            if (jugador.getTurno()) {
                if (cartaAux != null) {
                    enviarCartaSocket("desecharCarta");
                } else {
                    Mensaje msj = new Mensaje();
                    msj.show(Alert.AlertType.WARNING, "Error con carta", "No has seleccionado la carta");
                }
            } else {
                Mensaje ms = new Mensaje();
                ms.show(Alert.AlertType.WARNING, "Información de Juego", "No puedes realizar esta acción");
            }
        } else {
            Mensaje ms = new Mensaje();
            ms.show(Alert.AlertType.WARNING, "Información de Juego", "No puedes desechar una carta en este momento");
        }
    }

    public void enviarCartaSocket(String Mensaje) {
        try {
            System.out.println(jugador.getMazo().remove(cartaAux));
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

            imageViewDesechada.setImage(null);
            cartaAux = null;

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void enviarCartaJuegoSocket(String Mensaje, String padre, String hijo) {
        try {
            System.out.println(jugador.getMazo().remove(cartaAux));
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

            System.out.println("Closing socket and terminating program.");
            socket2.close();

            imageViewDesechada.setImage(null);
            cartaAux = null;

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void cambiarTurnoAux() {
        if (jugador.getTurno()) {
            if (jugador.getMazo().size() == 3) {
                try {
                    jugador.getMazo().remove(cartaAux);
                    Socket socket = new Socket(jugador.getIPS(), 44440);
                    DataOutputStream mensaje = new DataOutputStream(socket.getOutputStream());
                    DataInputStream entrada = new DataInputStream(socket.getInputStream());
                    System.out.println("Connected Text!");
                    mensaje.writeUTF("cambioTurno");
                    String mensajeRecibido;
                    mensajeRecibido = entrada.readUTF();
                    System.out.println(mensajeRecibido);
                    socket.close();
                    recogioCarta = false;
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                Mensaje ms = new Mensaje();
                ms.show(Alert.AlertType.WARNING, "Información de Juego", "El mazo no está completo");
            }
        } else {
            Mensaje ms = new Mensaje();
            ms.show(Alert.AlertType.WARNING, "Información de Juego", "No puedes realizar esta acción");
        }
    }

    @FXML
    private void CartadeMazo(MouseEvent event) {
        if (jugador.getTurno()) {
            if (jugador.getMazo().size() < 3) {
                recogioCarta = true;
                ObtenerCarta(jugador.getIPS());
                if (jugador.getMazo().size() == 3) {
                    cambiarTurnoAux();
                    Mensaje ms = new Mensaje();
                    ms.show(Alert.AlertType.WARNING, "Información de Juego", "Cambio de turno");
                }
            } else {
                Mensaje ms = new Mensaje();
                ms.show(Alert.AlertType.WARNING, "Información de Juego", "Usted ya tiene su mazo completo");
            }
        } else {
            Mensaje ms = new Mensaje();
            ms.show(Alert.AlertType.WARNING, "Información de Juego", "No puedes realizar esta acción");
        }
    }
}
