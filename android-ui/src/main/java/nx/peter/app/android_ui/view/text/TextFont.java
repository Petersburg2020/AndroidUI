package nx.peter.app.android_ui.view.text;

import nx.peter.app.android_ui.view.MultiActionView;

public class TextFont extends Text {
    public Font font;

    public TextFont(CharSequence text, Font font, int start) {
        this(text, null, font, start);
    }

    public TextFont(CharSequence text, MultiActionView view, Font font, int start) {
        super(text, Type.Font, view, start);
        this.font = font;
    }

}
