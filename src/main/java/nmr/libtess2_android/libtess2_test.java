package nmr.libtess2_android;

import nmr.libtess2_android.libtess2.TESStesselator;

import java.util.Arrays;

public class libtess2_test {
    private static final String TAG = "libtess2_test";

    public static void test() {
        testBasic2dBoxTriangulation();
        testBasic3dCubeTriangulation();
        testBasic2dBoxBoundary();
    }

    public static void testBasic2dBoxBoundary() {
        TESStesselator tess = libtess2.tessNewTess();
        libtess2.tessAddContour(tess, 2, new float[]{0,0, 0,1, 1,1, 1,0}, 8, 4, 0);
        libtess2.tessTesselate(tess, libtess2.TessWindingRule.TESS_WINDING_ODD,
                libtess2.TessElementType.TESS_BOUNDARY_CONTOURS, 3, 2, null);

        String expectedResult = "1 [0, 4] 4 [3, 0, 1, 2] [1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0]";
        checkExpectedResult(tess, expectedResult);
    }

    public static void testBasic2dBoxTriangulation() {
        TESStesselator tess = libtess2.tessNewTess();
        libtess2.tessAddContour(tess, 2, new float[]{0,0, 0,1, 1,1, 1,0}, 8, 4, 0);
        libtess2.tessTesselate(tess, libtess2.TessWindingRule.TESS_WINDING_ODD, libtess2.TessElementType.TESS_POLYGONS,
                3, 2, null);

        String expectedResult = "2 [0, 1, 2, 1, 0, 3] 4 [0, 2, 3, 1] [0.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0, 1.0]";
        checkExpectedResult(tess, expectedResult);
    }

    public static void testBasic3dCubeTriangulation() {
        TESStesselator tess = libtess2.tessNewTess();
        libtess2.tessAddContour(tess, 3, new float[]{0,0,0, 0,1,0, 1,1,0, 1,0,0}, 12, 4, 0);
        libtess2.tessTesselate(tess, libtess2.TessWindingRule.TESS_WINDING_ODD, libtess2.TessElementType.TESS_POLYGONS,
                3, 3, null);

        String expectedResult = "2 [0, 1, 2, 1, 0, 3] 4 [0, 2, 3, 1] [0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0]";
        checkExpectedResult(tess, expectedResult);
    }

    private static void checkExpectedResult(TESStesselator tess, String expectedResult) {
        int elementCount = libtess2.tessGetElementCount(tess);
        int[] elements = libtess2.tessGetElements(tess);
        int vertexCount = libtess2.tessGetVertexCount(tess);
        int[] vertexIndices = libtess2.tessGetVertexIndices(tess);
        float[] vertices = libtess2.tessGetVertices(tess);

        String result = elementCount + " " + Arrays.toString(elements) + " " + vertexCount + " " +
                Arrays.toString(vertexIndices) + " " + Arrays.toString(vertices);

        if (!expectedResult.equals(result)) {
            throw new Error();
        }
    }
}
