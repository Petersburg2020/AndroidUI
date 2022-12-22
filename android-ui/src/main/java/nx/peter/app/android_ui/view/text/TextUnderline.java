package nx.peter.app.android_ui.view.text;

import nx.peter.app.android_ui.view.MultiActionView;

public class TextUnderline extends Text {

	public TextUnderline(CharSequence text, int start) {
		super(text, Type.Underline, start);
	}
	
	public TextUnderline(CharSequence text, MultiActionView view, int start) {
		super(text, Type.Underline, view, start);
	}
	
}
