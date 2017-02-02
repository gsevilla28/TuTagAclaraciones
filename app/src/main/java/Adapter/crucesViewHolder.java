package Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gsevilla.mx.idmovil.R;
import models.ModelTags;
import models.modelCruces;

/**
 * Created by Administrator on 20/09/2016.
 */
public class crucesViewHolder extends RecyclerView.ViewHolder  {

    @BindView(R.id.txt_corredor_recycler)
    TextView txtCorredor;

    @BindView(R.id.txt_caseta_recycler)
    TextView txtCaseta;

    @BindView(R.id.txt_fecha_recycler)
    TextView txtFecha;

    @BindView(R.id.txt_costo_recycler)
    TextView txtCosto;

    @BindView(R.id.img_cruce)
    ImageView iconocruce;

    private modelCruces modelCruces;

    private  crucesAdapter.OnItemClickListener onItemClickListener;

    /*en esta clase se definen los campos que se van a mostrar*/
    public crucesViewHolder(final View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);


        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                itemView.setLongClickable(true);

                Toast.makeText(v.getContext(), "Position is " + modelCruces.getTramo(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }


    public void setItemClick (modelCruces modelCruces,crucesAdapter.OnItemClickListener onItemClickListener ){
        this.modelCruces=modelCruces;
        this.onItemClickListener=onItemClickListener;
    }

    @OnClick(R.id.img_cruce)
    public void onViewClick(View view){
        if (onItemClickListener !=null)
            onItemClickListener.onItemClick(modelCruces);
    }


}
