package com.aptly.app;

import android.content.Context;

import java.util.Arrays;
import java.util.List;

/**
 * One apartment listing: what the Dashboard card, Filters and Details screen show.
 * The numbers are below; all the text (area, host, description, review, title, price line)
 * is in res/values/apartments.xml and is loaded by loadText().
 */
final class Apartment {

    // Furnishing types (same text as the Filters options)
    static final String UNFURNISHED = "Unfurnished";
    static final String SEMI_FURNISHED = "Semi-furnished";
    static final String FULLY_FURNISHED = "Fully furnished";

    // Lease types (same order as the Filters checkboxes)
    static final String SHORT_TERM = "Short-term";
    static final String LONG_TERM = "Long-term";
    static final String MONTH_TO_MONTH = "Month-to-Month";
    static final String FIXED_TERM = "Fixed-term";
    static final String LEASE_TO_OWN = "Lease-to-Own";

    final int photo;            // drawable
    final double rating;
    final int reviews;
    final int reviewStars;      // stars (1–5) Juan D. gave in his guest review

    final int price;            // monthly rent in pesos
    final int bedrooms;
    final String furnishing;
    final boolean petsAllowed;
    final boolean hasParking;
    final List<String> leaseTypes;

    // Text from apartments.xml (filled by loadText)
    String area;                // e.g. "Bayan Park"
    String host;                // e.g. "Mommy Oni"
    private String title, subtitle, priceText, summary, description, reviewText;

    /** Heart state, shared by the Dashboard, Favorites and the Details screen. */
    boolean liked;

    private Apartment(int photo, double rating, int reviews, int reviewStars,
                      int price, int bedrooms, String furnishing, boolean petsAllowed,
                      boolean hasParking, String... leaseTypes) {
        this.photo = photo;
        this.rating = rating;
        this.reviews = reviews;
        this.reviewStars = reviewStars;
        this.price = price;
        this.bedrooms = bedrooms;
        this.furnishing = furnishing;
        this.petsAllowed = petsAllowed;
        this.hasParking = hasParking;
        this.leaseTypes = Arrays.asList(leaseTypes);
    }

    /** The 6 Dashboard apartments, in the same order as card_1 ... card_6 and apartments.xml. */
    static final Apartment[] ALL = {
            // photo, rating, reviews, Juan's stars, price, bedrooms, furnishing, pets, parking, leases
            new Apartment(R.drawable.apt_1, 5.00, 12, 5,                     // Aurora Hill
                    8500, 1, SEMI_FURNISHED, true, false, LONG_TERM, MONTH_TO_MONTH),
            new Apartment(R.drawable.apt_2, 4.75, 8, 5,                      // Trancoville
                    12000, 2, FULLY_FURNISHED, false, true, LONG_TERM, FIXED_TERM),
            new Apartment(R.drawable.apt_3, 4.50, 15, 4,                     // Leonila Hill
                    6000, 1, UNFURNISHED, true, false, SHORT_TERM, MONTH_TO_MONTH),
            new Apartment(R.drawable.apt_4, 2.50, 4, 3,                      // Bayan Park
                    15000, 3, FULLY_FURNISHED, true, true, LONG_TERM, LEASE_TO_OWN),
            new Apartment(R.drawable.apt_5, 3.00, 6, 3,                      // La Trinidad
                    7000, 2, UNFURNISHED, false, true, SHORT_TERM, FIXED_TERM),
            new Apartment(R.drawable.apt_6, 4.00, 10, 4,                     // Gov. Pack
                    10000, 2, SEMI_FURNISHED, true, false, MONTH_TO_MONTH, FIXED_TERM),
    };

    private static boolean textLoaded;

    /** Reads every apartment's text from apartments.xml (once). Call before using the text. */
    static void loadText(Context context) {
        if (textLoaded) return;
        String[] areas = context.getResources().getStringArray(R.array.apartment_areas);
        String[] hosts = context.getResources().getStringArray(R.array.apartment_hosts);
        String[] descriptions = context.getResources().getStringArray(R.array.apartment_descriptions);
        String[] reviewTexts = context.getResources().getStringArray(R.array.apartment_reviews);

        for (int i = 0; i < ALL.length; i++) {
            Apartment a = ALL[i];
            a.area = areas[i];
            a.host = hosts[i];
            a.description = descriptions[i];
            a.reviewText = reviewTexts[i];
            a.title = context.getString(R.string.apartment_title, a.area);
            a.subtitle = context.getString(R.string.apartment_subtitle, a.host, a.area);
            a.priceText = context.getString(R.string.apartment_price, a.price);
            String bedroomsText = context.getResources()
                    .getQuantityString(R.plurals.apartment_bedrooms, a.bedrooms, a.bedrooms);
            a.summary = context.getString(R.string.apartment_summary, a.priceText, bedroomsText);
        }
        textLoaded = true;
    }

    /** e.g. "Apartment in Bayan Park" */
    String title() { return title; }

    /** e.g. "Mommy Oni, Homes, Bayan Park" */
    String subtitle() { return subtitle; }

    /** e.g. "₱15,000" */
    String priceText() { return priceText; }

    /** Line under each card, e.g. "₱15,000/mo · 3 bedrooms" */
    String summary() { return summary; }

    String description() { return description; }

    /** Juan D.'s comment */
    String reviewText() { return reviewText; }

    int reviewStars() { return reviewStars; }

    /** Number of bright stars out of 5, e.g. 4.5 → 5, 2.5 → 3 (rounded to the nearest whole star). */
    int fullStars() {
        return (int) Math.round(rating);
    }
}
