package nx.peter.app.android_ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.AttributeSet;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.text.*;
import nx.peter.app.android_ui.view.util.Dimens;

public class ScrollingText extends AStyledText<ScrollingText> {


    public ScrollingText(Context context) {
        super(context);
    }

    public ScrollingText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_scrolling_text, this);
        view = findViewById(R.id.scrolling_text);

        reset();

        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ScrollingText);

            try {
                float size = a.getDimensionPixelSize(R.styleable.ScrollingText_android_textSize, (int) Dimens.toSp(16));
                setTextSize(size);
            } catch (Exception ignored) {}

            try {
                String text = a.getString(R.styleable.ScrollingText_android_text);
                if (text != null)
                    setText(text);
            } catch (Exception ignored) {}

            try {
                int alignment = a.getInt(R.styleable.ScrollingText_alignment, -1);
                setAlignment(alignment);
            } catch (Exception ignored) {}

            try {
                boolean scrollable = a.getBoolean(R.styleable.ScrollingText_scrollable, true);
                setScrollable(scrollable);
            } catch (Exception ignored) {}

            try {
                int color = a.getColor(R.styleable.ScrollingText_android_textColor, getTextColor());
                setTextColor(color);
            } catch (Exception ignored) {}

            try {
                int style = a.getInt(R.styleable.ScrollingText_view_font_style, -1);
                setStyle(style);
            } catch (Exception ignored) {}

            try {
                int background = a.getInt(R.styleable.ScrollingText_view_background, 11);
                setBackground(background);
            } catch (Exception ignored) {}

            a.recycle();
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
