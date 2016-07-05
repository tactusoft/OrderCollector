package co.tactusoft.ordercollector.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import co.tactusoft.ordercollector.entities.Novedad;
import co.tactusoft.ordercollector.entities.OrdenesEntradas;
import co.tactusoft.ordercollector.entities.Usuario;
import co.tactusoft.ordercollector.entities.VehiculosFoto;

/**
 * Created by csarmiento
 * 12/06/16
 * csarmiento@gentemovil.co
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android"s default system path of your application database.
    public String dbPath;
    public static String DB_NAME = "tactic.sqlite";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    String sqlOrdenesEntrada = "SELECT id, cliente_codigo, estado_orden, numero_documento_orden_cliente,\n" +
            "fecha_planeada_entrega_minima, fecha_planeada_entrega_maxima, hora_planeada_entrega_minima,\n" +
            "hora_planeada_entrega_maxima, fecha_actualizacion, usuario_actualizacion,\n" +
            "fecha_confirmacion, usuario_confirmacion, fecha_aprobacion_cliente, usuario_aprobacion_cliente,\n" +
            "transportadora_id, tipo_vehiculo_id, numero_placa_vehiculo, numero_placa_remolque,\n" +
            "fecha_notificacion_dellegada, fecha_registro_dellegada, conductor_numero_identificacion, conductor_nombres,\n" +
            "conductor_apellidos, conductor_telefono, observaciones_salida, imagen_salida, bloqueado, sincronizado\n" +
            "FROM ordenes_entrada\n";

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context Application Context
     */
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        if (myContext == null) {
            dbPath = "/data/data/co.tactusoft.ordercollector/databases/";
        } else {
            dbPath = myContext.getFilesDir().getPath().replace("files", "databases") + "/";
        }
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if(!dbExist){
            try {
                this.getWritableDatabase();
            } catch (Exception e) {
                try {
                    copyDataBase();
                } catch (IOException ex) {
                    throw new Error("Error copying database");
                }
            }
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn"t
     */
    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;
        try{
            String myPath = dbPath + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            //database does"t exist yet.
        }
        if(checkDB != null){
            checkDB.close();
        }
        return checkDB != null;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException {
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = dbPath + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        //Open the database
        String myPath = dbPath + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        return myDataBase;
    }

    @Override
    public synchronized void close() {
        if(myDataBase != null) {
            myDataBase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it"d be easy
    // to you to create adapters for your views.

    /**
     * Return values for a single row with the specified id
     * @return All column values are stored as properties in the ContentValues object
     */
    public Usuario getUsuario() {
        Usuario object = null;
        try {
            openDataBase();
            Cursor cur = myDataBase.rawQuery("select usuario_id, nombre_usuario, bodega_id, orden_id, tipo_orden, estado_orden from usuario", new String[] {});
            if (cur.moveToLast()) {
                object = new Usuario();
                object.setUsuarioId(cur.getInt(0));
                object.setNombreUsuario(cur.getString(1));
                object.setBodegaId(cur.getInt(2) == 0?null:cur.getInt(2));
                object.setOrdenId(cur.getInt(3) == 0?null:cur.getInt(3));
                object.setTipoOrden(cur.getString(4));
                object.setEstadoOrden(cur.getString(5));
            }
            cur.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return object;
    }

    public Long insertUsuario(Usuario object) {
        Long id = null;
        try {
            openDataBase();
            ContentValues row = new ContentValues();
            row.put("usuario_id", object.getUsuarioId());
            row.put("nombre_usuario", object.getNombreUsuario());
            row.put("bodega_id", object.getBodegaId());
            row.put("tipo_orden", object.getTipoOrden());
            row.put("orden_id", object.getOrdenId());
            row.put("estado_orden", object.getEstadoOrden());
            id = myDataBase.insert("usuario", null, row);
            if(id == -1) {
                myDataBase.update("usuario", row,
                        "usuario_id = ?", new String[] { String.valueOf(object.getUsuarioId()) });
                id = Long.valueOf(object.getUsuarioId());
            } else {
                object.setUsuarioId(id.intValue());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return id;
    }

    public OrdenesEntradas getOrdenesEntradasBloqueada() {
        OrdenesEntradas object = null;
        try {
            openDataBase();
            Cursor cur = myDataBase.rawQuery(sqlOrdenesEntrada + "WHERE bloqueado = 1", new String[] {});
            if (cur.moveToLast()) {
                object = new OrdenesEntradas();
                object.setId(cur.getInt(0));
                object.setClienteCodigo(cur.getString(1));
                object.setEstadoOrden(cur.getString(2));
                object.setNumeroDocumentoOrdenCliente(cur.getString(3));
                object.setFechaPlaneadaEntregaMinima(cur.getString(4));
                object.setFechaPlaneadaEntregaMaxima(cur.getString(5));
                object.setHoraPlaneadaEntregaMinima(cur.getString(6));
                object.setHoraPlaneadaEntregaMaxima(cur.getString(7));
                object.setFechaActualizacion(cur.getString(8));
                object.setUsuarioActualizacion(cur.getString(9));
                object.setFechaConfirmacion(cur.getString(10));
                object.setUsuarioConfirmacion(cur.getString(11));
                object.setFechaAprobacionCliente(cur.getString(12));
                object.setUsuarioAprobacionCliente(cur.getString(13));
                object.setTransportadoraId(cur.getInt(14));
                object.setTipoVehiculoId(cur.getInt(15));
                object.setNumeroPlacaVehiculo(cur.getString(16));
                object.setNumeroPlacaRemolque(cur.getString(17));
                object.setFechaNotificacionDeLlegada(cur.getString(18));
                object.setFechaRegistroDeLlegada(cur.getString(19));
                object.setConductorNumeroIdentificacion(cur.getString(20));
                object.setConductorNombres(cur.getString(21));
                object.setConductorApellidos(cur.getString(22));
                object.setConductorTelefono(cur.getString(23));
                object.setObservacionesSalida(cur.getString(24));
                object.setImagenSalida(cur.getString(25));
                object.setBloqueado(cur.getInt(26) == 1);
                object.setSincronizado(cur.getInt(27));
            }
            cur.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return object;
    }

    public List<OrdenesEntradas> getListOrdenesEntradas() {
        List<OrdenesEntradas> result = new LinkedList<>();
        OrdenesEntradas object;
        try {
            openDataBase();
            Cursor cur = myDataBase.rawQuery(sqlOrdenesEntrada, new String[] {});
            while (cur.moveToNext()) {
                object = new OrdenesEntradas();
                object.setId(cur.getInt(0));
                object.setClienteCodigo(cur.getString(1));
                object.setEstadoOrden(cur.getString(2));
                object.setNumeroDocumentoOrdenCliente(cur.getString(3));
                object.setFechaPlaneadaEntregaMinima(cur.getString(4));
                object.setFechaPlaneadaEntregaMaxima(cur.getString(5));
                object.setHoraPlaneadaEntregaMinima(cur.getString(6));
                object.setHoraPlaneadaEntregaMaxima(cur.getString(7));
                object.setFechaActualizacion(cur.getString(8));
                object.setUsuarioActualizacion(cur.getString(9));
                object.setFechaConfirmacion(cur.getString(10));
                object.setUsuarioConfirmacion(cur.getString(11));
                object.setFechaAprobacionCliente(cur.getString(12));
                object.setUsuarioAprobacionCliente(cur.getString(13));
                object.setTransportadoraId(cur.getInt(14));
                object.setTipoVehiculoId(cur.getInt(15));
                object.setNumeroPlacaVehiculo(cur.getString(16));
                object.setNumeroPlacaRemolque(cur.getString(17));
                object.setFechaNotificacionDeLlegada(cur.getString(18));
                object.setFechaRegistroDeLlegada(cur.getString(19));
                object.setConductorNumeroIdentificacion(cur.getString(20));
                object.setConductorNombres(cur.getString(21));
                object.setConductorApellidos(cur.getString(22));
                object.setConductorTelefono(cur.getString(23));
                object.setObservacionesSalida(cur.getString(24));
                object.setImagenSalida(cur.getString(25));
                object.setBloqueado(cur.getInt(24) == 1);
                object.setSincronizado(cur.getInt(25));
                result.add(object);
            }
            cur.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return result;
    }

    public OrdenesEntradas getOrdenesEntradas(Integer id) {
        OrdenesEntradas object = null;
        try {
            openDataBase();
            Cursor cur = myDataBase.rawQuery(sqlOrdenesEntrada + "WHERE id = ?", new String[] { String.valueOf(id) });
            if (cur.moveToLast()) {
                object = new OrdenesEntradas();
                object.setId(cur.getInt(0));
                object.setClienteCodigo(cur.getString(1));
                object.setEstadoOrden(cur.getString(2));
                object.setNumeroDocumentoOrdenCliente(cur.getString(3));
                object.setFechaPlaneadaEntregaMinima(cur.getString(4));
                object.setFechaPlaneadaEntregaMaxima(cur.getString(5));
                object.setHoraPlaneadaEntregaMinima(cur.getString(6));
                object.setHoraPlaneadaEntregaMaxima(cur.getString(7));
                object.setFechaActualizacion(cur.getString(8));
                object.setUsuarioActualizacion(cur.getString(9));
                object.setFechaConfirmacion(cur.getString(10));
                object.setUsuarioConfirmacion(cur.getString(11));
                object.setFechaAprobacionCliente(cur.getString(12));
                object.setUsuarioAprobacionCliente(cur.getString(13));
                object.setTransportadoraId(cur.getInt(14));
                object.setTipoVehiculoId(cur.getInt(15));
                object.setNumeroPlacaVehiculo(cur.getString(16));
                object.setNumeroPlacaRemolque(cur.getString(17));
                object.setFechaNotificacionDeLlegada(cur.getString(18));
                object.setFechaRegistroDeLlegada(cur.getString(19));
                object.setConductorNumeroIdentificacion(cur.getString(20));
                object.setConductorNombres(cur.getString(21));
                object.setConductorApellidos(cur.getString(22));
                object.setConductorTelefono(cur.getString(23));
                object.setObservacionesSalida(cur.getString(24));
                object.setImagenSalida(cur.getString(25));
                object.setBloqueado(cur.getInt(24) == 1);
                object.setSincronizado(cur.getInt(25));
            }
            cur.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return object;
    }

    public void insertOrdenesEntradas(OrdenesEntradas object) {
        ContentValues row = new ContentValues();
        try {
            openDataBase();
            row.put("id", object.getId());
            row.put("cliente_codigo", object.getClienteCodigo());
            row.put("estado_orden", object.getEstadoOrden());
            row.put("numero_documento_orden_cliente", object.getNumeroDocumentoOrdenCliente());
            row.put("fecha_planeada_entrega_minima", object.getFechaPlaneadaEntregaMinima());
            row.put("fecha_planeada_entrega_maxima", object.getFechaPlaneadaEntregaMaxima());
            row.put("hora_planeada_entrega_minima", object.getHoraPlaneadaEntregaMinima());
            row.put("hora_planeada_entrega_maxima", object.getHoraPlaneadaEntregaMinima());
            row.put("fecha_actualizacion", object.getFechaActualizacion());
            row.put("usuario_actualizacion", object.getUsuarioActualizacion());
            row.put("fecha_confirmacion", object.getFechaConfirmacion());
            row.put("usuario_confirmacion", object.getUsuarioConfirmacion());
            row.put("fecha_aprobacion_cliente", object.getFechaAprobacionCliente());
            row.put("usuario_aprobacion_cliente", object.getUsuarioAprobacionCliente());
            row.put("transportadora_id", object.getTransportadoraId());
            row.put("tipo_vehiculo_id", object.getTipoVehiculoId());
            row.put("numero_placa_vehiculo", object.getNumeroPlacaVehiculo());
            row.put("numero_placa_remolque", object.getNumeroPlacaRemolque());
            row.put("fecha_notificacion_dellegada", object.getFechaNotificacionDeLlegada());
            row.put("fecha_registro_dellegada", object.getFechaRegistroDeLlegada());
            row.put("conductor_numero_identificacion", object.getConductorNumeroIdentificacion());
            row.put("conductor_nombres", object.getConductorNombres());
            row.put("conductor_apellidos", object.getConductorApellidos());
            row.put("conductor_telefono", object.getConductorTelefono());
            row.put("observaciones_salida", object.getObservacionesSalida());
            row.put("imagen_salida", object.getImagenSalida());
            row.put("bloqueado", object.getBloqueado()?1:0);
            long id = myDataBase.insert("ordenes_entrada", null, row);
            if (id == -1) {
                myDataBase.update("ordenes_entrada", row,
                        "id = ?", new String[]{String.valueOf(object.getId())});
            }
        } catch (SQLiteConstraintException e) {
            myDataBase.update("ordenes_entrada", row,
                    "id = ?", new String[]{String.valueOf(object.getId())});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void insertVehiculosFoto(VehiculosFoto object) {
        ContentValues row = new ContentValues();
        try {
            openDataBase();
            row.put("orden_id", object.getOrdenId());
            row.put("codigo", object.getCodigo());
            row.put("descripcion", object.getDescripcion());
            row.put("foto", object.getFoto());
            row.put("foto_omision", object.getFotoOmision());
            row.put("sincronizado", object.getSincronizado());
            long id = myDataBase.insert("vehiculos_foto", null, row);
            if (id == -1) {
                myDataBase.update("vehiculos_foto", row,
                        "orden_id = ? AND codigo = ?", new String[]{String.valueOf(object.getOrdenId()),object.getCodigo()});
            } else {
                object.setId(id);
            }
        } catch (SQLiteConstraintException e) {
            myDataBase.update("vehiculos_foto", row,
                    "orden_id = ? AND codigo = ?", new String[]{String.valueOf(object.getOrdenId()),object.getCodigo()});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void insertNovedad(Novedad object) {
        ContentValues row = new ContentValues();
        try {
            openDataBase();
            row.put("id", object.getId());
            row.put("orden_id", object.getOrdenId());
            row.put("tipo", object.getTipo());
            row.put("clase", object.getClase());
            row.put("observacion", object.getObservacion());
            row.put("foto", object.getFoto());
            row.put("sincronizado", object.getSincronizado());
            long id = myDataBase.insert("novedad", null, row);
            if (id == -1) {
                myDataBase.update("novedad", row,
                        "id = ?", new String[]{String.valueOf(object.getId())});
            } else {
                object.setId(id);
            }
        } catch (SQLiteConstraintException e) {
            myDataBase.update("novedad", row,
                    "id = ?", new String[]{String.valueOf(object.getId())});
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public List<VehiculosFoto> getListVehiculosFoto(Integer ordenId) {
        List<VehiculosFoto> list = new LinkedList<>();
        VehiculosFoto object;
        try {
            openDataBase();
            Cursor cur = myDataBase.rawQuery("SELECT id, orden_id, codigo, descripcion, foto, foto_omision, sincronizado FROM vehiculos_foto WHERE orden_id = ? ORDER BY id", new String[] { String.valueOf(ordenId) });
            while (cur.moveToNext()) {
                object = new VehiculosFoto();
                object.setId(cur.getLong(0));
                object.setOrdenId(cur.getInt(1));
                object.setCodigo(cur.getString(2));
                object.setDescripcion(cur.getString(3));;
                object.setFoto(cur.getString(4));
                object.setFotoOmision(cur.getInt(5));
                object.setSincronizado(cur.getInt(6));
                list.add(object);
            }
            cur.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return list;
    }

    public List<Novedad> getListNovedad(Integer ordenId) {
        List<Novedad> list = new LinkedList<>();
        Novedad object;
        try {
            openDataBase();
            Cursor cur = myDataBase.rawQuery("SELECT id, orden_id, tipo, clase, foto, observacion, sincronizado " +
                    "FROM novedad WHERE orden_id = ? ORDER BY id", new String[] { String.valueOf(ordenId) });
            while (cur.moveToNext()) {
                object = new Novedad();
                object.setId(cur.getLong(0));
                object.setOrdenId(cur.getInt(1));
                object.setTipo(cur.getString(2));
                object.setClase(cur.getString(3));;
                object.setFoto(cur.getString(4));
                object.setObservacion(cur.getString(5));
                object.setSincronizado(cur.getInt(6));
                list.add(object);
            }
            cur.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return list;
    }

    public Integer deleteNovedad(Long id) {
        return deleteTable("Novedad", "id = " + id);
    }

    private Integer deleteTable(String table, String where) {
        Integer id = null;
        try {
            openDataBase();
            id = myDataBase.delete(table, where, null);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return id;
    }

    public Integer updateOrdenesEntradaBloquedas() {
        Integer id = null;
        try {
            openDataBase();
            ContentValues row = new ContentValues();
            row.put("bloqueado", "0");
            id = myDataBase.update("ordenes_entrada", row,
                    "1 = 1", new String[] { });
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return id;
    }

}
