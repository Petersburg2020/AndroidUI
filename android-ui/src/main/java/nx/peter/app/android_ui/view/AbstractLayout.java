package nx.peter.app.android_ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public abstract class AbstractLayout<A extends AbstractLayout, V extends View> extends AbstractView<A> implements ILayout<A, V> {

  public AbstractLayout(Context c) {
    super(c);
  }
  
  public AbstractLayout(Context c, AttributeSet attrs) {
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

