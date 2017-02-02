package gsevilla.mx.idmovil;


import android.app.ProgressDialog;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import models.ModelTags;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sql.DataSource;
import util.UtilPreference;
import webService.data;
import webService.wsTuTag;


public class CompleteRegisterActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.toolbar_main)
    Toolbar toolbar;

    private String alias, prefijo , numtar;
    private int tipopago;


    @BindView(R.id.txt_celular)
    EditText celular;
    @BindView(R.id.txt_email)
    EditText email;
    @BindView(R.id.txt_contrasena)
    EditText contrasena;
    @BindView(R.id.txt_confirm_contrasena)
    EditText confirmContrasena;


    private ProgressDialog progressDialog;
    private UtilPreference utilPreference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_complete_register);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Completar Registro");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        alias = getIntent().getExtras().getString("valias");
        prefijo = getIntent().getExtras().getString("vprefijo");
        numtar = getIntent().getExtras().getString("vnumtar");
        tipopago=getIntent().getExtras().getInt("vtipopago");

        findViewById(R.id.btn_termina_registro).setOnClickListener(this);

        /*ScrollView mainLayout = (ScrollView) findViewById(R.id.complete_layout);
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(CompleteRegisterActivity.this.getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        });*/

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_termina_registro){
            RegistraTag();
        }
    }

    private void RegistraTag() {

        String noCelular= this.celular.getText().toString();
        String correo = this.email.getText().toString();
        String contrasena = this.contrasena.getText().toString();
        String confirmaContra = this.confirmContrasena.getText().toString();

         if (TextUtils.isEmpty(noCelular) || TextUtils.isEmpty(correo) || TextUtils.isEmpty(contrasena) || TextUtils.isEmpty(confirmaContra)) {

             progressDialog = new ProgressDialog(this);
             progressDialog.setTitle("Registrando Tag");
             progressDialog.setMessage("Espere por favor...");
             progressDialog.setCancelable(false);
             progressDialog.show();

             if (contrasena.equals(confirmaContra)) {

                 new RegisterNew().execute("0", alias, prefijo, numtar, noCelular, correo, contrasena, String.valueOf(tipopago)); /*ejecuta tarea en 2do plano*/

             } else {
                 cerrarDialog();
                 Toast.makeText(CompleteRegisterActivity.this, "La contrseña y su confirmación no coinciden", Toast.LENGTH_SHORT).show();
             }
         }
        else{
             Snackbar.make(findViewById(android.R.id.content),"Ningun Campo puede quedar vacío",Snackbar.LENGTH_SHORT).show();
         }

    }
    private void cerrarDialog (){
        if (progressDialog != null){
            progressDialog.dismiss();
        }
    }


    private class RegisterNew extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(final String... params) {

            wsTuTag wsTuTag = data.getRetrofitInstance().create(webService.wsTuTag.class);
            Call<List<ModelTags>> llamadaWS = wsTuTag.RegistrarTagWithquery(Integer.parseInt(params[0]),params[1],params[2],params[3],params[4],params[5],params[6],Integer.parseInt(params[7].toString()));

            llamadaWS.enqueue(new Callback<List<ModelTags>>() {
                @Override
                public void onResponse(Call<List<ModelTags>> call, Response<List<ModelTags>> response) {

                    if (response.body().get(0).getIdcliente()>0){ /*si responde con idcliente>0*/

                        /*inserta datos en BD Local*/
                        DataSource dataSource = new DataSource(CompleteRegisterActivity.this);
                        dataSource.SaveTags(response.body().get(0).getIdcliente(),params[1],params[2],params[3],tipopago);

                        /*guarda Login en shared preferences*/
                        utilPreference = new UtilPreference(CompleteRegisterActivity.this);
                        utilPreference.SaveLogin(1);

                        CompleteRegisterActivity.this.cerrarDialog();
                        startActivity(new Intent(CompleteRegisterActivity.this,MainActivity.class));
                        finish();
                    }
                    else{
                        CompleteRegisterActivity.this.cerrarDialog();
                        Toast.makeText(CompleteRegisterActivity.this, "Registro Incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(final Call<List<ModelTags>> call, Throwable t) {
                    CompleteRegisterActivity.this.cerrarDialog();


                    /*si falla la comunicacion con el WS se le da la usuario la opcion de un reintento*/
                    Snackbar.make(findViewById(android.R.id.content),"Servicio no disponible",Snackbar.LENGTH_SHORT)
                            .setAction("Reintentar", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    wsTuTag wsTuTag = data.getRetrofitInstance().create(webService.wsTuTag.class);
                                    Call<List<ModelTags>> llamadaWS = wsTuTag.RegistrarTagWithquery(Integer.parseInt(params[0]),params[1],params[2],params[3],params[4],params[5],params[6],Integer.parseInt(params[7]));
                                    llamadaWS.enqueue(new Callback<List<ModelTags>>() {
                                        @Override
                                        public void onResponse(Call<List<ModelTags>> call, Response<List<ModelTags>> response) {
                                            CompleteRegisterActivity.this.cerrarDialog();
                                            startActivity(new Intent(CompleteRegisterActivity.this,MainActivity.class)
                                                    .putExtra("vidcliente",response.body().get(0).getIdcliente())
                                                    .putExtra("vnumtar",prefijo+numtar));
                                            finish();
                                        }

                                        @Override
                                        public void onFailure(Call<List<ModelTags>> call, Throwable t) {
                                            Toast.makeText(CompleteRegisterActivity.this, "Servicio no disponible", Toast.LENGTH_SHORT).show();

                                            /*quitar esta parte del codigo
                                            /*CompleteRegisterActivity.this.cerrarDialog();
                                            startActivity(new Intent(CompleteRegisterActivity.this,MainActivity.class));
                                            finish();*/

                                        }
                                    });

                                }
                            }).show();
                }

            });


            return null;
        }

    }
}
