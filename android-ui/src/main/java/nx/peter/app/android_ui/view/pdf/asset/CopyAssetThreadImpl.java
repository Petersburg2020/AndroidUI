package nx.peter.app.android_ui.view.pdf.asset;

import android.content.Context;
import android.os.Handler;
import java.io.IOException;
import nx.peter.app.android_ui.view.util.FileUtil;

public class CopyAssetThreadImpl implements CopyAsset {
    Context context;
    Handler uiThread;
    Listener listener = new NullListener();

    public CopyAssetThreadImpl(Context context, Handler uiThread, Listener listener) {
        this.context = context;
        this.uiThread = uiThread;
        if (listener != null) {
            this.listener = listener;
        }
    }

    public CopyAssetThreadImpl(Context context, Handler uiThread) {
        this.context = context;
        this.uiThread = uiThread;
    }

    @Override
    public void copy(final String assetName, final String destinationPath) {
        new Thread(
                        new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    FileUtil.copyAsset(context, assetName, destinationPath);
                                    notifySuccess(assetName, destinationPath);
                                } catch (IOException e) {
                                    notifyError(e);
                                }
                            }
                        })
                .start();
    }

    private void notifySuccess(final String assetName, final String destinationPath) {
        if (uiThread == null) {
            return;
        }

        uiThread.post(
                new Runnable() {
                    @Override
                    public void run() {
                        listener.success(assetName, destinationPath);
                    }
                });
    }

    private void notifyError(final IOException e) {
        if (uiThread == null) {
            return;
        }

        uiThread.post(
                new Runnable() {
                    @Override
                    public void run() {
                        listener.failure(e);
                    }
                });
    }

    protected class NullListener implements Listener {
        public void success(String assetName, String destinationPath) {}

        public void failure(Exception e) {}
    }
}
