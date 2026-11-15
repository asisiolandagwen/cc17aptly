package com.aptly.app;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

/** Screen 7: "How many bedrooms do you need?" (step 2 of 5). */
public class BedroomsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bedrooms);

        EditText bedrooms = findViewById(R.id.input_bedrooms);

        // Skip: go straight to the Dashboard
        findViewById(R.id.btn_skip).setOnClickListener(v -> SignInActivity.openDashboard(this));

        // Next: needs a number of at least 1
        findViewById(R.id.btn_next).setOnClickListener(v -> {
            if (TextUtils.isEmpty(bedrooms.getText())) {
                bedrooms.setError(getString(R.string.error_bedrooms));
                return;
            }
            if (Integer.parseInt(bedrooms.getText().toString()) < 1) {
                bedrooms.setError(getString(R.string.error_bedrooms_min));
                return;
            }
            goNext();
        });
    }

    /** Opens step 3 of 5. */
    private void goNext() {
        startActivity(new Intent(this, FurnishingActivity.class));
    }
}