package nx.peter.app.android_ui.view.pdf.subscale;

import android.graphics.PointF;

public interface OnStateChangedListener {
    void onScaleChanged(float newScale, int origin);

    void onCenterChanged(PointF newCenter, int origin);
}
