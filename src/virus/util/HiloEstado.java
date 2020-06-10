/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virus.util;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import virus.model.JugadorDto;
import virus.model.PartidaDto;

/**
 *
 * @author Usuario
 */
public class HiloEstado {

    private Timer timer = new Timer();
    private int tic = 0;
    private Pane pane;
    Boolean inmune;
    String estado ="";
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            tic++;
            System.out.println("TIC " + tic);
            if (tic == 5) {

                Platform.runLater(() -> {
                    if (inmune) {
                        pane.setRotate(90.0);
                        pane.setLayoutX(pane.getLayoutX() + 25.0);
                        inmune = false;
                    }
                    pane.getChildren().remove(pane.getChildren().size() - 1);
                    pane.getChildren().remove(pane.getChildren().size() - 1);
                    new Mensaje().show(Alert.AlertType.INFORMATION, "Información de Juego", estado);
                    timer.cancel();
                    task.cancel();
                    tic = 0;
                });
            }
        }
    };

    public void correrHilo(Pane pane, Boolean inmune, String estado) {
        System.out.println("PLOKKK");
        this.pane = pane;
        this.inmune = inmune;
        this.estado = estado;
        timer.schedule(task, 10, 1000);
    }
}
