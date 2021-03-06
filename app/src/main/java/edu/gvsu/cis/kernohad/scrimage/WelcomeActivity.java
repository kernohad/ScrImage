package edu.gvsu.cis.kernohad.scrimage;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;





public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Button play;
    private Button ach;
    private SignInButton signIn;
    private Button signOut;
    private Toolbar toolbar;

    //google api stuff
    private GoogleApiClient mGoogleApiClient;
    private static int RC_SIGN_IN = 9001;
    private boolean mResolvingConnectionFailure = false;
    private boolean mSignInClicked = false;
    private int REQUEST_ACHIEVEMENTS = 123;

    private final int PUZZLES_SOLVED = 121;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Instantiate the play button
        play = (Button) findViewById(R.id.play_button);
        play.setOnClickListener(this);

        //Instantiate the sign in and out buttons
        signIn = (SignInButton) findViewById(R.id.signInButton);
        signIn.setOnClickListener(this);

        signOut = (Button) findViewById(R.id.signOutButton);
        signOut.setOnClickListener(this);

        //Instantiate the achievement button
        ach = (Button) findViewById(R.id.achButton);
        ach.setOnClickListener(this);


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


        //});

        //*******************************************************************************

        // Create the Google Api Client with access to the Play Games services
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                        // add other APIs and scopes here as needed
                .build();


        //attempting toolbar remove title
        toolbar =(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
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
                startActivityForResult(launchViewer, PUZZLES_SOLVED);
                break;
            case R.id.signInButton:
                // start the asynchronous sign in flow
                mSignInClicked = true;
                mGoogleApiClient.connect();

                break;
            case R.id.signOutButton:
                mSignInClicked = false;
                if (mGoogleApiClient.isConnected()) {
                    Games.signOut(mGoogleApiClient);
                }

                mGoogleApiClient.disconnect();

                // show sign-in button, hide the sign-out and achievement buttons
                signIn.setVisibility(View.VISIBLE);
                signOut.setVisibility(View.GONE);
                ach.setVisibility(View.GONE);
                break;
            case R.id.achButton:
                    startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),
                            REQUEST_ACHIEVEMENTS);
                break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // show sign-out button, hide the sign-in button
        signIn.setVisibility(View.GONE);


        //show achievements button
        ach.setVisibility(View.VISIBLE);
        signOut.setVisibility(View.VISIBLE);
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
        if (mSignInClicked) {
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            if (connectionResult.hasResolution()){
                try{
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    mResolvingConnectionFailure = true;
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                    mResolvingConnectionFailure = false;
                    mGoogleApiClient.connect();
                }
            }else{
                Snackbar.make(signOut, connectionResult.getErrorMessage(),Snackbar.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == RC_SIGN_IN) {
            if (mResolvingConnectionFailure) {
                mResolvingConnectionFailure = false;
                mGoogleApiClient.connect();
            }
        }else if (requestCode == PUZZLES_SOLVED){
            if(data != null){
                int counter = data.getIntExtra("passingCounter", 0);
                unlockAchievement(counter);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void unlockAchievement(int counter) {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && counter > 0) {
            Games.Achievements.increment(mGoogleApiClient, getString(R.string.aBeg), counter);
            Games.Achievements.increment(mGoogleApiClient, getString(R.string.aInter), counter);
            Games.Achievements.increment(mGoogleApiClient, getString(R.string.aAdv), counter);
            Games.Achievements.increment(mGoogleApiClient, getString(R.string.aExp), counter);
            Games.Achievements.increment(mGoogleApiClient, getString(R.string.aGod), counter);
        }
    }

}
