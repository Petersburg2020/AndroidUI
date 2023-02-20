package nx.peter.app.android_ui.view.pdf;

import androidx.annotation.NonNull;

public interface PDFDocument<P extends PDFDocument> {
    boolean equals(PDFDocument document);


    interface Meta<M extends Meta> {
        String getTitle();
        String getAuthor();
        String getSubject();
        String getCreator();
        String getCreatedDate();
        boolean equals(@NonNull Meta<?> another);
    }

}
