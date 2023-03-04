package nx.peter.app.android_ui.view.text;

import nx.peter.app.android_ui.view.StyledView;

public class TextLink extends Text {
	public StyledView.OnLinkClickListener<?> link;
	
	public TextLink(CharSequence text, StyledView.OnLinkClickListener<?> link, int start) {
		this(text, null, link, start);
	}

	public TextLink(CharSequence text, StyledView<?> view, StyledView.OnLinkClickListener<?> link, int start) {
		super(text, Type.Link, view, start);
		this.link = link;
	}
	
}
