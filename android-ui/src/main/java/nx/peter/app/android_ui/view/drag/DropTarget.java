package nx.peter.app.android_ui.view.drag;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;

/**
 * Interface defining an object that can receive a view at the end of a drag operation.
 *
 */
public interface DropTarget<V extends View> {

    /**
     * Handle an object being dropped on the DropTarget
     *
     * @param source DragSource where the drag started
     * @param x X coordinate of the drop location
     * @param y Y coordinate of the drop location
     * @param xOffset Horizontal offset with the object being dragged where the original
     *          touch happened
     * @param yOffset Vertical offset with the object being dragged where the original
     *          touch happened
     * @param dragView The DragView that's being dragged around on screen.
     * @param dragInfo Data associated with the object being dragged
     *
     */
    void onDrop(@NonNull DragSource<V> source, int x, int y, int xOffset, int yOffset,
                @NonNull DragView<V> dragView, @NonNull Object dragInfo);

    void onDragEnter(@NonNull DragSource<V> source, int x, int y, int xOffset, int yOffset,
                     @NonNull DragView<V> dragView, @NonNull Object dragInfo);

    void onDragOver(@NonNull DragSource<V> source, int x, int y, int xOffset, int yOffset,
                    @NonNull DragView<V> dragView, @NonNull Object dragInfo);

    void onDragExit(@NonNull DragSource<V> source, int x, int y, int xOffset, int yOffset,
                    @NonNull DragView<V> dragView, @NonNull Object dragInfo);

    /**
     * Check if a drop action can occur at, or near, the requested location.
     * This may be called repeatedly during a drag, so any calls should return
     * quickly.
     *
     * @param source DragSource where the drag started
     * @param x X coordinate of the drop location
     * @param y Y coordinate of the drop location
     * @param xOffset Horizontal offset with the object being dragged where the
     *            original touch happened
     * @param yOffset Vertical offset with the object being dragged where the
     *            original touch happened
     * @param dragView The DragView that's being dragged around on screen.
     * @param dragInfo Data associated with the object being dragged
     * @return True if the drop will be accepted, false otherwise.
     */
    boolean acceptDrop(@NonNull DragSource<V> source, int x, int y, int xOffset, int yOffset,
                       @NonNull DragView<V> dragView, @NonNull Object dragInfo);

    /**
     * Estimate the surface area where this object would land if dropped at the
     * given location.
     *
     * @param source DragSource where the drag started
     * @param x X coordinate of the drop location
     * @param y Y coordinate of the drop location
     * @param xOffset Horizontal offset with the object being dragged where the
     *            original touch happened
     * @param yOffset Vertical offset with the object being dragged where the
     *            original touch happened
     * @param dragView The DragView that's being dragged around on screen.
     * @param dragInfo Data associated with the object being dragged
     * @param recycle {@link Rect} object to be possibly recycled.
     * @return Estimated area that would be occupied if object was dropped at
     *         the given location. Should return null if no estimate is found,
     *         or if this target doesn't provide estimations.
     */
    Rect estimateDropLocation(DragSource<V> source, int x, int y, int xOffset, int yOffset,
                              DragView<V> dragView, Object dragInfo, Rect recycle);

    // These methods are implemented in Views
    void getHitRect(@NonNull Rect outRect);
    void getLocationOnScreen(int[] loc);
    int getLeft();
    int getTop();
}
