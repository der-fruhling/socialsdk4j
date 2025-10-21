#ifndef __SOCIALSDK4J_HPP
#define __SOCIALSDK4J_HPP

#include <discordpp.h>
#include <jni.h>

extern JNIEnv *cbenv;

namespace s4j {

extern jclass logMethodClass;
extern jmethodID logMethodId;

#define VERBOSE 1
#define INFO 2
#define WARNING 3
#define ERROR 4

void Log(JNIEnv *env, jint severity, const char *message, ...);

jobject createClientResult(JNIEnv *env, discordpp::ClientResult &&res);
jobject createActivityInfo(JNIEnv *env, const discordpp::Activity &activity);

jlong getPointerLong(JNIEnv *env, jobject obj, jclass clazzMaybe = nullptr);

template<typename T>
T *getPointer(JNIEnv *env, jobject obj, jclass clazzMaybe = nullptr) {
    return reinterpret_cast<T*>(getPointerLong(env, obj, clazzMaybe));
}

} // namespace s4j

#endif // __SOCIALSDK4J_HPP
