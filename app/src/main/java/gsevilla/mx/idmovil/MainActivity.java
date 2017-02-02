package gsevilla.mx.idmovil;


import android.content.DialogInterface;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Fragments.fragment_movimientos;
import butterknife.BindView;
import butterknife.ButterKnife;

import models.ModelTags;

import models.modelMotivos;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import sql.DataSource;

import util.UtilDialog;
import util.UtilPreference;
import webService.data;
import webService.wsTuTag;


public class MainActivity extends AppCompatActivity {


    @BindView(R.id.toolbar_main)
    Toolbar toolbar;

    @BindView(R.id.navigationView)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @BindView(R.id.spinner_tags)
    Spinner spinner_tags;

    @BindView(R.id.txt_alias_tag_main)
    TextView txt_alias;

    @BindView(R.id.txt_tipopago_main)
    TextView txtTipoPago;

    /*@BindView(R.id.btn_floating)
    FloatingActionButton btn_floating;*/

    /*@BindView(R.id.txtIdCliente)
    TextView txtidcliente;*/

    @BindView(R.id.progress_main)
     ProgressBar progressBarMain;


    TextView TarjetaHeader;
    TextView AliasHeader;

    private UtilPreference utilPreference;
    private UtilDialog dialogo;
    private static final int REQUEST_CODE_REGISTER = 99;
    private DataSource dataSource;
    private String tarjeta;
    private wsTuTag wsTuTag = data.getRetrofitInstance().create(webService.wsTuTag.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        utilPreference = new UtilPreference(this);

        getSupportActionBar().setTitle(R.string.Movimientos);

        dataSource = new DataSource(this);/*inicializando metodo de SqLite*/
        /*btn_floating.setImageResource(R.drawable.ic_add_white_24dp);
        btn_floating.setOnClickListener(this);*/

        /*traer las tags de la BD local*/
        List<ModelTags> tagsLocal = dataSource.getTags();

        /*si no hay tags en Bd local pero el login es correcto entonces se tiene que traer los datos del WS*/
        dialogo = new UtilDialog(MainActivity.this);
        if (tagsLocal.size()<1){
            int  idlciente = getIntent().getExtras().getInt("idcliente");
            dialogo.GeneraDialogo("Conectando con el Servidor","Espere por favor",false).show();
            new ObtenerTags().execute(String.valueOf(idlciente));
        }
        else{ /*si hay tags se invoca funcion para llenar el spinner*/
            bindSpinner(tagsLocal);
        }

        /*verificar si la tabla de motivos se encuentra llena en DB Local, de lo contrario consultará WS*/
        if (dataSource.getMotivos().size()<1){
            dialogo.GeneraDialogo("Consultando al Servidor","Espere por favor",false).show();
            new ObtenerMotivos().execute();

        }

        /*cuando se seleccione una tarjeta*/
        spinner_tags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                progressBarMain.setVisibility(View.VISIBLE);

                tarjeta = parent.getItemAtPosition(position).toString();
                //Toast.makeText(MainActivity.this, "seleccionado: " + tarjeta, Toast.LENGTH_SHORT).show();

                ModelTags detailTag = dataSource.getTagDetail(tarjeta);

                txt_alias.setText(detailTag.getAlias());
                txtTipoPago.setText(detailTag.getTipopago() == 1 ? R.string.tipoPago1 : R.string.tipoPago2);

                /*asignando valores de acuerdo a la tag que fue selccionada*/
                TarjetaHeader = (TextView) navigationView.findViewById(R.id.txtTarjeta_NavigationHeader);
                AliasHeader = (TextView) navigationView.findViewById(R.id.txtAlias_NavigationHeader);
                TarjetaHeader.setText(tarjeta);
                AliasHeader.setText(detailTag.getAlias());
                getFragmentManager().beginTransaction().replace(R.id.fragment_holder,fragment_movimientos.NuevaInstancia(tarjeta)).commit();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBarMain.setVisibility(View.GONE);
                    }
                },1000*3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*opciones del menu deslizante*/
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();

                switch (item.getItemId()) {
                    case R.id.menu_add_tag:
                        RegistrarnuevaTag();
                        break;
                    case R.id.menu_salir:
                        LogOut();
                        break;
                    case R.id.menu_detalleAclaraciones:
                        DetalleAclaraciones();
                        break;
                    case R.id.menu_cuenta:
                        startActivity(new Intent(MainActivity.this,DatosCuentaActivity.class));
                        break;
                    case R.id.menu_contacto:
                        startActivity(new Intent(MainActivity.this, Contacto.class));
                        break;
                }

                return false;
            }

        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_eliminar) {

            if (dataSource.getTags().size() != 1) {

                new AlertDialog.Builder(this)
                        .setTitle("Confirmación")
                        .setMessage(String.format("¿Estas Seguro de eliminar la tag N° %s de tu cuenta?", tarjeta))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogo.GeneraDialogo("Consultando al Servidor", "Espere por favor", false).show();
                                new EliminaTagsDeLaCuenta().execute(tarjeta);

                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
            }
            else{

                new AlertDialog.Builder(this)
                        .setTitle("Aviso")
                        .setMessage("No se puede eliminar la única tarjeta de su cuenta")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton(android.R.string.ok , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                            }
                        }).create().show();
            }
        }
        return true;
    }

    private void DetalleAclaraciones() {
        startActivity(new Intent(MainActivity.this,DetalleAclaActivity.class).putExtra("numtar",tarjeta));
    }


    private void RegistrarnuevaTag() {
        startActivityForResult(new Intent(MainActivity.this, RegisterActivity.class)
                .putExtra("vAdicional",1),REQUEST_CODE_REGISTER);
    }

    private void LogOut() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmación")
                .setMessage("¿Estas seguro de cerrar la sesión?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        utilPreference.DeleteLogin();
                        dataSource.DeleteDataSession();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        finish();
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setCancelable(false).create().show();
    }

    /*@Override
    public void onClick(View v) {
        if (v.getId()== R.id.btn_floating){
            RegistrarnuevaTag();
        }
    }*/

    private void bindSpinner(List<ModelTags> tagsLocal){ /*metodo para llenar el spinner con todas las tags*/
        ArrayList<String> listaTags = new ArrayList<>();

        for (int i = 0; i <= tagsLocal.size() - 1; i++) { /*llenar el arraylist solo con los datos necesarios*/
            listaTags.add(tagsLocal.get(i).getPrefijo() + tagsLocal.get(i).getNumero());
        }
        /*creando el adaptador y se le envia el arreglo de tags*/
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaTags);
        spinner_tags.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_REGISTER==requestCode && resultCode==RESULT_OK){

            List<ModelTags> tagsLocal = dataSource.getTags();
            bindSpinner(tagsLocal);
        }
        else
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class ObtenerTags extends AsyncTask<String,String,String> { /*tarea Async para traer las tags sino existen en BD Local*/
        List<ModelTags> tagsLocal;

        @Override
        protected String doInBackground(String... params) {
            //wsTuTag = data.getRetrofitInstance().create(webService.wsTuTag.class);
            Call<List<ModelTags>> listadeTags= wsTuTag.RegistrarTagWithquery(Integer.parseInt(params[0]),"","","","","","",0);

            listadeTags.enqueue(new Callback<List<ModelTags>>() {
                @Override
                public void onResponse(Call<List<ModelTags>> call, Response<List<ModelTags>> response) {

                    tagsLocal= response.body();

                    final DataSource dataSource = new DataSource(MainActivity.this);/*inicializando metodo de SqLite*/
                    for (ModelTags mt : tagsLocal) { /*equivalencia de for each en C#*/
                        dataSource.SaveTags(mt.getIdcliente(),mt.getAlias(),mt.getPrefijo(),mt.getNumero(),mt.getTipopago());/*guardando en BD Local*/
                    }
                    dialogo.CerrarDialogo();
                    MainActivity.this.bindSpinner(tagsLocal);
                }

                @Override
                public void onFailure(Call<List<ModelTags>> call, Throwable t) {

                }
            });

            return null;
        }

    }

    private class ObtenerMotivos extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            //wsTuTag = data.getRetrofitInstance().create(webService.wsTuTag.class);
            Call<List<modelMotivos>> ListaMotivos = wsTuTag.ObtenerMotivosAclaraWithQuery();

            ListaMotivos.enqueue(new Callback<List<modelMotivos>>() {
                @Override
                public void onResponse(Call<List<modelMotivos>> call, Response<List<modelMotivos>> response) {
                    if (response.body() != null) {
                        dataSource.SaveMotivos(response.body());
                    }
                    dialogo.CerrarDialogo();
                }

                @Override
                public void onFailure(Call<List<modelMotivos>> call, Throwable t) {
                    dialogo.CerrarDialogo();
                }
            });

            return null;
        }
    }

    private class EliminaTagsDeLaCuenta extends AsyncTask<String, Void,Void>{

        @Override
        protected Void doInBackground(final String... strings) {

            Call<Boolean> respuesta = wsTuTag.QuitarTagDeLaCuenta(strings[0]);
            respuesta.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    dialogo.CerrarDialogo();
                    try {
                        if (response.body()) {
                            dataSource.DeleteTag(strings[0]);
                            List<ModelTags> tagsLocal = dataSource.getTags();
                            MainActivity.this.bindSpinner(tagsLocal);
                            Toast.makeText(MainActivity.this, "La tarjeta ha sido eliminada", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    dialogo.CerrarDialogo();
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    dialogo.CerrarDialogo();
                    Toast.makeText(MainActivity.this, "Falla con su conexión a internet",Toast.LENGTH_SHORT ).show();

                }
            });




            return null;
        }
    }


}
