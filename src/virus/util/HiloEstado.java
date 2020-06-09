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
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            tic++;
            if (tic == 5) {
                timer.cancel();
                task.cancel();
                Platform.runLater(() -> {
                    System.out.println(pane.getChildren().remove(pane.getChildren().size() - 1));
                    System.out.println(pane.getChildren().remove(pane.getChildren().size() - 1));
                    tic =0;
                });
            }
        }
    };

    public void correrHilo(Pane pane) {
        this.pane = pane;
        timer.schedule(task, 10, 1000);
    }
}
