package nx.peter.app.android_ui.view.pdf.reader;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;

import java.io.OutputStream;

public interface PDFDocument<P extends PDFDocument> extends nx.peter.app.android_ui.view.pdf.PDFDocument<P> {
    P addImage(@NonNull Bitmap image);
    P newPage(int pageNumber);
    boolean save(@NonNull OutputStream output);
}
