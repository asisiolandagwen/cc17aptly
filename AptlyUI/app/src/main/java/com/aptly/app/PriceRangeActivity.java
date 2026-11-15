package com.aptly.app;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

/** Screen 6: "What is your price range?" (step 1 of 5). */
public class PriceRangeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_range);

        EditText min = findViewById(R.id.input_min);
        EditText max = findViewById(R.id.input_max);

        // Skip: go straight to the Dashboard
        findViewById(R.id.btn_skip).setOnClickListener(v -> SignInActivity.openDashboard(this));

        // Next: check the numbers first
        findViewById(R.id.btn_next).setOnClickListener(v -> {
            if (TextUtils.isEmpty(min.getText())) {
                min.setError(getString(R.string.error_min));
                return;
            }
            if (TextUtils.isEmpty(max.getText())) {
                max.setError(getString(R.string.error_max));
                return;
            }
            int minValue = Integer.parseInt(min.getText().toString());
            int maxValue = Integer.parseInt(max.getText().toString());
            if (maxValue < minValue) {
                max.setError(getString(R.string.error_max_below_min));
                return;
            }
            goNext();
        });
    }

    /** Opens step 2 of 5. */
    private void goNext() {
        startActivity(new Intent(this, BedroomsActivity.class));
    }
}