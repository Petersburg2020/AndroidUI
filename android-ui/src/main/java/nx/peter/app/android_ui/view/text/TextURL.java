package nx.peter.app.android_ui.view.text;

import nx.peter.app.android_ui.view.MultiActionView;

public class TextURL extends Text {
	public String url;

	public TextURL(CharSequence text, CharSequence url, int start){
		this(text, null, url, start);
	}

	public TextURL(CharSequence text, MultiActionView view, CharSequence url, int start) {
		super(text, Type.Url, view, start);
		this.url = url.toString();
	}
}
