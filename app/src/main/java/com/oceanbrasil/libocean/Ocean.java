package com.oceanbrasil.libocean;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.oceanbrasil.libocean.control.http.Request;
import com.squareup.picasso.Picasso;

/**
 * Created by oceanbrasil on 19/08/2016.
 */
public class Ocean {

    private static Ocean ourInstance = new Ocean();

    private static Ocean getInstance() {
        return ourInstance;
    }

    private Ocean() {
    }

    /**
     * Criar nova requisao
     * @param urlServer
     * @param callback
     * @return
     */
    public static Request newRequest(String urlServer,Request.RequestListener callback){
        return new Request(urlServer,callback);
    }

    /**
     * Verifica se tem conexao no device
     * @param ctx
     * @return
     */
    public static boolean isConnected(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager)
                ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    /**
     * Tratamento de Imagem com Picasso
     * @param context
     * @return
     */
    public static Picasso picasso(Context context){
        return Picasso.with(context);
    }

}
