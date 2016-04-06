package edu.gvsu.cis.kernohad.scrimage;

/**
 * Created by Hans Dulimarta (Summer 2014)
 */

import android.graphics.Bitmap;

public class Cell implements Comparable<Cell> {
    public int row, column, value;
    Bitmap orig, bm1, bm2, bm3, bm4,
                         bm5, bm6, bm7, bm8,
                         bm9, bm10, bm11, bm12,
                         bm13, bm14, bm15;//bm16 empty


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

    public void splitImage(){
        int wSub = orig.getWidth()/4;
        int hSub = orig.getHeight()/4;

        //bitmap factory image
        //1st row
        bm1 = Bitmap.createBitmap(orig,      0, 0,   wSub, hSub);
        bm2 = Bitmap.createBitmap(orig,   wSub, 0, 2*wSub, hSub);
        bm3 = Bitmap.createBitmap(orig, 2*wSub, 0, 3*wSub, hSub);
        bm4 = Bitmap.createBitmap(orig, 3*wSub, 0, 4*wSub, hSub);

        //2nd row
        bm5 = Bitmap.createBitmap(orig,      0, hSub,   wSub, 2*hSub);
        bm6 = Bitmap.createBitmap(orig,   wSub, hSub, 2*wSub, 2*hSub);
        bm7 = Bitmap.createBitmap(orig, 2*wSub, hSub, 3*wSub, 2*hSub);
        bm8 = Bitmap.createBitmap(orig, 3*wSub, hSub, 4*wSub, 2*hSub);

        //3rd row
        bm9  = Bitmap.createBitmap(orig,      0, 2*hSub,   wSub, 3*hSub);
        bm10 = Bitmap.createBitmap(orig,   wSub, 2*hSub, 2*wSub, 3*hSub);
        bm11 = Bitmap.createBitmap(orig, 2*wSub, 2*hSub, 3*wSub, 3*hSub);
        bm12 = Bitmap.createBitmap(orig, 3*wSub, 2*hSub, 4*wSub, 3*hSub);

        //4th row
        bm13 = Bitmap.createBitmap(orig,      0, 3*hSub,   wSub, 4*hSub);
        bm14 = Bitmap.createBitmap(orig,   wSub, 3*hSub, 2*wSub, 4*hSub);
        bm15 = Bitmap.createBitmap(orig, 2*wSub, 3*hSub, 3*wSub, 4*hSub);
    }
}
//        orig,      0, 0,   wSub, hSub);
//        orig,   wSub, 0, 2*wSub, hSub);
//        orig, 2*wSub, 0, 3*wSub, hSub);
//        orig, 3*wSub, 0, 4*wSub, hSub);