package com.oceanbrasil.libocean.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.oceanbrasil.libocean.R;
import com.oceanbrasil.libocean.control.http.Request;

import org.json.JSONObject;


class MainActivity extends AppCompatActivity implements Request.RequestListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Ocean.newRequest("http://gitlab.oceanmanaus.com/snippets/1/raw",this).get().send();

        //Ocean.isConnected(this);

        //Ocean.picassoWith(this).load("").resize(200,200).centerCrop().into();


    }

    @Override
    public void onRequestOk(String response, JSONObject jsonObject, int error) {

    }
}
