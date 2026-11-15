package com.aptly.app;

import android.app.Activity;
import android.os.Bundle;

/** Screen 5: Terms & Conditions. */
public class TermsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        findViewById(R.id.btn_okay).setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });
    }
}
