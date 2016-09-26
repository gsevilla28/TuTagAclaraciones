package webService;



import java.util.ArrayList;
import java.util.List;


import models.Login;
import models.ModelTags;
import models.ValidaPropiedadTag;
import models.ValidationTag;
import models.modelCruces;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 08/09/2016.
 */
public interface wsTuTag {


    @GET("DoLogin")
    Call<Login>  getLoginWithQuery(@Query("usuario") String usuario, @Query("contrasena") String pwd);
    ///DoLogin?usuario=string&contrasena=string


    @GET("ValidationTag")
    Call<List<ValidationTag>> getValidaTagWithQuery(@Query("numtar") String numtar);
    ///ValidationTag?numtar=string

    @GET("RegisterNew")
    Call<List<ModelTags>> RegistrarTagWithquery (@Query("idcliente") int idcliente, @Query("Alias") String Alias, @Query("prefijo") String prefijo,
                                                 @Query("numtar") String numtar, @Query("celular") String celular, @Query("email") String email,
                                                 @Query("contrasena") String contrasena);
    //idcliente=string&Alias=string&prefijo=string&numtar=string&celular=string&email=string&contrasena=string

    @GET("ValidaPropiedadTag")
    Call<List<ValidaPropiedadTag>> ValidaPropiedadWithQuery(@Query("numtar") String numtar, @Query("primeros") String primeros, @Query("ultimos") String ultimos);
    //ValidaPropiedadTag?numtar=string&primeros=string&ultimos=string HTTP/1.1

    @GET("GetMovientosTagJsom")
    Call<List<modelCruces>> ObtenerCrucesWithQuery (@Query("tagId") String numtar, @Query("fechaInicio") String finicio, @Query("fechaFin") String ffin);
    //GetMovientosTagJsom?tagId=string&fechaInicio=string&fechaFin=string

}
