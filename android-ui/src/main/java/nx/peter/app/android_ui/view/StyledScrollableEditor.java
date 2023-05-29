package nx.peter.app.android_ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.text.Font;
import nx.peter.app.android_ui.view.util.Dimens;

public class StyledScrollableEditor extends AStyledScrollableView<StyledScrollableEditor, StyledEditor> implements IStyledEditor<StyledScrollableEditor> {
    public StyledScrollableEditor(Context context) {
        super(context);
    }

    public StyledScrollableEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_styled_scrollable_editor, this);
        vView = findViewById(R.id.v_view);
        hView = findViewById(R.id.h_view);
        vhView = findViewById(R.id.vh_view);
        hContainer = findViewById(R.id.horizontal);
        vContainer = findViewById(R.id.vertical);
        vhContainer = findViewById(R.id.both);

        reset();

        if (attrs != null) {
            TypedArray a = obtainStyledAttributes(attrs, R.styleable.StyledScrollableEditor);

            try {
                float size = a.getDimension(R.styleable.StyledScrollableEditor_android_textSize, (int) Dimens.toSp(16));
                setTextSize(size / getDisplayMetrics().scaledDensity);
            } catch (Exception ignored) {
            }

            try {
                float scale = a.getFloat(R.styleable.StyledScrollableEditor_scale, 1);
                setScale(scale);
            } catch (Exception ignored) {
            }

            try {
                float scale = a.getFloat(R.styleable.StyledScrollableEditor_scale_x, 1);
                setScaleX(scale);
            } catch (Exception ignored) {
            }

            try {
                float scale = a.getFloat(R.styleable.StyledScrollableEditor_scale_y, 1);
                setScaleY(scale);
            } catch (Exception ignored) {
            }

            try {
                String text = a.getString(R.styleable.StyledScrollableEditor_android_text);
                if (text != null)
                    setText(text);
            } catch (Exception ignored) {
            }

            try {
                int alignment = a.getInt(R.styleable.StyledScrollableEditor_alignment, -1);
                setAlignment(alignment);
            } catch (Exception ignored) {
            }

            try {
                int color = a.getColor(R.styleable.StyledScrollableEditor_android_textColor, getTextColor());
                setTextColor(color);
            } catch (Exception ignored) {
            }

            try {
                int color = a.getInt(R.styleable.StyledScrollableEditor_textColor, -1);
                setForegroundColor(color);
            } catch (Exception ignored) {
            }

            try {
                int font = a.getInt(R.styleable.StyledScrollableEditor_font_family, 0);
                setFontFamily(font);
            } catch (Exception ignored) {
            }

            try {
                int font = a.getInt(R.styleable.StyledScrollableEditor_view_font, -1);
                setFont(font);
            } catch (Exception ignored) {
            }

            try {
                int style = a.getInt(R.styleable.StyledScrollableEditor_view_font_style, -1);
                setStyle(style);
            } catch (Exception ignored) {
            }

            try {
                int background = a.getInt(R.styleable.StyledScrollableEditor_view_background, 11);
                setBackground(background);
            } catch (Exception ignored) {
            }

            try {
                int direction = a.getInt(R.styleable.StyledScrollableEditor_scroll_direction, 0);
                setScrollDirection(direction);
            } catch (Exception ignored) {
            }

            try {
                int padding = a.getDimensionPixelOffset(R.styleable.StyledScrollableEditor_view_padding, -1);
                if (padding > -1) setPadding(padding);
            } catch (Exception ignored) {
            }

            try {
                int padding = a.getDimensionPixelOffset(R.styleable.StyledScrollableEditor_view_padding_horizontal, -1);
                if (padding > -1) setPaddingHorizontal(padding);
            } catch (Exception ignored) {
            }

            try {
                int padding = a.getDimensionPixelOffset(R.styleable.StyledScrollableEditor_view_padding_vertical, -1);
                if (padding > -1) setPaddingVertical(padding);
            } catch (Exception ignored) {
            }

            try {
                int padding = a.getDimensionPixelOffset(R.styleable.StyledScrollableEditor_view_padding_bottom, -1);
                if (padding > -1) setPaddingBottom(padding);
            } catch (Exception ignored) {
            }

            try {
                int padding = a.getDimensionPixelOffset(R.styleable.StyledScrollableEditor_view_padding_top, -1);
                if (padding > -1) setPaddingTop(padding);
            } catch (Exception ignored) {
            }

            try {
                int padding = a.getDimensionPixelOffset(R.styleable.StyledScrollableEditor_view_padding_end, -1);
                if (padding > -1) setPaddingRight(padding);
            } catch (Exception ignored) {
            }

            try {
                int padding = a.getDimensionPixelOffset(R.styleable.StyledScrollableEditor_view_padding_start, -1);
                if (padding > -1) setPaddingLeft(padding);
            } catch (Exception ignored) {
            }

            try {
                int alpha = a.getInt(R.styleable.StyledScrollableEditor_textColorAlpha, 255);
                if (alpha >= 0 && alpha <= 255) setTextColorAlpha(alpha);
            } catch (Exception ignored) {
            }

            try {
                int alpha = a.getInt(R.styleable.StyledScrollableEditor_hintColorAlpha, 255);
                if (alpha >= 0 && alpha <= 255) setHintColorAlpha(alpha);
            } catch (Exception ignored) {
            }

            a.close();
        }
    }

    @Override
    protected void reset() {
        super.reset();
    }

    @Override
    public void setTokens(@NonNull Tokens tokens) {
        vView.setTokens(tokens);
        hView.setTokens(tokens);
        vhView.setTokens(tokens);
    }

    @Override
    public void setTokens(@NonNull CharSequence tokens) {
        vView.setTokens(tokens);
        hView.setTokens(tokens);
        vhView.setTokens(tokens);
    }

    @Override
    public Tokens getTokens() {
        return vView.getTokens();
    }

    @Override
    public Tokenizer getTokenizer() {
        return vView.getTokenizer();
    }

    @Override
    public void clearSuggestions() {
        vView.clearSuggestions();
        hView.clearSuggestions();
        vhView.clearSuggestions();
    }

    @Override
    public void addSuggestions(@NonNull CharSequence... suggestions) {
        vView.addSuggestions(suggestions);
        hView.addSuggestions(suggestions);
        vhView.addSuggestions(suggestions);
    }



}
