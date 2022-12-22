package nx.peter.app.android_ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import nx.peter.app.android_ui.view.IView.Background;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.text.*;
import nx.peter.app.android_ui.view.text.FontFamily.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MultiActionEditor extends AbstractView<MultiActionEditor>
        implements MultiActionView<MultiActionEditor> {
    protected MultiAutoCompleteTextView view;
    private SpannableString span;
    private String text;
    private boolean linksClickable;
    private Background background;
    private OnTextChangedListener<MultiActionEditor> listener;
    private OnPropertyChangedListener<MultiActionEditor> propertyChangedListener;
    // private Word oldText;
    protected int horizontal, vertical;
    private List<Text> subTexts;

    protected Tokens tokens;
    protected Tokenizer tokenizer;
    protected Adapter adapter;

    public MultiActionEditor(Context context) {
        super(context);
    }

    public MultiActionEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_multi_action_editor, this);
        view = findViewById(R.id.view);
        // Font.init(getContext());

        reset();

        if (attrs != null) {
            @SuppressLint("Recycle") TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MultiActionEditor);

            try {
                float size = a.getDimensionPixelSize(R.styleable.MultiActionEditor_android_textSize, 16);
                setTextSize(size);
            } catch (Exception ignored) {
            }

            try {
                String text = a.getString(R.styleable.MultiActionEditor_android_text);
                if (text != null)
                    setText(text);
            } catch (Exception ignored) {
            }

            try {
                int alignment = a.getInt(R.styleable.MultiActionEditor_alignment, -1);
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
                int color = a.getColor(R.styleable.MultiActionEditor_android_textColor, getTextColor());
                setTextColor(color);
            } catch (Exception ignored) {
            }

            try {
                int style = a.getInt(R.styleable.MultiActionEditor_view_font_style, -1);
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
                int background = a.getInt(R.styleable.MultiActionEditor_view_background, 11);
                switch (background) {
                    case 0: setBackground(Background.Black); break;
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

        }
        // reset();
    }

    private void reset() {
        subTexts = new ArrayList<>();
        backgrounds = new ArrayList<>();
        bullets = new ArrayList<>();
        colors = new ArrayList<>();
        strikeThroughs = new ArrayList<>();
        fonts = new ArrayList<>();
        images = new ArrayList<>();
        links = new ArrayList<>();
        sizes = new ArrayList<>();
        subscripts = new ArrayList<>();
        superscripts = new ArrayList<>();
        underlines = new ArrayList<>();
        urls = new ArrayList<>();
        
        tokens = Tokens.Dot;
        horizontal = Gravity.END;
        vertical = Gravity.TOP;
        propertyChangedListener = null;
        linksClickable = true;
        listener = null;
        adapter = new Adapter(new ArrayList<>());
        view.setAdapter(adapter);
        background = Background.Transparent;
        // setTextSize(18);
        setAlignment(Alignment.TopLeft);
        setPadding(0);

        // setTextColor(Color.BLUE);
        setText("Try google this color, icon icn, underline, large-text, with emphasis and click here!");

    }

    public void setTokens(@NonNull Tokens tokens) {
        this.tokens = tokens;
        switch (tokens) {
            case Comma:
                setTokenizer(new CommaTokenizer());
                break;
            case Dot:
                setTokenizer(new DotTokenizer());
                break;
            case Space:
                setTokenizer(new SpaceTokenizer());
                break;
        }
    }

    private void setTokenizer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
        view.setTokenizer(tokenizer);
    }

    public void setTokens(@NonNull CharSequence tokens) {
        if (tokens.length() > 0) {
            this.tokens = Tokens.Custom;
            setTokenizer(new CustomTokenizer(tokens.toString()));
        }
    }

    public Tokens getTokens() {
        return tokens;
    }

    public Tokenizer getTokenizer() {
        return tokenizer;
    }

    @Override
    public void setText(@NonNull CharSequence text) {
        CharSequence oldData = getText();
        this.text = text.toString();
        subTexts.clear();
        setSpan(new SpannableString(text));

        if (listener != null) listener.onTextChanged(this, oldData, getText());

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_TEXT, oldData, getText()));
    }

    private void setSpan(SpannableString span) {
        this.span = span;
        view.setText(span);
        view.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void clearText() {
        CharSequence oldData = getText();
        setText("");

        if (listener != null) listener.onTextChanged(this, oldData, getText());

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(
                    this, new PropertyChange<>(PROPERTY_TEXT, oldData, getText()));
    }

    public void clearSuggestions() {
        adapter.setData(new ArrayList<>());
    }

    public void addSuggestions(@NonNull CharSequence... suggestions) {
        String[] vals = new String[suggestions.length];
        for (int i = 0; i < suggestions.length; i++)
            vals[i] = suggestions[i].toString();
        addSuggestions(Arrays.asList(vals));
    }

    public void addSuggestions(@NonNull List<String> suggestions) {
        adapter.addData(suggestions);
    }

    @Override
    public void appendText(@NonNull CharSequence text) {
        appendText(text, getText().length());
    }

    public void appendText(@NonNull CharSequence text, int index) {
        CharSequence oldData = getText();

        if (index == 0) this.text = text + this.text;
        else if (index > 0 && index < this.text.length())
            this.text = this.text.substring(0, index + 1) + text + this.text.substring(index + 1);
        else this.text += text;

        for (Text sub : subTexts)
            if (sub.start - index + 1 >= 0) {
                sub.start += text.length();
                sub.end = sub.start + sub.text.length();
            }
        setup();

        if (listener != null) listener.onTextChanged(this, oldData, getText());

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(
                    this, new PropertyChange<CharSequence>(PROPERTY_TEXT, oldData, getText()));
    }

    @Override
    public void setFont(@NonNull Font font) {
        Font oldData = getFont();
        view.setTypeface(font.get());
        setup();

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(
                    this, new PropertyChange<>(PROPERTY_FONT_STYLE, oldData, getFont()));
    }

    @Override
    public void setFontFamily(@NonNull Family family, @NonNull Style style) {
        setFont(FontFamily.create(family, getContext()).getFont(style));
    }

    @Override
    public Font getFont() {
        return new Font(getContext(), view.getTypeface());
    }

    @Override
    public void setFontStyle(@NonNull Font.Style style) {
        Font.Style oldData = getFontStyle();
        Font font = getFont();
        font.setStyle(style);
        setFont(font);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(
                    this, new PropertyChange<>(PROPERTY_FONT_STYLE, oldData, getFontStyle()));
    }

    @Override
    public Font.Style getFontStyle() {
        return getFont().getStyle();
    }


    public CharSequence getText() {
        return view.getText();
    }

    public void setTextColor(int color) {
        int oldData = getTextColor();
        view.setTextColor(color);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(
                    this, new PropertyChange<>(PROPERTY_TEXT_COLOR, oldData, color));
    }

    public int getTextColor() {
        return view.getTextColors().getDefaultColor();
    }

    public void setLinksColor(int color) {
        int oldData = getLinksColor();
        view.setLinkTextColor(color);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(
                    this, new PropertyChange<>(PROPERTY_LINKS_COLOR, oldData, color));
    }

    public int getLinksColor() {
        return view.getLinkTextColors().getDefaultColor();
    }

    public void setLinkClickable(boolean clickable) {
        boolean oldData = isLinksClickable();

        view.setLinksClickable(clickable);
        linksClickable = clickable;

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(
                    this, new PropertyChange<>(PROPERTY_LINKS_CLICKABLE, oldData, clickable));
    }

    @Override
    public boolean isLinksClickable() {
        return linksClickable;
    }

    @Override
    public void setLineSpacing(float spacing) {
        float oldData = getLineSpacing();
        view.setLineSpacing(0, spacing);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(
                    this, new PropertyChange<>(PROPERTY_LINE_SPACING, oldData, getLineSpacing()));
    }

    @Override
    public void setViewHeight(int height) {
        int oldData = this.getViewHeight();
        super.setViewHeight(height);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(
                    this, new PropertyChange<>(PROPERTY_HEIGHT, oldData, height));
    }

    @Override
    public void setViewWidth(int width) {
        int oldData = this.getViewWidth();
        super.setViewWidth(width);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(
                    this, new PropertyChange<>(PROPERTY_WIDTH, oldData, width));
    }

    @Override
    public void setBackground(@NonNull Background background) {
        Background oldData = getViewBackground();
        this.background = background;
        switch (background) {
            case Black:
                setBackgroundResource(R.drawable.black);
                break;
            case White:
                setBackgroundResource(R.drawable.white);
                break;
            case Grey:
                setBackgroundResource(R.drawable.grey);
                break;
            case Blue:
                setBackgroundResource(R.drawable.blue);
                break;
            case Brown:
                setBackgroundResource(R.drawable.brown);
                break;
            case Cyan:
                setBackgroundResource(R.drawable.cyan);
                break;
            case Lime:
                setBackgroundResource(R.drawable.lime);
                break;
            case Green:
                setBackgroundResource(R.drawable.green);
                break;
            case Yellow:
                setBackgroundResource(R.drawable.yellow);
                break;
            case Red:
                setBackgroundResource(R.drawable.red);
                break;
            case Orange:
                setBackgroundResource(R.drawable.orange);
                break;
            case Purple:
                setBackgroundResource(R.drawable.purple);
                break;
            case Pink:
                setBackgroundResource(R.drawable.pink);
                break;
            default:
                setBackgroundResource(R.drawable.transparent);
        }

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(
                    this, new PropertyChange<>(PROPERTY_BACKGROUND, oldData, getViewBackground()));
    }

    @Override
    public void setBackgroundResource(int resid) {
        view.setBackgroundResource(resid);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        view.setPadding(left, top, right, bottom);
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        view.setPaddingRelative(start, top, end, bottom);
    }

    @Override
    public int getPaddingLeft() {
        return view.getPaddingLeft();
    }

    @Override
    public int getPaddingTop() {
        return view.getPaddingTop();
    }

    @Override
    public int getPaddingBottom() {
        return view.getPaddingBottom();
    }

    @Override
    public int getPaddingRight() {
        return view.getPaddingRight();
    }

    @Override
    public void setTextDirection(int textDirection) {
        view.setTextDirection(textDirection);
    }

    @Override
    public int getTextDirection() {
        return view.getTextDirection();
    }

    @Override
    public void setTextAlignment(int textAlignment) {
        view.setTextAlignment(textAlignment);
    }

    @Override
    public void setHorizontalScrollbarTrackDrawable(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            view.setHorizontalScrollbarTrackDrawable(drawable);
    }

    @Override
    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
        view.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
    }

    @Override
    public void setHorizontalFadingEdgeEnabled(boolean horizontalFadingEdgeEnabled) {
        view.setHorizontalFadingEdgeEnabled(horizontalFadingEdgeEnabled);
    }

    @Override
    public void setHorizontalScrollbarThumbDrawable(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            view.setHorizontalScrollbarThumbDrawable(drawable);
    }

    @Override
    public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
        view.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
    }

    @Override
    public void setVerticalScrollbarThumbDrawable(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            view.setVerticalScrollbarThumbDrawable(drawable);
    }

    @Override
    public void setVerticalScrollbarTrackDrawable(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
            view.setVerticalScrollbarTrackDrawable(drawable);
    }

    @Override
    public void setVerticalScrollbarPosition(int position) {
        view.setVerticalScrollbarPosition(position);
    }

    @Override
    public void setLineHeight(int height) {
        int oldData = getLineHeight();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            view.setLineHeight(height);
        }

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(
                    this, new PropertyChange<>(PROPERTY_LINE_HEIGHT, oldData, getLineHeight()));
    }

    @Override
    public int getLineHeight() {
        return view.getLineHeight();
    }

    @Override
    public void setMaxLine(int lines) {
        int oldData = getMaxLine();
        view.setMaxLines(lines);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(
                    this, new PropertyChange<>(PROPERTY_MAX_LINES, oldData, getMaxLine()));
    }

    @Override
    public void setLetterSpacing(float spacing) {
        float oldData = getLetterSpacing();
        view.setLetterSpacing(spacing);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(
                    this, new PropertyChange<>(PROPERTY_BACKGROUND, oldData, getLetterSpacing()));
    }

    @Override
    public float getLetterSpacing() {
        return view.getLetterSpacing();
    }

    @Override
    public float getLineSpacing() {
        return view.getLineSpacingMultiplier();
    }

    @Override
    public int getMaxLine() {
        return view.getMaxLines();
    }

    @Override
    public int getLineCount() {
        return view.getLineCount();
    }

    @Override
    public Background getViewBackground() {
        return background;
    }

    @Override
    public void setOnTextChangedListener(final OnTextChangedListener<MultiActionEditor> listener) {
        OnTextChangedListener<MultiActionEditor> oldData = getOnTextChangedListener();
        this.listener = listener;

        view.addTextChangedListener(new TextChangeListener<>(this, (view, oldText, newText) -> {
            if (!oldText.equals(newText)) //setup();
                Toast.makeText(view.getContext(), "old: " + oldText + "\nNew: " + newText, Toast.LENGTH_SHORT).show();
            if (listener != null) listener.onTextChanged(view, oldText, newText);
        }));

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(
                    this, new PropertyChange<>(PROPERTY_TEXT_CHANGED, oldData, getOnTextChangedListener()));
    }

    @Override
    public void setOnPropertyChangedListener(OnPropertyChangedListener<MultiActionEditor> listener) {
        OnPropertyChangedListener<MultiActionEditor> oldData = getOnPropertyChangedListener();
        propertyChangedListener = listener;

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(
                    this,
                    new PropertyChange<>(PROPERTY_PROPERTY_CHANGED, oldData, getOnPropertyChangedListener()));
    }

    @Override
    public OnTextChangedListener<MultiActionEditor> getOnTextChangedListener() {
        return listener;
    }

    @Override
    public OnPropertyChangedListener<MultiActionEditor> getOnPropertyChangedListener() {
        return propertyChangedListener;
    }

    @Override
    public boolean post(@Nullable Runnable action) {
        return view.post(action);
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void setHorizontalAlignment(@NonNull HorizontalAlignment alignment) {
        HorizontalAlignment oldData = getHorizontalAlignment();
        switch (alignment) {
            case Center:
                horizontal = Gravity.CENTER_HORIZONTAL;
                break;
            case Left:
                horizontal = Gravity.LEFT;
                break;
            case Right:
                horizontal = Gravity.RIGHT;
        }

        setAlignment();
        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(
                    this,
                    new PropertyChange<HorizontalAlignment>(
                            PROPERTY_ALIGNMENT, oldData, getHorizontalAlignment()));
    }

    @Override
    public Alignment getAlignment() {
        switch (getVerticalAlignment()) {
            case Center:
                switch (getHorizontalAlignment()) {
                    case Center:
                        return Alignment.Center;
                    case Left:
                        return Alignment.CenterLeft;
                    case Right:
                        return Alignment.CenterRight;
                }
                break;
            case Top:
                switch (getHorizontalAlignment()) {
                    case Center:
                        return Alignment.TopCenter;
                    case Left:
                        return Alignment.TopLeft;
                    case Right:
                        return Alignment.TopRight;
                }
                break;
            case Bottom:
                switch (getHorizontalAlignment()) {
                    case Center:
                        return Alignment.BottomCenter;
                    case Left:
                        return Alignment.BottomLeft;
                    case Right:
                        return Alignment.BottomRight;
                }
        }
        return Alignment.TopLeft;
    }

    @Override
    public void setAlignment(@NonNull Alignment alignment) {
        VerticalAlignment vertical = getVerticalAlignment();
        HorizontalAlignment horizontal = getHorizontalAlignment();
        switch (alignment) {
            case Center:
                vertical = VerticalAlignment.Center;
                horizontal = HorizontalAlignment.Center;
                break;
            case CenterLeft:
                vertical = VerticalAlignment.Center;
                horizontal = HorizontalAlignment.Left;
                break;
            case CenterRight:
                vertical = VerticalAlignment.Center;
                horizontal = HorizontalAlignment.Right;
                break;
            case TopCenter:
                vertical = VerticalAlignment.Top;
                horizontal = HorizontalAlignment.Center;
                break;
            case TopLeft:
                vertical = VerticalAlignment.Top;
                horizontal = HorizontalAlignment.Left;
                break;
            case TopRight:
                vertical = VerticalAlignment.Top;
                horizontal = HorizontalAlignment.Right;
                break;
            case BottomCenter:
                vertical = VerticalAlignment.Bottom;
                horizontal = HorizontalAlignment.Center;
                break;
            case BottomLeft:
                vertical = VerticalAlignment.Bottom;
                horizontal = HorizontalAlignment.Left;
                break;
            case BottomRight:
                vertical = VerticalAlignment.Bottom;
                horizontal = HorizontalAlignment.Right;
        }
        setAlignment(horizontal, vertical);
    }

    @Override
    public void setAlignment(
            @NonNull HorizontalAlignment horizontal, @NonNull VerticalAlignment vertical) {
        setVerticalAlignment(vertical);
        setHorizontalAlignment(horizontal);
    }

    @Override
    public HorizontalAlignment getHorizontalAlignment() {
        switch (horizontal) {
            case Gravity.CENTER_HORIZONTAL:
                return HorizontalAlignment.Center;
            case Gravity.RIGHT:
                return HorizontalAlignment.Right;
            default:
                return HorizontalAlignment.Left;
        }
    }

    @Override
    public void setVerticalAlignment(@NonNull VerticalAlignment alignment) {
        VerticalAlignment oldData = getVerticalAlignment();
        switch (alignment) {
            case Center:
                vertical = Gravity.CENTER_VERTICAL;
                break;
            case Bottom:
                vertical = Gravity.BOTTOM;
                break;
            case Top:
                vertical = Gravity.TOP;
        }

        setAlignment();
        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(
                    this,
                    new PropertyChange<VerticalAlignment>(
                            PROPERTY_ALIGNMENT, oldData, getVerticalAlignment()));
    }

    @Override
    public VerticalAlignment getVerticalAlignment() {
        switch (vertical) {
            case Gravity.CENTER_VERTICAL:
                return VerticalAlignment.Center;
            case Gravity.BOTTOM:
                return VerticalAlignment.Bottom;
            default:
                return VerticalAlignment.Top;
        }
    }

    protected void setAlignment() {
        view.setGravity(horizontal | vertical);
    }

    public void setTextSize(float size) {
        float oldData = getTextSize();
        view.setTextSize(size);
        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(
                    this, new PropertyChange<>(PROPERTY_PROPERTY_CHANGED, oldData, getTextSize()));
    }

    public float getTextSize() {
        return view.getTextSize();
    }

    public void addLinks(OnLinkClickListener<MultiActionEditor> link, CharSequence... subs) {
        addSubTexts(SpannableTextFormat.getLinkSpan(span, text, link, subs).texts);
    }

    public void addLinks(TextLink... links) {
        addLinks(Arrays.asList(links));
    }

    public void addLinks(@NonNull List<TextLink> links) {
        for (Text text : links) addSubText(text);
        setup();
    }

    @Override
    public void addUnderlines(@NonNull List<TextUnderline> underlines) {
        for (Text text : underlines) addSubText(text);
        setup();
    }

    @Override
    public void addUnderlines(CharSequence... underlines) {
        addSubTexts(SpannableTextFormat.getUnderlineSpan(span, text, underlines).texts);
    }

    public void addUrlLinks(@NonNull CharSequence url, CharSequence... subs) {
        addSubTexts(SpannableTextFormat.getUrlSpan(span, text, url, subs).texts);
    }

    public void addUrlLinks(TextURL... urls) {
        addUrlLinks(Arrays.asList(urls));
    }

    public void addUrlLinks(@NonNull List<TextURL> urls) {
        for (Text text : urls) addSubText(text);
        setup();
    }

    public void addSubColors(int color, CharSequence... subs) {
        addSubTexts(SpannableTextFormat.getColorSpan(span, text, color, subs).texts);
    }

    public void addSubColors(TextColor... colors) {
        addSubColors(Arrays.asList(colors));
    }

    public void addSubColors(@NonNull List<TextColor> colors) {
        for (Text text : colors) addSubText(text);
        setup();
    }

    @Override
    public void addSubBackgrounds(int color, CharSequence... subs) {
        addSubTexts(SpannableTextFormat.getBackgroundSpan(span, text, color, subs).texts);
    }

    @Override
    public void addSubBackgrounds(@NonNull List<TextBackground> backgrounds) {
        for (Text text : backgrounds) addSubText(text);
        setup();
    }

    @Override
    public void addSubImages(int image, float size, CharSequence... subs) {
        addSubTexts(
                SpannableTextFormat.getImageSpan(span, text, getContext(), image, size, subs).texts);
    }

    @Override
    public void addSubImages(int image, int tint, float size, CharSequence... subs) {
        addSubTexts(
                SpannableTextFormat.getImageSpan(span, text, getContext(), image, tint, size, subs).texts);
    }

    @Override
    public void addSubImages(int image, CharSequence... subs) {
        addSubImages(image, getTextSize(), subs);
    }

    @Override
    public void addSubImages(int image, int tint, CharSequence... subs) {
        addSubImages(image, tint, getTextSize(), subs);
    }

    @Override
    public void addSubImages(@NonNull List<TextImage> images) {
        for (Text text : images) addSubText(text);
        setup();
    }

    @Override
    public void addStrikeThroughs(CharSequence... subs) {
        addStrikeThroughs(getTextColor(), subs);
    }

    @Override
    public void addStrikeThroughs(int color, CharSequence... subs) {
        addSubTexts(SpannableTextFormat.getStrikeThroughSpan(span, text, color, subs).texts);
    }

    @Override
    public void addStrikeThroughs(@NonNull List<TextStrikeThrough> strikeThroughs) {
        for (Text text : strikeThroughs) addSubText(text);
        setup();
    }

    @Override
    public void addBullets(CharSequence... subs) {
        addSubTexts(SpannableTextFormat.getBulletSpan(span, text, subs).texts);
    }

    @Override
    public void addBullets(int gap, CharSequence... subs) {
        addBullets(gap, -1, subs);
    }

    @Override
    public void addBullets(int gap, int color, CharSequence... subs) {
        addBullets(gap, color, -1, subs);
    }

    @Override
    public void addBullets(int gap, int color, int radius, CharSequence... subs) {
        addSubTexts(SpannableTextFormat.getBulletSpan(span, text, gap, color, radius, subs).texts);
    }

    @Override
    public void addBullets(@NonNull List<TextBullet> bullets) {
        for (Text text : bullets) addSubText(text);
        setup();
    }

    @Override
    public void addSubscripts(CharSequence... subs) {
        addSubTexts(SpannableTextFormat.getSubscriptSpan(span, text, subs).texts);
    }

    @Override
    public void addSubscripts(@NonNull List<TextSubscript> subscripts) {
        for (Text text : subscripts) addSubText(text);
        setup();
    }

    @Override
    public void addSuperscripts(CharSequence... subs) {
        addSubTexts(SpannableTextFormat.getSuperscriptSpan(span, text, subs).texts);
    }

    @Override
    public void addSuperscripts(@NonNull List<TextSuperscript> superscripts) {
        for (Text text : superscripts) addSubText(text);
        setup();
    }

    public void addSubSizes(int size, CharSequence... subs) {
        addSubTexts(SpannableTextFormat.getSizeSpan(span, text, size, subs).texts);
    }

    public void addSubSizes(TextSize... colors) {
        addSubSizes(Arrays.asList(colors));
    }

    public void addSubSizes(@NonNull List<TextSize> sizes) {
        for (Text text : sizes) addSubText(text);
        setup();
    }

    public void addSubFonts(@NonNull Font font, CharSequence... subs) {
        addSubTexts(SpannableTextFormat.getFontSpan(span, text, font, subs).texts);
    }

    public void addSubFonts(TextFont... fonts) {
        addSubFonts(Arrays.asList(fonts));
    }

    @Override
    public void addSubFonts(Font.Style style, CharSequence... subs) {
        addSubFonts(getFont().setStyle(style), subs);
    }

    public void addSubFonts(@NonNull List<TextFont> fonts) {
        for (Text text : fonts) addSubText(text);
        setup();
    }

    public boolean removeSubText(@NonNull Text text) {
        for (Text txt : subTexts)
            if (txt.equals(text)) {
                return subTexts.remove(txt);
            }
        return false;
    }

    @Override
    public void addMultiStyles(
            int gap,
            int color,
            int radius,
            int size,
            @NonNull CharSequence url,
            @NonNull Font font,
            CharSequence... subs) {
        addBullets(gap, color, radius, subs);
        addSubSizes(size, subs);
        addUrlLinks(url, subs);
        addSubFonts(font, subs);
        addSubColors(color, subs);
    }

    private void addSubTexts(@NonNull List<Text> texts) {
        for (Text text : texts) addSubText(text);
        setup();
    }

    @Override
    public boolean containsSubText(@NonNull Text text) {
        return getSubText(text) != null;
    }

    private void addSubText(@NonNull Text text) {
        if (containsSubText(text))
            if (getSubText(text).type == text.type) subTexts.remove(getSubText(text));
        text.view = this;
        subTexts.add(text);
    }

    private Text getSubText(@NonNull Text text) {
        for (Text sub : subTexts) if (sub.equals(text)) return sub;
        return null;
    }

    private List<TextColor> colors;
    private List<TextFont> fonts;
    private List<TextLink> links;
    private List<TextSize> sizes;
    private List<TextStrikeThrough> strikeThroughs;
    private List<TextURL> urls;
    private List<TextImage> images;
    private List<TextBullet> bullets;
    private List<TextBackground> backgrounds;
    private List<TextSubscript> subscripts;
    private List<TextUnderline> underlines;
    private List<TextSuperscript> superscripts;

    private void setup() {
        backgrounds = new ArrayList<>();
        bullets = new ArrayList<>();
        colors = new ArrayList<>();
        strikeThroughs = new ArrayList<>();
        fonts = new ArrayList<>();
        images = new ArrayList<>();
        links = new ArrayList<>();
        sizes = new ArrayList<>();
        subscripts = new ArrayList<>();
        superscripts = new ArrayList<>();
        underlines = new ArrayList<>();
        urls = new ArrayList<>();

        List<Text> oldData = subTexts;
        for (Text sub : subTexts) {
            switch (sub.type) {
                case Background:
                    backgrounds.add((TextBackground) sub);
                    break;
                case Bullet:
                    bullets.add((TextBullet) sub);
                    break;
                case Color:
                    colors.add((TextColor) sub);
                    break;
                case Edit:
                    strikeThroughs.add((TextStrikeThrough) sub);
                    break;
                case Font:
                    fonts.add((TextFont) sub);
                    break;
                case Image:
                    images.add((TextImage) sub);
                    break;
                case Link:
                    links.add((TextLink) sub);
                    break;
                case Size:
                    sizes.add((TextSize) sub);
                    break;
                case Subscript:
                    subscripts.add((TextSubscript) sub);
                    break;
                case Superscript:
                    superscripts.add((TextSuperscript) sub);
                    break;
                case Underline:
                    underlines.add((TextUnderline) sub);
                    break;
                case Url:
                    urls.add((TextURL) sub);
                    break;
            }
        }

        span = new SpannableString(text);
        subTexts.clear();

        SpannableTextFormat.SpanText spanText = SpannableTextFormat.getBulletSpan(span, text, bullets);
        span = spanText.span;
        subTexts.addAll(spanText.texts);

        spanText = SpannableTextFormat.getBackgroundSpan(span, text, backgrounds);
        span = spanText.span;
        subTexts.addAll(spanText.texts);

        spanText = SpannableTextFormat.getFontSpan(span, text, fonts);
        span = spanText.span;
        subTexts.addAll(spanText.texts);

        spanText = SpannableTextFormat.getStrikeThroughSpan(span, text, strikeThroughs);
        span = spanText.span;
        subTexts.addAll(spanText.texts);

        spanText = SpannableTextFormat.getLinkSpan(span, text, links);
        span = spanText.span;
        subTexts.addAll(spanText.texts);

        spanText = SpannableTextFormat.getUrlSpan(span, text, urls);
        span = spanText.span;
        subTexts.addAll(spanText.texts);

        spanText = SpannableTextFormat.getColorSpan(span, text, colors);
        span = spanText.span;
        subTexts.addAll(spanText.texts);

        spanText = SpannableTextFormat.getSizeSpan(span, text, sizes);
        span = spanText.span;
        subTexts.addAll(spanText.texts);

        spanText = SpannableTextFormat.getImageSpan(span, text, images);
        span = spanText.span;
        subTexts.addAll(spanText.texts);

        spanText = SpannableTextFormat.getSuperscriptSpan(span, text, superscripts);
        span = spanText.span;
        subTexts.addAll(spanText.texts);

        spanText = SpannableTextFormat.getSubscriptSpan(span, text, subscripts);
        span = spanText.span;
        subTexts.addAll(spanText.texts);

        spanText = SpannableTextFormat.getUnderlineSpan(span, text, underlines);
        span = spanText.span;
        subTexts.addAll(spanText.texts);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(
                    this, new PropertyChange<>(PROPERTY_PROPERTY_CHANGED, oldData, subTexts));
        setSpan(span);
    }

    @Override
    public SubTexts<TextSize> getSubSizes(CharSequence... subs) {
        List<TextSize> texts = new ArrayList<>();
        for (CharSequence sub : subs)
            for (TextSize txt : sizes) if (txt.text.contentEquals(sub)) texts.add(txt);
        return new SubTexts<>(text, texts);
    }

    @Override
    public TextSize getSubSize(@NonNull CharSequence sub, int start) {
        for (TextSize txt : sizes) if (txt.text.contentEquals(sub) && txt.start == start) return txt;
        return null;
    }

    @Override
    public SubTexts<TextColor> getSubColors(CharSequence... subs) {
        List<TextColor> texts = new ArrayList<>();
        for (CharSequence sub : subs)
            for (TextColor txt : colors) if (txt.text.contentEquals(sub)) texts.add(txt);
        return new SubTexts<>(text, texts);
    }

    @Override
    public TextColor getSubColor(@NonNull CharSequence sub, int start) {
        for (TextColor txt : colors) if (txt.text.contentEquals(sub) && txt.start == start) return txt;
        return null;
    }

    @Override
    public SubTexts<TextFont> getSubFonts(CharSequence... subs) {
        List<TextFont> texts = new ArrayList<>();
        for (CharSequence sub : subs)
            for (TextFont txt : fonts) if (txt.text.contentEquals(sub)) texts.add(txt);
        return new SubTexts<>(text, texts);
    }

    @Override
    public TextFont getSubFont(@NonNull CharSequence sub, int start) {
        for (TextFont txt : fonts) if (txt.text.contentEquals(sub) && txt.start == start) return txt;
        return null;
    }

    @Override
    public SubTexts<TextImage> getSubImages(CharSequence... subs) {
        List<TextImage> texts = new ArrayList<>();
        for (CharSequence sub : subs)
            for (TextImage txt : images) if (txt.text.contentEquals(sub)) texts.add(txt);
        return new SubTexts<>(text, texts);
    }

    @Override
    public TextImage getSubImage(@NonNull CharSequence sub, int start) {
        for (TextImage txt : images) if (txt.text.contentEquals(sub) && txt.start == start) return txt;
        return null;
    }

    @Override
    public SubTexts<Text> getSubTexts(@NonNull CharSequence sub, int start) {
        List<Text> texts = new ArrayList<>();
        for (Text txt : subTexts)
            if (notContains(texts, txt) && txt.text.contentEquals(sub) && txt.start == start)
                texts.add(txt);
        return new SubTexts<>(text, texts);
    }

    @Override
    public SubTexts<Text> getSubTexts(CharSequence... subs) {
        List<Text> texts = new ArrayList<>();
        for (CharSequence sub : subs)
            for (Text txt : subTexts)
                if (notContains(texts, txt) && txt.text.contentEquals(sub)) texts.add(txt);
        return new SubTexts<>(text, texts);
    }

    protected boolean notContains(List<Text> texts, Text sub) {
        for (Text txt : texts) if (txt.equals(sub)) return false;
        return true;
    }

    @Override
    public SubTexts<Text> getSubTexts() {
        return new SubTexts<>(text, subTexts);
    }

    @Override
    public SubTexts<TextLink> getLinks() {
        return new SubTexts<>(text, links);
    }

    @Override
    public SubTexts<TextBullet> getBullets() {
        return new SubTexts<>(text, bullets);
    }

    @Override
    public SubTexts<TextURL> getUrlLinks() {
        return new SubTexts<>(text, urls);
    }

    @Override
    public SubTexts<TextImage> getSubImages() {
        return new SubTexts<>(text, images);
    }

    @Override
    public SubTexts<TextSubscript> getSubscripts() {
        return new SubTexts<TextSubscript>(text, subscripts);
    }

    @Override
    public SubTexts<TextSuperscript> getSuperscripts() {
        return new SubTexts<TextSuperscript>(text, superscripts);
    }

    @Override
    public SubTexts<TextFont> getSubFonts() {
        return new SubTexts<>(text, fonts);
    }

    @Override
    public SubTexts<TextSize> getSubSizes() {
        return new SubTexts<>(text, sizes);
    }

    @Override
    public SubTexts<TextColor> getSubColors() {
        return new SubTexts<>(text, colors);
    }

    @Override
    public SubTexts<TextStrikeThrough> getStrikeThroughs() {
        return new SubTexts<>(text, strikeThroughs);
    }

    @Override
    public SubTexts<TextUnderline> getUnderlines() {
        return new SubTexts<>(text, underlines);
    }


    public enum Tokens {
        Custom,
        Comma,
        Dot,
        Space
    }

    protected static class Adapter extends BaseAdapter implements Filterable {
        protected List<String> data, temp;

        public Adapter(List<String> data) {
            this.data = data;
            temp = new ArrayList<>();
        }

        public void setData(@NonNull List<String> data) {
            if (!this.data.isEmpty()) this.data.clear();
            addData(data);
        }

        public void addData(@NonNull List<String> data) {
            for (String d : data)
                if (!this.data.contains(d)) this.data.add(d);
            notifyDataSetChanged();
        }

        public void removeData(List<String> data) {
            for (String d : data)
                this.data.remove(d);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return temp.size();
        }

        @Override
        public String getItem(int position) {
            return temp.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_multi_action_text_editor, parent, false);
            MultiActionText item = convertView.findViewById(R.id.item);
            item.setAlignment(Alignment.CenterLeft);
            item.setText(getItem(position));
            item.setTextColor(Color.BLACK);
            return convertView;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    if (constraint != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            data.sort(Comparator.naturalOrder());
                        FilterResults filter = new FilterResults();
                        filter.values = data;
                        filter.count = data.size();
                        return filter;
                    }
                    return new FilterResults();
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        if (!temp.isEmpty()) temp.clear();
                        temp = (List<String>) results.values;
                        notifyDataSetChanged();
                    }
                }
            };
        }
    }

    public static class CustomTokenizer extends Tokenizer {
        public CustomTokenizer(@NonNull String tokens) {
            super(tokens);
        }
    }

    public static class DotTokenizer extends Tokenizer {
        public DotTokenizer() {
            super(".");
        }
    }

    public static class CommaTokenizer extends Tokenizer {
        public CommaTokenizer() {
            super(",");
        }
    }

    public static class SpaceTokenizer extends Tokenizer {
        public SpaceTokenizer() {
            super(" ");
        }
    }

    // As this java class implements MultiAutoCompleteTextView.Tokenizer,
    // we should write 3 methods i.e. findTokenStart,findTokenEnd and terminateToken
    public abstract static class Tokenizer implements MultiAutoCompleteTextView.Tokenizer {
        protected String tokens;

        public Tokenizer(@NonNull String tokens) {
            this.tokens = tokens;
        }

        protected boolean contains(char letter) {
            return tokens.contains(letter + "");
        }

        // Returns the start of the token that ends at offset cursor within text.
        public int findTokenStart(CharSequence inputText, int cursor) {
            int idx = cursor;

            while (idx > 0 && !contains(inputText.charAt(idx - 1))) idx--;

            while (idx < cursor && contains(inputText.charAt(idx))) idx++;

            return idx;
        }

        // Returns the end of the token (minus trailing punctuation) that
        // begins at offset cursor within text.
        public int findTokenEnd(CharSequence inputText, int cursor) {
            int idx = cursor;
            int length = inputText.length();

            while (idx < length)
                if (contains(inputText.charAt(idx))) return idx;
                else idx++;

            return length;
        }

        // Returns text, modified, if necessary, to ensure that it ends with a token terminator
        // (for example a space or comma).
        public CharSequence terminateToken(CharSequence inputText) {
            int idx = inputText.length();
            while (idx > 0 && contains(inputText.charAt(idx - 1))) idx--;

            if (idx > 0 && contains(inputText.charAt(idx - 1))) return inputText;
            else {
                if (inputText instanceof Spanned) {
                    SpannableString sp = new SpannableString(inputText + " ");
                    TextUtils.copySpansFrom((Spanned) inputText, 0, inputText.length(), Object.class, sp, 0);
                    return sp;
                } else return inputText + " ";
            }
        }
    }
}

