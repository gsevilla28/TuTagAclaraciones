package gsevilla.mx.idmovil;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import models.ModelTags;
import models.modelAclaracion;
import models.modelCaseta;
import models.modelCruces;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sql.DataSource;
import util.UtilDialog;
import webService.data;
import webService.wsTuTag;


public class ingresa_aclaracion extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.toolbar_main)
    Toolbar toolbar;

    @BindView(R.id.txtnumtar_aclara)
    TextView txtnumtar;
    @BindView(R.id.txtfecha_aclara)
    TextView txtfecha;
    @BindView(R.id.txthora_aclara)
    TextView txthora;
    @BindView(R.id.txtcosto_aclara)
    TextView txtCosto;
    @BindView(R.id.txtcorredor_aclara)
    TextView txtcorredor;
    @BindView(R.id.txtcaseta_aclara)
    TextView txtCaseta;
    @BindView(R.id.txt_Entrada)
    TextView txtEntrada;
    @BindView(R.id.txt_Salida)
    TextView txtSalida;

    @BindView(R.id.spinner_motivos)
    Spinner spinner_motivos;

    @BindView(R.id.imageTicket)
    ImageView imageView;

    @BindView(R.id.btn_camera)
    FloatingActionButton btnCamera;


    private UtilDialog dialog;
    private modelCruces modelCruces;
    private ModelTags tags;


    /*variables para la camara*/
    private String APP_DIRECTORY = "Aclaaci/";
    private String MEDIA_DIRECTORY = APP_DIRECTORY + "media";
    private String TEMPORAL_PICTURE_NAME = "temporal.jpg";

    private final int PHOTO_CODE = 100;
    private final int  SELECT_PICTURE = 200;

    private int iAccion=-1;

    List<modelCaseta> ListaCasetas;
    String[] ArrayCasetas;

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private wsTuTag wsTuTag = data.getRetrofitInstance().create(webService.wsTuTag.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingresa_aclaracion);

        ButterKnife.bind(this);
        DataSource dataSource;

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Aclarar Cruce");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        modelCruces = (models.modelCruces) getIntent().getExtras().getSerializable("cruce");

        /*Llenar el spinner con datos de SQLite*/
        dataSource = new DataSource(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, dataSource.getMotivos());
        spinner_motivos.setAdapter(adapter);
        tags = dataSource.getTagDetail( getIntent().getExtras().getString("numtar"));

        /*agregar un escucha a los Textview para que se modificar los datos*/
        txtEntrada.setOnClickListener(this);
        txtSalida.setOnClickListener(this);

        btnCamera.setImageResource(android.R.drawable.ic_menu_camera);
        btnCamera.setOnClickListener(this);


        /*agregando el Select Listener*/
        spinner_motivos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 1: //DIFERENCIA DE TARIFA, MOTIVO 1
                        btnCamera.setVisibility(View.GONE);
                        imageView.setVisibility(View.GONE);

                        if (modelCruces.getTramo().trim().equals("ARCO NORTE")
                                || modelCruces.getTramo().trim().equals("CORREDOR CHAMAPA - LA VENTA")
                                || modelCruces.getTramo().trim().equals("MEXICALI - TECATE - TIJUANA - ENSENADA")) {

                            txtEntrada.setVisibility(View.VISIBLE);
                            txtSalida.setVisibility(View.VISIBLE);
                            dialog = new UtilDialog(ingresa_aclaracion.this);
                            dialog.GeneraDialogo("CONSULTANDO EL SERVIDOR", "ESPERE POR FAVOR..", false).show();
                            new ObtenerCasetas().execute(modelCruces.getCaseta());
                        }

                        break;
                    case 5: //PAGO EN EFECTIVO, MOTIVO 5
                        txtEntrada.setVisibility(View.GONE);
                        txtSalida.setVisibility(View.GONE);
                        btnCamera.setVisibility(View.VISIBLE);
                        imageView.setVisibility(View.VISIBLE);
                        ElegirFotografia();
                        break;
                    default:
                        imageView.setVisibility(View.GONE);
                        btnCamera.setVisibility(View.GONE);
                        txtEntrada.setVisibility(View.GONE);
                        txtSalida.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        txtnumtar.setText(String.format("Tarjeta: %s", getIntent().getExtras().getString("numtar")) );
        txtfecha.setText(String.format("Fecha: %s", modelCruces.getFecha()));
        txthora.setText(String.format("Hora: %s ",modelCruces.getHora()));
        txtCaseta.setText(String.format("Caseta: %s" , modelCruces.getCaseta()));
        txtCosto.setText(String.format("Costo: $ %.2f", modelCruces.getMonto()));
        txtcorredor.setText(TextUtils.isEmpty(modelCruces.getTramo()) ? modelCruces.getIdFolio() : modelCruces.getTramo());

        if (!modelCruces.getIdFolio().contains("R")) {
            //String leyenda = TextUtils.isEmpty(modelCruces.getIdFolio()) ? "Aclarar" : "Redictaminar";

        }
        else{
            Snackbar.make(findViewById(android.R.id.content), "No es posible realizar otro Redictamen", Snackbar.LENGTH_INDEFINITE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_ing_aclaracion, menu);

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PHOTO_CODE:
                if (resultCode==RESULT_OK){
                    String dir = Environment.getExternalStorageDirectory() + File.separator
                            + MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;
                    decodeBitmap(dir);
                }
                break;
            case SELECT_PICTURE:
                if (resultCode==RESULT_OK){
                    Uri pathUri = data.getData();
                    Log.d("elURI-->",String.valueOf(pathUri));

                    String filePath = "";
                    Log.d("Android SDK-->", String.valueOf(android.os.Build.VERSION.SDK_INT));
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

                        String[] column = { MediaStore.Images.Media.DATA };
                        Cursor  cursor = getApplicationContext().getContentResolver().query(pathUri, column, null, null, null);
                        int columnindex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                        if (cursor.moveToFirst()) {
                            //filePath = cursor.getString(columnIndex);
                            filePath = cursor.getString(columnindex);
                        }
                        cursor.close();

                    }
                    Log.d("REALPATH--> ", filePath);

                    Uri UriFromPath= Uri.fromFile(new File(filePath));
                    BitmapFactory.Options ImagenOriginal = new BitmapFactory.Options();

                    try {
                        BitmapFactory.decodeStream(ingresa_aclaracion.this.getContentResolver().openInputStream(UriFromPath), null, ImagenOriginal);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                     int ancho = ImagenOriginal.outWidth, alto = ImagenOriginal.outHeight;
                     float escalaAncho = (float)(1024.0 / ancho);
                     float escalaAlto = (float)(1024.0 / alto);


                     Matrix matrix = new Matrix();
                     matrix.postScale(escalaAncho,escalaAlto);
                     Bitmap imagen = null;

                     try {
                         imagen = BitmapFactory.decodeStream(ingresa_aclaracion.this.getContentResolver().openInputStream(UriFromPath), null, ImagenOriginal);
                     } catch (FileNotFoundException e) {
                         e.printStackTrace();
                         Log.d("Error","Imagen no Encontrada");
                     }

                     Bitmap ResizeMap = Bitmap.createBitmap(imagen,0,0,ancho,alto, matrix,false);
                     imageView.setImageBitmap(ResizeMap);
                    break;

                }
                break;
        }

    }


    private void decodeBitmap(String dir) {
        Bitmap bitmap;
        bitmap = BitmapFactory.decodeFile(dir);
        //bitmap = BitmapFactory.decodeStream(getApplicationContext().getContentResolver().openInputStream(dir));
        imageView.setImageBitmap(bitmap);

    }

    private void ElegirFotografia() {
        final CharSequence[] options = {"Tomar foto", "Elegir de Galeria", "Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(ingresa_aclaracion.this);
            builder.setTitle("Elige una opcion");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int selection) {
                    iAccion = selection;
                    switch (selection) {
                        case 0: //tomar foto
                            //OpenCamera();
                            verifyStoragePermissions(ingresa_aclaracion.this, iAccion);
                            break;
                        case 1: // Galeria
                            verifyStoragePermissions(ingresa_aclaracion.this, iAccion);
                            break;
                        default:
                            dialog.dismiss();
                    }
                }
            })
              .create().show();


    }

    private void OpenCamera() {

        //verifyStoragePermissions(this);

        File file = new File(Environment.getExternalStorageDirectory(),MEDIA_DIRECTORY);
        file.mkdir(); /*crear directorio donde se va a guardar la imagen*/

        String path = Environment.getExternalStorageDirectory() + File.separator
                + MEDIA_DIRECTORY + File.separator + TEMPORAL_PICTURE_NAME;

        File MediaFile = new File(path);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); /*lanzar intent para abrir la camara*/
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(MediaFile));
        if  (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, PHOTO_CODE);
        }


    }

    private void AbrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"seleciona una opcion"),SELECT_PICTURE);
    }

    private void GeneraFolioAclaracion(){

        dialog = new UtilDialog(ingresa_aclaracion.this);
        dialog.GeneraDialogo("Espere por favor...","Procesando",false).show();

        String clatran = String.valueOf(tags.getIdcliente());
        String numtar = txtnumtar.getText().toString().replace("Tarjeta: ","") + "..";
        String tipousr = tags.getTipopago() == 1 ? "UP" : "UR";
        String importe = txtCosto.getText().toString().replace("Costo: $","");
        String motivo = String.valueOf(spinner_motivos.getSelectedItemPosition());
        String fecha = txtfecha.getText().toString().replace("Fecha: ","");
        String hora = txthora.getText().toString().replace("Hora: ","").trim();
        String ncarril = modelCruces.getCarril();
        String comentario = txtEntrada.getText().toString() + " - " + txtSalida.getText().toString();

        new GeneraAclaracion().execute(clatran,numtar,tipousr,importe,motivo,fecha, hora,ncarril,comentario);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.menu_Aclarar:
                switch (spinner_motivos.getSelectedItemPosition()){
                    case 0:
                        Toast.makeText(ingresa_aclaracion.this,"Es necesario seleccionar el motivo de su aclaracion",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        //if  (txtEntrada.getText().toString().contains("ORIGEN") && txtSalida.getText().toString().contains("DESTINO")) {
                            GeneraFolioAclaracion();
                        //}else{
                         //   Toast.makeText(ingresa_aclaracion.this,"Es necesario seleccionar su recorrido",Toast.LENGTH_SHORT).show();
                        //}
                        break;
                    case 5:
                        if(imageView.getDrawable() != null){
                            GeneraFolioAclaracion();
                        }
                        else{
                            Toast.makeText(ingresa_aclaracion.this,"Es necesario su comprobante de pago",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        GeneraFolioAclaracion();
                        break;
                }
                break;

        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_Entrada:

                AlertDialog.Builder builderEntrada = new AlertDialog.Builder(ingresa_aclaracion.this);
                builderEntrada.setTitle("Caseta de Entrada")
                        .setSingleChoiceItems(ArrayCasetas, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                txtEntrada.setText(String.format("ORIGEN: %s",  ArrayCasetas[which] ));
                                dialog.dismiss();
                            }
                        })
                        .create().show();

                break;
            case R.id.txt_Salida:

                AlertDialog.Builder builderSalida = new AlertDialog.Builder(ingresa_aclaracion.this);
                builderSalida.setTitle("Caseta de Salida")
                        .setSingleChoiceItems(ArrayCasetas, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                txtSalida.setText(String.format("DESTINO: %s",  ArrayCasetas[which] ));
                                dialog.dismiss();
                            }
                        })
                        .create().show();

                break;
            case R.id.btn_camera:
                ElegirFotografia();
                break;

        }
    }

    private void verifyStoragePermissions(Activity activity,int accion) {

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
        else{ /*si ya tiene permisos*/

            if (accion ==0)
                OpenCamera();
            else
                AbrirGaleria();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case REQUEST_EXTERNAL_STORAGE:/*si otorgó los permisos*/
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    if (iAccion ==0)
                        OpenCamera();
                    else
                        AbrirGaleria();
                }
                else{
                    txtEntrada.setText("es necesario dar permisos de lectura a la memoria");
                    txtEntrada.setVisibility(View.VISIBLE);
                }
            break;
        }
    }

    private class GeneraAclaracion extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(final String... params) {

            //wsTuTag wsTuTag = data.getRetrofitInstance().create(webService.wsTuTag.class);
            Call<modelAclaracion> llamadaWS;
            llamadaWS = wsTuTag.GeneraAclaracionWithQuery(params[0],params[1],
                    params[2],Double.parseDouble(params[3]),Integer.parseInt(params[4]),params[5],params[6],params[7],params[8]);

            llamadaWS.enqueue(new Callback<modelAclaracion>() {
                @Override
                public void onResponse(Call<modelAclaracion> call, Response<modelAclaracion> response) {

                    dialog.CerrarDialogo();
                    try {
                        if (!TextUtils.isEmpty(response.body().getComentario())) {

                            Intent intent = new Intent(ingresa_aclaracion.this,MainActivity.class);
                            intent.putExtra("Folio",response.body().getComentario());
                            intent.putExtra("Tarjeta",params[1]);
                            setResult(RESULT_OK,intent);
                            finish();

                        }
                        else{
                            Toast.makeText(ingresa_aclaracion.this,"Folio no Generado",Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch(Exception e) {
                        Toast.makeText(ingresa_aclaracion.this,"Servicio no Disponible",Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<modelAclaracion> call, Throwable t) {
                    dialog.CerrarDialogo();
                    Toast.makeText(ingresa_aclaracion.this,"Sin conexion con el servidor",Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

            return null;
        }
    }


    private class ObtenerCasetas extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... params) {
            Call<List<modelCaseta>> llamadaWS;
            llamadaWS = wsTuTag.ObtenCasetas(params[0]);

            llamadaWS.enqueue(new Callback<List<modelCaseta>>() {
                @Override
                public void onResponse(Call<List<modelCaseta>> call, Response<List<modelCaseta>> response) {
                    ListaCasetas = response.body();
                    ArrayCasetas = new String[ListaCasetas.size()];
                    for (int i=0; i<ListaCasetas.size();i++){
                        ArrayCasetas[i] = ListaCasetas.get(i).getNombreCaseta();
                    }
                    dialog.CerrarDialogo();
                }

                @Override
                public void onFailure(Call<List<modelCaseta>> call, Throwable t) {
                    dialog.CerrarDialogo();
                }
            });

            return null;
        }
    }

}
