package nx.peter.app.android_ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.NonNull;
import nx.peter.app.android_ui.view.util.Padding;
import nx.peter.app.android_ui.view.util.Scale;
import nx.peter.app.android_ui.view.util.Size;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public interface IView<V extends View> {
    int getViewWidth();

    int getViewHeight();

    Size getSize();

    Scale getScale();

    float getScaleX();

    float getScaleY();

    Padding getPadding();

    int getPaddingTop();

    int getPaddingLeft();

    int getPaddingRight();

    int getPaddingBottom();

    void setScale(float scale);

    void setScaleX(float scaleX);

    void setScaleY(float scaleY);

    void setScale(float x, float y);

    void setViewWidth(int width);

    void setViewHeight(int height);

    void setSize(@NonNull Size size);

    void setSize(int width, int height);

    void setBackground(@NonNull Background background);

    void setPadding(int padding);

    void setPaddingTop(int padding);

    void setPaddingRight(int padding);

    void setPaddingLeft(int padding);

    void setPaddingBottom(int padding);

    void setPadding(@NonNull Padding padding);

    void setPadding(int horizontal, int vertical);

    void setPaddingVertical(int padding);

    void setPaddingHorizontal(int padding);

    void setPadding(int left, int top, int right, int bottom);

    void setPaddingRelative(int top, int start, int bottom, int end);

    V getView();

    Context getContext();

    Drawable getBackground();

    Background getViewBackground();

    Printer getPrinter();


    interface Printer {
        Pages print();
    }

    interface Page {
        int getNumber();

        Bitmap getImage();
    }

    class PageException extends Exception {
        public PageException() {
            super();
        }

        public PageException(String message) {
            super(message);
        }

        public PageException(String message, Throwable cause) {
            super(message, cause);
        }

        public PageException(Throwable cause) {
            super(cause);
        }

    }

    class Pages implements Iterable<Page> {
        protected final List<Page> pages;

        public Pages(List<Page> pages) {
            this.pages = pages != null ? pages : new ArrayList<>();
        }

        public Page get(int number) throws PageException {
            if (number < 1) throw new PageException("Page number[" + number + "] can't be less than 1!");
            else if (number > getPageCount())
                throw new PageException("Page number[" + number + "] can't be greater than " + getPageCount() + "!");
            return pages.get(number - 1);
        }

        public boolean contains(Page page) {
            return pages.contains(page);
        }

        public int getPageCount() {
            return pages.size();
        }

        @NonNull
        @Override
        public Iterator<Page> iterator() {
            return pages.iterator();
        }
    }

    enum Background {
        Black,
        Grey,
        White,
        Blue,
        Brown,
        Pink,
        Red,
        Yellow,
        Green,
        Purple,
        Lime,
        Cyan,
        Orange,
        Transparent
    }

    enum Foreground {
        Black,
        Grey,
        White,
        Blue,
        Brown,
        Pink,
        Red,
        Yellow,
        Green,
        Purple,
        Lime,
        Cyan,
        Orange
    }

}
