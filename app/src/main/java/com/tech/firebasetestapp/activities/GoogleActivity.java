package com.tech.firebasetestapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.tech.firebasetestapp.R;
import com.tech.firebasetestapp.global.AppDialog;

public class GoogleActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    Toolbar toolbar;
    TextView tbTitle;
    ImageView tbIvBack;
    private com.google.android.gms.common.SignInButton btnGLogin;
    private TextView tvStatus;
    private TextView tvUserId;
    private TextView tvEmailVeri;
    private android.widget.Button btnLogout;
    private android.widget.Button btnDelete;
    private android.widget.LinearLayout llDetails;

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 1111;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);

        //Custom Toolbar
        toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        tbTitle = (TextView) toolbar.findViewById(R.id.tbTitle);
        tbIvBack = (ImageView) toolbar.findViewById(R.id.tbIvBack);
        tbTitle.setVisibility(View.VISIBLE);
        tbTitle.setText(getResources().getString(R.string.btnGoogle));
        tbIvBack.setVisibility(View.VISIBLE);
        tbIvBack.setOnClickListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        this.llDetails = (LinearLayout) findViewById(R.id.llDetails);
        this.btnDelete = (Button) findViewById(R.id.btnDelete);
        this.btnLogout = (Button) findViewById(R.id.btnLogout);
        this.tvEmailVeri = (TextView) findViewById(R.id.tvEmailVeri);
        this.tvUserId = (TextView) findViewById(R.id.tvUserId);
        this.tvStatus = (TextView) findViewById(R.id.tvStatus);
        this.btnGLogin = (SignInButton) findViewById(R.id.btnGLogin);
        btnGLogin.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        //For Google SignIn
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // [START_EXCLUDE]
                updateUI(user);
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]

    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            tvStatus.setText(user.getEmail());
            tvEmailVeri.setText("" + user.isEmailVerified());
            tvUserId.setText(user.getUid());
        } else {
            tvStatus.setText(getString(R.string.defaultTxt));
            tvEmailVeri.setText(getString(R.string.defaultTxt));
            tvUserId.setText(getString(R.string.defaultTxt));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tbIvBack:
                finish();
                break;
            case R.id.btnGLogin:
                GoogleLogin();
                break;
            case R.id.btnLogout:

                signOut();
                break;
            case R.id.btnDelete:
                revokeAccess();
                break;
            default:
                break;
        }

    }

    private void GoogleLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                        llDetails.setVisibility(View.GONE);
                        btnGLogin.setVisibility(View.VISIBLE);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        updateUI(null);
                        llDetails.setVisibility(View.GONE);
                        btnGLogin.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {

                updateUI(null);

            }
        }
    }

    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        AppDialog.showProgressDialog(this);
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            AppDialog.dismissProgressDialog();
                            AppDialog.showAlertDialog(GoogleActivity.this, null, task.getException().getMessage(), getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            });
                        } else {
                            AppDialog.dismissProgressDialog();
                            Toast.makeText(GoogleActivity.this, "Login Success !", Toast.LENGTH_LONG).show();
                            llDetails.setVisibility(View.VISIBLE);
                            btnGLogin.setVisibility(View.GONE);
                        }

                    }
                });
    }


}
