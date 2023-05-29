package nx.peter.app.android_ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RemoteViews;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleableRes;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.util.Padding;
import nx.peter.app.android_ui.view.util.Scale;
import nx.peter.app.android_ui.view.util.Size;

import java.util.Arrays;

/**
 * A layout that lets you specify exact locations (x/y coordinates) of its
 * children. Absolute layouts are less flexible and harder to maintain than
 * other types of layouts without absolute positioning.
 *
 * <p><strong>XML attributes</strong></p> <p> See {@link
 * android.R}.styleable#ViewGroup {@link ViewGroup Attributes}, {@link
 * android.R}.styleable#View {@link View Attributes}</p>
 *
 * <p>Note: This class is a clone of AbsoluteLayout, which is now deprecated.
 */

@RemoteViews.RemoteView
public class AbsoluteLayout extends ViewGroup /*implements ILayout<AbsoluteLayout, View>*/ {
    /*protected Background background;
    protected Resources res;*/


    public AbsoluteLayout(@NonNull Context context) {
        this(context, null);
    }

    public AbsoluteLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsoluteLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /*res = Resources.getSystem();
        setBackground(Background.Transparent);*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        int maxHeight = 0;
        int maxWidth = 0;

        // Find out how big everyone wants to be
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        // Find rightmost and bottom-most child
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                int childRight;
                int childBottom;

                LayoutParams lp
                        = (LayoutParams) child.getLayoutParams();

                childRight = lp.x + child.getMeasuredWidth();
                childBottom = lp.y + child.getMeasuredHeight();

                maxWidth = Math.max(maxWidth, childRight);
                maxHeight = Math.max(maxHeight, childBottom);
            }
        }

        // Account for padding too
        maxWidth += getPaddingLeft() + getPaddingRight();
        maxHeight += getPaddingTop() + getPaddingBottom();

        /* original
        maxWidth += mPaddingLeft + mPaddingRight;
        maxHeight += mPaddingTop + mPaddingBottom;
        */

        // Check against minimum height and width
        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        setMeasuredDimension(resolveSize(maxWidth, widthMeasureSpec),
                resolveSize(maxHeight, heightMeasureSpec));
    }

    /**
     * Returns a set of layout parameters with a width of
     * {@link ViewGroup.LayoutParams#WRAP_CONTENT},
     * a height of {@link ViewGroup.LayoutParams#WRAP_CONTENT}
     * and with the coordinates (0, 0).
     */
    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 0, 0);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();

        int paddingL = getPaddingLeft();
        int paddingT = getPaddingTop();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {

                LayoutParams lp = (LayoutParams) child.getLayoutParams();

                int childLeft = paddingL + lp.x;
                int childTop = paddingT + lp.y;

                child.layout(childLeft, childTop,
                        childLeft + child.getMeasuredWidth(),
                        childTop + child.getMeasuredHeight());

            }
        }
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    // Override to allow type-checking of LayoutParams.
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    /*@Override
    public void addChild(View view) {
        addView(view);
    }

    @Override
    public View getChildAt(int position) {
        return super.getChildAt(position);
    }

    @Override
    public View findChildById(@IdRes int id) {
        return findViewById(id);
    }

    public void addChild(View child, int index) {
        addView(child, index);
    }

    public void addChild(View child, LayoutDetails details) {
        addView(child, details);
    }

    @Override
    public LayoutDetails getLayoutParams() {
        return new LayoutDetails(super.getLayoutParams());
    }

    protected void setBackground(int background) {
        switch (background) {
            case 0:
                setBackground(Background.Black);
                break;
            case 1:
                setBackground(Background.Grey);
                break;
            case 2:
                setBackground(Background.White);
                break;
            case 3:
                setBackground(Background.Red);
                break;
            case 4:
                setBackground(Background.Blue);
                break;
            case 5:
                setBackground(Background.Green);
                break;
            case 6:
                setBackground(Background.Yellow);
                break;
            case 7:
                setBackground(Background.Orange);
                break;
            case 8:
                setBackground(Background.Purple);
                break;
            case 9:
                setBackground(Background.Pink);
                break;
            case 10:
                setBackground(Background.Brown);
                break;
            case 11:
                setBackground(Background.Transparent);
        }
    }

    protected DisplayMetrics getDisplayMetrics() {
        return res.getDisplayMetrics();
    }

    protected TypedArray obtainStyledAttributes(@Nullable AttributeSet attrs, @NonNull @StyleableRes int[] styledAttrs) {
        return getContext().obtainStyledAttributes(attrs, styledAttrs);
    }

    @Override
    public AbsoluteLayout getView() {
        return this;
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
        return new IPrinter(this);
    }*/


    /**
     * Per-child layout information associated with MyAbsoluteLayout.
     * See
     * {@link android.R}.styleable#AbsoluteLayout_Layout Absolute Layout Attributes
     * for a list of all child view attributes that this class supports.
     */
    public static class LayoutParams extends ViewGroup.LayoutParams {
        /**
         * The horizontal, or X, location of the child within the view group.
         */
        public int x;
        /**
         * The vertical, or Y, location of the child within the view group.
         */
        public int y;

        /**
         * Creates a new set of layout parameters with the specified width,
         * height and location.
         *
         * @param width  the width, either {@link #MATCH_PARENT},
         *               {@link #WRAP_CONTENT} or a fixed size in pixels
         * @param height the height, either {@link #MATCH_PARENT},
         *               {@link #WRAP_CONTENT} or a fixed size in pixels
         * @param x      the X location of the child
         * @param y      the Y location of the child
         */
        public LayoutParams(int width, int height, int x, int y) {
            super(width, height);
            this.x = x;
            this.y = y;
        }

        /**
         * Creates a new set of layout parameters. The values are extracted from
         * the supplied attributes set and context. The XML attributes mapped
         * to this set of layout parameters are:
         *
         * <ul>
         *   <li><code>layout_x</code>: the X location of the child</li>
         *   <li><code>layout_y</code>: the Y location of the child</li>
         *   <li>All the XML attributes from
         *   {@link ViewGroup.LayoutParams}</li>
         * </ul>
         *
         * @param c     the application environment
         * @param attrs the set of attributes from which to extract the layout
         *              parameters values
         */
        public LayoutParams(@NonNull Context c, @Nullable AttributeSet attrs) {
            super(c, attrs);
            // FIX THIS eventually. Without this, I don't think you can put x and y in layout xml files.
            @SuppressLint("CustomViewStyleable")
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.AbsoluteLayout_Layout);
            x = a.getDimensionPixelOffset(R.styleable.AbsoluteLayout_Layout_x, 0);
            y = a.getDimensionPixelOffset(R.styleable.AbsoluteLayout_Layout_y, 0);
            a.recycle();
            a.close();
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public String debug(String output) {
            return output + "AbsoluteLayout.LayoutParams={width="
                    + sizeToString(width) + ", height=" + sizeToString(height)
                    + " x=" + x + " y=" + y + "}";
        }

        /**
         * Converts the specified size to a readable String.
         *
         * @param size the size to convert
         * @return a String instance representing the supplied size
         * @hide
         */
        protected static String sizeToString(int size) {
            return (size == WRAP_CONTENT) ? "wrap-content" : (size == MATCH_PARENT) ? "match-parent" : String.valueOf(size);
        }
    } // end class

} // end class


