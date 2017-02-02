package gsevilla.mx.idmovil;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Contacto extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.ic_twitter)
    ImageView imgTwitter;
    @BindView(R.id.ic_facebook)
    ImageView imgFacebook;
    @BindView(R.id.txtTelContacto)
    TextView txtTelContacto;
    @BindView(R.id.txtMailContacto)
    TextView txtMailContacto;
    @BindView(R.id.txtWebContacto)
    TextView txtWebContacto;
    @BindView(R.id.toolbar_main)
    Toolbar toolbar;

    private final int REQUEST_PHONE_CALL = 1;

    private static String[] PERMISSIONS_PHONE = {
            Manifest.permission.CALL_PHONE,
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.contacto_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Medios de contacto");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        imgTwitter.setOnClickListener(this);
        imgFacebook.setOnClickListener(this);
        txtTelContacto.setOnClickListener(this);
        txtMailContacto.setOnClickListener(this);
        txtWebContacto.setOnClickListener(this);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ic_twitter:
                lanzarApp("T");
                break;
            case R.id.ic_facebook:
                lanzarApp("F");
                break;
            case R.id.txtTelContacto:
                verifyCallPhonePermissions(this);
                break;
            case R.id.txtMailContacto:
                launchSendMail();
                break;
            case R.id.txtWebContacto:
                launchBrowser();
                break;
        }
    }

    private void launchCallPhone() {


    }

    private void launchSendMail() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(String.format("mailto: %s", txtMailContacto.getText().toString())));
        intent.putExtra(intent.EXTRA_SUBJECT,"Solicito de su asistencia");
        startActivity(Intent.createChooser(intent,"Seleccione Aplicacion"));
    }

    private void launchBrowser() {
        String link = String.format("http://%s", txtWebContacto.getText().toString());

        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(link)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void lanzarApp(String param) {

        String packageApp = "", idApp="", wpagina="";

        if (param.equals("F")){
            packageApp = getResources().getString(R.string.packageFeis);
            idApp = getResources().getString(R.string.idfacebookApp);
            wpagina = getResources().getString(R.string.wwwFace);
        }
        else if (param.equals("T")){
            packageApp = getResources().getString(R.string.packagetwi);
            idApp = getResources().getString(R.string.idTwitterApp);
            wpagina = getResources().getString(R.string.wwwTwitter);
        }

        if (isAppInstalled(Contacto.this,packageApp)){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(idApp)));
            //fb://page/?id=182861581806654
        }
        else{ /*si no hay feis instalado entonces lanza el navegador*/
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(wpagina)));
        }
    }
    /*verifica si alguna app se encuentra instalada*/
    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void verifyCallPhonePermissions(Activity activity) {

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);


        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_PHONE, REQUEST_PHONE_CALL);
        }
        else{ /*si ya tiene permisos*/
            startActivity(new Intent(Intent.ACTION_CALL,Uri.parse(String.format("tel: %s",txtTelContacto.getText().toString()))));

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case REQUEST_PHONE_CALL:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startActivity(new Intent(Intent.ACTION_CALL,Uri.parse(String.format("tel: %s",txtTelContacto.getText().toString()))));
                }
                break;

        }
    }
}
