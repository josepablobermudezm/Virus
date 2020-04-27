/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virus.controller;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import virus.model.CartaDto;
import virus.model.JugadorDto;
import virus.util.FlowController;

/**
 * FXML Controller class
 *
 * @author Jose Pablo Bermudez
 */
public class InicioController extends Controller implements Initializable {

    @FXML
    private ImageView omg;
    @FXML
    private TextField txtIP;
    @FXML
    private TextField txtServidor;
    @FXML
    private TextField txtJugador;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image imgLogo;
        try {
            imgLogo = new Image("virus/resources/fondo2.png");
            omg.setImage(imgLogo);
        } catch (Exception e) {
        }
    }    

    @Override
    public void initialize() {


    }

    @FXML
    private void Salir(MouseEvent event) {
        FlowController.getInstance().goView("Menu");
    }

    @FXML
    private void Jugar(MouseEvent event) {
        try{
        Socket socket = new Socket("25.3.190.217", 44440);
        System.out.println("Connected!");

        // get the output stream from the socket.
        OutputStream outputStream = socket.getOutputStream();
        // create an object output stream from the output stream so we can send an object through it
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

        // make a bunch of messages to send.
        ArrayList<CartaDto> cartas = new ArrayList<>();
        cartas.add(new CartaDto("Virus","Roja", "XD.png","Desechada"));
        cartas.add(new CartaDto("Organo","Roja", "HOLA.png","Mazo"));
        cartas.add(new CartaDto("Medicina","Verde", "ASDF.png","Jugada"));
        
        ArrayList<CartaDto> cartasJugadas = new ArrayList<>();
        cartas.add(new CartaDto("Medicina","Verde", "ASDF.png","Jugada"));
        
        List<JugadorDto> jugadores = new ArrayList<>();
        jugadores.add(new JugadorDto(txtJugador.getText(), true, false, txtIP.getText(), cartas, cartasJugadas, txtServidor.getText()));

        System.out.println("Sending messages to the ServerSocket");
        objectOutputStream.writeObject(jugadores);

        System.out.println("Closing socket and terminating program.");
        socket.close();
        FlowController.getInstance().goView("Juego");
        }catch(Exception IO){
            
        }
        
    }
    
}
