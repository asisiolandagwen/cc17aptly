package com.aptly.app;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;

/**
 * Screen 11d: Apartment details (opened by tapping a card on the Dashboard).
 * Back arrow returns to the Dashboard; the heart turns red when tapped.
 */
public class ApartmentDetailsActivity extends Activity {

    static final String EXTRA_INDEX = "apartment_index";

    private Apartment apartment;
    private ImageView heart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment_details);
        Apartment.loadText(this);
        Screens.drawBehindStatusBar(this);

        apartment = Apartment.ALL[getIntent().getIntExtra(EXTRA_INDEX, 0)];

        ((ImageView) findViewById(R.id.img_photo)).setImageResource(apartment.photo);
        setText(R.id.txt_title, apartment.title());
        setText(R.id.txt_subtitle, apartment.subtitle());
        setText(R.id.txt_rating, String.format("%.2f", apartment.rating));
        showStars(R.id.stars_rating, apartment.fullStars());
        setText(R.id.txt_reviews, String.format("%02d", apartment.reviews));
        setText(R.id.txt_host, apartment.host);
        setText(R.id.txt_description, apartment.description());
        // Juan D.'s review: his own stars, the number, and his comment
        showStars(R.id.stars_review, apartment.reviewStars());
        setText(R.id.txt_review_score, getString(R.string.review_score, apartment.reviewStars()));
        setText(R.id.txt_review, apartment.reviewText());
        setText(R.id.txt_price, apartment.priceText());

        // Back arrow: return to the Dashboard
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Heart: tap to like (red) or unlike (outline)
        heart = findViewById(R.id.btn_heart);
        showHeart();
        heart.setOnClickListener(v -> {
            apartment.liked = !apartment.liked;
            showHeart();
        });

        // Message: open a chat with this apartment's host
        findViewById(R.id.btn_message).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra(ChatActivity.EXTRA_NAME, apartment.host);
            intent.putExtra(ChatActivity.EXTRA_SUBTITLE, getString(R.string.chat_host_subtitle, apartment.title()));
            startActivity(intent);
        });
    }

    private void showHeart() {
        if (apartment.liked) {
            heart.setImageResource(R.drawable.ic_heart_filled);
            heart.setImageTintList(null);
        } else {
            heart.setImageResource(R.drawable.ic_nav_favorites);
            heart.setImageTintList(ColorStateList.valueOf(0xFF111111));
        }
    }

    /** Lights up the first `full` stars of a 5-star row; the rest are faded. */
    private void showStars(int rowId, int full) {
        android.view.ViewGroup row = findViewById(rowId);
        for (int i = 0; i < row.getChildCount(); i++) {
            row.getChildAt(i).setAlpha(i < full ? 1f : 0.25f);
        }
    }

    private void setText(int id, String text) {
        ((TextView) findViewById(id)).setText(text);
    }
}
