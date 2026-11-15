package com.aptly.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Screen 11f: Chat with one person (opened from the Messages tab).
 * The user types in the "Message" box and sends with the send arrow or the keyboard's Send key.
 */
public class ChatActivity extends Activity {

    /** Name of the person to chat with (a Messages contact or an apartment host). */
    static final String EXTRA_NAME = "conversation_name";
    /** Optional line under the name, e.g. "Host · Apartment in Bayan Park". */
    static final String EXTRA_SUBTITLE = "conversation_subtitle";

    private Conversation conversation;
    private LinearLayout list;
    private ScrollView scroll;
    private EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Conversation.init(this);

        conversation = Conversation.find(getIntent().getStringExtra(EXTRA_NAME));

        conversation.showAvatar(findViewById(R.id.txt_avatar));
        ((TextView) findViewById(R.id.txt_name)).setText(conversation.name);

        String subtitle = getIntent().getStringExtra(EXTRA_SUBTITLE);
        TextView subtitleView = findViewById(R.id.txt_chat_subtitle);
        if (subtitle != null) {
            subtitleView.setText(subtitle);
            subtitleView.setVisibility(android.view.View.VISIBLE);
        }

        list = findViewById(R.id.list_messages);
        scroll = findViewById(R.id.scroll_messages);
        input = findViewById(R.id.input_message);

        // Replace the sample bubble from the XML with this chat's real messages
        list.removeAllViews();
        for (Conversation.Message m : conversation.messages) addBubble(m);
        scrollToBottom();

        // Back arrow: return to the Messages list
        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        // Send with the arrow button or the keyboard's Send key
        findViewById(R.id.btn_send).setOnClickListener(v -> send());
        input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                send();
                return true;
            }
            return false;
        });
    }

    private void send() {
        String text = input.getText().toString().trim();
        if (TextUtils.isEmpty(text)) return;

        Conversation.Message message = new Conversation.Message(text, true);
        conversation.messages.add(message);
        conversation.markStarted();   // a new host chat now appears on the Messages tab
        conversation.date = new SimpleDateFormat("MM/dd/yyyy", Locale.US).format(new Date());

        addBubble(message);
        input.setText("");
        scrollToBottom();
    }

    /**
     * Adds one bubble using the Aptly.ChatBubble style (same look as the bubble in the XML):
     * green on the right for the user, gray on the left for the other person.
     */
    private void addBubble(Conversation.Message m) {
        TextView bubble = new TextView(this, null, 0, R.style.Aptly_ChatBubble);
        bubble.setText(m.text);
        if (!m.fromMe) bubble.setBackgroundResource(R.drawable.bg_chat_bubble_other);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        p.gravity = m.fromMe ? Gravity.END : Gravity.START;
        p.topMargin = dp(8);
        list.addView(bubble, p);
    }

    private void scrollToBottom() {
        scroll.post(() -> scroll.fullScroll(ScrollView.FOCUS_DOWN));
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }
}
