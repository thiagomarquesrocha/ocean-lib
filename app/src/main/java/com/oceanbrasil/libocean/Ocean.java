package com.oceanbrasil.libocean;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.oceanbrasil.libocean.control.glide.GlideImage;
import com.oceanbrasil.libocean.control.http.Request;

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
     * Abrir uma imagem recuperando da piscina de objetos a imagem
     * @param photo imagem ( URL, URI ou Recurso )
     * @return requisicao da imagem
     */
    public static GlideImage glide(Context context,Object photo){
        GlideImage image = new GlideImage(context);

        if(photo instanceof String)
            image = (GlideImage) image.load((String) photo);
        else if(photo instanceof Uri)
            image = (GlideImage) image.load((Uri) photo);
        else if(photo instanceof Integer)
            image = (GlideImage) image.load((int) photo);

        return image;
    }

}
