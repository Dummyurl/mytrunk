package com.car.portal.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;

public class PicassoTarget implements Target {


    private File file;
    private String name;
    private ImageView imageView;
    public PicassoTarget(ImageView imageView,File file) {
        this.file = file;
        this.name=name;
        this.imageView=imageView;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        FileOutputStream ostream;
        try {
            ostream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            ostream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
