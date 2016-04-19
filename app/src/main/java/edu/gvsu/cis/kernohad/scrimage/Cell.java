package edu.gvsu.cis.kernohad.scrimage;

/**
 * Created by Hans Dulimarta (Summer 2014)
 */

import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.widget.ImageView;

public class Cell implements Comparable<Cell> {
    public int row, column, value;



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
}
//        orig,      0, 0,   wSub, hSub);
//        orig,   wSub, 0, 2*wSub, hSub);
//        orig, 2*wSub, 0, 3*wSub, hSub);
//        orig, 3*wSub, 0, 4*wSub, hSub);