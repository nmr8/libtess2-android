package nmr.libtess2_android;

import android.util.Log;

import java.lang.annotation.ElementType;

public class libtess2 {
    private static final String TAG = "libtess2";

    static {
        try {
            System.loadLibrary("tess2");
        } catch (LinkageError e) {
            Log.e(TAG, "", e);
        }
    }

    public static final int TESS_UNDEF = ~0;

    public enum TessWindingRule {
        TESS_WINDING_ODD,
        TESS_WINDING_NONZERO,
        TESS_WINDING_POSITIVE,
        TESS_WINDING_NEGATIVE,
        TESS_WINDING_ABS_GEQ_TWO,
    }

    public enum TessElementType {
        TESS_POLYGONS,
        TESS_CONNECTED_POLYGONS,
        TESS_BOUNDARY_CONTOURS,
    }

    public static final class TESStesselator {
        private final long nativePointer;

        private TESStesselator(long nativePointer) {
            this.nativePointer = nativePointer;
        }

        @Override protected void finalize() throws Throwable {
            _tessDeleteTess(nativePointer);
            super.finalize();
        }
    }

    private libtess2() {}

    public static TESStesselator tessNewTess() {
        return new TESStesselator(_tessNewTess());
    }

    private static native long _tessNewTess();

    private static native void _tessDeleteTess(long nativePointer);

    public static native void tessAddContour(TESStesselator tess, int size, float[] contour, int stride, int count,
                                             int offset);

    public static int tessTesselate(TESStesselator tess, TessWindingRule windingRule, TessElementType elementType,
                                    int polySize, int vertexSize, float[] normal) {
        return tessTesselate(tess, windingRule.ordinal(), elementType.ordinal(), polySize, vertexSize, normal);
    }

    public static native int tessTesselate(TESStesselator tess, int windingRule, int elementType,
                                           int polySize, int vertexSize, float[] normal);

    public static native int tessGetVertexCount(TESStesselator tess);

    public static native float[] tessGetVertices(TESStesselator tess);

    public static native int[] tessGetVertexIndices(TESStesselator tess);

    public static native int tessGetElementCount(TESStesselator tess);

    public static native int[] tessGetElements(TESStesselator tess);
}
