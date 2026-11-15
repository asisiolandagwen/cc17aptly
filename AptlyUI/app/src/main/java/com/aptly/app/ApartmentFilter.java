package com.aptly.app;

import android.content.Intent;

import java.util.ArrayList;

/**
 * The choices made on the Filters screen. Empty fields mean "any".
 * Passed between the Dashboard and the Filters screen inside an Intent.
 */
final class ApartmentFilter {

    static final int ANY = -1;

    int priceMin = ANY, priceMax = ANY;
    int bedroomsMin = ANY, bedroomsMax = ANY;
    String furnishing;                 // null = any
    Boolean petsAllowed;               // null = any
    boolean needsParking;
    ArrayList<String> leaseTypes = new ArrayList<>();   // empty = any

    /** True when nothing is set, so every apartment shows. */
    boolean isEmpty() {
        return priceMin == ANY && priceMax == ANY && bedroomsMin == ANY && bedroomsMax == ANY
                && furnishing == null && petsAllowed == null && !needsParking && leaseTypes.isEmpty();
    }

    boolean matches(Apartment a) {
        if (priceMin != ANY && a.price < priceMin) return false;
        if (priceMax != ANY && a.price > priceMax) return false;
        if (bedroomsMin != ANY && a.bedrooms < bedroomsMin) return false;
        if (bedroomsMax != ANY && a.bedrooms > bedroomsMax) return false;
        if (furnishing != null && !furnishing.equals(a.furnishing)) return false;
        if (petsAllowed != null && petsAllowed != a.petsAllowed) return false;
        if (needsParking && !a.hasParking) return false;
        if (!leaseTypes.isEmpty()) {
            // Show the apartment if it offers at least one of the ticked lease types
            boolean any = false;
            for (String type : leaseTypes) {
                if (a.leaseTypes.contains(type)) { any = true; break; }
            }
            if (!any) return false;
        }
        return true;
    }

    // ----- Saving to / reading from an Intent -----

    void putInto(Intent intent) {
        intent.putExtra("price_min", priceMin);
        intent.putExtra("price_max", priceMax);
        intent.putExtra("bedrooms_min", bedroomsMin);
        intent.putExtra("bedrooms_max", bedroomsMax);
        intent.putExtra("furnishing", furnishing);
        intent.putExtra("pets", petsAllowed == null ? null : petsAllowed ? "allowed" : "not_allowed");
        intent.putExtra("parking", needsParking);
        intent.putStringArrayListExtra("lease_types", leaseTypes);
    }

    static ApartmentFilter from(Intent intent) {
        ApartmentFilter f = new ApartmentFilter();
        if (intent == null) return f;
        f.priceMin = intent.getIntExtra("price_min", ANY);
        f.priceMax = intent.getIntExtra("price_max", ANY);
        f.bedroomsMin = intent.getIntExtra("bedrooms_min", ANY);
        f.bedroomsMax = intent.getIntExtra("bedrooms_max", ANY);
        f.furnishing = intent.getStringExtra("furnishing");
        String pets = intent.getStringExtra("pets");
        f.petsAllowed = pets == null ? null : pets.equals("allowed");
        f.needsParking = intent.getBooleanExtra("parking", false);
        ArrayList<String> lease = intent.getStringArrayListExtra("lease_types");
        if (lease != null) f.leaseTypes = lease;
        return f;
    }
}
