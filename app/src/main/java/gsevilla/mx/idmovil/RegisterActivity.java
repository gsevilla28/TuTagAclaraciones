package gsevilla.mx.idmovil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import models.ModelTags;
import models.ValidationTag;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sql.DataSource;
import webService.data;
import webService.wsTuTag;

/**
 * Created by Administrator on 06/09/2016.
 */
public class RegisterActivity extends AppCompatActivity{

    @BindView(R.id.toolbar_main)
    Toolbar toolbar;

    @BindView(R.id.spinner_prefijos)
    Spinner spinner_prefijos;

    @BindView(R.id.txt_alias_registro)
    EditText txtAlias;
    @BindView(R.id.txt_numtar_registro)
    EditText txtNumtar;

    private ProgressDialog progressDialog;

    private int adicional=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); /*indica que no debe girar la pantalla*/

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Registro");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        /*metodo para ocultar el teclado cuando se le de un tab en cualquier lugar de la pantalla*/
        ScrollView Register = (ScrollView) findViewById(R.id.register_layout);
        Register.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(RegisterActivity.this.getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        });


        /*llenando el spinner con datos obtenidos de un values ///tipos_tag///*/
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,R.array.array_tipotag,R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_prefijos.setAdapter(adapter);

        /*bandera para saber si es un registro nuevo o una tag adicional*/
        try {
            adicional = getIntent().getExtras().getInt("vAdicional", 0);
        }
        catch(Exception e) {
            adicional=0;
        }


        findViewById(R.id.btn_continuar_registro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!TextUtils.isEmpty(txtAlias.getText())){
                    if(!TextUtils.isEmpty(txtNumtar.getText())){
                        /*aqui consulta el WS para saber sino ha sido registrada, */

                        progressDialog = new ProgressDialog(RegisterActivity.this);
                        progressDialog.setTitle("Verificando datos");
                        progressDialog.setMessage("Espere por favor...");
                        progressDialog.show();

                        String alias = txtAlias.getText().toString();
                        String prefijo = (String) spinner_prefijos.getItemAtPosition(spinner_prefijos.getSelectedItemPosition());
                        String numtar = txtNumtar.getText().toString();

                        new validaRegistro().execute(alias,prefijo, numtar ); /*inicia tarea en 2do plano*/

                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "El número de tag es necesario", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Indique un alias para su Tag", Toast.LENGTH_SHORT).show();
                }
            }
        });


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

    private void CerrarDialog (){
        if (RegisterActivity.this.progressDialog != null){
            RegisterActivity.this.progressDialog.dismiss();
        }
    }

    private class validaRegistro extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(final String... params) {
            wsTuTag wsTuTag = data.getRetrofitInstance().create(webService.wsTuTag.class);
            Call<List<ValidationTag>> call = wsTuTag.getValidaTagWithQuery(params[1] + params[2]);

            final String alias = params[0];
            final String prefijo = params[1];
            final String numtar = params[2];

            call.enqueue(new Callback<List<ValidationTag>>() {
                @Override
                public void onResponse(Call<List<ValidationTag>> call, Response<List<ValidationTag>> response) {

                    final int vtipopago;

                    /*valida que */
                    if(response.body().get(0).getTabla().equals("TAGSUP") && response.body().get(0).getValidacion()==1 ) {
                        /*valida que no exita un registro previo en app*/
                            if(response.body().get(1).getTabla().equals("REGISTROAPP") && response.body().get(1).getValidacion() ==0) {

                                vtipopago=response.body().get(0).getTipoPago();

                            /*valida el tipo de pago*/
                                if (vtipopago == 1) { /*si es postpago*/

                                    RegisterActivity.this.CerrarDialog(); /*cerrar el bloqueo*/
                                    startActivity(new Intent(RegisterActivity.this, VerifyPropertyActivity.class)
                                            .putExtra("valias", alias)
                                            .putExtra("vprefijo", prefijo)
                                            .putExtra("vnumtar", numtar)
                                            .putExtra("vtipopago", vtipopago)
                                            .putExtra("vAdicional", adicional));
                                    finish();
                                } else { /*si es prepago se va completar el registro. */

                                    if (adicional == 0) { /*si es registro de cliente nuevo*/

                                         /*cerrar el bloqueo*/
                                        startActivity(new Intent(RegisterActivity.this, CompleteRegisterActivity.class)
                                                .putExtra("valias", alias)
                                                .putExtra("vprefijo", prefijo)
                                                .putExtra("vtipopago", vtipopago)
                                                .putExtra("vnumtar", numtar));
                                        finish();
                                    } else { /*si es tag adicional*/

                                        final DataSource dataSource = new DataSource(RegisterActivity.this);
                                        /*primero obtenemos el idcliente*/
                                        final int idcliente = dataSource.getIdCliente();

                                        wsTuTag wsTuTag = data.getRetrofitInstance().create(webService.wsTuTag.class);
                                        Call<List<ModelTags>> llamadaWS = wsTuTag.RegistrarTagWithquery(idcliente, alias, prefijo, numtar, "", "", "", vtipopago); /*registrar tag adicional*/

                                        llamadaWS.enqueue(new Callback<List<ModelTags>>() {
                                            @Override
                                            public void onResponse(Call<List<ModelTags>> call, Response<List<ModelTags>> response) {

                                                RegisterActivity.this.CerrarDialog();
                                                dataSource.SaveTags(idcliente, alias, prefijo, numtar,vtipopago);
                                                //startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                                                Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                                                setResult(RESULT_OK,i);
                                                finish();

                                            }

                                            @Override
                                            public void onFailure(Call<List<ModelTags>> call, Throwable t) {
                                                /*si el WS no contesta*/
                                                Toast.makeText(RegisterActivity.this,"Servicio no disponible",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }

                            }
                            else {
                                RegisterActivity.this.CerrarDialog(); /*cerrar el bloqueo*/
                                /*muestra un cuadro de dialogo*/
                                new AlertDialog.Builder(RegisterActivity.this)
                                        .setTitle("Atención")
                                        .setMessage("La tag indicada ya se encuentra registrada")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).setCancelable(false).create().show();
                            }
                    }
                    else{ /*no existe la tag en el catalogo*/
                        RegisterActivity.this.CerrarDialog(); /*cerrar el bloqueo*/
                            /*muestra un cuadro de dialogo*/
                        new AlertDialog.Builder(RegisterActivity.this)
                                .setMessage("La tarjeta no existe en el catalogo")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).setCancelable(false).create().show();
                    }

                }

                @Override
                public void onFailure(Call<List<ValidationTag>> call, Throwable t) {
                    RegisterActivity.this.CerrarDialog(); /*cerrar el bloqueo*/
                    Toast.makeText(RegisterActivity.this, "Servicio no Disponible", Toast.LENGTH_SHORT).show();

                }
            });

            return "";
        }

    }

}


