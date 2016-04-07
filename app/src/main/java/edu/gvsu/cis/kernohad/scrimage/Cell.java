package edu.gvsu.cis.kernohad.scrimage;

/**
 * Created by Hans Dulimarta (Summer 2014)
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

    public Bitmap splitImage(int i){
        int wSub, hSub, x = getDisplayWidth();
        String w = ""+x;
        String url = "https://unsplash.it/"+w+"/"+w+"/?random";
        if(getBitmapFromURL(url) == null)
            Log.d("Error", "orig Bitmap is null");
        else {
            orig = getBitmapFromURL(url);
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

                //3rd row
                case 8:  return Bitmap.createBitmap(orig, 0, 2 * hSub, wSub, 3 * hSub);
                case 9:  return Bitmap.createBitmap(orig, wSub, 2 * hSub, 2 * wSub, 3 * hSub);
                case 10: return Bitmap.createBitmap(orig, 2 * wSub, 2 * hSub, 3 * wSub, 3 * hSub);
                case 11: return Bitmap.createBitmap(orig, 3 * wSub, 2 * hSub, 4 * wSub, 3 * hSub);

                //4th row
                case 12: return Bitmap.createBitmap(orig, 0, 3 * hSub, wSub, 4 * hSub);
                case 13: return Bitmap.createBitmap(orig, wSub, 3 * hSub, 2 * wSub, 4 * hSub);
                case 14: return Bitmap.createBitmap(orig, 2 * wSub, 3 * hSub, 3 * wSub, 4 * hSub);
            }
        }
        return null;
    }

    public Bitmap getBitmapFromURL(String src){
        try {
            URL url = new URL(src);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.connect();
            InputStream input = con.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            Log.d("IOException","IOException in getBitmapFromURL");
            return null;
        }
    }
}
//        orig,      0, 0,   wSub, hSub);
//        orig,   wSub, 0, 2*wSub, hSub);
//        orig, 2*wSub, 0, 3*wSub, hSub);
//        orig, 3*wSub, 0, 4*wSub, hSub);