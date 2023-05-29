package nx.peter.app.android_ui.view;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import androidx.annotation.AnyThread;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.exifinterface.media.ExifInterface;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.pdf.subscale.*;
import nx.peter.app.android_ui.view.pdf.subscale.decoder.CompatDecoderFactory;
import nx.peter.app.android_ui.view.pdf.subscale.decoder.DecoderFactory;
import nx.peter.app.android_ui.view.pdf.subscale.decoder.ImageDecoder;
import nx.peter.app.android_ui.view.pdf.subscale.decoder.ImageRegionDecoder;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class SubsamplingScaleImageView extends View {
    private Bitmap bitmap;
    private boolean bitmapIsPreview;
    private boolean bitmapIsCached;
    private Uri uri;
    private int fullImageSampleSize;
    private Map<Integer, List<Tile>> tileMap;
    private int orientation = Constants.ORIENTATION_0;
    private float maxScale = 2F;
    private float minScale = minScale();
    private int minimumTileDpi = -1;
    private int panLimit = Constants.PAN_LIMIT_INSIDE;
    private int minimumScaleType = Constants.SCALE_TYPE_CENTER_INSIDE;
    public static final int TILE_SIZE_AUTO = Integer.MAX_VALUE;
    private int maxTileWidth = TILE_SIZE_AUTO;
    private int maxTileHeight = TILE_SIZE_AUTO;
    private Executor executor = AsyncTask.THREAD_POOL_EXECUTOR;
    private boolean eagerLoadingEnabled = true;
    private boolean panEnabled = true;
    private boolean zoomEnabled = true;
    private boolean quickScaleEnabled = true;
    private float doubleTapZoomScale = 1F;
    private int doubleTapZoomStyle = Constants.ZOOM_FOCUS_FIXED;
    private int doubleTapZoomDuration = 500;
    private float scale;
    private float scaleStart;
    private PointF vTranslate;
    private PointF vTranslateStart;
    private PointF vTranslateBefore;
    private Float pendingScale;
    private PointF sPendingCenter;
    private PointF sRequestedCenter;
    private int sWidth;
    private int sHeight;
    private int sOrientation;
    private Rect sRegion;
    private Rect pRegion;
    private boolean isZooming;
    private boolean isPanning;
    private boolean isQuickScaling;
    private int maxTouchCount;
    private GestureDetector detector;
    private GestureDetector singleDetector;
    private ImageRegionDecoder decoder;
    private final ReadWriteLock decoderLock = new ReentrantReadWriteLock(true);
    private DecoderFactory<? extends ImageDecoder> bitmapDecoderFactory = new CompatDecoderFactory<ImageDecoder>(SkiaImageDecoder.class);
    private DecoderFactory<? extends ImageRegionDecoder> regionDecoderFactory = new CompatDecoderFactory<ImageRegionDecoder>(SkiaImageRegionDecoder.class);
    private PointF vCenterStart;
    private float vDistStart;
    private final float quickScaleThreshold;
    private float quickScaleLastDistance;
    private boolean quickScaleMoved;
    private PointF quickScaleVLastPoint;
    private PointF quickScaleSCenter;
    private PointF quickScaleVStart;
    private Anim anim;
    private boolean readySent;
    private boolean imageLoadedSent;
    private OnImageEventListener onImageEventListener;
    private OnStateChangedListener onStateChangedListener;
    private OnLongClickListener onLongClickListener;
    private final Handler handler;
    public static final int MESSAGE_LONG_CLICK = 1;
    private Paint bitmapPaint;
    private Paint tileBgPaint;
    private ScaleAndTranslate satTemp;
    private Matrix matrix;
    private RectF sRect;
    private final float[] srcArray = new float[8];
    private final float[] dstArray = new float[8];
    private final float density;
    private static Bitmap.Config preferredBitmapConfig;

    public SubsamplingScaleImageView(Context context, AttributeSet attr) {
        super(context, attr);
        density = getResources().getDisplayMetrics().density;
        setMinimumDpi(160);
        setDoubleTapZoomDpi(160);
        setMinimumTileDpi(320);
        setGestureDetector(context);
        this.handler = new Handler(message -> {
            if (message.what == MESSAGE_LONG_CLICK && onLongClickListener != null) {
                maxTouchCount = 0;
                SubsamplingScaleImageView.super.setOnLongClickListener(onLongClickListener);
                performLongClick();
                SubsamplingScaleImageView.super.setOnLongClickListener(null);
            }
            return true;
        });

        if (attr != null) {
            TypedArray a = getContext().obtainStyledAttributes(attr, R.styleable.SubsamplingScaleImageView);
            try {
                String assetName = a.getString(R.styleable.SubsamplingScaleImageView_assetName);
                if (assetName != null && assetName.length() > 0) setImage(ImageSource.asset(assetName).tilingEnabled());
            } catch (Exception ignored) {
            }
            try {
                int resId = a.getResourceId(R.styleable.SubsamplingScaleImageView_src, 0);
                if (resId > 0) setImage(ImageSource.resource(resId).tilingEnabled());
            } catch (Exception ignored) {
            }

            try {
                setPanEnabled(a.getBoolean(R.styleable.SubsamplingScaleImageView_panEnabled, true));
            } catch (Exception ignored) {
            }

            try {
                setZoomEnabled(a.getBoolean(R.styleable.SubsamplingScaleImageView_zoomEnabled, true));
            } catch (Exception ignored) {
            }

            try {
                setQuickScaleEnabled(a.getBoolean(R.styleable.SubsamplingScaleImageView_quickScaleEnabled, true));
            } catch (Exception ignored) {
            }

            try {
                setTileBackgroundColor(a.getColor(R.styleable.SubsamplingScaleImageView_tileBackgroundColor, Color.TRANSPARENT));
            } catch (Exception ignored) {
            }

            a.recycle();
            a.close();
        }

        quickScaleThreshold = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getContext().getResources().getDisplayMetrics());
    }

    public SubsamplingScaleImageView(Context context) {
        this(context, null);
    }

    public static Bitmap.Config getPreferredBitmapConfig() {
        return preferredBitmapConfig;
    }

    public static void setPreferredBitmapConfig(Bitmap.Config preferredBitmapConfig) {
        SubsamplingScaleImageView.preferredBitmapConfig = preferredBitmapConfig;
    }

    public final void setOrientation(int orientation) {
        if (!Constants.VALID_ORIENTATIONS.contains(orientation))
            throw new IllegalArgumentException("Invalid orientation: " + orientation);
        this.orientation = orientation;
        reset(false);
        invalidate();
        requestLayout();
    }

    public final void setImage(@NonNull ImageSource imageSource) {
        setImage(imageSource, null, null);
    }

    public final void setImage(@NonNull ImageSource imageSource, ImageViewState state) {
        setImage(imageSource, null, state);
    }

    public final void setImage(@NonNull ImageSource imageSource, ImageSource previewSource) {
        setImage(imageSource, previewSource, null);
    }

    public final void setImage(@NonNull ImageSource imageSource, ImageSource previewSource, ImageViewState state) {
        reset(true);
        if (state != null) restoreState(state);

        if (previewSource != null) {
            if (imageSource.getBitmap() != null)
                throw new IllegalArgumentException("Preview image cannot be used when a bitmap is provided for the main image");
            if (imageSource.getSWidth() <= 0 || imageSource.getSHeight() <= 0)
                throw new IllegalArgumentException("Preview image cannot be used unless dimensions are provided for the main image");
            this.sWidth = imageSource.getSWidth();
            this.sHeight = imageSource.getSHeight();
            this.pRegion = previewSource.getSRegion();
            if (previewSource.getBitmap() != null) {
                this.bitmapIsCached = previewSource.isCached();
                onPreviewLoaded(previewSource.getBitmap());
            } else {
                Uri uri = previewSource.getUri();
                if (uri == null && previewSource.getResource() != null)
                    uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getContext().getPackageName() + "/" + previewSource.getResource());
                BitmapLoadTask task = new BitmapLoadTask(this, getContext(), bitmapDecoderFactory, uri, true);
                execute(task);
            }
        }

        if (imageSource.getBitmap() != null && imageSource.getSRegion() != null)
            onImageLoaded(Bitmap.createBitmap(imageSource.getBitmap(), imageSource.getSRegion().left, imageSource.getSRegion().top, imageSource.getSRegion().width(), imageSource.getSRegion().height()), Constants.ORIENTATION_0, false);
        else if (imageSource.getBitmap() != null)
            onImageLoaded(imageSource.getBitmap(), Constants.ORIENTATION_0, imageSource.isCached());
        else {
            sRegion = imageSource.getSRegion();
            uri = imageSource.getUri();
            if (uri == null && imageSource.getResource() != null)
                uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getContext().getPackageName() + "/" + imageSource.getResource());
            if (imageSource.getTile() || sRegion != null)
                execute(new TilesInitTask(this, getContext(), regionDecoderFactory, uri));
            else
                execute(new BitmapLoadTask(this, getContext(), bitmapDecoderFactory, uri, false));
        }
    }

    private void reset(boolean newImage) {
        scale = 0f;
        scaleStart = 0f;
        vTranslate = null;
        vTranslateStart = null;
        vTranslateBefore = null;
        pendingScale = 0f;
        sPendingCenter = null;
        sRequestedCenter = null;
        isZooming = false;
        isPanning = false;
        isQuickScaling = false;
        maxTouchCount = 0;
        fullImageSampleSize = 0;
        vCenterStart = null;
        vDistStart = 0;
        quickScaleLastDistance = 0f;
        quickScaleMoved = false;
        quickScaleSCenter = null;
        quickScaleVLastPoint = null;
        quickScaleVStart = null;
        anim = null;
        satTemp = null;
        matrix = null;
        sRect = null;
        if (newImage) {
            uri = null;
            decoderLock.writeLock().lock();
            try {
                if (decoder != null) {
                    decoder.recycle();
                    decoder = null;
                }
            } finally {
                decoderLock.writeLock().unlock();
            }
            if (bitmap != null && !bitmapIsCached)
                bitmap.recycle();
            if (bitmap != null && bitmapIsCached && onImageEventListener != null)
                onImageEventListener.onPreviewReleased();
            sWidth = 0;
            sHeight = 0;
            sOrientation = 0;
            sRegion = null;
            pRegion = null;
            readySent = false;
            imageLoadedSent = false;
            bitmap = null;
            bitmapIsPreview = false;
            bitmapIsCached = false;
        }
        if (tileMap != null) {
            for (Map.Entry<Integer, List<Tile>> tileMapEntry : tileMap.entrySet())
                for (Tile tile : tileMapEntry.getValue()) {
                    tile.visible = false;
                    if (tile.bitmap != null) {
                        tile.bitmap.recycle();
                        tile.bitmap = null;
                    }
                }
            tileMap = null;
        }
        setGestureDetector(getContext());
    }

    private void setGestureDetector(final Context context) {
        this.detector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (panEnabled && readySent && vTranslate != null && e1 != null && e2 != null && (Math.abs(e1.getX() - e2.getX()) > 50 || Math.abs(e1.getY() - e2.getY()) > 50) && (Math.abs(velocityX) > 500 || Math.abs(velocityY) > 500) && !isZooming) {
                    PointF vTranslateEnd = new PointF(vTranslate.x + (velocityX * 0.25f), vTranslate.y + (velocityY * 0.25f));
                    float sCenterXEnd = ((getWidth() / 2f) - vTranslateEnd.x) / scale;
                    float sCenterYEnd = ((getHeight() / 2f) - vTranslateEnd.y) / scale;
                    new AnimationBuilder(new PointF(sCenterXEnd, sCenterYEnd)).withEasing(Constants.EASE_OUT_QUAD).withPanLimited(false).withOrigin(Constants.ORIGIN_FLING).start();
                    return true;
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                performClick();
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (zoomEnabled && readySent && vTranslate != null) {
                    setGestureDetector(context);
                    if (quickScaleEnabled) {
                        vCenterStart = new PointF(e.getX(), e.getY());
                        vTranslateStart = new PointF(vTranslate.x, vTranslate.y);
                        scaleStart = scale;
                        isQuickScaling = true;
                        isZooming = true;
                        quickScaleLastDistance = -1F;
                        quickScaleSCenter = viewToSourceCoord(vCenterStart);
                        quickScaleVStart = new PointF(e.getX(), e.getY());
                        quickScaleVLastPoint = new PointF(quickScaleSCenter.x, quickScaleSCenter.y);
                        quickScaleMoved = false;

                        return false;
                    } else {
                        doubleTapZoom(viewToSourceCoord(new PointF(e.getX(), e.getY())), new PointF(e.getX(), e.getY()));
                        return true;
                    }
                }
                return super.onDoubleTapEvent(e);
            }
        });

        singleDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                performClick();
                return true;
            }
        });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        PointF sCenter = getCenter();
        if (readySent && sCenter != null) {
            this.anim = null;
            this.pendingScale = scale;
            this.sPendingCenter = sCenter;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        boolean resizeWidth = widthSpecMode != MeasureSpec.EXACTLY;
        boolean resizeHeight = heightSpecMode != MeasureSpec.EXACTLY;
        int width = parentWidth;
        int height = parentHeight;
        if (sWidth > 0 && sHeight > 0) {
            if (resizeWidth && resizeHeight) {
                width = sWidth();
                height = sHeight();
            } else if (resizeHeight)
                height = (int) ((((double) sHeight() / (double) sWidth()) * width));
            else if (resizeWidth)
                width = (int) ((((double) sWidth() / (double) sHeight()) * height));
        }
        width = Math.max(width, getSuggestedMinimumWidth());
        height = Math.max(height, getSuggestedMinimumHeight());
        setMeasuredDimension(width, height);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {

        if (anim != null && !anim.interruptible) {
            requestDisallowInterceptTouchEvent(true);
            return true;
        } else {
            if (anim != null && anim.listener != null)
                try {
                    anim.listener.onInterruptedByUser();
                } catch (Exception ignored) {
                }
            anim = null;
        }

        if (vTranslate == null) {
            if (singleDetector != null) {
                singleDetector.onTouchEvent(event);
            }
            return true;
        }

        if (!isQuickScaling && (detector == null || detector.onTouchEvent(event))) {
            isZooming = false;
            isPanning = false;
            maxTouchCount = 0;
            return true;
        }

        if (vTranslateStart == null) {
            vTranslateStart = new PointF(0, 0);
        }
        if (vTranslateBefore == null) {
            vTranslateBefore = new PointF(0, 0);
        }
        if (vCenterStart == null) {
            vCenterStart = new PointF(0, 0);
        }

        float scaleBefore = scale;
        vTranslateBefore.set(vTranslate);

        boolean handled = onTouchEventInternal(event);
        sendStateChanged(scaleBefore, vTranslateBefore, Constants.ORIGIN_TOUCH);
        return handled || super.onTouchEvent(event);
    }

    @SuppressWarnings("deprecation")
    private boolean onTouchEventInternal(@NonNull MotionEvent event) {
        int touchCount = event.getPointerCount();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_1_DOWN:
            case MotionEvent.ACTION_POINTER_2_DOWN:
                anim = null;
                requestDisallowInterceptTouchEvent(true);
                maxTouchCount = Math.max(maxTouchCount, touchCount);
                if (touchCount >= 2) {
                    if (zoomEnabled) {
                        float distance = distance(event.getX(0), event.getX(1), event.getY(0), event.getY(1));
                        scaleStart = scale;
                        vDistStart = distance;
                        vTranslateStart.set(vTranslate.x, vTranslate.y);
                        vCenterStart.set((event.getX(0) + event.getX(1)) / 2, (event.getY(0) + event.getY(1)) / 2);
                    } else maxTouchCount = 0;

                    handler.removeMessages(MESSAGE_LONG_CLICK);
                } else if (!isQuickScaling) {
                    vTranslateStart.set(vTranslate.x, vTranslate.y);
                    vCenterStart.set(event.getX(), event.getY());

                    handler.sendEmptyMessageDelayed(MESSAGE_LONG_CLICK, 600);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                boolean consumed = false;
                if (maxTouchCount > 0) {
                    if (touchCount >= 2) {
                        float vDistEnd = distance(event.getX(0), event.getX(1), event.getY(0), event.getY(1));
                        float vCenterEndX = (event.getX(0) + event.getX(1)) / 2;
                        float vCenterEndY = (event.getY(0) + event.getY(1)) / 2;

                        if (zoomEnabled && (distance(vCenterStart.x, vCenterEndX, vCenterStart.y, vCenterEndY) > 5 || Math.abs(vDistEnd - vDistStart) > 5 || isPanning)) {
                            isZooming = true;
                            isPanning = true;
                            consumed = true;

                            double previousScale = scale;
                            scale = Math.min(maxScale, (vDistEnd / vDistStart) * scaleStart);

                            if (scale <= minScale()) {
                                vDistStart = vDistEnd;
                                scaleStart = minScale();
                                vCenterStart.set(vCenterEndX, vCenterEndY);
                                vTranslateStart.set(vTranslate);
                            } else if (panEnabled) {
                                float vLeftStart = vCenterStart.x - vTranslateStart.x;
                                float vTopStart = vCenterStart.y - vTranslateStart.y;
                                float vLeftNow = vLeftStart * (scale / scaleStart);
                                float vTopNow = vTopStart * (scale / scaleStart);
                                vTranslate.x = vCenterEndX - vLeftNow;
                                vTranslate.y = vCenterEndY - vTopNow;
                                if ((previousScale * sHeight() < getHeight() && scale * sHeight() >= getHeight()) || (previousScale * sWidth() < getWidth() && scale * sWidth() >= getWidth())) {
                                    fitToBounds(true);
                                    vCenterStart.set(vCenterEndX, vCenterEndY);
                                    vTranslateStart.set(vTranslate);
                                    scaleStart = scale;
                                    vDistStart = vDistEnd;
                                }
                            } else if (sRequestedCenter != null) {
                                vTranslate.x = (getWidth() / 2f) - (scale * sRequestedCenter.x);
                                vTranslate.y = (getHeight() / 2f) - (scale * sRequestedCenter.y);
                            } else {

                                vTranslate.x = (getWidth() / 2f) - (scale * (sWidth() / 2f));
                                vTranslate.y = (getHeight() / 2f) - (scale * (sHeight() / 2f));
                            }

                            fitToBounds(true);
                            refreshRequiredTiles(eagerLoadingEnabled);
                        }
                    } else if (isQuickScaling) {
                        float dist = Math.abs(quickScaleVStart.y - event.getY()) * 2 + quickScaleThreshold;

                        if (quickScaleLastDistance == -1f) quickScaleLastDistance = dist;
                        boolean isUpwards = event.getY() > quickScaleVLastPoint.y;
                        quickScaleVLastPoint.set(0, event.getY());

                        float spanDiff = Math.abs(1 - (dist / quickScaleLastDistance)) * 0.5f;

                        if (spanDiff > 0.03f || quickScaleMoved) {
                            quickScaleMoved = true;

                            float multiplier = 1;
                            if (quickScaleLastDistance > 0) multiplier = isUpwards ? (1 + spanDiff) : (1 - spanDiff);

                            double previousScale = scale;
                            scale = Math.max(minScale(), Math.min(maxScale, scale * multiplier));

                            if (panEnabled) {
                                float vLeftStart = vCenterStart.x - vTranslateStart.x;
                                float vTopStart = vCenterStart.y - vTranslateStart.y;
                                float vLeftNow = vLeftStart * (scale / scaleStart);
                                float vTopNow = vTopStart * (scale / scaleStart);
                                vTranslate.x = vCenterStart.x - vLeftNow;
                                vTranslate.y = vCenterStart.y - vTopNow;
                                if ((previousScale * sHeight() < getHeight() && scale * sHeight() >= getHeight()) || (previousScale * sWidth() < getWidth() && scale * sWidth() >= getWidth())) {
                                    fitToBounds(true);
                                    vCenterStart.set(sourceToViewCoord(quickScaleSCenter));
                                    vTranslateStart.set(vTranslate);
                                    scaleStart = scale;
                                    dist = 0;
                                }
                            } else if (sRequestedCenter != null) {
                                vTranslate.x = (getWidth() / 2f) - (scale * sRequestedCenter.x);
                                vTranslate.y = (getHeight() / 2f) - (scale * sRequestedCenter.y);
                            } else {
                                vTranslate.x = (getWidth() / 2f) - (scale * (sWidth() / 2f));
                                vTranslate.y = (getHeight() / 2f) - (scale * (sHeight() / 2f));
                            }
                        }

                        quickScaleLastDistance = dist;

                        fitToBounds(true);
                        refreshRequiredTiles(eagerLoadingEnabled);

                        consumed = true;
                    } else if (!isZooming) {
                        float dx = Math.abs(event.getX() - vCenterStart.x);
                        float dy = Math.abs(event.getY() - vCenterStart.y);

                        float offset = density * 5;
                        if (dx > offset || dy > offset || isPanning) {
                            consumed = true;
                            vTranslate.x = vTranslateStart.x + (event.getX() - vCenterStart.x);
                            vTranslate.y = vTranslateStart.y + (event.getY() - vCenterStart.y);

                            float lastX = vTranslate.x;
                            float lastY = vTranslate.y;
                            fitToBounds(true);
                            boolean atXEdge = lastX != vTranslate.x;
                            boolean atYEdge = lastY != vTranslate.y;
                            boolean edgeXSwipe = atXEdge && dx > dy && !isPanning;
                            boolean edgeYSwipe = atYEdge && dy > dx && !isPanning;
                            boolean yPan = lastY == vTranslate.y && dy > offset * 3;
                            if (!edgeXSwipe && !edgeYSwipe && (!atXEdge || !atYEdge || yPan || isPanning))
                                isPanning = true;
                            else if (dx > offset || dy > offset) {
                                maxTouchCount = 0;
                                handler.removeMessages(MESSAGE_LONG_CLICK);
                                requestDisallowInterceptTouchEvent(false);
                            }
                            if (!panEnabled) {
                                vTranslate.x = vTranslateStart.x;
                                vTranslate.y = vTranslateStart.y;
                                requestDisallowInterceptTouchEvent(false);
                            }

                            refreshRequiredTiles(eagerLoadingEnabled);
                        }
                    }
                }
                if (consumed) {
                    handler.removeMessages(MESSAGE_LONG_CLICK);
                    invalidate();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_POINTER_2_UP:
                handler.removeMessages(MESSAGE_LONG_CLICK);
                if (isQuickScaling) {
                    isQuickScaling = false;
                    if (!quickScaleMoved) doubleTapZoom(quickScaleSCenter, vCenterStart);
                }
                if (maxTouchCount > 0 && (isZooming || isPanning)) {
                    if (isZooming && touchCount == 2) {
                        isPanning = true;
                        vTranslateStart.set(vTranslate.x, vTranslate.y);
                        if (event.getActionIndex() == 1) vCenterStart.set(event.getX(0), event.getY(0));
                        else vCenterStart.set(event.getX(1), event.getY(1));
                    }
                    if (touchCount < 3) isZooming = false;
                    if (touchCount < 2) {
                        isPanning = false;
                        maxTouchCount = 0;
                    }

                    refreshRequiredTiles(true);
                    return true;
                }
                if (touchCount == 1) {
                    isZooming = false;
                    isPanning = false;
                    maxTouchCount = 0;
                }
                return true;
            default:
                break;
        }
        return false;
    }

    private void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        ViewParent parent = getParent();
        if (parent != null) parent.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    private void doubleTapZoom(PointF sCenter, PointF vFocus) {
        if (!panEnabled) {
            if (sRequestedCenter != null) {
                sCenter.x = sRequestedCenter.x;
                sCenter.y = sRequestedCenter.y;
            } else {
                sCenter.x = sWidth() / 2f;
                sCenter.y = sHeight() / 2f;
            }
        }
        float doubleTapZoomScale = Math.min(maxScale, SubsamplingScaleImageView.this.doubleTapZoomScale);
        boolean zoomIn = (scale <= doubleTapZoomScale * 0.9) || scale == minScale;
        float targetScale = zoomIn ? doubleTapZoomScale : minScale();
        if (doubleTapZoomStyle == Constants.ZOOM_FOCUS_CENTER_IMMEDIATE)
            setScaleAndCenter(targetScale, sCenter);
        else if (doubleTapZoomStyle == Constants.ZOOM_FOCUS_CENTER || !zoomIn || !panEnabled)
            new AnimationBuilder(targetScale, sCenter).withInterruptible(false).withDuration(doubleTapZoomDuration).withOrigin(Constants.ORIGIN_DOUBLE_TAP_ZOOM).start();
        else if (doubleTapZoomStyle == Constants.ZOOM_FOCUS_FIXED)
            new AnimationBuilder(targetScale, sCenter, vFocus).withInterruptible(false).withDuration(doubleTapZoomDuration).withOrigin(Constants.ORIGIN_DOUBLE_TAP_ZOOM).start();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        createPaints();

        if (sWidth == 0 || sHeight == 0 || getWidth() == 0 || getHeight() == 0) return;
        if (tileMap == null && decoder != null) initialiseBaseLayer(getMaxBitmapDimensions(canvas));
        if (!checkReady()) return;

        preDraw();

        if (anim != null && anim.vFocusStart != null) {
            float scaleBefore = scale;
            if (vTranslateBefore == null) vTranslateBefore = new PointF(0, 0);
            vTranslateBefore.set(vTranslate);

            long scaleElapsed = System.currentTimeMillis() - anim.time;
            boolean finished = scaleElapsed > anim.duration;
            scaleElapsed = Math.min(scaleElapsed, anim.duration);
            scale = ease(anim.easing, scaleElapsed, anim.scaleStart, anim.scaleEnd - anim.scaleStart, anim.duration);

            float vFocusNowX = ease(anim.easing, scaleElapsed, anim.vFocusStart.x, anim.vFocusEnd.x - anim.vFocusStart.x, anim.duration);
            float vFocusNowY = ease(anim.easing, scaleElapsed, anim.vFocusStart.y, anim.vFocusEnd.y - anim.vFocusStart.y, anim.duration);

            vTranslate.x -= sourceToViewX(anim.sCenterEnd.x) - vFocusNowX;
            vTranslate.y -= sourceToViewY(anim.sCenterEnd.y) - vFocusNowY;

            fitToBounds(finished || (anim.scaleStart == anim.scaleEnd));
            sendStateChanged(scaleBefore, vTranslateBefore, anim.origin);
            refreshRequiredTiles(finished);
            if (finished) {
                if (anim.listener != null)
                    try {
                        anim.listener.onComplete();
                    } catch (Exception e) {
                        Log.w(Constants.TAG, "Error thrown by animation listener", e);
                    }
                anim = null;
            }
            invalidate();
        }

        if (tileMap != null && isBaseLayerReady()) {
            int sampleSize = Math.min(fullImageSampleSize, calculateInSampleSize(scale));

            boolean hasMissingTiles = false;
            for (Map.Entry<Integer, List<Tile>> tileMapEntry : tileMap.entrySet()) {
                if (tileMapEntry.getKey() == sampleSize)
                    for (Tile tile : tileMapEntry.getValue())
                        if (tile.visible && (tile.loading || tile.bitmap == null)) {
                            hasMissingTiles = true;
                            break;
                        }
                if (hasMissingTiles) break;
            }

            for (Map.Entry<Integer, List<Tile>> tileMapEntry : tileMap.entrySet())
                if (tileMapEntry.getKey() == sampleSize || hasMissingTiles)
                    for (Tile tile : tileMapEntry.getValue()) {
                        sourceToViewRect(tile.sRect, tile.vRect);
                        if (!tile.loading && tile.bitmap != null) {
                            if (tileBgPaint != null)
                                canvas.drawRect(tile.vRect, tileBgPaint);
                            if (matrix == null)
                                matrix = new Matrix();
                            matrix.reset();
                            setMatrixArray(srcArray, 0, 0, tile.bitmap.getWidth(), 0, tile.bitmap.getWidth(), tile.bitmap.getHeight(), 0, tile.bitmap.getHeight());
                            switch (getRequiredRotation()) {
                                case Constants.ORIENTATION_0:
                                    setMatrixArray(dstArray, tile.vRect.left, tile.vRect.top, tile.vRect.right, tile.vRect.top, tile.vRect.right, tile.vRect.bottom, tile.vRect.left, tile.vRect.bottom);
                                    break;
                                case Constants.ORIENTATION_90:
                                    setMatrixArray(dstArray, tile.vRect.right, tile.vRect.top, tile.vRect.right, tile.vRect.bottom, tile.vRect.left, tile.vRect.bottom, tile.vRect.left, tile.vRect.top);
                                    break;
                                case Constants.ORIENTATION_180:
                                    setMatrixArray(dstArray, tile.vRect.right, tile.vRect.bottom, tile.vRect.left, tile.vRect.bottom, tile.vRect.left, tile.vRect.top, tile.vRect.right, tile.vRect.top);
                                    break;
                                case Constants.ORIENTATION_270:
                                    setMatrixArray(dstArray, tile.vRect.left, tile.vRect.bottom, tile.vRect.left, tile.vRect.top, tile.vRect.right, tile.vRect.top, tile.vRect.right, tile.vRect.bottom);
                            }
                            /*if (getRequiredRotation() == Constants.ORIENTATION_0)
                                setMatrixArray(dstArray, tile.vRect.left, tile.vRect.top, tile.vRect.right, tile.vRect.top, tile.vRect.right, tile.vRect.bottom, tile.vRect.left, tile.vRect.bottom);
                            else if (getRequiredRotation() == Constants.ORIENTATION_90)
                                setMatrixArray(dstArray, tile.vRect.right, tile.vRect.top, tile.vRect.right, tile.vRect.bottom, tile.vRect.left, tile.vRect.bottom, tile.vRect.left, tile.vRect.top);
                            else if (getRequiredRotation() == Constants.ORIENTATION_180)
                                setMatrixArray(dstArray, tile.vRect.right, tile.vRect.bottom, tile.vRect.left, tile.vRect.bottom, tile.vRect.left, tile.vRect.top, tile.vRect.right, tile.vRect.top);
                            else if (getRequiredRotation() == Constants.ORIENTATION_270)
                                setMatrixArray(dstArray, tile.vRect.left, tile.vRect.bottom, tile.vRect.left, tile.vRect.top, tile.vRect.right, tile.vRect.top, tile.vRect.right, tile.vRect.bottom);*/
                            matrix.setPolyToPoly(srcArray, 0, dstArray, 0, 4);
                            canvas.drawBitmap(tile.bitmap, matrix, bitmapPaint);
                        }
                    }
        } else if (bitmap != null) {
            float xScale = scale, yScale = scale;
            if (bitmapIsPreview) {
                xScale = scale * ((float) sWidth / bitmap.getWidth());
                yScale = scale * ((float) sHeight / bitmap.getHeight());
            }

            if (matrix == null)
                matrix = new Matrix();
            matrix.reset();
            matrix.postScale(xScale, yScale);
            matrix.postRotate(getRequiredRotation());
            matrix.postTranslate(vTranslate.x, vTranslate.y);

            if (getRequiredRotation() == Constants.ORIENTATION_180)
                matrix.postTranslate(scale * sWidth, scale * sHeight);
            else if (getRequiredRotation() == Constants.ORIENTATION_90)
                matrix.postTranslate(scale * sHeight, 0);
            else if (getRequiredRotation() == Constants.ORIENTATION_270)
                matrix.postTranslate(0, scale * sWidth);

            if (tileBgPaint != null) {
                if (sRect == null) sRect = new RectF();
                sRect.set(0f, 0f, bitmapIsPreview ? bitmap.getWidth() : sWidth, bitmapIsPreview ? bitmap.getHeight() : sHeight);
                matrix.mapRect(sRect);
                canvas.drawRect(sRect, tileBgPaint);
            }
            canvas.drawBitmap(bitmap, matrix, bitmapPaint);
        }
    }

    private void setMatrixArray(float[] array, float f0, float f1, float f2, float f3, float f4, float f5, float f6, float f7) {
        array[0] = f0;
        array[1] = f1;
        array[2] = f2;
        array[3] = f3;
        array[4] = f4;
        array[5] = f5;
        array[6] = f6;
        array[7] = f7;
    }

    private boolean isBaseLayerReady() {
        if (bitmap != null && !bitmapIsPreview) return true;
        if (tileMap != null) {
            boolean baseLayerReady = true;
            for (Map.Entry<Integer, List<Tile>> tileMapEntry : tileMap.entrySet()) {
                if (tileMapEntry.getKey() == fullImageSampleSize) {
                    for (Tile tile : tileMapEntry.getValue())
                        if (tile.loading || tile.bitmap == null) {
                            baseLayerReady = false;
                            break;
                        }
                    if (!baseLayerReady) break;
                }
            }
            return baseLayerReady;
        }
        return false;
    }

    private boolean checkReady() {
        boolean ready = getWidth() > 0 && getHeight() > 0 && sWidth > 0 && sHeight > 0 && (bitmap != null || isBaseLayerReady());
        if (!readySent && ready) {
            preDraw();
            readySent = true;
            onReady();
            if (onImageEventListener != null)
                onImageEventListener.onReady();
        }
        return ready;
    }

    private boolean checkImageLoaded() {
        boolean imageLoaded = isBaseLayerReady();
        if (!imageLoadedSent && imageLoaded) {
            preDraw();
            imageLoadedSent = true;
            onImageLoaded();
            if (onImageEventListener != null)
                onImageEventListener.onImageLoaded();
        }
        return imageLoaded;
    }

    private void createPaints() {
        if (bitmapPaint == null) {
            bitmapPaint = new Paint();
            bitmapPaint.setAntiAlias(true);
            bitmapPaint.setFilterBitmap(true);
            bitmapPaint.setDither(true);
        }
    }

    private synchronized void initialiseBaseLayer(@NonNull Point maxTileDimensions) {
        satTemp = new ScaleAndTranslate(0f, new PointF(0, 0));
        fitToBounds(true, satTemp);

        fullImageSampleSize = calculateInSampleSize(satTemp.scale);
        if (fullImageSampleSize > 1)
            fullImageSampleSize /= 2;

        if (fullImageSampleSize == 1 && sRegion == null && sWidth() < maxTileDimensions.x && sHeight() < maxTileDimensions.y) {
            decoder.recycle();
            decoder = null;
            BitmapLoadTask task = new BitmapLoadTask(this, getContext(), bitmapDecoderFactory, uri, false);
            execute(task);
        } else {
            initialiseTileMap(maxTileDimensions);

            List<Tile> baseGrid = tileMap.get(fullImageSampleSize);
            if (baseGrid != null)
                for (Tile baseTile : baseGrid) {
                    TileLoadTask task = new TileLoadTask(this, decoder, baseTile);
                    execute(task);
                }
            refreshRequiredTiles(true);
        }
    }

    private void refreshRequiredTiles(boolean load) {
        if (decoder == null || tileMap == null)
            return;

        int sampleSize = Math.min(fullImageSampleSize, calculateInSampleSize(scale));

        for (Map.Entry<Integer, List<Tile>> tileMapEntry : tileMap.entrySet()) {
            for (Tile tile : tileMapEntry.getValue()) {
                if (tile.sampleSize < sampleSize || (tile.sampleSize > sampleSize && tile.sampleSize != fullImageSampleSize)) {
                    tile.visible = false;
                    if (tile.bitmap != null) {
                        tile.bitmap.recycle();
                        tile.bitmap = null;
                    }
                }
                if (tile.sampleSize == sampleSize) {
                    if (tileVisible(tile)) {
                        tile.visible = true;
                        if (!tile.loading && tile.bitmap == null && load) {
                            TileLoadTask task = new TileLoadTask(this, decoder, tile);
                            execute(task);
                        }
                    } else if (tile.sampleSize != fullImageSampleSize) {
                        tile.visible = false;
                        if (tile.bitmap != null) {
                            tile.bitmap.recycle();
                            tile.bitmap = null;
                        }
                    }
                } else if (tile.sampleSize == fullImageSampleSize) tile.visible = true;
            }
        }
    }

    private boolean tileVisible(Tile tile) {
        float sVisLeft = viewToSourceX(0), sVisRight = viewToSourceX(getWidth()), sVisTop = viewToSourceY(0), sVisBottom = viewToSourceY(getHeight());
        return !(sVisLeft > tile.sRect.right || tile.sRect.left > sVisRight || sVisTop > tile.sRect.bottom || tile.sRect.top > sVisBottom);
    }

    private void preDraw() {
        if (getWidth() == 0 || getHeight() == 0 || sWidth <= 0 || sHeight <= 0)
            return;

        if (sPendingCenter != null && pendingScale != null) {
            scale = pendingScale;
            if (vTranslate == null)
                vTranslate = new PointF();
            vTranslate.x = (getWidth() / 2f) - (scale * sPendingCenter.x);
            vTranslate.y = (getHeight() / 2f) - (scale * sPendingCenter.y);
            sPendingCenter = null;
            pendingScale = null;
            fitToBounds(true);
            refreshRequiredTiles(true);
        }
        fitToBounds(false);
    }

    private int calculateInSampleSize(float scale) {
        if (minimumTileDpi > 0) {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            float averageDpi = (metrics.xdpi + metrics.ydpi) / 2;
            scale = (minimumTileDpi / averageDpi) * scale;
        }

        int reqWidth = (int) (sWidth() * scale);
        int reqHeight = (int) (sHeight() * scale);

        int inSampleSize = 1;
        if (reqWidth == 0 || reqHeight == 0) {
            return 32;
        }

        if (sHeight() > reqHeight || sWidth() > reqWidth) {
            int heightRatio = Math.round((float) sHeight() / (float) reqHeight);
            int widthRatio = Math.round((float) sWidth() / (float) reqWidth);

            inSampleSize = Math.min(heightRatio, widthRatio);
        }

        int power = 1;
        while (power * 2 < inSampleSize) power = power * 2;

        return power;
    }

    private void fitToBounds(boolean center, ScaleAndTranslate sat) {
        if (panLimit == Constants.PAN_LIMIT_OUTSIDE && isReady())
            center = false;

        PointF vTranslate = sat.vTranslate;
        float scale = limitedScale(sat.scale);
        float scaleWidth = scale * sWidth();
        float scaleHeight = scale * sHeight();

        if (panLimit == Constants.PAN_LIMIT_CENTER && isReady()) {
            vTranslate.x = Math.max(vTranslate.x, getWidth() / 2f - scaleWidth);
            vTranslate.y = Math.max(vTranslate.y, getHeight() / 2f - scaleHeight);
        } else if (center) {
            vTranslate.x = Math.max(vTranslate.x, getWidth() - scaleWidth);
            vTranslate.y = Math.max(vTranslate.y, getHeight() - scaleHeight);
        } else {
            vTranslate.x = Math.max(vTranslate.x, -scaleWidth);
            vTranslate.y = Math.max(vTranslate.y, -scaleHeight);
        }

        float xPaddingRatio = getPaddingLeft() > 0 || getPaddingRight() > 0 ? getPaddingLeft() / (float) (getPaddingLeft() + getPaddingRight()) : 0.5f;
        float yPaddingRatio = getPaddingTop() > 0 || getPaddingBottom() > 0 ? getPaddingTop() / (float) (getPaddingTop() + getPaddingBottom()) : 0.5f;

        float maxTx;
        float maxTy;
        if (panLimit == Constants.PAN_LIMIT_CENTER && isReady()) {
            maxTx = Math.max(0, getWidth() / 2);
            maxTy = Math.max(0, getHeight() / 2);
        } else if (center) {
            maxTx = Math.max(0, (getWidth() - scaleWidth) * xPaddingRatio);
            maxTy = Math.max(0, (getHeight() - scaleHeight) * yPaddingRatio);
        } else {
            maxTx = Math.max(0, getWidth());
            maxTy = Math.max(0, getHeight());
        }

        vTranslate.x = Math.min(vTranslate.x, maxTx);
        vTranslate.y = Math.min(vTranslate.y, maxTy);

        sat.scale = scale;
    }

    private void fitToBounds(boolean center) {
        boolean init = false;
        if (vTranslate == null) {
            init = true;
            vTranslate = new PointF(0, 0);
        }
        if (satTemp == null)
            satTemp = new ScaleAndTranslate(0, new PointF(0, 0));
        satTemp.scale = scale;
        satTemp.vTranslate.set(vTranslate);
        fitToBounds(center, satTemp);
        scale = satTemp.scale;
        vTranslate.set(satTemp.vTranslate);
        if (init && minimumScaleType != Constants.SCALE_TYPE_START)
            vTranslate.set(vTranslateForSCenter(sWidth() / 2f, sHeight() / 2f, scale));
    }

    private void initialiseTileMap(Point maxTileDimensions) {
        this.tileMap = new LinkedHashMap<>();
        int sampleSize = fullImageSampleSize;
        int xTiles = 1;
        int yTiles = 1;
        while (true) {
            int sTileWidth = sWidth() / xTiles;
            int sTileHeight = sHeight() / yTiles;
            int subTileWidth = sTileWidth / sampleSize;
            int subTileHeight = sTileHeight / sampleSize;
            while (subTileWidth + xTiles + 1 > maxTileDimensions.x || (subTileWidth > getWidth() * 1.25 && sampleSize < fullImageSampleSize)) {
                xTiles += 1;
                sTileWidth = sWidth() / xTiles;
                subTileWidth = sTileWidth / sampleSize;
            }
            while (subTileHeight + yTiles + 1 > maxTileDimensions.y || (subTileHeight > getHeight() * 1.25 && sampleSize < fullImageSampleSize)) {
                yTiles += 1;
                sTileHeight = sHeight() / yTiles;
                subTileHeight = sTileHeight / sampleSize;
            }
            List<Tile> tileGrid = new ArrayList<>(xTiles * yTiles);
            for (int x = 0; x < xTiles; x++)
                for (int y = 0; y < yTiles; y++) {
                    Tile tile = new Tile();
                    tile.sampleSize = sampleSize;
                    tile.visible = sampleSize == fullImageSampleSize;
                    tile.sRect = new Rect(x * sTileWidth, y * sTileHeight, x == xTiles - 1 ? sWidth() : (x + 1) * sTileWidth, y == yTiles - 1 ? sHeight() : (y + 1) * sTileHeight);
                    tile.vRect = new Rect(0, 0, 0, 0);
                    tile.fileSRect = new Rect(tile.sRect);
                    tileGrid.add(tile);
                }
            tileMap.put(sampleSize, tileGrid);
            if (sampleSize == 1)
                break;
            sampleSize /= 2;
        }
    }

    private static class TilesInitTask extends AsyncTask<Void, Void, int[]> {
        private final WeakReference<SubsamplingScaleImageView> viewRef;
        private final WeakReference<Context> contextRef;
        private final WeakReference<DecoderFactory<? extends ImageRegionDecoder>> decoderFactoryRef;
        private final Uri source;
        private ImageRegionDecoder decoder;
        private Exception exception;

        TilesInitTask(SubsamplingScaleImageView view, Context context, DecoderFactory<? extends ImageRegionDecoder> decoderFactory, Uri source) {
            this.viewRef = new WeakReference<>(view);
            this.contextRef = new WeakReference<>(context);
            this.decoderFactoryRef = new WeakReference<DecoderFactory<? extends ImageRegionDecoder>>(decoderFactory);
            this.source = source;
        }

        @Override
        protected int[] doInBackground(Void... params) {
            try {
                String sourceUri = source.toString();
                Context context = contextRef.get();
                DecoderFactory<? extends ImageRegionDecoder> decoderFactory = decoderFactoryRef.get();
                SubsamplingScaleImageView view = viewRef.get();
                if (context != null && decoderFactory != null && view != null) {
                    decoder = decoderFactory.make();
                    Point dimensions = decoder.init(context, source);
                    int sWidth = dimensions.x;
                    int sHeight = dimensions.y;
                    int exifOrientation = view.getExifOrientation(context, sourceUri);
                    if (view.sRegion != null) {
                        view.sRegion.left = Math.max(0, view.sRegion.left);
                        view.sRegion.top = Math.max(0, view.sRegion.top);
                        view.sRegion.right = Math.min(sWidth, view.sRegion.right);
                        view.sRegion.bottom = Math.min(sHeight, view.sRegion.bottom);
                        sWidth = view.sRegion.width();
                        sHeight = view.sRegion.height();
                    }
                    return new int[]{sWidth, sHeight, exifOrientation};
                }
            } catch (Exception e) {
                Log.e(Constants.TAG, "Failed to initialise bitmap decoder", e);
                this.exception = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(int[] xyo) {
            final SubsamplingScaleImageView view = viewRef.get();
            if (view != null) {
                if (decoder != null && xyo != null && xyo.length == 3) {
                    view.onTilesInitiated(decoder, xyo[0], xyo[1], xyo[2]);
                } else if (exception != null && view.onImageEventListener != null) {
                    view.onImageEventListener.onImageLoadError(exception);
                }
            }
        }
    }

    private synchronized void onTilesInitiated(ImageRegionDecoder decoder, int sWidth, int sHeight, int sOrientation) {
        if (this.sWidth > 0 && this.sHeight > 0 && (this.sWidth != sWidth || this.sHeight != sHeight)) {
            reset(false);
            if (bitmap != null) {
                if (!bitmapIsCached) {
                    bitmap.recycle();
                }
                bitmap = null;
                if (onImageEventListener != null && bitmapIsCached) {
                    onImageEventListener.onPreviewReleased();
                }
                bitmapIsPreview = false;
                bitmapIsCached = false;
            }
        }
        this.decoder = decoder;
        this.sWidth = sWidth;
        this.sHeight = sHeight;
        this.sOrientation = sOrientation;
        checkReady();
        if (!checkImageLoaded() && maxTileWidth > 0 && maxTileWidth != TILE_SIZE_AUTO && maxTileHeight > 0 && maxTileHeight != TILE_SIZE_AUTO && getWidth() > 0 && getHeight() > 0) {
            initialiseBaseLayer(new Point(maxTileWidth, maxTileHeight));
        }
        invalidate();
        requestLayout();
    }

    private static class TileLoadTask extends AsyncTask<Void, Void, Bitmap> {
        private final WeakReference<SubsamplingScaleImageView> viewRef;
        private final WeakReference<ImageRegionDecoder> decoderRef;
        private final WeakReference<Tile> tileRef;
        private Exception exception;

        TileLoadTask(SubsamplingScaleImageView view, ImageRegionDecoder decoder, Tile tile) {
            this.viewRef = new WeakReference<>(view);
            this.decoderRef = new WeakReference<>(decoder);
            this.tileRef = new WeakReference<>(tile);
            tile.loading = true;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                SubsamplingScaleImageView view = viewRef.get();
                ImageRegionDecoder decoder = decoderRef.get();
                Tile tile = tileRef.get();
                if (decoder != null && tile != null && view != null && decoder.isReady() && tile.visible) {
                    view.decoderLock.readLock().lock();
                    try {
                        if (decoder.isReady()) {

                            view.fileSRect(tile.sRect, tile.fileSRect);
                            if (view.sRegion != null) {
                                tile.fileSRect.offset(view.sRegion.left, view.sRegion.top);
                            }
                            return decoder.decodeRegion(tile.fileSRect, tile.sampleSize);
                        } else {
                            tile.loading = false;
                        }
                    } finally {
                        view.decoderLock.readLock().unlock();
                    }
                } else if (tile != null) {
                    tile.loading = false;
                }
            } catch (Exception e) {
                Log.e(Constants.TAG, "Failed to decode tile", e);
                this.exception = e;
            } catch (OutOfMemoryError e) {
                Log.e(Constants.TAG, "Failed to decode tile - OutOfMemoryError", e);
                this.exception = new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            final SubsamplingScaleImageView subsamplingScaleImageView = viewRef.get();
            final Tile tile = tileRef.get();
            if (subsamplingScaleImageView != null && tile != null) {
                if (bitmap != null) {
                    tile.bitmap = bitmap;
                    tile.loading = false;
                    subsamplingScaleImageView.onTileLoaded();
                } else if (exception != null && subsamplingScaleImageView.onImageEventListener != null) {
                    subsamplingScaleImageView.onImageEventListener.onTileLoadError(exception);
                }
            }
        }
    }

    private synchronized void onTileLoaded() {
        checkReady();
        checkImageLoaded();
        if (isBaseLayerReady() && bitmap != null) {
            if (!bitmapIsCached) {
                bitmap.recycle();
            }
            bitmap = null;
            if (onImageEventListener != null && bitmapIsCached) {
                onImageEventListener.onPreviewReleased();
            }
            bitmapIsPreview = false;
            bitmapIsCached = false;
        }
        invalidate();
    }

    private static class BitmapLoadTask extends AsyncTask<Void, Void, Integer> {
        private final WeakReference<SubsamplingScaleImageView> viewRef;
        private final WeakReference<Context> contextRef;
        private final WeakReference<DecoderFactory<? extends ImageDecoder>> decoderFactoryRef;
        private final Uri source;
        private final boolean preview;
        private Bitmap bitmap;
        private Exception exception;

        BitmapLoadTask(SubsamplingScaleImageView view, Context context, DecoderFactory<? extends ImageDecoder> decoderFactory, Uri source, boolean preview) {
            this.viewRef = new WeakReference<>(view);
            this.contextRef = new WeakReference<>(context);
            this.decoderFactoryRef = new WeakReference<DecoderFactory<? extends ImageDecoder>>(decoderFactory);
            this.source = source;
            this.preview = preview;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            try {
                String sourceUri = source.toString();
                Context context = contextRef.get();
                DecoderFactory<? extends ImageDecoder> decoderFactory = decoderFactoryRef.get();
                SubsamplingScaleImageView view = viewRef.get();
                if (context != null && decoderFactory != null && view != null) {
                    bitmap = decoderFactory.make().decode(context, source);
                    return view.getExifOrientation(context, sourceUri);
                }
            } catch (Exception e) {
                Log.e(Constants.TAG, "Failed to load bitmap", e);
                this.exception = e;
            } catch (OutOfMemoryError e) {
                Log.e(Constants.TAG, "Failed to load bitmap - OutOfMemoryError", e);
                this.exception = new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer orientation) {
            SubsamplingScaleImageView subsamplingScaleImageView = viewRef.get();
            if (subsamplingScaleImageView != null) {
                if (bitmap != null && orientation != null) {
                    if (preview) {
                        subsamplingScaleImageView.onPreviewLoaded(bitmap);
                    } else {
                        subsamplingScaleImageView.onImageLoaded(bitmap, orientation, false);
                    }
                } else if (exception != null && subsamplingScaleImageView.onImageEventListener != null) {
                    if (preview) {
                        subsamplingScaleImageView.onImageEventListener.onPreviewLoadError(exception);
                    } else {
                        subsamplingScaleImageView.onImageEventListener.onImageLoadError(exception);
                    }
                }
            }
        }
    }

    private synchronized void onPreviewLoaded(Bitmap previewBitmap) {
        if (bitmap != null || imageLoadedSent) {
            previewBitmap.recycle();
            return;
        }
        bitmap = pRegion != null ? Bitmap.createBitmap(previewBitmap, pRegion.left, pRegion.top, pRegion.width(), pRegion.height()) : previewBitmap;
        bitmapIsPreview = true;
        if (checkReady()) {
            invalidate();
            requestLayout();
        }
    }

    private synchronized void onImageLoaded(Bitmap bitmap, int sOrientation, boolean bitmapIsCached) {
        if (this.sWidth > 0 && this.sHeight > 0 && (this.sWidth != bitmap.getWidth() || this.sHeight != bitmap.getHeight())) {
            reset(false);
        }
        if (this.bitmap != null && !this.bitmapIsCached) {
            this.bitmap.recycle();
        }

        if (this.bitmap != null && this.bitmapIsCached && onImageEventListener != null) {
            onImageEventListener.onPreviewReleased();
        }

        this.bitmapIsPreview = false;
        this.bitmapIsCached = bitmapIsCached;
        this.bitmap = bitmap;
        this.sWidth = bitmap.getWidth();
        this.sHeight = bitmap.getHeight();
        this.sOrientation = sOrientation;
        boolean ready = checkReady();
        boolean imageLoaded = checkImageLoaded();
        if (ready || imageLoaded) {
            invalidate();
            requestLayout();
        }
    }

    @AnyThread
    private int getExifOrientation(Context context, String sourceUri) {
        int exifOrientation = Constants.ORIENTATION_0;
        if (sourceUri.startsWith(ContentResolver.SCHEME_CONTENT)) {
            Cursor cursor = null;
            try {
                String[] columns = {MediaStore.Images.Media.ORIENTATION};
                cursor = context.getContentResolver().query(Uri.parse(sourceUri), columns, null, null, null);
                if (cursor != null)
                    if (cursor.moveToFirst()) {
                        int orientation = cursor.getInt(0);
                        if (Constants.VALID_ORIENTATIONS.contains(orientation) && orientation != Constants.ORIENTATION_USE_EXIF)
                            exifOrientation = orientation;
                        else
                            Log.w(Constants.TAG, "Unsupported orientation: " + orientation);
                    }
            } catch (Exception e) {
                Log.w(Constants.TAG, "Could not get orientation of image from media store");
            } finally {
                if (cursor != null) cursor.close();
            }
        } else if (sourceUri.startsWith(ImageSource.FILE_SCHEME) && !sourceUri.startsWith(ImageSource.ASSET_SCHEME)) {
            try {
                ExifInterface exifInterface = new ExifInterface(sourceUri.substring(ImageSource.FILE_SCHEME.length() - 1));
                int orientationAttr = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                if (orientationAttr == ExifInterface.ORIENTATION_ROTATE_90)
                    exifOrientation = Constants.ORIENTATION_90;
                else if (orientationAttr == ExifInterface.ORIENTATION_ROTATE_180)
                    exifOrientation = Constants.ORIENTATION_180;
                else if (orientationAttr == ExifInterface.ORIENTATION_ROTATE_270)
                    exifOrientation = Constants.ORIENTATION_270;
                else Log.w(Constants.TAG, "Unsupported EXIF orientation: " + orientationAttr);
            } catch (Exception e) {
                Log.w(Constants.TAG, "Could not get EXIF orientation of image");
            }
        }
        return exifOrientation;
    }

    private void execute(AsyncTask<Void, Void, ?> asyncTask) {
        asyncTask.executeOnExecutor(executor);
    }

    private static class Tile {
        private Rect sRect;
        private int sampleSize;
        private Bitmap bitmap;
        private boolean loading;
        private boolean visible;

        private Rect vRect;
        private Rect fileSRect;
    }

    private static class Anim {
        private float scaleStart;
        private float scaleEnd;
        private PointF sCenterStart;
        private PointF sCenterEnd;
        private PointF sCenterEndRequested;
        private PointF vFocusStart;
        private PointF vFocusEnd;
        private long duration = 500;
        private boolean interruptible = true;
        private int easing = Constants.EASE_IN_OUT_QUAD;
        private int origin = Constants.ORIGIN_ANIM;
        private long time = System.currentTimeMillis();
        private OnAnimationEventListener listener;
    }

    private static class ScaleAndTranslate {
        private ScaleAndTranslate(float scale, PointF vTranslate) {
            this.scale = scale;
            this.vTranslate = vTranslate;
        }

        private float scale;
        private final PointF vTranslate;
    }

    private void restoreState(ImageViewState state) {
        if (state != null && Constants.VALID_ORIENTATIONS.contains(state.getOrientation())) {
            this.orientation = state.getOrientation();
            this.pendingScale = state.getScale();
            this.sPendingCenter = state.getCenter();
            invalidate();
        }
    }

    public void setMaxTileSize(int maxPixels) {
        this.maxTileWidth = maxPixels;
        this.maxTileHeight = maxPixels;
    }

    public void setMaxTileSize(int maxPixelsX, int maxPixelsY) {
        this.maxTileWidth = maxPixelsX;
        this.maxTileHeight = maxPixelsY;
    }

    @NonNull
    private Point getMaxBitmapDimensions(Canvas canvas) {
        return new Point(Math.min(canvas.getMaximumBitmapWidth(), maxTileWidth), Math.min(canvas.getMaximumBitmapHeight(), maxTileHeight));
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private int sWidth() {
        int rotation = getRequiredRotation();
        if (rotation == 90 || rotation == 270) {
            return sHeight;
        } else {
            return sWidth;
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    private int sHeight() {
        int rotation = getRequiredRotation();
        if (rotation == 90 || rotation == 270) {
            return sWidth;
        } else {
            return sHeight;
        }
    }

    @SuppressWarnings("SuspiciousNameCombination")
    @AnyThread
    private void fileSRect(Rect sRect, Rect target) {
        if (getRequiredRotation() == 0) {
            target.set(sRect);
        } else if (getRequiredRotation() == 90) {
            target.set(sRect.top, sHeight - sRect.right, sRect.bottom, sHeight - sRect.left);
        } else if (getRequiredRotation() == 180) {
            target.set(sWidth - sRect.right, sHeight - sRect.bottom, sWidth - sRect.left, sHeight - sRect.top);
        } else {
            target.set(sWidth - sRect.bottom, sRect.left, sWidth - sRect.top, sRect.right);
        }
    }

    @AnyThread
    private int getRequiredRotation() {
        return orientation == Constants.ORIENTATION_USE_EXIF ? sOrientation : orientation;
    }

    private float distance(float x0, float x1, float y0, float y1) {
        return (float) Math.sqrt(Math.pow((x0 - x1), 2) + Math.pow((y0 - y1), 2));
    }

    public void recycle() {
        reset(true);
        bitmapPaint = null;
        tileBgPaint = null;
    }

    private float viewToSourceX(float vx) {
        return vTranslate == null ? Float.NaN : (vx - vTranslate.x) / scale;
    }

    private float viewToSourceY(float vy) {
        return vTranslate == null ? Float.NaN : (vy - vTranslate.y) / scale;
    }

    public void viewToFileRect(Rect vRect, Rect fRect) {
        if (vTranslate == null || !readySent) return;
        fRect.set((int) viewToSourceX(vRect.left), (int) viewToSourceY(vRect.top), (int) viewToSourceX(vRect.right), (int) viewToSourceY(vRect.bottom));
        fileSRect(fRect, fRect);
        fRect.set(Math.max(0, fRect.left), Math.max(0, fRect.top), Math.min(sWidth, fRect.right), Math.min(sHeight, fRect.bottom));
        if (sRegion != null) fRect.offset(sRegion.left, sRegion.top);
    }

    public void visibleFileRect(Rect fRect) {
        if (vTranslate == null || !readySent) return;
        fRect.set(0, 0, getWidth(), getHeight());
        viewToFileRect(fRect, fRect);
    }

    public final PointF viewToSourceCoord(PointF vxy) {
        return viewToSourceCoord(vxy.x, vxy.y, new PointF());
    }

    public final PointF viewToSourceCoord(float vx, float vy) {
        return viewToSourceCoord(vx, vy, new PointF());
    }

    public final PointF viewToSourceCoord(PointF vxy, @NonNull PointF sTarget) {
        return viewToSourceCoord(vxy.x, vxy.y, sTarget);
    }

    public final PointF viewToSourceCoord(float vx, float vy, @NonNull PointF sTarget) {
        if (vTranslate == null) return null;
        sTarget.set(viewToSourceX(vx), viewToSourceY(vy));
        return sTarget;
    }

    private float sourceToViewX(float sx) {
        if (vTranslate == null) return Float.NaN;
        return (sx * scale) + vTranslate.x;
    }

    private float sourceToViewY(float sy) {
        return vTranslate == null ? Float.NaN : (sy * scale) + vTranslate.y;
    }

    public final PointF sourceToViewCoord(PointF sxy) {
        return sourceToViewCoord(sxy.x, sxy.y, new PointF());
    }

    public final PointF sourceToViewCoord(float sx, float sy) {
        return sourceToViewCoord(sx, sy, new PointF());
    }

    @SuppressWarnings("UnusedReturnValue")
    public final PointF sourceToViewCoord(PointF sxy, @NonNull PointF vTarget) {
        return sourceToViewCoord(sxy.x, sxy.y, vTarget);
    }

    public final PointF sourceToViewCoord(float sx, float sy, @NonNull PointF vTarget) {
        if (vTranslate == null) return null;
        vTarget.set(sourceToViewX(sx), sourceToViewY(sy));
        return vTarget;
    }

    private void sourceToViewRect(@NonNull Rect sRect, @NonNull Rect vTarget) {
        vTarget.set((int) sourceToViewX(sRect.left), (int) sourceToViewY(sRect.top), (int) sourceToViewX(sRect.right), (int) sourceToViewY(sRect.bottom));
    }

    @NonNull
    private PointF vTranslateForSCenter(float sCenterX, float sCenterY, float scale) {
        int vxCenter = getPaddingLeft() + (getWidth() - getPaddingRight() - getPaddingLeft()) / 2;
        int vyCenter = getPaddingTop() + (getHeight() - getPaddingBottom() - getPaddingTop()) / 2;
        if (satTemp == null) satTemp = new ScaleAndTranslate(0, new PointF(0, 0));
        satTemp.scale = scale;
        satTemp.vTranslate.set(vxCenter - (sCenterX * scale), vyCenter - (sCenterY * scale));
        fitToBounds(true, satTemp);
        return satTemp.vTranslate;
    }

    @NonNull
    private PointF limitedSCenter(float sCenterX, float sCenterY, float scale, @NonNull PointF sTarget) {
        PointF vTranslate = vTranslateForSCenter(sCenterX, sCenterY, scale);
        int vxCenter = getPaddingLeft() + (getWidth() - getPaddingRight() - getPaddingLeft()) / 2;
        int vyCenter = getPaddingTop() + (getHeight() - getPaddingBottom() - getPaddingTop()) / 2;
        float sx = (vxCenter - vTranslate.x) / scale;
        float sy = (vyCenter - vTranslate.y) / scale;
        sTarget.set(sx, sy);
        return sTarget;
    }

    private float minScale() {
        int vPadding = getPaddingBottom() + getPaddingTop();
        int hPadding = getPaddingLeft() + getPaddingRight();
        return minimumScaleType == Constants.SCALE_TYPE_CENTER_CROP || minimumScaleType == Constants.SCALE_TYPE_START ? Math.max((getWidth() - hPadding) / (float) sWidth(), (getHeight() - vPadding) / (float) sHeight()) :
                minimumScaleType == Constants.SCALE_TYPE_CUSTOM && minScale > 0 ? minScale :
                        Math.min((getWidth() - hPadding) / (float) sWidth(), (getHeight() - vPadding) / (float) sHeight());
    }

    private float limitedScale(float targetScale) {
        targetScale = Math.max(minScale(), targetScale);
        targetScale = Math.min(maxScale, targetScale);
        return targetScale;
    }

    private float ease(int type, long time, float from, float change, long duration) {
        switch (type) {
            case Constants.EASE_IN_OUT_QUAD:
                return easeInOutQuad(time, from, change, duration);
            case Constants.EASE_OUT_QUAD:
                return easeOutQuad(time, from, change, duration);
            default:
                throw new IllegalStateException("Unexpected easing type: " + type);
        }
    }

    private float easeOutQuad(long time, float from, float change, long duration) {
        float progress = (float) time / (float) duration;
        return -change * progress * (progress - 2) + from;
    }

    private float easeInOutQuad(long time, float from, float change, long duration) {
        float timeF = time / (duration / 2f);
        if (timeF < 1) {
            return (change / 2f * timeF * timeF) + from;
        } else {
            timeF--;
            return (-change / 2f) * (timeF * (timeF - 2) - 1) + from;
        }
    }

    private int px(int px) {
        return (int) (density * px);
    }

    public final void setRegionDecoderClass(@NonNull Class<? extends ImageRegionDecoder> regionDecoderClass) {
        this.regionDecoderFactory = new CompatDecoderFactory<>(regionDecoderClass);
    }

    public final void setRegionDecoderFactory(@NonNull DecoderFactory<? extends ImageRegionDecoder> regionDecoderFactory) {
        this.regionDecoderFactory = regionDecoderFactory;
    }

    public final void setBitmapDecoderClass(@NonNull Class<? extends ImageDecoder> bitmapDecoderClass) {
        this.bitmapDecoderFactory = new CompatDecoderFactory<>(bitmapDecoderClass);
    }

    public final void setBitmapDecoderFactory(@NonNull DecoderFactory<? extends ImageDecoder> bitmapDecoderFactory) {
        this.bitmapDecoderFactory = bitmapDecoderFactory;
    }

    public final void getPanRemaining(RectF vTarget) {
        if (!isReady())
            return;
        float scaleWidth = scale * sWidth();
        float scaleHeight = scale * sHeight();

        if (panLimit == Constants.PAN_LIMIT_CENTER) {
            vTarget.top = Math.max(0, -(vTranslate.y - (getHeight() / 2f)));
            vTarget.left = Math.max(0, -(vTranslate.x - (getWidth() / 2f)));
            vTarget.bottom = Math.max(0, vTranslate.y - ((getHeight() / 2f) - scaleHeight));
            vTarget.right = Math.max(0, vTranslate.x - ((getWidth() / 2f) - scaleWidth));
        } else if (panLimit == Constants.PAN_LIMIT_OUTSIDE) {
            vTarget.top = Math.max(0, -(vTranslate.y - getHeight()));
            vTarget.left = Math.max(0, -(vTranslate.x - getWidth()));
            vTarget.bottom = Math.max(0, vTranslate.y + scaleHeight);
            vTarget.right = Math.max(0, vTranslate.x + scaleWidth);
        } else {
            vTarget.top = Math.max(0, -vTranslate.y);
            vTarget.left = Math.max(0, -vTranslate.x);
            vTarget.bottom = Math.max(0, (scaleHeight + vTranslate.y) - getHeight());
            vTarget.right = Math.max(0, (scaleWidth + vTranslate.x) - getWidth());
        }
    }

    public final void setPanLimit(int panLimit) {
        if (!Constants.VALID_PAN_LIMITS.contains(panLimit))
            throw new IllegalArgumentException("Invalid pan limit: " + panLimit);
        this.panLimit = panLimit;
        if (isReady()) {
            fitToBounds(true);
            invalidate();
        }
    }

    public final void setMinimumScaleType(int scaleType) {
        if (!Constants.VALID_SCALE_TYPES.contains(scaleType))
            throw new IllegalArgumentException("Invalid scale type: " + scaleType);
        this.minimumScaleType = scaleType;
        if (isReady()) {
            fitToBounds(true);
            invalidate();
        }
    }

    public final void setMaxScale(float maxScale) {
        this.maxScale = maxScale;
    }

    public final void setMinScale(float minScale) {
        this.minScale = minScale;
    }

    public final void setMinimumDpi(int dpi) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float averageDpi = (metrics.xdpi + metrics.ydpi) / 2;
        setMaxScale(averageDpi / dpi);
    }

    public final void setMaximumDpi(int dpi) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float averageDpi = (metrics.xdpi + metrics.ydpi) / 2;
        setMinScale(averageDpi / dpi);
    }

    public float getMaxScale() {
        return maxScale;
    }

    public final float getMinScale() {
        return minScale();
    }

    public void setMinimumTileDpi(int minimumTileDpi) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float averageDpi = (metrics.xdpi + metrics.ydpi) / 2;
        this.minimumTileDpi = (int) Math.min(averageDpi, minimumTileDpi);
        if (isReady()) {
            reset(false);
            invalidate();
        }
    }

    public final PointF getCenter() {
        int mX = getWidth() / 2;
        int mY = getHeight() / 2;
        return viewToSourceCoord(mX, mY);
    }

    public final float getScale() {
        return scale;
    }

    public final void setScaleAndCenter(float scale, @Nullable PointF sCenter) {
        this.anim = null;
        this.pendingScale = scale;
        this.sPendingCenter = sCenter;
        this.sRequestedCenter = sCenter;
        invalidate();
    }

    public final void resetScaleAndCenter() {
        this.anim = null;
        this.pendingScale = limitedScale(0);
        if (isReady()) this.sPendingCenter = new PointF(sWidth() / 2f, sHeight() / 2f);
        else this.sPendingCenter = new PointF(0, 0);
        invalidate();
    }

    public final boolean isReady() {
        return readySent;
    }

    protected void onReady() {
    }

    public final boolean isImageLoaded() {
        return imageLoadedSent;
    }

    protected void onImageLoaded() {
    }

    public final int getSWidth() {
        return sWidth;
    }

    public final int getSHeight() {
        return sHeight;
    }

    public final int getOrientation() {
        return orientation;
    }

    public final int getAppliedOrientation() {
        return getRequiredRotation();
    }

    public final ImageViewState getState() {
        if (vTranslate != null && sWidth > 0 && sHeight > 0) {

            return new ImageViewState(getScale(), getCenter(), getOrientation());
        }
        return null;
    }

    public final boolean isZoomEnabled() {
        return zoomEnabled;
    }

    public final void setZoomEnabled(boolean zoomEnabled) {
        this.zoomEnabled = zoomEnabled;
    }

    public final boolean isQuickScaleEnabled() {
        return quickScaleEnabled;
    }

    public final void setQuickScaleEnabled(boolean quickScaleEnabled) {
        this.quickScaleEnabled = quickScaleEnabled;
    }

    public final boolean isPanEnabled() {
        return panEnabled;
    }

    public final void setPanEnabled(boolean panEnabled) {
        this.panEnabled = panEnabled;
        if (!panEnabled && vTranslate != null) {
            vTranslate.x = (getWidth() / 2f) - (scale * (sWidth() / 2f));
            vTranslate.y = (getHeight() / 2f) - (scale * (sHeight() / 2f));
            if (isReady()) {
                refreshRequiredTiles(true);
                invalidate();
            }
        }
    }

    public final void setTileBackgroundColor(int tileBgColor) {
        if (Color.alpha(tileBgColor) == 0) {
            tileBgPaint = null;
        } else {
            tileBgPaint = new Paint();
            tileBgPaint.setStyle(Style.FILL);
            tileBgPaint.setColor(tileBgColor);
        }
        invalidate();
    }

    public final void setDoubleTapZoomScale(float doubleTapZoomScale) {
        this.doubleTapZoomScale = doubleTapZoomScale;
    }

    public final void setDoubleTapZoomDpi(int dpi) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float averageDpi = (metrics.xdpi + metrics.ydpi) / 2;
        setDoubleTapZoomScale(averageDpi / dpi);
    }

    public final void setDoubleTapZoomStyle(int doubleTapZoomStyle) {
        if (!Constants.VALID_ZOOM_STYLES.contains(doubleTapZoomStyle))
            throw new IllegalArgumentException("Invalid zoom style: " + doubleTapZoomStyle);
        this.doubleTapZoomStyle = doubleTapZoomStyle;
    }

    public final void setDoubleTapZoomDuration(int durationMs) {
        this.doubleTapZoomDuration = Math.max(0, durationMs);
    }

    public void setExecutor(@NonNull Executor executor) {
        this.executor = executor;
    }

    public void setEagerLoadingEnabled(boolean eagerLoadingEnabled) {
        this.eagerLoadingEnabled = eagerLoadingEnabled;
    }

    public boolean hasImage() {
        return uri != null || bitmap != null;
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    public void setOnImageEventListener(OnImageEventListener onImageEventListener) {
        this.onImageEventListener = onImageEventListener;
    }

    public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener) {
        this.onStateChangedListener = onStateChangedListener;
    }

    private void sendStateChanged(float oldScale, PointF oldVTranslate, int origin) {
        if (onStateChangedListener != null && scale != oldScale)
            onStateChangedListener.onScaleChanged(scale, origin);
        if (onStateChangedListener != null && !vTranslate.equals(oldVTranslate))
            onStateChangedListener.onCenterChanged(getCenter(), origin);
    }

    public AnimationBuilder animateCenter(PointF sCenter) {
        return !isReady() ? null : new AnimationBuilder(sCenter);
    }

    public AnimationBuilder animateScale(float scale) {
        return !isReady() ? null : new AnimationBuilder(scale);
    }

    public AnimationBuilder animateScaleAndCenter(float scale, PointF sCenter) {
        return !isReady() ? null : new AnimationBuilder(scale, sCenter);
    }

    public final class AnimationBuilder {
        private final float targetScale;
        private final PointF targetSCenter;
        private final PointF vFocus;
        private long duration = 500;
        private int easing = Constants.EASE_IN_OUT_QUAD;
        private int origin = Constants.ORIGIN_ANIM;
        private boolean interruptible = true;
        private boolean panLimited = true;
        private OnAnimationEventListener listener;

        private AnimationBuilder(PointF sCenter) {
            this.targetScale = scale;
            this.targetSCenter = sCenter;
            this.vFocus = null;
        }

        private AnimationBuilder(float scale) {
            this.targetScale = scale;
            this.targetSCenter = getCenter();
            this.vFocus = null;
        }

        private AnimationBuilder(float scale, PointF sCenter) {
            this.targetScale = scale;
            this.targetSCenter = sCenter;
            this.vFocus = null;
        }

        private AnimationBuilder(float scale, PointF sCenter, PointF vFocus) {
            this.targetScale = scale;
            this.targetSCenter = sCenter;
            this.vFocus = vFocus;
        }

        @NonNull
        public AnimationBuilder withDuration(long duration) {
            this.duration = duration;
            return this;
        }

        @NonNull
        public AnimationBuilder withInterruptible(boolean interruptible) {
            this.interruptible = interruptible;
            return this;
        }

        @NonNull
        public AnimationBuilder withEasing(int easing) {
            if (!Constants.VALID_EASING_STYLES.contains(easing))
                throw new IllegalArgumentException("Unknown easing type: " + easing);
            this.easing = easing;
            return this;
        }

        @NonNull
        public AnimationBuilder withOnAnimationEventListener(OnAnimationEventListener listener) {
            this.listener = listener;
            return this;
        }

        @NonNull
        private AnimationBuilder withPanLimited(boolean panLimited) {
            this.panLimited = panLimited;
            return this;
        }

        @NonNull
        private AnimationBuilder withOrigin(int origin) {
            this.origin = origin;
            return this;
        }

        public void start() {
            if (anim != null && anim.listener != null)
                try {
                    anim.listener.onInterruptedByNewAnim();
                } catch (Exception e) {
                    Log.w(Constants.TAG, "Error thrown by animation listener", e);
                }

            int vxCenter = getPaddingLeft() + (getWidth() - getPaddingRight() - getPaddingLeft()) / 2;
            int vyCenter = getPaddingTop() + (getHeight() - getPaddingBottom() - getPaddingTop()) / 2;
            float targetScale = limitedScale(this.targetScale);
            PointF targetSCenter = panLimited ? limitedSCenter(this.targetSCenter.x, this.targetSCenter.y, targetScale, new PointF()) : this.targetSCenter;
            anim = new Anim();
            anim.scaleStart = scale;
            anim.scaleEnd = targetScale;
            anim.time = System.currentTimeMillis();
            anim.sCenterEndRequested = targetSCenter;
            anim.sCenterStart = getCenter();
            anim.sCenterEnd = targetSCenter;
            anim.vFocusStart = sourceToViewCoord(targetSCenter);
            anim.vFocusEnd = new PointF(vxCenter, vyCenter);
            anim.duration = duration;
            anim.interruptible = interruptible;
            anim.easing = easing;
            anim.origin = origin;
            anim.time = System.currentTimeMillis();
            anim.listener = listener;

            if (vFocus != null) {
                float vTranslateXEnd = vFocus.x - (targetScale * anim.sCenterStart.x);
                float vTranslateYEnd = vFocus.y - (targetScale * anim.sCenterStart.y);
                ScaleAndTranslate satEnd = new ScaleAndTranslate(targetScale, new PointF(vTranslateXEnd, vTranslateYEnd));

                fitToBounds(true, satEnd);

                anim.vFocusEnd = new PointF(vFocus.x + (satEnd.vTranslate.x - vTranslateXEnd), vFocus.y + (satEnd.vTranslate.y - vTranslateYEnd));
            }
            invalidate();
        }
    }

    public static class Constants {
        public static final String TAG = SubsamplingScaleImageView.class.getSimpleName();

        public static final int ORIENTATION_USE_EXIF = -1;
        public static final int ORIENTATION_0 = 0;
        public static final int ORIENTATION_90 = 90;
        public static final int ORIENTATION_180 = 180;
        public static final int ORIENTATION_270 = 270;

        public static final List<Integer> VALID_ORIENTATIONS = Arrays.asList(ORIENTATION_0, ORIENTATION_90, ORIENTATION_180, ORIENTATION_270, ORIENTATION_USE_EXIF);

        public static final int ZOOM_FOCUS_FIXED = 1;
        public static final int ZOOM_FOCUS_CENTER = 2;
        public static final int ZOOM_FOCUS_CENTER_IMMEDIATE = 3;

        public static final List<Integer> VALID_ZOOM_STYLES = Arrays.asList(ZOOM_FOCUS_FIXED, ZOOM_FOCUS_CENTER, ZOOM_FOCUS_CENTER_IMMEDIATE);

        public static final int EASE_OUT_QUAD = 1;
        public static final int EASE_IN_OUT_QUAD = 2;

        public static final List<Integer> VALID_EASING_STYLES = Arrays.asList(EASE_IN_OUT_QUAD, EASE_OUT_QUAD);

        public static final int PAN_LIMIT_INSIDE = 1;
        public static final int PAN_LIMIT_OUTSIDE = 2;
        public static final int PAN_LIMIT_CENTER = 3;

        public static final List<Integer> VALID_PAN_LIMITS = Arrays.asList(PAN_LIMIT_INSIDE, PAN_LIMIT_OUTSIDE, PAN_LIMIT_CENTER);

        public static final int SCALE_TYPE_CENTER_INSIDE = 1;
        public static final int SCALE_TYPE_CENTER_CROP = 2;
        public static final int SCALE_TYPE_CUSTOM = 3;
        public static final int SCALE_TYPE_START = 4;

        public static final List<Integer> VALID_SCALE_TYPES = Arrays.asList(SCALE_TYPE_CENTER_CROP, SCALE_TYPE_CENTER_INSIDE, SCALE_TYPE_CUSTOM, SCALE_TYPE_START);

        public static final int ORIGIN_ANIM = 1;
        public static final int ORIGIN_TOUCH = 2;
        public static final int ORIGIN_FLING = 3;
        public static final int ORIGIN_DOUBLE_TAP_ZOOM = 4;
    }


    static class SkiaImageDecoder implements ImageDecoder {
        private static final String FILE_PREFIX = "file://";
        private static final String ASSET_PREFIX = FILE_PREFIX + "/android_asset/";
        private static final String RESOURCE_PREFIX = ContentResolver.SCHEME_ANDROID_RESOURCE + "://";

        private final Bitmap.Config bitmapConfig;

        @Keep
        public SkiaImageDecoder() {
            this(null);
        }

        public SkiaImageDecoder(@Nullable Bitmap.Config bitmapConfig) {
            Bitmap.Config globalBitmapConfig = SubsamplingScaleImageView.getPreferredBitmapConfig();
            this.bitmapConfig = bitmapConfig != null ? bitmapConfig : globalBitmapConfig != null ? globalBitmapConfig : Bitmap.Config.RGB_565;
        }

        @Override
        @NonNull
        public Bitmap decode(Context context, @NonNull Uri uri) throws Exception {
            String uriString = uri.toString();
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap;
            options.inPreferredConfig = bitmapConfig;
            if (uriString.startsWith(RESOURCE_PREFIX)) {
                Resources res;
                String packageName = uri.getAuthority();
                if (context.getPackageName().equals(packageName)) {
                    res = context.getResources();
                } else {
                    PackageManager pm = context.getPackageManager();
                    res = pm.getResourcesForApplication(packageName);
                }

                int id = 0;
                List<String> segments = uri.getPathSegments();
                int size = segments.size();
                if (size == 2 && segments.get(0).equals("drawable")) {
                    String resName = segments.get(1);
                    id = res.getIdentifier(resName, "drawable", packageName);
                } else if (size == 1 && TextUtils.isDigitsOnly(segments.get(0))) {
                    try {
                        id = Integer.parseInt(segments.get(0));
                    } catch (NumberFormatException ignored) {
                    }
                }

                bitmap = BitmapFactory.decodeResource(context.getResources(), id, options);
            } else if (uriString.startsWith(ASSET_PREFIX)) {
                String assetName = uriString.substring(ASSET_PREFIX.length());
                bitmap = BitmapFactory.decodeStream(context.getAssets().open(assetName), null, options);
            } else if (uriString.startsWith(FILE_PREFIX)) {
                bitmap = BitmapFactory.decodeFile(uriString.substring(FILE_PREFIX.length()), options);
            } else {
                InputStream inputStream = null;
                try {
                    ContentResolver contentResolver = context.getContentResolver();
                    inputStream = contentResolver.openInputStream(uri);
                    bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                } finally {
                    if (inputStream != null)
                        try {
                            inputStream.close();
                        } catch (Exception e) {
                            /* Ignore */
                        }
                }
            }
            if (bitmap == null)
                throw new RuntimeException("Skia image region decoder returned null bitmap - image format may not be supported");
            return bitmap;
        }
    }


    static class SkiaImageRegionDecoder implements ImageRegionDecoder {
        private BitmapRegionDecoder decoder;
        private final ReadWriteLock decoderLock = new ReentrantReadWriteLock(true);

        private static final String FILE_PREFIX = "file://";
        private static final String ASSET_PREFIX = FILE_PREFIX + "/android_asset/";
        private static final String RESOURCE_PREFIX = ContentResolver.SCHEME_ANDROID_RESOURCE + "://";

        private final Bitmap.Config bitmapConfig;

        @Keep
        public SkiaImageRegionDecoder() {
            this(null);
        }

        public SkiaImageRegionDecoder(@Nullable Bitmap.Config bitmapConfig) {
            Bitmap.Config globalBitmapConfig = SubsamplingScaleImageView.getPreferredBitmapConfig();
            this.bitmapConfig = bitmapConfig != null ? bitmapConfig : globalBitmapConfig != null ? globalBitmapConfig : Bitmap.Config.RGB_565;
        }

        @Override
        @NonNull
        public Point init(Context context, @NonNull Uri uri) throws Exception {
            String uriString = uri.toString();
            if (uriString.startsWith(RESOURCE_PREFIX)) {
                Resources res;
                String packageName = uri.getAuthority();
                if (context.getPackageName().equals(packageName)) {
                    res = context.getResources();
                } else {
                    PackageManager pm = context.getPackageManager();
                    res = pm.getResourcesForApplication(packageName);
                }

                int id = 0;
                List<String> segments = uri.getPathSegments();
                int size = segments.size();
                if (size == 2 && segments.get(0).equals("drawable")) {
                    String resName = segments.get(1);
                    id = res.getIdentifier(resName, "drawable", packageName);
                } else if (size == 1 && TextUtils.isDigitsOnly(segments.get(0))) {
                    try {
                        id = Integer.parseInt(segments.get(0));
                    } catch (NumberFormatException ignored) {
                    }
                }

                decoder = BitmapRegionDecoder.newInstance(context.getResources().openRawResource(id), false);
            } else if (uriString.startsWith(ASSET_PREFIX)) {
                String assetName = uriString.substring(ASSET_PREFIX.length());
                decoder = BitmapRegionDecoder.newInstance(
                        context.getAssets().open(assetName, AssetManager.ACCESS_RANDOM),
                        false);
            } else if (uriString.startsWith(FILE_PREFIX)) {
                decoder = BitmapRegionDecoder.newInstance(
                        uriString.substring(FILE_PREFIX.length()),
                        false);
            } else {
                InputStream inputStream = null;
                try {
                    ContentResolver contentResolver = context.getContentResolver();
                    inputStream = contentResolver.openInputStream(uri);
                    decoder = BitmapRegionDecoder.newInstance(inputStream, false);
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (Exception e) { /* Ignore */ }
                    }
                }
            }
            return new Point(decoder.getWidth(), decoder.getHeight());
        }

        @Override
        @NonNull
        public Bitmap decodeRegion(@NonNull Rect sRect, int sampleSize) {
            getDecodeLock().lock();
            try {
                if (decoder != null && !decoder.isRecycled()) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = sampleSize;
                    options.inPreferredConfig = bitmapConfig;
                    Bitmap bitmap = decoder.decodeRegion(sRect, options);
                    if (bitmap == null)
                        throw new RuntimeException("Skia image decoder returned null bitmap - image format may not be supported");
                    return bitmap;
                } else throw new IllegalStateException("Cannot decode region after decoder has been recycled");
            } finally {
                getDecodeLock().unlock();
            }
        }

        @Override
        public synchronized boolean isReady() {
            return decoder != null && !decoder.isRecycled();
        }

        @Override
        public synchronized void recycle() {
            decoderLock.writeLock().lock();
            try {
                decoder.recycle();
                decoder = null;
            } finally {
                decoderLock.writeLock().unlock();
            }
        }

        private Lock getDecodeLock() {
            return decoderLock.readLock();
        }
    }

}
