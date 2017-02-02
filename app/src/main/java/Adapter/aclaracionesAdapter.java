package Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import gsevilla.mx.idmovil.R;
import models.aclaracionesResponse;

/**
 * Created by Administrator on 02/11/2016.
 */

public class aclaracionesAdapter extends RecyclerView.Adapter<aclaracionesViewHolder> {

    public aclaracionesAdapter (){}

    private List<aclaracionesResponse> modelAclaracionesAdapter;
    private OnItemClickListener onItemClickListener;


    @Override
    public aclaracionesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new aclaracionesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.items_detalle_aclaraciones,parent,false));
    }

    @Override
    public void onBindViewHolder(aclaracionesViewHolder holder, int position) {

        aclaracionesResponse aclaracionesAdapter = modelAclaracionesAdapter.get(position);

        holder.txtidfolio.setText("Folio: " + aclaracionesAdapter.getIdfolio());
        holder.txtFechaSolicitud.setText("Fecha Solicitud: " + aclaracionesAdapter.getFechaAlta());
        holder.txtEstatus.setText("Estatus: " + aclaracionesAdapter.getStatus());
        holder.txtImporteD.setText("Importe Devuelto: $" + String.format("%.2f", aclaracionesAdapter.getImporteDevuelto()));

        holder.setItemClick(aclaracionesAdapter,onItemClickListener);


    }

    @Override
    public int getItemCount() {
        return modelAclaracionesAdapter != null ? modelAclaracionesAdapter.size() : 0;
    }

    public void setaclaracionesResponse(List<aclaracionesResponse> setAclaracionesAdapter){
        modelAclaracionesAdapter=setAclaracionesAdapter;
    }

    public void setOnItemClickListener(OnItemClickListener vonItemClickListener){
        this.onItemClickListener=vonItemClickListener;

    }

    public interface OnItemClickListener{
        void onItemClick(aclaracionesResponse aclaracionesResponse);
    }
}
