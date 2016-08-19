package com.oceanmanaus.libocean.control.http;

import android.os.AsyncTask;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by oceanmanaus on 19/08/2016.
 */
public class Request {

    // Servidor
    private String URL = "URL DO SERVIDOR";

    /**
     * Retornos de possiveis erros
     */
    public static final int NENHUM_ERROR = 0; // HttpException
    public static final int ERROR_CONEXAO = 1; // HttpException
    public static final int ERROR_INESPERADO = 2; // RunntimeException, NullpointerException
    public static final int ERROR_SINTAXE_JSON = 3; // JSONException

    public static final int POST = 1;
    public static final int GET = 2;
    // Timeout de resposta do servidor
    protected int TIMEOUT = 20000;
    // Tipo de requisicao que sera executada
    private int request;
    // Execeção lancada caso ocorra algum erro
    private Exception exception;
    // Tipo de erro ocorrido
    private int error;
    // Response Handle da requisicao
    private HttpCallback.PostJSONListener callbackJSON;
    // Api client para conexao

    private final Map<String, Object> data;
    private String response;

    private RequestListener callback;

    public Request(String URL,RequestListener callback) {
        this.URL = URL;
        data = new HashMap<>();
        this.callback = callback;
    }

    public Request(String URL, int timeout) {
        this.URL = URL;
        this.TIMEOUT = timeout;
        data = new HashMap<>();
    }




    /**
     * Ativa um callback para a conexao informando se a requisicao foi bem sucediada ou não
     * @param callbackJSON referencia do callback
     * @return instancia de Request
     */
    private Request setCallbackJSON(HttpCallback.PostJSONListener callbackJSON) {
        this.callbackJSON = callbackJSON;
        return this;
    }

    public Request get() {
        this.request = GET;
        return this;
    }

    public Request post() {
        this.request = POST;
        return this;
    }

    public final int getRequest() {
        return request;
    }

    public JSONObject send(){

        Log.i("info","#### EXECUTANDO UMA REQUISICAO HTTP ####");

        JSONObject json = null;
        exception = null;
        error = NENHUM_ERROR;

        try {
            // Recupera a localizacao ( latitude, longitude )
            //getLocalizacao();

            print(); // Imprimir todos parametros da requisição

            response = "";

            new AsyncTask<Void,Void,JSONObject>(){

                @Override
                protected JSONObject doInBackground(Void... voids) {

                    try {
                        switch (request){
                            case GET:  // Se o método é GET
                                requestGet();
                                break;
                            case POST :  // Se o método é POST
                                requestPost();
                                break;
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    Log.i("info","[RESPONSE] : " + response);
                    JSONObject json = null;
                    try {
                        if (!response.isEmpty()) {
                            json = new JSONObject(response);
                        } else {
                            json = null;
                        }

                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    return json;
                }

                @Override
                protected void onPostExecute(JSONObject jsonObject) {
                    super.onPostExecute(jsonObject);
                    if (callback != null){
                        callback.onRequestOk(getResponse(),jsonObject,getError());
                    }
                }
            }.execute();


        }catch (Exception e){
            exception = e;

            if(error == NENHUM_ERROR){
                if(e instanceof JSONException)
                    error = ERROR_SINTAXE_JSON;
                else
                    error = ERROR_INESPERADO;
            }

            e.printStackTrace();
            json = null;
        }

        return json;
    }

    private void requestGet() throws Exception{
        HttpRequest request = HttpRequest.get(getUrl(), data, true).connectTimeout(TIMEOUT);

        Log.i("info","#### Enviando via GET #### \n [URL] " + URL);

        if (!request.ok()) {
            error = ERROR_CONEXAO;
            throw new UnsupportedOperationException("Página não encontrada");
        }

        response = request.body();
    }

    private void requestPost() throws Exception{
        HttpClient client = new HttpClient(getUrl());
        client.connectForMultipart();
        //Log.d(Constante.LOG, "[parametros] " + data.toString());
        // Adiciona os parametros na conexao POST
        for (Map.Entry<String, Object> entrada : data.entrySet()) {
            //Log.d(Constante.LOG, "[VALORES] " + entrada.toString());

            if (entrada.getValue() instanceof HashMap) { // Se é arquivo ( byte ) Map<String, Object> => key, value ( Map<String, Object> )
                Map<String, Object> arquivos = (Map<String, Object>) entrada.getValue();
                for (Map.Entry<String, Object> arquivo : arquivos.entrySet()) {
                    String nomeImagem = arquivo.getKey();
                    byte[] byteImage_photo = (byte[]) arquivo.getValue();
                    client.addFilePart(entrada.getKey(), nomeImagem, byteImage_photo);
                }
            } else {
                if (entrada.getValue() != null) {
                    client.addFormPart(entrada.getKey(), entrada.getValue().toString());
                    // Log.i(Constante.INFO, " Adicionando : " + entrada.getKey() + " : " + entrada.getValue().toString());
                }
            }
        }

        Log.i("info","#### Enviando via POST #### \n" +
                " [URL] " + URL);

        client.finishMultipart();
        response = client.getResponse();
        boolean ok = client.OK();

        if (!ok) {
            error = ERROR_CONEXAO;
            throw new UnsupportedOperationException("Página não encontrada");
        }
    }

    public final Request remove(String key) {
        if(data.containsKey(key))
            data.remove(key);
        return this;
    }

    public Exception getException() {
        return exception;
    }

    public String getResponse() {
        return response;
    }

    public final Request add(String key, Object valor) {
        data.put(key, valor);
        return this;
    }

    public final Request print()
    {
        for(Map.Entry<String, Object> entrada : this.data.entrySet())
        {
            if(entrada.getKey() != null && entrada.getValue() != null)
                Log.d("debug",entrada.getKey() + ":" + entrada.getValue().toString());
            else
                Log.d("debug","O valor de " + entrada.getKey() + ": não existe");
        }
        return this;
    }

    public final Request clear(){
        data.clear();
        return this;
    }

    public int getError() {
        return error;
    }

    public final String getUrl() {
        return URL;
    }

    public interface RequestListener{
        void onRequestOk(String response, JSONObject jsonObject, int error);
    }



}
