package co.tactusoft.ordercollector.entities;

import java.io.Serializable;

/**
 * Created by csarmiento
 * 10/06/16
 * csarmiento@gentemovil.co
 */
public class MensajeDTO implements Serializable {

    String severidad;
    String codigo;
    String grupo;
    String texto;

    public MensajeDTO() {
    }

    public String getSeveridad() {
        return severidad;
    }

    public void setSeveridad(String severidad) {
        this.severidad = severidad;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
