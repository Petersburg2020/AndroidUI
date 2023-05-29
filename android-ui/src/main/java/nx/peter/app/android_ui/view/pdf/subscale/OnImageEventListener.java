package nx.peter.app.android_ui.view.pdf.subscale;

public interface OnImageEventListener {
    void onReady();

    void onImageLoaded();

    void onPreviewLoadError(Exception e);

    void onImageLoadError(Exception e);

    void onTileLoadError(Exception e);

    void onPreviewReleased();
}
