package nx.peter.app.android_ui.view.text;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import nx.peter.app.android_ui.view.StyledView;

public class TextImage extends Text {
	public Drawable image;
	public float size;

	public TextImage(CharSequence text, @NonNull Context context, int image, float size, int start) {
		super(text, Type.Image, start);
		this.size = size;
		this.image = context.getDrawable(image);
	}

	public TextImage(CharSequence text, Context context, @DrawableRes int image, @ColorInt int tint, float size, int start) {
		this(text, context, image, size, start);
		if (this.image != null)
			this.image.setColorFilter(tint, PorterDuff.Mode.SRC_ATOP);
	}

	public TextImage(CharSequence text, StyledView view, int image, @ColorInt int tint, float size, int start) {
		super(text, Type.Image, view, start);
		this.size = size;
		this.image = view.getContext().getDrawable(image);
		if (this.image != null)
			this.image.setColorFilter(tint, PorterDuff.Mode.SRC_ATOP);
	}
}
