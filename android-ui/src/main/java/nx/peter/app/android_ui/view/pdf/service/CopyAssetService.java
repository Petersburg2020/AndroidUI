package nx.peter.app.android_ui.view.pdf.service;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import java.io.IOException;
import nx.peter.app.android_ui.view.util.FileUtil;

public class CopyAssetService extends IntentService {
    private static String ACTION_COPY_ASSET = "";
           // BuildConfig.LIBRARY_PACKAGE_NAME + ;
    private static String EXTRA_ASSET = "";
           // BuildConfig.LIBRARY_PACKAGE_NAME + ".asset";
    private static String EXTRA_DESTINATION = "";
           // BuildConfig.LIBRARY_PACKAGE_NAME + ".destination_path"; */

    public CopyAssetService() {
        super("CopyAssetService");
    }

    public static void startCopyAction(Context context, String asset, String destinationPath) {
        ACTION_COPY_ASSET = context.getPackageName() + ".copy_asset";
        EXTRA_ASSET = context.getPackageName() + ".asset";
        EXTRA_DESTINATION = context.getPackageName() + ".destination_path";
        
        Intent intent = new Intent(context, CopyAssetService.class);
        intent.setAction(ACTION_COPY_ASSET);
        intent.putExtra(EXTRA_ASSET, asset);
        intent.putExtra(EXTRA_DESTINATION, destinationPath);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_COPY_ASSET.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_ASSET);
                final String param2 = intent.getStringExtra(EXTRA_DESTINATION);
                handleActionCopyAsset(param1, param2);
            }
        }
    }

    private void handleActionCopyAsset(String asset, String destinationPath) {
        try {
            FileUtil.copyAsset(this, asset, destinationPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
