package com.aptly.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/** Screen 11b: PROFILE tab. Each "Edit" opens the Edit Profile screen for that detail. */
public class ProfileActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        UserProfile.init(this);

        // Crop the profile photo into a circle
        findViewById(R.id.img_profile).setClipToOutline(true);

        // Edit links: open the matching edit screen
        findViewById(R.id.edit_name).setOnClickListener(v -> openEdit(EditProfileActivity.FIELD_NAME));
        findViewById(R.id.edit_phone).setOnClickListener(v -> openEdit(EditProfileActivity.FIELD_PHONE));
        findViewById(R.id.edit_email).setOnClickListener(v -> openEdit(EditProfileActivity.FIELD_EMAIL));
        findViewById(R.id.edit_address).setOnClickListener(v -> openEdit(EditProfileActivity.FIELD_ADDRESS));

        // Bottom navigation
        findViewById(R.id.nav_explore).setOnClickListener(v -> openTab(MainActivity.class));
        findViewById(R.id.nav_favorites).setOnClickListener(v -> openTab(FavoritesActivity.class));
        findViewById(R.id.nav_messages).setOnClickListener(v -> openTab(MessagesActivity.class));
    }

    /** Show the latest details (they may have just been changed on the Edit screen). */
    @Override
    protected void onResume() {
        super.onResume();
        setText(R.id.txt_name, UserProfile.name);
        setText(R.id.txt_legal_name, UserProfile.name);
        setText(R.id.txt_phone, UserProfile.phone);
        setText(R.id.txt_email, UserProfile.email);
        setText(R.id.txt_address, UserProfile.address);
    }

    private void openEdit(String field) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra(EditProfileActivity.EXTRA_FIELD, field);
        startActivity(intent);
    }

    private void setText(int id, String text) {
        ((TextView) findViewById(id)).setText(text);
    }

    /** Switches to another tab without an animation. */
    private void openTab(Class<?> screen) {
        Intent intent = new Intent(this, screen);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
