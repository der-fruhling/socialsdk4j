#define DISCORDPP_IMPLEMENTATION

#include <net_derfruhling_discord_socialsdk4j_SocialSdk.h>
#include <discordpp.h>
#include <stdarg.h>

#include "log.hpp"
#include "callbacks.hpp"

JNIEXPORT jlong JNICALL
Java_net_derfruhling_discord_socialsdk4j_SocialSdk_createClientNative
(JNIEnv *env, jclass cls) {
    discordpp::Client *client = new discordpp::Client();
    logMethodClass = cls;
    logMethodId = env->GetStaticMethodID(cls, "javaLog", "(ILjava/lang/String;)V");

    client->AddLogCallback([](
        std::string message,
        discordpp::LoggingSeverity severity
    ) { Log(cbenv, (jint)severity, "%s", message.c_str()); }, discordpp::LoggingSeverity::Verbose);

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
