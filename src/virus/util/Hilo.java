/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virus.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static virus.controller.InicioController.enviarObjetos;
import virus.model.CartaDto;
import virus.model.JugadorDto;
import virus.model.PartidaDto;

/**
 *
 * @author Jose Pablo Bermudez
 */
public class Hilo extends Thread {

    public Hilo() {
        super();
    }

    @Override
    public void run() {

        try {
            DataInputStream entrada;
            DataOutputStream salida;
            Socket socket;
            ServerSocket serverSocket;
            String mensajeRecibido;
            serverSocket = new ServerSocket(44440);
            System.out.println("Esperando una conexión...");
            socket = serverSocket.accept();
            System.out.println("Un cliente se ha conectado...");
            // Para los canales de entrada y salida de datos
            entrada = new DataInputStream(socket.getInputStream());

            salida = new DataOutputStream(socket.getOutputStream());

            ObjectInputStream objectInputStream = new ObjectInputStream(entrada);
            //ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            // make a bunch of messages to send.
            System.out.println("Sending messages to the ServerSocket");

            System.out.println("Confirmando conexion al cliente....");

            // Para recibir el mensaje
            mensajeRecibido = entrada.readUTF();
            System.out.println(mensajeRecibido);
            
            try {
                ArrayList<JugadorDto> jugadores;
                jugadores = (ArrayList<JugadorDto>) objectInputStream.readObject();
                AppContext.getInstance().set("Jugadores", jugadores);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Hilo.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            salida.writeUTF("Recibido");

            serverSocket.close();
        } catch (IOException IO) {
            System.out.println(IO.getMessage());
        }
        pantallaCarga.finalizado = true;
        FlowController.getInstance().goViewInWindowTransparent("Juego");
    }
}
