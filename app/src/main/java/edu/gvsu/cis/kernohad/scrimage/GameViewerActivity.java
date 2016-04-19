package edu.gvsu.cis.kernohad.scrimage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class GameViewerActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, IView{

    GridLayout gridLayout;
    GestureDetectorCompat gDetector;
    IPresenter  presenter;
    ImageView[][] ivArray;
    Bitmap orig;
    Bitmap[] bm = new Bitmap[16];
    Target loadTarget;
    int size;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        gDetector = new GestureDetectorCompat(this, this);

        Drawable border = getResources().getDrawable(R.drawable.drawable);
        ImageView hold = new ImageView(this);
        ivArray = new ImageView[4][4];

        //fill ivArray with placeholder images
        for(int i=0;i<4;i++)
            for(int j=0;j<4;j++)
                ivArray[i][j] = hold;

        //call loadBitmap. pass url,height,width,context
        size = getDisplayWidth() - 100;
        loadBitmap("https://source.unsplash.com/random/", size, size, getApplicationContext());


        presenter = new Presenter();
        presenter.onAttachView(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_welcome, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d("X", "" + velocityX);
        Log.d("Y", "" + velocityY);

        if (velocityX > 3000 )
            presenter.onSlide(SlideDirection.RIGHT);
        else if (velocityX < -3000 )
            presenter.onSlide(SlideDirection.LEFT);
        else if (velocityY < -3000)
            presenter.onSlide(SlideDirection.UP);
        else if (velocityY > 3000)
            presenter.onSlide(SlideDirection.DOWN);

        return false;
    }

    @Override
    public void swapTiles(int r1, int c1, int r2, int c2) {
        Drawable tmp = ivArray[r1][c1].getDrawable();
        Drawable tmp2 = ivArray[r2][c2].getDrawable();
        ivArray[r1][c1].setImageDrawable(tmp2);
        ivArray[r2][c2].setImageDrawable(tmp);
    }

    @Override
    public void redrawTiles(int[][] arr) {
        for (int k = 0; k < arr.length; k++)
            for (int m = 0; m < arr[k].length; m++)
                if (arr[k][m] != 0)
                    ivArray[k][m].setImageBitmap(bm[arr[k][m]]);
                else {
                    ivArray[k][m].setImageDrawable(null);
                }

    }

    @Override
    public void showMessage(String msg) {

    }

    public int getDisplayWidth(){
        //DisplayMetrics metrics = new DisplayMetrics();
        //return metrics.widthPixels;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        return width;
    }

    public void loadBitmap(String url, int x, int y, Context context){
        if(loadTarget == null) loadTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                splitImage(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(context).load(url)
                .resize(x,y)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(loadTarget);
    }

    private void splitImage(Bitmap img)
    {

        drawGrid();

        int wSub, hSub;
        orig = img;
        wSub = size / 4;
        hSub = size / 4;
        //1st row
        bm[0] = Bitmap.createBitmap(orig, 0, 0, wSub, hSub);
        bm[1] = Bitmap.createBitmap(orig, wSub, 0, wSub, hSub);
        bm[2] = Bitmap.createBitmap(orig, 2 * wSub, 0, wSub, hSub);
        bm[3] = Bitmap.createBitmap(orig, 3 * wSub, 0, wSub, hSub);
        //2nd row
        bm[4] = Bitmap.createBitmap(orig, 0, hSub, wSub, hSub);
        bm[5] = Bitmap.createBitmap(orig, wSub, hSub, wSub, hSub);
        bm[6] = Bitmap.createBitmap(orig, 2 * wSub, hSub, wSub, hSub);
        bm[7] = Bitmap.createBitmap(orig, 3 * wSub, hSub, wSub, hSub);
        //3rd roW
        bm[8] = Bitmap.createBitmap(orig, 0, 2 * hSub, wSub, hSub);
        bm[9] = Bitmap.createBitmap(orig, wSub, 2 * hSub, wSub, hSub);
        bm[10] = Bitmap.createBitmap(orig, 2 * wSub, 2 * hSub, wSub, hSub);
        bm[11] = Bitmap.createBitmap(orig, 3 * wSub, 2 * hSub, wSub, hSub);
        //4th row
        bm[12] = Bitmap.createBitmap(orig, 0, 3 * hSub, wSub, hSub);
        bm[13] = Bitmap.createBitmap(orig, wSub, 3 * hSub, wSub, hSub);
        bm[14] = Bitmap.createBitmap(orig, 2 * wSub, 3 * hSub, wSub, hSub);

        //drawGrid();
    }

    private void drawGrid(){
        for (int k = 0; k < 16; k++) {
            int ri = k / 4;    /* determine row and column index */
            int ci = k % 4;

            ImageView myimage = new ImageView(this);

            ivArray[ri][ci] = myimage;

            GridLayout.Spec r_spec = GridLayout.spec (ri, GridLayout.CENTER);
            GridLayout.Spec c_spec = GridLayout.spec (ci, GridLayout.CENTER);
            GridLayout.LayoutParams par = new GridLayout.LayoutParams (r_spec, c_spec);
            // add the following immediately before the addView call
            par.setGravity(Gravity.FILL_HORIZONTAL);
            gridLayout.addView(myimage, par);
        }
    }
}
