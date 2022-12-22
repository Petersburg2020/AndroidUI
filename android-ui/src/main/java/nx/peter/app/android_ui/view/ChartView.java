package nx.peter.app.android_ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public interface ChartView<V extends View, D extends ChartView.Data, L extends ChartView.DataList>
        extends IView<V> {
    Type getType();

    L getData();

    String getTitle();

    Animator getAnimator();

    ColorSwatch getColorSwatch();

    int getValueColor();

    float getValueSize();

    boolean containsData(@NonNull D data);

    boolean isDescriptionEnabled();

    void animateX(int x);

    void animateY(int y);

    void animateXY(int x, int y);

    void addData(@NonNull CharSequence label, float valueX, float valueY);

    void addData(@NonNull D data);

    void removeData(@NonNull D data);

    void setData(@NonNull List<D> data);

    void setForeground(@NonNull Foreground foreground);

    void setValueColor(@ColorInt int color);

    void setValueSize(float size);

    void setTitle(@NonNull CharSequence title);

    void setEnableDescription(boolean enable);

    void setColorSwatch(@NonNull ColorSwatch color);

    void setAnimator(Animator animator);

    enum Type {
        PieChart,
        BarChart,
        CandleStick
    }

    enum Animator {
        EaseInBounce,
        EaseOutBounce,
        EaseInCircle,
        EaseOutCircle,
        EaseInCubic,
        EaseOutCubic,
        EaseInElastic,
        EaseOutElastic,
        EaseInQuad,
        EaseOutQuad,
        Linear
    }

    enum ColorSwatch {
        Colorful,
        Joyful,
        Liberty,
        Material,
        Pastel,
        Vordiplom
    }

    interface Data<T extends Data> {
        float getValue1();
        float getValue2();
        String getLabel();
        T get();
    }

    abstract class DataList<D extends Data, N extends Entry> implements Iterable<D> {
        protected List<D> data;

        public DataList(@NonNull List<D> data) {
            this.data = data;
        }

        public D get(int index) {
            return index >= 0 && index < size() ? data.get(index) : null;
        }

        public int size() {
            return data.size();
        }

        public D get(CharSequence label) {
            for (D data : this.data) if (data.getLabel().contentEquals(label)) return data;
            return null;
        }

        public boolean contains(CharSequence label) {
            return get(label) != null;
        }

        public boolean contains(D data) {
            for (D d : this.data) if (d.getLabel().contentEquals(data.getLabel())) return true;
            return false;
        }

        public List<String> getLabels() {
            List<String> l = new ArrayList<>();
            for (D d : data) l.add(d.getLabel());
            return l;
        }

        public abstract List<N> getEntries();

        @NonNull
        @Override
        public Iterator<D> iterator() {
            return data.iterator();
        }
    }

    abstract class Builder<
            B extends Builder, C extends Chart, S extends DataSet, I extends Data, E extends DataList>
            extends AbstractView<B> implements ChartView<B, I, E> {
        // protected Statistics stat;
        protected ColorSwatch color;
        protected List<I> data;
        protected C chart;
        protected S dataSet;
        protected String title;
        protected Animator animator;
        protected int animateX, animateY;

        public Builder(Context c) {
            super(c);
        }

        public Builder(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        protected void reset() {
            setBackground(Background.Transparent);
            title = "Enter a title here...";
            data = new ArrayList<>();
            color = ColorSwatch.Colorful;
            animateX = 5000;
            animateY = 5000;
            animator = Animator.Linear;

            setup();
        }

        protected abstract void setup();

        @Override
        public boolean isDescriptionEnabled() {
            return chart.getDescription().isEnabled();
        }

        @Override
        public void setEnableDescription(boolean enable) {
            chart.getDescription().setEnabled(enable);
        }

        @Override
        public void setColorSwatch(@NonNull ColorSwatch color) {
            this.color = color;
            // dataSet.setColors(getColor(this.color));
            setup();
        }

        @Override
        public void setAnimator(@NonNull Animator animator) {
            this.animator = animator;
            setup();
        }
        @Override
        public Animator getAnimator() {
            return animator;
        }

        @Override
        public void setValueColor(@ColorInt int color) {
            dataSet.setValueTextColor(color);
        }

        @Override
        public void setValueSize(float size) {
            dataSet.setValueTextSize(size);
        }

        @Override
        public int getValueColor() {
            return dataSet.getValueTextColor();
        }

        @Override
        public void setForeground(@NonNull Foreground foreground) {
            switch (foreground) {
                case Blue: setValueColor(Color.BLUE); break;
                case Black: setValueColor(Color.BLACK); break;
                case Brown: setValueColor(Color.parseColor("#FF603608")); break;
                case Cyan: setValueColor(Color.CYAN); break;
                case Green: setValueColor(Color.GREEN); break;
                case Grey: setValueColor(Color.GRAY); break;
                case Lime: setValueColor(Color.parseColor("#FF84FF00")); break;
                case Pink: setValueColor(Color.parseColor("#FFFF00D8")); break;
                case Orange: setValueColor(Color.parseColor("#FFFF7500")); break;
                case Purple: setValueColor(Color.parseColor("#FF7500FF")); break;
                case Red: setValueColor(Color.RED); break;
                case White: setValueColor(Color.WHITE); break;
                case Yellow: setValueColor(Color.YELLOW); break;
            }
        }

        @Override
        public float getValueSize() {
            return dataSet.getValueTextSize();
        }

        protected Easing.EasingFunction getEase(Animator animator) {
            switch (animator) {
                case EaseInBounce: return Easing.EaseInBounce;
                case EaseOutBounce: return Easing.EaseOutBounce;
                case EaseInCircle: return Easing.EaseInCirc;
                case EaseOutCircle: return Easing.EaseOutCirc;
                case EaseInCubic: return Easing.EaseInCubic;
                case EaseOutCubic: return Easing.EaseOutCubic;
                case EaseInElastic: return Easing.EaseInElastic;
                case EaseOutElastic: return Easing.EaseOutElastic;
                case EaseInQuad: return Easing.EaseInQuad;
                case EaseOutQuad: return Easing.EaseOutQuad;
                default: return Easing.Linear;
            }
        }

        protected int[] getColor(ColorSwatch color) {
            switch (color) {
                case Joyful:
                    return ColorTemplate.JOYFUL_COLORS;
                case Liberty:
                    return ColorTemplate.LIBERTY_COLORS;
                case Material:
                    return ColorTemplate.MATERIAL_COLORS;
                case Pastel:
                    return ColorTemplate.PASTEL_COLORS;
                case Vordiplom:
                    return ColorTemplate.VORDIPLOM_COLORS;
                default:
                    return ColorTemplate.COLORFUL_COLORS;
            }
        }

        @Override
        public void setTitle(@NonNull CharSequence title) {
            this.title = title.toString();
            // dataSet.setLabel(this.title);
            setup();
        }

        @Override
        public ColorSwatch getColorSwatch() {
            return color;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public void setData(@NonNull List<I> data) {
            this.data = data;
            setup();
        }

        @Override
        public void addData(@NonNull I data) {
            this.data.add(data);
            setup();
        }

        @Override
        public void removeData(@NonNull I data) {
            this.data.remove(data);
            setup();
        }

        @Override
        public boolean containsData(@NonNull I data) {
            return getData().contains(data);
        }

        @Override
        public void setBackgroundResource(int res) {
            chart.setBackgroundResource(res);
        }

        @Override
        public void setPaddingRelative(int start, int top, int end, int bottom) {
            chart.setPaddingRelative(start, top, end, bottom);
        }

        @Override
        public void setPadding(int left, int top, int right, int bottom) {
            chart.setPadding(left, top, right, bottom);
        }

        @Override
        public int getPaddingRight() {
            return chart.getPaddingRight();
        }

        @Override
        public int getPaddingLeft() {
            return chart.getPaddingLeft();
        }

        @Override
        public int getPaddingTop() {
            return chart.getPaddingTop();
        }

        @Override
        public int getPaddingBottom() {
            return chart.getPaddingBottom();
        }

        @Override
        public void animateX(int x) {
            animateX = x;
            setup();
        }

        @Override
        public void animateY(int y) {
            animateY = y;
            setup();
        }

        @Override
        public void animateXY(int x, int y) {
            animateX(x);
            animateY(y);
        }
    }
}

