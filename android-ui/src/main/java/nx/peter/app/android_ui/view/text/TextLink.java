package nx.peter.app.android_ui.view.text;

import nx.peter.app.android_ui.view.MultiActionView;

public class TextLink extends Text {
	public MultiActionView.OnLinkClickListener<?> link;
	
	public TextLink(CharSequence text, MultiActionView.OnLinkClickListener<?> link, int start) {
		this(text, null, link, start);
	}

	public TextLink(CharSequence text, MultiActionView<?> view, MultiActionView.OnLinkClickListener<?> link, int start) {
		super(text, Type.Link, view, start);
		this.link = link;
	}
	
}
