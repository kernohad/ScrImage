package edu.gvsu.cis.kernohad.scrimage;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

public class GameViewerActivity extends AppCompatActivity implements GestureDetector.OnGestureListener, IView{

    GridLayout gridLayout;
    GestureDetectorCompat gDetector;
    IPresenter  presenter;
    TextView[][] tvArray;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        gDetector = new GestureDetectorCompat(this, this);

        Drawable border = getResources().getDrawable(R.drawable.drawable);

        tvArray = new TextView[4][4];

        for (int k = 0; k < 16; k++) {
            int ri = k / 4;    /* determine row and column index */
            int ci = k % 4;

            TextView mytext = new TextView(this);

            tvArray[ri][ci] = mytext;

            mytext.setText (String.valueOf(k));
            mytext.setTextSize (52);   /* or use a number that works better for your device */
            /* place the TextView at the desired row and column */
            GridLayout.Spec r_spec = GridLayout.spec (ri, GridLayout.CENTER);
            GridLayout.Spec c_spec = GridLayout.spec (ci, GridLayout.CENTER);
            GridLayout.LayoutParams par = new GridLayout.LayoutParams (r_spec, c_spec);
            // add the following immediately before the addView call
            par.setGravity(Gravity.FILL_HORIZONTAL);
            gridLayout.addView(mytext, par);
            mytext.setBackground(border);
            mytext.setGravity(Gravity.CENTER_HORIZONTAL);
            mytext.setWidth(300);    // or any number of pixels that work for your device
        }

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
        String lab1 = tvArray[r1][c1].getText().toString();
        String lab2 = tvArray[r2][c2].getText().toString();
        tvArray[r1][c1].setText(lab2);
        tvArray[r2][c2].setText(lab1);
    }

    @Override
    public void redrawTiles(int[][] arr) {
        for (int k = 0; k < arr.length; k++)
            for (int m = 0; m < arr[k].length; m++)
                if (arr[k][m] != 0)
                    tvArray[k][m].setText(String.valueOf(arr[k][m]));
                else
                    tvArray[k][m].setText("");
    }

    @Override
    public void showMessage(String msg) {

    }
}
