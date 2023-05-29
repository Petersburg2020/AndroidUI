package nx.peter.app.android_ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import nx.peter.app.android_ui.R;
import nx.peter.app.android_ui.view.pdf.remote.DownloadFile;
import nx.peter.app.android_ui.view.util.FileUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

class RemotePDFViewPager extends ViewPager implements DownloadFile.Listener {
    protected DownloadFile downloadFile;
    protected DownloadFile.Listener listener;

    public RemotePDFViewPager(Context context, String pdfUrl, DownloadFile.Listener listener) {
        super(context);
        this.listener = listener;

        setUrl(pdfUrl);
    }

    public RemotePDFViewPager(
            Context context,
            DownloadFile downloadFile,
            String pdfUrl,
            DownloadFile.Listener listener) {
        super(context);
        this.listener = listener;

        init(downloadFile, pdfUrl);
    }

    public RemotePDFViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public void setUrl(@NonNull CharSequence url) {
        init(new DownloadFileUrlConnectionImpl(getContext(), new Handler(), this), url.toString());
    }

    private void init(DownloadFile downloadFile, String pdfUrl) {
        setDownloader(downloadFile);
        downloadFile.download(pdfUrl, new File(getContext().getCacheDir(), FileUtil.extractFileNameFromURL(pdfUrl)).getAbsolutePath());
    }

    @SuppressLint("CustomViewStyleable")
    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a;

            a = getContext().obtainStyledAttributes(attrs, R.styleable.PDFViewPager);
            String pdfUrl = a.getString(R.styleable.PDFViewPager_pdfUrl);

            if (pdfUrl != null && pdfUrl.length() > 0)
                init(new DownloadFileUrlConnectionImpl(getContext(), new Handler(), this), pdfUrl);

            a.recycle();
            a.close();
        }
    }

    public void setDownloader(DownloadFile downloadFile) {
        this.downloadFile = downloadFile;
    }

    public DownloadFile getDownloader() {
        return downloadFile;
    }

    @Override
    public void onSuccess(String url, String destinationPath) {
        listener.onSuccess(url, destinationPath);
    }

    @Override
    public void onFailure(Exception e) {
        listener.onFailure(e);
    }

    @Override
    public void onProgressUpdate(int progress, int total) {
        listener.onProgressUpdate(progress, total);
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



    static class DownloadFileUrlConnectionImpl implements DownloadFile {
        private static final int KILOBYTE = 1024;

        private static final int BUFFER_LEN = KILOBYTE;
        private static final int NOTIFY_PERIOD = 150 * KILOBYTE;

        Context context;
        Handler uiThread;
        Listener listener = new DownloadFileUrlConnectionImpl.NullListener();

        public DownloadFileUrlConnectionImpl(Context context, Handler uiThread) {
            this.context = context;
            this.uiThread = uiThread;
        }

        public DownloadFileUrlConnectionImpl(Context context, Handler uiThread, Listener listener) {
            this(context, uiThread);
            this.listener = listener;
        }

        @Override
        public void download(final String url, final String destinationPath) {
            new Thread(() -> {
                try {
                    File file = new File(destinationPath);
                    FileOutputStream fileOutput = new FileOutputStream(file);
                    HttpURLConnection urlConnection;
                    URL urlObj = new URL(url);
                    urlConnection = (HttpURLConnection) urlObj.openConnection();
                    int totalSize = urlConnection.getContentLength();
                    int downloadedSize = 0;
                    int counter = 0;
                    byte[] buffer = new byte[BUFFER_LEN];
                    int bufferLength;
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    while ((bufferLength = in.read(buffer)) > 0) {
                        fileOutput.write(buffer, 0, bufferLength);
                        downloadedSize += bufferLength;
                        counter += bufferLength;
                        if (listener != null && counter > NOTIFY_PERIOD) {
                            notifyProgressOnUiThread(downloadedSize, totalSize);
                            counter = 0;
                        }
                    }

                    urlConnection.disconnect();
                    fileOutput.close();

                    notifySuccessOnUiThread(url, destinationPath);
                } catch (IOException e) {
                    notifyFailureOnUiThread(e);
                }
            }).start();
        }

        protected void notifySuccessOnUiThread(final String url, final String destinationPath) {
            if (uiThread == null)
                return;

            uiThread.post(() -> listener.onSuccess(url, destinationPath));
        }

        protected void notifyFailureOnUiThread(final Exception e) {
            if (uiThread == null)
                return;

            uiThread.post(() -> listener.onFailure(e));
        }

        private void notifyProgressOnUiThread(final int downloadedSize, final int totalSize) {
            if (uiThread == null)
                return;

            uiThread.post(() -> listener.onProgressUpdate(downloadedSize, totalSize));
        }

        protected static class NullListener implements Listener {
            @Override
            public void onSuccess(String url, String destinationPath) {
                /* Empty */
            }

            @Override
            public void onFailure(Exception e) {
                /* Empty */
            }

            @Override
            public void onProgressUpdate(int progress, int total) {
                /* Empty */
            }
        }
    }

}
