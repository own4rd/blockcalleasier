package br.com.ownard.activities;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import br.com.ownard.forgetme.R;

public class SplashActivity extends Activity {

    private static final int PERMISSIONS_REQUEST = 1234;

    String[] allPermissionNeeded = {
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.INTERNET,
            Manifest.permission.CALL_PHONE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if(checkPermissions()){
            startNextActivity();
        }
    }

    public void requestPermissions(View view){
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions((Activity) view.getContext(), allPermissionNeeded, PERMISSIONS_REQUEST);
        } else {
            startNextActivity();
        }
    }

    public void startNextActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        this.finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (checkPermissions()) {
            startNextActivity();
        }
    }

    public boolean checkPermissions() {
        for (String permission : allPermissionNeeded) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}