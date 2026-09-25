#include <net_derfruhling_discord_socialsdk4j_SocialSdk.h>
#include <discordpp.h>
#include <stdarg.h>

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_delete
(JNIEnv *, jobject, jlong ptr) {
    delete reinterpret_cast<discordpp::Client*>(ptr);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Lobby_delete
(JNIEnv *, jobject, jlong ptr) {
    delete reinterpret_cast<discordpp::LobbyHandle*>(ptr);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_LobbyMember_delete
(JNIEnv *, jobject, jlong ptr) {
    delete reinterpret_cast<discordpp::LobbyMemberHandle*>(ptr);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Call_delete
(JNIEnv *, jobject, jlong ptr) {
    delete reinterpret_cast<discordpp::Call*>(ptr);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_User_delete
(JNIEnv *, jobject, jlong ptr) {
    delete reinterpret_cast<discordpp::UserHandle*>(ptr);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Message_delete
(JNIEnv *, jclass, jlong ptr) {
    delete reinterpret_cast<discordpp::MessageHandle*>(ptr);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_ClientResult_00024Results_delete
(JNIEnv *, jobject, jlong ptr) {
    delete reinterpret_cast<discordpp::ClientResult*>(ptr);
}
