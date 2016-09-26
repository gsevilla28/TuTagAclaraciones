package sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import models.ModelTags;

/**
 * Created by Administrator on 15/09/2016.
 */
public class DataSource {
    private final SQLiteDatabase sql;
    private final ContentValues contentValues = new ContentValues();


    public DataSource(Context context) {
        SqLiteHelper helper = new SqLiteHelper(context);
        sql = helper.getWritableDatabase();
    }

    public void SaveTags (int idcliente, String alias, String prefijo, String numero, int tipopago){

        contentValues.put(SqLiteHelper.COLUMN_ID_CLIENTE, idcliente);
        contentValues.put(SqLiteHelper.COLUMN_ALIAS,alias);
        contentValues.put(SqLiteHelper.COLUMN_PREFIJO, prefijo);
        contentValues.put(SqLiteHelper.COLUMN_NUMERO, numero);
        contentValues.put(SqLiteHelper.COLUMN_TIPOPAGO, tipopago);

        sql.insert(SqLiteHelper.TABLE_NAME,null,contentValues);
        contentValues.clear();

    }

    public int getIdCliente(){

        int idcliente=0;

        Cursor c = sql.query(SqLiteHelper.TABLE_NAME,new String[]{SqLiteHelper.COLUMN_ID_CLIENTE},null,null,SqLiteHelper.COLUMN_ID_CLIENTE,null,null);

        if(c.moveToNext()){
            idcliente = c.getInt(c.getColumnIndexOrThrow(SqLiteHelper.COLUMN_ID_CLIENTE));
        }

        return  idcliente;

    }

    public List<ModelTags> getTags(){

        ArrayList<ModelTags> tagslistLocal = new ArrayList<>();
        ModelTags modeloTarjetas;

        Cursor c = sql.query(SqLiteHelper.TABLE_NAME,new String[]{SqLiteHelper.COLUMN_ID_CLIENTE, SqLiteHelper.COLUMN_PREFIJO , SqLiteHelper.COLUMN_NUMERO},null,null,null,null,SqLiteHelper.COLUMN_NUMERO);


        while (c.moveToNext()){
            modeloTarjetas = new ModelTags();
            modeloTarjetas.setIdcliente(c.getInt(c.getColumnIndexOrThrow(SqLiteHelper.COLUMN_ID_CLIENTE)));
            modeloTarjetas.setPrefijo(c.getString(c.getColumnIndexOrThrow(SqLiteHelper.COLUMN_PREFIJO)));
            modeloTarjetas.setNumero(c.getString(c.getColumnIndexOrThrow(SqLiteHelper.COLUMN_NUMERO)));
            tagslistLocal.add(modeloTarjetas);
        }
        c.close();
        return tagslistLocal;

    }
//String[]{SqLiteHelper.COLUMN_PREFIJO + SqLiteHelper.COLUMN_NUMERO + "=" + numtar}
    public ModelTags getTagDetail(String numtar){

        Cursor cursor = sql.query(SqLiteHelper.TABLE_NAME,null,SqLiteHelper.COLUMN_PREFIJO+ "||"+SqLiteHelper.COLUMN_NUMERO + "=?",new String[]{numtar},null,null,null);

        ModelTags tag = new ModelTags();
        if(cursor.moveToNext()){
            tag.setIdcliente(cursor.getInt(cursor.getColumnIndexOrThrow(SqLiteHelper.COLUMN_ID_CLIENTE)));
            tag.setAlias(cursor.getString(cursor.getColumnIndexOrThrow(SqLiteHelper.COLUMN_ALIAS)));
            tag.setPrefijo(cursor.getString(cursor.getColumnIndexOrThrow(SqLiteHelper.COLUMN_PREFIJO)));
            tag.setNumero(cursor.getString(cursor.getColumnIndexOrThrow(SqLiteHelper.COLUMN_NUMERO)));
            tag.setTipoPago(cursor.getInt(cursor.getColumnIndexOrThrow(SqLiteHelper.COLUMN_TIPOPAGO)));
        }
        return  tag;

    }
}
