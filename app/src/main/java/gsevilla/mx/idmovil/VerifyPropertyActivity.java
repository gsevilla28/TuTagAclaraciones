package gsevilla.mx.idmovil;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import models.ModelTags;
import models.ValidaPropiedadTag;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sql.DataSource;
import util.UtilDialog;
import webService.data;
import webService.wsTuTag;

/**
 * Created by Administrator on 06/09/2016.
 */
public class VerifyPropertyActivity extends AppCompatActivity  {

    @BindView(R.id.toolbar_main)
    Toolbar toolbar;

    @BindView(R.id.numtar_verify)
    TextView numtar;

    @BindView(R.id.txt_primeros_registro)
    EditText primerosCuatro;

    @BindView(R.id.txt_ultimos_registro)
    EditText ultimosCuatro;

    private UtilDialog utilDialog;

    private String alias;
    private String prefijo;
    private String tarjeta;
    private int tipopago;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_property);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

         alias= getIntent().getExtras().getString("valias");
         prefijo= getIntent().getExtras().getString("vprefijo");
        tarjeta= getIntent().getExtras().getString("vnumtar");
        tipopago=getIntent().getExtras().getInt("vtipopago");


        numtar.setText(prefijo + tarjeta);

        getSupportActionBar().setTitle("Registro");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ScrollView mainLayout = (ScrollView) findViewById(R.id.verify_layout);
        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(VerifyPropertyActivity.this.getCurrentFocus().getWindowToken(), 0);
                return false;
            }
        });


            findViewById(R.id.btn_continuar_verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(findViewById(android.R.id.content),"Tarjeta correcta",Snackbar.LENGTH_SHORT).show();
            }
        });


        findViewById(R.id.btn_continuar_verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String vnumtar = numtar.getText().toString();
                String vprimeros = primerosCuatro.getText().toString();
                String vultimos = ultimosCuatro.getText().toString();

                utilDialog=  new UtilDialog(VerifyPropertyActivity.this);
                utilDialog.GeneraDialogo("Espere por favor", "Validando datos",false).show();

                new AsyncVerify().execute(vnumtar,vprimeros,vultimos);

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


    private  class AsyncVerify extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(final String... params) {

            wsTuTag wsTuTag = data.getRetrofitInstance().create(webService.wsTuTag.class);

            Call<List<ValidaPropiedadTag>> llamada = wsTuTag.ValidaPropiedadWithQuery(params[0],params[1], params[2]);
            llamada.enqueue(new Callback<List<ValidaPropiedadTag>>() {
                @Override
                public void onResponse(Call<List<ValidaPropiedadTag>> call, Response<List<ValidaPropiedadTag>> response) {

                    if (response.body().get(0).getSuccess().equals("Correcto")) {

                        if (getIntent().getExtras().getInt("vAdicional")==0) {

                            startActivity(new Intent(VerifyPropertyActivity.this, CompleteRegisterActivity.class)
                                    .putExtra("valias", alias)
                                    .putExtra("vprefijo", prefijo)
                                    .putExtra("vnumtar", tarjeta)
                                    .putExtra("vtipopago", tipopago));
                            finish();
                        }
                        else{

                            final DataSource dataSource = new DataSource(VerifyPropertyActivity.this);

                            /*primero obtenemos el idcliente*/
                            final int idcliente = dataSource.getIdCliente();

                            wsTuTag wsTuTag = data.getRetrofitInstance().create(webService.wsTuTag.class);
                            Call<List<ModelTags>> llamadaWS = wsTuTag.RegistrarTagWithquery(idcliente,alias,prefijo,tarjeta,"","","",tipopago); /*registrar tag adicional*/

                            llamadaWS.enqueue(new Callback<List<ModelTags>>() {
                                @Override
                                public void onResponse(Call<List<ModelTags>> call, Response<List<ModelTags>> response) {

                                    /*si es adicional, inserta en bd local*/
                                    dataSource.SaveTags(idcliente,alias,prefijo,tarjeta,tipopago);

                                    startActivity(new Intent(VerifyPropertyActivity.this, MainActivity.class)
                                            .putExtra("valias", alias)
                                            .putExtra("vprefijo", prefijo)
                                            .putExtra("vnumtar", tarjeta)
                                            .putExtra("vtipopago", tipopago));

                                    Toast.makeText(VerifyPropertyActivity.this, "Tarjetas actuales: " + response.body().size() , Toast.LENGTH_SHORT).show();

                                    finish();
                                }

                                @Override
                                public void onFailure(Call<List<ModelTags>> call, Throwable t) {

                                }
                            });


                        }

                    }
                    else{
                        Toast.makeText(VerifyPropertyActivity.this, "El numero de TDC en sistema no coincide con el capturado", Toast.LENGTH_SHORT).show();
                    }

                    utilDialog.CerrarDialogo();

                }

                @Override
                public void onFailure(Call<List<ValidaPropiedadTag>> call, Throwable t) {
                    utilDialog.CerrarDialogo();

                    Toast.makeText(VerifyPropertyActivity.this, "Servicio no disponible", Toast.LENGTH_SHORT).show();

                }
            });



            return null;
        }
    }


}
