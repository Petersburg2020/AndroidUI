package nx.peter.app.android_ui.view.pdf.subscale;

public interface OnAnimationEventListener {
    void onComplete();

    void onInterruptedByUser();

    void onInterruptedByNewAnim();
}
