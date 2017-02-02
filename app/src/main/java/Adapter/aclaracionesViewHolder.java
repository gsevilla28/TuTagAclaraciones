package Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gsevilla.mx.idmovil.R;
import models.aclaracionesResponse;

/**
 * Created by Administrator on 02/11/2016.
 */

public class aclaracionesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.txtidfolio_detalleAcla)
    TextView txtidfolio;

    @BindView(R.id.txtFechaSolicitud_detalleAcla)
    TextView txtFechaSolicitud;

    @BindView(R.id.txtEstatus_detalleAcla)
    TextView txtEstatus;

    @BindView(R.id.txtImporteD_detalleAcla)
    TextView txtImporteD;

    @BindView(R.id.imageView_detalleAcla)
    ImageView imgDetail;

    private aclaracionesResponse aclaracionesResponse;

    private aclaracionesAdapter.OnItemClickListener onItemClickListener;



    public aclaracionesViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this,itemView);


    }

    public void setItemClick(aclaracionesResponse vaclaracionesResponse,aclaracionesAdapter.OnItemClickListener vonItemClickListener) {
        this.onItemClickListener = vonItemClickListener;
        this.aclaracionesResponse = vaclaracionesResponse;
    }

    @OnClick(R.id.imageView_detalleAcla)
    public void onViewClick(View view){
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(aclaracionesResponse);
    }



}
