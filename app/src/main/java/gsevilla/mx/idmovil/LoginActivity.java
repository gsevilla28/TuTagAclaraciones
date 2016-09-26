package gsevilla.mx.idmovil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import models.Login;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.UtilPreference;
import webService.data;
import webService.wsTuTag;



/**
 * Created by Administrator on 06/09/2016.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.txt_usuario_login)
    EditText txtusuario;
    @BindView(R.id.txt_pwd_login)
    EditText txtpwd;

    private ProgressDialog progressDialog ;
    private UtilPreference utilPreference;

    private String usuario="";
    private String pwd="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); /*indica que no debe girar la pantalla*/

        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.btnRegistrase).setOnClickListener(this);

        utilPreference = new UtilPreference(LoginActivity.this);
        int LoginSuccess = utilPreference.GetLogin();

        if (LoginSuccess !=0){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnRegistrase:
                MakeRegister();
                break;
            case R.id.btnLogin:
                DoLogin();
                break;
        }

    }

    private void MakeRegister() {
        //Toast.makeText(LoginActivity.this, "Realizar Registro", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

    }

    private void DoLogin() {

            try {
                if (!TextUtils.isEmpty(txtpwd.getText()) && !TextUtils.isEmpty(txtusuario.getText())) { /*validar nulos*/

                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setTitle("Cargando");
                    progressDialog.setMessage("Validando datos...");
                    progressDialog.show();

                    usuario = txtusuario.getText().toString();
                    pwd = txtpwd.getText().toString();

                    new LoginTask().execute(usuario, pwd); /*ejecuta proceso en 2do plano*/

                } else {
                    Toast.makeText(LoginActivity.this, "ningun campo debe estar vacio", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(LoginActivity.this, "Servicio no Disponible", Toast.LENGTH_SHORT).show();

            }
    }

    private void CerrarDialogo() {
        if (LoginActivity.this.progressDialog != null) {
            LoginActivity.this.progressDialog.dismiss();

        }
    }

    private class LoginTask extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... args) {

            //instanciando WS
            wsTuTag wsTuTag = data.getRetrofitInstance().create(webService.wsTuTag.class);
             //ejecutando web service
            Call<Login> callService = wsTuTag.getLoginWithQuery(args[0].toString(), args[1].toString());

            callService.enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {

                    if (response.body().getLogin() == 1) { //aqui deberia consultar al WS
                        //Snackbar.make(findViewById(android.R.id.content), "LOGIN CON WS CORRECTO! :" + response.body().getUsuario(), Snackbar.LENGTH_INDEFINITE).show();


                        CerrarDialogo(); //cierra el bloqueo
                        /*si el log in es correcto se almacena una bandera en shared preferences*/
                        utilPreference.SaveLogin(1);

                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();

                    } else {
                        CerrarDialogo(); //cierra el bloqueo
                        Snackbar.make(findViewById(android.R.id.content), "ERROR DE  Login", Snackbar.LENGTH_INDEFINITE).show();

                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    CerrarDialogo(); //cierra el bloqueo
                    Toast.makeText(LoginActivity.this, "Servicio no Disponible", Toast.LENGTH_SHORT).show();

                    /*solo para efecto de navegacion, quitar esta parte del codigo
                    utilPreference.SaveLogin(1);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();*/

                }
            });

            return  "";
        }

    }


}
