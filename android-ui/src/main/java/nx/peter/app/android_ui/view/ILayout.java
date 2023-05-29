package nx.peter.app.android_ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.IdRes;

public interface ILayout<I extends View, V extends View> extends IView<I> {
	void addChild(V child);
	void addChild(V child, int index);
	void addChild(V child, LayoutDetails details);
	V getChildAt(int index);
	V findChildById(@IdRes int id);
	LayoutDetails getLayoutParams();
	
	class LayoutDetails extends AView.LayoutParams {
		public LayoutDetails(int width, int height) {
			super(width, height);
		}
		
		public LayoutDetails(Context c, AttributeSet attrs) {
			super(c, attrs);
		}
		
		public LayoutDetails(ViewGroup.LayoutParams params) {
			super(params);
		}
	}

}
