/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virus.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import virus.model.CartaDto;
import virus.model.JugadorDto;
import virus.util.AppContext;
import virus.util.FlowController;

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

        JugadorDto jugador = (JugadorDto) AppContext.getInstance().get("JugadorDto");
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

}
