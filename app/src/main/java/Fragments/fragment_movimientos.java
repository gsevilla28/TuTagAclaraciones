package Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import Adapter.crucesAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import gsevilla.mx.idmovil.R;

import models.modelCruces;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import webService.data;
import webService.wsTuTag;

/**
 * Created by Administrator on 22/09/2016.
 */
public class fragment_movimientos extends Fragment {

    /*en los fragmentos no hay comunicacion por Intent, entonces se tiene que crear un Bundle*/
    public static fragment_movimientos NuevaInstancia(String numtar){
        fragment_movimientos f = new fragment_movimientos();
        Bundle b = new Bundle();
        b.putString("vnumtar",numtar);
        f.setArguments(b);
        return f;
    }


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private String numtar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_movimientos,container,false);
        ButterKnife.bind(this,view);

        Bundle b = getArguments();
        numtar=b.getString("vnumtar");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(linearLayoutManager);

        final crucesAdapter crucesAdapter = new crucesAdapter();


            wsTuTag wstutag = data.getRetrofitInstance().create(wsTuTag.class);
            Call<List<modelCruces>> llamadaWSCruces = wstutag.ObtenerCrucesWithQuery(numtar,"01/01/2016","10/09/2016");

            llamadaWSCruces.enqueue(new Callback<List<modelCruces>>() {
                @Override
                public void onResponse(Call<List<modelCruces>> call, Response<List<modelCruces>> response) {

                    crucesAdapter.setModelCrucesList(response.body());
                    recyclerView.setAdapter(crucesAdapter);

                }

                @Override
                public void onFailure(Call<List<modelCruces>> call, Throwable t) {

                }
            });


        return view;

    }
}
