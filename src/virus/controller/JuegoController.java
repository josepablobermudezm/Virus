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
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import virus.util.AppContext;
import virus.util.FlowController;
import virus.util.Hilo;
/**
 * FXML Controller class
 *
 * @author Jose Pablo Bermudez
 */
public class JuegoController extends Controller implements Initializable {

    @FXML
    private ImageView omg;
    @FXML
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // fondoJuego
        Image imgLogo;
        try {
            imgLogo = new Image("virus/resources/fondoJuego1.jpg");
            omg.setImage(imgLogo);
        } catch (Exception e) {
        }

        jugador = (JugadorDto) AppContext.getInstance().get("JugadorDto");
        CartaDto carta1 = jugador.getMazo().get(0);
        CartaDto carta2 = jugador.getMazo().get(1);
        CartaDto carta3 = jugador.getMazo().get(2);
        
        
        
        user.setText(jugador.getNombre());
        
        ArrayList<JugadorDto> jugadores = (ArrayList<JugadorDto>) AppContext.getInstance().get("Jugadores");
        
        ArrayList<Label> nombres = new ArrayList();
        nombres.add(user);
        nombres.add(user2);
        nombres.add(user3);
        nombres.add(user4);
        nombres.add(user5);
        nombres.add(user6);
        
        for(int i = 0; i < jugadores.size(); i++){
            nombres.get(i).setText(jugadores.get(i).getNombre());
        }
        
        /*ImageView image1 = new ImageView("virus/resources/" + carta1.getImagen());
        image1.setFitHeight(107.25);
        image1.setFitWidth(74.75);
        image1.setLayoutX(75);
        ImageView image2 = new ImageView("virus/resources/" + carta2.getImagen());
        image2.setFitHeight(107.25);
        image2.setFitWidth(74.75);
        image2.setLayoutX(image1.getLayoutX() + 102.5);
        ImageView image3 = new ImageView("virus/resources/" + carta3.getImagen());
        image3.setFitHeight(107.25);
        image3.setFitWidth(74.75);
        image3.setLayoutX(image2.getLayoutX() + 102.5);

        anchor.getChildren().add(image1);
        anchor.getChildren().add(image2);
        anchor.getChildren().add(image3);
        
        ImageView image4 = new ImageView("virus/resources/" + carta1.getImagen());
        image4.setFitHeight(107.25);
        image4.setFitWidth(74.75);
        image4.setLayoutX(image3.getLayoutX() + 252.5);
        ImageView image5 = new ImageView("virus/resources/" + carta2.getImagen());
        image5.setFitHeight(107.25);
        image5.setFitWidth(74.75);
        image5.setLayoutX(image4.getLayoutX() + 102.5);
        ImageView image6 = new ImageView("virus/resources/" + carta3.getImagen());
        image6.setFitHeight(107.25);
        image6.setFitWidth(74.75);
        image6.setLayoutX(image5.getLayoutX() + 102.5);

        anchor.getChildren().add(image4);
        anchor.getChildren().add(image5);
        anchor.getChildren().add(image6);
        */

        ImageView image7 = new ImageView("virus/resources/" + carta1.getImagen());
        image7.setFitHeight(107.25);
        image7.setFitWidth(74.75);
        image7.setLayoutX(300);
        image7.setLayoutY(400);
        ImageView image8 = new ImageView("virus/resources/" + carta2.getImagen());
        image8.setFitHeight(107.25);
        image8.setFitWidth(74.75);
        image8.setLayoutX(image7.getLayoutX() + 102.5);
        image8.setLayoutY(400);
        ImageView image9 = new ImageView("virus/resources/" + carta3.getImagen());
        image9.setFitHeight(107.25);
        image9.setFitWidth(74.75);
        image9.setLayoutX(image8.getLayoutX() + 102.5);
        image9.setLayoutY(400);

        anchor.getChildren().add(image7);
        anchor.getChildren().add(image8);
        anchor.getChildren().add(image9);
    }

    @FXML
    private void Salir(MouseEvent event) {
        FlowController.getInstance().goView("Menu");
    }

    @Override
    public void initialize() {

    }
    
    
    public static void ObtenerCarta(String IP_Servidor){
        Socket socket;
        DataOutputStream mensaje;
        DataInputStream respuesta;
        try {
            socket = new Socket(IP_Servidor, 44440);
            mensaje = new DataOutputStream(socket.getOutputStream());
            respuesta = new DataInputStream(socket.getInputStream());
            System.out.println("Connected Text!");
            //Enviamos un mensaje
            mensaje.writeUTF("pedirCartas");
            socket.close();
            socket = new Socket(IP_Servidor, 44440);
            mensaje = new DataOutputStream(socket.getOutputStream());
            respuesta = new DataInputStream(socket.getInputStream());
            System.out.println("Connected Text!");
            mensaje.writeUTF("EsperandoCarta...");
            
            
            InputStream respuesta2 = new DataInputStream(socket.getInputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(respuesta2);
            
            CartaDto carta = (CartaDto) objectInputStream.readObject();
            jugador.getMazo().add(carta);

            String mensajeRecibido = respuesta.readUTF();//Leemos respuesta
            System.out.println(mensajeRecibido);
            //Cerramos la conexión
        } catch (UnknownHostException e ) {
            System.out.println("El host no existe o no está activo.");
        } catch (IOException e) {
            System.out.println(e + "serás?");
        } catch (ClassNotFoundException e){
            System.out.println(e + "o tú serás?");
        }
    }

    @FXML
    private void obtenerCarta(MouseEvent event) {
        ObtenerCarta(jugador.getIPS());
        jugador.getMazo().forEach(action -> {System.out.println(action.getImagen());});
    }

}
/*OutputStream outputStream = socket.getOutputStream();
            InputStream respuesta = new DataInputStream(socket.getInputStream());
            // create an object output stream from the output stream so we can send an object through it
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            ObjectInputStream objectInputStream = new ObjectInputStream(respuesta);
            //ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            // make a bunch of messages to send.
            System.out.println("Sending messages to the ServerSocket");
            objectOutputStream.writeObject(jugador);
            ArrayList<CartaDto> cartas = (ArrayList<CartaDto>) objectInputStream.readObject();
            jugador.setMazo(cartas);
            System.out.println("Closing socket and terminating program.");
            socket.close();*/