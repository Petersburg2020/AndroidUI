package nx.peter.app.android_ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import androidx.annotation.NonNull;
import com.unnamed.b.atv.view.AndroidTreeView;

public class PdfView extends AbstractView<PdfView> {
    protected WebView view;

    public PdfView(Context context) {
        super(context);
    }

    public PdfView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {

    }

    @Override
    protected void reset() {

    }

    public void fromAssets(@NonNull CharSequence path) {



    }



}
