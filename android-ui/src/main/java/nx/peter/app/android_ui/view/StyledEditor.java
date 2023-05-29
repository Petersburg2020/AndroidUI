package nx.peter.app.android_ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.util.Dimens;

public class StyledEditor extends AStyledEditor<StyledEditor> {

    public StyledEditor(Context context) {
        super(context);
    }

    public StyledEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_styled_editor, this);
        view = findViewById(R.id.view);
        reset();

        if (attrs != null) {
            TypedArray a = obtainStyledAttributes(attrs, R.styleable.StyledEditor);

            try {
                float size = a.getDimension(R.styleable.StyledEditor_android_textSize, (int) Dimens.toSp(16));
                setTextSize(size / getDisplayMetrics().scaledDensity);
            } catch (Exception ignored) {
            }

            try {
                float scale = a.getFloat(R.styleable.StyledEditor_scale, 1);
                setScale(scale);
            } catch (Exception ignored) {
            }

            try {
                float scale = a.getFloat(R.styleable.StyledEditor_scale_x, 1);
                setScaleX(scale);
            } catch (Exception ignored) {
            }

            try {
                float scale = a.getFloat(R.styleable.StyledEditor_scale_y, 1);
                setScaleY(scale);
            } catch (Exception ignored) {
            }

            try {
                String text = a.getString(R.styleable.StyledEditor_android_text);
                if (text != null)
                    setText(text);
            } catch (Exception ignored) {
            }

            try {
                String hint = a.getString(R.styleable.StyledEditor_android_hint);
                if (hint != null)
                    setHint(hint);
            } catch (Exception ignored) {
            }

            try {
                int width = a.getDimensionPixelOffset(R.styleable.StyledEditor_view_width, -1);
                if (width > -1) setViewWidth(width);
            } catch (Exception ignored) {
            }

            try {
                int height = a.getDimensionPixelOffset(R.styleable.StyledEditor_view_height, -1);
                if (height > -1) setViewHeight(height);
            } catch (Exception ignored) {
            }

            try {
                int alignment = a.getInt(R.styleable.StyledEditor_alignment, -1);
                setAlignment(alignment);
            } catch (Exception ignored) {
            }

            try {
                int color = a.getColor(R.styleable.StyledEditor_android_textColor, getTextColor());
                setTextColor(color);
            } catch (Exception ignored) {
            }

            try {
                int color = a.getInt(R.styleable.StyledEditor_textColor, -1);
                setForegroundColor(color);
            } catch (Exception ignored) {
            }

            try {
                int color = a.getColor(R.styleable.StyledEditor_android_textColorHint, getTextColor());
                setHintColor(color);
            } catch (Exception ignored) {
            }

            try {
                int font = a.getInt(R.styleable.StyledEditor_font_family, 0);
                setFontFamily(font);
            } catch (Exception ignored) {
            }

            try {
                int font = a.getInt(R.styleable.StyledEditor_view_font, -1);
                setFont(font);
            } catch (Exception ignored) {
            }

            try {
                int style = a.getInt(R.styleable.StyledEditor_view_font_style, -1);
                setStyle(style);
            } catch (Exception ignored) {
            }

            try {
                String text = a.getString(R.styleable.StyledEditor_link);
                OnLinkClickListener<StyledEditor> listener = (view, txt, link) -> {
                };
                if (text != null)
                    addLinks(listener, text);
            } catch (Exception ignored) {
            }

            try {
                String url = a.getString(R.styleable.StyledEditor_url);
                String text = a.getString(R.styleable.StyledEditor_urlText);
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
                int format = a.getInt(R.styleable.StyledEditor_autoComplete_format, -1);
                setAutoComplete(format);
            } catch (Exception ignored) {}

            try {
                int background = a.getInt(R.styleable.StyledEditor_view_background, 11);
                setBackground(background);
            } catch (Exception ignored) {
            }

            a.close();
        }
    }

    @Override
    protected void reset() {
        super.reset();
        setText("");
        setHint("Here is a sample hint...");
        setAlignment(Alignment.TopLeft);
        setPadding(20);
        // setBackground(Background.Black);
        // setFont(Font.BEFORE_THE_RAIN);
    }

    @Override
    public void setBackgroundResource(int resid) {
        view.setBackgroundResource(resid);
    }
}

