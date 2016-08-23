package com.oceanbrasil.libocean.control.glide;

import com.bumptech.glide.signature.MediaStoreSignature;

/**
 * Created by oceanmanaus on 23/08/2016.
 */
class CacheGlide extends MediaStoreSignature {

    public CacheGlide(String mimeType, long dateModified, int orientation) {
        super(mimeType, dateModified, orientation);
    }
}