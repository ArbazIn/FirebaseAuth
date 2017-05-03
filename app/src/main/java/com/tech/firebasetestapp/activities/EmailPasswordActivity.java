package com.tech.firebasetestapp.activities;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tech.firebasetestapp.R;
import com.tech.firebasetestapp.global.AppDialog;
import com.tech.firebasetestapp.global.Global;


public class EmailPasswordActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser currentUser;

    private android.widget.EditText etEmail;
    private android.widget.EditText etPassword;
    private android.widget.Button btnLogin;
    private android.widget.Button btnRegister;
    private android.widget.TextView tvForgot;
    private android.support.design.widget.TextInputLayout tilEmail;
    private android.support.design.widget.TextInputLayout tilPass;

    private TextView tvStatus;
    private TextView tvUserId;
    private LinearLayout llLR;
    private Button btnLogout;
    private LinearLayout llSV;
    private TextView tvEmailVeri;
    private LinearLayout llDetails;
    private TextView tvEmailVeriHere;

    private Button btnDelete;
    private Button btnVerified;
    private Button btnCEmail;
    private Button btnCPass;

    private static final String TAG = "Email And Password";
    Toolbar toolbar;
    TextView tbTitle;
    ImageView tbIvBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password);

        //get current user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
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
                updateUI(user);
            }
        };
        // [END auth_state_listener]

        //Custom Toolbar
        toolbar = (Toolbar) findViewById(R.id.custom_toolbar);
        setSupportActionBar(toolbar);
        tbTitle = (TextView) toolbar.findViewById(R.id.tbTitle);
        tbIvBack = (ImageView) toolbar.findViewById(R.id.tbIvBack);
        tbTitle.setVisibility(View.VISIBLE);
        tbTitle.setText(getResources().getString(R.string.btnEmailPass));
        tbIvBack.setVisibility(View.VISIBLE);
        tbIvBack.setOnClickListener(this);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        this.tilPass = (TextInputLayout) findViewById(R.id.tilPass);
        this.tilEmail = (TextInputLayout) findViewById(R.id.tilEmail);
        this.tvForgot = (TextView) findViewById(R.id.tvForgot);


        this.etEmail = (EditText) findViewById(R.id.etEmail);
        this.etPassword = (EditText) findViewById(R.id.etPassword);

        this.tvStatus = (TextView) findViewById(R.id.tvStatus);
        this.tvEmailVeri = (TextView) findViewById(R.id.tvEmailVeri);
        this.tvUserId = (TextView) findViewById(R.id.tvUserId);

        this.llDetails = (LinearLayout) findViewById(R.id.llDetails);
        this.llLR = (LinearLayout) findViewById(R.id.llLR);
        this.llSV = (LinearLayout) findViewById(R.id.llSV);

        this.btnLogin = (Button) findViewById(R.id.btnLogin);
        this.btnRegister = (Button) findViewById(R.id.btnRegister);
        this.btnLogout = (Button) findViewById(R.id.btnLogout);
        this.btnDelete = (Button) findViewById(R.id.btnDelete);
        this.btnVerified = (Button) findViewById(R.id.btnVerified);

        this.btnCPass = (Button) findViewById(R.id.btnCPass);
        this.btnCEmail = (Button) findViewById(R.id.btnCEmail);

        etEmail.setText("arbaz6794@gmail.com");
        etPassword.setText("MasAndy");

        llDetails.setVisibility(View.GONE);
        llLR.setVisibility(View.VISIBLE);
        llSV.setVisibility(View.GONE);

        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnVerified.setOnClickListener(this);

        btnCEmail.setOnClickListener(this);
        btnCPass.setOnClickListener(this);

        tvForgot.setOnClickListener(this);


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


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                userLogin();
                break;
            case R.id.btnRegister:
                userRegister();
                break;
            case R.id.btnVerified:
                sendEmailVerification();
                break;
            case R.id.btnLogout:
                userLogout();
                break;
            case R.id.tvForgot:
                forgotDialog();
                break;
            case R.id.btnDelete:
                deleteAccount();
                break;
            case R.id.btnCEmail:

                changeEmail();
                break;
            case R.id.btnCPass:
                changePassword();
                break;
            case R.id.tbIvBack:
                finish();
                break;
            default:
                break;
        }
    }


    //For Setting Details
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void updateUI(FirebaseUser user) {
        if (user != null) {

            tvStatus.setText(user.getEmail());
            if (user.isEmailVerified() == true) {
                tvEmailVeri.setText("Verified");
                tvEmailVeri.setTextColor(getColor(R.color.colorGreen));
            } else {
                tvEmailVeri.setText("Pending");
                tvEmailVeri.setTextColor(getColor(R.color.colorRed));
            }

            tvUserId.setText(user.getUid());
        } else {
            tvStatus.setText(getString(R.string.defaultTxt));
            tvEmailVeri.setText(getString(R.string.defaultTxt));
            tvUserId.setText(getString(R.string.defaultTxt));
        }

    }


    //For User Login
    private void userLogin() {
        String emailStr = etEmail.getText().toString();
        String passwordStr = etPassword.getText().toString();
        if (!TextUtils.isEmpty(emailStr)) {
            if (!TextUtils.isEmpty(passwordStr)) {
                AppDialog.showProgressDialog(this);
                mAuth.signInWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            AppDialog.dismissProgressDialog();
                            AppDialog.showAlertDialog(EmailPasswordActivity.this, null, task.getException().getMessage(), getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            });
                        } else {
                            AppDialog.dismissProgressDialog();
                            Toast.makeText(EmailPasswordActivity.this, "Login Success !", Toast.LENGTH_LONG).show();
                            llDetails.setVisibility(View.VISIBLE);
                            llLR.setVisibility(View.GONE);
                            llSV.setVisibility(View.VISIBLE);
                            etEmail.setFocusable(false);
                            etEmail.setFocusableInTouchMode(false);
                            etPassword.setFocusable(false);
                            etPassword.setFocusableInTouchMode(false);

                        }

                    }
                });
            } else {

                tilPass.setError(getString(R.string.pass_validation));
            }
        } else {
            tilEmail.setError(getString(R.string.email_validation));
        }
    }

    //User Logout
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void userLogout() {
        mAuth.signOut();
        updateUI(null);
        llDetails.setVisibility(View.GONE);
        llLR.setVisibility(View.VISIBLE);
        llSV.setVisibility(View.GONE);
        etEmail.setFocusable(true);
        etEmail.setFocusableInTouchMode(true);
        etPassword.setFocusable(true);
        etPassword.setFocusableInTouchMode(true);

    }

    //For User Registration
    private void userRegister() {
        String emailStrR = etEmail.getText().toString();
        String passwordStrR = etPassword.getText().toString();
        if (!TextUtils.isEmpty(emailStrR)) {
            if (!TextUtils.isEmpty(passwordStrR)) {
                AppDialog.showProgressDialog(this);
                mAuth.createUserWithEmailAndPassword(emailStrR, passwordStrR).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            AppDialog.dismissProgressDialog();
                            AppDialog.showAlertDialog(EmailPasswordActivity.this, null, task.getException().getMessage(), getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        } else {
                            AppDialog.dismissProgressDialog();
                            Toast.makeText(EmailPasswordActivity.this, "Registration Success !", Toast.LENGTH_LONG).show();

                            llDetails.setVisibility(View.VISIBLE);
                            llLR.setVisibility(View.GONE);
                            llSV.setVisibility(View.VISIBLE);
                        }

                    }
                });
            } else {
                tilPass.setError(getString(R.string.pass_validation));
            }
        } else {
            tilEmail.setError(getString(R.string.email_validation));
        }
    }

    //For forgot Password
    private void forgotDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EmailPasswordActivity.this);
        alertDialog.setTitle("Forgot Password");
        alertDialog.setMessage("Enter Email");

        final EditText input = new EditText(EmailPasswordActivity.this);
        input.setText(etEmail.getText().toString());
        input.setMaxLines(1);
        input.setSingleLine(true);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        //alertDialog.setIcon(R.drawable.key);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String email = input.getText().toString();
                        if (Global.isNetworkAvailable(EmailPasswordActivity.this)) {


                            if (!TextUtils.isEmpty(email)) {

                                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            AppDialog.showAlertDialog(EmailPasswordActivity.this, null, "We have sent you instructions to reset your password!", getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();

                                                }
                                            });

                                        } else {
                                            AppDialog.showAlertDialog(EmailPasswordActivity.this, null, "Failed to send reset email!", getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });


                                        }

                                    }
                                });
                            }
                        } else {

                            AppDialog.noNetworkDialog(EmailPasswordActivity.this, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                        }
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    //For Delete User
    private void deleteAccount() {
        AppDialog.showProgressDialog(this);
        if (currentUser != null) {
            currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        AppDialog.dismissProgressDialog();
                        AppDialog.showAlertDialog(EmailPasswordActivity.this, null, "Your profile is deleted! Create a account now!", getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                llDetails.setVisibility(View.GONE);
                                llLR.setVisibility(View.VISIBLE);
                                llSV.setVisibility(View.GONE);
                                dialog.dismiss();
                                userLogout();
                            }
                        });
                    } else {
                        AppDialog.dismissProgressDialog();
                        AppDialog.showAlertDialog(EmailPasswordActivity.this, null, "Failed to delete your account!", getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                    }
                }
            });
        }
    }

    //For Email Verification
    private void sendEmailVerification() {
        AppDialog.showProgressDialog(this);
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        if (user.isEmailVerified() == true) {

            AppDialog.dismissProgressDialog();
            AppDialog.showAlertDialog(EmailPasswordActivity.this, null, "Email address is already verified!", getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            user.sendEmailVerification()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // [START_EXCLUDE]

                            if (task.isSuccessful()) {
                                AppDialog.dismissProgressDialog();
                                AppDialog.showAlertDialog(EmailPasswordActivity.this, null, "Verification email sent to " + user.getEmail(), getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.M)
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        tvEmailVeri.setText("Pending");
                                        tvEmailVeri.setTextColor(getColor(R.color.colorRed));

                                        dialog.dismiss();


                                    }
                                });

                            } else {
                                AppDialog.dismissProgressDialog();
                                AppDialog.showAlertDialog(EmailPasswordActivity.this, null, "Failed to send verification email.", getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                Log.e(TAG, "sendEmailVerification", task.getException());

                            }
                            // [END_EXCLUDE]
                        }
                    });
            // [END send_email_verification]
        }
    }

    //Change Email
    private void changeEmail() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(EmailPasswordActivity.this);
        alertDialog.setTitle("Enter New Email");
        /*alertDialog.setMessage("Enter New Email");*/
        alertDialog.setCancelable(false);

        final EditText input = new EditText(EmailPasswordActivity.this);
        input.setMaxLines(1);
        input.setSingleLine(true);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        //alertDialog.setIcon(R.drawable.key);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        String email = input.getText().toString();
                        if (Global.isNetworkAvailable(EmailPasswordActivity.this)) {
                            if (!TextUtils.isEmpty(email)) {
                                AppDialog.showProgressDialog(EmailPasswordActivity.this);
                                if (currentUser != null) {
                                    currentUser.updateEmail(email)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @RequiresApi(api = Build.VERSION_CODES.M)
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(EmailPasswordActivity.this, "Email address is updated. Please sign in with new email id!", Toast.LENGTH_LONG).show();
                                                        userLogout();
                                                        AppDialog.dismissProgressDialog();
                                                        EmailPasswordActivity.this.finish();

                                                    } else {
                                                        Toast.makeText(EmailPasswordActivity.this, "Failed to update email!", Toast.LENGTH_LONG).show();
                                                        AppDialog.dismissProgressDialog();
                                                        EmailPasswordActivity.this.finish();
                                                    }
                                                }
                                            });
                                }


                            } else {

                                Toast.makeText(EmailPasswordActivity.this, "Enter Email!", Toast.LENGTH_LONG).show();
                                AppDialog.dismissProgressDialog();
                            }
                        } else {
                            AppDialog.noNetworkDialog(EmailPasswordActivity.this, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                        }
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }

    //For Change Password
    private void changePassword() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EmailPasswordActivity.this);
        alertDialog.setTitle("Enter New Password");
        /*alertDialog.setMessage("Enter New Password");*/

        final EditText input = new EditText(EmailPasswordActivity.this);
        input.setMaxLines(1);
        input.setSingleLine(true);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        //alertDialog.setIcon(R.drawable.key);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String pass = input.getText().toString();
                        if (Global.isNetworkAvailable(EmailPasswordActivity.this)) {


                            if (!TextUtils.isEmpty(pass)) {
                                AppDialog.showProgressDialog(EmailPasswordActivity.this);
                                if (currentUser != null) {
                                    currentUser.updatePassword(pass)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @RequiresApi(api = Build.VERSION_CODES.M)
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(EmailPasswordActivity.this, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show();
                                                        userLogout();
                                                        AppDialog.dismissProgressDialog();
                                                    } else {
                                                        Toast.makeText(EmailPasswordActivity.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                                        AppDialog.dismissProgressDialog();
                                                    }
                                                }
                                            });
                                }
                            } else if (etEmail.getText().toString().trim().equals("")) {
                                AppDialog.dismissProgressDialog();
                            }
                        } else {

                            AppDialog.noNetworkDialog(EmailPasswordActivity.this, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                        }
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }
}

