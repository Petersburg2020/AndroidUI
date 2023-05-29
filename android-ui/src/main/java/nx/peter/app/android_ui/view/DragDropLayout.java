package nx.peter.app.android_ui.view;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DragDropLayout extends AbsoluteLayout {

    public DragDropLayout(@NonNull Context c) {
        super(c);
    }

    public DragDropLayout(@NonNull Context c, @Nullable AttributeSet attrs) {
        super(c, attrs);
    }

    public View findChildById(@IdRes int id) {
        DragView view = findViewById(id);
        return view != null ? view.getContentView() : null;
    }

    @Override
    public View getChildAt(int position) {
        View view = super.getChildAt(position);
        return view != null ? ((DragView) view).getContentView() : null;
    }

    /*@Override
    public void addChild(View view) {
        addView(view);
    }

    @Override
    public void addChild(View child, int index) {
        addView(child, index);
    }

    @Override
    public void addChild(View child, LayoutDetails details) {
        addView(child, new LayoutDetails(details));
    }*/

    @Override
    public void addView(View child) {
        super.addView(new DragView(child));
    }

    @Override
    public void addView(View child, int index) {
        super.addView(new DragView(child), index);
    }

    @Override
    public void addView(View child, int width, int height) {
        super.addView(new DragView(child), width, height);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(new DragView(child), params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(new DragView(child), index, params);
    }


    public static class LayoutParams extends AbsoluteLayout.LayoutParams {
        public LayoutParams(int width, int height, int x, int y) {
            super(width, height, x, y);
        }

        public LayoutParams(@NonNull Context c, @Nullable AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    protected static class DragView extends Content<DragView, View> {
        public DragView(@NonNull Context context) {
            super(context);
        }

        public DragView(@NonNull View view) {
            this(view.getContext());
            setContentView(view);
            setId(view.getId());
        }

        @Override
        protected void init(AttributeSet attrs) {
            super.init(attrs);

            setOnLongClickListener(v -> {
                if (getContentView() != null) {
                    CharSequence clipLabel = "This is our ClipData label";
                    ClipData.Item item = new ClipData.Item(clipLabel);
                    String[] mimeTypes = new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN};
                    ClipData data = new ClipData(clipLabel, mimeTypes, item);

                    DragShadowBuilder dragShadowBuilder = new DragShadowBuilder(v);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        v.startDragAndDrop(data, dragShadowBuilder, v, 0);

                    v.setVisibility(INVISIBLE);
                }
                return true;
            });

            setOnDragListener((v, event) -> {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        return event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN);
                    case DragEvent.ACTION_DRAG_ENTERED:
                    case DragEvent.ACTION_DRAG_ENDED:
                    case DragEvent.ACTION_DRAG_EXITED:
                        v.invalidate();
                        return true;
                    case DragEvent.ACTION_DRAG_LOCATION:
                        return true;
                    case DragEvent.ACTION_DROP: {
                        ClipData.Item item = event.getClipData().getItemAt(0);
                        CharSequence dragData = item.getText();

                        // Show that we got the correct data
                        Toast.makeText(v.getContext(), dragData, Toast.LENGTH_SHORT).show();

                        // Invalidate view in-between
                        v.invalidate();

                        View view = (View) event.getLocalState();
                        AbsoluteLayout owner = (AbsoluteLayout) view.getParent();
                        owner.removeView(view);

                        int x = (int) event.getX();
                        int y = (int) event.getY();

                        AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) view.getLayoutParams();
                        lp.x = x;
                        lp.y = y;
                        owner.addView(view, lp);
                        view.setVisibility(VISIBLE);
                        return true;
                    }
                    default:
                        return false;
                }
            });
        }
    }

}
