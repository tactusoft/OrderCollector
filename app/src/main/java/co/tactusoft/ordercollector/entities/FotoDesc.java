package co.tactusoft.ordercollector.entities;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by csarmiento
 * 13/06/16
 * csarmiento@gentemovil.co
 */
public class FotoDesc implements Serializable {

    private String codigo;
    private String descripcion;
    private Bitmap bitmap;

    public FotoDesc() {
    }

    public Integer getId() {
        return codigo.hashCode();
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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
