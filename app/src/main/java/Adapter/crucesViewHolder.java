package Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import gsevilla.mx.idmovil.R;

/**
 * Created by Administrator on 20/09/2016.
 */
public class crucesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.txt_corredor_recycler)
    TextView txtCorredor;

    @BindView(R.id.txt_caseta_recycler)
    TextView txtCaseta;

    @BindView(R.id.txt_fecha_recycler)
    TextView txtFecha;

    @BindView(R.id.txt_costo_recycler)
    TextView txtCosto;

    /*en esta clase se definen los campos que se van a mostrar*/
    public crucesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}
