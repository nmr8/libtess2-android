#include "nmr_libtess2_android_libtess2.h"
#include "Include/tesselator.h"
#include <stddef.h>
#include <stdint.h>

static int getTessOutputElementType(JNIEnv *env, jobject tess) {
    jclass tessCls = (*env)->GetObjectClass(env, tess);
    jfieldID oetId = (*env)->GetFieldID(env, tessCls, "outputElementType", "I");
    return (*env)->GetIntField(env, tess, oetId);
}

static int getTessOutputPolygonSize(JNIEnv *env, jobject tess) {
    jclass tessCls = (*env)->GetObjectClass(env, tess);
    jfieldID opsId = (*env)->GetFieldID(env, tessCls, "outputPolySize", "I");
    return (*env)->GetIntField(env, tess, opsId);
}

static int getTessOutputVertexSize(JNIEnv *env, jobject tess) {
    jclass tessCls = (*env)->GetObjectClass(env, tess);
    jfieldID ovsId = (*env)->GetFieldID(env, tessCls, "outputVertexSize", "I");
    return (*env)->GetIntField(env, tess, ovsId);
}

static TESStesselator * getTessp(JNIEnv *env, jobject tess) {
    jclass tessCls = (*env)->GetObjectClass(env, tess);
    jfieldID ptrId = (*env)->GetFieldID(env, tessCls, "nativePointer", "J");
    return (TESStesselator *) (uintptr_t) (*env)->GetLongField(env, tess, ptrId);
}

JNIEXPORT jlong JNICALL Java_nmr_libtess2_1android_libtess2__1tessNewTess(JNIEnv *env, jclass cls) {
    return (jlong) (uintptr_t) tessNewTess(NULL);
}

JNIEXPORT void JNICALL Java_nmr_libtess2_1android_libtess2__1tessDeleteTess(JNIEnv *env, jclass cls, jlong tess) {
    tessDeleteTess((TESStesselator *) (uintptr_t) tess);
}

JNIEXPORT void JNICALL Java_nmr_libtess2_1android_libtess2_tessAddContour
  (JNIEnv *env, jclass cls, jobject tess, jint size, jfloatArray contour, jint stride, jint count, jint offset) {
    TESStesselator *tessp = getTessp(env, tess);
    float *contour_ = (*env)->GetPrimitiveArrayCritical(env, contour, 0);
    tessAddContour(tessp, size, contour_, stride, count);
    (*env)->ReleasePrimitiveArrayCritical(env, contour, contour_, 0);
}

JNIEXPORT jint JNICALL Java_nmr_libtess2_1android_libtess2_tessTesselate
  (JNIEnv *env, jclass cls, jobject tess, jint windingRule, jint elementType, jint polySize, jint vertexSize,
  jfloatArray normals) {
    TESStesselator *tessp = getTessp(env, tess);
    float *normals_ = normals == NULL ? NULL : (*env)->GetPrimitiveArrayCritical(env, normals, 0);
    int result = tessTesselate(tessp, windingRule, elementType, polySize, vertexSize, normals_);
    if (normals_ != NULL) {
        (*env)->ReleasePrimitiveArrayCritical(env, normals, normals_, 0);
    }
    return result;
}

JNIEXPORT jint JNICALL Java_nmr_libtess2_1android_libtess2_tessGetVertexCount(JNIEnv *env, jclass cls, jobject tess) {
    return tessGetVertexCount(getTessp(env, tess));
}

JNIEXPORT jfloatArray JNICALL Java_nmr_libtess2_1android_libtess2_tessGetVertices(JNIEnv *env, jclass cls,
  jobject tess) {
    TESStesselator *tessp = getTessp(env, tess);
    const float* vertices = tessGetVertices(tessp);
    int vertexCount = tessGetVertexCount(tessp);
    int resultSize = vertexCount * getTessOutputVertexSize(env, tess);

    jfloatArray result = (*env)->NewFloatArray(env, resultSize);
    (*env)->SetFloatArrayRegion(env, result, 0, resultSize, vertices);
    return result;
}

JNIEXPORT jintArray JNICALL Java_nmr_libtess2_1android_libtess2_tessGetVertexIndices(JNIEnv *env, jclass cls,
  jobject tess) {
    TESStesselator *tessp = getTessp(env, tess);
    const int* vertexIndices = tessGetVertexIndices(tessp);
    int vertexCount = tessGetVertexCount(tessp);
    jintArray result = (*env)->NewIntArray(env, vertexCount);
    (*env)->SetIntArrayRegion(env, result, 0, vertexCount, vertexIndices);
    return result;
}

JNIEXPORT jint JNICALL Java_nmr_libtess2_1android_libtess2_tessGetElementCount(JNIEnv *env, jclass cls, jobject tess) {
    return tessGetElementCount(getTessp(env, tess));
}

JNIEXPORT jintArray JNICALL Java_nmr_libtess2_1android_libtess2_tessGetElements(JNIEnv *env, jclass cls, jobject tess) {
    TESStesselator *tessp = getTessp(env, tess);
    const int* elements = tessGetElements(tessp);
    int elementCount = tessGetElementCount(tessp);

    // xxx this is wrong for TESS_BOUNDARY_CONTOURS, need to find out how that output is formatted
    int resultSize = elementCount * getTessOutputPolygonSize(env, tess);
    int tessOet = getTessOutputElementType(env, tess);
    resultSize *= tessOet == TESS_CONNECTED_POLYGONS ? 2 : 1;

    jintArray result = (*env)->NewIntArray(env, resultSize);
    (*env)->SetIntArrayRegion(env, result, 0, resultSize, elements);
    return result;
}
