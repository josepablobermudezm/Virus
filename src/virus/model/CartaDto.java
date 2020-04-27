/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package virus.model;

import java.io.Serializable;

/**
 *
 * @author Usuario
 */

public class CartaDto implements Serializable {
    private static final long serialVersionUID = 4L;
    private String tipoCarta;
    private String color;
    private String imagen;
    private String estado;
    
    public CartaDto(String tipoCarta, String color, String imagen, String estado) {
        this.tipoCarta = tipoCarta;
        this.color = color;
        this.imagen = imagen;
        this.estado = estado;
    }

    public CartaDto() {
    }

    public String getTipoCarta() {
        return tipoCarta;
    }

    public void setTipoCarta(String tipoCarta) {
        this.tipoCarta = tipoCarta;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "CartaDto{" + "tipoCarta=" + tipoCarta + ", color=" + color + ", imagen=" + imagen + ", estado=" + estado + '}';
    }
    
}
