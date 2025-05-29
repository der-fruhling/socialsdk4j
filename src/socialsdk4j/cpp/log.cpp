#include "log.hpp"

jclass logMethodClass;
jmethodID logMethodId;

void Log(JNIEnv *env, jint severity, const char *message, ...) {
    va_list va;
    va_start(va, message);

    thread_local char buffer[16000];
    buffer[vsprintf(buffer, message, va)] = 0;

    env->CallStaticVoidMethod(logMethodClass, logMethodId, severity, env->NewStringUTF(buffer));

    va_end(va);
}
