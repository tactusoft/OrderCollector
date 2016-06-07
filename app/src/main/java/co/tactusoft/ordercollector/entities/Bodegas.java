package co.tactusoft.ordercollector.entities;

import java.io.Serializable;

/**
 * Created by csarmiento on 27/05/16.
 */
public class Bodegas implements Serializable {

    private Integer bodegaId;
    private String bodegaCodigo;
    private String bodegaNombre;
    private String bodegaCiudadNombre;
    private String bodegaDireccion;

    public Bodegas() {
    }

    public Integer getBodegaId() {
        return bodegaId;
    }

    public void setBodegaId(Integer bodegaId) {
        this.bodegaId = bodegaId;
    }

    public String getBodegaCodigo() {
        return bodegaCodigo;
    }

    public void setBodegaCodigo(String bodegaCodigo) {
        this.bodegaCodigo = bodegaCodigo;
    }

    public String getBodegaNombre() {
        return bodegaNombre;
    }

    public void setBodegaNombre(String bodegaNombre) {
        this.bodegaNombre = bodegaNombre;
    }

    public String getBodegaCiudadNombre() {
        return bodegaCiudadNombre;
    }

    public void setBodegaCiudadNombre(String bodegaCiudadNombre) {
        this.bodegaCiudadNombre = bodegaCiudadNombre;
    }

    public String getBodegaDireccion() {
        return bodegaDireccion;
    }

    public void setBodegaDireccion(String bodegaDireccion) {
        this.bodegaDireccion = bodegaDireccion;
    }
}
