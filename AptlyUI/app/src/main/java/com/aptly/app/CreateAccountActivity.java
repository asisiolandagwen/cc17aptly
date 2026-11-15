package com.aptly.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;

/** Screen 4: Create Account. */
public class CreateAccountActivity extends Activity {

    private static final int REQUEST_TERMS = 1;
    private CheckBox agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        EditText firstName = findViewById(R.id.input_first_name);
        EditText lastName = findViewById(R.id.input_last_name);
        EditText email = findViewById(R.id.input_email);
        EditText password = findViewById(R.id.input_password);
        agree = findViewById(R.id.check_terms);

        findViewById(R.id.link_terms).setOnClickListener(v ->
                startActivityForResult(new Intent(this, TermsActivity.class), REQUEST_TERMS));

        findViewById(R.id.btn_continue).setOnClickListener(v -> {
            if (isEmpty(firstName, getString(R.string.error_first_name))) return;
            if (isEmpty(lastName, getString(R.string.error_last_name))) return;
            if (isEmpty(email, getString(R.string.error_email))) return;
            if (isEmpty(password, getString(R.string.error_new_password))) return;
            if (!agree.isChecked()) {
                agree.setError(getString(R.string.error_agree_terms));
                return;
            }
            startActivity(new Intent(this, PriceRangeActivity.class));
        });
    }

    /** Pressing "Okay" on the Terms screen ticks the checkbox. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TERMS && resultCode == RESULT_OK) {
            agree.setChecked(true);
            agree.setError(null);
        }
    }

    private boolean isEmpty(EditText field, String error) {
        if (TextUtils.isEmpty(field.getText())) {
            field.setError(error);
            return true;
        }
        return false;
    }
}
