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

            float screenDensity = Resources.getSystem().getDisplayMetrics().density;

            try {
                float size = a.getDimension(R.styleable.StyledScrollableText_android_textSize, getContext().getResources().getDimension(R.dimen.text_size));
                setTextSize(TypedValue.COMPLEX_UNIT_SP, size / screenDensity);
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
            } catch (Exception ignored) {
            }

            try {
                int color = a.getColor(R.styleable.StyledScrollableText_android_textColor, getTextColor());
                setTextColor(color);
            } catch (Exception ignored) {
            }

            try {
                int color = a.getInt(R.styleable.StyledScrollableText_textColor, -1);
                switch (color) {
                    case 0:
                        setTextColor(Color.BLACK);
                        break;
                    case 1:
                        setTextColor(Color.GRAY);
                        break;
                    case 2:
                        setTextColor(Color.WHITE);
                        break;
                    case 3:
                        setTextColor(Color.RED);
                        break;
                    case 4:
                        setTextColor(Color.BLUE);
                        break;
                    case 5:
                        setTextColor(Color.GREEN);
                        break;
                    case 6:
                        setTextColor(Color.YELLOW);
                        break;
                    case 7:
                        setTextColor(Color.parseColor("#FFFF7500"));
                        break;
                    case 8:
                        setTextColor(Color.parseColor("#FF7500FF"));
                        break;
                    case 9:
                        setTextColor(Color.parseColor("#FFFF00D8"));
                        break;
                    case 10:
                        setTextColor(Color.parseColor("#FF603608"));
                        break;
                    case 11:
                        setTextColor(Color.CYAN);
                        break;
                    case 12:
                        setTextColor(Color.parseColor("#FF00FFFF"));
                }
            } catch (Exception ignored) {
            }

            try {
                int font = a.getInt(R.styleable.StyledScrollableText_font_family, 0);
                switch (font) {
                    case 0:
                        setFont(getFont().set(Typeface.create("sans-serif", Typeface.NORMAL)));
                        break;
                    case 1:
                        setFont(getFont().set(Typeface.create("serif", Typeface.NORMAL)));
                        break;
                    case 3:
                        setFont(getFont().set(Typeface.create("casual", Typeface.NORMAL)));
                        break;
                    case 2:
                        setFont(getFont().set(Typeface.create("cursive", Typeface.NORMAL)));
                        break;
                    case 4:
                        setFont(getFont().set(Typeface.create("monospace", Typeface.NORMAL)));
                }
            } catch (Exception ignored) {
            }

            try {
                int style = a.getInt(R.styleable.StyledScrollableText_view_font_style, -1);
                switch (style) {
                    case 0:
                        setFontStyle(Font.Style.Bold);
                        break;
                    case 1:
                        setFontStyle(Font.Style.Italic);
                        break;
                    case 2:
                        setFontStyle(Font.Style.Regular);
                        break;
                    case 3:
                        setFontStyle(Font.Style.BoldItalic);
                }
            } catch (Exception ignored) {
            }

            try {
                int background = a.getInt(R.styleable.StyledScrollableText_view_background, 11);
                switch (background) {
                    case 0:
                        setBackground(Background.Black);
                        break;
                    case 1:
                        setBackground(Background.Grey);
                        break;
                    case 2:
                        setBackground(Background.White);
                        break;
                    case 3:
                        setBackground(Background.Red);
                        break;
                    case 4:
                        setBackground(Background.Blue);
                        break;
                    case 5:
                        setBackground(Background.Green);
                        break;
                    case 6:
                        setBackground(Background.Yellow);
                        break;
                    case 7:
                        setBackground(Background.Orange);
                        break;
                    case 8:
                        setBackground(Background.Purple);
                        break;
                    case 9:
                        setBackground(Background.Pink);
                        break;
                    case 10:
                        setBackground(Background.Brown);
                        break;
                    case 11:
                        setBackground(Background.Transparent);
                        break;
                    case 12:
                        setBackground(Background.Cyan);
                        break;
                    case 13:
                        setBackground(Background.Lime);
                }
            } catch (Exception ignored) {
            }

            try {
                int direction = a.getInt(R.styleable.StyledScrollableText_scroll_direction, 0);
                switch (direction) {
                    case 0:
                        setScrollDirection(Direction.Vertical);
                        break;
                    case 1:
                        setScrollDirection(Direction.Horizontal);
                        break;
                    case 2:
                        setScrollDirection(Direction.Both);
                }
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
