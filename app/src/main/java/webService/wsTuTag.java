package webService;




import java.util.List;


import models.Login;
import models.ModelTags;
import models.ValidaPropiedadTag;
import models.ValidationTag;
import models.aclaracionesResponse;
import models.modelAclaracion;
import models.modelCaseta;
import models.modelCruces;
import models.modelMotivos;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


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
                                                 @Query("contrasena") String contrasena, @Query("tipopago") int tipopago);
    //idcliente=string&Alias=string&prefijo=string&numtar=string&celular=string&email=string&contrasena=string

    @GET("ValidaPropiedadTag")
    Call<List<ValidaPropiedadTag>> ValidaPropiedadWithQuery(@Query("numtar") String numtar, @Query("primeros") String primeros, @Query("ultimos") String ultimos);
    //ValidaPropiedadTag?numtar=string&primeros=string&ultimos=string HTTP/1.1

    @GET("GetMovientosTagJsom")
    Call<List<modelCruces>> ObtenerCrucesWithQuery (@Query("tagId") String numtar, @Query("fechaInicio") String finicio, @Query("fechaFin") String ffin);
    //GetMovientosTagJsom?tagId=string&fechaInicio=string&fechaFin=string

    @GET("GeneraAclaracionUP")
    Call<modelAclaracion> GeneraAclaracionWithQuery (@Query("clatran") String clatran, @Query("numtar") String numtar, @Query("tipousr") String TipoUsr, @Query("importe") double importe, @Query("idmotivo") int idmotivo,
                                                     @Query("fecha") String fecha, @Query("hora") String hora, @Query("ncarril") String ncarril, @Query("comentario") String comentario);
    //clatran=string&numtar=string&tipousr=string&importe=string&idmotivo=string&fecha=string&hora=string&ncarril=string&comentario=string

    @GET("ObtenDetalleAclaraciones")
    Call<List<aclaracionesResponse>> GetAclaraciones (@Query("numtar") String numtar);
    //numtar=string

    @GET("DetalleMotivosResponse")
    Call<List<modelMotivos>> ObtenerMotivosAclaraWithQuery();

    @GET("CasetasCircuitoCerrado")
    Call<List<modelCaseta>> ObtenCasetas(@Query("nomcaseta") String nomcaseta);

    @GET("DatosContactoResponse")
    Call<String[]> ObtenerDatosContacto(@Query("idclienteApp") String idclienteApp);

    @GET("EliminaTag")
    Call<Boolean> QuitarTagDeLaCuenta(@Query("numtar") String numtar);

    @GET("CambioDatosResponse")
    Call<Integer> CambiarDatos (@Query("idclienteApp") int idcliente, @Query("NewContrasena") String newContrasena, @Query("NewEmail") String email, @Query("NewTel") String tel);



}
