package nx.peter.app.android_ui.view.general;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.AbstractView;
import nx.peter.app.android_ui.view.IView;

public class Content<I extends View, V extends View> extends AbstractView<I> {
    protected Background background;

    public Content(Context context) {
        super(context);
    }

    public Content(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {

    }

    public void setContentView(@LayoutRes int layoutRes) {
        try {
            setContentView(LayoutInflater.from(getContext()).inflate(layoutRes, this, false));
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public <V extends View> void setContentView(V view) {
        if (view != null) {
            if (getChildCount() > 0) removeAllViews();
            addView(view, new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
    }

    public <V extends View> V getContentView() {
        return isNotEmpty() ? (V) getChildAt(0) : null;
    }

    public boolean isEmpty() {
        return getChildCount() == 0;
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    @Override
    public void setViewWidth(int width) {
        if (isEmpty()) super.setViewWidth(width);
        else if (getContentView() instanceof IView) ((IView<?>) getContentView()).setViewWidth(width);
        else getContentView().getLayoutParams().width = width;
    }

    @Override
    public void setViewHeight(int height) {
        if (isEmpty()) super.setViewHeight(height);
        else if (getContentView() instanceof IView) ((IView<?>) getContentView()).setViewHeight(height);
        else getContentView().getLayoutParams().height = height;
    }

    @Override
    public void setPadding(int padding) {
        setPadding(padding, padding);
    }

    @Override
    public void setPadding(int horizontal, int vertical) {
        setPadding(horizontal, vertical, horizontal, vertical);
    }

    @Override
    public void setPaddingHorizontal(int horizontal) {
        setPadding(horizontal, getPaddingTop(), horizontal, getPaddingBottom());
    }

    @Override
    public void setPadding(int left, int top, int right, int botton) {
        if (isNotEmpty()) getContentView().setPadding(left, top, right, botton);
        else super.setPadding(left, top, right, botton);
    }

    @Override
    public int getViewWidth() {
        return isEmpty()
                ? super.getViewWidth()
                : getContentView() instanceof IView
                ? ((IView) getContentView()).getViewWidth()
                : getContentView().getLayoutParams().width;
    }

    @Override
    public int getViewHeight() {
        return isEmpty()
                ? super.getViewHeight()
                : getContentView() instanceof IView
                ? ((IView) getContentView()).getViewHeight()
                : getContentView().getLayoutParams().height;
    }

    public boolean equals(V view) {
        Content<I, V> content = new Content<>(getContext());
        content.setContentView(view);
        return equals(content);
    }

    @Override
    public ViewGroup.LayoutParams getLayoutParams() {
        return isNotEmpty() ? getContentView().getLayoutParams() : super.getLayoutParams();
    }

    public boolean equals(Content<? extends View, ? extends View> content) {
        return isNotEmpty()
                && content != null
                && content.isNotEmpty()
                && getContentView().equals(content.getContentView());
    }

    @Override
    public void setBackground(@NonNull Background b) {
        background = b;
        if (isNotEmpty() && getContentView() instanceof IView)
            ((IView<?>) getContentView()).setBackground(b);
        else {
            switch (background) {
                case Black:
                    setBackgroundResource(R.drawable.black);
                    break;
                case White:
                    setBackgroundResource(R.drawable.white);
                    break;
                case Grey:
                    setBackgroundResource(R.drawable.grey);
                    break;
                case Blue:
                    setBackgroundResource(R.drawable.blue);
                    break;
                case Brown:
                    setBackgroundResource(R.drawable.brown);
                    break;
                case Cyan:
                    setBackgroundResource(R.drawable.cyan);
                    break;
                case Lime:
                    setBackgroundResource(R.drawable.lime);
                    break;
                case Green:
                    setBackgroundResource(R.drawable.green);
                    break;
                case Yellow:
                    setBackgroundResource(R.drawable.yellow);
                    break;
                case Red:
                    setBackgroundResource(R.drawable.red);
                    break;
                case Orange:
                    setBackgroundResource(R.drawable.orange);
                    break;
                case Purple:
                    setBackgroundResource(R.drawable.purple);
                    break;
                case Pink:
                    setBackgroundResource(R.drawable.pink);
                    break;
                default:
                    setBackgroundResource(R.drawable.transparent);
            }
        }
    }

    @Override
    public void setBackgroundResource(int res) {
        if (isNotEmpty()) getContentView().setBackgroundResource(res);
        else super.setBackgroundResource(res);
    }

    @Override
    public Background getViewBackground() {
        return background;
    }
}

