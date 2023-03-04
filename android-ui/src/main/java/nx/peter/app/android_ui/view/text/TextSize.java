package nx.peter.app.android_ui.view.text;

import nx.peter.app.android_ui.view.StyledView;

public class TextSize extends Text {
	public int size;

	public TextSize(CharSequence text, int size, int start) {
		this(text, null, size, start);
	}

	public TextSize(CharSequence text, StyledView<?> view, int size, int start) {
		super(text, Type.Size, view, start);
		this.size = size;
	}
}
