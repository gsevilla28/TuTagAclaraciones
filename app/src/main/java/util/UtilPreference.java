package util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import models.Login;

/**
 * Created by Administrator on 12/09/2016.
 */
public class UtilPreference {

    private static final String File_Name = "PreferencesMiTag";
    private final SharedPreferences sharedPreferences;


    public UtilPreference(Context context) {
        sharedPreferences = context.getSharedPreferences(File_Name,context.MODE_PRIVATE);
    }

    /*guardar usuario y contraseña*/
    public void SaveLogin(int loginSuccess){

        sharedPreferences.edit().putInt("usuario",loginSuccess).apply();
        //sharedPreferences.edit().putString("contrasena",login.getContrasena()).apply();

    }

    public int GetLogin(){

        int success = sharedPreferences.getInt("usuario",0);

        return success;

    }

    public void DeleteLogin() {
        sharedPreferences.edit().remove("usuario").apply();
    }
}
