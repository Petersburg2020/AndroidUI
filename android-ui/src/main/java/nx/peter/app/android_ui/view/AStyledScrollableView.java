package nx.peter.app.android_ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.text.*;

import java.util.Arrays;
import java.util.List;

abstract class AStyledScrollableView<I extends AStyledScrollableView, T extends AStyledView> extends AView<I> implements StyledView<I> {
    protected T hView, vView, vhView;
    protected OnTextChangedListener<I> listener;
    protected OnPropertyChangedListener<I> propertyChangedListener;
    protected ScrollView vContainer, vhContainer;
    protected HorizontalScrollView hContainer;


    public AStyledScrollableView(Context context) {
        super(context);
    }

    public AStyledScrollableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void reset() {
        setPadding(0);
        setScrollDirection(Direction.Vertical);
        setText("Try google this color, icon icn, underline, large-text, with emphasis and click here!");
        addLinks((view, text, link) -> {
        }, "here");
        addUrlLinks("https://google.com", "google");
        addSubSizes((int) (getTextSize() * 1.05f), "large-text");
        addSubImages(R.drawable.no_image, "icn");
        addSubFonts(Font.Style.Bold, "emphasis");
        addUnderlines("underline");
        addSubColors(Color.GREEN, "color");
    }

    protected void setStyle(int style) {
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
    }

    protected void setFont(int index) {
        switch (index) {
            case 0: setFont(Font.CANTERBURY); break;
            case 1: setFont(Font.JOSEFIN_SANS_BOLD); break;
            case 2: setFont(Font.JOSEFIN_SANS_BOLD_ITALIC); break;
            case 3: setFont(Font.JOSEFIN_SANS_EXTRA_BOLD); break;
            case 4: setFont(Font.JOSEFIN_SANS_EXTRA_BOLD_ITALIC); break;
            case 5: setFont(Font.JOSEFIN_SANS_ITALIC); break;
            case 6: setFont(Font.JOSEFIN_SANS_LIGHT); break;
            case 7: setFont(Font.JOSEFIN_SANS_LIGHT_ITALIC); break;
            case 8: setFont(Font.JOSEFIN_SANS_REGULAR); break;
            case 9: setFont(Font.JOSEFIN_SANS_SEMI_BOLD); break;
            case 10: setFont(Font.JOSEFIN_SANS_SEMI_BOLD_ITALIC); break;
            case 11: setFont(Font.PAJAMA_PANTS); break;
            case 12: setFont(Font.PAJAMA_PANTS_BOLD); break;
            case 13: setFont(Font.PAJAMA_PANTS_LIGHT); break;
            case 14: setFont(Font.ROWDIES_BOLD); break;
            case 15: setFont(Font.ROWDIES_REGULAR); break;
            case 16: setFont(Font.RUSTHINA); break;
            case 17: setFont(Font.THE_ARTISAN_MARKER_SERIF); break;
            case 18: setFont(Font.TRESDIAS);
        }
    }

    protected void setFontFamily(int font) {
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
    }

    protected void setScrollDirection(int direction) {
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
    }

    protected void setAlignment(int alignment) {
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
    }

    protected void setForegroundColor(int color) {
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
    }


    public void setTextSize(int unit, float size) {
        vView.setTextSize(unit, size);
        hView.setTextSize(unit, size);
        vhView.setTextSize(unit, size);
    }

    public void setHint(@NonNull CharSequence hint) {
        vView.setHint(hint);
        hView.setHint(hint);
        vhView.setHint(hint);
    }

    public CharSequence getHint() {
        return vView.getHint();
    }

    @Override
    public void setHintColor(@ColorInt int color) {
        vView.setHintColor(color);
        hView.setHintColor(color);
        vhView.setHintColor(color);
    }

    @Override
    public int getHintColor() {
        return vView.getHintColor();
    }

    @Override
    public void setTextColorAlpha(@IntRange(from = 0, to = 255) int alpha) {
        vView.setTextColorAlpha(alpha);
        hView.setTextColorAlpha(alpha);
        vhView.setTextColorAlpha(alpha);
    }

    @Override
    public void setHintColorAlpha(@IntRange(from = 0, to = 255) int alpha) {
        vView.setHintColorAlpha(alpha);
        hView.setHintColorAlpha(alpha);
        vView.setHintColorAlpha(alpha);
    }

    @Override
    public void setText(@NonNull CharSequence text) {
        CharSequence oldData = getText();
        vView.setText(text);
        hView.setText(text);
        vhView.setText(text);

        if (listener != null)
            listener.onTextChanged(this, oldData, getText());

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_TEXT, oldData, getText()));
    }

    public void setScrollDirection(@NonNull Direction direction) {
        vContainer.setVisibility(direction.equals(Direction.Vertical) ? VISIBLE : GONE);
        hContainer.setVisibility(direction.equals(Direction.Horizontal) ? VISIBLE : GONE);
        vhContainer.setVisibility(direction.equals(Direction.Both) ? VISIBLE : GONE);
    }

    public Direction getScrollDirection() {
        return isVisible(vContainer) ? Direction.Vertical : isVisible(hContainer) ? Direction.Horizontal : Direction.Both;
    }

    protected boolean isVisible(View view) {
        return view.getVisibility() == VISIBLE;
    }

    @Override
    public void setEllipsize(Ellipsize ellipsize) {
        vView.setEllipsize(ellipsize);
        hView.setEllipsize(ellipsize);
        vhView.setEllipsize(ellipsize);
    }

    @Override
    public Ellipsize getEllipsize() {
        return vView.getEllipsize();
    }

    @Override
    public void setSingleLine(boolean isSingleLine) {
        vView.setSingleLine(isSingleLine);
        hView.setSingleLine(isSingleLine);
        vhView.setSingleLine(isSingleLine);
    }

    @Override
    public boolean isSingleLine() {
        return vView.isSingleLine();
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
        vView.appendText(text, index);
        hView.appendText(text, index);
        vhView.appendText(text, index);

        if (listener != null)
            listener.onTextChanged(this, oldData, getText());

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_TEXT, oldData, getText()));
    }

    @Override
    public void setFont(@NonNull Font font) {
        Font oldData = getFont();
        hView.setFont(font);
        vhView.setFont(font);
        vView.setFont(font);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_FONT_STYLE, oldData, getFont()));
    }

    @Override
    public Font getFont() {
        return vView.getFont();
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
        vhView.setTextColor(color);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_TEXT_COLOR, oldData, color));
    }

    public int getTextColor() {
        return vView.getTextColor();
    }

    public void setLinksColor(int color) {
        int oldData = getLinksColor();
        vView.setLinksColor(color);
        hView.setLinksColor(color);
        vhView.setLinksColor(color);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_LINKS_COLOR, oldData, color));
    }

    public int getLinksColor() {
        return vView.getLinksColor();
    }

    public void setLinkClickable(boolean clickable) {
        boolean oldData = isLinksClickable();
        vView.setLinkClickable(clickable);
        hView.setLinkClickable(clickable);
        vhView.setLinkClickable(clickable);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_LINKS_CLICKABLE, oldData, clickable));
    }

    @Override
    public boolean isLinksClickable() {
        return vView.isLinksClickable();
    }

    @Override
    public void setLineSpacing(float spacing) {
        float oldData = getLineSpacing();
        vView.setLineSpacing(spacing);
        hView.setLineSpacing(spacing);
        vhView.setLineSpacing(spacing);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_LINE_SPACING, oldData, getLineSpacing()));
    }

    @Override
    public boolean post(@Nullable Runnable action) {
        return hView.post(action) && vView.post(action) && vhView.post(action);
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
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        vhView.setLayoutParams(params);
        vView.setLayoutParams(params);
        hView.setLayoutParams(params);
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
        vView.setBackground(background);
        hView.setBackground(background);
        vhView.setBackground(background);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_BACKGROUND, oldData, getViewBackground()));
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        vView.setPadding(left, top, right, bottom);
        hView.setPadding(left, top, right, bottom);
        vhView.setPadding(left, top, right, bottom);
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        vView.setPaddingRelative(start, top, end, bottom);
        hView.setPaddingRelative(start, top, end, bottom);
        vhView.setPaddingRelative(start, top, end, bottom);
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
        hView.setTextDirection(textDirection);
        vhView.setTextDirection(textDirection);
    }

    @Override
    public int getTextDirection() {
        return vView.getTextDirection();
    }

    @Override
    public void setTextAlignment(int textAlignment) {
        vView.setTextAlignment(textAlignment);
        hView.setTextAlignment(textAlignment);
        vhView.setTextAlignment(textAlignment);
    }

    @Override
    public void setHorizontalScrollbarTrackDrawable(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vView.setHorizontalScrollbarTrackDrawable(drawable);
            hView.setHorizontalScrollbarTrackDrawable(drawable);
            vhView.setHorizontalScrollbarTrackDrawable(drawable);
        }
    }

    @Override
    public void setVerticalScrollBarEnabled(boolean verticalScrollBarEnabled) {
        vView.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
        hView.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
        vhView.setVerticalScrollBarEnabled(verticalScrollBarEnabled);
    }

    @Override
    public void setHorizontalFadingEdgeEnabled(boolean horizontalFadingEdgeEnabled) {
        vView.setHorizontalFadingEdgeEnabled(horizontalFadingEdgeEnabled);
        hView.setHorizontalFadingEdgeEnabled(horizontalFadingEdgeEnabled);
        vhView.setHorizontalFadingEdgeEnabled(horizontalFadingEdgeEnabled);
    }

    @Override
    public void setHorizontalScrollbarThumbDrawable(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vView.setHorizontalScrollbarThumbDrawable(drawable);
            hView.setHorizontalScrollbarThumbDrawable(drawable);
            vhView.setHorizontalScrollbarThumbDrawable(drawable);
        }
    }

    @Override
    public void setHorizontalScrollBarEnabled(boolean horizontalScrollBarEnabled) {
        vView.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
        hView.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
        vhView.setHorizontalScrollBarEnabled(horizontalScrollBarEnabled);
    }

    @Override
    public void setVerticalScrollbarThumbDrawable(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vView.setVerticalScrollbarThumbDrawable(drawable);
            hView.setVerticalScrollbarThumbDrawable(drawable);
            vhView.setVerticalScrollbarThumbDrawable(drawable);
        }
    }

    @Override
    public void setVerticalScrollbarTrackDrawable(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vView.setVerticalScrollbarTrackDrawable(drawable);
            hView.setVerticalScrollbarTrackDrawable(drawable);
            vhView.setVerticalScrollbarTrackDrawable(drawable);
        }
    }

    @Override
    public void setVerticalScrollbarPosition(int position) {
        vView.setVerticalScrollbarPosition(position);
        hView.setVerticalScrollbarPosition(position);
        vhView.setVerticalScrollbarPosition(position);
    }


    @Override
    public void setLineHeight(int height) {
        int oldData = getLineHeight();
        vView.setLineHeight(height);
        hView.setLineHeight(height);
        vhView.setLineHeight(height);
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
        vView.setMaxLine(lines);
        hView.setMaxLine(lines);
        vhView.setMaxLine(lines);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_MAX_LINES, oldData, getMaxLine()));
    }

    @Override
    public void setLetterSpacing(float spacing) {
        float oldData = getLetterSpacing();
        vView.setLetterSpacing(spacing);
        hView.setLetterSpacing(spacing);
        vhView.setLetterSpacing(spacing);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_BACKGROUND, oldData, getLetterSpacing()));
    }

    @Override
    public float getLetterSpacing() {
        return vView.getLetterSpacing();
    }

    @Override
    public float getLineSpacing() {
        return vView.getLineSpacing();
    }

    @Override
    public int getLineCount() {
        return vView.getLineCount();
    }

    @Override
    public int getMaxLine() {
        return vView.getMaxLine();
    }

    @Override
    public Background getViewBackground() {
        return vView.getViewBackground();
    }


    @Override
    public void setOnTextChangedListener(OnTextChangedListener<I> listener) {
        OnTextChangedListener<I> oldData = getOnTextChangedListener();
        this.listener = listener;
        OnTextChangedListener<T> textChangedListener = (view, oldText, newText) -> {
            if (listener != null) listener.onTextChanged(AStyledScrollableView.this, oldText, newText);
        };

        hView.setOnTextChangedListener(textChangedListener);
        vhView.setOnTextChangedListener(textChangedListener);
        vView.setOnTextChangedListener(textChangedListener);

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_TEXT_CHANGED, oldData, getOnTextChangedListener()));
    }

    @Override
    public void setOnPropertyChangedListener(OnPropertyChangedListener<I> listener) {
        OnPropertyChangedListener<I> oldData = getOnPropertyChangedListener();
        propertyChangedListener = listener;

        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_PROPERTY_CHANGED, oldData, getOnPropertyChangedListener()));
    }


    @Override
    public OnTextChangedListener<I> getOnTextChangedListener() {
        return listener;
    }

    @Override
    public OnPropertyChangedListener<I> getOnPropertyChangedListener() {
        return propertyChangedListener;
    }


    @Override
    public void setHorizontalAlignment(@NonNull HorizontalAlignment alignment) {
        HorizontalAlignment oldData = getHorizontalAlignment();
        vView.setHorizontalAlignment(alignment);
        hView.setHorizontalAlignment(alignment);
        vView.setHorizontalAlignment(alignment);
        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_ALIGNMENT, oldData, getHorizontalAlignment()));
    }

    @Override
    public Alignment getAlignment() {
        return vView.getAlignment();
    }

    @Override
    public void setAlignment(@NonNull Alignment alignment) {
        vView.setAlignment(alignment);
        hView.setAlignment(alignment);
        vhView.setAlignment(alignment);
    }

    @Override
    public void setAlignment(@NonNull HorizontalAlignment horizontal, @NonNull VerticalAlignment vertical) {
        setVerticalAlignment(vertical);
        setHorizontalAlignment(horizontal);
    }

    @Override
    public HorizontalAlignment getHorizontalAlignment() {
        return vView.getHorizontalAlignment();
    }

    @Override
    public void setVerticalAlignment(@NonNull VerticalAlignment alignment) {
        VerticalAlignment oldData = getVerticalAlignment();
        vhView.setVerticalAlignment(alignment);
        vView.setVerticalAlignment(alignment);
        hView.setVerticalAlignment(alignment);
        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_ALIGNMENT, oldData, getVerticalAlignment()));
    }

    @Override
    public VerticalAlignment getVerticalAlignment() {
        return vView.getVerticalAlignment();
    }

    public void setTextSize(float size) {
        float oldData = getTextSize();
        vView.setTextSize(size);
        hView.setTextSize(size);
        vhView.setTextSize(size);
        if (propertyChangedListener != null)
            propertyChangedListener.onPropertyChanged(this, new PropertyChange<>(PROPERTY_PROPERTY_CHANGED, oldData, getTextSize()));
    }

    public float getTextSize() {
        return vView.getTextSize();
    }

    public void addLinks(OnLinkClickListener<I> link, CharSequence... subs) {
        OnLinkClickListener<T> listener = (view, text, l) -> {
            if (link != null) link.onClickLink(AStyledScrollableView.this, text, l);
        };
        vView.addLinks(listener, subs);
        hView.addLinks(listener, subs);
        vhView.addLinks(listener, subs);
    }

    public void addLinks(TextLink... links) {
        addLinks(Arrays.asList(links));
    }

    public void addLinks(@NonNull List<TextLink> links) {
        vView.addLinks(links);
        hView.addLinks(links);
        vhView.addLinks(links);
    }

    @Override
    public void addUnderlines(@NonNull List<TextUnderline> underlines) {
        vView.addUnderlines(underlines);
        hView.addUnderlines(underlines);
        vhView.addUnderlines(underlines);
    }

    @Override
    public void addUnderlines(CharSequence... underlines) {
        vView.addUnderlines(underlines);
        hView.addUnderlines(underlines);
        vhView.addUnderlines(underlines);
    }


    public void addUrlLinks(@NonNull CharSequence url, CharSequence... subs) {
        vView.addUrlLinks(url, subs);
        hView.addUrlLinks(url, subs);
        vhView.addUrlLinks(url, subs);
    }

    public void addUrlLinks(TextURL... urls) {
        addUrlLinks(Arrays.asList(urls));
    }

    public void addUrlLinks(@NonNull List<TextURL> urls) {
        vView.addUrlLinks(urls);
        hView.addUrlLinks(urls);
        vhView.addUrlLinks(urls);
    }


    public void addSubColors(int color, CharSequence... subs) {
        vView.addSubColors(color, subs);
        hView.addSubColors(color, subs);
        vhView.addSubColors(color, subs);
    }

    public void addSubColors(TextColor... colors) {
        addSubColors(Arrays.asList(colors));
    }

    public void addSubColors(@NonNull List<TextColor> colors) {
        vView.addSubColors(colors);
        hView.addSubColors(colors);
        vhView.addSubColors(colors);
    }

    @Override
    public void addSubBackgrounds(int color, CharSequence... subs) {
        vView.addSubBackgrounds(color, subs);
        hView.addSubBackgrounds(color, subs);
        vhView.addSubBackgrounds(color, subs);
    }

    @Override
    public void addSubBackgrounds(@NonNull List<TextBackground> backgrounds) {
        vView.addSubBackgrounds(backgrounds);
        hView.addSubBackgrounds(backgrounds);
        vhView.addSubBackgrounds(backgrounds);
    }

    @Override
    public void addSubImages(int image, float size, CharSequence... subs) {
        vView.addSubImages(image, size, subs);
        hView.addSubImages(image, size, subs);
        vhView.addSubImages(image, size, subs);
    }

    @Override
    public void addSubImages(int image, int tint, float size, CharSequence... subs) {
        vView.addSubImages(image, tint, size, subs);
        hView.addSubImages(image, tint, size, subs);
        vhView.addSubImages(image, tint, size, subs);
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
        vView.addSubImages(images);
        hView.addSubImages(images);
        vhView.addSubImages(images);
    }


    @Override
    public void addStrikeThroughs(CharSequence... subs) {
        addStrikeThroughs(getTextColor(), subs);
    }

    @Override
    public void addStrikeThroughs(int color, CharSequence... subs) {
        vView.addStrikeThroughs(color, subs);
        hView.addStrikeThroughs(color, subs);
        vhView.addStrikeThroughs(color, subs);
    }

    @Override
    public void addStrikeThroughs(@NonNull List<TextStrikeThrough> strikeThrough) {
        vView.addStrikeThroughs(strikeThrough);
        vhView.addStrikeThroughs(strikeThrough);
        hView.addStrikeThroughs(strikeThrough);
    }


    @Override
    public void addBullets(CharSequence... subs) {
        vView.addBullets(subs);
        hView.addBullets(subs);
        vhView.addBullets(subs);
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
        vView.addBullets(gap, color, radius, subs);
        hView.addBullets(gap, color, radius, subs);
        vhView.addBullets(gap, color, radius, subs);
    }

    @Override
    public void addBullets(@NonNull List<TextBullet> bullets) {
        vView.addBullets(bullets);
        hView.addBullets(bullets);
        vhView.addBullets(bullets);
    }


    @Override
    public void addSubscripts(CharSequence... subs) {
        vView.addSubscripts(subs);
        hView.addSubscripts(subs);
        vhView.addSubscripts(subs);
    }

    @Override
    public void addSubscripts(@NonNull List<TextSubscript> subscripts) {
        vView.addSubscripts(subscripts);
        hView.addSubscripts(subscripts);
        vhView.addSubscripts(subscripts);
    }


    @Override
    public void addSuperscripts(CharSequence... subs) {
        vView.addSuperscripts(subs);
        hView.addSuperscripts(subs);
        vhView.addSuperscripts(subs);
    }

    @Override
    public void addSuperscripts(@NonNull List<TextSuperscript> superscripts) {
        vView.addSuperscripts(superscripts);
        hView.addSuperscripts(superscripts);
        vhView.addSuperscripts(superscripts);
    }


    public void addSubSizes(int size, CharSequence... subs) {
        vView.addSubSizes(size, subs);
        hView.addSubSizes(size, subs);
        vhView.addSubSizes(size, subs);
    }

    public void addSubSizes(TextSize... colors) {
        addSubSizes(Arrays.asList(colors));
    }

    @Override
    public void addSubSizes(@NonNull List<TextSize> sizes) {
        vView.addSubSizes(sizes);
        hView.addSubSizes(sizes);
        vhView.addSubSizes(sizes);
    }

    @Override
    public void addSubFonts(@NonNull Font font, CharSequence... subs) {
        vView.addSubFonts(font, subs);
        hView.addSubFonts(font, subs);
        vhView.addSubFonts(font, subs);
    }

    public void addSubFonts(TextFont... fonts) {
        addSubFonts(Arrays.asList(fonts));
    }

    @Override
    public void addSubFonts(Font.Style style, CharSequence... subs) {
        addSubFonts(getFont().setStyle(style), subs);
    }

    public void addSubFonts(@NonNull List<TextFont> fonts) {
        vView.addSubFonts(fonts);
        hView.addSubFonts(fonts);
        vhView.addSubFonts(fonts);
    }


    public boolean removeSubText(@NonNull Text text) {
        return vView.removeSubText(text) && hView.removeSubText(text) && vhView.removeSubText(text);
    }


    @Override
    public void addMultiStyles(int gap, int color, int radius, int size, @NonNull CharSequence url, @NonNull Font font, CharSequence... subs) {
        addBullets(gap, color, radius, subs);
        addSubSizes(size, subs);
        addUrlLinks(url, subs);
        addSubFonts(font, subs);
        addSubColors(color, subs);
    }


    @Override
    public boolean containsSubText(@NonNull Text text) {
        return vView.containsSubText(text);
    }

    @Override
    public SubTexts<TextSize> getSubSizes(CharSequence... subs) {
        return vView.getSubSizes(subs);
    }

    @Override
    public TextSize getSubSize(@NonNull CharSequence sub, int start) {
        return vView.getSubSize(sub, start);
    }

    @Override
    public SubTexts<TextColor> getSubColors(CharSequence... subs) {
        return vView.getSubColors(subs);
    }

    @Override
    public TextColor getSubColor(@NonNull CharSequence sub, int start) {
        return vView.getSubColor(sub, start);
    }

    @Override
    public SubTexts<TextFont> getSubFonts(CharSequence... subs) {
        return vView.getSubFonts(subs);
    }

    @Override
    public TextFont getSubFont(@NonNull CharSequence sub, int start) {
        return vView.getSubFont(sub, start);
    }

    @Override
    public SubTexts<TextImage> getSubImages(CharSequence... subs) {
        return vView.getSubImages(subs);
    }

    @Override
    public TextImage getSubImage(@NonNull CharSequence sub, int start) {
        return vView.getSubImage(sub, start);
    }

    @Override
    public SubTexts<Text> getSubTexts(@NonNull CharSequence sub, int start) {
        return vView.getSubTexts(sub, start);
    }

    @Override
    public SubTexts<Text> getSubTexts(CharSequence... subs) {
        return vView.getSubTexts(subs);
    }

    @Override
    public SubTexts<Text> getSubTexts() {
        return vView.getSubTexts();
    }

    @Override
    public SubTexts<TextLink> getLinks() {
        return vView.getLinks();
    }

    @Override
    public SubTexts<TextBullet> getBullets() {
        return vView.getBullets();
    }

    @Override
    public SubTexts<TextURL> getUrlLinks() {
        return vView.getUrlLinks();
    }

    @Override
    public SubTexts<TextImage> getSubImages() {
        return vView.getSubImages();
    }

    @Override
    public SubTexts<TextSubscript> getSubscripts() {
        return vView.getSubscripts();
    }

    @Override
    public SubTexts<TextSuperscript> getSuperscripts() {
        return vView.getSuperscripts();
    }

    @Override
    public SubTexts<TextFont> getSubFonts() {
        return vView.getSubFonts();
    }

    @Override
    public SubTexts<TextSize> getSubSizes() {
        return vView.getSubSizes();
    }

    @Override
    public SubTexts<TextColor> getSubColors() {
        return vView.getSubColors();
    }

    @Override
    public SubTexts<TextStrikeThrough> getStrikeThroughs() {
        return vView.getStrikeThroughs();
    }

    @Override
    public SubTexts<TextUnderline> getUnderlines() {
        return vView.getUnderlines();
    }


    public enum Direction {
        Both,
        Horizontal,
        Vertical
    }


}
