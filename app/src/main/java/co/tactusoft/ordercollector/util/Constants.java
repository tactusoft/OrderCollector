package co.tactusoft.ordercollector.util;

/**
 * Created by csarmiento on 6/06/16.
 */
public class Constants {

    public static final String REST_URL = "http://connect2.tacticlogistics.com:8585/sateliteDev/wms/";

    public enum TIPOS_ORDENES {
        ENTRADA, SALIDA
    }

    public enum ESTADOS_ORDENES {
        ACEPTADA, CONFIRMADA, EN_EJECUCION, FINALIZADA
    }

    public enum SEVERIDAD_ERRORES {
        INFO, WARN, ERROR
    }

    public static final int TYPE_ITEM_COLORED = 1;
    public static final int TYPE_ITEM_NORMAL = 0;

    public static final int RESULT_OK = -1;
    public static final int RESULT_LOAD_IMAGE = 1000;
    public static final int REQUEST_IMAGE_CAPTURE = 1001;
}
