package nx.peter.app.android_ui.view.pdf.subscale.decoder;
import androidx.annotation.NonNull;
import java.lang.reflect.InvocationTargetException;

public interface DecoderFactory<T> {
    @NonNull
    T make() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException;
}
