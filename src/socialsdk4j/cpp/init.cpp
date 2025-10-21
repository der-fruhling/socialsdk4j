#define DISCORDPP_IMPLEMENTATION

#include <net_derfruhling_discord_socialsdk4j_SocialSdk.h>
#include <discordpp.h>
#include <stdarg.h>

#include "socialsdk4j.hpp"

JNIEXPORT jlong JNICALL
Java_net_derfruhling_discord_socialsdk4j_SocialSdk_createClientNative
(JNIEnv *env, jclass cls) {
    discordpp::Client *client = new discordpp::Client();
    s4j::logMethodClass = cls;
    s4j::logMethodId = env->GetStaticMethodID(cls, "javaLog", "(ILjava/lang/String;)V");

    client->AddLogCallback([](
        std::string message,
        discordpp::LoggingSeverity severity
    ) { s4j::Log(cbenv, (jint)severity, "%s", message.c_str()); }, discordpp::LoggingSeverity::Verbose);

    return reinterpret_cast<jlong>(client);
}

JNIEnv *cbenv;

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_SocialSdk_runCallbacksNative
(JNIEnv *env, jclass cls) {
    cbenv = env;
    discordpp::RunCallbacks();
    cbenv = nullptr;
}
