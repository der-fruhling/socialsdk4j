#ifndef SOCIALSDK4J_CLIENT_RESULT_HPP
#define SOCIALSDK4J_CLIENT_RESULT_HPP

#include <jni.h>
#include <discordpp.h>

inline jobject CreateClientResult(JNIEnv *env, discordpp::ClientResult &&res) {
    discordpp::ClientResult *resultPtr = new discordpp::ClientResult(std::move(res));
    jclass resultCls = env->FindClass("net/derfruhling/discord/socialsdk4j/ClientResult");
    jmethodID resultCons = env->GetMethodID(resultCls, "<init>", "(J)V");
    jobject result = env->NewObject(resultCls, resultCons, reinterpret_cast<jlong>(resultPtr));
    return result;
}

#endif // SOCIALSDK4J_CLIENT_RESULT_HPP
