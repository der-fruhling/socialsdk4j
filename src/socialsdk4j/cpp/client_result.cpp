#include <net_derfruhling_discord_socialsdk4j_ClientResult.h>
#include <discordpp.h>
#include "socialsdk4j.hpp"

jobject s4j::createClientResult(JNIEnv *env, discordpp::ClientResult &&res) {
    discordpp::ClientResult *resultPtr = new discordpp::ClientResult(std::move(res));
    jclass resultCls = env->FindClass("net/derfruhling/discord/socialsdk4j/ClientResult");
    jmethodID resultCons = env->GetMethodID(resultCls, "<init>", "(J)V");
    jobject result = env->NewObject(resultCls, resultCons, reinterpret_cast<jlong>(resultPtr));
    return result;
}

JNIEXPORT jint JNICALL
Java_net_derfruhling_discord_socialsdk4j_ClientResult_errorCode0
(JNIEnv *, jclass, jlong ptr) {
    discordpp::ClientResult *result = reinterpret_cast<discordpp::ClientResult *>(ptr);
    return static_cast<jint>(result->Type());
}

JNIEXPORT jstring JNICALL
Java_net_derfruhling_discord_socialsdk4j_ClientResult_errorMessage0
(JNIEnv *env, jclass, jlong ptr) {
    discordpp::ClientResult *result = reinterpret_cast<discordpp::ClientResult *>(ptr);
    std::string error = result->Error();
    return env->NewStringUTF(error.c_str());
}

JNIEXPORT jboolean JNICALL
Java_net_derfruhling_discord_socialsdk4j_ClientResult_isRetryable0
(JNIEnv *, jclass, jlong ptr) {
    discordpp::ClientResult *result = reinterpret_cast<discordpp::ClientResult *>(ptr);
    return static_cast<jboolean>(result->Retryable());
}

JNIEXPORT jfloat JNICALL Java_net_derfruhling_discord_socialsdk4j_ClientResult_getRetryDelay0
(JNIEnv *, jclass, jlong ptr) {
    discordpp::ClientResult *result = reinterpret_cast<discordpp::ClientResult *>(ptr);
    return static_cast<jfloat>(result->RetryAfter());
}
