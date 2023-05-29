package nx.peter.app.android_ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.util.Dimens;

public class StyledText extends AStyledText<StyledText> {
    public StyledText(Context context) {
        super(context);
    }

    public StyledText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_styled_text, this);
        view = findViewById(R.id.text);
        reset();

        if (attrs != null) {
            TypedArray a = obtainStyledAttributes(attrs, R.styleable.StyledText);

            try {
                float size = a.getDimensionPixelSize(R.styleable.StyledText_android_textSize, (int) Dimens.toSp(16));
                setTextSize(size / getDisplayMetrics().scaledDensity);
            } catch (Exception ignored) {
            }

            try {
                float scale = a.getFloat(R.styleable.StyledText_scale, 1);
                setScale(scale);
            } catch (Exception ignored) {
            }

            try {
                float scale = a.getFloat(R.styleable.StyledText_scale_x, 1);
                setScaleX(scale);
            } catch (Exception ignored) {
            }

            try {
                float scale = a.getFloat(R.styleable.StyledText_scale_y, 1);
                setScaleY(scale);
            } catch (Exception ignored) {
            }

            try {
                int width = a.getDimensionPixelOffset(R.styleable.StyledText_view_width, -1);
                if (width > -1) setViewWidth(width);
            } catch (Exception ignored) {
            }

            try {
                int height = a.getDimensionPixelOffset(R.styleable.StyledText_view_height, -1);
                if (height > -1) setViewHeight(height);
            } catch (Exception ignored) {
            }

            try {
                String text = a.getString(R.styleable.StyledText_android_text);
                if (text != null)
                    setText(text);
            } catch (Exception ignored) {
            }

            try {
                int alignment = a.getInt(R.styleable.StyledText_alignment, -1);
                setAlignment(alignment);
            } catch (Exception ignored) {
            }

            try {
                int color = a.getColor(R.styleable.StyledText_android_textColor, getTextColor());
                setTextColor(color);
            } catch (Exception ignored) {
            }

            try {
                int color = a.getInt(R.styleable.StyledText_textColor, -1);
                setForegroundColor(color);
            } catch (Exception ignored) {
            }

            try {
                int font = a.getInt(R.styleable.StyledText_font_family, 0);
                setFontFamily(font);
            } catch (Exception ignored) {
            }

            try {
                int font = a.getInt(R.styleable.StyledText_view_font, -1);
                setFont(font);
            } catch (Exception ignored) {
            }

            try {
                int style = a.getInt(R.styleable.StyledText_view_font_style, -1);
                setStyle(style);
            } catch (Exception ignored) {
            }

            try {
                int color = a.getColor(R.styleable.StyledText_android_textColorLink, getLinksColor());
                setLinksColor(color);
            } catch (Exception ignored) {
            }

            try {
                String text = a.getString(R.styleable.StyledText_link);
                OnLinkClickListener<StyledText> listener = (view, txt, link) -> {};
                if (text != null)
                    addLinks(listener, text);
            } catch (Exception ignored) {
            }

            try {
                String url = a.getString(R.styleable.StyledText_url);
                String text = a.getString(R.styleable.StyledText_urlText);
                if (url != null && text != null)
                    addUrlLinks(url, text);
            } catch (Exception ignored) {
            }

            try {
                int padding = a.getDimensionPixelOffset(R.styleable.StyledEditor_view_padding, 0);
                setPadding(padding);
            } catch (Exception ignored) {
            }

            try {
                int padding = a.getDimensionPixelOffset(R.styleable.StyledEditor_view_padding_horizontal, 0);
                setPaddingHorizontal(padding);
            } catch (Exception ignored) {
            }

            try {
                int padding = a.getDimensionPixelOffset(R.styleable.StyledEditor_view_padding_vertical, 0);
                setPaddingVertical(padding);
            } catch (Exception ignored) {
            }

            try {
                int padding = a.getDimensionPixelOffset(R.styleable.StyledEditor_view_padding_start, 0);
                setPaddingLeft(padding);
            } catch (Exception ignored) {
            }

            try {
                int padding = a.getDimensionPixelOffset(R.styleable.StyledEditor_view_padding_end, 0);
                setPaddingRight(padding);
            } catch (Exception ignored) {
            }

            try {
                int padding = a.getDimensionPixelOffset(R.styleable.StyledEditor_view_padding_top, 0);
                setPaddingTop(padding);
            } catch (Exception ignored) {
            }

            try {
                int padding = a.getDimensionPixelOffset(R.styleable.StyledEditor_view_padding_bottom, 0);
                setPaddingBottom(padding);
            } catch (Exception ignored) {
            }

            try {
                int background = a.getInt(R.styleable.StyledText_view_background, 11);
                setBackground(background);
            } catch (Exception ignored) {
            }

            a.close();
        }
        // reset();
    }

}
