package nx.peter.app.android_ui.view;

import android.view.View;
import androidx.annotation.NonNull;

public interface IStyledEditor<V extends View> extends StyledView<V> {

    /**
     * Add suggestions to popup
     * @param suggestions an array of suggestions
     */
    void addSuggestions(@NonNull CharSequence... suggestions);


    /**
     * Set tokens for this text editor
     * @param tokens the tokens
     */
    void setTokens(@NonNull Tokens tokens);

    /**
     * Set tokens for this text editor
     * @param tokens the tokens
     */
    void setTokens(@NonNull CharSequence tokens);

    /**
     * Remove all suggestions
     */
    void clearSuggestions();

    /**
     * Get editor's tokenizer
     * @return tokenizer
     */
    Tokenizer getTokenizer();

    /**
     * Get the tokens set for this text editor
     * @return the tokens on this editor
     */
    Tokens getTokens();
}
