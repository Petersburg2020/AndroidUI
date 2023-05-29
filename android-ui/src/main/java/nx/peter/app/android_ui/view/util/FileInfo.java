package nx.peter.app.android_ui.view.util;

import android.net.Uri;
import androidx.annotation.NonNull;
import nx.peter.java.io.File;

public interface FileInfo {
    @NonNull
    Uri getUri();
    @NonNull
    CharSequence getPath();
    @NonNull
    File getFile();
    long getDateAdded();
}
