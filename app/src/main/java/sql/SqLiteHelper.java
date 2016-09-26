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
    private static final int DATABASE_VERSION = 1;


    /*TABLA TARJETAS*/
    public static final String TABLE_NAME = "TARJETAS";
    public static final String COLUMN_ID = BaseColumns._ID;
    public static final String COLUMN_ID_CLIENTE = "ID_CLIENTE";
    public static final String COLUMN_ALIAS = "ALIAS";
    public static final String COLUMN_PREFIJO = "PREFIJO";
    public static final String COLUMN_NUMERO = "NUMERO";
    public static final String COLUMN_TIPOPAGO = "TIPOPAGO";


    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_ID_CLIENTE + " INTEGER, " +
            COLUMN_ALIAS + " TEXT NOT NULL, " +
            COLUMN_PREFIJO + " TEXT NOT NULL, " +
            COLUMN_NUMERO + " TEXT NOT NULL, " +
            COLUMN_TIPOPAGO + " INTEGER)";


    public SqLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
