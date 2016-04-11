package edu.gvsu.cis.kernohad.scrimage;

/**
 * Created by Hans Dulimarta (Summer 2014)
 */

import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.widget.ImageView;

public class Cell implements Comparable<Cell> {
    public int row, column, value;
    public Bitmap orig;


    public Cell()
    {
        this(0,0,0);
    }
    public Cell(int r, int c, int v)
    {
        row = r;
        column = c;
        value = v;
    }


    /* must override equals to ensure List::contains() works
     * correctly
     */
    @Override
    public int compareTo (Cell other) {
        if (this.row < other.row) return -1;
        if (this.row > other.row) return +1;

        /* break the tie using column */
        return this.column - other.column;
    }

    public int getDisplayWidth(){
        DisplayMetrics metrics = new DisplayMetrics();
        return metrics.widthPixels;
    }

    public Bitmap splitImage(ImageView img, int i)
            /*
            parameter: an int to tell the function which bitmap to return.
            Sugg.:  Use a loop from 0 to 14 to "paint" each returned bitmap to
                    the 2d array of ImageView.
            */
    {
        int wSub, hSub;
        orig = getBitmapFromImageView(img);
        wSub = orig.getWidth() / 4;
        hSub = orig.getHeight() / 4;
        switch (i) {
            //1st row
            case 0: return Bitmap.createBitmap(orig, 0, 0, wSub, hSub);
            case 1: return Bitmap.createBitmap(orig, wSub, 0, 2 * wSub, hSub);
            case 2: return Bitmap.createBitmap(orig, 2 * wSub, 0, 3 * wSub, hSub);
            case 3: return Bitmap.createBitmap(orig, 3 * wSub, 0, 4 * wSub, hSub);
            //2nd row
            case 4: return Bitmap.createBitmap(orig, 0, hSub, wSub, 2 * hSub);
            case 5: return Bitmap.createBitmap(orig, wSub, hSub, 2 * wSub, 2 * hSub);
            case 6: return Bitmap.createBitmap(orig, 2 * wSub, hSub, 3 * wSub, 2 * hSub);
            case 7: return Bitmap.createBitmap(orig, 3 * wSub, hSub, 4 * wSub, 2 * hSub);
            //3rd roW
            case 8:  return Bitmap.createBitmap(orig, 0, 2 * hSub, wSub, 3 * hSub);
            case 9:  return Bitmap.createBitmap(orig, wSub, 2 * hSub, 2 * wSub, 3 * hSub);
            case 10: return Bitmap.createBitmap(orig, 2 * wSub, 2 * hSub, 3 * wSub, 3 * hSub);
            case 11: return Bitmap.createBitmap(orig, 3 * wSub, 2 * hSub, 4 * wSub, 3 * hSub);
            //4th row
            case 12: return Bitmap.createBitmap(orig, 0, 3 * hSub, wSub, 4 * hSub);
            case 13: return Bitmap.createBitmap(orig, wSub, 3 * hSub, 2 * wSub, 4 * hSub);
            case 14: return Bitmap.createBitmap(orig, 2 * wSub, 3 * hSub, 3 * wSub, 4 * hSub);
        }
        return null;
    }

    public Bitmap getBitmapFromImageView(ImageView img){
        img.buildDrawingCache();
        return img.getDrawingCache();
    }
}
//        orig,      0, 0,   wSub, hSub);
//        orig,   wSub, 0, 2*wSub, hSub);
//        orig, 2*wSub, 0, 3*wSub, hSub);
//        orig, 3*wSub, 0, 4*wSub, hSub);