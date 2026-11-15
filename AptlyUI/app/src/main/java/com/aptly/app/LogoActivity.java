package com.aptly.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AnimationUtils;

/** Screen 2: Logo. Shown after Welcome; shows the logo, then opens Sign In. */
public class LogoActivity extends Activity {

    private static final long LOGO_DURATION_MS = 2000;
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        Screens.drawBehindStatusBar(this);

        // Fade the logo in (animation defined in res/anim/logo_fade_in.xml)
        findViewById(R.id.img_logo).startAnimation(
                AnimationUtils.loadAnimation(this, R.anim.logo_fade_in));

        handler.postDelayed(() -> {
            Intent intent = new Intent(this, SignInActivity.class);
            intent.putExtra(WelcomeActivity.EXTRA_USER_TYPE,
                    getIntent().getStringExtra(WelcomeActivity.EXTRA_USER_TYPE));
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, LOGO_DURATION_MS);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
