package Adapter;

import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

import gsevilla.mx.idmovil.R;

import models.modelCruces;

/**
 * Created by Administrator on 20/09/2016.
 */
public class crucesAdapter extends RecyclerView.Adapter<crucesViewHolder> {

    private List<modelCruces> modelCrucesList;

    public crucesAdapter (){}


    @Override
    public crucesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new crucesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.items_cruces,parent,false));
    }

    @Override
    public void onBindViewHolder(crucesViewHolder holder, int position) {
        /*modelo o pojo de cruces*/
        modelCruces modelcruces= modelCrucesList.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Long fecha = Long.parseLong(modelcruces.getFecha().replace("/Date(","").replace(")/",""));
        Date date = new Date(fecha - (60 * 60 * 1000)); /*Le resto una hora por que el ws me la regresa mal*/
        String fechaformato= sdf.format(date);

        /*llenando los datos*/
        holder.txtCorredor.setText(modelcruces.getTramo());
        holder.txtCaseta.setText(modelcruces.getCaseta());
        holder.txtFecha.setText("Fecha: " + fechaformato);
        holder.txtCosto.setText("$" + String.valueOf(modelcruces.getMonto()));

    }

    @Override
    public int getItemCount() {
        return modelCrucesList != null ? modelCrucesList.size():0;
    }

    public void setModelCrucesList(List<modelCruces> vmodelCruces){
        modelCrucesList=vmodelCruces;
    }

}
