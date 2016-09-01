package com.oceanbrasil.libocean.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.oceanbrasil.libocean.Ocean;
import com.oceanbrasil.libocean.R;
import com.oceanbrasil.libocean.control.glide.GlideRequest;
import com.oceanbrasil.libocean.control.glide.ImageDelegate;
import com.oceanbrasil.libocean.control.http.Request;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity implements Request.RequestListener, ImageDelegate.BytesListener, ImageDelegate.BitmapListener {

    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        Ocean.glide(this)
                .load("http://novatec.com.br/figuras/capas/9788575223505.gif")
                .build(GlideRequest.BYTES)
                .addDelegateImageBytes(this)
                .toBytes(150, 150);

        //Ocean.newRequest("http://gitlab.oceanmanaus.com/snippets/1/raw",this).get().send();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_placeholder);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitMapData = stream.toByteArray();

        HashMap<String, byte[]> file = new HashMap<>();
        file.put("file.png", bitMapData);

        Log.d("Image", bitMapData.length + "");

        Ocean.newRequest("http://10.0.2.2/teste", this).get().add("teste", 1).send();


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
