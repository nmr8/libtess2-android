package nmr.libtess2_android;

import android.util.Log;
import nmr.libtess2_android.libtess2.TESStesselator;

import java.util.Arrays;

public class libtess2_test {
    private static final String TAG = "libtess2_test";

    public static void test() {
        TESStesselator tess = libtess2.tessNewTess();
        libtess2.tessAddContour(tess, 4, new float[]{0,0, 0,1, 1,1, 1,0}, 8, 4, 0);
        libtess2.tessTesselate(tess, libtess2.TessWindingRule.TESS_WINDING_ODD, libtess2.TessElementType.TESS_POLYGONS,
                3, 2, null);

        int elementCount = libtess2.tessGetElementCount(tess);
        int[] elements = libtess2.tessGetElements(tess);
        int vertexCount = libtess2.tessGetVertexCount(tess);
        int[] vertexIndices = libtess2.tessGetVertexIndices(tess);
        float[] vertices = libtess2.tessGetVertices(tess);

        String result = elementCount + " " + Arrays.toString(elements) + " " + vertexCount + " " +
                Arrays.toString(vertexIndices) + " " + Arrays.toString(vertices);
        Log.d(TAG, result);

        if (!"2 [0, 1, 2, 1, 0, 3] 4 [3, 1, 2, 0] [1.0, 0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0]".equals(result)) {
            throw new Error();
        }
    }
}
