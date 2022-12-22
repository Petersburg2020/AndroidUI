package nx.peter.app.android_ui.view.text;

import nx.peter.app.android_ui.view.MultiActionView;

public class TextSubscript extends Text {
	public TextSubscript(CharSequence text, int start) {
		super(text, Type.Subscript, start);
	}

	public TextSubscript(CharSequence text, MultiActionView view, int start) {
		super(text, Type.Subscript, view, start);
	}
}
