package com.ocean.lib.control.glide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import com.bumptech.glide.load.resource.transcode.BitmapBytesTranscoder;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;

/**
 * Created by oceanmanaus on 23/08/2016.
 */
class GlideBuilder {

    public static final int PORTRAIT = 1;
    // Reduz em 80% a qualidade da imagem
    public static final int QUALITY = 80;

    public static void bitmapAndImageView(final GlideImage glide){
        if(glide.getBuilder() == null) throw new IllegalArgumentException("Falta iniciar o build");

        BitmapImageViewTarget bitmapImageViewTarget = new BitmapImageViewTarget(glide.getImageView()){
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                super.onResourceReady(resource, glideAnimation);
                if(glide.getBitmapDelegate() != null){
                        Log.d("Glide", "Imagem recebida (bitmap) : " + resource.getByteCount());
                    glide.getBitmapDelegate().createdImageBitmap(resource);
                }

            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                Log.e("Glide", "Ocorreu um erro ao abrir o bitmap da imagem");
                if(e != null)
                    e.printStackTrace();
            }
        };

        // Carrega a imagem na view
        glide.getBuilder().into(bitmapImageViewTarget);
    }

    public static void toBitmap(final GlideImage glide){
        if(glide.getBuilder() == null) throw new IllegalArgumentException("Falta iniciar o build");


        RequestListener requestListener = new RequestListener<Uri, Bitmap>() {
            @Override
            public boolean onException(Exception e, Uri model, Target<Bitmap> target, boolean isFirstResource) {
                if (glide.getBitmapDelegate() != null) {
                    Log.e("Glide","Ocorreu um erro ao processar a imagem ");
                    glide.getBitmapDelegate().createdImageBitmap(null);
                }
                return true;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Uri model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                if (glide.getBitmapDelegate() != null) {
                        Log.d("Glide","Imagem recebida (bitmap) : " + resource.getByteCount());
                    glide.getBitmapDelegate().createdImageBitmap(null);
                }

                return false;
            }
        };

        glide.setBuilder(glide.getBuilder().listener(requestListener));
    }

    public static void toBytes(final GlideImage glide, int width, int height){
        if(width == 0 && height == 0) throw new IllegalArgumentException("Width e Height nao podem ser 0");

        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(glide.getContext().getContentResolver().openInputStream(glide.getUri()), null, options);

            int w = options.outWidth;
            int h = options.outHeight;

            if(w < width || h < width){
                int min = Math.min(w, h);
                if(min > w)
                    min = w;
                width = min;
                height = min;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        SimpleTarget target = new SimpleTarget<byte[]>(width, height) {

            @Override
            public void onResourceReady(byte[] resource, GlideAnimation<? super byte[]> glideAnimation) {
                if (resource != null) {
                    if (glide.getBytesDelegate() != null) {
                            Log.d("Glide","Imagem recebida (bytes) : " + resource.length);
                        glide.getBytesDelegate().createdImageBytes(resource);
                    }
                }

            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                Log.e("Glide","Ocorreu um erro ao carregar os bytes da imagem ");
            }
        };

        // Carrega os bytes da imagem dentro do target
        glide.getBuilder()
                .transcoder(new BitmapBytesTranscoder(Bitmap.CompressFormat.JPEG, QUALITY))
                .into(target);
    }

}
