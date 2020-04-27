/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virus.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Usuario
 */
public class JugadorDto implements Serializable{
    private String nombre;
    private Boolean turno;
    private Boolean ganador;
    private String IP;
    private String IPS;
    private ArrayList <CartaDto> mazo;
    private ArrayList <CartaDto> jugadas;
    private static final long serialVersionUID = 4L;
    
    public JugadorDto() {
    }

    public JugadorDto(String nombre, Boolean turno, Boolean ganador, String IP, ArrayList<CartaDto> mazo, ArrayList<CartaDto> jugadas, String IPS ) {
        this.nombre = nombre;
        this.turno = turno;
        this.ganador = ganador;
        this.IP = IP;
        this.mazo = mazo;
        this.jugadas = jugadas;
        this.IPS = IPS;
    }

    public String getIPS() {
        return IPS;
    }

    public void setIPS(String IPS) {
        this.IPS = IPS;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean getTurno() {
        return turno;
    }

    public void setTurno(Boolean turno) {
        this.turno = turno;
    }

    public Boolean getGanador() {
        return ganador;
    }

    public void setGanador(Boolean ganador) {
        this.ganador = ganador;
    }

    public ArrayList<CartaDto> getMazo() {
        return mazo;
    }

    public void setMazo(ArrayList<CartaDto> mazo) {
        this.mazo = mazo;
    }

    public ArrayList<CartaDto> getJugadas() {
        return jugadas;
    }

    public void setJugadas(ArrayList<CartaDto> jugadas) {
        this.jugadas = jugadas;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }
    
    @Override
    public String toString() {
        return "JugadorDto{" + "nombre=" + nombre + ", turno=" + turno + ", ganador=" + ganador + ", mazo=" + mazo + ", jugadas=" + jugadas + '}';
    }
    
    
}
