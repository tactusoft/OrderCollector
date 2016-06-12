package co.tactusoft.ordercollector.entities;

import java.io.Serializable;

/**
 * Created by csarmiento
 * 12/06/16
 * csarmiento@gentemovil.co
 */
public class TiposVehiculos implements Serializable {

    Integer tipoVehiculoId;
    String tipoVehiculoCodigo;
    String tipoVehiculoNombre;
    Boolean tipoVehiculoRequiereRemolque;

    public TiposVehiculos() {

    }

    public Integer getTipoVehiculoId() {
        return tipoVehiculoId;
    }

    public void setTipoVehiculoId(Integer tipoVehiculoId) {
        this.tipoVehiculoId = tipoVehiculoId;
    }

    public String getTipoVehiculoCodigo() {
        return tipoVehiculoCodigo;
    }

    public void setTipoVehiculoCodigo(String tipoVehiculoCodigo) {
        this.tipoVehiculoCodigo = tipoVehiculoCodigo;
    }

    public String getTipoVehiculoNombre() {
        return tipoVehiculoNombre;
    }

    public void setTipoVehiculoNombre(String tipoVehiculoNombre) {
        this.tipoVehiculoNombre = tipoVehiculoNombre;
    }

    public Boolean getTipoVehiculoRequiereRemolque() {
        return tipoVehiculoRequiereRemolque;
    }

    public void setTipoVehiculoRequiereRemolque(Boolean tipoVehiculoRequiereRemolque) {
        this.tipoVehiculoRequiereRemolque = tipoVehiculoRequiereRemolque;
    }

    @Override
    public String toString() {
        return tipoVehiculoNombre;
    }
}
