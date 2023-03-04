package nx.peter.app.android_ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.text.Font;

public class StyledEditor extends AStyledEditor<StyledEditor> {

    public StyledEditor(Context context) {
        super(context);
    }

    public StyledEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_multi_action_editor, this);
        view = findViewById(R.id.view);
        reset();

        if (attrs != null) {
            TypedArray a = obtainStyledAttributes(attrs, R.styleable.StyledEditor);

            float screenDensity  = Resources.getSystem().getDisplayMetrics().density;

            try {
                float size = a.getDimension(R.styleable.StyledEditor_android_textSize, Resources.getSystem().getDimension(R.dimen.text_size));
                setTextSize(TypedValue.COMPLEX_UNIT_SP, size/screenDensity);
            } catch (Exception ignored) {
            }

            try {
                float scale = a.getFloat(R.styleable.StyledEditor_scale, 1);
                setScale(scale);
            } catch (Exception ignored) {}

            try {
                float scale = a.getFloat(R.styleable.StyledEditor_scale_x, 1);
                setScaleX(scale);
            } catch (Exception ignored) {}

            try {
                float scale = a.getFloat(R.styleable.StyledEditor_scale_y, 1);
                setScaleY(scale);
            } catch (Exception ignored) {}

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
                int alignment = a.getInt(R.styleable.StyledEditor_alignment, -1);
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
                int color = a.getColor(R.styleable.StyledEditor_android_textColor, getTextColor());
                setTextColor(color);
            } catch (Exception ignored) {
            }

            try {
                int color = a.getColor(R.styleable.StyledEditor_android_textColorHint, getTextColor());
                setHintColor(color);
            } catch (Exception ignored) {
            }

            try {
                int style = a.getInt(R.styleable.StyledEditor_view_font_style, -1);
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
                int background = a.getInt(R.styleable.StyledEditor_view_background, 11);
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

