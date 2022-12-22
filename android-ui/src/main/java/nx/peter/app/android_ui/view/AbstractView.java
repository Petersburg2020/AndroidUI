package nx.peter.app.android_ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.util.Padding;
import nx.peter.app.android_ui.view.util.Scale;
import nx.peter.app.android_ui.view.util.Size;

import java.util.Arrays;

public abstract class AbstractView<V extends View> extends LinearLayout implements IView<V> {
    protected Background background;

    public AbstractView(Context context) {
        this(context, null);
    }

    public AbstractView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    protected abstract void init(AttributeSet attrs);

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

