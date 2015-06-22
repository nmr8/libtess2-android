package nmr.libtess2_android;

import android.util.Log;

import java.lang.annotation.ElementType;

public class libtess2 {
    public static final int TESS_UNDEF = ~0;

    private static final String TAG = "libtess2";

    static {
        try {
            System.loadLibrary("tess2");
        } catch (LinkageError e) {
            Log.e(TAG, "", e);
        }
    }

    /**
     * See "Tessellation Properties" in http://www.glprogramming.com/red/chapter11.html
     */
    public enum TessWindingRule {
        TESS_WINDING_ODD,
        TESS_WINDING_NONZERO,
        TESS_WINDING_POSITIVE,
        TESS_WINDING_NEGATIVE,
        TESS_WINDING_ABS_GEQ_TWO,
    }

    public enum TessElementType {
        /**
         * elements are polygons
         */
        TESS_POLYGONS,

        /**
         * elements are polygons, each followed by four indices to neighboring elements (or TESS_UNDEF if none)
         */
        TESS_CONNECTED_POLYGONS,

        // xxx what does this do?
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

    /**
     * create new tesselator state
     */
    public static TESStesselator tessNewTess() {
        return new TESStesselator(_tessNewTess());
    }

    private static native long _tessNewTess();

    private static native void _tessDeleteTess(long nativePointer);

    /**
     * @param tess tesselator state
     * @param size number of coordinates per vertex. Must be 2 or 3.
     * @param contour packed contour vertex data
     * @param stride in bytes between vertices
     * @param count number of vertices
     * @param offset unused
     */
    public static native void tessAddContour(TESStesselator tess, int size, float[] contour, int stride, int count,
                                             int offset); // xxx offset is unimplemented

    /**
     * @param tess tesselator state
     * @param outputElementType the type of output elements to compute
     * @param outputPolySize size in vertices an output polygon should be
     * @param vertexSize number of coordinates per vertex, must be 2 or 3 xxx input (normals) or output?
     * @param normal defines the normal of the input contours, or null and the normals will be calculated.
     * @return 0 on failure
     */
    public static int tessTesselate(TESStesselator tess, TessWindingRule windingRule, TessElementType outputElementType,
                                    int outputPolySize, int vertexSize, float[] normal) {
        return tessTesselate(tess, windingRule.ordinal(), outputElementType.ordinal(), outputPolySize, vertexSize, normal);
    }

    public static native int tessTesselate(TESStesselator tess, int windingRule, int elementType,
                                           int polySize, int vertexSize, float[] normal);

    public static native int tessGetVertexCount(TESStesselator tess);

    public static native float[] tessGetVertices(TESStesselator tess);

    public static native int[] tessGetVertexIndices(TESStesselator tess);

    public static native int tessGetElementCount(TESStesselator tess);

    public static native int[] tessGetElements(TESStesselator tess);
}
