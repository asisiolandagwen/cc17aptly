package com.aptly.app;

import android.content.Context;

/**
 * The signed-in user's details, shared by the Profile tab and the Edit Profile screen.
 * The starting values come from strings.xml (profile_name, profile_phone, ...).
 */
final class UserProfile {

    private UserProfile() { }

    static String name;
    static String phone;
    static String email;
    static String address;

    /** Loads the starting details from strings.xml the first time a profile screen opens. */
    static void init(Context context) {
        if (name != null) return;
        name = context.getString(R.string.profile_name);
        phone = context.getString(R.string.profile_phone);
        email = context.getString(R.string.profile_email);
        address = context.getString(R.string.profile_address);
    }
}
