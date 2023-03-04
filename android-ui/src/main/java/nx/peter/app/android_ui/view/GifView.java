package nx.peter.app.android_ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.util.Padding;
import nx.peter.app.android_ui.view.util.Scale;
import nx.peter.app.android_ui.view.util.Size;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class GifView extends View implements IView<GifView> {
    private Background background;
    private Movie movie;
    private Paint paint;
    private long startTime, currentTime, movieStart = 0, elapsed, lastStartTime;
    private boolean isPlaying, isPaused, isVisible;

    private int movieMovieResourceId = 0, currentAnimationTime = 0;
    protected float movieLeft = 0F, movieTop = 0F, movieScale = 0F;

    protected int movieMeasuredMovieWidth = 0, movieMeasuredMovieHeight = 0;
    private InputStream in;
    private State state;

    private OnStateChangedListener stateListener;
    private OnGifListener listener;

    public GifView(@NonNull Context c) {
        this(c, null);
    }

    public GifView(@NonNull Context c, AttributeSet attrs) {
        super(c, attrs);
        init(attrs);
    }

    protected void init(AttributeSet attrs) {
        // inflate(getContext(), R.layout.view_gif, this);
        // view = findViewById(R.id.gif);
        reset();

        setLayerType(LAYER_TYPE_SOFTWARE, null);

        TypedArray array = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.GifView
        );

        //-1 is default value
        movieMovieResourceId = array.getResourceId(R.styleable.GifView_gif, -1);
        isPaused = array.getBoolean(R.styleable.GifView_paused, false);

        array.recycle();

        if (movieMovieResourceId != -1) {
            movie = Movie.decodeStream(getResources().openRawResource(movieMovieResourceId));
        }
    }

    protected void reset() {
        listener = null;
        stateListener = null;
        state = State.UNPREPARED;
        startTime = -1;
        elapsed = 0;
        currentTime = elapsed;
        lastStartTime = startTime;
        isPlaying = false;
        isPaused = false;
        isVisible = true;
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void setRawRes(int res) {
        setInput(getContext().getResources().openRawResource(res));
    }

    public void setAsset(@NonNull CharSequence path) {
        try {
            setInput(getContext().getAssets().open(path.toString()));
        } catch (Exception e) {
            Log.e("GifView", e.getMessage());
        }
    }

    @Override
    protected void onMeasure(int width, int height) {
        if (movie != null) {
            int movieWidth = movie.width();
            int movieHeight = movie.height();

            // Calculate horizontal scaling
            float scaleH = 1f;
            int measureModeWidth = MeasureSpec.getMode(getMeasuredWidth());

            if (measureModeWidth != MeasureSpec.UNSPECIFIED) {
                int maximumWidth = MeasureSpec.getSize(getMeasuredWidth());
                if (movieWidth > maximumWidth)
                    scaleH = movieWidth * 1f / maximumWidth;
            }

            // Calculate vertical scaling
            float scaleW = 1f;
            int measureModeHeight = MeasureSpec.getMode(getMeasuredHeight());

            if (measureModeHeight != MeasureSpec.UNSPECIFIED) {
                int maximumHeight = MeasureSpec.getSize(getMeasuredHeight());
                if (movieHeight > maximumHeight) {
                    scaleW = movieHeight * 1f / maximumHeight;
                }
            }

            // Calculate overall scale
            movieScale = 1f / Math.max(scaleH, scaleW);
            movieMeasuredMovieWidth = (int) (movieWidth * movieScale);
            movieMeasuredMovieHeight = (int) (movieHeight * movieScale);
            setMeasuredDimension(movieMeasuredMovieWidth, movieMeasuredMovieHeight);
        } else
            // No movie set, just set minimum available size.
            setMeasuredDimension(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // Calculate movieLeft / movieTop for drawing in center
        movieLeft = (getViewWidth() - movieMeasuredMovieWidth) / 2f;
        movieTop = (getViewHeight() - movieMeasuredMovieHeight) / 2f;
        isVisible = getVisibility() == VISIBLE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Toast.makeText(getContext(), "Elapsed " + getElapsed(), Toast.LENGTH_SHORT).show();
        if (movie != null) {
            if (!isPaused) {
                updateAnimationTime();
                drawMovieFrame(canvas);
                invalidateView();
            } else
                drawMovieFrame(canvas);
        }
        if (isPlaying && startTime > -1 && movie != null) {
            elapsed = (elapsed + (!isPaused ? (getNow() - lastStartTime) : 0)) % getDuration();
            // currentTime = !pause ? elapsed : currentTime;
            canvas.scale(getViewWidth() / movie.width(), getViewHeight() / movie.height());
            movie.setTime((int) elapsed);
            movie.draw(canvas, 0, 0, paint);
            if (listener != null) listener.onChange(this, getState(), getStartTime(), getElapsed());
            this.invalidate();
        }
    }

    /**
     * Invalidates view only if it is isVisible.
     * <br></br>
     * [.postInvalidateOnAnimation] is used for Jelly Bean and higher.
     */
    @SuppressLint("NewApi")
    private void invalidateView() {
        if (isVisible)
            postInvalidateOnAnimation();
    }

    /**
     * Calculate current animation time
     */
    private void updateAnimationTime() {
        long now = getNow();

        if (movieStart == 0L)
            movieStart = now;

        int duration = movie.duration();

        if (duration == 0)
            duration = DEFAULT_MOVIE_VIEW_DURATION;

        currentAnimationTime = (int) ((now - movieStart) % duration);
    }

    /**
     * Draw current GIF frame
     */
    private void drawMovieFrame(Canvas canvas) {
        movie.setTime(currentAnimationTime);
        canvas.save();
        canvas.scale(movieScale, movieScale);
        movie.draw(canvas, movieLeft / movieScale, movieTop / movieScale);
        canvas.restore();
    }

    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        isVisible = screenState == SCREEN_STATE_ON;
        invalidateView();
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        isVisible = visibility == VISIBLE;
        invalidateView();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        isVisible = visibility == VISIBLE;
        invalidateView();
    }

    public static final int DEFAULT_MOVIE_VIEW_DURATION = 1000;

    public State getState() {
        return state;
    }

    public void setInput(InputStream in) {
        this.in = in;
        movie = in != null ? Movie.decodeStream(in) : null;
        isPlaying = false;
        isPaused = false;
        if (in != null) state = State.STOPPED;
        else state = State.UNPREPARED;

        play();
    }

    public long getNow() {
        return SystemClock.uptimeMillis();
    }

    public long getStartTime() {
        return startTime;
    }

    public int getDuration() {
        return movie != null ? movie.duration() : 0;
    }

    public int getElapsed() {
        return movie != null && isPlaying() ? (int) elapsed : 0;
    }

    public boolean isPlaying() {
        return movie != null && !isPaused;
    }

    public boolean isPaused() {
        return movie != null && isPaused;
    }

    private Movie getMovie() {
        return movie;
    }

    public void play() {
        if (movie != null && !state.equals(State.PLAYING)) {
            this.isPaused = false;

            /*
             * Calculate new movie start time, so that it resumes from the same
             * frame.
             */
            movieStart = SystemClock.uptimeMillis() - currentAnimationTime;

            if (stateListener != null)
                stateListener.onChange(this, getState(), State.PLAYING, getElapsed());
            state = State.PLAYING;

            postInvalidate();
        }
    }

    public void pause() {
        if (movie != null && state.equals(State.PLAYING) && !isPaused) {
            isPaused = true;
            if (stateListener != null)
                stateListener.onChange(this, getState(), State.PAUSED, getElapsed());
            state = State.PAUSED;
            postInvalidate();
        }
    }

    public void stop() {
        if (state.equals(State.PLAYING) || state.equals(State.PAUSED)) {
            isPaused = false;
            startTime = -1;
            if (stateListener != null)
                stateListener.onChange(this, getState(), State.STOPPED, getElapsed());
            state = State.STOPPED;
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        stop();
        super.onDetachedFromWindow();
    }
/*

  public void setUrl(String url) {
    setInput(WebManger.getInputStream(url, "GET"));
  }
*/

    @SuppressLint("NewApi")
    public void setFile(File file) {
        try {
            setInput(new BufferedInputStream(Files.newInputStream(file.toPath()), 32768));
        } catch (IOException e) {
            Log.e("GifView", e.getMessage());
        }
    }

    public enum State {
        PLAYING,
        PAUSED,
        STOPPED,
        UNPREPARED
    }

    public interface OnGifListener {
        void onChange(GifView view, State state, long startTime, long elapsed);
    }

    public interface OnStateChangedListener {
        void onChange(GifView view, State old, State current, long elapsed);
    }

    @Override
    public GifView getView() {
        return this;
    }

    @Override
    public int getViewHeight() {
        return getLayoutParams().height;
    }

    @Override
    public int getViewWidth() {
        return getLayoutParams().width;
    }

    @Override
    public void setPadding(@NonNull Padding padding) {
        setPadding(padding.left, padding.top, padding.right, padding.bottom);
    }

    @Override
    public void setViewWidth(int width) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.width = width;
        setLayoutParams(lp);
    }

    @Override
    public void setViewHeight(int height) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = height;
        setLayoutParams(lp);
    }

    @Override
    public void setSize(@NonNull Size size) {
        setViewWidth(size.width);
        setViewHeight(size.height);
    }

    @Override
    public void setSize(int width, int height) {
        setSize(new Size(width, height));
    }

    @Override
    public Size getSize() {
        return new Size(getViewWidth(), getViewHeight());
    }

    @Override
    public Scale getScale() {
        return new Scale() {
            @Override
            public float getX() {
                return getScaleX();
            }

            @Override
            public float getY() {
                return getScaleY();
            }

            @Override
            public boolean isScaled() {
                return getX() != 1 || getY() != 1;
            }

            @NonNull
            @Override
            public String toString() {
                return "x: " + getX() + ", y: " + getY();
            }
        };
    }

    @Override
    public Padding getPadding() {
        return new Padding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

    @Override
    public void setPadding(int padding) {
        setPadding(padding, padding);
    }

    @Override
    public void setPaddingTop(int padding) {
        setPadding(getPaddingLeft(), padding, getPaddingRight(), getPaddingBottom());
    }

    @Override
    public void setPaddingLeft(int padding) {
        setPadding(padding, getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

    @Override
    public void setPaddingRight(int padding) {
        setPadding(getPaddingLeft(), getPaddingTop(), padding, getPaddingBottom());
    }

    @Override
    public void setPaddingBottom(int padding) {
        setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), padding);
    }

    @Override
    public void setScale(float scale) {
        setScale(scale, scale);
    }

    @Override
    public void setScaleX(float scaleX) {
        setSize((int) (scaleX * getViewWidth()), getViewHeight());
    }

    @Override
    public void setScaleY(float scaleY) {
        setSize(getViewWidth(), (int) (scaleY * getViewHeight()));
    }

    @Override
    public void setScale(float x, float y) {
        setSize((int) (x * getViewWidth()), (int) (y * getViewHeight()));
    }

    @Override
    public void setPadding(int horizontal, int vertical) {
        setPadding(horizontal, vertical, horizontal, vertical);
    }

    @Override
    public void setPaddingVertical(int padding) {
        setPadding(getPaddingLeft(), padding, getPaddingEnd(), padding);
    }

    @Override
    public void setPaddingHorizontal(int padding) {
        setPadding(padding, getPaddingTop(), padding, getPaddingBottom());
    }

    @Override
    public void setBackground(@NonNull Background background) {
        this.background = background;
        switch (background) {
            case Black:
                setBackgroundResource(R.drawable.black);
                break;
            case White:
                setBackgroundResource(R.drawable.white);
                break;
            case Grey:
                setBackgroundResource(R.drawable.grey);
                break;
            case Blue:
                setBackgroundResource(R.drawable.blue);
                break;
            case Brown:
                setBackgroundResource(R.drawable.brown);
                break;
            case Cyan:
                setBackgroundResource(R.drawable.cyan);
                break;
            case Lime:
                setBackgroundResource(R.drawable.lime);
                break;
            case Green:
                setBackgroundResource(R.drawable.green);
                break;
            case Yellow:
                setBackgroundResource(R.drawable.yellow);
                break;
            case Red:
                setBackgroundResource(R.drawable.red);
                break;
            case Orange:
                setBackgroundResource(R.drawable.orange);
                break;
            case Purple:
                setBackgroundResource(R.drawable.purple);
                break;
            case Pink:
                setBackgroundResource(R.drawable.pink);
                break;
            default:
                setBackgroundResource(R.drawable.transparent);
        }
    }

    @Override
    public Background getViewBackground() {
        return background;
    }

    protected static class IPrinter implements Printer {
        View view;

        public IPrinter(View view) {
            this.view = view;
        }

        @Override
        public Pages print() {
            return null;
        }
    }

    @Override
    public Printer getPrinter() {
        return new IPrinter(this);
    }
}

