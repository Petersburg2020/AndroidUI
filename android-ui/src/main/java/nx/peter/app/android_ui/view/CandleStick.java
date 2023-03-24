package nx.peter.app.android_ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import nx.peter.app.android_ui.R;

import java.util.ArrayList;
import java.util.List;

public class CandleStick extends ChartView.Builder<CandleStick, CandleStickChart, CandleDataSet, CandleStick.CanData, CandleStick.CanList> {


    public CandleStick(Context c) {
        super(c);
    }

    public CandleStick(Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_candle_stick, this);
        chart = findViewById(R.id.candle);

        reset();
    }

    @Override
    protected void reset() {
        super.reset();

    }

    @Override
    public Type getType() {
        return Type.CandleStick;
    }

    @Override
    public CanList getData() {
        return new CanList(data);
    }

    @Override
    public void addData(@NonNull CharSequence label, float x, float shadow) {
        addData(x, shadow, shadow, 1f, 1f, null, label);
    }

    public void addData(float x, float shadowH, float shadowL, float open, float close) {
        this.data.add(new CanData(x, shadowH, shadowL, open, close));
    }

    public void addData(float x, float shadowH, float shadowL, float open, float close, Object data) {
        this.data.add(new CanData(x, shadowH, shadowL, open, close, data));
    }

    public void addData(float x, float shadowH, float shadowL, float open, float close, Drawable icon) {
        this.data.add(new CanData(x, shadowH, shadowL, open, close, icon));
    }

    public void addData(float x, float shadowH, float shadowL, float open, float close, Drawable icon, Object data) {
        this.data.add(new CanData(x, shadowH, shadowL, open, close, icon, data));
    }

    @Override
    protected void setup() {
        dataSet = new CandleDataSet(getData().getEntries(), getTitle());
        chart.setData(new CandleData(dataSet));
        dataSet.setColors(getColor(color));
        chart.animateXY(animateX, animateY, getEase(animator));
    }

    public static class CanData implements Data<CanData> {

        public CanData(float x, float shadowH, float shadowL, float open, float close, Drawable icon) {
            this(x, shadowH, shadowL, open, close, icon, null);
        }

        public CanData(float x, float shadowH, float shadowL, float open, float close, Object data) {
            this(x, shadowH, shadowL, open, close, null, data);
        }

        public CanData(float x, float shadowH, float shadowL, float open, float close) {
            this(x, shadowH, shadowL, open, close, null, null);
        }

        public CanData(float x, float shadowH, float shadowL, float open, float close, Drawable icon, Object data) {
            this.x = x;
            this.shadowH = shadowH;
            this.shadowL = shadowL;
            this.open = open;
            this.close = close;
            this.icon = icon;
            this.data = data;
        }

        public Drawable getIcon() {
            return icon;
        }

        public float getClose() {
            return close;
        }

        public float getOpen() {
            return open;
        }

        public float getShadowH() {
            return shadowH;
        }

        public float getShadowL() {
            return shadowL;
        }

        public float getX() {
            return x;
        }

        @Override
        public float getValue1() {
            return 0;
        }

        @Override
        public float getValue2() {
            return 0;
        }

        @Override
        public String getLabel() {
            return getData().toString();
        }

        @Override
        public CanData get() {
            return this;
        }

        public Object getData() {
            return data;
        }

        protected final float x, shadowH, shadowL, open, close;
        protected final Drawable icon;
        protected final Object data;
    }

    public static class CanList extends DataList<CanData, CandleEntry> {
        public CanList(List<CanData> data) {
            super(data);
        }

        @Override
        public List<CandleEntry> getEntries() {
            List<CandleEntry> p = new ArrayList<>();
            for (CanData d : data)
                p.add(new CandleEntry(d.x, d.shadowH, d.shadowL, d.open, d.close, d.icon, d.data));
            return p;
        }
    }
}
