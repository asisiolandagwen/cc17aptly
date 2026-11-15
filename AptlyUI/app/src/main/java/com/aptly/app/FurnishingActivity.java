package com.aptly.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/** Screen 8: "What furnishing type do you prefer?" (step 3 of 5). */
public class FurnishingActivity extends Activity {

    private TextView[] options;
    private TextView selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furnishing);

        options = new TextView[] {
                findViewById(R.id.option_unfurnished),
                findViewById(R.id.option_semi_furnished),
                findViewById(R.id.option_fully_furnished)
        };

        // Tapping an option selects it and unselects the others
        for (TextView option : options) {
            option.setOnClickListener(v -> select(option));
        }

        // Skip: go straight to the Dashboard
        findViewById(R.id.btn_skip).setOnClickListener(v -> SignInActivity.openDashboard(this));

        // Next: needs one option chosen
        findViewById(R.id.btn_next).setOnClickListener(v -> {
            if (selected == null) {
                Toast.makeText(this, R.string.error_choose_one, Toast.LENGTH_SHORT).show();
                return;
            }
            goNext();
        });
    }

    private void select(TextView chosen) {
        selected = chosen;
        for (TextView option : options) {
            boolean isChosen = option == chosen;
            option.setSelected(isChosen);
            option.setTextColor(getColor(isChosen ? R.color.button_green : R.color.text_black));
            option.setTypeface(null, isChosen ? Typeface.BOLD : Typeface.NORMAL);
        }
    }

    /** Opens step 4 of 5. */
    private void goNext() {
        startActivity(new Intent(this, LeaseTypeActivity.class));
    }
}