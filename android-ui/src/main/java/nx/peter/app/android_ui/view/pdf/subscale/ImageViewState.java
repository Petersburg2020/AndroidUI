package nx.peter.app.android_ui.view.pdf.subscale;

import android.graphics.PointF;
import androidx.annotation.NonNull;

import java.io.Serializable;

public class ImageViewState implements Serializable {
    private final float scale, centerX, centerY;
    private final int orientation;

    public ImageViewState(float scale, @NonNull PointF center, int orientation) {
        this.scale = scale;
        this.centerX = center.x;
        this.centerY = center.y;
        this.orientation = orientation;
    }

    public float getScale() {
        return scale;
    }

    @NonNull
    public PointF getCenter() {
        return new PointF(centerX, centerY);
    }

    public int getOrientation() {
        return orientation;
    }
}
