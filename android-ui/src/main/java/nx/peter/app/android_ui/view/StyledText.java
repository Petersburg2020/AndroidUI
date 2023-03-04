package nx.peter.app.android_ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.text.Font;

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
        // Font.init(getContext());

        reset();

        if (attrs != null) {
            @SuppressLint("Recycle") TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.StyledText);

            try {
                float size = a.getDimensionPixelSize(R.styleable.StyledText_android_textSize, 16);
                setTextSize(size);
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
                int color = a.getColor(R.styleable.StyledText_android_textColor, getTextColor());
                setTextColor(color);
            } catch (Exception ignored) {
            }

            try {
                int style = a.getInt(R.styleable.StyledText_view_font_style, -1);
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
                int background = a.getInt(R.styleable.StyledText_view_background, 11);
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
                }
            } catch (Exception ignored) {
            }

            a.close();
        }
        // reset();
    }

}
