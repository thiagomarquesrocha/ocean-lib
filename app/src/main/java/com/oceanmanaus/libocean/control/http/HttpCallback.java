package com.oceanmanaus.libocean.control.http;

/**
 * Created by oceanmanaus on 19/08/2016.
 */
public class HttpCallback {
    public interface PostJSONListener{
        void onSuccess(int statusCode, String response, byte[] responseBody);
        void onFailure(int statusCode,  String response, byte[] responseBody, Throwable error);
    }
}
