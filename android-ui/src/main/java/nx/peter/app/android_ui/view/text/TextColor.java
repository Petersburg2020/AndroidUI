package nx.peter.app.android_ui.view.text;

import nx.peter.app.android_ui.view.StyledView;

public class TextColor extends Text {
	public int color;
	
	public TextColor(CharSequence text, int color, int start) {
		this(text, null, color, start);
	}

	public TextColor(CharSequence text, StyledView view, int color, int start) {
		super(text, Type.Color, view, start);
		this.color = color;
	}
}
