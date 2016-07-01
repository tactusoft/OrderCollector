package co.tactusoft.ordercollector.entities;

import java.io.Serializable;

/**
 * Created by csarmiento
 * 10/06/16
 * csarmiento@gentemovil.co
 */
public class OrdenesEntradas implements Serializable {

    private Integer id;
    private String clienteCodigo;
    private String estadoOrden;
    private String numeroDocumentoOrdenCliente;
    private String fechaPlaneadaEntregaMinima;
    private String fechaPlaneadaEntregaMaxima;
    private String horaPlaneadaEntregaMinima;
    private String horaPlaneadaEntregaMaxima;
    private String fechaActualizacion;
    private String usuarioActualizacion;
    private String fechaConfirmacion;
    private String usuarioConfirmacion;
    private String fechaAprobacionCliente;
    private String usuarioAprobacionCliente;
    private Integer transportadoraId;
    private Integer tipoVehiculoId;
    private String numeroPlacaVehiculo;
    private String numeroPlacaRemolque;
    private String fechaNotificacionDeLlegada;
    private String fechaRegistroDeLlegada;
    private String conductorNumeroIdentificacion;
    private String conductorNombres;
    private String conductorApellidos;
    private String conductorTelefono;
    private String observacionesSalida;
    private String imagenSalida;
    private Boolean bloqueado;
    private Integer sincronizado;

    public OrdenesEntradas() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClienteCodigo() {
        return clienteCodigo;
    }

    public void setClienteCodigo(String clienteCodigo) {
        this.clienteCodigo = clienteCodigo;
    }

    public String getEstadoOrden() {
        return estadoOrden;
    }

    public void setEstadoOrden(String estadoOrden) {
        this.estadoOrden = estadoOrden;
    }

    public String getNumeroDocumentoOrdenCliente() {
        return numeroDocumentoOrdenCliente;
    }

    public void setNumeroDocumentoOrdenCliente(String numeroDocumentoOrdenCliente) {
        this.numeroDocumentoOrdenCliente = numeroDocumentoOrdenCliente;
    }

    public String getFechaPlaneadaEntregaMinima() {
        return fechaPlaneadaEntregaMinima;
    }

    public void setFechaPlaneadaEntregaMinima(String fechaPlaneadaEntregaMinima) {
        this.fechaPlaneadaEntregaMinima = fechaPlaneadaEntregaMinima;
    }

    public String getFechaPlaneadaEntregaMaxima() {
        return fechaPlaneadaEntregaMaxima;
    }

    public void setFechaPlaneadaEntregaMaxima(String fechaPlaneadaEntregaMaxima) {
        this.fechaPlaneadaEntregaMaxima = fechaPlaneadaEntregaMaxima;
    }

    public String getHoraPlaneadaEntregaMinima() {
        return horaPlaneadaEntregaMinima;
    }

    public void setHoraPlaneadaEntregaMinima(String horaPlaneadaEntregaMinima) {
        this.horaPlaneadaEntregaMinima = horaPlaneadaEntregaMinima;
    }

    public String getHoraPlaneadaEntregaMaxima() {
        return horaPlaneadaEntregaMaxima;
    }

    public void setHoraPlaneadaEntregaMaxima(String horaPlaneadaEntregaMaxima) {
        this.horaPlaneadaEntregaMaxima = horaPlaneadaEntregaMaxima;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getUsuarioActualizacion() {
        return usuarioActualizacion;
    }

    public void setUsuarioActualizacion(String usuarioActualizacion) {
        this.usuarioActualizacion = usuarioActualizacion;
    }

    public String getFechaConfirmacion() {
        return fechaConfirmacion;
    }

    public void setFechaConfirmacion(String fechaConfirmacion) {
        this.fechaConfirmacion = fechaConfirmacion;
    }

    public String getUsuarioConfirmacion() {
        return usuarioConfirmacion;
    }

    public void setUsuarioConfirmacion(String usuarioConfirmacion) {
        this.usuarioConfirmacion = usuarioConfirmacion;
    }

    public String getFechaAprobacionCliente() {
        return fechaAprobacionCliente;
    }

    public void setFechaAprobacionCliente(String fechaAprobacionCliente) {
        this.fechaAprobacionCliente = fechaAprobacionCliente;
    }

    public String getUsuarioAprobacionCliente() {
        return usuarioAprobacionCliente;
    }

    public void setUsuarioAprobacionCliente(String usuarioAprobacionCliente) {
        this.usuarioAprobacionCliente = usuarioAprobacionCliente;
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

    public String getObservacionesSalida() {
        return observacionesSalida;
    }

    public void setObservacionesSalida(String observacionesSalida) {
        this.observacionesSalida = observacionesSalida;
    }

    public String getImagenSalida() {
        return imagenSalida;
    }

    public void setImagenSalida(String imagenSalida) {
        this.imagenSalida = imagenSalida;
    }

    public Boolean getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(Boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public Integer getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(Integer sincronizado) {
        this.sincronizado = sincronizado;
    }
}
