package nx.peter.app.android_ui.view.text;

import nx.peter.app.android_ui.view.StyledView;

public class TextStrikeThrough extends Text {
	public int color;
	
	public TextStrikeThrough(CharSequence text, int color, int start) {
		this(text, null, color, start);
	}

	public TextStrikeThrough(CharSequence text, StyledView<?> view, int color, int start) {
		super(text, Type.Strikethrough, view, start);
		this.color = color;
	}
}
