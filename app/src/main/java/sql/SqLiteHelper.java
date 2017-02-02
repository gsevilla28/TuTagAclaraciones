package sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Administrator on 15/09/2016.
 */
public class SqLiteHelper extends SQLiteOpenHelper{

    /*nombre y version de la BD*/
    private static final String DATABASE_NAME = "IDMOVIL";
    private static final int DATABASE_VERSION = 2;

    /*TABLA TARJETAS*/
    public static final String TABLE_NAME = "TARJETAS";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_ID_CLIENTE = "ID_CLIENTE";
    public static final String COLUMN_ALIAS = "ALIAS";
    public static final String COLUMN_PREFIJO = "PREFIJO";
    public static final String COLUMN_NUMERO = "NUMERO";
    public static final String COLUMN_TIPOPAGO = "TIPOPAGO";

    /*CREAR TABLA DE TARJETAS*/
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ID_CLIENTE + " INTEGER, " +
            COLUMN_ALIAS + " TEXT NOT NULL, " +
            COLUMN_PREFIJO + " TEXT NOT NULL, " +
            COLUMN_NUMERO + " TEXT NOT NULL, " +
            COLUMN_TIPOPAGO + " INTEGER)";
    
    /*TABLA DE MOTIVOS*/
    public static final String TABLE_MOTIVOS = "MOTIVOS";
    public static final String ID_COLUMN = "ID_MOTIVO";
    public static final String DESCRIPCION_COLUMN = "DESCRIPCION";

    /*CREAR TABLA MOTIVOS*/
    private static  final  String CREATE_TABLE_MOTIVOS = "CREATE TABLE " + TABLE_MOTIVOS +
            " ( " + ID_COLUMN + " INTEGER PRIMARY KEY, " +
            DESCRIPCION_COLUMN + " TEXT NOT NULL)";

    /*TABLA DATOS_CUENTA*/
    public static final String TABLE_DATOSCUENTA = "DATOS_CUENTA";
    public static final String COLUMN_CELULAR = "CELULAR";
    public static final String COLUMN_EMAIL = "EMAIL";
    public static final String COLUMN_CONTRASENA = "CONTRASENA";

    private static final String CREATE_TABLE_DATOSCUENTA = "CREATE TABLE " + TABLE_DATOSCUENTA +
            " ( " + COLUMN_CELULAR + " INTEGER PRIMARY KEY, "  +
            COLUMN_EMAIL + " TEXT NOT NULL, " +
            COLUMN_CONTRASENA + " TEXT)";


    

    public SqLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TABLE_MOTIVOS);
        db.execSQL(CREATE_TABLE_DATOSCUENTA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion ==1 && newVersion==2){
            db.execSQL(CREATE_TABLE_DATOSCUENTA);
        }

    }
}
