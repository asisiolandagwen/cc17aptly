package com.aptly.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

/** Screen 3: Sign In. */
public class SignInActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        EditText email = findViewById(R.id.input_email);
        EditText password = findViewById(R.id.input_password);

        findViewById(R.id.btn_continue).setOnClickListener(v -> {
            if (TextUtils.isEmpty(email.getText())) {
                email.setError(getString(R.string.error_email));
                return;
            }
            if (TextUtils.isEmpty(password.getText())) {
                password.setError(getString(R.string.error_password));
                return;
            }
            startActivity(new Intent(this, PriceRangeActivity.class));
        });

        findViewById(R.id.link_create_account).setOnClickListener(v ->
                startActivity(new Intent(this, CreateAccountActivity.class)));
    }
    /** Opens the Dashboard and clears the sign-in screens behind it. */
    static void openDashboard(Activity from) {
        Intent intent = new Intent(from, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        from.startActivity(intent);
        from.finish();
    }
    /** Opens the Profile screen and clears the sign-in screens behind it. */
    static void openProfile(Activity from) {
        Intent intent = new Intent(from, ProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        from.startActivity(intent);
        from.finish();
    }
}
