package com.example.book_trading.datenbank;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    /**
     * Klassen Attribute
     * BASE_URL ist die Adresse für den MySQL Server
     */
    public static final String BASE_URL = "https://82.165.125.177/";//default von Android
    public static Retrofit retrofit = null;

    /**
     * der APIClient wird erstellt und baut eine Verbindung zu dem Server auf verbindung mittels Selbst signed
     * Zertifikat verschlüsselt
     * @return
     */
    public static Retrofit getApiClient(){
        if(retrofit==null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(getUnsafeOkHttpClient()).addConverterFactory(GsonConverterFactory.create()).build();
            //retrofit= new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }


    /**
     * Methode damit das SSL Zertifikat Akzeptiert wird
     *
     * @return
     */
    public static OkHttpClient getUnsafeOkHttpClient() {

        try {
            // Trust manager überprüft nicht die Chain of Trust
            final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            } };

            // Trust manager wird installiert und akzeptiert alle Zertifikate
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            // SSL Socket wird mit dem Trust manager erstellt
            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient = okHttpClient.newBuilder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}