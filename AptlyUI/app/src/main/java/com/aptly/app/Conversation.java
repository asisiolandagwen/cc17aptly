package com.aptly.app;

import android.content.Context;
import android.content.res.ColorStateList;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** One chat on the Messages tab (shared by the Messages list and the Chat screen). */
final class Conversation {

    /** One chat bubble. */
    static final class Message {
        final String text;
        final boolean fromMe;   // true = sent by the user (green bubble on the right)

        Message(String text, boolean fromMe) {
            this.text = text;
            this.fromMe = fromMe;
        }
    }

    final String name;
    final int avatarColor;      // color of the letter circle
    /** The row for this chat written in activity_messages.xml, or 0 for chats started later. */
    final int rowId;
    String date;
    final List<Message> messages = new ArrayList<>();

    private Conversation(String name, int avatarColor, int rowId, String date, String firstMessage) {
        this.name = name;
        this.avatarColor = avatarColor;
        this.rowId = rowId;
        this.date = date;
        if (firstMessage != null) messages.add(new Message(firstMessage, true));
    }

    /**
     * Conversations shown on the Messages tab (newest chats are added at the top).
     * Filled by init() from strings.xml the first time the Messages tab or a chat opens.
     */
    static List<Conversation> ALL;

    /** Creates the two starting conversations, using the names and message in strings.xml. */
    static void init(Context context) {
        if (ALL != null) return;
        String date = context.getString(R.string.chat_start_date);
        String first = context.getString(R.string.chat_first_message);
        ALL = new ArrayList<>(Arrays.asList(
                new Conversation(context.getString(R.string.chat_banana_man), 0xFFF2C94C,
                        R.id.row_banana_man, date, first),
                new Conversation(context.getString(R.string.chat_strawberry_girl), 0xFFE57373,
                        R.id.row_strawberry_girl, date, first)
        ));
    }

    /** Hosts that have no chat yet; kept so reopening the chat before sending reuses it. */
    private static final List<Conversation> NOT_STARTED = new ArrayList<>();

    /** Sage green for host avatars. */
    private static final int HOST_COLOR = 0xFF9CAF88;

    /** Finds the chat with this name, or makes a new empty one (not listed until a message is sent). */
    static Conversation find(String name) {
        for (Conversation c : ALL) if (c.name.equals(name)) return c;
        for (Conversation c : NOT_STARTED) if (c.name.equals(name)) return c;
        Conversation c = new Conversation(name, HOST_COLOR, 0, "", null);
        NOT_STARTED.add(c);
        return c;
    }

    /** Puts a chat on the Messages tab (at the top) the first time a message is sent in it. */
    void markStarted() {
        if (!ALL.contains(this)) {
            NOT_STARTED.remove(this);
            ALL.add(0, this);
        }
    }

    /** Text under the name in the Messages list. */
    String lastMessage() {
        return messages.isEmpty() ? "" : messages.get(messages.size() - 1).text;
    }

    /** Fills a letter avatar (style Aptly.AvatarLetter) with this person's letter and color. */
    void showAvatar(TextView avatar) {
        avatar.setText(name.substring(0, 1));
        avatar.setBackgroundTintList(ColorStateList.valueOf(avatarColor));
    }
}
