package com.ocean.lib.control.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.ocean.lib.R;

/**
 * Created by oceanmanaus on 23/08/2016.
 */
public class GlideImage {

    private CircleTransform circleTransform;
    private Context context; // Contexto
    private String url; // URL da imagem
    private Uri uri; // Uri da imagem
    private Integer resource; // Imagem em forma de recurso
    private Integer placeHolder; // Recurso que substitui caso nao tenha imagem
    private BitmapRequestBuilder builder; // Requisicao construtora da imagem
    private ImageView imageView; // Lista de views de imagem
    private ImageDelegate.BytesListener bytesDelegate; // lista de callback de bytes de imagens
    private ImageDelegate.BitmapListener bitmapDelegate; // lista de callback de imagens
    private RequestManager requestManager; // Request Manager

    public GlideImage(Context context) {
        this.context = context;
        this.requestManager = Glide.with(context);
        url = null;
        uri = null;
        resource = 0;
        placeHolder = R.mipmap.ic_placeholder;
        builder = null;
        imageView = null;
        bytesDelegate = null;
        bitmapDelegate = null;
        circleTransform = new CircleTransform(context);
    }

    public GlideImage load(String url) {
        this.url = url;
        return this;
    }

    public GlideImage load(Uri uri) {
        this.uri = uri;
        return this;
    }

    public GlideImage load(int resource) {
        this.resource = resource;
        return this;
    }

    public GlideImage placeHolder(int placeHolder) {
        this.placeHolder = placeHolder;
        return this;
    }

    public Uri parseToUri(){
        if(context == null) throw new IllegalArgumentException("O context nao pode ser nulo");
        if(requestManager == null) throw new IllegalArgumentException("O requestManager nao pode ser nulo");
        return GlideRequest.toURI(this);
    }

    public GlideImage build(int type){
        if(context == null) throw new IllegalArgumentException("O context nao pode ser nulo");
        if(requestManager == null) throw new IllegalArgumentException("O requestManager nao pode ser nulo");
        BitmapTypeRequest request = requestManager.load(GlideRequest.toURI(this)).asBitmap();

        // Configura a requisicao da imagem para RGB
        builder = request.format(DecodeFormat.PREFER_RGB_565);

        // Transforma em bytes a saida
        switch (type){
            case GlideRequest.BYTES :
                builder = request.toBytes(Bitmap.CompressFormat.JPEG, GlideBuilder.QUALITY);
                break;
        }
        return this;
    }

    public GlideImage resize(int width, int height){
        if(width == 0 && height == 0) throw new IllegalArgumentException("Width e Height nao podem ser 0");
        if(builder == null) throw new IllegalArgumentException("Falta iniciar build");
        builder = builder.override(width, height).centerCrop();
        return this;
    }

    public GlideImage into(ImageView imageView) {
        this.imageView = imageView;
        GlideBuilder.bitmapAndImageView(this);
        return this;
    }

    public GlideImage cache(long time){
        if(builder == null) throw new IllegalArgumentException("Falta iniciar o objeto de build");
        // Recupera o objeto do cache caso ele ja tenha sido criado
        CacheGlide cache = new CacheGlide(MediaStoreSignature.STRING_CHARSET_NAME, time, GlideBuilder.PORTRAIT);
        // Cria o cache da imagem
        builder = builder.signature(cache);
        return this;
    }

    public GlideImage circle(){
        if(builder == null) throw new IllegalArgumentException("Precisa ter uma requisicao para utilizar o crop em circulo");
        builder = builder.transform(circleTransform);
        return this;
    }

    public GlideImage toBitmap(){
        GlideBuilder.toBitmap(this);
        return this;
    }

    public GlideImage toBytes(int width, int height){
        GlideBuilder.toBytes(this, width, height);
        return this;
    }

    public GlideImage addDelegateImageBitmap(ImageDelegate.BitmapListener delegate) {
        bitmapDelegate = delegate;
        return this;
    }

    public GlideImage addDelegateImageBytes(ImageDelegate.BytesListener delegate) {
        bytesDelegate = delegate;
        return this;
    }

    public void setCircleTransform(CircleTransform circleTransform) {
        this.circleTransform = circleTransform;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setBuilder(BitmapRequestBuilder builder) {
        this.builder = builder;
    }

    public BitmapRequestBuilder getBuilder() {
        return builder;
    }

    public ImageDelegate.BitmapListener getBitmapDelegate() {
        return bitmapDelegate;
    }

    public ImageDelegate.BytesListener getBytesDelegate() {
        return bytesDelegate;
    }

    public Uri getUri() {
        return uri;
    }

    public String getUrl() {
        return url;
    }

    public Context getContext() {
        return context;
    }

    public Integer getPlaceHolder() {
        return placeHolder;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public Integer getResource() {
        return resource;
    }

    public ImageView getImageView() {
        return imageView;
    }
}