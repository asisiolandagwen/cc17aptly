package com.aptly.app;

import android.app.Activity;
import android.view.View;

/** Small helper shared by the screens. */
final class Screens {

    private Screens() { }

    /** Lets the layout fill the whole screen, behind a transparent status bar (dark icons). */
    static void drawBehindStatusBar(Activity activity) {
        activity.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
}
