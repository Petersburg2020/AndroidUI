package nx.peter.app.android_ui.view.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViewportLayoutManager extends LinearLayoutManager {
    public ViewportLayoutManager(Context context) {
        super(context);
    }

    public ViewportLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public ViewportLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return spanLayout(super.generateDefaultLayoutParams());
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        return spanLayout(super.generateLayoutParams(lp));
    }

    @Override
    public RecyclerView.LayoutParams generateLayoutParams(Context c, AttributeSet attrs) {
        return spanLayout(super.generateLayoutParams(c, attrs));
    }

    @NonNull
    private RecyclerView.LayoutParams spanLayout(@NonNull RecyclerView.LayoutParams params) {
        params.width = getSpanWidth();
        params.height = getSpanHeight();
        return params;
    }

    public int getSpanWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    public int getSpanHeight() {
        return getHeight() - getPaddingBottom() - getPaddingTop();
    }

}
