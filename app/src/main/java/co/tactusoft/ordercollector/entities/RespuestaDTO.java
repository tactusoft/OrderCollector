package co.tactusoft.ordercollector.entities;

import java.io.Serializable;

/**
 * Created by csarmiento
 * 10/06/16
 * csarmiento@gentemovil.co
 */
public class RespuestaDTO implements Serializable {

    String severidadMaxima;
    OrdenesEntradaPUT data;
    MensajeDTO[] mensajes;

    public RespuestaDTO() {
    }

    public String getSeveridadMaxima() {
        return severidadMaxima;
    }

    public void setSeveridadMaxima(String severidadMaxima) {
        this.severidadMaxima = severidadMaxima;
    }

    public OrdenesEntradaPUT getData() {
        return data;
    }

    public void setData(OrdenesEntradaPUT data) {
        this.data = data;
    }

    public MensajeDTO[] getMensajes() {
        return mensajes;
    }

    public void setMensajes(MensajeDTO[] mensajes) {
        this.mensajes = mensajes;
    }
}
