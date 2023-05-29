package nx.peter.app.android_ui.view.pdf.remote;

public interface DownloadFile {
    void download(String url, String destinationPath);

    interface Listener {
        void onSuccess(String url, String destinationPath);

        void onFailure(Exception e);

        void onProgressUpdate(int progress, int total);
    }
}
