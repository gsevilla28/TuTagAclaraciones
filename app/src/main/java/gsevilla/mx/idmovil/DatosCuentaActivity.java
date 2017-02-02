package gsevilla.mx.idmovil;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;

import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import models.modelDatosCuenta;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sql.DataSource;
import util.UtilDialog;
import webService.data;
import webService.wsTuTag;


public class DatosCuentaActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_main)
    Toolbar toolbar;
    @BindView(R.id.txtTel_cuenta)
    TextView txtTel;
    @BindView(R.id.txtEmailCuenta)
    TextView txtEmail;
    @BindView(R.id.txtContraCuenta)
    TextView txtContra;

    AlertDialog.Builder alertDialog;

    private LinearLayout layout;

    private EditText txtDato2;
    private EditText txtDato3;
    private EditText txtDato;


    private DataSource dataSource;
    private UtilDialog vdialog;
    private modelDatosCuenta datos;

    private wsTuTag ws = data.getRetrofitInstance().create(wsTuTag.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_datoscuenta);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Datos de la cuenta");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        txtTel.setOnClickListener(this);
        txtEmail.setOnClickListener(this);
        txtContra.setOnClickListener(this);

        dataSource = new DataSource(this);
        datos = dataSource.getDatosSQLite();

        /*sino hay datos en BD LOCAL*/
        if  (TextUtils.isEmpty(datos.getCelular())){

            vdialog = new UtilDialog(DatosCuentaActivity.this);
            vdialog.GeneraDialogo("Espere por favor","Consultando al Servidor",false).show();

            new obtenDatosCuenta().execute(String.valueOf(dataSource.getIdCliente()));
        }else{
            bindDatosCuenta(datos);
        }


    }

    public void bindDatosCuenta(modelDatosCuenta datos) {
        this.txtTel.setText(datos.getCelular());
        this.txtEmail.setText(datos.getEmail());
        this.txtContra.setText(datos.getContrasena());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtTel_cuenta:
                actualizaTelefono();
                break;
            case R.id.txtEmailCuenta:
                actualizaEmail();
                break;
            case R.id.txtContraCuenta:
                actualizaContrasena();
                break;
        }

    }

    private void actualizaContrasena() {
        alertDialog = alert("Cambiar Contraseña","contraseña", R.drawable.ic_action_communication_vpn_key);
        alertDialog.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (txtDato2.getText().toString().equals(txtDato3.getText().toString())) {

                    vdialog = new UtilDialog(DatosCuentaActivity.this);
                    vdialog.GeneraDialogo("Espere por favor","Contactando al servidor central",false).show();
                    new CambiarDatosCuenta().execute(String.valueOf(dataSource.getIdCliente()), txtDato2.getText().toString(), txtEmail.getText().toString(),txtTel.getText().toString());
                }
                else{
                    Toast.makeText(DatosCuentaActivity.this,"La contraseña nueva y su confirmacíon no coinciden",Toast.LENGTH_SHORT).show();
                }

            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


             }
        }).show();
    }

    private void actualizaEmail() {

        alertDialog =  alert("Actualiza Email","Email",R.drawable.ic_action_communication_email);
        alertDialog.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /*buscar el EditText Sobre el LayOut que se construyó para el alert dialog*/
                //EditText txtEmail = (EditText) layout.findViewWithTag("txtDato");
                //String email = txtEmail.getText().toString();
                String email = txtDato.getText().toString();

                if (!TextUtils.isEmpty(email)) {

                    vdialog = new UtilDialog(DatosCuentaActivity.this);
                    vdialog.GeneraDialogo("Contactando al servidor central","Espere por favor",false).show();
                    new CambiarDatosCuenta().execute(String.valueOf(dataSource.getIdCliente()), txtContra.getText().toString(), email ,txtTel.getText().toString());

                }
                else
                    Toast.makeText(DatosCuentaActivity.this,"El nuevo correo no puede estar vacio",Toast.LENGTH_SHORT).show();

            }

        })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }

    private void actualizaTelefono() {

        alertDialog = alert("Actualiza Número","número" ,R .drawable.ic_action_communication_call);
        alertDialog.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        /*buscar el EditText Sobre el LayOut que se construyó para el alert dialog*/
                        EditText txtTel = (EditText) layout.findViewWithTag("txtDato");
                        String telefono = txtTel.getText().toString();

                        if (!TextUtils.isEmpty(telefono)) {
                            dataSource.updateTel(txtTel.getText().toString());
                            bindDatosCuenta(dataSource.getDatosSQLite());
                        }
                        else
                            Toast.makeText(DatosCuentaActivity.this,"El nuevo Telfono no puede estar vacio",Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .show();
    }


    private AlertDialog.Builder alert(String title,String hint ,int IconId){

        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        txtDato = new EditText(this);

        txtDato.setHint(String.format("Nuevo %s", hint));
        if (hint.equals("Email")) {
            layout.addView(txtDato);
            txtDato.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT);
            txtDato.setTag("txtDato");
        }
        else if (hint.equals("número")) {
            layout.addView(txtDato);
            txtDato.setTag("txtDato");
            txtDato.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        else{
            txtDato.setHint("Contraseña actual");
            txtDato.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
            txtDato.animate();
            txtDato2 = new EditText(this);
            txtDato2.setTag("txtDato2");
            txtDato2.setHint("Nueva contraseña");
            txtDato2.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            txtDato2.setTransformationMethod(PasswordTransformationMethod.getInstance());
            txtDato2.animate();
            txtDato3 = new EditText(this);
            txtDato3.setTag("txtDato3");
            txtDato3.setHint("Confirmar contraseña");
            txtDato3.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            txtDato3.setTransformationMethod(PasswordTransformationMethod.getInstance());
            txtDato3.animate();
            layout.addView(txtDato);
            layout.addView(txtDato2);
            layout.addView(txtDato3);
        }

        alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(IconId)
                .setTitle(title)
                .setView(layout)
                .setCancelable(false)
                .create();

        return  alertDialog;
    }


    private class obtenDatosCuenta extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(String... params) {

            Call<String[]> llamada = ws.ObtenerDatosContacto(params[0]);

            llamada.enqueue(new Callback<String[]>() {
                @Override
                public void onResponse(Call<String[]> call, Response<String[]> response) {
                    vdialog.CerrarDialogo();

                    if (response.body() != null){
                        dataSource.SaveDatosCuenta(response.body()); /*guardar los datos de la cuenta en BD*/
                        DatosCuentaActivity.this.bindDatosCuenta(dataSource.getDatosSQLite());

                    }else{
                        Toast.makeText(DatosCuentaActivity.this,"Sin respuesta del servidor central",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<String[]> call, Throwable t) {
                    vdialog.CerrarDialogo();
                    Toast.makeText(DatosCuentaActivity.this,"No se pudo conectar con el Servidor, revise su conexión a internet", Toast.LENGTH_SHORT).show();
                }
            });

            return null;
        }
    }

    private class CambiarDatosCuenta extends AsyncTask<String,Void,Void>{

        @Override
        protected Void doInBackground(final String... params) {
            Call<Integer> cambio = ws.CambiarDatos(Integer.parseInt(params[0]), params[1],params[2], params[3]); //idcliente,contraseña,email,tel

            cambio.enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    vdialog.CerrarDialogo();
                    if (response.body() != null) {
                        if (response.body() == 10) { //cambio exitoso
                            dataSource.updateContra(params[1]);
                            dataSource.updateTel(params[3]);
                            dataSource.updateMail(params[2]);
                            bindDatosCuenta(dataSource.getDatosSQLite());
                            Toast.makeText(DatosCuentaActivity.this, "Cambio realizado correctamente", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DatosCuentaActivity.this, "No se pudo Realizar el cambio, intente mas tarde", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(DatosCuentaActivity.this, "Sin respuesta del servidor, revise su conexión a internet", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    vdialog.CerrarDialogo();
                    Toast.makeText(DatosCuentaActivity.this,"Sin Conexión a Internet", Toast.LENGTH_SHORT).show();

                }
            });
            return null;
        }
    }
}
