package nx.peter.app.android_ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.itextpdf.text.pdf.PdfReader;
import nx.peter.app.android_ui.R;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class PdfView extends AView<PdfView> {
    protected ImageView view;
    protected PdfFile pdfFile;

    public PdfView(Context context) {
        super(context);
    }

    public PdfView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(AttributeSet attrs) {
        inflate(getContext(), R.layout.view_pdf, this);



    }

    @Override
    protected void reset() {

    }

    public nx.peter.app.android_ui.view.pdf.PdfFile getPdfFile() {
        return pdfFile;
    }

    public void fromAssets(@NonNull CharSequence path) {
        try {
            AssetFileDescriptor afd = getContext().getAssets().openFd(path.toString());
            FileDescriptor descriptor = afd.getFileDescriptor();
            afd.close();
            pdfFile = new PdfFile(ParcelFileDescriptor.dup(descriptor), new PdfReader(getContext().getAssets().open(path.toString())));
        } catch (IOException e) {
            Log.e(PdfFile.TAG, e.getMessage());
        }
    }

    public void fromFile(@NonNull File file) {
        try {
            @SuppressLint({"NewApi", "LocalSuppress"})
            PdfReader reader = new PdfReader(Files.newInputStream(file.toPath()));

            ParcelFileDescriptor pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
            pdfFile = new PdfFile(pfd, reader);
        } catch (IOException e) {
            Log.e(PdfFile.TAG, e.getMessage());
        }
    }

    public void fromFile(@NonNull CharSequence path) {
        fromFile(new File(path.toString()));
    }


    static class PdfFile implements nx.peter.app.android_ui.view.pdf.PdfFile {
        public static final String TAG = "PdfView.PdfFile";
        protected List<Page> pages;
        protected ParcelFileDescriptor descriptor;
        protected PdfReader reader;

        public PdfFile(@NonNull ParcelFileDescriptor descriptor, @NonNull PdfReader reader) {
            this.descriptor = descriptor;
            this.reader = reader;
            pages = new ArrayList<>();
            try {
                PdfRenderer renderer = new PdfRenderer(descriptor);
                for (int number = 0; number < renderer.getPageCount(); number++) {
                    PdfRenderer.Page page = renderer.openPage(number);
                    Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                    pages.add(new IPage(page.getIndex() + 1, bitmap));
                    page.close();
                }
                renderer.close();
                Log.i(TAG, "Pdf was rendered Successfully!");
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }

        @Override
        public boolean equals(@NonNull nx.peter.app.android_ui.view.pdf.PdfFile document) {
            return document.getPages().equals(getPages());
        }

        @Override
        public Pages getPages() {
            return new Pages(pages);
        }

        @Override
        public Page getPage(int page) {
            return getPages().getPage(page);
        }

        @Override
        public Meta getMeta() {
            return null;
        }

        @Override
        public void close() {
            try {
                descriptor.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }


        protected static class IPage implements Page {
            public int number;
            public Bitmap bitmap;

            public IPage(int number, Bitmap bitmap) {
                this.number = number;
                this.bitmap = bitmap;
            }

            @Override
            public int getNumber() {
                return number;
            }

            @Override
            public int getWidth() {
                return getBitmap().getWidth();
            }

            @Override
            public int getHeight() {
                return getBitmap().getHeight();
            }

            @Override
            public Bitmap getBitmap() {
                return bitmap;
            }
        }
    }

}
