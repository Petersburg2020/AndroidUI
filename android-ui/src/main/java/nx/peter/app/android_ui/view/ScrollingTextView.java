package nx.peter.app.android_ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;
import androidx.annotation.NonNull;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.text.*;
import nx.peter.app.android_ui.view.text.FontFamily.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScrollingTextView extends IMultiActionText<ScrollingTextView> {


    public ScrollingTextView(Context context) {
        super(context);
    }

    public ScrollingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_scrolling_text, this);
        view = findViewById(R.id.scrolling_text);

        reset();

        if (attrs != null) {
            @SuppressLint("Recycle") TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ScrollingTextView);

            try {
                float size = a.getDimensionPixelSize(R.styleable.ScrollingTextView_android_textSize, 16);
                setTextSize(size);
            } catch (Exception ignored) {}

            try {
                String text = a.getString(R.styleable.ScrollingTextView_android_text);
                if (text != null)
                    setText(text);
            } catch (Exception ignored) {}

            try {
                int alignment = a.getInt(R.styleable.ScrollingTextView_alignment, -1);
                switch (alignment) {
                    case 0:
                        setAlignment(Alignment.Center);
                        break;
                    case 1:
                        setAlignment(Alignment.TopRight);
                        break;
                    case 2:
                        setAlignment(Alignment.TopLeft);
                        break;
                    case 3:
                        setAlignment(Alignment.BottomRight);
                        break;
                    case 4:
                        setAlignment(Alignment.BottomLeft);
                        break;
                    case 5:
                        setAlignment(Alignment.CenterLeft);
                        break;
                    case 6:
                        setAlignment(Alignment.CenterRight);
                        break;
                    case 7:
                        setAlignment(Alignment.TopCenter);
                        break;
                    case 8:
                        setAlignment(Alignment.BottomCenter);
                }
            } catch (Exception ignored) {}

            try {
                boolean scrollable = a.getBoolean(R.styleable.ScrollingTextView_scrollable, true);
                setScrollable(scrollable);
            } catch (Exception ignored) {}

            try {
                int color = a.getColor(R.styleable.ScrollingTextView_android_textColor, getTextColor());
                setTextColor(color);
            } catch (Exception ignored) {}

            try {
                int style = a.getInt(R.styleable.ScrollingTextView_view_font_style, -1);
                switch (style) {
                    case 0: setFontStyle(Font.Style.Bold); break;
                    case 1: setFontStyle(Font.Style.Italic); break;
                    case 2: setFontStyle(Font.Style.Regular); break;
                    case 3: setFontStyle(Font.Style.BoldItalic);
                }
            } catch (Exception ignored) {}

            try {
                int background = a.getInt(R.styleable.ScrollingTextView_view_background, 11);
                switch (background) {
                    case 0: setBackground(Background.Black); break;
                    case 1: setBackground(Background.Grey); break;
                    case 2: setBackground(Background.White); break;
                    case 3: setBackground(Background.Red); break;
                    case 4: setBackground(Background.Blue); break;
                    case 5: setBackground(Background.Green); break;
                    case 6: setBackground(Background.Yellow); break;
                    case 7: setBackground(Background.Orange); break;
                    case 8: setBackground(Background.Purple); break;
                    case 9: setBackground(Background.Pink); break;
                    case 10: setBackground(Background.Brown); break;
                    case 11: setBackground(Background.Transparent); break;
                }
            } catch (Exception ignored) {}

            a.close();
        }
    }

    protected void reset() {
        super.reset();
        setScrollable(true);
    }

    @Override
    protected void setSpan(SpannableString span) {
        this.span = span;
        view.setText(span);
        // view.setMovementMethod(LinkMovementMethod.getInstance());
    }

    protected void setScrollable(boolean scrollable) {
        this.scrollable = scrollable;
        // view.setMovementMethod(ScrollingMovementMethod.getInstance());
        
        view.setHorizontallyScrolling(true);
        view.setSingleLine(true);
        view.setMarqueeRepeatLimit(-1);
        view.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        view.setFocusable(true);
        // view.setHorizontalScrollBarEnabled(true);
        view.setFocusableInTouchMode(true);
        view.setSelected(scrollable);
    }

    public boolean isScrolling() {
        return view.isSelected();
    }

}
