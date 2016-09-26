package gsevilla.mx.idmovil;


import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;

import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Fragments.fragment_movimientos;
import butterknife.BindView;
import butterknife.ButterKnife;

import models.ModelTags;
import sql.DataSource;

import util.UtilPreference;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


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

    @BindView(R.id.btn_floating)
    FloatingActionButton btn_floating;

    private UtilPreference utilPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        utilPreference = new UtilPreference(this);

        final DataSource dataSource = new DataSource(this);/*inicializando metodo de SqLite*/

        btn_floating.setImageResource(android.R.drawable.ic_input_add);
        btn_floating.setOnClickListener(this);


        /*traer las tags de la BD local*/
        List<ModelTags> tagsLocal = dataSource.getTags();
        ArrayList<String> listaTags = new ArrayList<>();

        for (int i = 0; i <= tagsLocal.size() - 1; i++) { /*llenar el arraylist solo con los datos necesarios*/
            listaTags.add(tagsLocal.get(i).getPrefijo() + tagsLocal.get(i).getNumero());
        }
        /*creando el adaptador y se le envia el arreglo de tags*/
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaTags);
        spinner_tags.setAdapter(adapter);


        /*cuando se seleccione una opcion*/
        spinner_tags.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tarjeta = parent.getItemAtPosition(position).toString();
                Toast.makeText(MainActivity.this, "seleccionado: " + tarjeta, Toast.LENGTH_SHORT).show();

                ModelTags detailTag = dataSource.getTagDetail(tarjeta);

                txt_alias.setText(detailTag.getAlias());
                txtTipoPago.setText(detailTag.getTipoPago() == 1 ? R.string.tipoPago1 : R.string.tipoPago2);

                /*se envia la tarjeta en el bundle por que no hay intents*/
                getFragmentManager().beginTransaction().replace(R.id.fragment_holder, fragment_movimientos.NuevaInstancia(tarjeta)).commit();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                drawerLayout.closeDrawers();

                switch (item.getItemId()) {
                    case R.id.menu_add_tag:
                        //Snackbar.make(findViewById(android.R.id.content),"Agregar nueva TAG",Snackbar.LENGTH_SHORT).show();
                        RegistrarnuevaTag();
                        break;
                    case R.id.menu_salir:
                        LogOut();
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

    private void RegistrarnuevaTag() {
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }

    private void LogOut() {
        new AlertDialog.Builder(this)
                .setTitle("Confirmación")
                .setMessage("¿Estas seguro de cerrar la sesión?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        utilPreference.DeleteLogin();
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        finish();
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setCancelable(false).create().show();
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.btn_floating){
            RegistrarnuevaTag();
        }
    }
}
