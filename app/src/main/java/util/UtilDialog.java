package util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Administrator on 15/09/2016.
 */
public class UtilDialog extends ProgressDialog {

    private ProgressDialog progressDialog;

    public UtilDialog(Context context) {
        super(context);
        progressDialog = new ProgressDialog(context);
    }

    public ProgressDialog GeneraDialogo (String mensaje, String titulo, boolean fijo){

        progressDialog.setMessage(mensaje);
        progressDialog.setTitle(titulo);
        progressDialog.setCancelable(fijo);
        //progressDialog.show();

        return progressDialog;

    }
    public void CerrarDialogo(){
        if (progressDialog != null){
            progressDialog.dismiss();
        }

    }



}
