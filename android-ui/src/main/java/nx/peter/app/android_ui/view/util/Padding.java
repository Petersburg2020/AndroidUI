package nx.peter.app.android_ui.view.util;

public class Padding {
  public final int left, top, right, bottom;
  
  public Padding(int padding) {
      this(padding, padding);
  }
  
  public Padding(int horizontal, int vertical) {
      this(horizontal, vertical, horizontal, vertical);
  }

  public Padding(int left, int top, int right, int bottom) {
    this.left = left;
    this.top = top;
    this.right = right;
    this.bottom = bottom;
  }
}

