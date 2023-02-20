package nx.peter.app.android_ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.text.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiActionScrollableText extends AbstractView<MultiActionScrollableText> implements MultiActionView<MultiActionScrollableText> {
    protected TextView hView, vView, vhView;
    private SpannableString span;
    private LinearLayout layout;
    private String text;
    private boolean linksClickable, scrollable, singleLine;
    private OnTextChangedListener<MultiActionScrollableText> listener;
    private OnPropertyChangedListener<MultiActionScrollableText> propertyChangedListener;
    // private Word oldText;
    protected int horizontal, vertical;
    private ScrollView vContainer, vhContainer;
    private HorizontalScrollView hContainer;
    private List<Text> subTexts;
    protected Ellipsize ellipsize;


    public MultiActionScrollableText(Context context) {
        super(context);
    }

    public MultiActionScrollableText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_multi_action_scrollable_text, this);
        vView = findViewById(R.id.v_view);
        hView = findViewById(R.id.h_view);
        vhView = findViewById(R.id.vh_view);
        hContainer = findViewById(R.id.horizontal);
        vContainer = findViewById(R.id.vertical);
        vhContainer = findViewById(R.id.both);
        layout = findViewById(R.id.layout);
        // Font.init(getContext());

        reset();
        /*
        if (attrs != null) {
            @SuppressLint("Recycle") TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.MultiActionText);

            try {
                float size = a.getDimensionPixelSize(R.styleable.MultiActionText_android_textSize, 16);
                setTextSize(size);
            } catch (Exception ignored) {}

            try {
                String text = a.getString(R.styleable.MultiActionText_android_text);
                if (text != null)
                    setText(text);
            } catch (Exception ignored) {}

            try {
                int alignment = a.getInt(R.styleable.MultiActionText_alignment, -1);
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
                int color = a.getColor(R.styleable.MultiActionText_android_textColor, getTextColor());
                setTextColor(color);
            } catch (Exception ignored) {}

            try {
                int style = a.getInt(R.styleable.MultiActionText_view_font_style, -1);
                switch (style) {
                    case 0: setFontStyle(Font.Style.Bold); break;
                    case 1: setFontStyle(Font.Style.Italic); break;
                    case 2: setFontStyle(Font.Style.Regular); break;
                    case 3: setFontStyle(Font.Style.BoldItalic);
                }
            } catch (Exception ignored) {}

            try {
                int background = a.getInt(R.styleable.MultiActionText_view_background, 11);
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
                    case 11: setBackground(Background.Transparent);
                }
            } catch (Exception ignored) {}

        }*/
        // reset();
    }

    protected void reset() {
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

        horizontal = Gravity.END;
        vertical = Gravity.TOP;
        background = Background.Transparent;
        propertyChangedListener = null;
        linksClickable = true;
        listener = null;
        singleLine = false;
        ellipsize = Ellipsize.None;

        setPadding(0);
        setScrollDirection(Direction.Vertical);
        setText("Try google this color, icon icn, underline, large-text, with emphasis and click here!");

        // setFont(Font.SERIF);
    }

    @Override
    public void setText(@NonNull CharSequence text) {
        CharSequence oldData = getText();
        this.text = text.toString();
        subTexts.clear();
        setSpan(new SpannableString(text));

        if (listener != null)
            listener.onTextChanged(this, oldData, getText());

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_TEXT, oldData, getText()));
    }

    private void setSpan(SpannableString span) {
        this.span = span;
        vView.setText(span);
        hView.setText(span);
        vView.setMovementMethod(LinkMovementMethod.getInstance());
        hView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setScrollDirection(@NonNull Direction direction) {
        if (direction.equals(Direction.Horizontal)) {
            vContainer.setVisibility(GONE);
            vhContainer.setVisibility(GONE);
            hContainer.setVisibility(VISIBLE);
        } else if (direction.equals(Direction.Vertical)){
            vContainer.setVisibility(VISIBLE);
            vhContainer.setVisibility(GONE);
            hContainer.setVisibility(GONE);
        } else {
            vContainer.setVisibility(GONE);
            vhContainer.setVisibility(VISIBLE);
            hContainer.setVisibility(GONE);
        }
    }

    public Direction getScrollDirection() {
        return isVisible(vContainer) ? Direction.Vertical : isVisible(hContainer) ? Direction.Horizontal : Direction.Both;
    }

    protected boolean isVisible(View view) {
        return view.getVisibility() == VISIBLE;
    }

    @Override
    public void setEllipsize(Ellipsize ellipsize) {
        this.ellipsize = ellipsize != null ? ellipsize : Ellipsize.None;
        setSingleLine(!isSingleLine() && !this.ellipsize.equals(Ellipsize.None));
        vView.setHorizontallyScrolling(this.ellipsize.equals(Ellipsize.Marquee));
        switch (this.ellipsize) {
            case End:
                vView.setEllipsize(TextUtils.TruncateAt.END);
                hView.setEllipsize(TextUtils.TruncateAt.END);
                break;
            case Middle:
                vView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                hView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
                break;
            case Marquee:
                vView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                hView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                break;
            case Start:
                vView.setEllipsize(TextUtils.TruncateAt.START);
                hView.setEllipsize(TextUtils.TruncateAt.START);
                break;
            case None:
                vView.setEllipsize(null);
                hView.setEllipsize(null);
        }
    }

    @Override
    public Ellipsize getEllipsize() {
        return ellipsize;
    }

    @Override
    public void setSingleLine(boolean isSingleLine) {
        vView.setSingleLine(isSingleLine);
        hView.setSingleLine(isSingleLine);
        singleLine = isSingleLine;
    }

    @Override
    public boolean isSingleLine() {
        return singleLine;
    }

    @Override
    public void clearText() {
        CharSequence oldData = getText();
        setText("");

        if (listener != null)
            listener.onTextChanged(this, oldData, getText());

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_TEXT, oldData, getText()));
    }

    @Override
    public void appendText(@NonNull CharSequence text) {
        appendText(text, getText().length());
    }

    public void appendText(@NonNull CharSequence text, int index) {
        CharSequence oldData = getText();

        if (index == 0)
            this.text = text + this.text;
        else if (index > 0 && index < this.text.length())
            this.text = this.text.substring(0, index + 1) + text + this.text.substring(index + 1);
        else
            this.text += text;

        for (Text sub : subTexts)
            if (sub.start - index + 1 >= 0) {
                sub.start += text.length();
                sub.end = sub.start + sub.text.length();
            }
        setup();

        if (listener != null)
            listener.onTextChanged(this, oldData, getText());

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<CharSequence>(PROPERTY_TEXT, oldData, getText()));
    }

    @Override
    public void setFont(@NonNull Font font) {
        Font oldData = getFont();
        hView.setTypeface(font.get());
        vView.setTypeface(font.get());
        setup();

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_FONT_STYLE, oldData, getFont()));
    }

    @Override
    public Font getFont() {
        return new Font(getContext(), vView.getTypeface());
    }

    @Override
    public void setFontFamily(@NonNull FontFamily.Family family, @NonNull FontFamily.Style style) {
        setFont(FontFamily.create(family, getContext()).getFont(style));
    }

    @Override
    public void setFontStyle(@NonNull Font.Style style) {
        Font.Style oldData = getFontStyle();
        Font font = getFont();
        font.setStyle(style);
        setFont(font);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_FONT_STYLE, oldData, getFontStyle()));
    }

    @Override
    public Font.Style getFontStyle() {
        return getFont().getStyle();
    }

    public CharSequence getText() {
        return vView.getText();
    }

    public void setTextColor(int color) {
        int oldData = getTextColor();
        vView.setTextColor(color);
        hView.setTextColor(color);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_TEXT_COLOR, oldData, color));
    }

    public int getTextColor() {
        return vView.getTextColors().getDefaultColor();
    }

    public void setLinksColor(int color) {
        int oldData = getLinksColor();
        vView.setLinkTextColor(color);
        hView.setLinkTextColor(color);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_LINKS_COLOR, oldData, color));
    }

    public int getLinksColor() {
        return vView.getLinkTextColors().getDefaultColor();
    }

    public void setLinkClickable(boolean clickable) {
        boolean oldData = isLinksClickable();

        vView.setLinksClickable(clickable);
        hView.setLinksClickable(clickable);
        linksClickable = clickable;

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_LINKS_CLICKABLE, oldData, clickable));
    }

    @Override
    public boolean isLinksClickable() {
        return linksClickable;
    }

    @Override
    public void setLineSpacing(float spacing) {
        float oldData = getLineSpacing();
        vView.setLineSpacing(0, spacing);
        hView.setLineSpacing(0, spacing);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_LINE_SPACING, oldData, getLineSpacing()));
    }

    @Override
    public boolean post(@Nullable Runnable action) {
        return hView.post(action) && vView.post(action);
    }

    @Override
    public void setViewHeight(int height) {
        int oldData = this.getViewHeight();

        LayoutParams lp = (LayoutParams) getLayoutParams();
        lp.height = height;
        setLayoutParams(lp);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_HEIGHT, oldData, height));
    }

    @Override
    public void setViewWidth(int width) {
        int oldData = this.getViewWidth();
        LayoutParams lp = (LayoutParams) getLayoutParams();
        lp.width = width;
        setLayoutParams(lp);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_WIDTH, oldData, width));
    }


    @Override
    public void setBackground(@NonNull Background background) {
        Background oldData = getViewBackground();
        this.background = background;
        super.setBackground(background);
        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_BACKGROUND, oldData, getViewBackground()));
    }

    @Override
    public void setBackgroundResource(int resid) {
        vView.setBackgroundResource(resid);
        hView.setBackgroundResource(resid);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        vView.setPadding(left, top, right, bottom);
        hView.setPadding(left, top, right, bottom);
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        vView.setPaddingRelative(start, top, end, bottom);
        hView.setPaddingRelative(start, top, end, bottom);
    }

    @Override
    public int getPaddingLeft() {
        return vView.getPaddingLeft();
    }

    @Override
    public int getPaddingTop() {
        return vView.getPaddingTop();
    }

    @Override
    public int getPaddingBottom() {
        return vView.getPaddingBottom();
    }

    @Override
    public int getPaddingRight() {
        return vView.getPaddingRight();
    }

    @Override
    public void setTextDirection(int textDirection) {
        vView.setTextDirection(textDirection);
    }

    @Override
    public int getTextDirection() {
        return vView.getTextDirection();
    }

    @Override
    public void setTextAlignment(int textAlignment) {
        vView.setTextAlignment(textAlignment);
        hView.setTextAlignment(textAlignment);
    }

    @Override
    public void setHorizontalScrollbarTrackDrawable(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vView.setHorizontalScrollbarTrackDrawable(drawable);
            hView.setHorizontalScrollbarTrackDrawable(drawable);
        }
    }

    @Override
    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
        vView.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
        hView.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
    }

    @Override
    public void setHorizontalFadingEdgeEnabled(boolean horizontalFadingEdgeEnabled) {
        vView.setHorizontalFadingEdgeEnabled(horizontalFadingEdgeEnabled);
        hView.setHorizontalFadingEdgeEnabled(horizontalFadingEdgeEnabled);
    }

    @Override
    public void setHorizontalScrollbarThumbDrawable(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vView.setHorizontalScrollbarThumbDrawable(drawable);
            hView.setHorizontalScrollbarThumbDrawable(drawable);
        }
    }

    @Override
    public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
        vView.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
        hView.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
    }

    @Override
    public void setVerticalScrollbarThumbDrawable(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vView.setVerticalScrollbarThumbDrawable(drawable);
            hView.setVerticalScrollbarThumbDrawable(drawable);
        }
    }

    @Override
    public void setVerticalScrollbarTrackDrawable(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vView.setVerticalScrollbarTrackDrawable(drawable);
            hView.setVerticalScrollbarTrackDrawable(drawable);
        }
    }

    @Override
    public void setVerticalScrollbarPosition(int position) {
        vView.setVerticalScrollbarPosition(position);
        hView.setVerticalScrollbarPosition(position);
    }


    @Override
    public void setLineHeight(int height) {
        int oldData = getLineHeight();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            vView.setLineHeight(height);
            hView.setLineHeight(height);
        }
        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_LINE_HEIGHT, oldData, getLineHeight()));
    }

    @Override
    public int getLineHeight() {
        return vView.getLineHeight();
    }

    @Override
    public void setMaxLine(int lines) {
        int oldData = getMaxLine();
        vView.setMaxLines(lines);
        hView.setMaxLines(lines);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_MAX_LINES, oldData, getMaxLine()));
    }

    @Override
    public void setLetterSpacing(float spacing) {
        float oldData = getLetterSpacing();
        vView.setLetterSpacing(spacing);
        hView.setLetterSpacing(spacing);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_BACKGROUND, oldData, getLetterSpacing()));
    }

    @Override
    public float getLetterSpacing() {
        return vView.getLetterSpacing();
    }

    @Override
    public float getLineSpacing() {
        return vView.getLineSpacingMultiplier();
    }

    @Override
    public int getLineCount() {
        return vView.getLineCount();
    }

    @Override
    public int getMaxLine() {
        return vView.getMaxLines();
    }

    @Override
    public Background getViewBackground() {
        return background;
    }


    @Override
    public void setOnTextChangedListener(final OnTextChangedListener<MultiActionScrollableText> listener) {
        OnTextChangedListener<MultiActionScrollableText> oldData = getOnTextChangedListener();
        this.listener = listener;

        hView.addTextChangedListener(new TextChangeListener<>(this, /*oldText,*/ listener));
        vView.addTextChangedListener(new TextChangeListener<>(this, /*oldText,*/ listener));

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_TEXT_CHANGED, oldData, getOnTextChangedListener()));
    }

    @Override
    public void setOnPropertyChangedListener(OnPropertyChangedListener<MultiActionScrollableText> listener) {
        OnPropertyChangedListener<MultiActionScrollableText> oldData = getOnPropertyChangedListener();
        propertyChangedListener = listener;

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_PROPERTY_CHANGED, oldData, getOnPropertyChangedListener()));
    }


    @Override
    public OnTextChangedListener<MultiActionScrollableText> getOnTextChangedListener() {
        return listener;
    }

    @Override
    public OnPropertyChangedListener<MultiActionScrollableText> getOnPropertyChangedListener() {
        return propertyChangedListener;
    }


    @Override
    public void setHorizontalAlignment(@NonNull HorizontalAlignment alignment) {
        HorizontalAlignment oldData = getHorizontalAlignment();
        switch (alignment) {
            case Center:
                horizontal = Gravity.CENTER_HORIZONTAL;
                break;
            case Left:
                horizontal = Gravity.START;
                break;
            case Right:
                horizontal = Gravity.END;
        }

        setAlignment();
        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<HorizontalAlignment>(PROPERTY_ALIGNMENT, oldData, getHorizontalAlignment()));
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
    public void setAlignment(@NonNull HorizontalAlignment horizontal, @NonNull VerticalAlignment vertical) {
        setVerticalAlignment(vertical);
        setHorizontalAlignment(horizontal);
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public HorizontalAlignment getHorizontalAlignment() {
        switch (horizontal) {
            case Gravity.CENTER_HORIZONTAL:
                return HorizontalAlignment.Center;
            case Gravity.END:
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
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<VerticalAlignment>(PROPERTY_ALIGNMENT, oldData, getVerticalAlignment()));
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
        vView.setGravity(horizontal | vertical);
        hView.setGravity(horizontal | vertical);
    }

    public void setTextSize(float size) {
        float oldData = getTextSize();
        vView.setTextSize(size);
        hView.setTextSize(size);
        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_PROPERTY_CHANGED, oldData, getTextSize()));
    }

    public float getTextSize() {
        return vView.getTextSize();
    }

    public void addLinks(OnLinkClickListener<MultiActionScrollableText> link, CharSequence... subs) {
        addSubTexts(SpannableTextFormat.getLinkSpan(span, text, link, subs).texts);
    }

    public void addLinks(TextLink... links) {
        addLinks(Arrays.asList(links));
    }

    public void addLinks(@NonNull List<TextLink> links) {
        for (Text text : links)
            addSubText(text);
        setup();
    }

    @Override
    public void addUnderlines(@NonNull List<TextUnderline> underlines) {
        for (Text text : underlines)
            addSubText(text);
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
        for (Text text : urls)
            addSubText(text);
        setup();
    }


    public void addSubColors(int color, CharSequence... subs) {
        addSubTexts(SpannableTextFormat.getColorSpan(span, text, color, subs).texts);
    }

    public void addSubColors(TextColor... colors) {
        addSubColors(Arrays.asList(colors));
    }

    public void addSubColors(@NonNull List<TextColor> colors) {
        for (Text text : colors)
            addSubText(text);
        setup();
    }

    @Override
    public void addSubBackgrounds(int color, CharSequence... subs) {
        addSubTexts(SpannableTextFormat.getBackgroundSpan(span, text, color, subs).texts);
    }

    @Override
    public void addSubBackgrounds(@NonNull List<TextBackground> backgrounds) {
        for (Text text : backgrounds)
            addSubText(text);
        setup();
    }

    @Override
    public void addSubImages(int image, float size, CharSequence... subs) {
        addSubTexts(SpannableTextFormat.getImageSpan(span, text, getContext(), image, size, subs).texts);
    }

    @Override
    public void addSubImages(int image, int tint, float size, CharSequence... subs) {
        addSubTexts(SpannableTextFormat.getImageSpan(span, text, getContext(), image, tint, size, subs).texts);
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
        for (Text text : images)
            addSubText(text);
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
        for (Text text : strikeThroughs)
            addSubText(text);
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
        for (Text text : bullets)
            addSubText(text);
        setup();
    }


    @Override
    public void addSubscripts(CharSequence... subs) {
        addSubTexts(SpannableTextFormat.getSubscriptSpan(span, text, subs).texts);
    }

    @Override
    public void addSubscripts(@NonNull List<TextSubscript> subscripts) {
        for (Text text : subscripts)
            addSubText(text);
        setup();
    }


    @Override
    public void addSuperscripts(CharSequence... subs) {
        addSubTexts(SpannableTextFormat.getSuperscriptSpan(span, text, subs).texts);
    }

    @Override
    public void addSuperscripts(@NonNull List<TextSuperscript> superscripts) {
        for (Text text : superscripts)
            addSubText(text);
        setup();
    }


    public void addSubSizes(int size, CharSequence... subs) {
        addSubTexts(SpannableTextFormat.getSizeSpan(span, text, size, subs).texts);
    }

    public void addSubSizes(TextSize... colors) {
        addSubSizes(Arrays.asList(colors));
    }

    public void addSubSizes(@NonNull List<TextSize> sizes) {
        for (Text text : sizes)
            addSubText(text);
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
        for (Text text : fonts)
            addSubText(text);
        setup();
    }


    public boolean removeSubText(@NonNull Text text) {
        for (Text txt : subTexts)
            if (txt.equals(text))
                return subTexts.remove(txt);
        return false;
    }


    @Override
    public void addMultiStyles(int gap, int color, int radius, int size, @NonNull CharSequence url, @NonNull Font font, CharSequence... subs) {
        addBullets(gap, color, radius, subs);
        addSubSizes(size, subs);
        addUrlLinks(url, subs);
        addSubFonts(font, subs);
        addSubColors(color, subs);
    }


    private void addSubTexts(@NonNull List<Text> texts) {
        for (Text text : texts)
            addSubText(text);
        setup();
    }

    @Override
    public boolean containsSubText(@NonNull Text text) {
        return getSubText(text) != null;
    }

    private void addSubText(@NonNull Text text) {
        if (containsSubText(text))
            if (getSubText(text).type == text.type)
                subTexts.remove(getSubText(text));
        text.view = this;
        subTexts.add(text);
    }

    private Text getSubText(@NonNull Text text) {
        for (Text sub : subTexts)
            if (sub.equals(text))
                return sub;
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
                case Strikethrough:
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
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_PROPERTY_CHANGED, oldData, subTexts));
        setSpan(span);
    }

    @Override
    public SubTexts<TextSize> getSubSizes(CharSequence... subs) {
        List<TextSize> texts = new ArrayList<>();
        for (CharSequence sub : subs)
            for (TextSize txt : sizes)
                if (txt.text.contentEquals(sub))
                    texts.add(txt);
        return new SubTexts<>(text, texts);
    }

    @Override
    public TextSize getSubSize(@NonNull CharSequence sub, int start) {
        for (TextSize txt : sizes)
            if (txt.text.contentEquals(sub) && txt.start == start)
                return txt;
        return null;
    }

    @Override
    public SubTexts<TextColor> getSubColors(CharSequence... subs) {
        List<TextColor> texts = new ArrayList<>();
        for (CharSequence sub : subs)
            for (TextColor txt : colors)
                if (txt.text.contentEquals(sub))
                    texts.add(txt);
        return new SubTexts<>(text, texts);
    }

    @Override
    public TextColor getSubColor(@NonNull CharSequence sub, int start) {
        for (TextColor txt : colors)
            if (txt.text.contentEquals(sub) && txt.start == start)
                return txt;
        return null;
    }

    @Override
    public SubTexts<TextFont> getSubFonts(CharSequence... subs) {
        List<TextFont> texts = new ArrayList<>();
        for (CharSequence sub : subs)
            for (TextFont txt : fonts)
                if (txt.text.contentEquals(sub))
                    texts.add(txt);
        return new SubTexts<>(text, texts);
    }

    @Override
    public TextFont getSubFont(@NonNull CharSequence sub, int start) {
        for (TextFont txt : fonts)
            if (txt.text.contentEquals(sub) && txt.start == start)
                return txt;
        return null;
    }

    @Override
    public SubTexts<TextImage> getSubImages(CharSequence... subs) {
        List<TextImage> texts = new ArrayList<>();
        for (CharSequence sub : subs)
            for (TextImage txt : images)
                if (txt.text.contentEquals(sub))
                    texts.add(txt);
        return new SubTexts<>(text, texts);
    }

    @Override
    public TextImage getSubImage(@NonNull CharSequence sub, int start) {
        for (TextImage txt : images)
            if (txt.text.contentEquals(sub) && txt.start == start)
                return txt;
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
                if (notContains(texts, txt) && txt.text.contentEquals(sub))
                    texts.add(txt);
        return new SubTexts<>(text, texts);
    }

    protected boolean notContains(List<Text> texts, Text sub) {
        for (Text txt : texts)
            if (txt.equals(sub))
                return false;
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

    @Override
    public int getViewWidth() {
        return getLayoutParams().width;
    }

    @Override
    public int getViewHeight() {
        return getLayoutParams().height;
    }


    public enum Direction {
        Both,
        Horizontal,
        Vertical
    }


}
