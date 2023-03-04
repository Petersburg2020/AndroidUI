package nx.peter.app.android_ui.view.drag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.*;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import nx.peter.app.android_ui.view.Content;

public class DragView<D extends View> extends Content<DragView, D> {
    // Number of pixels to add to the dragged item for scaling.  Should be even for pixel alignment.
    private static final int DRAG_SCALE = 40;   // In Launcher, value is 40

    private final Bitmap mBitmap;
    private Paint mPaint;
    private int mRegistrationX, mRegistrationY, registrationX, registrationY;

    private float mScale, mAnimationScale = 1.0f;

    private WindowManager.LayoutParams mLayoutParams;
    private final WindowManager mWindowManager;
    protected float scaleFactor;

    /**
     * Construct the drag view.
     * <p>
     * The registration point is the point inside our view that the touch events should
     * be centered upon.
     *
     * @param context       A context
     * @param bitmap        The view that we're dragging around.  We scale it up when we draw it.
     * @param registrationX The x coordinate of the registration point.
     * @param registrationY The y coordinate of the registration point.
     */
    public DragView(Context context, Bitmap bitmap, int registrationX, int registrationY,
                    int left, int top, int width, int height) {
        super(context);
        // mWindowManager = WindowManagerImpl.getDefault();
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        this.registrationX = registrationX;
        this.registrationY = registrationY;

        scaleFactor = width;
        Matrix scale = new Matrix();
        scaleFactor = mScale = (scaleFactor + DRAG_SCALE) / scaleFactor;
        scale.setScale(scaleFactor, scaleFactor);
        mBitmap = Bitmap.createBitmap(bitmap, left, top, width, height, scale, true);
        setupShadow();
    }

    protected void setupShadow() {
        // The point in our scaled bitmap that the touch events are located
        mRegistrationX = registrationX + (DRAG_SCALE / 2);
        mRegistrationY = registrationY + (DRAG_SCALE / 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mBitmap.getWidth(), mBitmap.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // for debugging
        @SuppressLint("DrawAllocation") Paint p = new Paint();
        p.setStyle(Paint.Style.FILL);
        p.setColor(0x88dd0011);
        canvas.drawRect(0, 0, getWidth(), getHeight(), p);

        float scale = mAnimationScale;
        if (scale < 0.999f) { // allow for some float error
            float width = mBitmap.getWidth();
            float offset = (width - (width * scale)) / 2;
            canvas.translate(offset, offset);
            canvas.scale(scale, scale);
        }
        canvas.drawBitmap(mBitmap, 0.0f, 0.0f, mPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mBitmap.recycle();
    }

    public void setPaint(Paint paint) {
        mPaint = paint;
        invalidate();
    }

    /**
     * Create a window containing this view and show it.
     *
     * @param windowToken obtained from v.getWindowToken() from one of your views
     * @param touchX      the x coordinate the user touched in screen coordinates
     * @param touchY      the y coordinate the user touched in screen coordinates
     */
    @SuppressLint("RtlHardcoded")
    public void show(IBinder windowToken, int touchX, int touchY) {
        WindowManager.LayoutParams lp;
        int pixelFormat;

        pixelFormat = PixelFormat.TRANSLUCENT;

        lp = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                touchX - mRegistrationX, touchY - mRegistrationY,
                WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                /*| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM*/,
                pixelFormat);
//        lp.token = mStatusBarView.getWindowToken();
        lp.gravity = Gravity.LEFT | Gravity.TOP;
        lp.token = windowToken;
        lp.setTitle("DragView");
        mLayoutParams = lp;

        mWindowManager.addView(this, lp);
    }

    /**
     * Move the window containing this view.
     *
     * @param touchX the x coordinate the user touched in screen coordinates
     * @param touchY the y coordinate the user touched in screen coordinates
     */
    public void move(int touchX, int touchY) {
        // This is what was done in the Launcher code.
        WindowManager.LayoutParams lp = mLayoutParams;
        lp.x = touchX - mRegistrationX;
        lp.y = touchY - mRegistrationY;
        mWindowManager.updateViewLayout(this, lp);
    }

    public void remove() {
        mWindowManager.removeView(this);
    }

}

