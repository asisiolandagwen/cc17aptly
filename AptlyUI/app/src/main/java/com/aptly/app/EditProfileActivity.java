package com.aptly.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Screen 11g: Edit Profile, opened by an "Edit" link on the Profile tab.
 * One screen for all four: it shows the Name, Phone#, Email or Address box depending on EXTRA_FIELD.
 */
public class EditProfileActivity extends Activity {

    static final String EXTRA_FIELD = "field";
    static final String FIELD_NAME = "name";
    static final String FIELD_PHONE = "phone";
    static final String FIELD_EMAIL = "email";
    static final String FIELD_ADDRESS = "address";

    private String field;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        UserProfile.init(this);

        findViewById(R.id.img_profile).setClipToOutline(true);
        ((TextView) findViewById(R.id.txt_name)).setText(UserProfile.name);

        field = getIntent().getStringExtra(EXTRA_FIELD);
        if (field == null) field = FIELD_NAME;

        TextView label = findViewById(R.id.txt_label);
        input = findViewById(R.id.input_value);

        // Label, current value and keyboard type for the field being edited
        switch (field) {
            case FIELD_PHONE:
                label.setText(R.string.label_phone);
                input.setText(UserProfile.phone);
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            case FIELD_EMAIL:
                label.setText(R.string.label_email);
                input.setText(UserProfile.email);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case FIELD_ADDRESS:
                label.setText(R.string.label_address);
                input.setText(UserProfile.address);
                input.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                input.setMaxLines(4);
                break;
            default:
                label.setText(R.string.label_legal_name);
                input.setText(UserProfile.name);
                input.setInputType(InputType.TYPE_CLASS_TEXT
                        | InputType.TYPE_TEXT_VARIATION_PERSON_NAME | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                break;
        }

        // Cursor at the end of the text, like the prototype
        input.requestFocus();
        input.setSelection(input.getText().length());

        // x button: clear the box
        findViewById(R.id.btn_clear).setOnClickListener(v -> {
            input.setText("");
            input.requestFocus();
        });

        // Save: check the value, store it, go back to the Profile tab
        findViewById(R.id.btn_save).setOnClickListener(v -> save());
    }

    private void save() {
        String value = input.getText().toString().trim();

        if (TextUtils.isEmpty(value)) {
            input.setError(getString(R.string.error_empty));
            return;
        }

        switch (field) {
            case FIELD_PHONE:
                String digits = value.replaceAll("[^0-9]", "");
                if (digits.length() < 7 || digits.length() > 15) {
                    input.setError(getString(R.string.error_phone));
                    return;
                }
                UserProfile.phone = value;
                break;
            case FIELD_EMAIL:
                if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
                    input.setError(getString(R.string.error_email_invalid));
                    return;
                }
                UserProfile.email = value;
                break;
            case FIELD_ADDRESS:
                UserProfile.address = value;
                break;
            default:
                UserProfile.name = value;
                break;
        }
        finish();
    }
}
