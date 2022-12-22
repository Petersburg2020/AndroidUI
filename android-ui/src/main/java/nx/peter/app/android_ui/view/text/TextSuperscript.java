package nx.peter.app.android_ui.view.text;

import nx.peter.app.android_ui.view.MultiActionView;

public class TextSuperscript extends Text {
	public TextSuperscript(CharSequence text, int start) {
		super(text, Type.Superscript, start);
	}
	
	public TextSuperscript(CharSequence text, MultiActionView view, int start) {
		super(text, Type.Superscript, view, start);
	}
}
