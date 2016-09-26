package webService;

import gsevilla.mx.idmovil.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 08/09/2016.
 */
public class data {

    public static Retrofit getRetrofitInstance(){

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder okhttpClient = new OkHttpClient.Builder();
        okhttpClient.addInterceptor(httpLoggingInterceptor);

        return new Retrofit.Builder().baseUrl(BuildConfig.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClient.build())
                .build();

    }
}
