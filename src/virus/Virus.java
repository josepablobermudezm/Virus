/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virus;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.FlowController;

/**
 *
 * @author Jose Pablo Bermudez
 */
public class Virus extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        /*Parent root = FXMLLoader.load(getClass().getResource("/view/Menu.fxml"));
        
        Scene scene = new Scene(root);
        
        //FlowController.getInstance()
        
        stage.setScene(scene);
        stage.show();*/
        
        FlowController.getInstance().InitializeFlow(stage, null);
        FlowController.getInstance().Main();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
