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
import nx.peter.app.android_ui.view.text.Font;
import nx.peter.app.android_ui.view.util.Constant;
import nx.peter.java.util.data.Number;
import nx.peter.java.util.data.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CodeEditor extends AView<CodeEditor> {
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
        setText("#!/bin/bash\ndouble n = 10.0;\nint age = 27; // Here is a single line comment.\nString name = \"Peter\"");
        setBackground(Background.Black);
        setTextSize(18);
        setTheme(Theme.Dracula);

        // editor.appendText(getProcessor().extractValidNumbers(2).toString());
        appendText("\nfor (int i = 0; i < 20; i++) {\n\tSystem.out.println(i);\n}");
        // editor.setText("Welcome\nHome...");
        // lineNumber.appendText("\n02");
        // appendText("" + newLineCount());

        // setup();
        // setText(getSourceCode().getContent().get() + "\n" + getLineCount());

        editor.setOnTextChangedListener((view, oldText, newText) ->
                editor.post(() -> {
                    if (editor.getLineCount() > lastLineCount)
                        updateLine();
                }));

        addKeywords(Function.Others, "for");
        addKeywords(Function.NativeDataType, "int", "double", "long", "float", "short", "byte");
    }

    public Source getSourceCode() {
        return getProcessor().getSource();
    }

    public Blocks extractStrings() {
        return getProcessor().extractBlock(BlockType.String, "\"", "\"");
    }

    public Blocks extractComments() {
        return getProcessor().extractBlock(BlockType.String, "//", "//");
    }

    public Processor getProcessor() {
        return new Processor(editor.getText());
    }

    public CharSequence getText() {
        return editor.getText();
    }

    public void setText(@NonNull CharSequence text) {
        this.text = text;
        setup();
    }

    public void appendText(@NonNull CharSequence text) {
        this.text += text.toString();
        setup();
    }

    public void setFont(@NonNull Font font) {
        editor.setFont(font);
        lineNumber.setFont(font);
    }

    public Font getFont() {
        return editor.getFont();
    }

    public Font.Style getFontStyle() {
        return getFont().getStyle();
    }

    public void setFontStyle(@NonNull Font.Style style) {
        editor.setFontStyle(style);
        lineNumber.setFontStyle(style);
    }

    public void setTextSize(float size) {
        editor.setTextSize(size);
        lineNumber.setTextSize(size);
    }

    public float getTextSize() {
        return editor.getTextSize();
    }

    public int getLineCount() {
        return newLineCount() + 1;
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

    protected void addKeyword(@NonNull CharSequence name, @NonNull Function function) {
        IKeyword keyword = new IKeyword(function, name, data.getTypeColor(function));
        keywords.add(keyword);
        setup();
        // editor.addSuggestions(name);
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
        // editor.setTextColor(data.getValueColor(Function.Others));
        updateLine();
        // editor.clearSuggestions();
        // editor.addSuggestions(keywords.getKeywords());

        data = (IThemeData) getTheme(theme);
        setBackground(data.background);

        // editor.appendText("\n" + keywords.getKeywords().length);

        keywords();
        numbers();
        // comments();
    }

    protected void updateLine() {
        lineNumber.clearText();
        for (int n = 1; n <= getLineCount(); n++)
            lineNumber.appendText(n + "\n");
        lastLineCount = getLineCount();

        /*editor.post(() -> {
            lineNumber.clearText();
            for (int n = 1; n <= getLineCount(); n++)
                lineNumber.appendText(n + "\n");
            lastLineCount = getLineCount();
        });*/
    }

    protected void keywords() {
        if (keywords.isNotEmpty())
            for (Function function : keywords.getFunctions()) {
                IKeywords k = ((IKeywords) keywords.getByFunction(function));
                int color = data.getTypeColor(function);
                k.setColor(color);
                editor.addSubColors(color, k.getKeywords());
            }
    }

    protected void numbers() {
        for (Double number : new Texts(editor.getText()).extractDoubles())
            editor.addSubColors(data.getValueColor(Function.NativeDataType), number.toString());

        for (Number number : new Texts(editor.getText()).extractNumbers())
            editor.addSubColors(data.getValueColor(Function.NativeDataType), number.get());
    }

    protected int newLineCount() {
        return DataCreator.createSentence(null, getText()).count('\n');
    }

    protected void comments() {
        // Single line comment format
        for (int line = 1; line < getSourceCode().getLineCount(); line++) {
            Line l = getSourceCode().getLine(line);
            editor.addSubColors(data.getTypeColor(Function.Comment), l.getContent().subLetters(l.getContent().indexOf("//")));
        }
    }


    @ColorInt
    protected static int getColor(@NonNull String colorHex) {
        return Color.parseColor("#" + colorHex);
    }


    public interface BreadCrumb {
        int getEnd();

        int getStart();

        <S extends ISentence<S>> S getContent();

        boolean isValid();

        int getLineInCode();

        @NonNull
        CharSequence format(@NonNull Formatter formatter);
    }

    public interface Block extends BreadCrumb {
        BlockType getType();

        int getLineInCode();

        Tag getTag();

        Source getSource();

        Texts getContent();

        int getLineCount();

        Line getLine(int line);

        Lines extractLines();

        boolean isInBlock(BreadCrumb crumb);
    }

    public interface Source {
        Texts getContent();

        int getLineCount();

        int getLength();

        Lines extractLines();

        Line getLine(int line);

        Line lineOf(int index);

        int getCount(Keyword keyword);

        int getCount(CharSequence text);

        boolean contains(Line line);

        boolean contains(Keyword keyword);

        boolean contains(BreadCrumb crumb);
    }

    public interface Line {
        int getNumber();

        boolean isValid();

        boolean isEmpty();

        int getEnd();

        int getStart();

        Sentence getContent();

        boolean isNotEmpty();

        int getCount(Keyword keyword);

        int getCount(CharSequence text);

        boolean equals(Line line);

        boolean isOnLine(BreadCrumb crumb);

        boolean endsOnLine(BreadCrumb crumb);

        boolean startsFromLine(BreadCrumb crumb);

        boolean containsTag(CharSequence start, CharSequence end);
    }

    public interface Lines extends Iterable<Line> {
        Line getLine(int line);

        boolean contains(Line line);

        int lineNumberOf(Line line);

        int getLineCount();

        Source getSource();

        boolean equals(Lines lines);
    }

    public interface Blocks extends Iterable<Block> {
        int getIndexOf(Block block);

        boolean contains(Block block);

        Blocks getBlocksByType(BlockType type);

        Blocks getBlocksByTag(CharSequence start, CharSequence end);

        boolean equals(Blocks blocks);

        Source getSource();
    }

    static class ILine implements Line {
        Source source;
        String content;
        int start, number;

        public ILine(Source source, CharSequence content, int start, int number) {
            setSource(source);
            setContent(content);
            setNumber(number);
            setStart(start);
        }

        public void setSource(Source source) {
            this.source = source;
        }

        public void setContent(CharSequence content) {
            this.content = content != null ? content.toString() : "";
        }

        public void setStart(int start) {
            this.start = start;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        @Override
        public int getNumber() {
            return number;
        }

        @Override
        public boolean isValid() {
            return start >= 0 && start < source.getLength() && !content.isEmpty();
        }

        @Override
        public boolean isEmpty() {
            return content.isEmpty();
        }

        @Override
        public int getEnd() {
            return isValid() ? start + content.length() : -1;
        }

        @Override
        public int getStart() {
            return start;
        }

        @Override
        public boolean isNotEmpty() {
            return !isEmpty();
        }

        @Override
        public Sentence getContent() {
            return new Sentence(content);
        }

        @Override
        public int getCount(Keyword keyword) {
            return getCount(keyword != null ? (source.getContent().startsWith(keyword.getName()) ? "" : " ") + keyword.getName() + (source.getContent().endsWith(keyword.getName()) ? "" : " ") : null);
        }

        @Override
        public int getCount(CharSequence text) {
            return text != null ? getContent().count(text) : 0;
        }

        @Override
        public boolean equals(Line line) {
            return line != null && line.getContent().contentEquals(content) && line.getNumber() == number;
        }

        @Override
        public boolean isOnLine(BreadCrumb crumb) {
            return startsFromLine(crumb) && endsOnLine(crumb);
        }

        @Override
        public boolean endsOnLine(BreadCrumb crumb) {
            return crumb != null && crumb.getEnd() <= getEnd();
        }

        @Override
        public boolean startsFromLine(BreadCrumb crumb) {
            return crumb != null && crumb.getStart() >= start;
        }

        @Override
        public boolean containsTag(CharSequence start, CharSequence end) {
            int sIndex = getContent().indexOf(start);
            int eIndex = getContent().indexOf(end, sIndex);
            Tag tag = new ITag(start, end, "");
            return tag.isSingleTag() ? sIndex > -1 : tag.isDoubleTag() && sIndex > -1 && eIndex > -1;
        }
    }

    static class ILines implements Lines {
        List<Line> lines;
        Source source;

        public ILines(Source source, List<Line> lines) {
            this.lines = lines;
            setSource(source);
        }

        public void setSource(Source source) {
            this.source = source;
            for (Line line : lines)
                ((ILine) line).setSource(source);
        }

        @Override
        public Line getLine(int line) {
            return line > 0 && line <= getLineCount() ? lines.get(line - 1) : new
                    ILine(source, "", -1, 0);
        }

        public void add(Line line) {
            if (line != null) {
                ((ILine) line).setNumber(lines.size() + 1);
                lines.add(line);
            }
        }

        @Override
        public boolean contains(Line line) {
            return line != null && lines.contains(line);
        }

        @Override
        public int lineNumberOf(Line line) {
            return line != null ? lines.indexOf(line) : 0;
        }

        @Override
        public int getLineCount() {
            return lines.size();
        }

        @Override
        public Source getSource() {
            return source;
        }

        @Override
        public boolean equals(Lines lines) {
            return lines != null && ((ILines) lines).lines.equals(this.lines);
        }

        @NonNull
        @Override
        public Iterator<Line> iterator() {
            return lines.iterator();
        }
    }

    static class IBlock implements Block {
        String start, end;
        Source source;
        BlockType type;
        int index;

        public IBlock(@NonNull BlockType type, CharSequence start, CharSequence end, int index) {
            this(type, null, start, end, index);
        }

        public IBlock(@NonNull BlockType type, Source source, CharSequence start, CharSequence end, int index) {
            setSource(source);
            setStart(start);
            setType(type);
            setEnd(end);
            setStart(index);
        }

        public void setType(BlockType type) {
            this.type = type;
        }

        public void setSource(Source source) {
            this.source = source;
        }

        public void setStart(int index) {
            this.index = index;
        }

        public void setStart(CharSequence start) {
            this.start = start != null ? start.toString() : "";
        }

        public void setEnd(CharSequence end) {
            this.end = end != null ? end.toString() : "";
        }

        @Override
        public BlockType getType() {
            return type;
        }

        @Override
        public int getEnd() {
            return index >= 0 ? index + getContent().length() : -1;
        }

        @Override
        public int getStart() {
            return index;
        }

        @Override
        public boolean isValid() {
            return index > -1 && getContent().isNotEmpty();
        }

        @Override
        public int getLineInCode() {
            return new Texts(getSource().getContent().subLetters(0, index).get()).extractParagraphs().size();
        }

        @Override
        public Tag getTag() {
            return new ITag(start, end, type.name());
        }

        @Override
        public Source getSource() {
            return source;
        }

        @Override
        public Texts getContent() {
            return new Texts(getSource().getContent().subLetters(start, index, end));
        }

        @Override
        public int getLineCount() {
            return extractLines().getLineCount();
        }

        @Override
        public Line getLine(int line) {
            return line > 0 && line < getLineCount() ? extractLines().getLine(line) : new ILine(source, "", -1, 0);
        }

        @Override
        public Lines extractLines() {
            List<Line> lines = new ArrayList<>();
            int index = 0, line = 1;
            ISentence.Paragraphs paragraphs = getContent().extractParagraphs();
            for (Paragraph paragraph : paragraphs)
                while (line < paragraphs.size()) {
                    index = getContent().indexOf(paragraph, index);

                    if (index <= -1 || line > paragraphs.size()) break;

                    lines.add(new ILine(source, paragraph.get(), index, line));
                    line++;
                }
            return new ILines(source, lines);
        }

        @Override
        public boolean isInBlock(BreadCrumb crumb) {
            return crumb != null && crumb.getStart() >= getStart() && crumb.getEnd() <= getEnd();
        }

        @NonNull
        @Override
        public CharSequence format(@NonNull Formatter formatter) {
            return formatter.format(getContent().get());
        }

    }

    static class ISource implements Source {
        String source;
        ITags tags;

        public ISource(CharSequence source, ITags tags) {
            setSource(source);
            this.tags = tags != null ? tags : new ITags(new ArrayList<>());
        }

        public void setSource(CharSequence source) {
            this.source = source != null ? source.toString() : "";
        }

        public void addTag(Tag tag) {
            tags.add(tag);
        }

        @Override
        public Texts getContent() {
            return new Texts(source);
        }

        @Override
        public int getLineCount() {
            return extractLines().getLineCount();
        }

        @Override
        public int getLength() {
            return source.length();
        }

        @Override
        public Lines extractLines() {
            List<Line> lines = new ArrayList<>();
            ISentence.Paragraphs paragraphs = getContent().extractParagraphs();
            int index = 0, paragraph = 1;
            while (paragraph > -1) {
                Paragraph para = paragraphs.getParagraph(paragraph);
                index = getContent().indexOf(para, index);

                if (index <= -1 || paragraph > paragraphs.size()) break;

                lines.add(new ILine(this, para.get(), index, paragraph));
                paragraph++;
            }
            return new ILines(this, lines);
        }

        @Override
        public Line getLine(int line) {
            return line > 0 && line <= getLineCount() ? extractLines().getLine(line) : new ILine(this, "", -1, 0);
        }

        @Override
        public Line lineOf(int index) {
            return getLine(new Texts(getContent().subLetters(0, index).get()).extractParagraphs().size());
        }

        @Override
        public int getCount(Keyword keyword) {
            return getCount(keyword.getName());
        }

        @Override
        public int getCount(CharSequence text) {
            return getContent().wordCount(text);
        }

        @Override
        public boolean contains(Line line) {
            return extractLines().contains(line);
        }

        @Override
        public boolean contains(Keyword keyword) {
            return getCount(keyword) <= 0;
        }

        @Override
        public boolean contains(BreadCrumb crumb) {
            return crumb != null && source.contains(crumb.getContent().get());
        }
    }

    static class IBlocks implements Blocks {
        List<Block> blocks;
        Source source;

        public IBlocks(Source source, List<Block> blocks) {
            for (Block b : blocks)
                ((IBlock) b).setSource(source);
            this.blocks = blocks;
            this.source = source;
        }

        public void addBlock(Block block) {
            blocks.add(block);
        }

        public void setSource(Source source) {
            this.source = source;
        }

        public void setBlocks(List<Block> blocks) {
            this.blocks = blocks;
        }

        @Override
        public int getIndexOf(Block block) {
            if (block != null)
                for (Block b : blocks)
                    if (b.equals(block)) return b.getStart();
            return -1;
        }

        @Override
        public boolean contains(Block block) {
            return block != null && blocks.contains(block);
        }

        @Override
        public Blocks getBlocksByType(BlockType type) {
            List<Block> blcks = new ArrayList<>();
            for (Block b : blocks)
                if (b.getType().equals(type)) blcks.add(b);
            return new IBlocks(source, blcks);
        }

        @Override
        public Blocks getBlocksByTag(CharSequence start, CharSequence end) {
            List<Block> blcks = new ArrayList<>();
            for (Block b : blocks)
                if (b.getTag().equals(start, end)) blcks.add(b);
            return new IBlocks(source, blcks);
        }

        @Override
        public boolean equals(Blocks blocks) {
            return blocks != null && ((IBlocks) blocks).blocks.equals(this.blocks);
        }

        @Override
        public Source getSource() {
            return source;
        }

        @NonNull
        @Override
        public Iterator<Block> iterator() {
            return blocks.iterator();
        }
    }

    public interface Tag {
        String getName();

        String getStart();

        String getEnd();

        boolean isDoubleTag();

        boolean isSingleTag();

        boolean equals(Tag tag);

        boolean equals(CharSequence start, CharSequence end);
    }

    static class ITag implements Tag {
        String start, end, name;

        public ITag(CharSequence start, CharSequence end, CharSequence name) {
            setEnd(end);
            setName(name);
            setStart(start);
        }

        public void setStart(CharSequence start) {
            this.start = start != null ? start.toString() : "";
        }

        public void setEnd(CharSequence end) {
            this.end = end != null ? end.toString() : "";
        }

        public void setName(CharSequence name) {
            this.name = name != null ? name.toString() : "";
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getStart() {
            return start;
        }

        @Override
        public String getEnd() {
            return end;
        }

        @Override
        public boolean isDoubleTag() {
            return !isSingleTag() && !start.isEmpty() && !end.isEmpty();
        }

        @Override
        public boolean isSingleTag() {
            return (!start.isEmpty() && end.isEmpty()) || (start.isEmpty() && !end.isEmpty());
        }

        @Override
        public boolean equals(Tag tag) {
            return tag != null && tag.getStart().contentEquals(start) && tag.getEnd().contentEquals(end);
        }

        @Override
        public boolean equals(CharSequence start, CharSequence end) {
            return equals(new ITag(start, end, null));
        }

    }

    public interface Tags extends Iterable<Tag> {
        Tag getTag(CharSequence name);

        int tagCount();

        boolean contains(Tag tag);

        boolean equals(Tags tags);
    }

    static class ITags implements Tags {
        List<Tag> tags;

        public ITags(List<Tag> tags) {
            this.tags = tags;
        }

        public void setTags(List<Tag> tags) {
            this.tags = tags;
        }

        public void add(Tag tag) {
            if (tag != null) tags.add(tag);
        }

        public void remove(Tag tag) {
            if (tag != null) tags.remove(tag);
        }

        @Override
        public Tag getTag(CharSequence name) {
            if (name != null)
                for (Tag tag : tags)
                    if (tag.getName().contentEquals(name)) return tag;
            return null;
        }

        @Override
        public int tagCount() {
            return tags.size();
        }

        @Override
        public boolean contains(Tag tag) {
            return tag != null && tags.contains(tag);
        }

        @Override
        public boolean equals(Tags tags) {
            return tags != null && ((ITags) tags).tags.equals(this.tags);
        }

        @NonNull
        @Override
        public Iterator<Tag> iterator() {
            return tags.iterator();
        }
    }

    interface Syntax {

    }

    enum BlockType {
        Block,
        Comment,
        String,
        Array,
        List,
        Map
    }

    public enum DataType {
        String,
        Integer,
        Float,
        Double,
        Byte,
        Array,
        Map,
        List,
        Object
    }

    static class Processor {
        ISource source;
        Blocks comment, strings;

        public Processor(CharSequence source) {
            setSource(source);
        }

        public Processor(CharSequence source, @NonNull List<Tag> tags) {
            setSource(source);
            setTags(tags);
        }

        public void setTags(@NonNull List<Tag> tags) {
            for (Tag tag : tags)
                source.addTag(tag);
        }

        /**
         * Extract all blocks with tag and block type
         *
         * @param type  block type
         * @param start start tag
         * @param end   end tag
         * @return block list within the source code
         */
        public Blocks extractBlock(BlockType type, CharSequence start, CharSequence end) {
            List<Block> blocks = new ArrayList<>();
            int index = 0;
            while (index > -1) {
                index = source.getContent().indexOf(start, index);
                int sIndex = index; // Start Index
                if (index <= -1) break; // terminate if start tag does not exist

                // index = source.getContent().indexOf(end, index); // Get index of end tag
                // int eIndex = index; // End index
                // if (index <= -1) break; // Terminate if end tag does not exist
                blocks.add(new IBlock(type, source, start, end, index));
            }
            return new IBlocks(source, blocks);
        }

        @NonNull
        public Source getSource() {
            return source;
        }

        public void setSource(CharSequence source) {
            this.source = new ISource(source != null ? source : "", null);
        }

        public void addTag(CharSequence name, CharSequence start, CharSequence end) {
            source.addTag(new ITag(start, end, name));
        }

        public Lines extractLines() {
            return getSource().extractLines();
        }

        public Line getLine(int line) {
            return extractLines().getLine(line);
        }

        public boolean hasNumber(int line) {
            return getLine(line).getContent().hasNumber();
        }

        /**
         * This helps extract numbers that exists before the comment and string tags on the line in question
         *
         * @param line the line number in question
         * @return numbers that exists in the line
         */
        public Letters.Numbers extractValidNumbers(int line) {
            // To get a valid string from the line,
            // initialize the valid string as the entire line
            Sentence valid = getLine(line).getContent();
            // Check if comment exists in the line,
            // If comment exists, the extract the code before comment
            // Also check if number is in string
            int stringIndex = valid.indexOf("\"");
            int commentIndex = valid.indexOf("//");

            // Get the index of the first attribute(string || comment)
            // to exist on the line
            int index = getFirst(stringIndex, commentIndex);

            // Check if at least a string or comment is on the line,
            // If index > -1, then it exists,
            // then index is the first of the two attributes to exist.
            // If not, then th entire line is a valid string to be used
            if (index > -1)
                valid.set(valid.subLetters(0, index));

            // Extract all numbers preceding the attributes
            return valid.extractNumbers();
        }

        int getFirst(int a, int b) {
            return a > -1 && a < b ? a : b > -1 && b < a ? b : -1;
        }

        /**
         * This helps extract booleans that exists before the comment and string tags on the line in question
         *
         * @param line the line number in question
         * @return booleans that exists in the line
         */
        public Letters.Words extractValidBooleans(int line) {
            // To get a valid string from the line,
            // initialize the valid string as the entire line
            Sentence valid = getLine(line).getContent();
            // Check if comment exists in the line,
            // If comment exists, the extract the code before comment
            // Also check if number is in string
            int stringIndex = valid.indexOf("\"");
            int commentIndex = valid.indexOf("//");

            // Get the index of the first attribute(string || comment)
            // to exist on the line
            int index = getFirst(stringIndex, commentIndex);

            // Check if at least a string or comment is on the line,
            // If index > -1, then it exists,
            // then index is the first of the two attributes to exist.
            // If not, then th entire line is a valid string to be used
            if (index > -1)
                valid.set(valid.subLetters(0, index));

            // Extract all booleans preceding the attributes
            List<Word> words = new ArrayList<>();
            for (Word word : valid.extractWords())
                if (word.contentEquals("true") || word.contentEquals("false"))
                    words.add(word);
            return new Letters.Words(words, valid);
        }

        /**
         * Get the number of lines in source code
         *
         * @return number of lines in source code
         */
        public int getLineCount() {
            return extractLines().getLineCount();
        }

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
            Constant.LIME,
            Constant.PURPLE_LIGHT
    );

    public static final ThemeData ARIAKE_DARK = new IThemeData(
            Constant.PURPLE_LIGHT,
            Constant.BLUE_LIGHT,
            Constant.PINK,
            Color.GREEN,
            Constant.PURPLE,
            Constant.GOLD
    );

    public static final ThemeData NIGHT_OWL = new IThemeData(
            Constant.VIOLET,
            Constant.LIME,
            Constant.ORANGE,
            Constant.GOLD,
            Constant.PINK,
            Constant.PURPLE_LIGHT
    );

    public static final ThemeData DRACULA = new IThemeData(
            Background.Black,
            Constant.PURPLE,
            Constant.GREY_LIGHT,
            Constant.GREY_LIGHT,
            Constant.ORANGE,
            Constant.ORANGE,
            Color.WHITE,
            Color.WHITE,
            Constant.WHITE,
            Color.GRAY,
            Constant.BLUE_LIGHT,
            Constant.ORANGE
    );


    public interface ThemeData {
        int getTypeColor(@NonNull Function function);
    }

    protected static class IThemeData implements ThemeData {
        public final Background background;
        @ColorInt
        public final int Klass, Method, Function, Accessibility, NativeDataType, StringType, Others, Normal, Comment, NativeDataValue, StringValue, KlassName, MethodValue, OtherValue;

        public IThemeData(
                @ColorInt int klass,
                @ColorInt int method,
                @ColorInt int nativeDataType,
                @ColorInt int stringType,
                @ColorInt int others,
                @ColorInt int dataValue
        ) {
            this(klass, method, method, klass, nativeDataType, stringType, others, dataValue);
        }

        public IThemeData(
                @ColorInt int klass,
                @ColorInt int method,
                @ColorInt int function,
                @ColorInt int accessibility,
                @ColorInt int nativeDataType,
                @ColorInt int stringType,
                @ColorInt int others,
                @ColorInt int dataValue
        ) {
            this(Background.Black, klass, method, function, accessibility, nativeDataType, stringType, others, Color.WHITE, Constant.GREY_LIGHT, dataValue);
        }

        public IThemeData(
                @NonNull Background background,
                @ColorInt int klass,
                @ColorInt int method,
                @ColorInt int nativeDataType,
                @ColorInt int stringType,
                @ColorInt int others,
                @ColorInt int dataValue
        ) {
            this(background, klass, method, method, klass, nativeDataType, stringType, others, dataValue);
        }

        public IThemeData(
                @NonNull Background background,
                @ColorInt int klass,
                @ColorInt int method,
                @ColorInt int function,
                @ColorInt int accessibility,
                @ColorInt int nativeDataType,
                @ColorInt int stringType,
                @ColorInt int others,
                @ColorInt int dataValue
        ) {
            this(background, klass, method, function, accessibility, nativeDataType, stringType, others, Color.WHITE, Constant.GREY_LIGHT, dataValue);
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
                @ColorInt int comment,
                @ColorInt int dataValue) {
            this(background, klass, method, function, accessibility, nativeDataType, stringType, others, normal, comment, dataValue, dataValue);
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
                @ColorInt int comment,
                @ColorInt int nativeDataValue,
                @ColorInt int stringValue) {
            this(background, klass, method, function, accessibility, nativeDataType, stringType, others, normal, comment, nativeDataValue, stringValue, normal);
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
                @ColorInt int comment,
                @ColorInt int nativeDataValue,
                @ColorInt int stringValue,
                @ColorInt int otherValue) {
            this(background, klass, method, function, accessibility, nativeDataType, stringType, others, normal, comment, nativeDataValue, stringValue, otherValue, otherValue, otherValue);
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
                @ColorInt int comment,
                @ColorInt int nativeDataValue,
                @ColorInt int stringValue,
                @ColorInt int klassName,
                @ColorInt int methodValue,
                @ColorInt int otherValue) {
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
            NativeDataValue = nativeDataValue;
            StringValue = stringValue;
            KlassName = klassName;
            MethodValue = methodValue;
            OtherValue = otherValue;
        }

        public int getTypeColor(@NonNull Function function) {
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

        @ColorInt
        public int getValueColor(@NonNull Function function) {
            switch (function) {
                case Klass:
                    return KlassName;
                case Method:
                case Function:
                    return MethodValue;
                case Others:
                    return OtherValue;
                case Comment:
                    return Comment;
                case NativeDataType:
                    return NativeDataValue;
                case StringType:
                    return StringValue;
                default:
                    return Normal;
            }

        }
    }

    public interface Keyword {
        Function getFunction();

        CharSequence getName();

        @ColorInt
        int getDataColor();

        @ColorInt
        int getValueColor();

        boolean equals(Keyword keyword);

        boolean equals(CharSequence keyword);

        boolean equals(@ColorInt int color);

        boolean equals(Function function);

        boolean hasEqualColor(Keyword keyword);

        boolean hasEqualName(Keyword keyword);

        boolean hasEqualFunction(Keyword keyword);
    }

    protected static class IKeyword implements Keyword {
        public final Function function;
        public final CharSequence name;
        @ColorInt
        public int color, valueColor;

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

        public void setColor(@ColorInt int color) {
            this.color = color;
        }

        @Override
        public int getDataColor() {
            return color;
        }

        @Override
        public int getValueColor() {
            return valueColor;
        }

        public void setValueColor(@ColorInt int valueColor) {
            this.valueColor = valueColor;
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
            return keyword != null && equals(keyword.getDataColor());
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
            return getDataColor() == color;
        }

        @Override
        public boolean equals(Function function) {
            return function != null && function.equals(getFunction());
        }
    }

    interface Code {
        @NonNull
        Source getSource();

        @NonNull
        Keywords getKeywords();

        Blocks getBlocks();

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
        public Source getSource() {
            return new ISource(text, null);
        }

        @NonNull
        @Override
        public Keywords getKeywords() {
            return keywords;
        }

        @Override
        public Blocks getBlocks() {
            return null;
        }

        @Override
        public boolean equals(Code code) {
            return code != null && code.getKeywords().equals(keywords) && code.getSource().equals(text);
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
