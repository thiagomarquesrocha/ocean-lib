package com.oceanmanaus.libocean.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by oceanmanaus on 19/08/2016.
 */
public class Teste {

    public static void alerta(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

}
