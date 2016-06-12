package co.tactusoft.ordercollector.entities;

import java.io.Serializable;

/**
 * Created by csarmiento
 * 12/06/16
 * csarmiento@gentemovil.co
 */
public class Transportadoras implements Serializable {

    Integer transportadoraId;
    String transportadoraCodigo;
    String transportadoraNombre;
    String transportadoraNumeroIdentificacion;

    public Transportadoras() {

    }

    public Integer getTransportadoraId() {
        return transportadoraId;
    }

    public void setTransportadoraId(Integer transportadoraId) {
        this.transportadoraId = transportadoraId;
    }

    public String getTransportadoraCodigo() {
        return transportadoraCodigo;
    }

    public void setTransportadoraCodigo(String transportadoraCodigo) {
        this.transportadoraCodigo = transportadoraCodigo;
    }

    public String getTransportadoraNombre() {
        return transportadoraNombre;
    }

    public void setTransportadoraNombre(String transportadoraNombre) {
        this.transportadoraNombre = transportadoraNombre;
    }

    public String getTransportadoraNumeroIdentificacion() {
        return transportadoraNumeroIdentificacion;
    }

    public void setTransportadoraNumeroIdentificacion(String transportadoraNumeroIdentificacion) {
        this.transportadoraNumeroIdentificacion = transportadoraNumeroIdentificacion;
    }

    @Override
    public String toString() {
        return transportadoraNombre;
    }
}
