package nx.peter.app.android_ui.view;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import nx.peter.app.android_ui.R;

public class StyledScrollableEditor extends AStyledScrollableView<StyledScrollableEditor, StyledEditor> implements IStyledEditor<StyledScrollableEditor> {
    public StyledScrollableEditor(Context context) {
        super(context);
    }

    public StyledScrollableEditor(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_multi_action_scrollable_editor, this);
        vView = findViewById(R.id.v_view);
        hView = findViewById(R.id.h_view);
        vhView = findViewById(R.id.vh_view);
        hContainer = findViewById(R.id.horizontal);
        vContainer = findViewById(R.id.vertical);
        vhContainer = findViewById(R.id.both);

        reset();

    }

    @Override
    protected void reset() {
        super.reset();
    }

    @Override
    public void setTokens(@NonNull Tokens tokens) {
        vView.setTokens(tokens);
        hView.setTokens(tokens);
        vhView.setTokens(tokens);
    }

    @Override
    public void setTokens(@NonNull CharSequence tokens) {
        vView.setTokens(tokens);
        hView.setTokens(tokens);
        vhView.setTokens(tokens);
    }

    @Override
    public Tokens getTokens() {
        return vView.getTokens();
    }

    @Override
    public Tokenizer getTokenizer() {
        return vView.getTokenizer();
    }

    @Override
    public void clearSuggestions() {
        vView.clearSuggestions();
        hView.clearSuggestions();
        vhView.clearSuggestions();
    }

    @Override
    public void addSuggestions(@NonNull CharSequence... suggestions) {
        vView.addSuggestions(suggestions);
        hView.addSuggestions(suggestions);
        vhView.addSuggestions(suggestions);
    }



}
