package nx.peter.app.android_ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;

import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.pdf.adapter.PdfScale;

class PDFViewPagerZoom extends PDFViewPager {
    public PDFViewPagerZoom(Context context, String pdfPath) {
        super(context, pdfPath);
    }

    public PDFViewPagerZoom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressLint("CustomViewStyleable")
    protected void init(AttributeSet attrs) {
        if (isInEditMode()) {
            setBackgroundResource(R.drawable.flaticon_pdf_dummy);
            return;
        }

        if (attrs != null) {
            TypedArray a;

            a = getContext().obtainStyledAttributes(attrs, R.styleable.PDFViewPager);
            String assetFileName = a.getString(R.styleable.PDFViewPager_assetFileName);
            float scale = a.getFloat(R.styleable.PDFViewPager_scale, PdfScale.DEFAULT_SCALE);

            if (assetFileName != null && assetFileName.length() > 0)
                setAdapter(new PDFPagerAdapter.Builder(getContext())
                        .setPdfPath(assetFileName)
                        .setScale(scale)
                        .setOffScreenSize(getOffscreenPageLimit())
                        .create());

            a.recycle();
            a.close();
        }
    }

    /**
     * Bugfix explained in <a href="https://github.com/chrisbanes/PhotoView">github.com/chrisbanes/PhotoView</a>
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
    }
}
