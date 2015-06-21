package nmr8.libtess2_android;

import android.util.Log;

public class libtess2 {
    private static final String TAG = "libtess2";

    static {
        try {
            System.loadLibrary("tess2");
        } catch (LinkageError e) {
            Log.e(TAG, "", e);
        }
    }

    private libtess2() {}

    public static void
}
