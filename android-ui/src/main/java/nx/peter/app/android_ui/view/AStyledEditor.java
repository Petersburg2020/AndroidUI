package nx.peter.app.android_ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.text.Font;

import java.util.*;

/**
 * AStyledEditor is an abstract view that prototyped every text editor with
 * multiple actions and styles.
 * @param <I> the type of AStyledEditor view
 */
abstract class AStyledEditor<I extends AStyledEditor> extends AStyledView<I, MultiAutoCompleteTextView> implements IStyledEditor<I> {

    protected Tokens tokens;
    protected Tokenizer tokenizer;
    protected Adapter adapter;

    public AStyledEditor(Context context) {
        super(context);
    }

    public AStyledEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void reset() {
        super.reset();
        adapter = new Adapter(new ArrayList<>());
        view.setAdapter(adapter);
        setText("Try google this color, icon icn, underline, large-text, with emphasis and click here!");
        addLinks((view, text, link) -> {}, "here");
        addUrlLinks("https://google.com", "google");
        addSubSizes((int) (getTextSize() * 1.2f), "large-text");
        addSubImages(R.drawable.no_image, "icn");
        addSubFonts(Font.Style.Bold, "emphasis");
        addUnderlines("underline");
        addSubColors(Color.GREEN, "color");
    }

    protected void setAutoComplete(int format) {
        switch (format) {
            case 0: setTokenizer(new CustomTokenizer(Tokenizer.CODING)); break;
            case 1: setTokenizer(new CustomTokenizer(":")); break;
            case 2: setTokens(Tokens.Comma); break;
            case 3: setTokens(Tokens.Dot); break;
            case 4: setTokenizer(new CustomTokenizer(";")); break;
            case 6: setTokens(Tokens.Space);
        }
    }

    @Override
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
        String[] values = new String[suggestions.length];
        for (int i = 0; i < suggestions.length; i++)
            values[i] = suggestions[i].toString();
        addSuggestions(Arrays.asList(values));
    }

    /**
     * Add suggestions to popup
     * @param suggestions a list of suggestions
     */
    public void addSuggestions(@NonNull List<String> suggestions) {
        adapter.addData(suggestions);
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
            StyledText item = convertView.findViewById(R.id.item);
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

}
