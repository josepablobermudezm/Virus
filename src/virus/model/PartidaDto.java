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

public class PartidaDto implements Serializable {
    private ArrayList <CartaDto> mazo;
    private ArrayList <CartaDto> desechadas;
    private ArrayList <JugadorDto> jugadores;
    private static final long serialVersionUID = 4L;
    
    public PartidaDto(ArrayList<CartaDto> mazo, ArrayList<CartaDto> desechadas, ArrayList<JugadorDto> jugadores) {
        this.mazo = mazo;
        this.desechadas = desechadas;
        this.jugadores = jugadores;
    }

    public PartidaDto() {
    }
    
    public ArrayList <CartaDto> getMazo() {
        return mazo;
    }

    public void setMazo(ArrayList<CartaDto> mazo) {
        this.mazo = mazo;
    }

    public ArrayList<CartaDto> getDesechadas() {
        return desechadas;
    }

    public void setDesechadas(ArrayList<CartaDto> desechadas) {
        this.desechadas = desechadas;
    }

    public ArrayList<JugadorDto> getJugadores() {
        if(jugadores==null){
            jugadores = new ArrayList();
        }
        return jugadores;
    }

    public void setJugadores(ArrayList<JugadorDto> jugadores) {
        this.jugadores = jugadores;
    }

    @Override
    public String toString() {
        return "PartidaDto{" + "mazo=" + mazo + ", desechadas=" + desechadas + ", jugadores=" + jugadores + '}';
    }
    
}
