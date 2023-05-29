package nx.peter.app.android_ui.view.pdf.adapter;
import android.graphics.Bitmap;

public interface BitmapContainer {
    Bitmap get(int position);

    void remove(int position);

    void clear();
}
