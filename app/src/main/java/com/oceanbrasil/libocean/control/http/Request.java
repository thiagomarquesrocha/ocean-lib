package com.oceanbrasil.libocean.control.http;

import android.os.AsyncTask;
import android.util.Log;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by oceanbrasil on 19/08/2016.
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
    private Map<String, String> headers;
    private Map<String, String> resultHeaders;

    private RequestListener callback;

    public Request(String URL,RequestListener callback) {
        this.URL = URL;
        data = new HashMap<>();
        resultHeaders = new HashMap<>();
        headers = new HashMap<>();
        this.callback = callback;
    }

    public Request(String URL, RequestListener callback, int timeout) {
        this.URL = URL;
        this.callback = callback;
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

    public final Request resultFieldHeader(String... name){
        for(int i=0; i<name.length; i++){
            resultHeaders.put(name[i], "");
        }
        return this;
    }

    public Request header(String key, String value){
        headers.put(key, value);
        return this;
    }

    public Request send(){

        Log.i("info","#### EXECUTANDO UMA REQUISICAO HTTP ####");

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

                    JSONObject json = new JSONObject();
                    try {
                        Log.i("info","Request : " + request);
                        switch (request){
                            case GET:  // Se o método é GET
                                requestGet();
                                break;
                            case POST :  // Se o método é POST
                                requestPost();
                                break;
                        }

                        json.put("resultFieldHeader", getHeaderJSON());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    Log.i("info","[RESPONSE] : " + response);
                    try {
                        if (!response.isEmpty()) {
                            json.put("data", new JSONObject(response));
                        } else {
                            json.put("data", new JSONObject());
                        }
                    }catch (JSONException e){
                        //e.printStackTrace();
                        try {
                            if (!response.isEmpty()) {
                                JSONArray array = new JSONArray(response);
                                json.put("data", array);
                            } else {
                                json.put("data", new JSONObject());
                            }
                        } catch (JSONException e1) {
                            //e1.printStackTrace();
                        }
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
        }

        return this;
    }

    private void requestGet() throws Exception{
        HttpRequest request = HttpRequest.get(getUrl(), data, true);

        if(!headers.isEmpty()){
            for(Map.Entry<String, String> row : headers.entrySet()){
                request.header(row.getKey(), row.getValue());
            }
        }

        request.connectTimeout(TIMEOUT);

        Log.i("info","#### Enviando via GET #### \n [URL] " + URL);

        if (!request.ok()) {
            error = ERROR_CONEXAO;
            Log.i("info", request.body());
            throw new UnsupportedOperationException("Página não encontrada");
        }

        //Log.d("Header", request.resultFieldHeader("keep-alive"));

        if(!resultHeaders.isEmpty()){
            for(Map.Entry<String, String> row : resultHeaders.entrySet()){
                resultHeaders.put(row.getKey(), request.header(row.getKey()));
            }
        }

        response = request.body();
    }

    private void requestPost() throws Exception{
        HttpClient client = new HttpClient(getUrl());
        client.connectForMultipart(headers);

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
                    //Log.i("Info", " Adicionando : " + entrada.getKey() + " : " + entrada.getValue().toString());
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

        if(!resultHeaders.isEmpty()){
            for(Map.Entry<String, String> row : resultHeaders.entrySet()){
                resultHeaders.put(row.getKey(), client.getHeader(row.getKey()));
            }
        }
    }

    public final Request remove(String key) {
        if(data.containsKey(key))
            data.remove(key);
        return this;
    }

    public JSONObject getHeaderJSON() throws Exception{
        JSONObject json = new JSONObject();
        if(!resultHeaders.isEmpty()){
            for(Map.Entry<String, String> row : resultHeaders.entrySet()){
                json.put(row.getKey(), row.getValue());
            }
        }
        return json;
    }

    private String getHeader(String name){
        return resultHeaders.get(name);
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
