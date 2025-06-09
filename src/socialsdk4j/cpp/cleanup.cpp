#include <net_derfruhling_discord_socialsdk4j_SocialSdk.h>
#include <discordpp.h>
#include <stdarg.h>

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_SocialSdk_deleteClientNative
(JNIEnv *, jclass, long ptr) {
    delete reinterpret_cast<discordpp::Client*>(ptr);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_SocialSdk_deleteLobbyNative
(JNIEnv *, jclass, jlong ptr) {
    delete reinterpret_cast<discordpp::LobbyHandle*>(ptr);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_SocialSdk_deleteLobbyMemberNative
(JNIEnv *, jclass, jlong ptr) {
    delete reinterpret_cast<discordpp::LobbyMemberHandle*>(ptr);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_SocialSdk_deleteCallNative
(JNIEnv *, jclass, jlong ptr) {
    delete reinterpret_cast<discordpp::Call*>(ptr);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_SocialSdk_deleteUserNative
(JNIEnv *, jclass, jlong ptr) {
    delete reinterpret_cast<discordpp::UserHandle*>(ptr);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_SocialSdk_deleteMessageNative
(JNIEnv *, jclass, jlong ptr) {
    delete reinterpret_cast<discordpp::MessageHandle*>(ptr);
}
