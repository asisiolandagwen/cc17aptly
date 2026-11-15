package com.aptly.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/** Screen 1: Welcome / Select User Type. Opens the Logo screen. */
public class WelcomeActivity extends Activity {

    public static final String EXTRA_USER_TYPE = "user_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Screens.drawBehindStatusBar(this);

        findViewById(R.id.btn_tenant).setOnClickListener(v -> openLogo("Tenant"));
        findViewById(R.id.btn_homeowner).setOnClickListener(v -> openLogo("Homeowner"));
    }

    private void openLogo(String userType) {
        Intent intent = new Intent(this, LogoActivity.class);
        intent.putExtra(EXTRA_USER_TYPE, userType);
        startActivity(intent);
    }
}
