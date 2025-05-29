#ifndef SOCIALSDK4J_LOG_HPP
#define SOCIALSDK4J_LOG_HPP

#include <jni.h>

extern jclass logMethodClass;
extern jmethodID logMethodId;

#define VERBOSE 1
#define INFO 2
#define WARNING 3
#define ERROR 4

void Log(JNIEnv *env, jint severity, const char *message, ...);

#endif // SOCIALSDK4J_LOG_HPP
