package nx.peter.app.android_ui.view.text;

import nx.peter.app.android_ui.view.MultiActionView;

public abstract class Text {
	public MultiActionView view;
	public final String text;
	public final Type type;
	public int start, end;
	
	public Text(CharSequence text, Type type, int start)  {
		this(text, type, null, start);
	}

	public Text(CharSequence text, Type type, MultiActionView view, int start) {
		this.text = text.toString();
		this.type = type;
		this.start = start;
		this.end = start + text.length();
		this.view = view;
	}

	public boolean equals(Text another) {
		return another != null ? another.start == start && another.text == text && another.type.equals(type) : false;
	}

	@Override
	public String toString() {
		return "<" + type + ">" + text + "(" + start + ", " + end + ")</" + type + ">";
	}
	
	public enum Type {
		Background,
		Bullet,
		Color, 
		Edit,
		Font, 
		Image,
		Link, 
		Size, 
		Subscript,
		Superscript,
		Underline,
		Url
	}
	
}
