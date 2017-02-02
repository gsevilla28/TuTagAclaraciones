package sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;


import models.ModelTags;
import models.modelDatosCuenta;
import models.modelMotivos;


public class DataSource {

    private Cursor c;
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
        c = sql.query(SqLiteHelper.TABLE_NAME,new String[]{SqLiteHelper.COLUMN_ID_CLIENTE},null,null,SqLiteHelper.COLUMN_ID_CLIENTE,null,null);
        if(c.moveToNext()){
            idcliente = c.getInt(c.getColumnIndexOrThrow(SqLiteHelper.COLUMN_ID_CLIENTE));
        }
        c.close();
        return  idcliente;

    }

    //public List<ModelTags> getTags(int idcliente){
    public List<ModelTags> getTags(){

        ArrayList<ModelTags> tagslistLocal = new ArrayList<>();
        ModelTags modeloTarjetas;

        //c = sql.query(SqLiteHelper.TABLE_NAME,new String[]{SqLiteHelper.COLUMN_ID_CLIENTE, SqLiteHelper.COLUMN_PREFIJO , SqLiteHelper.COLUMN_NUMERO},SqLiteHelper.COLUMN_ID_CLIENTE +"=?",new String[]{String.valueOf(idcliente)},null,null,SqLiteHelper.COLUMN_NUMERO);
        c = sql.query(SqLiteHelper.TABLE_NAME,new String[]{SqLiteHelper.COLUMN_ID_CLIENTE, SqLiteHelper.COLUMN_PREFIJO , SqLiteHelper.COLUMN_NUMERO},null,null,null,null,SqLiteHelper.COLUMN_NUMERO);

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
    public void DeleteTag(String numtar){

        sql.delete(SqLiteHelper.TABLE_NAME, SqLiteHelper.COLUMN_PREFIJO + "||"+ SqLiteHelper.COLUMN_NUMERO + "=?", new String[]{numtar});
        //sql.execSQL("DELETE FROM TARJETAS WHERE PREFIJO || NUMERO = '" + numtar  + "'"); //una forma de hacerlo

    }

    public ModelTags getTagDetail(String numtar){

        c = sql.query(SqLiteHelper.TABLE_NAME,null,SqLiteHelper.COLUMN_PREFIJO+ "||"+SqLiteHelper.COLUMN_NUMERO + "=?",new String[]{numtar},null,null,null);

        ModelTags tag = new ModelTags();
        if(c.moveToNext()){
            tag.setIdcliente(c.getInt(c.getColumnIndexOrThrow(SqLiteHelper.COLUMN_ID_CLIENTE)));
            tag.setAlias(c.getString(c.getColumnIndexOrThrow(SqLiteHelper.COLUMN_ALIAS)));
            tag.setPrefijo(c.getString(c.getColumnIndexOrThrow(SqLiteHelper.COLUMN_PREFIJO)));
            tag.setNumero(c.getString(c.getColumnIndexOrThrow(SqLiteHelper.COLUMN_NUMERO)));
            tag.setTipopago(c.getInt(c.getColumnIndexOrThrow(SqLiteHelper.COLUMN_TIPOPAGO)));
        }
        c.close();
        return  tag;

    }

    public void SaveMotivos(List<modelMotivos> motivos){

        if (motivos.size()>0){

            for (modelMotivos dr : motivos) { /*for each*/
                contentValues.put(SqLiteHelper.ID_COLUMN, dr.getIdMotivo());
                contentValues.put(SqLiteHelper.DESCRIPCION_COLUMN, dr.getDescripcion());

                sql.insert(SqLiteHelper.TABLE_MOTIVOS,null,contentValues);
                contentValues.clear();
            }
        }
    }

    public List<String> getMotivos(){

        c = sql.query(SqLiteHelper.TABLE_MOTIVOS,null,SqLiteHelper.ID_COLUMN + "<?",new String[]{String.valueOf(6)},null,null,null);
        ArrayList<String> Arrmotivos = new ArrayList<>();

        while(c.moveToNext()) {
            Arrmotivos.add(c.getInt(c.getColumnIndexOrThrow(SqLiteHelper.ID_COLUMN)) + "-" + c.getString(c.getColumnIndexOrThrow(SqLiteHelper.DESCRIPCION_COLUMN)));
        }
        c.close();
        return Arrmotivos;
    }

    public modelDatosCuenta getDatosSQLite(){
        modelDatosCuenta modelDatosCuenta = new modelDatosCuenta();
        try {
            c = sql.query(SqLiteHelper.TABLE_DATOSCUENTA, null, null, null, null, null, null);

            if (c.moveToNext()) {
                modelDatosCuenta.setCelular(c.getString(c.getColumnIndexOrThrow(SqLiteHelper.COLUMN_CELULAR)));
                modelDatosCuenta.setEmail(c.getString(c.getColumnIndexOrThrow(SqLiteHelper.COLUMN_EMAIL)));
                modelDatosCuenta.setContrasena(c.getString(c.getColumnIndexOrThrow(SqLiteHelper.COLUMN_CONTRASENA)));
            }
            c.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return modelDatosCuenta;
    }

    public void SaveDatosCuenta(String[] datos){
        if (datos.length>0){
            contentValues.put(SqLiteHelper.COLUMN_CELULAR,datos[0]);
            contentValues.put(SqLiteHelper.COLUMN_EMAIL,datos[1]);
            contentValues.put(SqLiteHelper.COLUMN_CONTRASENA,datos[2]);

            sql.insert(SqLiteHelper.TABLE_DATOSCUENTA,null,contentValues);
            contentValues.clear();
        }
    }

    public void updateMail(String correo){

        try {
            contentValues.put(SqLiteHelper.COLUMN_EMAIL, correo);
            sql.update(SqLiteHelper.TABLE_DATOSCUENTA, contentValues, null, null);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void updateTel(String tel){

        try {
            contentValues.put(SqLiteHelper.COLUMN_CELULAR, tel);
            sql.update(SqLiteHelper.TABLE_DATOSCUENTA, contentValues, null, null);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void updateContra(String contrasena){

        try {
            contentValues.put(SqLiteHelper.COLUMN_CONTRASENA, contrasena);
            sql.update(SqLiteHelper.TABLE_DATOSCUENTA, contentValues, null, null);
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
    public void DeleteDataSession(){
        try{

            sql.delete(SqLiteHelper.TABLE_NAME,null,null);
            sql.delete(SqLiteHelper.TABLE_DATOSCUENTA,null,null);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}
