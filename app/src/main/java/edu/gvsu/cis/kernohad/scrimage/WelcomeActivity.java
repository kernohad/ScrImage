package edu.gvsu.cis.kernohad.scrimage;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.Freezable;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.leaderboard.Leaderboard;
import com.google.android.gms.*;
import com.google.example.games.basegameutils.BaseGameUtils;




public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener, Freezable<Leaderboard>,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Button play;
    private Button leaderboard;
    private Button signIn;
    private Button signOut;

    //google api stuff
    private GoogleApiClient mGoogleApiClient;
    private static int RC_SIGN_IN = 9001;
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInFlow = true;
    private boolean mSignInClicked = false;

    boolean mExplicitSignOut = false;
    boolean mInSignInFlow = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Instantiate the play button
        play = (Button) findViewById(R.id.play_button);
        play.setOnClickListener(this);

        //Instatiate the leaderboard button
        leaderboard = (Button) findViewById(R.id.lbButton);
        leaderboard.setOnClickListener(this);

        //Instatiate the sign in and out buttons
//        signIn = (Button) findViewById(R.id.signInButton);
//        signIn.setOnClickListener(this);
//
//        signOut = (Button) findViewById(R.id.signOutButton);
//        signOut.setOnClickListener(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        // ******************* Commented out to remove FAB *****************************

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });**/

        // Create the Google Api Client with access to the Play Games services
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                        // add other APIs and scopes here as needed
                .build();
        //});

        //*******************************************************************************
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
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.play_button:
                // Creates and starts the game activity. Where the puzzle is.
                Intent launchViewer = new Intent(WelcomeActivity.this, GameViewerActivity.class);
                startActivity(launchViewer);
                break;
            case R.id.lbButton:
                // creates and opens leaderboard
               // startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                 //       LEADERBOARD_ID), REQUEST_LEADERBOARD);
                break;
//            case R.id.signInButton:
//                //starts the async flow for sign in
//                mSignInClicked = true;
//                mGoogleApiClient.connect();
//                mInSignInFlow = true;
//
//                // show sign-out button, hide the sign-in button
//                signOut.setVisibility(View.VISIBLE);
//                signIn.setVisibility(View.GONE);
//                break;
//            case R.id.signOutButton:
//                // sign out
//
//                mExplicitSignOut = true;
//                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
//                    Games.signOut(mGoogleApiClient);
//                    mGoogleApiClient.disconnect();
//                }
//
//                // show sign-in button, hide the sign-out button
//                signIn.setVisibility(View.VISIBLE);
//                signOut.setVisibility(View.GONE);
//                break;
        }

    }

    /* Google Api Stuff */

    @Override
    protected void onStart() {
//        super.onStart();
//        mGoogleApiClient.connect();

        super.onStart();
        if (!mInSignInFlow && !mExplicitSignOut) {
            // auto sign in
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public Leaderboard freeze() {
        return null;
    }

    @Override
    public boolean isDataValid() {
        return false;
    }

    @Override
    public void onConnected(Bundle bundle) {
        // show sign-out button, hide the sign-in button
        signIn.setVisibility(View.GONE);
        signOut.setVisibility(View.VISIBLE);

        //test to see if onConnected is ever reached
        Intent launchViewer = new Intent(WelcomeActivity.this, GameViewerActivity.class);
        startActivity(launchViewer);

        // (your code here: update UI, enable functionality that depends on sign in, etc)
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // Already resolving
            return;
        }

        // If the sign in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.signin_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign in, please try again later."
            String errorString = getResources().getString(R.string.signin_other_error);
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, errorString)) {
                mResolvingConnectionFailure = false;
            }
        }
        // Put code here to display the sign-in button
//        signIn.setVisibility(View.VISIBLE);
//        signOut.setVisibility(View.GONE);
    }
}
