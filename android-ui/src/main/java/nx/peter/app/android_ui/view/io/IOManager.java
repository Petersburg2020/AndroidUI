package nx.peter.app.android_ui.view.io;

import androidx.annotation.NonNull;

import java.io.*;

public class IOManager {
    public static byte[] toByteArray(InputStream in) {
        ByteArrayOutputStream baout = new ByteArrayOutputStream();
        copy(in, baout);
        return baout.toByteArray();
    }
    public static long copy(InputStream input, OutputStream output) {
        try {
            byte[] buffer = new byte[4096];
            long count = 0;
            int n;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
                count += n;
            }
            return count;
        } catch (IOException e) {
            return 0;
        }
    }

    public static long populateBuffer(@NonNull InputStream in, byte[] buffer) {
        try {
            int remaining = buffer.length;
            while (remaining > 0) {
                int bufferWritePos = buffer.length - remaining;
                int bytesRead = in.read(buffer, bufferWritePos, remaining);
                if (bytesRead < 0) break; //EOD
                remaining -= bytesRead;
            }
            return buffer.length - remaining;
        } catch (IOException e) {
            return -1;
        }
    }

    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null)
                closeable.close();
        } catch (IOException ignored) {
            // ignore
        }
    }
}
