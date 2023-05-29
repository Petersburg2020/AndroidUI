package nx.peter.app.android_ui.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import com.google.android.material.textview.MaterialTextView;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.text.Font;

abstract class AStyledText<I extends AStyledText> extends AStyledView<I, MaterialTextView> {

    public AStyledText(Context context) {
        super(context);
    }

    public AStyledText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void reset() {
        super.reset();
        setText("Try google this color, icon icn, underline, large-text, with emphasis and click here!");
        addLinks((view, text, link) -> {}, "here");
        addUrlLinks("https://google.com", "google");
        addSubSizes((int) (getTextSize() * 1.2f), "large-text");
        addSubImages(R.drawable.no_image, "icn");
        addSubFonts(Font.Style.Bold, "emphasis");
        addUnderlines("underline");
        addSubColors(Color.GREEN, "color");
    }


}
