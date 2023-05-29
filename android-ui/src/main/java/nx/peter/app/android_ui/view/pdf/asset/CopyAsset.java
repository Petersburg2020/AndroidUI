package nx.peter.app.android_ui.view.pdf.asset;

public interface CopyAsset {
    void copy(String assetName, String destinationPath);

    interface Listener {
        void success(String assetName, String destinationPath);
        void failure(Exception e);
    }
}
