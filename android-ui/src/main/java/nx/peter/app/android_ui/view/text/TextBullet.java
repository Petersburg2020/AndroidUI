package nx.peter.app.android_ui.view.text;

import nx.peter.app.android_ui.view.StyledView;

public class TextBullet extends Text {
	public int radius, gap, color;
	
	public TextBullet(CharSequence text, int start) {
		this(text, null, -1, -1, -1, start);
	}
	
	
	public TextBullet(CharSequence text, int gap, int color, int radius, int start) {
		this(text, null, gap, color, radius, start);
	}

	public TextBullet(CharSequence text, StyledView view, int gap, int color, int radius, int start) {
		super(text, Type.Bullet, view, start);
		this.radius = radius;
		this.gap = gap;
		this.color = color;
	}
}
