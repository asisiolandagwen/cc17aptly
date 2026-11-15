package com.aptly.app;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Screen 11a: FAVORITES tab: the apartments the user hearted (on the Dashboard or Details screen).
 * All 6 cards are in activity_favorites.xml; only the hearted ones are shown, 2 per row.
 * Tapping a card opens its details; tapping its heart removes it from Favorites.
 */
public class FavoritesActivity extends Activity {

    private static final int[] CARDS = {
            R.id.fav_card_1, R.id.fav_card_2, R.id.fav_card_3,
            R.id.fav_card_4, R.id.fav_card_5, R.id.fav_card_6};
    private static final int[] PHOTOS = {
            R.id.fav_photo_1, R.id.fav_photo_2, R.id.fav_photo_3,
            R.id.fav_photo_4, R.id.fav_photo_5, R.id.fav_photo_6};
    private static final int[] HEARTS = {
            R.id.fav_heart_1, R.id.fav_heart_2, R.id.fav_heart_3,
            R.id.fav_heart_4, R.id.fav_heart_5, R.id.fav_heart_6};
    private static final int[] PRICES = {
            R.id.fav_price_1, R.id.fav_price_2, R.id.fav_price_3,
            R.id.fav_price_4, R.id.fav_price_5, R.id.fav_price_6};

    /** The 3 card rows and their "empty right half" spacers in activity_favorites.xml. */
    private static final int[] ROWS = {R.id.fav_row_1, R.id.fav_row_2, R.id.fav_row_3};
    private static final int[] SPACERS = {R.id.fav_spacer_1, R.id.fav_spacer_2, R.id.fav_spacer_3};

    private final View[] cards = new View[CARDS.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Apartment.loadText(this);

        for (int i = 0; i < CARDS.length; i++) {
            final int index = i;
            cards[i] = findViewById(CARDS[i]);

            // Rounded photo corners (also on older phones)
            findViewById(PHOTOS[i]).setClipToOutline(true);

            // Price line, kept in sync with Apartment.java
            ((TextView) findViewById(PRICES[i])).setText(Apartment.ALL[i].summary());

            // Tap the card: open its details
            cards[i].setOnClickListener(v -> {
                Intent intent = new Intent(this, ApartmentDetailsActivity.class);
                intent.putExtra(ApartmentDetailsActivity.EXTRA_INDEX, index);
                startActivity(intent);
            });

            // Tap the heart: unlike, so it leaves Favorites
            findViewById(HEARTS[i]).setOnClickListener(v -> {
                Apartment.ALL[index].liked = !Apartment.ALL[index].liked;
                showFavorites();
            });
        }

        // Bottom navigation
        findViewById(R.id.nav_explore).setOnClickListener(v -> openTab(MainActivity.class));
        findViewById(R.id.nav_messages).setOnClickListener(v -> openTab(MessagesActivity.class));
        findViewById(R.id.nav_profile).setOnClickListener(v -> openTab(ProfileActivity.class));
    }

    /** Hearts may have changed on another screen, so refresh every time the tab is shown. */
    @Override
    protected void onResume() {
        super.onResume();
        showFavorites();
    }

    /** Puts the hearted cards back into the grid, 2 per row, or shows the "No favorites" message. */
    private void showFavorites() {
        List<View> hearted = new ArrayList<>();
        for (int i = 0; i < cards.length; i++) {
            showHeart(i);
            if (Apartment.ALL[i].liked) hearted.add(cards[i]);
        }

        // Take the cards out of their rows (the rows and spacers stay)
        for (View card : cards) {
            ViewGroup parent = (ViewGroup) card.getParent();
            if (parent != null) parent.removeView(card);
        }

        // Fill row 1, then row 2, then row 3 (all in activity_favorites.xml) with the hearted cards
        for (int r = 0; r < ROWS.length; r++) {
            LinearLayout row = findViewById(ROWS[r]);
            int first = r * 2, second = r * 2 + 1;
            boolean hasFirst = first < hearted.size();
            boolean hasSecond = second < hearted.size();

            if (hasFirst) row.addView(hearted.get(first), 0, cardParams(false));
            if (hasSecond) row.addView(hearted.get(second), 1, cardParams(true));

            row.setVisibility(hasFirst ? View.VISIBLE : View.GONE);
            // Lone card: show the empty right half so the card stays half-width
            findViewById(SPACERS[r]).setVisibility(hasFirst && !hasSecond ? View.VISIBLE : View.GONE);
        }

        findViewById(R.id.txt_no_favorites)
                .setVisibility(hearted.isEmpty() ? View.VISIBLE : View.GONE);
    }

    private void showHeart(int index) {
        ImageView heart = findViewById(HEARTS[index]);
        if (Apartment.ALL[index].liked) {
            heart.setImageResource(R.drawable.ic_heart_filled);
            heart.setImageTintList(null);
        } else {
            heart.setImageResource(R.drawable.ic_nav_favorites);
            heart.setImageTintList(ColorStateList.valueOf(0xFF222222));
        }
    }

    private LinearLayout.LayoutParams cardParams(boolean second) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        if (second) p.setMarginStart(dp(16));
        return p;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    /** Switches to another tab without an animation. */
    private void openTab(Class<?> screen) {
        Intent intent = new Intent(this, screen);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
