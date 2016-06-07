package co.tactusoft.ordercollector.entities;

import java.io.Serializable;

/**
 * Created by csarmiento on 27/05/16.
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
}
