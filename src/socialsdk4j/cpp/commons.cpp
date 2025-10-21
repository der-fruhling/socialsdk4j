#include "jni.h"
#include "socialsdk4j.hpp"

jlong s4j::getPointerLong(JNIEnv *env, jobject obj, jclass clazzMaybe) {
    jclass clazz = clazzMaybe ? clazzMaybe : env->GetObjectClass(obj);
    jfieldID field = env->GetFieldID(clazz, "pointer", "J");
    jlong value = env->GetLongField(obj, field);

    return value;
}
