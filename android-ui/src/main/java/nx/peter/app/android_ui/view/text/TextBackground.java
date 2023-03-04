package nx.peter.app.android_ui.view.text;

import nx.peter.app.android_ui.view.StyledView;

public class TextBackground extends Text {
	public int color;

	public TextBackground(CharSequence text, int color, int start) {
		this(text, null, color, start);
	}

	public TextBackground(CharSequence text, StyledView view, int color, int start) {
		super(text, Type.Background, view, start);
		this.color = color;
	}
}
