package co.tactusoft.ordercollector.util;

import co.tactusoft.ordercollector.entities.Usuario;

/**
 * Created by csarmiento on 27/05/16.
 */
public class Singleton {
    private static Singleton ourInstance = new Singleton();
    private Usuario usuario;

    public static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
