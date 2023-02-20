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

    @Override
    protected void reset() {

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
        else super.setBackground(b);
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

