package nx.peter.app.android_ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.pdf.adapter.PdfErrorHandler;
import nx.peter.app.android_ui.view.pdf.adapter.PdfScale;
import nx.peter.app.android_ui.view.pdf.subscale.ImageSource;

class PDFPagerAdapter extends BasePDFPagerAdapter {
    private static final float DEFAULT_SCALE = 1f;

    PdfScale scale = new PdfScale();
    View.OnClickListener pageClickListener = new EmptyClickListener();

    public PDFPagerAdapter(Context context, String pdfPath) {
        super(context, pdfPath);
    }

    public PDFPagerAdapter(Context context, String pdfPath, PdfErrorHandler errorHandler) {
        super(context, pdfPath, errorHandler);
    }

    @NonNull
    @Override
    @SuppressWarnings("NewApi")
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v = inflater.inflate(R.layout.view_pdf_page, container, false);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        SubsamplingScaleImageView ssiv = v.findViewById(R.id.subsamplingImageView);

        if (renderer == null || getCount() < position)
            return v;

        PdfRenderer.Page page = getPDFPage(renderer, position);

        Bitmap bitmap = bitmapContainer.get(position);
        ssiv.setImage(ImageSource.bitmap(bitmap));

        ssiv.setOnClickListener(v1 -> pageClickListener.onClick(v1));

        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        page.close();

        container.addView(v, 0);

        return v;
    }

    public static class Builder {
        Context context;
        String pdfPath = "";
        float scale = DEFAULT_SCALE;
        float centerX = 0f, centerY = 0f;
        int offScreenSize = DEFAULT_OFFSCREEN_SIZE;
        float renderQuality = DEFAULT_QUALITY;
        PdfErrorHandler errorHandler = new NullPdfErrorHandler();
        View.OnClickListener pageClickListener = new EmptyClickListener();

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setScale(float scale) {
            this.scale = scale;
            return this;
        }

        public Builder setScale(PdfScale scale) {
            this.scale = scale.getScale();
            this.centerX = scale.getCenterX();
            this.centerY = scale.getCenterY();
            return this;
        }

        public Builder setCenterX(float centerX) {
            this.centerX = centerX;
            return this;
        }

        public Builder setCenterY(float centerY) {
            this.centerY = centerY;
            return this;
        }

        public Builder setRenderQuality(float renderQuality) {
            this.renderQuality = renderQuality;
            return this;
        }

        public Builder setOffScreenSize(int offScreenSize) {
            this.offScreenSize = offScreenSize;
            return this;
        }

        public Builder setPdfPath(String path) {
            this.pdfPath = path;
            return this;
        }

        public Builder setErrorHandler(@NonNull PdfErrorHandler handler) {
            this.errorHandler = handler;
            return this;
        }

        public Builder setOnPageClickListener(View.OnClickListener listener) {
            if (listener != null)
                pageClickListener = listener;
            return this;
        }

        public PDFPagerAdapter create() {
            PDFPagerAdapter adapter = new PDFPagerAdapter(context, pdfPath, errorHandler);
            adapter.scale.setScale(scale);
            adapter.scale.setCenterX(centerX);
            adapter.scale.setCenterY(centerY);
            adapter.offScreenSize = offScreenSize;
            adapter.renderQuality = renderQuality;
            adapter.pageClickListener = pageClickListener;
            return adapter;
        }
    }

    static class NullPdfErrorHandler implements PdfErrorHandler {
        @Override
        public void onPdfError(Throwable t) {
            /* Empty */
        }
    }

    static class EmptyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            /* Empty */
        }
    }
}
