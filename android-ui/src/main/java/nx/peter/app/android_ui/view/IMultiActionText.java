package nx.peter.app.android_ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

abstract class IMultiActionText<I extends IMultiActionText> extends IMultiActionView<I, TextView> {

    public IMultiActionText(Context context) {
        super(context);
    }

    public IMultiActionText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void reset() {
        super.reset();
        setText("Try google this color, icon icn, underline, large-text, with emphasis and click here!");

    }
}
