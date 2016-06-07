package co.tactusoft.ordercollector.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import co.tactusoft.ordercollector.entities.Usuario;

/**
 * Created by csarmiento on 11/04/16.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android"s default system path of your application database.
    public static String DB_PATH =  "/data/data/co.tactusoft.ordercollector/databases/";
    public static String DB_NAME = "tactic.sqlite";
    private SQLiteDatabase myDataBase;
    private final Context myContext;
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if(!dbExist){
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
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
            String myPath = DB_PATH + DB_NAME;
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
        String outFileName = DB_PATH + DB_NAME;

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
        String myPath = DB_PATH + DB_NAME;
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

}
