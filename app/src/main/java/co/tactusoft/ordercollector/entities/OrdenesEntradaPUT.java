package co.tactusoft.ordercollector.entities;

import java.io.Serializable;

/**
 * Created by csarmiento
 * 10/06/16
 * csarmiento@gentemovil.co
 */
public class OrdenesEntradaPUT implements Serializable {

    Integer id;
    Integer transportadoraId;
    Integer tipoVehiculoId;
    String numeroPlacaVehiculo;
    String numeroPlacaRemolque;
    String fechaNotificacionDeLlegada;
    String fechaRegistroDeLlegada;
    String conductorNumeroIdentificacion;
    String conductorNombres;
    String conductorApellidos;
    String conductorTelefono;
    String usuarioActualizacion;

    public OrdenesEntradaPUT() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTransportadoraId() {
        return transportadoraId;
    }

    public void setTransportadoraId(Integer transportadoraId) {
        this.transportadoraId = transportadoraId;
    }

    public Integer getTipoVehiculoId() {
        return tipoVehiculoId;
    }

    public void setTipoVehiculoId(Integer tipoVehiculoId) {
        this.tipoVehiculoId = tipoVehiculoId;
    }

    public String getNumeroPlacaVehiculo() {
        return numeroPlacaVehiculo;
    }

    public void setNumeroPlacaVehiculo(String numeroPlacaVehiculo) {
        this.numeroPlacaVehiculo = numeroPlacaVehiculo;
    }

    public String getNumeroPlacaRemolque() {
        return numeroPlacaRemolque;
    }

    public void setNumeroPlacaRemolque(String numeroPlacaRemolque) {
        this.numeroPlacaRemolque = numeroPlacaRemolque;
    }

    public String getFechaNotificacionDeLlegada() {
        return fechaNotificacionDeLlegada;
    }

    public void setFechaNotificacionDeLlegada(String fechaNotificacionDeLlegada) {
        this.fechaNotificacionDeLlegada = fechaNotificacionDeLlegada;
    }

    public String getFechaRegistroDeLlegada() {
        return fechaRegistroDeLlegada;
    }

    public void setFechaRegistroDeLlegada(String fechaRegistroDeLlegada) {
        this.fechaRegistroDeLlegada = fechaRegistroDeLlegada;
    }

    public String getConductorNumeroIdentificacion() {
        return conductorNumeroIdentificacion;
    }

    public void setConductorNumeroIdentificacion(String conductorNumeroIdentificacion) {
        this.conductorNumeroIdentificacion = conductorNumeroIdentificacion;
    }

    public String getConductorNombres() {
        return conductorNombres;
    }

    public void setConductorNombres(String conductorNombres) {
        this.conductorNombres = conductorNombres;
    }

    public String getConductorApellidos() {
        return conductorApellidos;
    }

    public void setConductorApellidos(String conductorApellidos) {
        this.conductorApellidos = conductorApellidos;
    }

    public String getConductorTelefono() {
        return conductorTelefono;
    }

    public void setConductorTelefono(String conductorTelefono) {
        this.conductorTelefono = conductorTelefono;
    }

    public String getUsuarioActualizacion() {
        return usuarioActualizacion;
    }

    public void setUsuarioActualizacion(String usuarioActualizacion) {
        this.usuarioActualizacion = usuarioActualizacion;
    }
}
