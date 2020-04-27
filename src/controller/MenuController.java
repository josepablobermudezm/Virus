/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import controller.Controller;
import util.FlowController;

/**
 *
 * @author Jose Pablo Bermudez
 */
public class MenuController extends Controller implements Initializable {
    
    private Label label;
    @FXML
    private ImageView omg;
    
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image imgLogo;
        try {
            imgLogo = new Image("/resources/fondo2.png");
            omg.setImage(imgLogo);
        } catch (Exception e) {
        }
    }    
    
    @Override
    public void initialize() {
        Image imgLogo;
        try {
            imgLogo = new Image("/pacmanfx/resources/fondo4.jpg");
            omg.setImage(imgLogo);
        } catch (Exception e) {
        }
    }

    @FXML
    private void Salir(MouseEvent event) {
    }

    @FXML
    private void Configuracion(MouseEvent event) {
    }

    @FXML
    private void Start(MouseEvent event) {
        
        //FlowController.getInstance().goView();
        
    }

    @FXML
    private void jugador(MouseEvent event) {
    }
    
}
