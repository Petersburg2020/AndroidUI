package nx.peter.app.android_ui.view.util;

import androidx.annotation.NonNull;

public class Size {
	public final int width, height;

	public Size(int width, int height) {
		this.width = width;
		this.height = height;
	}

	@NonNull
	@Override
	public String toString() {
		return "width: " + width + ", height: " + height;
	}
	
}
