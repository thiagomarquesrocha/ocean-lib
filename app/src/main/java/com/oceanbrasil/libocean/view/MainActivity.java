package com.oceanbrasil.libocean.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.oceanbrasil.libocean.Ocean;
import com.oceanbrasil.libocean.R;
import com.oceanbrasil.libocean.control.glide.GlideRequest;
import com.oceanbrasil.libocean.control.glide.ImageDelegate;
import com.oceanbrasil.libocean.control.http.Request;

import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements Request.RequestListener, ImageDelegate.BytesListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ocean.newRequest("http://gitlab.oceanmanaus.com/snippets/1/raw",this).get().send();

        //Ocean.isConnected(this);

        //Ocean.picassoWith(this).load("").resize(200,200).centerCrop().into();

        Ocean.glide(this, "https://firebasestorage.googleapis.com/v0/b/ocean-book.appspot.com/o/capas%2F2016-08-23_034518camera.jpg_camera?alt=media&token=f73afa6d-0ac1-47ed-833c-8977c4244e3b")
                .placeHolder(R.mipmap.ic_launcher)
                .build(GlideRequest.BYTES)
                .addDelegateImageBytes(this)
                .toBytes(100,100);


    }

    @Override
    public void onRequestOk(String response, JSONObject jsonObject, int error) {

    }

    @Override
    public void createdImageBytes(byte[] data) {
        Log.i("Ale","data "+data.length);
    }
}
