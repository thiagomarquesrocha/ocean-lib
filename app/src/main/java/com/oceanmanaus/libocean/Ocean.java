package com.oceanmanaus.libocean;

import com.oceanmanaus.libocean.control.http.Request;

/**
 * Created by oceanmanaus on 19/08/2016.
 */
public class Ocean {

    private static Ocean ourInstance = new Ocean();

    private static Ocean getInstance() {
        return ourInstance;
    }

    private Ocean() {
    }

    public static Request newRequest(String urlServer,Request.RequestListener callback){
        return new Request(urlServer,callback);
    }

}
