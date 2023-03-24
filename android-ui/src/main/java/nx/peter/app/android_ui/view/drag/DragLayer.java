package nx.peter.app.android_ui.view.drag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import nx.peter.app.android_ui.view.AbsoluteLayout;

/**
 * A ViewGroup that coordinates dragging across its dscendants.
 *
 * <p> This class used DragLayer in the Android Launcher activity as a model.
 * It is a bit different in several respects:
 * (1) It extends MyAbsoluteLayout rather than FrameLayout; (2) it implements DragSource and DropTarget methods
 * that were done in a separate Workspace class in the Launcher.
 */
public class DragLayer<V extends View> extends AbsoluteLayout implements DragSource<V>, DropTarget<V> {
    protected DragController<V> mDragController;


    public DragLayer(Context context) {
        super(context);
    }

    /**
     * Used to create a new DragLayer from XML.
     *
     * @param context The application's context.
     * @param attrs   The attribtues set containing the Workspace's customization values.
     */
    public DragLayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDragController(@NonNull DragController<V> controller) {
        mDragController = controller;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return mDragController.dispatchKeyEvent(event) || super.dispatchKeyEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragController.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mDragController.onTouchEvent(ev);
    }

    @Override
    public boolean dispatchUnhandledMove(View focused, int direction) {
        return mDragController.dispatchUnhandledMove(focused, direction);
    }

    // DragSource interface methods

    /**
     * This method is called to determine if the DragSource has something to drag.
     *
     * @return True if there is something to drag
     */
    public boolean allowDrag() {
        // In this simple demo, any view that you touch can be dragged.
        return true;
    }

    /**
     * onDropCompleted
     */
    public void onDropCompleted(@NonNull View target, boolean success) {
        toast("DragLayer2.onDropCompleted: " + target.getId() + " Check that the view moved.");
    }


    // DropTarget interface implementation

    /**
     * Handle an object being dropped on the DropTarget.
     * This is the where a dragged view gets repositioned at the end of a drag.
     *
     * @param source   DragSource where the drag started
     * @param x        X coordinate of the drop location
     * @param y        Y coordinate of the drop location
     * @param xOffset  Horizontal offset with the object being dragged where the original
     *                 touch happened
     * @param yOffset  Vertical offset with the object being dragged where the original
     *                 touch happened
     * @param dragView The DragView that's being dragged around on screen.
     * @param dragInfo Data associated with the object being dragged
     */
    @Override
    public void onDrop(@NonNull DragSource<V> source, int x, int y, int xOffset, int yOffset,
                       @NonNull DragView<V> dragView, @NonNull Object dragInfo) {
        View v = (View) dragInfo;
        toast("DragLayer2.onDrop accepts view: " + v.getId()
                + "x, y, xO, yO :" + x + ", " + y + ", "
                + xOffset + ", " + yOffset);

        int w = v.getWidth();
        int h = v.getHeight();
        int left = x - xOffset;
        int top = y - yOffset;
        LayoutParams lp = new LayoutParams(w, h, left, top);
        this.updateViewLayout(v, lp);
    }

    @Override
    public void onDragEnter(@NonNull DragSource<V> source, int x, int y, int xOffset, int yOffset,
                            @NonNull DragView<V> dragView, @NonNull Object dragInfo) {

    }

    @Override
    public void onDragOver(@NonNull DragSource<V> source, int x, int y, int xOffset, int yOffset,
                           @NonNull DragView<V> dragView, @NonNull Object dragInfo) {
    }

    @Override
    public void onDragExit(@NonNull DragSource<V> source, int x, int y, int xOffset, int yOffset,
                           @NonNull DragView<V> dragView, @NonNull Object dragInfo) {
    }

    /**
     * Check if a drop action can occur at, or near, the requested location.
     * This may be called repeatedly during a drag, so any calls should return
     * quickly.
     *
     * @param source   DragSource where the drag started
     * @param x        X coordinate of the drop location
     * @param y        Y coordinate of the drop location
     * @param xOffset  Horizontal offset with the object being dragged where the
     *                 original touch happened
     * @param yOffset  Vertical offset with the object being dragged where the
     *                 original touch happened
     * @param dragView The DragView that's being dragged around on screen.
     * @param dragInfo Data associated with the object being dragged
     * @return True if the drop will be accepted, false otherwise.
     */
    @Override
    public boolean acceptDrop(@NonNull DragSource<V> source, int x, int y, int xOffset, int yOffset,
                              @NonNull DragView<V> dragView, @NonNull Object dragInfo) {
        return true;
    }

    /**
     * Estimate the surface area where this object would land if dropped at the
     * given location.
     *
     * @param source   DragSource where the drag started
     * @param x        X coordinate of the drop location
     * @param y        Y coordinate of the drop location
     * @param xOffset  Horizontal offset with the object being dragged where the
     *                 original touch happened
     * @param yOffset  Vertical offset with the object being dragged where the
     *                 original touch happened
     * @param dragView The DragView that's being dragged around on screen.
     * @param dragInfo Data associated with the object being dragged
     * @param recycle  {@link Rect} object to be possibly recycled.
     * @return Estimated area that would be occupied if object was dropped at
     * the given location. Should return null if no estimate is found,
     * or if this target doesn't provide estimations.
     */
    public Rect estimateDropLocation(DragSource<V> source, int x, int y, int xOffset, int yOffset, DragView<V> dragView, Object dragInfo, Rect recycle) {
        return new Rect(x, y, x + xOffset, y + yOffset);
    }

    // More methods

    /**
     * Show a string on the screen via Toast.
     *
     * @param msg String
     */
    public void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    } // end toast

} // end class
