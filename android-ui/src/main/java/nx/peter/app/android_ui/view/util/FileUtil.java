package nx.peter.app.android_ui.view.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import nx.peter.java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static boolean copyAsset(Context ctx, String assetName, String destinationPath) throws IOException {
        InputStream in = ctx.getAssets().open(assetName);
        OutputStream out = new FileOutputStream(destinationPath);

        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1)
            out.write(buffer, 0, read);

        in.close();
        out.close();
        return true;
    }

    public static List<FileInfo> getAllFiles(@NonNull Context context, @NonNull CharSequence directory) {
        List<FileInfo> fileInfo = new ArrayList<>();
        if (!new File(directory).exists() || !new File(directory).isDirectory()) return fileInfo;

        String selection;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            selection = MediaStore.Files.FileColumns.RELATIVE_PATH + " LIKE ? ";
        else selection = MediaStore.Files.FileColumns.DATA + " LIKE ? ";

        String[] selectionArgs = new String[] {directory.toString()};

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Files.getContentUri("external"),
                null,
                selection,
                selectionArgs,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
        );
        if (cursor != null) {
            cursor.moveToFirst();
            do {
                int col = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
                String path = cursor.getString(col);
                long date = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED));
                Uri uri = Uri.parse(path);

                fileInfo.add(new IFileInfo(path, uri, date));
            } while (cursor.moveToNext());

            cursor.close();
        }

        return fileInfo;
    }



    public static String extractFileNameFromURL(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }


    static class IFileInfo implements FileInfo {
        final String path;
        final Uri uri;
        long date;

        IFileInfo(@NonNull String path, @NonNull Uri uri, long date) {
            this.path = path;
            this.uri = uri;
            this.date = date;
        }


        @NonNull
        @Override
        public Uri getUri() {
            return uri;
        }

        @NonNull
        @Override
        public CharSequence getPath() {
            return path;
        }

        @NonNull
        @Override
        public File getFile() {
            return new File(path);
        }

        @Override
        public long getDateAdded() {
            return date;
        }
    }
}
