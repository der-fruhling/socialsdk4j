#include "socialsdk4j.hpp"

jclass s4j::logMethodClass;
jmethodID s4j::logMethodId;

void s4j::Log(JNIEnv *env, jint severity, const char *message, ...) {
    va_list va;
    va_start(va, message);

    thread_local char buffer[16000];
    buffer[vsprintf(buffer, message, va)] = 0;

    env->CallStaticVoidMethod(s4j::logMethodClass, s4j::logMethodId, severity, env->NewStringUTF(buffer));

    va_end(va);
}
