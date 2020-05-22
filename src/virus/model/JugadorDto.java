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
    private ArrayList <CartaDto> cartas1;
    private ArrayList <CartaDto> cartas2;
    private ArrayList <CartaDto> cartas3;
    private ArrayList <CartaDto> cartas4;
    private static final long serialVersionUID = 4L;
    
    public JugadorDto() {
    }

    public JugadorDto(String nombre, Boolean turno, Boolean ganador, String IP, ArrayList<CartaDto> cartas1, ArrayList<CartaDto> cartas2, ArrayList<CartaDto> cartas3, ArrayList<CartaDto> cartas4, String IPS ) {
        this.nombre = nombre;
        this.turno = turno;
        this.ganador = ganador;
        this.IP = IP;
        this.cartas1 = cartas1;
        this.cartas2 = cartas2;
        this.cartas3 = cartas3;
        this.cartas4 = cartas4;
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

    public ArrayList<CartaDto> getCartas1() {
        if(cartas1 == null){
            cartas1 = new ArrayList();
        }
        return cartas1;
    }

    public void setCartas1(ArrayList<CartaDto> cartas1) {
        this.cartas1 = cartas1;
    }
    
    public ArrayList<CartaDto> getCartas2() {
        if(cartas2 == null){
            cartas2 = new ArrayList();
        }
        return cartas2;
    }

    public void setCartas2(ArrayList<CartaDto> cartas2) {
        this.cartas2 = cartas2;
    }

    public ArrayList<CartaDto> getCartas3() {
        if(cartas3 == null){
            cartas3 = new ArrayList();
        }
        return cartas3;
    }

    public void setCartas3(ArrayList<CartaDto> cartas3) {
        this.cartas3 = cartas3;
    }
    
    public ArrayList<CartaDto> getCartas4() {
        if(cartas4 == null){
            cartas4 = new ArrayList();
        }
        return cartas4;
    }

    public void setCartas4(ArrayList<CartaDto> cartas4) {
        this.cartas4 = cartas4;
    }
    
    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }
    
    @Override
    public String toString() {
        return "JugadorDto{" + "nombre=" + nombre + ", turno=" + turno + ", ganador=" + ganador + ", mazo=" + mazo + ", jugadas=" + cartas1 + cartas2 + cartas3 + cartas4 + '}';
    }
    
    
}
