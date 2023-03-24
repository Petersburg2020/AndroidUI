package nx.peter.app.android_ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleableRes;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.util.Padding;
import nx.peter.app.android_ui.view.util.Scale;
import nx.peter.app.android_ui.view.util.Size;

import java.util.Arrays;

abstract class AView<V extends View> extends LinearLayout implements IView<V> {
    protected Background background;

    public AView(Context context) {
        this(context, null);
    }

    public AView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    protected abstract void init(AttributeSet attrs);

    protected abstract void reset();

    protected TypedArray obtainStyledAttributes(@Nullable AttributeSet attrs, @NonNull @StyleableRes int[] styledAttrs) {
        return getContext().obtainStyledAttributes(attrs, styledAttrs);
    }

    @Override
    public V getView() {
        return (V) this;
    }

    @Override
    public int getViewHeight() {
        return getLayoutParams().height;
    }

    @Override
    public int getViewWidth() {
        return getLayoutParams().width;
    }

    @Override
    public void setPadding(@NonNull Padding padding) {
        setPadding(padding.left, padding.top, padding.right, padding.bottom);
    }

    @Override
    public void setViewWidth(int width) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.width = width;
        setLayoutParams(lp);
    }

    @Override
    public void setViewHeight(int height) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = height;
        setLayoutParams(lp);
    }

    @Override
    public void setScale(float scale) {
        setScale(scale, scale);
    }

    @Override
    public void setScaleX(float scaleX) {
        setSize((int) (scaleX * getViewWidth()), getViewHeight());
    }

    @Override
    public void setScaleY(float scaleY) {
        setSize(getViewWidth(), (int) (scaleY * getViewHeight()));
    }

    @Override
    public void setScale(float x, float y) {
        setSize((int) (x * getViewWidth()), (int) (y * getViewHeight()));
    }

    @Override
    public void setSize(@NonNull Size size) {
        setViewWidth(size.width);
        setViewHeight(size.height);
    }

    @Override
    public void setSize(int width, int height) {
        setSize(new Size(width, height));
    }

    @Override
    public Size getSize() {
        return new Size(getViewWidth(), getViewHeight());
    }

    @Override
    public Scale getScale() {
        return new Scale() {
            @Override
            public float getX() {
                return getScaleX();
            }

            @Override
            public float getY() {
                return getScaleY();
            }

            @Override
            public boolean isScaled() {
                return getX() != 1 || getY() != 1;
            }

            @NonNull
            @Override
            public String toString() {
                return "x: " + getX() + ", y: " + getY();
            }
        };
    }

    @Override
    public Padding getPadding() {
        return new Padding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

    @Override
    public void setPadding(int padding) {
        setPadding(padding, padding);
    }

    @Override
    public void setPaddingTop(int padding) {
        setPadding(getPaddingLeft(), padding, getPaddingRight(), getPaddingBottom());
    }

    @Override
    public void setPaddingLeft(int padding) {
        setPadding(padding, getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

    @Override
    public void setPaddingRight(int padding) {
        setPadding(getPaddingLeft(), getPaddingTop(), padding, getPaddingBottom());
    }

    @Override
    public void setPaddingBottom(int padding) {
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), padding);
    }

    @Override
    public void setPadding(int horizontal, int vertical) {
        setPadding(horizontal, vertical, horizontal, vertical);
    }

    @Override
    public void setPaddingVertical(int padding) {
        setPadding(getPaddingLeft(), padding, getPaddingEnd(), padding);
    }

    @Override
    public void setPaddingHorizontal(int padding) {
        setPadding(padding, getPaddingTop(), padding, getPaddingBottom());
    }

    @Override
    public void setBackground(@NonNull Background background) {
        this.background = background;
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

    @Override
    public Background getViewBackground() {
        return background;
    }

    protected static class IPrinter implements Printer {
        View view;

        public IPrinter(View view) {
            this.view = view;
        }

        @Override
        public Pages print() {
            return new Pages(Arrays.asList(new Page[]{new Page() {
                @Override
                public int getNumber() {
                    return 1;
                }

                @Override
                public Bitmap getImage() {
                    return screenshot();
                }
            }}));
        }

        private Bitmap screenshot() {
            view.setDrawingCacheEnabled(true);
            view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            return bitmap;
        }

    }

    @Override
    public Printer getPrinter() {
        return new IPrinter(getView());
    }
}

