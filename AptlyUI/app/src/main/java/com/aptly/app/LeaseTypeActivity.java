package com.aptly.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/** Screen 9: "What lease type do you prefer?" (step 4 of 5). Pick all that apply. */
public class LeaseTypeActivity extends Activity {

    private TextView[] options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lease_type);

        options = new TextView[] {
                findViewById(R.id.option_short_term),
                findViewById(R.id.option_long_term),
                findViewById(R.id.option_month_to_month),
                findViewById(R.id.option_fixed_term),
                findViewById(R.id.option_lease_to_own)
        };

        // Tapping an option turns it on or off (more than one can be chosen)
        for (TextView option : options) {
            option.setOnClickListener(v -> toggle(option));
        }

        // Skip: go straight to the Dashboard
        findViewById(R.id.btn_skip).setOnClickListener(v -> SignInActivity.openDashboard(this));

        // Next: needs at least one option chosen
        findViewById(R.id.btn_next).setOnClickListener(v -> {
            if (!anySelected()) {
                Toast.makeText(this, R.string.error_choose_at_least_one, Toast.LENGTH_SHORT).show();
                return;
            }
            goNext();
        });
    }

    private void toggle(TextView option) {
        boolean isChosen = !option.isSelected();
        option.setSelected(isChosen);
        option.setTextColor(getColor(isChosen ? R.color.button_green : R.color.text_black));
        option.setTypeface(null, isChosen ? Typeface.BOLD : Typeface.NORMAL);
    }

    private boolean anySelected() {
        for (TextView option : options) {
            if (option.isSelected()) return true;
        }
        return false;
    }

    /** Opens step 5 of 5. */
    private void goNext() {
        startActivity(new Intent(this, PetsActivity.class));
    }
}
