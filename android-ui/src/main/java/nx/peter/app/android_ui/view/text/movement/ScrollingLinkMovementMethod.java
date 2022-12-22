package nx.peter.app.android_ui.view.text.movement;

// import android.compat.annotation.UnsupportedAppUsage;

import android.text.Layout;
import android.text.NoCopySpan;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.text.method.Touch;
import android.text.style.ClickableSpan;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.textclassifier.TextLinks.TextLinkSpan;
import android.widget.TextView;

public class ScrollingLinkMovementMethod extends AbstractMovementMethod {
  private static final int CLICK = 1;
  private static final int UP = 2;
  private static final int DOWN = 3;
  private static final int AMOUNT = 5;
  private static final int HIDE_FLOATING_TOOLBAR_DELAY_MS = 200;

  @Override
  public boolean canSelectArbitrarily() {
    return true;
  }

  @Override
  protected boolean handleMovementKey(TextView widget, Spannable buffer, int keyCode, int movementMetaState, KeyEvent event) {
    switch (keyCode) {
      case KeyEvent.KEYCODE_DPAD_CENTER:
      case KeyEvent.KEYCODE_ENTER:
        if (KeyEvent.metaStateHasNoModifiers(movementMetaState)) {
          if (event.getAction() == KeyEvent.ACTION_DOWN
              && event.getRepeatCount() == 0
              && action(CLICK, widget, buffer)) {
            return true;
          }
        }
        break;
    }
    return super.handleMovementKey(widget, buffer, keyCode, movementMetaState, event);
  }

  @Override
  protected boolean up(TextView widget, Spannable buffer) {
    return action(UP, widget, buffer) && scrollUp(widget, buffer, AMOUNT);
  }

  @Override
  protected boolean down(TextView widget, Spannable buffer) {
    return action(DOWN, widget, buffer) && scrollDown(widget, buffer, AMOUNT);
  }

  @Override
  protected boolean right(TextView widget, Spannable buffer) {
    return action(DOWN, widget, buffer) && scrollRight(widget, buffer, AMOUNT);
  }

  private boolean action(int what, TextView widget, Spannable buffer) {
    Layout layout = widget.getLayout();
    int padding = widget.getTotalPaddingTop() + widget.getTotalPaddingBottom();
    int areaTop = widget.getScrollY();
    int areaBot = areaTop + widget.getHeight() - padding;
    int lineTop = layout.getLineForVertical(areaTop);
    int lineBot = layout.getLineForVertical(areaBot);
    int first = layout.getLineStart(lineTop);
    int last = layout.getLineEnd(lineBot);
    ClickableSpan[] candidates = buffer.getSpans(first, last, ClickableSpan.class);
    int a = Selection.getSelectionStart(buffer);
    int b = Selection.getSelectionEnd(buffer);
    int selStart = Math.min(a, b);
    int selEnd = Math.max(a, b);
    if (selStart < 0) {
      if (buffer.getSpanStart(FROM_BELOW) >= 0) {
        selStart = selEnd = buffer.length();
      }
    }
    if (selStart > last) selStart = selEnd = Integer.MAX_VALUE;
    if (selEnd < first) selStart = selEnd = -1;
    switch (what) {
      case CLICK:
        if (selStart == selEnd) {
          return false;
        }
        ClickableSpan[] links = buffer.getSpans(selStart, selEnd, ClickableSpan.class);
        if (links.length != 1) {
          return false;
        }
        ClickableSpan link = links[0];
        if (link instanceof TextLinkSpan) {
          ((TextLinkSpan) link).onClick(widget /*, TextLinkSpan.INVOCATION_METHOD_KEYBOARD*/);
        } else {
          link.onClick(widget);
        }
        break;
      case UP:
        int bestStart, bestEnd;
        bestStart = -1;
        bestEnd = -1;
        for (int i = 0; i < candidates.length; i++) {
          int end = buffer.getSpanEnd(candidates[i]);
          if (end < selEnd || selStart == selEnd) {
            if (end > bestEnd) {
              bestStart = buffer.getSpanStart(candidates[i]);
              bestEnd = end;
            }
          }
        }
        if (bestStart >= 0) {
          Selection.setSelection(buffer, bestEnd, bestStart);
          return true;
        }
        break;
      case DOWN:
        bestStart = Integer.MAX_VALUE;
        bestEnd = Integer.MAX_VALUE;
        for (int i = 0; i < candidates.length; i++) {
          int start = buffer.getSpanStart(candidates[i]);
          if (start > selStart || selStart == selEnd) {
            if (start < bestStart) {
              bestStart = start;
              bestEnd = buffer.getSpanEnd(candidates[i]);
            }
          }
        }
        if (bestEnd < Integer.MAX_VALUE) {
          Selection.setSelection(buffer, bestStart, bestEnd);
          return true;
        }
        break;
    }
    return false;
  }

  @Override
  public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
    int action = event.getAction();
    if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
      int x = (int) event.getX();
      int y = (int) event.getY();
      x -= widget.getTotalPaddingLeft();
      y -= widget.getTotalPaddingTop();
      x += widget.getScrollX();
      y += widget.getScrollY();
      Layout layout = widget.getLayout();
      int line = layout.getLineForVertical(y);
      int off = layout.getOffsetForHorizontal(line, x);
      ClickableSpan[] links = buffer.getSpans(off, off, ClickableSpan.class);
      if (links.length != 0) {
        ClickableSpan link = links[0];
        if (action == MotionEvent.ACTION_UP) {
          /*
          if (link instanceof TextLinkSpan) {
            ((TextLinkSpan) link).onClick(widget); // , TextLinkSpan.INVOCATION_METHOD_TOUCH);
          } else {
            link.onClick(widget);
          } */
          link.onClick(widget);
        } else if (action == MotionEvent.ACTION_DOWN) {
          /*
          if (widget.getContext().getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.P) {
            // Selection change will reposition the toolbar. Hide it for a few ms for a
            // smoother transition.
            // widget.hideFloatingToolbar(HIDE_FLOATING_TOOLBAR_DELAY_MS);
          }
          */
          Selection.setSelection(buffer, buffer.getSpanStart(link), buffer.getSpanEnd(link));
        }
        return true;
      } else {
        Selection.removeSelection(buffer);
      }
    }
    return Touch.onTouchEvent(widget, buffer, event);
  }

  @Override
  public void initialize(TextView widget, Spannable text) {
    Selection.removeSelection(text);
    text.removeSpan(FROM_BELOW);
  }

  @Override
  public void onTakeFocus(TextView widget, Spannable text, int dir) {
    Selection.removeSelection(text);
    if ((dir & View.FOCUS_BACKWARD) != 0) {
      text.setSpan(FROM_BELOW, 0, 0, Spannable.SPAN_POINT_POINT);
    } else {
      text.removeSpan(FROM_BELOW);
    }

    Layout layout = widget.getLayout();
    if (layout != null && (dir & View.FOCUS_FORWARD) != 0) {
      widget.scrollTo(widget.getScrollX(), layout.getLineTop(0));
    }
    if (layout != null && (dir & View.FOCUS_BACKWARD) != 0) {
      int padding = widget.getTotalPaddingTop() + widget.getTotalPaddingBottom();
      int line = layout.getLineCount() - 1;
      widget.scrollTo(
          widget.getScrollX(), layout.getLineTop(line + 1) - (widget.getHeight() - padding));
    }
  }

  public static MovementMethod getInstance() {
    if (sInstance == null) sInstance = new ScrollingLinkMovementMethod();
    return sInstance;
  }

  private static ScrollingLinkMovementMethod sInstance;
  private static Object FROM_BELOW = new NoCopySpan.Concrete();

  @Override
  protected boolean lineEnd(TextView widget, Spannable buffer) {
    return scrollLineEnd(widget, buffer);
  }

  @Override
  protected boolean lineStart(TextView widget, Spannable buffer) {
    return scrollLineStart(widget, buffer);
  }

  @Override
  protected boolean bottom(TextView widget, Spannable buffer) {
    return scrollBottom(widget, buffer);
  }

  @Override
  protected boolean home(TextView widget, Spannable buffer) {
    return top(widget, buffer);
  }

  @Override
  protected boolean pageUp(TextView widget, Spannable buffer) {
    return scrollPageUp(widget, buffer);
  }

  @Override
  protected boolean pageDown(TextView widget, Spannable buffer) {
    return scrollPageDown(widget, buffer);
  }

  @Override
  protected boolean top(TextView widget, Spannable buffer) {
    return scrollTop(widget, buffer);
  }

  @Override
  protected boolean left(TextView widget, Spannable buffer) {
    return scrollLeft(widget, buffer, AMOUNT);
  }

  @Override
  protected boolean end(TextView widget, Spannable buffer) {
    return bottom(widget, buffer);
  }
}

