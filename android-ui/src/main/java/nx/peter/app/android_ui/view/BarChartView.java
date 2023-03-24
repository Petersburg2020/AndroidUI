package nx.peter.app.android_ui.view;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import nx.peter.app.android_ui.R;

import java.util.ArrayList;
import java.util.List;

public class BarChartView
        extends ChartView.Builder<BarChartView, BarChart, BarDataSet, BarChartView.BaData, BarChartView.BaList> {

    public BarChartView(Context c) {
        super(c);
    }

    public BarChartView(Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_bar_chart, this);
        chart = findViewById(R.id.bar);

        reset();
    }

    @Override
    protected void reset() {
        super.reset();

        setTitle("Shapes");
        setColorSwatch(ColorSwatch.Material);
        addData("Square", 0.7f, 1.5f);
        addData("Circle", 0.8f, 2.5f);
        addData("Rectangle", 0.6f, 1.8f);

        // chart.animateXY(0,0, Easing.EaseInBounce);
    }

    @Override
    public Type getType() {
        return Type.BarChart;
    }

    @Override
    public BaList getData() {
        return new BaList(data);
    }

    @Override
    public void addData(@NonNull CharSequence label, float valueX, float valueY) {
        addData(new BaData(valueX, valueY));
    }

    @Override
    protected void setup() {
        dataSet = new BarDataSet(getData().getEntries(), getTitle());
        chart.setData(new BarData(dataSet));
        dataSet.setColors(getColor(color));
        chart.animateXY(animateX, animateY, getEase(animator));
    }

    public static class BaData implements Data<BaData> {
        protected float value1, value2;

        public BaData(float value1, float value2) {
            this.value1 = value1;
            this.value2 = value2;
        }

        @Override
        public BaData get() {
            return this;
        }

        @Override
        public float getValue1() {
            return value1;
        }

        @Override
        public float getValue2() {
            return value2;
        }

        @Override
        public String getLabel() {
            return "-";
        }

    }

    public static class BaList extends DataList<BaData, BarEntry> {
        public BaList(List<BaData> data) {
            super(data);
        }

        @Override
        public List<BarEntry> getEntries() {
            List<BarEntry> p = new ArrayList<>();
            for (BaData d : data)
                p.add(new BarEntry(d.getValue1(), d.getValue2()));
            return p;
        }
    }
}

