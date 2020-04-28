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
import static virus.controller.InicioController.enviarObjetos;
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

            System.out.println("Confirmando conexion al cliente....");

            // Para recibir el mensaje
            mensajeRecibido = entrada.readUTF();
            System.out.println(mensajeRecibido);
            salida.writeUTF("Recibido");
            serverSocket.close();
        } catch (Exception IO) {
        }
        pantallaCarga.finalizado = true;
        FlowController.getInstance().goView("Juego");
    }
}
