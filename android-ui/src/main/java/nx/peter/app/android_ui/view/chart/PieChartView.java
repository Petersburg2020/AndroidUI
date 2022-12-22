package nx.peter.app.android_ui.view.chart;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.ChartView;

import java.util.ArrayList;
import java.util.List;

public class PieChartView
        extends ChartView.Builder<PieChartView, PieChart, PieDataSet, PieChartView.PiData, PieChartView.PiList> {

    public PieChartView(Context c) {
        super(c);
    }

    public PieChartView(Context c, AttributeSet attrs) {
        super(c, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_pie_chart, this);
        chart = findViewById(R.id.pie);

        reset();
    }

    @Override
    protected void reset() {
        super.reset();

    }

    @Override
    public Type getType() {
        return Type.PieChart;
    }

    @Override
    public PiList getData() {
        return new PiList(data);
    }

    @Override
    public void addData(@NonNull CharSequence label, float valueX, float valueY) {
        addData(new PiData(valueX, valueY, label));
    }

    @Override
    protected void setup() {
        dataSet = new PieDataSet(getData().getEntries(), getTitle());
        chart.setData(new PieData(dataSet));
        dataSet.setColors(getColor(color));
        chart.animateXY(animateX, animateY);
    }

    public static class PiData implements Data<PiData> {
        protected float value1, value2;
        protected String label;

        public PiData(float value1, float value2, @NonNull CharSequence label) {
            this.value2 = value2;
            this.value1 = value1;
            this.label = label.toString();
        }

        @Override
        public PiData get() {
            return this;
        }

        public float getValue1() {
            return value1;
        }

        public float getValue2() {
            return value2;
        }

        @Override
        public String getLabel() {
            return label;
        }
    }

    public static class PiList extends DataList<PiData, PieEntry> {
        public PiList(List<PiData> data) {
            super(data);
        }

        @Override
        public List<PieEntry> getEntries() {
            List<PieEntry> p = new ArrayList<>();
            for (PiData d : data) p.add(new PieEntry(d.getValue1(), d.getValue2()));
            return p;
        }
    }

}

