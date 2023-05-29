package nx.peter.app.android_ui.view.pdf;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;

import java.util.Iterator;
import java.util.List;

public interface PdfFile {
    boolean equals(PdfFile document);
    Pages getPages();
    Page getPage(int page);
    Meta getMeta();
    void close();


    interface Meta {
        String getTitle();

        String getAuthor();

        String getSubject();

        String getCreator();

        long getCreatedDate();

        boolean equals(@NonNull Meta another);
    }

    interface Page {
        int getNumber();
        int getWidth();
        int getHeight();
        Bitmap getBitmap();
    }

    class Pages implements Iterable<Page> {
        protected final List<Page> pages;

        public Pages(@NonNull List<Page> pages) {
            this.pages = pages;
        }

        /**
         * Returns the size of pages in PDF
         * @return number of pages
         */
        public int size() {
            return pages.size();
        }

        /**
         * Get the page number of the {@link Page} object provided
         * @param page page object
         * @return returns the page number if object exists in pages, else returns zero (0)
         */
        public int pageOf(Page page) {
            return pages.indexOf(page) + 1;
        }

        /**
         * Checks if the provided page object is in this list of pages
         * @param page page object
         * @return true if page is in this pages
         */
        public boolean contains(Page page) {
            return page != null && pages.contains(page);
        }

        /**
         * Gets the Page object of the provided number
         * @param number number of page
         * @return page object at page number
         */
        public Page getPage(int number) {
            return number > 0 && number < size() ? pages.get(number - 1) : null;
        }

        @NonNull
        @Override
        public Iterator<Page> iterator() {
            return pages.iterator();
        }

        public boolean equals(@NonNull Pages pages) {
            return equals(pages.pages);
        }

        public boolean equals(@NonNull List<Page> pages) {
            return this.pages.equals(pages);
        }
    }

}
