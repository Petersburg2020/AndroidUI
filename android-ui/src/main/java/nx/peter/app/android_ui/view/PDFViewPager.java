package nx.peter.app.android_ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.viewpager.widget.ViewPager;

import nx.peter.app.android_ui.R;
// import nx.peter.app.android_ui.view.IView;


class PDFViewPager extends ViewPager { //implements IView<PDFViewPager> {

    public PDFViewPager(Context context, String pdfPath) {
        super(context);
        init(pdfPath);
    }

    public PDFViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    protected void init(String pdfPath) {
        initAdapter(getContext(), pdfPath);
    }

    protected void init(AttributeSet attrs) {
        if (isInEditMode()) {
            setBackgroundResource(R.drawable.flaticon_pdf_dummy);
            return;
        }

        if (attrs != null) {
            TypedArray a;

            a = getContext().obtainStyledAttributes(attrs, R.styleable.PDFViewPager);
            String assetFileName = a.getString(R.styleable.PDFViewPager_assetFileName);

            if (assetFileName != null && assetFileName.length() > 0)
                initAdapter(getContext(), assetFileName);

            a.recycle();
            a.close();
        }
    }

    protected void initAdapter(Context context, String pdfPath) {
        setAdapter(new PDFPagerAdapter.Builder(context)
                .setPdfPath(pdfPath)
                .setOffScreenSize(getOffscreenPageLimit())
                .create());
    }

    /**
     * PDFViewPager uses PhotoView, so this bugfix should be added Issue explained in
     * <a href="https://github.com/chrisbanes/PhotoView">github.com/chrisbanes/PhotoView</a>
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
