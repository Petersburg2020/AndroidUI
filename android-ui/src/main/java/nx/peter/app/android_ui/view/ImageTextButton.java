package nx.peter.app.android_ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.*;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.text.*;
import nx.peter.app.android_ui.view.text.FontFamily.*;
import nx.peter.app.android_ui.view.util.Size;

import java.util.List;

public class ImageTextButton extends AbstractView<ImageTextButton> implements StyledView<ImageTextButton> {
    private ImageView imageView;
    private StyledText view;
    private LinearLayout layout;
    private Background background;
    private OnTextChangedListener<ImageTextButton> textChanged;
    private OnPropertyChangedListener<ImageTextButton> propertyChanged;

    public ImageTextButton(Context context) {
        super(context);
    }

    public ImageTextButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_image_text_button, this);
        imageView = findViewById(R.id.image);
        view = findViewById(R.id.multi_text);
        layout = findViewById(R.id.layout);

        Resources res = Resources.getSystem();
        float sp = res.getDisplayMetrics().scaledDensity;

        if (attrs != null) {
            TypedArray a = obtainStyledAttributes(attrs, R.styleable.ImageTextButton);

            try {
                String text = a.getString(R.styleable.ImageTextButton_android_text);
                if (text == null)
                    text = "Button Text";
                setText(text);
            } catch (Exception ignored) {
            }

            try {
                int color = a.getColor(R.styleable.ImageTextButton_android_textColor, Color.DKGRAY);
                setTextColor(color);
            } catch (Exception ignored) {
            }

            try {
                float size = a.getDimension(R.styleable.ImageTextButton_android_textSize, getContext().getResources().getDimension(R.dimen.text_size));
                setTextSize(size / sp);
            } catch (Exception ignored) {
            }

            try {
                Drawable image = a.getDrawable(R.styleable.ImageTextButton_android_src);
                if (image == null)
                    image = res.getDrawable(R.drawable.ic_launcher_background);
                setImage(image);
            } catch (Exception ignored) {
            }

            try {
                boolean wrapped = a.getBoolean(R.styleable.ImageTextButton_isWrapped, false);
                setWrapped(wrapped);
            } catch (Exception ignored) {
            }

            try {
                int margin = (int) (a.getDimensionPixelSize(R.styleable.ImageTextButton_image_text_margin, getContext().getResources().getDimensionPixelSize(R.dimen.image_text_margin)) / sp);
                setImageTextMargin(margin);
            } catch (Exception ignored) {
            }

            try {
                int size = (int) (a.getDimensionPixelSize(R.styleable.ImageTextButton_image_size, 0) / sp);
                setImageSize(size);
            } catch (Exception ignored) {
            }

            try {
                int width = (int) (a.getDimensionPixelSize(R.styleable.ImageTextButton_image_width, getContext().getResources().getDimensionPixelSize(R.dimen.image_size)) / sp);
                if (getImageWidth() <= 0)
                    setImageWidth(width);
            } catch (Exception ignored) {
            }

            try {
                int height = (int) (a.getDimensionPixelSize(R.styleable.ImageTextButton_image_height, getContext().getResources().getDimensionPixelSize(R.dimen.image_size)) / sp);
                if (getImageHeight() <= 0)
                    setImageHeight(height);
            } catch (Exception ignored) {
            } finally {
                a.recycle();
            }
        }

        reset();
    }

    @Override
    protected void reset() {
        propertyChanged = null;
        textChanged = null;


        setBackground(Background.Transparent);
        setText("facebook comment");
        // setForeground(Foreground.Brown);
        setTextSize(16);
        // setImage(R.drawable.ic_launcher_background);
        setImageSize(20);
    }

    public void setHint(@NonNull CharSequence hint) {
        view.setHint(hint);
    }

    public CharSequence getHint() {
        return view.getHint();
    }

    @Override
    public void setHintColor(@ColorInt int color) {
        view.setHintColor(color);
    }

    @Override
    public void setHintColorAlpha(@IntRange(from = 0, to = 255) int alpha) {
        view.setHintColorAlpha(alpha);
    }

    @Override
    public int getHintColor() {
        return view.getHintColor();
    }

    @Override
    public void setEllipsize(Ellipsize ellipsize) {
        view.setEllipsize(ellipsize);
    }

    @Override
    public Ellipsize getEllipsize() {
        return view.getEllipsize();
    }

    @Override
    public void setSingleLine(boolean isSingleLine) {
        view.setSingleLine(isSingleLine);
    }

    @Override
    public boolean isSingleLine() {
        return view.isSingleLine();
    }

    public void setText(@NonNull CharSequence text) {
        view.setText(text);
    }

    public void setTextColor(@ColorInt int color) {
        view.setTextColor(color);
    }

    @Override
    public void setTextColorAlpha(@IntRange(from = 0, to = 255) int alpha) {
        view.setTextColorAlpha(alpha);
    }

    public int getTextColor() {
        return view.getTextColor();
    }

    public void setImage(@DrawableRes int imageRes) {
        imageView.setImageResource(imageRes);
    }

    public void setImage(@NonNull Bitmap image) {
        imageView.setImageBitmap(image);
    }

    public void setImage(@NonNull Drawable image) {
        imageView.setImageDrawable(image);
    }

    public Drawable getImage() {
        return imageView.getDrawable();
    }

    public void setImageSize(int size) {
        setImageSize(size, size);
    }

    public void setImageSize(int width, int height) {
        setImageWidth(width);
        setImageHeight(height);
    }

    public void setImageColor(int color) {
        imageView.setImageTintList(ColorStateList.valueOf(color));
    }

    public void setImageHeight(int height) {
        LayoutParams lp = (LayoutParams) imageView.getLayoutParams();
        lp.height = (int) (height * Resources.getSystem().getDisplayMetrics().scaledDensity);
        imageView.setLayoutParams(lp);
    }

    public void setImageWidth(int width) {
        LayoutParams lp = (LayoutParams) imageView.getLayoutParams();
        lp.width = (int) (width * Resources.getSystem().getDisplayMetrics().scaledDensity);
        imageView.setLayoutParams(lp);
    }

    public void setImageSize(@NonNull Size size) {
        setImageSize(size.width, size.height);
    }

    public void setColor(int color) {
        setTextColor(color);
        setImageColor(color);
    }

    public void setForeground(@NonNull Foreground foreground) {
        switch (foreground) {
            case Blue:
                setColor(Color.BLUE);
                break;
            case Black:
                setColor(Color.BLACK);
                break;
            case Brown:
                setColor(Color.parseColor("#FF603608"));
                break;
            case Cyan:
                setColor(Color.CYAN);
                break;
            case Green:
                setColor(Color.GREEN);
                break;
            case Grey:
                setColor(Color.GRAY);
                break;
            case Lime:
                setColor(Color.parseColor("#FF84FF00"));
                break;
            case Pink:
                setColor(Color.parseColor("#FFFF00D8"));
                break;
            case Orange:
                setColor(Color.parseColor("#FFFF7500"));
                break;
            case Purple:
                setColor(Color.parseColor("#FF7500FF"));
                break;
            case Red:
                setColor(Color.RED);
                break;
            case White:
                setColor(Color.WHITE);
                break;
            case Yellow:
                setColor(Color.YELLOW);
                break;
        }
    }

    public int getImageColor() {
        return imageView.getImageTintList().getDefaultColor();
    }

    public int getImageHeight() {
        return imageView.getLayoutParams().height;
    }

    public int getImageWidth() {
        return imageView.getLayoutParams().width;
    }

    public Size getImageSize() {
        return new Size(getImageWidth(), getImageHeight());
    }

    public void setImageTextMargin(int margin) {
        LayoutParams lp = (LayoutParams) imageView.getLayoutParams();
        lp.rightMargin = (int) (margin * Resources.getSystem().getDisplayMetrics().scaledDensity);
    }

    public void setPaddingVertical(int padding) {
        layout.setPadding(layout.getPaddingLeft(), padding, layout.getPaddingRight(), padding);
    }

    public void setWrapped(boolean wrapped) {
        LayoutParams lp = (LayoutParams) layout.getLayoutParams();
        lp.height = wrapped ? Math.max(view.getMeasuredHeight(), getImageHeight()) + 2 * layout.getPaddingTop() : getViewHeight();
        layout.setLayoutParams(lp);
        setGravity(Gravity.CENTER);
        setPaddingVertical(layout.getPaddingBottom());
    }


    @Override
    public void clearText() {
        view.clearText();
    }

    @Override
    public void appendText(@NonNull CharSequence text) {
        view.appendText(text);
    }

    @Override
    public void appendText(@NonNull CharSequence text, int index) {
        view.appendText(text, index);
    }

    @Override
    public void setFont(@NonNull Font font) {
        view.setFont(font);
    }

    @Override
    public Font getFont() {
        return view.getFont();
    }

    @Override
    public void setFontStyle(@NonNull Font.Style style) {
        view.setFontStyle(style);
    }

    @Override
    public void setFontFamily(@NonNull Family family, @NonNull Style style) {
        setFont(FontFamily.create(family, getContext()).getFont(style));
    }

    @Override
    public Font.Style getFontStyle() {
        return getFont().getStyle();
    }

    @Override
    public CharSequence getText() {
        return view.getText();
    }

    public void setLinksColor(int color) {
        view.setLinksColor(color);
    }

    public int getLinksColor() {
        return view.getLinksColor();
    }

    public void setLinkClickable(boolean clickable) {
        view.setLinkClickable(clickable);
    }

    @Override
    public boolean isLinksClickable() {
        return view.isLinksClickable();
    }

    @Override
    public void setLineSpacing(float spacing) {
        view.setLineSpacing(spacing);
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
    }

    @Override
    public void setBackgroundResource(int resid) {
        layout.setBackgroundResource(resid);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        layout.setPadding(left, top, right, bottom);
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        layout.setPaddingRelative(start, top, end, bottom);
    }

    @Override
    public int getPaddingLeft() {
        return layout.getPaddingLeft();
    }

    @Override
    public int getPaddingTop() {
        return layout.getPaddingTop();
    }

    @Override
    public int getPaddingBottom() {
        return layout.getPaddingBottom();
    }

    @Override
    public int getPaddingRight() {
        return layout.getPaddingRight();
    }

    @Override
    public void setLineHeight(int height) {
        view.setLineHeight(height);
    }

    @Override
    public int getLineHeight() {
        return view.getLineHeight();
    }

    @Override
    public void setMaxLine(int lines) {
        view.setMaxLine(lines);
    }

    @Override
    public void setLetterSpacing(float spacing) {
        view.setLetterSpacing(spacing);
    }

    @Override
    public float getLetterSpacing() {
        return view.getLetterSpacing();
    }

    @Override
    public float getLineSpacing() {
        return view.getLineSpacing();
    }

    @Override
    public int getMaxLine() {
        return view.getMaxLine();
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
    public void setOnTextChangedListener(OnTextChangedListener<ImageTextButton> listener) {
        textChanged = listener;
    }

    @Override
    public void setOnPropertyChangedListener(OnPropertyChangedListener<ImageTextButton> listener) {
        propertyChanged = listener;
    }

    @Override
    public OnTextChangedListener<ImageTextButton> getOnTextChangedListener() {
        return textChanged;
    }

    @Override
    public OnPropertyChangedListener<ImageTextButton> getOnPropertyChangedListener() {
        return propertyChanged;
    }

    @Override
    public void setHorizontalAlignment(@NonNull HorizontalAlignment alignment) {
        view.setHorizontalAlignment(alignment);
    }

    @Override
    public Alignment getAlignment() {
        return view.getAlignment();
    }

    @Override
    public void setAlignment(@NonNull Alignment alignment) {
        view.setAlignment(alignment);
    }

    @Override
    public void setAlignment(@NonNull HorizontalAlignment horizontal, @NonNull VerticalAlignment vertical) {
        view.setAlignment(horizontal, vertical);
    }

    @Override
    public HorizontalAlignment getHorizontalAlignment() {
        return view.getHorizontalAlignment();
    }

    @Override
    public void setVerticalAlignment(@NonNull VerticalAlignment alignment) {
        view.setVerticalAlignment(alignment);
    }

    @Override
    public VerticalAlignment getVerticalAlignment() {
        return view.getVerticalAlignment();
    }

    @Override
    public void setTextSize(float size) {
        view.setTextSize(size);
    }

    @Override
    public void setTextSize(int unit, float size) {
        view.setTextSize(unit, size);
    }

    public float getTextSize() {
        return view.getTextSize();
    }

    public void addLinks(final OnLinkClickListener<ImageTextButton> link, CharSequence... subs) {
        view.addLinks((view, text, l) -> {
            if (link != null) link.onClickLink(ImageTextButton.this, text, l);
        }, subs);
    }

    public void addLinks(TextLink... links) {
        view.addLinks(links);
    }

    public void addLinks(@NonNull List<TextLink> links) {
        view.addLinks(links);
    }

    @Override
    public void addUnderlines(@NonNull List<TextUnderline> underlines) {
        view.addUnderlines(underlines);
    }

    @Override
    public void addUnderlines(CharSequence... underlines) {
        view.addUnderlines(underlines);
    }


    public void addUrlLinks(@NonNull CharSequence url, CharSequence... subs) {
        view.addUrlLinks(url, subs);
    }

    public void addUrlLinks(TextURL... urls) {
        view.addUrlLinks(urls);
    }

    public void addUrlLinks(@NonNull List<TextURL> urls) {
        view.addUrlLinks(urls);
    }


    public void addSubColors(int color, CharSequence... subs) {
        view.addSubColors(color, subs);
    }

    public void addSubColors(TextColor... colors) {
        view.addSubColors(colors);
    }

    public void addSubColors(@NonNull List<TextColor> colors) {
        view.addSubColors(colors);
    }

    @Override
    public void addSubBackgrounds(int color, CharSequence... subs) {
        view.addSubBackgrounds(color, subs);
    }

    @Override
    public void addSubBackgrounds(@NonNull List<TextBackground> backgrounds) {
        view.addSubBackgrounds(backgrounds);
    }

    @Override
    public void addSubImages(int image, float size, CharSequence... subs) {
        view.addSubImages(image, size, subs);
    }

    @Override
    public void addSubImages(int image, int tint, float size, CharSequence... subs) {
        view.addSubImages(image, tint, size, subs);
    }

    @Override
    public void addSubImages(int image, CharSequence... subs) {
        view.addSubImages(image, subs);
    }

    @Override
    public void addSubImages(int image, int tint, CharSequence... subs) {
        view.addSubImages(image, tint, subs);
    }

    @Override
    public void addSubImages(@NonNull List<TextImage> images) {
        view.addSubImages(images);
    }


    @Override
    public void addStrikeThroughs(CharSequence... subs) {
        view.addStrikeThroughs(subs);
    }

    @Override
    public void addStrikeThroughs(int color, CharSequence... subs) {
        view.addStrikeThroughs(color, subs);
    }

    @Override
    public void addStrikeThroughs(@NonNull List<TextStrikeThrough> strikeThroughs) {
        view.addStrikeThroughs(strikeThroughs);
    }


    @Override
    public void addBullets(CharSequence... subs) {
        view.addBullets(subs);
    }

    @Override
    public void addBullets(int gap, CharSequence... subs) {
        view.addBullets(gap, subs);
    }

    @Override
    public void addBullets(int gap, int color, CharSequence... subs) {
        view.addBullets(gap, color, subs);
    }

    @Override
    public void addBullets(int gap, int color, int radius, CharSequence... subs) {
        view.addBullets(gap, color, radius, subs);
    }

    @Override
    public void addBullets(@NonNull List<TextBullet> bullets) {
        view.addBullets(bullets);
    }


    @Override
    public void addSubscripts(CharSequence... subs) {
        view.addSubscripts(subs);
    }

    @Override
    public void addSubscripts(@NonNull List<TextSubscript> subscripts) {
        view.addSubscripts(subscripts);
    }

    @Override
    public void addSuperscripts(CharSequence... subs) {
        view.addSuperscripts(subs);
    }

    @Override
    public void addSuperscripts(@NonNull List<TextSuperscript> superscripts) {
        view.addSuperscripts(superscripts);
    }


    public void addSubSizes(int size, CharSequence... subs) {
        view.addSubSizes(size, subs);
    }

    public void addSubSizes(TextSize... sizes) {
        view.addSubSizes(sizes);
    }

    public void addSubSizes(@NonNull List<TextSize> sizes) {
        view.addSubSizes(sizes);
    }


    public void addSubFonts(@NonNull Font font, CharSequence... subs) {
        view.addSubFonts(font, subs);
    }

    public void addSubFonts(TextFont... fonts) {
        view.addSubFonts(fonts);
    }

    @Override
    public void addSubFonts(Font.Style style, CharSequence... subs) {
        view.addSubFonts(style, subs);
    }

    public void addSubFonts(@NonNull List<TextFont> fonts) {
        view.addSubFonts(fonts);
    }


    public boolean removeSubText(@NonNull Text text) {
        return view.removeSubText(text);
    }


    @Override
    public void addMultiStyles(int gap, int color, int radius, int size, @NonNull CharSequence url, @NonNull Font font, CharSequence... subs) {
        view.addMultiStyles(gap, color, radius, size, url, font, subs);
    }

    @Override
    public boolean containsSubText(@NonNull Text text) {
        return view.containsSubText(text);
    }

    @Override
    public SubTexts<TextSize> getSubSizes(CharSequence... subs) {
        return view.getSubSizes(subs);
    }

    @Override
    public TextSize getSubSize(@NonNull CharSequence sub, int start) {
        return view.getSubSize(sub, start);
    }

    @Override
    public SubTexts<TextColor> getSubColors(CharSequence... subs) {
        return view.getSubColors(subs);
    }

    @Override
    public TextColor getSubColor(@NonNull CharSequence sub, int start) {
        return view.getSubColor(sub, start);
    }

    @Override
    public SubTexts<TextFont> getSubFonts(CharSequence... subs) {
        return view.getSubFonts(subs);
    }

    @Override
    public TextFont getSubFont(@NonNull CharSequence sub, int start) {
        return view.getSubFont(sub, start);
    }

    @Override
    public SubTexts<TextImage> getSubImages(CharSequence... subs) {
        return view.getSubImages(subs);
    }

    @Override
    public TextImage getSubImage(@NonNull CharSequence sub, int start) {
        return view.getSubImage(sub, start);
    }

    @Override
    public SubTexts<Text> getSubTexts(@NonNull CharSequence sub, int start) {
        return view.getSubTexts(sub, start);
    }

    @Override
    public SubTexts<Text> getSubTexts(CharSequence... subs) {
        return view.getSubTexts(subs);
    }

    @Override
    public SubTexts<Text> getSubTexts() {
        return view.getSubTexts();
    }


    @Override
    public SubTexts<TextLink> getLinks() {
        return view.getLinks();
    }

    @Override
    public SubTexts<TextBullet> getBullets() {
        return view.getBullets();
    }

    @Override
    public SubTexts<TextURL> getUrlLinks() {
        return view.getUrlLinks();
    }

    @Override
    public SubTexts<TextImage> getSubImages() {
        return view.getSubImages();
    }

    @Override
    public SubTexts<TextSubscript> getSubscripts() {
        return view.getSubscripts();
    }

    @Override
    public SubTexts<TextSuperscript> getSuperscripts() {
        return view.getSuperscripts();
    }

    @Override
    public SubTexts<TextFont> getSubFonts() {
        return view.getSubFonts();
    }

    @Override
    public SubTexts<TextSize> getSubSizes() {
        return view.getSubSizes();
    }

    @Override
    public SubTexts<TextColor> getSubColors() {
        return view.getSubColors();
    }

    @Override
    public SubTexts<TextStrikeThrough> getStrikeThroughs() {
        return view.getStrikeThroughs();
    }

    @Override
    public SubTexts<TextUnderline> getUnderlines() {
        return view.getUnderlines();
    }

}
