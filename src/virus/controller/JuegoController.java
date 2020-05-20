/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virus.controller;

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

    private ImageView omg;
    private AnchorPane anchor;
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
    public CartaDto carta1;
    public CartaDto carta2;
    public CartaDto carta3;
    @FXML
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // fondoJuego
        /*Image imgLogo;
        try {
            imgLogo = new Image("virus/resources/fondoJuego1.jpg");
            omg.setImage(imgLogo);
        } catch (Exception e) {
        }*/

        jugador = (JugadorDto) AppContext.getInstance().get("JugadorDto");
        carta1 = jugador.getMazo().get(0);
        carta2 = jugador.getMazo().get(1);
        carta3 = jugador.getMazo().get(2);

        user.setText(jugador.getNombre());

        ArrayList<JugadorDto> jugadores = (ArrayList<JugadorDto>) AppContext.getInstance().get("Jugadores");

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

        ImageView image7 = new ImageView("virus/resources/" + carta1.getImagen());
        image7.setId("carta1");
        image7.setFitHeight(107.25);
        image7.setFitWidth(74.75);
        image7.setLayoutX(300);
        image7.setLayoutY(400);
        image7.setOnMouseClicked(cartaAdesechar);

        ImageView image8 = new ImageView("virus/resources/" + carta2.getImagen());
        image8.setId("carta2");
        image8.setFitHeight(107.25);
        image8.setFitWidth(74.75);
        image8.setLayoutX(image7.getLayoutX() + 102.5);
        image8.setLayoutY(400);
        image8.setOnMouseClicked(cartaAdesechar);

        ImageView image9 = new ImageView("virus/resources/" + carta3.getImagen());
        image9.setId("carta3");
        image9.setFitHeight(107.25);
        image9.setFitWidth(74.75);
        image9.setLayoutX(image8.getLayoutX() + 102.5);
        image9.setLayoutY(400);
        image9.setOnMouseClicked(cartaAdesechar);

        anchor.getChildren().add(image7);
        anchor.getChildren().add(image8);
        anchor.getChildren().add(image9);
        
        Hilo_Peticiones peticiones = new Hilo_Peticiones(partida, imgDesechada);
        peticiones.start();
    }

    EventHandler <MouseEvent> cartaAdesechar = event -> {
        imageViewDesechada = ((ImageView) event.getSource());
        if (((ImageView) event.getSource()).getId().equals("carta3")) {
            cartaAux = carta3;
        } else if (((ImageView) event.getSource()).getId().equals("carta2")) {
            cartaAux = carta2;
        } else {//carta 1
            cartaAux = carta1;
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
    private void obtenerCarta(MouseEvent event) {
        ObtenerCarta(jugador.getIPS());
        jugador.getMazo().forEach(action -> {
            System.out.println(action.getImagen());
        });
    }

    @FXML
    private void CartaDesechada(MouseEvent event) {

        if (cartaAux != null) {
            try {
                jugador.getMazo().remove(cartaAux);
                Socket socket = new Socket(jugador.getIPS(), 44440);
                DataOutputStream mensaje = new DataOutputStream(socket.getOutputStream());
                DataInputStream entrada = new DataInputStream(socket.getInputStream());
                System.out.println("Connected Text!");
                mensaje.writeUTF("desecharCarta");
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
        } else {
            Mensaje msj = new Mensaje();
            msj.show(Alert.AlertType.WARNING, "Error con carta", "No has seleccionado la carta");
        }
    }
}
