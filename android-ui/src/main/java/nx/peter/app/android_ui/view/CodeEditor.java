package nx.peter.app.android_ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.util.Constant;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CodeEditor extends AbstractView<CodeEditor> {
    protected StyledEditor editor;
    protected StyledText lineNumber;
    protected LinearLayout divider;
    protected Theme theme;
    protected IThemeData data;
    protected IKeywords keywords;
    protected CharSequence text;
    protected Formatter formatter;
    protected int lastLineCount;


    public CodeEditor(Context context) {
        super(context);
    }

    public CodeEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_code, this);
        lineNumber = findViewById(R.id.line_number);
        editor = findViewById(R.id.editor);
        divider = findViewById(R.id.divider);
        // init(getContext());
        reset();

    }

    protected void reset() {
        theme = Theme.Default;
        lastLineCount = 0;
        formatter = code -> {
            Log.i("CodeEditor", code.toString());
            return code;
        };
        keywords = new IKeywords(new ArrayList<>());
        setText("Welcome!\nMy name is Peter.");
        setBackground(Background.Brown);
        // editor.setText("Welcome\nHome...");
        // lineNumber.appendText("\n02");

        // setup();


        editor.setOnTextChangedListener((view, oldText, newText) -> editor.post(() -> {
            if (editor.getLineCount() > lastLineCount)
                updateLine();
        }));
    }

    public void setText(@NonNull CharSequence text) {
        this.text = text;
        setup();
    }

    public int getLineCount() {
        return editor.getLineCount();
    }

    public Theme getTheme() {
        return theme;
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
        setup();
    }

    public void setDividerColor(@ColorInt int color) {
        divider.setBackgroundColor(color);
    }

    public void removeKeyword(@NonNull CharSequence name) {
        keywords.remove(getKeyword(name));
    }

    public void addKeyword(@NonNull CharSequence name, @NonNull Function function) {
        IKeyword keyword = new IKeyword(function, name, data.getColor(function));
        keywords.add(keyword);
        keywords();
        editor.addSuggestions(name);
    }

    public void addKeywords(@NonNull Function function, @NonNull CharSequence... keywords) {
        for (CharSequence key : keywords)
            addKeyword(key, function);
    }

    @NonNull
    public Keywords getKeywords() {
        return keywords;
    }

    @Nullable
    public Keyword getKeyword(@NonNull CharSequence name) {
        return keywords.get(name);
    }

    @Override
    public void setBackground(@NonNull Background background) {
        super.setBackground(background);
        editor.setBackground(background);
        lineNumber.setBackground(background);
    }

    protected ThemeData getTheme(Theme theme) {
        switch (theme) {
            case AriakeDark:
                return ARIAKE_DARK;
            case Dracula:
                return DRACULA;
            case NightOwl:
                return NIGHT_OWL;
            default:
                return NORMAL_THEME;
        }
    }


    protected void setup() {
        editor.setText(formatter != null ? formatter.format(text) : text);
        updateLine();
        // editor.clearSuggestions();
        // editor.addSuggestions(keywords.getKeywords());

        data = (IThemeData) getTheme(theme);
        setBackground(data.background);

        keywords();
    }

    protected void updateLine() {
        editor.post(() -> {
            lineNumber.clearText();
            for (int n = 1; n <= editor.getLineCount(); n++)
                lineNumber.appendText(n + "\n");
            lastLineCount = editor.getLineCount();
        });
    }

    protected void keywords() {
        if (keywords.isNotEmpty())
            for (Function function : keywords.getFunctions()) {
                IKeywords k = ((IKeywords) keywords.getByFunction(function));
                int color = data.getColor(function);
                k.setColor(color);

                editor.addSubColors(color, k.getKeywords());
            }
    }

    @ColorInt
    public static int getColor(@NonNull String colorHex) {
        return Color.parseColor("#" + colorHex);
    }

    public interface Keywords extends Iterable<Keyword> {
        int getCount();

        boolean isEmpty();

        boolean isNotEmpty();

        @Nullable
        Keyword get(int index);

        @Nullable
        Keyword get(@NonNull CharSequence name);

        @NonNull
        Keywords getByColor(@ColorInt int color);

        @NonNull
        Keywords getByFunction(@NonNull Function function);

        @NonNull
        CharSequence[] getKeywords(@NonNull Function function);

        @NonNull
        CharSequence[] getKeywords();

        @NonNull
        List<Function> getFunctions();

        boolean contains(Keyword keyword);

        boolean equals(Keywords keywords);
    }

    protected static class IKeywords implements Keywords {
        protected List<Keyword> keywords;

        public IKeywords(List<Keyword> keywords) {
            set(keywords);
        }

        public void set(@NonNull List<Keyword> keywords) {
            this.keywords = keywords;
        }

        public void setColor(@ColorInt int color) {
            for (Keyword keyword : keywords)
                ((IKeyword) keyword).color = color;
        }

        @NonNull
        @Override
        public Iterator<Keyword> iterator() {
            return keywords.iterator();
        }

        @Override
        public int getCount() {
            return keywords.size();
        }

        @Override
        public boolean isEmpty() {
            return keywords.isEmpty();
        }

        @Override
        public boolean isNotEmpty() {
            return !isEmpty();
        }

        @Nullable
        @Override
        public Keyword get(int index) {
            return index >= 0 && index < getCount() ? keywords.get(index) : null;
        }

        @Nullable
        @Override
        public Keyword get(@NonNull CharSequence name) {
            for (Keyword keyword : keywords)
                if (keyword.equals(name)) return keyword;
            return null;
        }

        @NonNull
        @Override
        public Keywords getByColor(@ColorInt int color) {
            List<Keyword> keywords = new ArrayList<>();
            for (Keyword keyword : this.keywords)
                if (keyword.equals(color)) keywords.add(keyword);
            return new IKeywords(keywords);
        }

        @NonNull
        @Override
        public Keywords getByFunction(@NonNull Function function) {
            List<Keyword> keywords = new ArrayList<>();
            for (Keyword keyword : this.keywords)
                if (keyword.equals(function)) keywords.add(keyword);
            return new IKeywords(keywords);
        }

        @NonNull
        @Override
        public CharSequence[] getKeywords(@NonNull Function function) {
            Keywords keywords = getByFunction(function);
            CharSequence[] names = new CharSequence[keywords.getCount()];
            int index = 0;
            for (Keyword keyword : keywords)
                names[index++] = keyword.getName();
            return names;
        }

        @NonNull
        @Override
        public CharSequence[] getKeywords() {
            Log.i("CodeEditor", getCount() + " count");
            CharSequence[] names = new CharSequence[getCount()];
            int index = 0;
            for (Keyword keyword : keywords)
                names[index++] = keyword.getName();
            return names;
        }

        @NonNull
        @Override
        public List<Function> getFunctions() {
            List<Function> functions = new ArrayList<>();
            for (Keyword keyword : keywords)
                if (!functions.contains(keyword.getFunction()))
                    functions.add(keyword.getFunction());
            return functions;
        }

        @Override
        public boolean contains(Keyword keyword) {
            return keyword != null && keywords.contains(keyword);
        }

        @Override
        public boolean equals(Keywords keywords) {
            return keywords instanceof IKeywords && ((IKeywords) keywords).keywords.equals(this.keywords);
        }

        public void remove(Keyword keyword) {
            keywords.remove(keyword);
        }

        public void add(Keyword keyword) {
            if (keyword != null && !contains(keyword)) keywords.add(keyword);
        }
    }

    public enum Function {
        Klass,
        Method,
        Function,
        Accessibility,
        NativeDataType,
        StringType,
        Comment,
        Others
    }

    public enum Theme {
        AriakeDark,
        Default,
        Dracula,
        NightOwl
    }

    protected static void init(Context c) {
        context = c;
    }

    @SuppressLint("StaticFieldLeak")
    protected static Context context;

    public static final ThemeData NORMAL_THEME = new IThemeData(
            Color.GREEN,
            Constant.PINK,
            Constant.BLUE_LIGHT,
            Color.YELLOW,
            Constant.LIME
    );

    public static final ThemeData ARIAKE_DARK = new IThemeData(
            Constant.PURPLE_LIGHT,
            Constant.BLUE_LIGHT,
            Constant.PINK,
            Color.GREEN,
            Constant.PURPLE
    );

    public static final ThemeData NIGHT_OWL = new IThemeData(
            Constant.VIOLET,
            Constant.LIME,
            Constant.ORANGE,
            Constant.GOLD,
            Constant.PINK
    );

    public static final ThemeData DRACULA = new IThemeData(
            Constant.PINK,
            Constant.LIME,
            Constant.PURPLE_LIGHT,
            Constant.GOLD,
            Color.CYAN
    );


    public interface ThemeData {
        int getColor(@NonNull Function function);
    }

    protected static class IThemeData implements ThemeData {
        public final Background background;
        @ColorInt
        public final int Klass, Method, Function, Accessibility, NativeDataType, StringType, Others, Normal, Comment;

        public IThemeData(
                @ColorInt int klass,
                @ColorInt int method,
                @ColorInt int nativeDataType,
                @ColorInt int stringType,
                @ColorInt int others
        ) {
            this(klass, method, method, klass, nativeDataType, stringType, others);
        }

        public IThemeData(
                @ColorInt int klass,
                @ColorInt int method,
                @ColorInt int function,
                @ColorInt int accessibility,
                @ColorInt int nativeDataType,
                @ColorInt int stringType,
                @ColorInt int others
        ) {
            this(Background.Black, klass, method, function, accessibility, nativeDataType, stringType, others, Color.WHITE, Constant.GREY_LIGHT);
        }

        public IThemeData(
                @NonNull Background background,
                @ColorInt int klass,
                @ColorInt int method,
                @ColorInt int nativeDataType,
                @ColorInt int stringType,
                @ColorInt int others
        ) {
            this(background, klass, method, method, klass, nativeDataType, stringType, others);
        }

        public IThemeData(
                @NonNull Background background,
                @ColorInt int klass,
                @ColorInt int method,
                @ColorInt int function,
                @ColorInt int accessibility,
                @ColorInt int nativeDataType,
                @ColorInt int stringType,
                @ColorInt int others
        ) {
            this(background, klass, method, function, accessibility, nativeDataType, stringType, others, Color.WHITE, Constant.GREY_LIGHT);
        }

        public IThemeData(
                Background background,
                @ColorInt int klass,
                @ColorInt int method,
                @ColorInt int function,
                @ColorInt int accessibility,
                @ColorInt int nativeDataType,
                @ColorInt int stringType,
                @ColorInt int others,
                @ColorInt int normal,
                @ColorInt int comment) {
            this.background = background;
            Klass = klass;
            Method = method;
            Function = function;
            Accessibility = accessibility;
            NativeDataType = nativeDataType;
            StringType = stringType;
            Others = others;
            Normal = normal;
            Comment = comment;
        }

        public int getColor(@NonNull Function function) {
            switch (function) {
                case Klass:
                    return Klass;
                case Method:
                    return Method;
                case Others:
                    return Others;
                case Comment:
                    return Comment;
                case Function:
                    return Function;
                case NativeDataType:
                    return NativeDataType;
                case Accessibility:
                    return Accessibility;
                case StringType:
                    return StringType;
                default:
                    return Normal;
            }
        }
    }

    public interface Keyword {
        Function getFunction();

        CharSequence getName();

        @ColorInt
        int getColor();

        boolean equals(Keyword keyword);

        boolean equals(CharSequence keyword);

        boolean equals(@ColorInt int color);

        boolean equals(Function function);

        boolean hasEqualColor(Keyword keyword);

        boolean hasEqualName(Keyword keyword);

        boolean hasEqualFunction(Keyword keyword);
    }

    protected static class IKeyword implements Keyword {
        final Function function;
        final CharSequence name;
        @ColorInt
        int color;

        protected IKeyword(Function function, CharSequence name, int color) {
            this.function = function;
            this.name = name;
            this.color = color;
        }

        @Override
        public Function getFunction() {
            return function;
        }

        @Override
        public CharSequence getName() {
            return name;
        }

        @Override
        public int getColor() {
            return color;
        }

        @Override
        public boolean equals(Keyword keyword) {
            return keyword != null && equals(keyword.getName());
        }

        @Override
        public boolean equals(CharSequence keyword) {
            return name.equals(keyword);
        }

        @Override
        public boolean hasEqualColor(Keyword keyword) {
            return keyword != null && equals(keyword.getColor());
        }

        @Override
        public boolean hasEqualName(Keyword keyword) {
            return keyword != null && equals(keyword.getName());
        }

        @Override
        public boolean hasEqualFunction(Keyword keyword) {
            return keyword != null && equals(keyword.getFunction());
        }

        @Override
        public boolean equals(int color) {
            return getColor() == color;
        }

        @Override
        public boolean equals(Function function) {
            return function != null && function.equals(getFunction());
        }
    }

    interface Code {
        @NonNull
        CharSequence getText();

        @NonNull
        Keywords getKeywords();

        boolean equals(Code code);
    }

    protected static class ICode implements Code {
        CharSequence text;
        Keywords keywords;

        public ICode(CharSequence text, Keywords keywords) {
            this.text = text;
            this.keywords = keywords;
        }

        @NonNull
        @Override
        public CharSequence getText() {
            return text;
        }

        @NonNull
        @Override
        public Keywords getKeywords() {
            return keywords;
        }

        @Override
        public boolean equals(Code code) {
            return code != null && code.getKeywords().equals(keywords) && code.getText().equals(text);
        }
    }

    public interface OnCodeTextChangedListener {
        void onChanged(@NonNull CodeEditor editor, @NonNull Code oldCode, @NonNull Code newCode);
    }

    public interface Formatter {
        @NonNull
        CharSequence format(@NonNull CharSequence code);
    }

}
