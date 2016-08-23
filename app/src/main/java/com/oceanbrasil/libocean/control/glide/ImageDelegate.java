package com.oceanbrasil.libocean.control.glide;

import android.graphics.Bitmap;

/**
 * Created by oceanmanaus on 23/08/2016.
 */
public class ImageDelegate {
    public interface BitmapListener {
        void createdImageBitmap(Bitmap imageBitmap);
    }

    public interface BytesListener {
        void createdImageBytes(byte[] data);
    }
}
