package com.aptly.app;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

/** Screen 10: "Do you need a pet-friendly place?" (step 5 of 5). */
public class PetsActivity extends Activity {

    private TextView[] options;
    private TextView selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets);

        options = new TextView[] {
                findViewById(R.id.option_pets_allowed),
                findViewById(R.id.option_no_pets)
        };

        // Tapping an option selects it and unselects the other
        for (TextView option : options) {
            option.setOnClickListener(v -> select(option));
        }

        // Submit: needs one option chosen, then opens the Dashboard
        findViewById(R.id.btn_submit).setOnClickListener(v -> {
            if (selected == null) {
                Toast.makeText(this, R.string.error_choose_one, Toast.LENGTH_SHORT).show();
                return;
            }
            SignInActivity.openDashboard(this);
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
}
