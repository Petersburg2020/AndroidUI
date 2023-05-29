package nx.peter.app.android_ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.pdf.PdfFile;
import nx.peter.app.android_ui.view.pdf.remote.DownloadFile;
import nx.peter.java.io.File;

import java.io.IOException;
import java.util.List;

public class PdfView extends AView<PdfView> {
    protected Content<PdfView, ViewPager> view;

    public PdfView(Context context) {
        super(context);
    }

    public PdfView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_pdf, this);
        view = findViewById(R.id.view);
        reset();


    }

    @Override
    protected void reset() {

    }

    public void setPdfUrl(@NonNull CharSequence url, DownloadListener listener) {
        DownloadListener downloadListener = new DownloadListener() {
            @Override
            public void onSuccess(String url, String destinationPath) {
                if (listener != null) listener.onSuccess(url, destinationPath);
            }

            @Override
            public void onFailure(Exception e) {
                if (listener != null) listener.onFailure(e);
            }

            @Override
            public void onProgressUpdate(int progress, int total) {
                if (listener != null) listener.onProgressUpdate(progress, total);
            }
        };

        RemotePDFViewPager pager = new RemotePDFViewPager(getContext(), url.toString(), downloadListener);

        setView(pager);
    }

    public void setPdfFile(@NonNull CharSequence path) {
        try {
            ParcelFileDescriptor pfd = ParcelFileDescriptor.open(new File(path), ParcelFileDescriptor.MODE_READ_ONLY);
            PdfRenderer renderer = new PdfRenderer(pfd);
            // renderer.openPage(1).
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // List<PdfDocument.PageInfo> pages = doc.getPages();
    }

    protected void setView(ViewPager pager) {
        view.setContentView(pager);
    }


    public interface DownloadListener extends DownloadFile.Listener {}

    protected static class IPage implements PdfFile.Page {
        int number;
        final Bitmap bitmap;

        protected IPage(int number, Bitmap bitmap) {
            this.number = number;
            this.bitmap = bitmap;
        }

        @Override
        public int getNumber() {
            return number;
        }

        @Override
        public int getWidth() {
            return bitmap.getWidth();
        }

        @Override
        public int getHeight() {
            return bitmap.getHeight();
        }

        @Override
        public Bitmap getBitmap() {
            return bitmap;
        }
    }

    protected static class IPdfFile implements PdfFile {
        List<Page> pages;

        @Override
        public boolean equals(PdfFile document) {
            return false;
        }

        @Override
        public Pages getPages() {
            return new Pages(pages);
        }

        @Override
        public Page getPage(int page) {
            for (Page p : pages)
                if (p.getNumber() == page) return p;
            return null;
        }

        @Override
        public Meta getMeta() {
            return null;
        }

        @Override
        public void close() {

        }
    }

}
