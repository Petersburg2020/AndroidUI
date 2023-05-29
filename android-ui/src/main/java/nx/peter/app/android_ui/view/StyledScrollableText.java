package nx.peter.app.android_ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.text.Font;
import nx.peter.app.android_ui.view.util.Dimens;

public class StyledScrollableText extends AStyledScrollableView<StyledScrollableText, StyledText> {

    public StyledScrollableText(Context context) {
        super(context);
    }

    public StyledScrollableText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @SuppressLint("Range")
    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_styled_scrollable_text, this);
        vView = findViewById(R.id.v_view);
        hView = findViewById(R.id.h_view);
        vhView = findViewById(R.id.vh_view);
        hContainer = findViewById(R.id.horizontal);
        vContainer = findViewById(R.id.vertical);
        vhContainer = findViewById(R.id.both);

        reset();

        if (attrs != null) {
            TypedArray a = obtainStyledAttributes(attrs, R.styleable.StyledScrollableText);

            try {
                float size = a.getDimension(R.styleable.StyledScrollableText_android_textSize, (int) Dimens.toSp(16));
                setTextSize(size / getDisplayMetrics().scaledDensity);
            } catch (Exception ignored) {
            }

            try {
                float scale = a.getFloat(R.styleable.StyledScrollableText_scale, 1);
                setScale(scale);
            } catch (Exception ignored) {
            }

            try {
                float scale = a.getFloat(R.styleable.StyledScrollableText_scale_x, 1);
                setScaleX(scale);
            } catch (Exception ignored) {
            }

            try {
                float scale = a.getFloat(R.styleable.StyledScrollableText_scale_y, 1);
                setScaleY(scale);
            } catch (Exception ignored) {
            }

            try {
                String text = a.getString(R.styleable.StyledScrollableText_android_text);
                if (text != null)
                    setText(text);
            } catch (Exception ignored) {
            }

            try {
                int alignment = a.getInt(R.styleable.StyledScrollableText_alignment, -1);
                setAlignment(alignment);
            } catch (Exception ignored) {
            }

            try {
                int color = a.getColor(R.styleable.StyledScrollableText_android_textColor, getTextColor());
                setTextColor(color);
            } catch (Exception ignored) {
            }

            try {
                int color = a.getInt(R.styleable.StyledScrollableText_textColor, -1);
                setForegroundColor(color);
            } catch (Exception ignored) {
            }

            try {
                int font = a.getInt(R.styleable.StyledScrollableText_font_family, 0);
                setFontFamily(font);
            } catch (Exception ignored) {
            }

            try {
                int font = a.getInt(R.styleable.StyledScrollableText_view_font, -1);
                setFont(font);
            } catch (Exception ignored) {
            }

            try {
                int style = a.getInt(R.styleable.StyledScrollableText_view_font_style, -1);
                setStyle(style);
            } catch (Exception ignored) {
            }

            try {
                int background = a.getInt(R.styleable.StyledScrollableText_view_background, 11);
                setBackground(background);
            } catch (Exception ignored) {
            }

            try {
                int direction = a.getInt(R.styleable.StyledScrollableText_scroll_direction, 0);
                setScrollDirection(direction);
            } catch (Exception ignored) {
            }

            try {
                int padding = a.getDimensionPixelOffset(R.styleable.StyledScrollableText_view_padding, -1);
                if (padding > -1) setPadding(padding);
            } catch (Exception ignored) {
            }

            try {
                int padding = a.getDimensionPixelOffset(R.styleable.StyledScrollableText_view_padding_horizontal, -1);
                if (padding > -1) setPaddingHorizontal(padding);
            } catch (Exception ignored) {
            }

            try {
                int padding = a.getDimensionPixelOffset(R.styleable.StyledScrollableText_view_padding_vertical, -1);
                if (padding > -1) setPaddingVertical(padding);
            } catch (Exception ignored) {
            }

            try {
                int padding = a.getDimensionPixelOffset(R.styleable.StyledScrollableText_view_padding_bottom, -1);
                if (padding > -1) setPaddingBottom(padding);
            } catch (Exception ignored) {
            }

            try {
                int padding = a.getDimensionPixelOffset(R.styleable.StyledScrollableText_view_padding_top, -1);
                if (padding > -1) setPaddingTop(padding);
            } catch (Exception ignored) {
            }

            try {
                int padding = a.getDimensionPixelOffset(R.styleable.StyledScrollableText_view_padding_end, -1);
                if (padding > -1) setPaddingRight(padding);
            } catch (Exception ignored) {
            }

            try {
                int padding = a.getDimensionPixelOffset(R.styleable.StyledScrollableText_view_padding_start, -1);
                if (padding > -1) setPaddingLeft(padding);
            } catch (Exception ignored) {
            }

            try {
                int alpha = a.getInt(R.styleable.StyledScrollableText_textColorAlpha, 255);
                if (alpha > -1 && alpha <= 255) setTextColorAlpha(alpha);
            } catch (Exception ignored) {
            }

            try {
                int alpha = a.getInt(R.styleable.StyledScrollableText_hintColorAlpha, 255);
                if (alpha > -1 && alpha <= 255) setHintColorAlpha(alpha);
            } catch (Exception ignored) {
            }

            a.close();
        }
    }


}
