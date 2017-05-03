package com.tech.firebasetestapp.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.tech.firebasetestapp.R;
import com.tech.firebasetestapp.global.AppDialog;
import com.tech.firebasetestapp.global.Global;

public class SplashActivity extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*For fullScreen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        if (Global.isNetworkAvailable(this)) {
            Thread lTimer = new Thread() {

                public void run() {

                    try {
                        int lTimer1 = 0;
                        while (lTimer1 < SPLASH_DISPLAY_LENGTH) {
                            sleep(100);
                            lTimer1 = lTimer1 + 100;
                        }
                        startActivity(new Intent(getApplicationContext(), SelectionActivity.class));
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        finish();
                    }
                }
            };
            lTimer.start();
        } else {
            AppDialog.showAlertDialog(SplashActivity.this, null, getString(R.string.txt_no_network),
                    getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);
                        }
                    });
          /*  final Dialog dialog = ModuleProjectDialog.showMessageDialogCustom(this, getResources().getString(R.string.txt_no_network));
            Button btnDismiss = (Button) dialog.findViewById(R.id.btn_cstm_msg);
            btnDismiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });*/

        }
    }
}