package nx.peter.app.android_ui;

import android.app.Activity;
import nx.peter.app.android_ui.view.text.Font;

public class AndroidUI {
    AndroidUI() {}

    public static void init(Activity activity) {
        Font.init(activity);
    }
}
