package nx.peter.app.android_ui.view;

import android.os.Build;
import android.text.*;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MultiAutoCompleteTextView;
import androidx.annotation.*;
import nx.peter.app.android_ui.view.text.*;
import nx.peter.app.android_ui.view.text.FontFamily.Family;
import nx.peter.app.android_ui.view.text.FontFamily.Style;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public interface StyledView<V extends View> extends IView<V> {
    /**
     * Set text for this multi action view
     * @param text text to be displayed
     */
    void setText(@NonNull CharSequence text);

    /**
     * Set hint for this multi action view
     * @param hint hint to be displayed
     */
    void setHint(@NonNull CharSequence hint);

    /**
     * Set the text displayed color
     * @param color text displayed color
     */
    void setTextColor(@ColorInt int color);

    void setTextColorAlpha(@IntRange(from = 0, to = 255) int alpha);

    void setHintColor(@ColorInt int color);

    void setHintColorAlpha(@IntRange(from = 0, to = 255) int alpha);

    void setTextSize(float size);

    void setTextSize(int unit, float size);

    void setFont(@NonNull Font font);

    void setFontFamily(@NonNull Family family, @NonNull Style style);

    void setFontStyle(@NonNull Font.Style style);

    void clearText();

    void setMaxLine(int lines);

    void setLinksColor(@ColorInt int color);

    void appendText(@NonNull CharSequence text);

    void appendText(@NonNull CharSequence text, int index);

    void setLineHeight(int height);

    void setLineSpacing(float spacing);

    void setLetterSpacing(float spacing);

    void setLinkClickable(boolean clickable);

    void setLayoutParams(ViewGroup.LayoutParams params);

    void setOnTextChangedListener(OnTextChangedListener<V> listener);

    void setOnPropertyChangedListener(OnPropertyChangedListener<V> listener);

    boolean post(@Nullable Runnable runnable);

    void addLinks(@NonNull List<TextLink> links);

    void addUrlLinks(@NonNull List<TextURL> urls);

    void addSubColors(@NonNull List<TextColor> colors);

    void addSubFonts(@NonNull List<TextFont> fonts);

    void addStrikeThroughs(@NonNull List<TextStrikeThrough> strikeThroughs);

    void addSubImages(@NonNull List<TextImage> images);

    void addSubSizes(@NonNull List<TextSize> sizes);

    void addBullets(@NonNull List<TextBullet> bullets);

    void addSubBackgrounds(@NonNull List<TextBackground> backgrounds);

    void addUnderlines(@NonNull List<TextUnderline> underlines);

    void addSubscripts(@NonNull List<TextSubscript> subscripts);

    void addSuperscripts(@NonNull List<TextSuperscript> superscripts);

    void addLinks(OnLinkClickListener<V> link, CharSequence... subs);

    void addUrlLinks(@NonNull CharSequence url, CharSequence... subs);

    void addSubColors(int color, CharSequence... subs);

    void addSubColors(TextColor... colors);
    void addSubFonts(@NonNull Font font, CharSequence... subs);

    void addSubFonts(Font.Style style, CharSequence... subs);

    void addSubSizes(TextSize... colors);

    void addSubSizes(int size, CharSequence... subs);

    void addSubBackgrounds(int color, CharSequence... subs);

    void addSubImages(@DrawableRes int image, CharSequence... subs);

    void addSubImages(@DrawableRes int image, @ColorInt int tint, CharSequence... subs);

    void addSubImages(@DrawableRes int image, float size, CharSequence... subs);

    void addSubImages(@DrawableRes int image, @ColorInt int tint, float size, CharSequence... subs);

    void addLinks(TextLink... links);

    void addBullets(CharSequence... subs);

    void addBullets(int gap, CharSequence... subs);

    void addBullets(int gap, int color, CharSequence... subs);

    void addBullets(int gap, int color, int radius, CharSequence... subs);

    void addStrikeThroughs(CharSequence... subs);

    void addStrikeThroughs(int color, CharSequence... subs);

    void addUnderlines(CharSequence... subs);

    void addSubscripts(CharSequence... subs);

    void addSuperscripts(CharSequence... subs);

    void addMultiStyles(int gap, int color, int radius, int size, @NonNull CharSequence url, @NonNull Font font, CharSequence... subs);

    void setBackgroundColor(int color);

    void setSingleLine(boolean isSingleLine);

    void setEllipsize(Ellipsize ellipsize);

    boolean removeSubText(@NonNull Text text);

    void setVerticalAlignment(@NonNull VerticalAlignment alignment);

    void setHorizontalAlignment(@NonNull HorizontalAlignment alignment);

    void setAlignment(@NonNull Alignment alignment);

    void setAlignment(@NonNull HorizontalAlignment horizontal, @NonNull VerticalAlignment vertical);

    Font getFont();

    Font.Style getFontStyle();

    boolean isSingleLine();

    @ColorInt int getTextColor();

    @ColorInt int getHintColor();

    int getLinksColor();

    int getMaxLine();

    int getLineCount();

    int getLineHeight();

    float getLetterSpacing();

    float getLineSpacing();

    float getTextSize();

    CharSequence getText();

    CharSequence getHint();

    Ellipsize getEllipsize();

    boolean isLinksClickable();

    boolean containsSubText(Text text);

    SubTexts<TextImage> getSubImages(CharSequence... subs);

    TextImage getSubImage(@NonNull CharSequence sub, int start);

    SubTexts<TextSize> getSubSizes(CharSequence... subs);

    TextSize getSubSize(@NonNull CharSequence sub, int start);

    SubTexts<TextFont> getSubFonts(CharSequence... subs);

    TextFont getSubFont(@NonNull CharSequence sub, int start);

    SubTexts<TextColor> getSubColors(CharSequence... subs);

    TextColor getSubColor(@NonNull CharSequence sub, int start);

    SubTexts<Text> getSubTexts(CharSequence... subs);

    SubTexts<Text> getSubTexts(@NonNull CharSequence sub, int start);

    SubTexts<Text> getSubTexts();

    SubTexts<TextLink> getLinks();

    SubTexts<TextURL> getUrlLinks();

    SubTexts<TextFont> getSubFonts();

    SubTexts<TextSize> getSubSizes();

    SubTexts<TextColor> getSubColors();

    SubTexts<TextImage> getSubImages();

    SubTexts<TextBullet> getBullets();

    SubTexts<TextSubscript> getSubscripts();

    SubTexts<TextSuperscript> getSuperscripts();

    SubTexts<TextStrikeThrough> getStrikeThroughs();

    SubTexts<TextUnderline> getUnderlines();

    Alignment getAlignment();

    VerticalAlignment getVerticalAlignment();

    HorizontalAlignment getHorizontalAlignment();

    OnTextChangedListener<V> getOnTextChangedListener();

    OnPropertyChangedListener<V> getOnPropertyChangedListener();


    enum Ellipsize {
        End,
        Middle,
        None,
        Marquee,
        Start,
    }

    enum Alignment {
        Center,
        TopLeft,
        TopRight,
        TopCenter,
        CenterLeft,
        CenterRight,
        BottomLeft,
        BottomRight,
        BottomCenter
    }

    enum HorizontalAlignment {
        Center,
        Left,
        Right
    }

    enum VerticalAlignment {
        Bottom,
        Center,
        Top
    }

    interface OnLinkClickListener<V extends View> {
        void onClickLink(@NonNull StyledView<V> view, CharSequence text, TextLink link);
    }

    interface OnTextChangedListener<V extends View> {
        void onTextChanged(StyledView<V> view, CharSequence oldText, CharSequence newText);
    }

    interface OnPropertyChangedListener<V extends View> {
        void onPropertyChanged(StyledView<V> view, PropertyChange<?> propertyChange);
    }

    class SubTexts<T extends Text> implements Iterable<T> {
        protected final List<T> subTexts;
        protected final CharSequence text;

        public SubTexts(CharSequence text, List<T> subTexts) {
            this.subTexts = subTexts;
            this.text = text;
        }

        public List<T> getAll() {
            return new ArrayList<T>(subTexts);
        }

        public T get(int index) {
            return index < size() && index >= 0 ? subTexts.get(index) : null;
        }

        public T get(CharSequence sub) {
            for (T txt : subTexts)
                if (txt.text.contentEquals(sub))
                    return txt;
            return null;
        }

        public void sort(@NonNull Param param) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                subTexts.sort((a, b) -> {
                    switch (param) {
                        case End:
                            return String.valueOf(a.end).compareTo(String.valueOf(b.end));
                        case Start:
                            return String.valueOf(a.start).compareTo(String.valueOf(b.start));
                        case Type:
                            return a.type.compareTo(b.type);
                        default:
                            return a.text.compareTo(b.text);
                    }
                });
            else {
                List<T> temp = new ArrayList<>();
                int lastIndex = 0;
                int index = 0;
                for (T text : subTexts) {
                    String old = "";
                    T mText = text;
                    switch (param) {
                        case End:
                            old = String.valueOf(text.end);
                            break;
                        case Start:
                            old = String.valueOf(text.start);
                            break;
                        case Text:
                            old = text.text;
                            break;
                        case Type:
                            old = String.valueOf(text.type);
                    }
                    for (T txt : subTexts) {
                        String curr = "";
                        switch (param) {
                            case End:
                                curr = String.valueOf(txt.end);
                                break;
                            case Start:
                                curr = String.valueOf(txt.start);
                                break;
                            case Text:
                                curr = txt.text;
                                break;
                            case Type:
                                curr = String.valueOf(txt.type);
                        }
                        if (old.compareTo(curr) > 0) {
                            old = curr;
                            mText = txt;
                        }
                    }
                    temp.add(mText);
                }

                subTexts.clear();
                subTexts.addAll(temp);
            }
        }

        public int indexOf(T text) {
            return indexOf(text, 0);
        }

        public int indexOf(T text, int start) {
            if (text == null || start < 0 || start >= size())
                return -1;
            for (int index = start; index < size(); index++)
                if (get(index).equals(text))
                    return index;
            return -1;
        }

        public int indexOf(CharSequence sub) {
            return indexOf(sub, 0);
        }

        public int indexOf(CharSequence sub, int start) {
            if (sub != null && start >= 0 || start <= size())
                for (int index = start; index < size(); index++)
                    if (get(index).text.contentEquals(sub))
                        return index;
            return -1;
        }

        public boolean contains(CharSequence sub) {
            return get(sub) != null;
        }

        public boolean contains(T text) {
            for (T txt : subTexts)
                if (txt.equals(text))
                    return true;
            return false;
        }

        public boolean isEmpty() {
            return subTexts.isEmpty();
        }

        public boolean isNotEmpty() {
            return !isEmpty();
        }

        public int size() {
            return subTexts.size();
        }

        @NonNull
        @Override
        public String toString() {
            return subTexts.toString();
        }

        @NonNull
        @Override
        public Iterator<T> iterator() {
            return subTexts.iterator();
        }

        public enum Param {
            Text,
            Start,
            End,
            Type
        }
    }

    class PropertyChange<D> {
        public final String key;
        public final D oldData;
        public final D newData;

        public PropertyChange(String key, D oldData, D newData) {
            this.key = key;
            this.oldData = oldData;
            this.newData = newData;
        }

        public boolean isChanged() {
            return !oldData.equals(newData);
        }
    }

    class TextChangeListener<V extends View> implements TextWatcher {
        private final OnTextChangedListener<V> listener;
        private final StyledView<V> view;
        private CharSequence old;

        public TextChangeListener(StyledView<V> view, OnTextChangedListener<V> listener) {
            this.listener = listener;
            this.view = view;
            old = "";
        }


        @Override
        public void beforeTextChanged(CharSequence text, int start, int count, int before) {
            old = text;
        }

        @Override
        public void onTextChanged(CharSequence text, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable text) {
            if (listener != null)
                listener.onTextChanged(view, old, text);
        }

    }


    /**
     * Tokens refers to the suggestion triggers.
     * Once a token is inputted in the editor, it automatically triggers
     * auto suggestions popup.
     */
    enum Tokens {
        /**
         * A user defined token
         */
        Custom,

        /**
         * Comma token
         */
        Comma,

        /**
         * Dot token
         */
        Dot,

        /**
         * Space token
         */
        Space
    }

    /**
     * CustomTokenizer - is a type of {@link Tokenizer} that takes user customized tokens as its token
     */
    class CustomTokenizer extends Tokenizer {
        public CustomTokenizer(@NonNull String tokens) {
            super(tokens);
        }
    }

    /**
     * DotTokenizer - is a type of {@link Tokenizer} that takes only dot as its token
     */
    class DotTokenizer extends Tokenizer {
        public DotTokenizer() {
            super(".");
        }
    }

    /**
     * CommaTokenizer - is a type of {@link Tokenizer} that takes only comma as its token
     */
    class CommaTokenizer extends Tokenizer {
        public CommaTokenizer() {
            super(",");
        }
    }

    /**
     * SpaceTokenizer - is a type of {@link Tokenizer} that takes only spaces as its token
     */
    class SpaceTokenizer extends Tokenizer {
        public SpaceTokenizer() {
            super(" ");
        }
    }



    /**
     * As this Java abstract class implements {@link MultiAutoCompleteTextView.Tokenizer} interface,
     * it also implements its 3 methods i.e. <b>findTokenStart</b>, <b>findTokenEnd</b> and <b>terminateToken</b>.
     * The Tokenizer helps tell the Editor, using some defined tokens, when to trigger suggestion popups while
     * editing texts.
     */
    abstract class Tokenizer implements MultiAutoCompleteTextView.Tokenizer {
        public static final String CODING = " .,{[()]}:;><|/\\";


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

    String PROPERTY_TEXT = "TEXT";
    String PROPERTY_FONT = "FONT";
    String PROPERTY_WIDTH = "WIDTH";
    String PROPERTY_HEIGHT = "HEIGHT";
    String PROPERTY_ALIGNMENT = "ALIGNMENT";
    String PROPERTY_LINKS_COLOR = "LINKS COLOR";
    String PROPERTY_LINKS_CLICKABLE = "LINKS CLICKABLE";
    String PROPERTY_TEXT_CHANGED = "TEXT CHANGED";
    String PROPERTY_PROPERTY_CHANGED = "PROPERTY CHANGED";
    String PROPERTY_FONT_STYLE = "FONT STYLE";
    String PROPERTY_BACKGROUND = "BACKGROUND";
    String PROPERTY_SUB_TEXTS = "SUB TEXTS";
    String PROPERTY_TEXT_SIZE = "TEXT SIZE";
    String PROPERTY_TEXT_COLOR = "TEXT COLOR";
    String PROPERTY_MAX_LINES = "MAX LINES";
    String PROPERTY_LINE_HEIGHT = "LINE HEIGHT";
    String PROPERTY_LINE_SPACING = "LINE SPACING";
    String PROPERTY_LETTER_SPACING = "LETTER SPACING";
}

