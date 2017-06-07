package com.ocean.lib.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.ocean.lib.Ocean;
import com.ocean.lib.R;
import com.ocean.lib.control.glide.GlideRequest;
import com.ocean.lib.control.glide.ImageDelegate;
import com.ocean.lib.control.http.Request;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;


class LibOceanMainActivity extends AppCompatActivity implements Request.RequestListener, ImageDelegate.BytesListener, ImageDelegate.BitmapListener {

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Ocean.newRequest("http://gitlab.oceanmanaus.com/snippets/1/raw",this).get().send();

        //Ocean.isConnected(this);

        //Ocean.picassoWith(this).load("").resize(200,200).centerCrop().into();

        /**
         *
         * Bitmap exemplo
         *
         * Ocean.glide(this)
             .load("http://novatec.com.br/figuras/capas/9788575223505.gif")
             .build(GlideRequest.BITMAP)
             .addDelegateImageBitmap(this)
             .resize(200, 200)
             .into(ImageView);

           Bytes exemplo

             Ocean.glide(this)
             .load("http://novatec.com.br/figuras/capas/9788575223505.gif")
             .build(GlideRequest.BYTES)
             .addDelegateImageBytes(this)
             .toBytes(150, 150);

         *
         */


        image = (ImageView) findViewById(R.id.image);

//        Ocean.glide(this)
//                .load("http://novatec.com.br/figuras/capas/9788575223505.gif")
//                .build(GlideRequest.BYTES)
//                .addDelegateImageBytes(this)
//                .toBytes(150, 150);

        //Ocean.newRequest("http://gitlab.oceanmanaus.com/snippets/1/raw",this).get().send();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_placeholder);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitMapData = stream.toByteArray();

        HashMap<String, byte[]> file = new HashMap<>();
        file.put("file.png", bitMapData);

        Log.d("Image", bitMapData.length + "");

        // http://10.0.2.2/teste/index.php
        Ocean.newRequest("http://mobile-aceite.tcu.gov.br:80/appCivicoRS/rest/pessoas/autenticar", new Request.RequestListener() {
            @Override
            public void onRequestOk(String response, JSONObject jsonObject, int error) {
                Log.d("JSON", jsonObject.toString());

            }
        })
        .get()
         .header("email", "thiago@gmail.com")
         .header("googleToken", "eyJhbGciOiJSUzI1NiIsImtpZCI6ImQ1MjA4ODBiNDYzNGE1YTNjNDFiNWNmNjU1M2U5ZWE0YTViNjA5ZjIifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJpYXQiOjE0Nzg3MTc3ODEsImV4cCI6MTQ3ODcyMTM4MSwiYXVkIjoiNjYwMTMzMzAxNDYzLWRna2hncmJqbGQxZW43YjQ4bzZ0cjA5N3Vtb2NnNnIwLm")
        .resultFieldHeader("appToken")
        .resultFieldHeader("server")
//        .add("teste", 1)
//        .add("file", file)
        .send();

        /*Ocean.glide(this)
                .load(R.mipmap.slider_1)
                .build(GlideRequest.BITMAP)
                .resize(120, 120) // Tamanho em pixel
                .circle()
                .into(image);
        */

        try {
            //doHttp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestOk(String response, JSONObject jsonObject, int error) {

    }

    @Override
    public void createdImageBytes(byte[] data) {
        Log.i("Ale","data "+data.length);
        Bitmap bitmap = Ocean.byteToBitmap(data);
        image.setImageBitmap(bitmap);
    }

    @Override
    public void createdImageBitmap(Bitmap imageBitmap) {
        Log.i("Ale","Bitmap criado " + imageBitmap.getHeight());
    }
}
