/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virus.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import virus.util.FlowController;

/**
 *
 * @author Jose Pablo Bermudez
 */
public class MenuController extends Controller implements Initializable {
    
   
    @FXML
    private AnchorPane omg;
  
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*Image imgLogo;
        try {
            imgLogo = new Image("virus/resources/fondo2.png");
            omg.setImage(imgLogo);
        } catch (Exception e) {
        }*/
    }    
    
    @Override
    public void initialize() {
       
    }

    @FXML
    private void Salir(MouseEvent event) {
        FlowController.getInstance().salir();
    }

    @FXML
    private void Configuracion(MouseEvent event) {
    }

    @FXML
    private void Start(MouseEvent event) {
        FlowController.getInstance().goView("Inicio");
        //FlowController.getInstance().goView();
    }

    @FXML
    private void jugador(MouseEvent event) {
    }
    
}
