package com.aptly.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

/**
 * Screen 11e: MESSAGES tab.
 * The conversation rows are in activity_messages.xml; the search box hides rows whose name
 * doesn't match. Tapping a row opens the Chat. Chats started later (e.g. with an apartment
 * host) get a new row from item_conversation.xml, added at the top.
 */
public class MessagesActivity extends Activity {

    private LinearLayout list;
    private EditText search;
    private TextView noResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        Conversation.init(this);

        list = findViewById(R.id.list_conversations);
        search = findViewById(R.id.input_search);
        noResults = findViewById(R.id.txt_no_results);

        // Search: update the list on every letter typed
        search.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) { }
            @Override public void afterTextChanged(Editable s) { showConversations(); }
        });

        // Bottom navigation
        findViewById(R.id.nav_explore).setOnClickListener(v -> openTab(MainActivity.class));
        findViewById(R.id.nav_favorites).setOnClickListener(v -> openTab(FavoritesActivity.class));
        findViewById(R.id.nav_profile).setOnClickListener(v -> openTab(ProfileActivity.class));
    }

    /** Refresh when coming back from a chat, so the last message and date are up to date. */
    @Override
    protected void onResume() {
        super.onResume();
        showConversations();
    }

    /** Fills every row and shows only those whose name contains the search text. */
    private void showConversations() {
        String query = search.getText().toString().trim().toLowerCase(Locale.ROOT);
        int shown = 0;

        for (int i = 0; i < Conversation.ALL.size(); i++) {
            Conversation c = Conversation.ALL.get(i);
            View row = rowFor(c, i);

            ((TextView) row.findViewById(R.id.txt_name)).setText(c.name);
            ((TextView) row.findViewById(R.id.txt_date)).setText(c.date);
            ((TextView) row.findViewById(R.id.txt_last_message)).setText(c.lastMessage());
            c.showAvatar(row.findViewById(R.id.txt_avatar));
            row.setOnClickListener(v -> openChat(c));

            boolean matches = c.name.toLowerCase(Locale.ROOT).contains(query);
            row.setVisibility(matches ? View.VISIBLE : View.GONE);
            if (matches) shown++;
        }

        noResults.setText(getString(R.string.no_names_match, search.getText().toString().trim()));
        noResults.setVisibility(shown == 0 ? View.VISIBLE : View.GONE);
    }

    /**
     * The row for a conversation: the one in the XML, or a row from item_conversation.xml
     * (created once, at the conversation's position in the list).
     */
    private View rowFor(Conversation c, int position) {
        if (c.rowId != 0) return findViewById(c.rowId);

        View row = list.findViewWithTag(c.name);
        if (row == null) {
            row = getLayoutInflater().inflate(R.layout.item_conversation, list, false);
            row.setTag(c.name);
            list.addView(row, position);
        }
        return row;
    }

    private void openChat(Conversation c) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(ChatActivity.EXTRA_NAME, c.name);
        startActivity(intent);
    }

    /** Switches to another tab without an animation. */
    private void openTab(Class<?> screen) {
        Intent intent = new Intent(this, screen);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
