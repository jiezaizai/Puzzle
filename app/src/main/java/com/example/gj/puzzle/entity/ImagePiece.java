package com.example.gj.puzzle.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by gj
 * Created on 8/28/19
 * Description
 */


public class ImagePiece implements Serializable{

    private int index;
    private Bitmap bitmap;

    public ImagePiece() {

    }

    public ImagePiece(int index, Bitmap bitmap) {
        this.index = index;
        this.bitmap = bitmap;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "ImagePiece [index=" + index + ", bitmap=" + bitmap + "]";
    }
}
