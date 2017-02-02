package Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;


import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import Adapter.crucesAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

import gsevilla.mx.idmovil.R;

import gsevilla.mx.idmovil.ingresa_aclaracion;
import models.modelCruces;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import webService.data;
import webService.wsTuTag;


public class fragment_movimientos extends Fragment {

    /*en los fragmentos no hay comunicacion por Intent, entonces se tiene que crear un Bundle*/
    public static fragment_movimientos NuevaInstancia(String numtar){
        fragment_movimientos f = new fragment_movimientos();
        Bundle b = new Bundle();
        b.putString("vnumtar",numtar);
        f.setArguments(b);
        return f;
    }

    public static final int REQUEST_RESULT_ACLARA = 1000;


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;


    private String numtar;
    private crucesAdapter crucesAdapter;

    //private ProgressBar progressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_movimientos,container,false);
        ButterKnife.bind(this,view);

        //progressBar = (ProgressBar) findViewById(R.id.progress_main);


        Bundle b = getArguments();
        numtar=b.getString("vnumtar");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(linearLayoutManager);

        crucesAdapter = new crucesAdapter();
        crucesAdapter.setOnItemClickListener(new crucesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(modelCruces modelCruces) {

                startActivityForResult(new Intent(getActivity(),ingresa_aclaracion.class)
                        .putExtra("cruce",modelCruces)
                        .putExtra("numtar",numtar),REQUEST_RESULT_ACLARA);

            }
        });

           ObtenerCrucesWS(numtar);

        return view;

    }

    private void ObtenerCrucesWS(String Tarjeta) {

        wsTuTag wstutag = data.getRetrofitInstance().create(wsTuTag.class);

        String fInicio = getResources().getString(R.string.fechaInicioCruces);
        String fFiinn = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        Call<List<modelCruces>> llamadaWSCruces = wstutag.ObtenerCrucesWithQuery(Tarjeta,fInicio,fFiinn);

        llamadaWSCruces.enqueue(new Callback<List<modelCruces>>() {


            @Override
            public void onResponse(Call<List<modelCruces>> call, Response<List<modelCruces>> response)  {
               // progressBar.setVisibility(recyclerView.GONE);
                try {
                    if (response.body().size() != 0) {
                        crucesAdapter.setModelCrucesList(response.body());
                        recyclerView.setAdapter(crucesAdapter);
                    } else {
                        //Snackbar.make(getView(),"no hay datos con la tag seleccionada", Snackbar.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(), "no hay datos con la tag seleccionada", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(getActivity(), "No se han encontrado datos con la tag indica, verifique que no se haya migrado a un cliente corporativo", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();

                }

            }

            @Override
            public void onFailure(Call<List<modelCruces>> call, Throwable t) {
                //Progress_main.setVisibility(recyclerView.GONE);
                Toast.makeText(getActivity(),"No se pudo conectar con el servidor central",Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (fragment_movimientos.REQUEST_RESULT_ACLARA==requestCode && resultCode==-1){
            String FolioAclaracion = data.getExtras().getString("Folio");
            String numtar =data.getExtras().getString("Tarjeta");

            Toast.makeText(getActivity(),"Folio Generado: " + FolioAclaracion, Toast.LENGTH_LONG).show();

            ObtenerCrucesWS(numtar.replace("..",""));

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
