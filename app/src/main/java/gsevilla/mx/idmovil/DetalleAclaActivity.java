package gsevilla.mx.idmovil;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.List;
import java.util.zip.Inflater;

import Adapter.aclaracionesAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import models.Login;
import models.ModelTags;
import models.ValidaPropiedadTag;
import models.ValidationTag;
import models.aclaracionesResponse;
import models.modelAclaracion;
import models.modelCruces;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;
import webService.data;
import webService.wsTuTag;

/**
 * Created by Administrator on 18/10/2016.
 */


public class DetalleAclaActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_main)
    Toolbar toolbar;

    @BindView(R.id.progress_bar_aclara)
    ProgressBar progressBar;

    @BindView(R.id.recycler_view_aclara)
    RecyclerView recyclerViewAcla;

   
    private String numtar;
    private aclaracionesAdapter aclaracionesAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_aclaraciones);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        numtar = getIntent().getStringExtra("numtar");
        getSupportActionBar().setTitle(numtar);
        getSupportActionBar().setSubtitle("Detalle de Aclaraciones");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewAcla.setLayoutManager(linearLayoutManager);


        aclaracionesAdapter = new aclaracionesAdapter();
        aclaracionesAdapter.setOnItemClickListener(new aclaracionesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final aclaracionesResponse aclaracionesResponse) {
                /*aqui va un alert dialog*/

                AlertDialog.Builder builder = new AlertDialog.Builder(DetalleAclaActivity.this);

                LayoutInflater inflater = getLayoutInflater();

                View dialoglayout = inflater.inflate(R.layout.dialog_signin, null);

                TextView txtImportCruce = (TextView) dialoglayout.findViewById(R.id.importeCruce_pop);


                //dependiendo del dictamen se mostrará un dialogo diferente
                if (aclaracionesResponse.getStatus().equals("NUEVO")) {

                    txtImportCruce.setText("Su aclaración aún se encuentra en revisión");
                    txtImportCruce.setGravity(Gravity.CENTER);

                    builder.setView(dialoglayout) //le agregamos la vista que inflamos arriba
                            // Add action buttons
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            })
                            .create().show();

                }
                else {

                    TextView txtComentario = (TextView) dialoglayout.findViewById(R.id.comentario_pop);
                    TextView txtFechaDictamen = (TextView) dialoglayout.findViewById(R.id.fechaDictamen_pop);
                    TextView txtFechaCruce = (TextView) dialoglayout.findViewById(R.id.FechaCruce_pop);
                    TextView txtCasetaCruce = (TextView) dialoglayout.findViewById(R.id.CasetaCruce_pop);

                    txtFechaDictamen.setText("Fecha Dictamen: " + aclaracionesResponse.getFechaDictamen());
                    txtComentario.setText("Dictamen: " + aclaracionesResponse.getComentarioDictamen());
                    txtImportCruce.setText(String.format("Importe Cruce: %.2f", aclaracionesResponse.getImporteCruce()));
                    txtFechaCruce.setText("Fecha: " + aclaracionesResponse.getFechaCruce() + " " + aclaracionesResponse.getHoraCruce());
                    txtCasetaCruce.setText("Caseta: " + aclaracionesResponse.getNomCaseta());

                    builder.setView(dialoglayout) //le agregamos la vista que inflamos arriba

                            .setPositiveButton("Enterado", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            })
                            .setNegativeButton("Redictaminar", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    modelCruces modelCruces = new modelCruces();
                                    //modelCruces.setCarril(aclaracionesResponse.get);
                                    modelCruces.setCaseta(aclaracionesResponse.getNomCaseta());
                                    modelCruces.setFecha(aclaracionesResponse.getFechaCruce());
                                    modelCruces.setHora(aclaracionesResponse.getHoraCruce());
                                    modelCruces.setMonto(aclaracionesResponse.getImporteCruce());
                                    modelCruces.setIdFolio(aclaracionesResponse.getIdfolio());

                                    startActivity(new Intent(DetalleAclaActivity.this,ingresa_aclaracion.class)
                                    .putExtra("numtar",numtar)
                                    .putExtra("cruce",modelCruces));
                                    finish();
                                }
                            })
                            .create().show();
                }

            }


        });

        new ConsultaAclaraciones().execute(numtar);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

        }
        return  true;
    }




    private class ConsultaAclaraciones extends AsyncTask<String,Integer,Boolean>{

        @Override
        protected void onPreExecute() {

        }


        @Override
        protected Boolean doInBackground(String... params) {

            wsTuTag wsTuTag = data.getRetrofitInstance().create(webService.wsTuTag.class);
            String vnumtar =  params[0].toString() + "..";

            Call<List<aclaracionesResponse>> getAclaraciones = wsTuTag.GetAclaraciones(vnumtar);

            getAclaraciones.enqueue(new Callback<List<aclaracionesResponse>>() {
                @Override
                public void onResponse(Call<List<aclaracionesResponse>> call, Response<List<aclaracionesResponse>> response) {

                    if (response.body() != null ){

                       progressBar.setVisibility(View.GONE);

                       aclaracionesAdapter.setaclaracionesResponse(response.body());
                       recyclerViewAcla.setAdapter(aclaracionesAdapter);


                    }
                    else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Snackbar.make(findViewById(android.R.id.content), "No hubo respuesta del servidor central", Snackbar.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<aclaracionesResponse>> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Snackbar.make(findViewById(android.R.id.content),"No se pudo conectar con el servidor central",Snackbar.LENGTH_SHORT).show();

                }
            });

            return true;
        }



        @Override
        protected void onPostExecute(Boolean result) {

        }

        @Override
        protected void onCancelled() {

        }

    }


}
