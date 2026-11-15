package com.aptly.app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Screen 11c: Filters (opened from the Dashboard filter icon).
 * Scrollable list of price, bedrooms, furnishing, pets, parking and lease-type filters.
 * "See results" sends the choices back to the Dashboard, which shows only matching apartments.
 */
public class FiltersActivity extends Activity {

    private static final String[] FURNISHING_VALUES = {
            Apartment.UNFURNISHED, Apartment.SEMI_FURNISHED, Apartment.FULLY_FURNISHED};
    private static final String[] LEASE_VALUES = {
            Apartment.SHORT_TERM, Apartment.LONG_TERM, Apartment.MONTH_TO_MONTH,
            Apartment.FIXED_TERM, Apartment.LEASE_TO_OWN};

    private EditText priceMin, priceMax, bedroomsMin, bedroomsMax;
    private TextView[] furnishing;
    private TextView[] pets;            // [0] = Pets allowed, [1] = No pets allowed
    private CheckBox parking;
    private CheckBox[] lease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        priceMin = findViewById(R.id.input_price_min);
        priceMax = findViewById(R.id.input_price_max);
        bedroomsMin = findViewById(R.id.input_bedrooms_min);
        bedroomsMax = findViewById(R.id.input_bedrooms_max);

        furnishing = new TextView[] {
                findViewById(R.id.option_unfurnished),
                findViewById(R.id.option_semi_furnished),
                findViewById(R.id.option_fully_furnished)
        };
        pets = new TextView[] {
                findViewById(R.id.option_pets_allowed),
                findViewById(R.id.option_no_pets)
        };
        parking = findViewById(R.id.check_parking);
        lease = new CheckBox[] {
                findViewById(R.id.check_short_term),
                findViewById(R.id.check_long_term),
                findViewById(R.id.check_month_to_month),
                findViewById(R.id.check_fixed_term),
                findViewById(R.id.check_lease_to_own)
        };

        // Furnishing and Pets: choose one per group (tap again to unselect)
        for (TextView option : furnishing) {
            option.setOnClickListener(v -> choose(furnishing, option));
        }
        for (TextView option : pets) {
            option.setOnClickListener(v -> choose(pets, option));
        }

        // Show the filters that are already applied on the Dashboard
        show(ApartmentFilter.from(getIntent()));

        // Back arrow: return to the Dashboard without changing anything
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Reset: clear every text box, option and checkbox
        findViewById(R.id.btn_reset).setOnClickListener(v -> show(new ApartmentFilter()));

        // See results: check the Min/Max pairs, then send the filters to the Dashboard
        findViewById(R.id.btn_see_results).setOnClickListener(v -> {
            if (!minNotAboveMax(priceMin, priceMax)) return;
            if (!minNotAboveMax(bedroomsMin, bedroomsMax)) return;
            Intent result = new Intent();
            read().putInto(result);
            setResult(RESULT_OK, result);
            finish();
        });
    }

    /** Fills the screen from a filter (an empty filter clears everything). */
    private void show(ApartmentFilter f) {
        setNumber(priceMin, f.priceMin);
        setNumber(priceMax, f.priceMax);
        setNumber(bedroomsMin, f.bedroomsMin);
        setNumber(bedroomsMax, f.bedroomsMax);
        for (int i = 0; i < furnishing.length; i++) {
            style(furnishing[i], FURNISHING_VALUES[i].equals(f.furnishing));
        }
        style(pets[0], Boolean.TRUE.equals(f.petsAllowed));
        style(pets[1], Boolean.FALSE.equals(f.petsAllowed));
        parking.setChecked(f.needsParking);
        for (int i = 0; i < lease.length; i++) {
            lease[i].setChecked(f.leaseTypes.contains(LEASE_VALUES[i]));
        }
    }

    /** Reads the screen into a filter. */
    private ApartmentFilter read() {
        ApartmentFilter f = new ApartmentFilter();
        f.priceMin = getNumber(priceMin);
        f.priceMax = getNumber(priceMax);
        f.bedroomsMin = getNumber(bedroomsMin);
        f.bedroomsMax = getNumber(bedroomsMax);
        for (int i = 0; i < furnishing.length; i++) {
            if (furnishing[i].isSelected()) f.furnishing = FURNISHING_VALUES[i];
        }
        if (pets[0].isSelected()) f.petsAllowed = true;
        if (pets[1].isSelected()) f.petsAllowed = false;
        f.needsParking = parking.isChecked();
        for (int i = 0; i < lease.length; i++) {
            if (lease[i].isChecked()) f.leaseTypes.add(LEASE_VALUES[i]);
        }
        return f;
    }

    /** Selects one option in a group; tapping the selected one again unselects it. */
    private void choose(TextView[] group, TextView chosen) {
        boolean turnOn = !chosen.isSelected();
        for (TextView option : group) {
            style(option, turnOn && option == chosen);
        }
    }

    private void style(TextView option, boolean isChosen) {
        option.setSelected(isChosen);
        option.setTextColor(getColor(isChosen ? R.color.button_green : R.color.text_black));
        option.setTypeface(null, isChosen ? Typeface.BOLD : Typeface.NORMAL);
    }

    private static void setNumber(EditText field, int value) {
        field.setText(value == ApartmentFilter.ANY ? "" : String.valueOf(value));
        field.setError(null);
    }

    private static int getNumber(EditText field) {
        return TextUtils.isEmpty(field.getText())
                ? ApartmentFilter.ANY
                : Integer.parseInt(field.getText().toString());
    }

    /** Both fields are optional, but if both are filled Max can't be lower than Min. */
    private boolean minNotAboveMax(EditText min, EditText max) {
        int lo = getNumber(min), hi = getNumber(max);
        if (lo != ApartmentFilter.ANY && hi != ApartmentFilter.ANY && hi < lo) {
            max.setError(getString(R.string.error_max_below_min));
            max.requestFocus();
            return false;
        }
        return true;
    }
}
