package nx.peter.app.android_ui.view.text;

import nx.peter.app.android_ui.view.StyledView;

public class TextUnderline extends Text {

	public TextUnderline(CharSequence text, int start) {
		super(text, Type.Underline, start);
	}
	
	public TextUnderline(CharSequence text, StyledView view, int start) {
		super(text, Type.Underline, view, start);
	}
	
}
