package co.tactusoft.ordercollector.entities;

import java.io.Serializable;

/**
 * Created by csarmiento
 * 15/06/16
 * csarmiento@gentemovil.co
 */
public class VehiculosFoto implements Serializable {

    Long id;
    Integer ordenId;
    String codigo;
    String descripcion;
    String foto;
    Integer fotoOmision;
    Integer sincronizado;

    public VehiculosFoto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrdenId() {
        return ordenId;
    }

    public void setOrdenId(Integer ordenId) {
        this.ordenId = ordenId;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Integer getFotoOmision() {
        return fotoOmision;
    }

    public void setFotoOmision(Integer fotoOmision) {
        this.fotoOmision = fotoOmision;
    }

    public Integer getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(Integer sincronizado) {
        this.sincronizado = sincronizado;
    }
}
