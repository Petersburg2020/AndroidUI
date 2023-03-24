package nx.peter.app.android_ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

abstract class ALayout<A extends ALayout, V extends View> extends AView<A> implements ILayout<A, V> {

  public ALayout(Context c) {
    super(c);
  }
  
  public ALayout(Context c, AttributeSet attrs) {
      super(c, attrs);
  }
  
  @Override
  protected void init(AttributeSet attrs) {}
  
  
  @Override
  public void addChild(V view) {
      addView(view);
  }
  
  @Override
  public V getChildAt(int position) {
      return (V) super.getChildAt(position);
  }
  
  public void addChild(V child, int index) {
      addView(child, index);
  }
  
  public void addChild(V child, LayoutDetails details) {
      addView(child, details);
  }
  
  @Override
  public LayoutDetails getLayoutParams() {
      return new LayoutDetails(super.getLayoutParams());
  }
  
}

