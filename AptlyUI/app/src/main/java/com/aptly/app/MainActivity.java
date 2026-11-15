package com.aptly.app;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Screen 11: DASHBOARD (Explore tab).
 * Search bar, filter button, apartment cards and the bottom navigation.
 * The filter button opens the Filters screen; its "See results" shows only matching cards here.
 */
public class MainActivity extends Activity {

    private static final int REQUEST_FILTERS = 1;

    private static final int[] CARDS = {
            R.id.card_1, R.id.card_2, R.id.card_3, R.id.card_4, R.id.card_5, R.id.card_6};
    private static final int[] PRICES = {
            R.id.price_1, R.id.price_2, R.id.price_3, R.id.price_4, R.id.price_5, R.id.price_6};
    private static final int[] PHOTOS = {
            R.id.photo_1, R.id.photo_2, R.id.photo_3, R.id.photo_4, R.id.photo_5, R.id.photo_6};
    private static final int[] HEARTS = {
            R.id.heart_1, R.id.heart_2, R.id.heart_3, R.id.heart_4, R.id.heart_5, R.id.heart_6};

    /** The 3 card rows and their "empty right half" spacers in activity_main.xml. */
    private static final int[] ROWS = {R.id.card_row_1, R.id.card_row_2, R.id.card_row_3};
    private static final int[] SPACERS = {R.id.spacer_1, R.id.spacer_2, R.id.spacer_3};

    private final View[] cards = new View[CARDS.length];
    private ApartmentFilter filter = new ApartmentFilter();
    private EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Apartment.loadText(this);

        for (int i = 0; i < CARDS.length; i++) {
            LinearLayout card = findViewById(CARDS[i]);
            cards[i] = card;

            // Price line under each card (in activity_main.xml); filled from Apartment.java
            // so it always matches what the Filters screen checks
            ((TextView) findViewById(PRICES[i])).setText(Apartment.ALL[i].summary());

            // Rounded photo corners (also on older phones)
            findViewById(PHOTOS[i]).setClipToOutline(true);
        }

        for (int i = 0; i < CARDS.length; i++) {
            final int index = i;

            // Tapping the photo or text opens the apartment's details
            cards[i].setOnClickListener(v -> {
                Intent intent = new Intent(this, ApartmentDetailsActivity.class);
                intent.putExtra(ApartmentDetailsActivity.EXTRA_INDEX, index);
                startActivity(intent);
            });

            // Heart: tap to like (red) or unlike (outline)
            findViewById(HEARTS[i]).setOnClickListener(v -> {
                Apartment.ALL[index].liked = !Apartment.ALL[index].liked;
                showHeart(index);
            });
        }

        // Filter button: opens the Filters screen with the current filters filled in
        findViewById(R.id.btn_filter).setOnClickListener(v -> {
            Intent intent = new Intent(this, FiltersActivity.class);
            filter.putInto(intent);
            startActivityForResult(intent, REQUEST_FILTERS);
        });

        // Search box: show only apartments whose name, area or host contains the text, as you type
        search = findViewById(R.id.input_search);
        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void afterTextChanged(Editable s) { showResults(); }
        });
        // Keyboard's Search key: just close the keyboard (results are already shown)
        search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyboard();
                return true;
            }
            return false;
        });

        // "Clear filters" in the results bar: clear the search and filters, show every apartment
        findViewById(R.id.btn_clear_filters).setOnClickListener(v -> {
            filter = new ApartmentFilter();
            search.setText("");   // also calls showResults()
            hideKeyboard();
            showResults();
        });

        // Bottom navigation
        findViewById(R.id.nav_favorites).setOnClickListener(v -> openTab(FavoritesActivity.class));
        findViewById(R.id.nav_messages).setOnClickListener(v -> openTab(MessagesActivity.class));
        findViewById(R.id.nav_profile).setOnClickListener(v -> openTab(ProfileActivity.class));
    }

    /** Refresh hearts in case one was changed on the Details screen. */
    @Override
    protected void onResume() {
        super.onResume();
        for (int i = 0; i < HEARTS.length; i++) showHeart(i);
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

    /** "See results" on the Filters screen comes back here. */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FILTERS && resultCode == RESULT_OK) {
            filter = ApartmentFilter.from(data);
            showResults();
        }
    }

    /**
     * Shows only the apartments that match the search text AND the filters, 2 per row, using
     * the 3 rows and spacers written in activity_main.xml (cards are moved up so there are no gaps).
     */
    private void showResults() {
        String query = search.getText().toString().trim().toLowerCase(Locale.ROOT);

        List<View> matching = new ArrayList<>();
        for (int i = 0; i < cards.length; i++) {
            Apartment a = Apartment.ALL[i];
            if (filter.matches(a) && matchesSearch(a, query)) matching.add(cards[i]);
        }

        // Take the cards out of their rows (the rows and spacers stay)
        for (View card : cards) {
            ViewGroup parent = (ViewGroup) card.getParent();
            if (parent != null) parent.removeView(card);
        }

        // Fill row 1, then row 2, then row 3 with the matching cards
        for (int r = 0; r < ROWS.length; r++) {
            LinearLayout row = findViewById(ROWS[r]);
            int first = r * 2, second = r * 2 + 1;
            boolean hasFirst = first < matching.size();
            boolean hasSecond = second < matching.size();

            if (hasFirst) row.addView(matching.get(first), 0, cardParams(false));
            if (hasSecond) row.addView(matching.get(second), 1, cardParams(true));

            row.setVisibility(hasFirst ? View.VISIBLE : View.GONE);
            // Lone card: show the empty right half so the card stays half-width
            findViewById(SPACERS[r]).setVisibility(hasFirst && !hasSecond ? View.VISIBLE : View.GONE);
        }

        // "No apartments match" message (in activity_main.xml) when nothing matches
        findViewById(R.id.txt_no_results)
                .setVisibility(matching.isEmpty() ? View.VISIBLE : View.GONE);

        // Results bar, e.g. "3 results" (wording in strings.xml): shown while searching or filtering
        View bar = findViewById(R.id.results_bar);
        bar.setVisibility(filter.isEmpty() && query.isEmpty() ? View.GONE : View.VISIBLE);
        TextView count = findViewById(R.id.results_count);
        int n = matching.size();
        count.setText(getResources().getQuantityString(R.plurals.results_count, n, n));
    }

    /** True if the search box is empty, or the apartment's name, area or host contains the text. */
    private static boolean matchesSearch(Apartment a, String query) {
        if (query.isEmpty()) return true;
        return a.title().toLowerCase(Locale.ROOT).contains(query)
                || a.host.toLowerCase(Locale.ROOT).contains(query);
    }

    private void hideKeyboard() {
        InputMethodManager keyboard = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(search.getWindowToken(), 0);
        search.clearFocus();
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
