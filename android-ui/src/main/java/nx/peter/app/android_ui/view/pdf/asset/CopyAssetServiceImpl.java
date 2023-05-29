package nx.peter.app.android_ui.view.pdf.asset;

import android.content.Context;
import nx.peter.app.android_ui.view.pdf.service.CopyAssetService;

public class CopyAssetServiceImpl implements CopyAsset {
    private final Context context;

    public CopyAssetServiceImpl(Context context) {
        this.context = context;
    }

    @Override
    public void copy(String assetName, String destinationPath) {
        CopyAssetService.startCopyAction(context, assetName, destinationPath);
    }
}
