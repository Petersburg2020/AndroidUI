package nx.peter.app.android_ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
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
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.text.*;

import java.util.*;

/**
 * IMultiActionEditor is an abstract view that prototyped every text editor with
 * multiple actions and styles.
 * @param <I> the type of IMultiActionEditor view
 */
abstract class IMultiActionEditor<I extends IMultiActionEditor> extends IMultiActionView<I, MultiAutoCompleteTextView> {

    protected Tokens tokens;
    protected Tokenizer tokenizer;
    protected Adapter adapter;

    public IMultiActionEditor(Context context) {
        super(context);
    }

    public IMultiActionEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void reset() {
        super.reset();
        adapter = new Adapter(new ArrayList<>());
        view.setAdapter(adapter);
        background = Background.Transparent;
        setText("Try google this color, icon icn, underline, large-text, with emphasis and click here!");

    }

    /**
     * Set tokens for this text editor
     * @param tokens the tokens
     */
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

    /**
     * Set tokener for this text editor
     * @param tokenizer the tokenizer
     */
    private void setTokenizer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
        view.setTokenizer(tokenizer);
    }

    /**
     * Set tokens for this text editor
     * @param tokens the tokens
     */
    public void setTokens(@NonNull CharSequence tokens) {
        if (tokens.length() > 0) {
            this.tokens = Tokens.Custom;
            setTokenizer(new CustomTokenizer(tokens.toString()));
        }
    }

    /**
     * Get the tokens set for this text editor
     * @return the tokens on this editor
     */
    public Tokens getTokens() {
        return tokens;
    }

    /**
     * Get editor's tokenizer
     * @return tokenizer
     */
    public Tokenizer getTokenizer() {
        return tokenizer;
    }

    /**
     * Remove all suggestions
     */
    public void clearSuggestions() {
        adapter.setData(new ArrayList<>());
    }

    /**
     * Add suggestions to popup
     * @param suggestions an array of suggestions
     */
    public void addSuggestions(@NonNull CharSequence... suggestions) {
        String[] vals = new String[suggestions.length];
        for (int i = 0; i < suggestions.length; i++)
            vals[i] = suggestions[i].toString();
        addSuggestions(Arrays.asList(vals));
    }

    /**
     * Add suggestions to popup
     * @param suggestions a list of suggestions
     */
    public void addSuggestions(@NonNull List<String> suggestions) {
        adapter.addData(suggestions);
    }


    /**
     * Tokens refers to the suggestion triggers.
     * Once a token is inputted in the editor, it automatically triggers
     * auto suggestions popup.
     */
    public enum Tokens {
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
     * The adapter for the MultiAutoCompleteTextView that houses all suggestions
     * and allow for modification of each suggestion tile
     */
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

    /**
     * CustomTokenizer - is a type of {@link Tokenizer} that takes user customized tokens as its token
     */
    public static class CustomTokenizer extends Tokenizer {
        public CustomTokenizer(@NonNull String tokens) {
            super(tokens);
        }
    }

    /**
     * DotTokenizer - is a type of {@link Tokenizer} that takes only dot as its token
     */
    public static class DotTokenizer extends Tokenizer {
        public DotTokenizer() {
            super(".");
        }
    }

    /**
     * CommaTokenizer - is a type of {@link Tokenizer} that takes only comma as its token
     */
    public static class CommaTokenizer extends Tokenizer {
        public CommaTokenizer() {
            super(",");
        }
    }

    /**
     * SpaceTokenizer - is a type of {@link Tokenizer} that takes only spaces as its token
     */
    public static class SpaceTokenizer extends Tokenizer {
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
