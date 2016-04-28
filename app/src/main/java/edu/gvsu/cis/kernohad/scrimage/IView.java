package edu.gvsu.cis.kernohad.scrimage;

import android.graphics.Bitmap;

import java.io.File;
import java.io.IOException;

/**
 * Created by Hans Dulimarta on Feb 23, 2016.
 */
public interface IView {
    void swapTiles(int r1, int c1, int r2, int c2);
    void redrawTiles(int[][] arr);
    void showMessage(String msg);
    void addImageToFiles();
}
