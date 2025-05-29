#include <jni.h>
#include <net_derfruhling_discord_socialsdk4j_LobbyMember.h>
#include <discordpp.h>
#include <optional>
#include <unordered_map>

jfieldID lobbyMemberPtrF = nullptr;

JNIEXPORT jboolean JNICALL
Java_net_derfruhling_discord_socialsdk4j_LobbyMember_canLinkLobby
(JNIEnv *env, jobject obj) {
    if (!lobbyMemberPtrF) lobbyMemberPtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::LobbyMemberHandle *member = (discordpp::LobbyMemberHandle *)env->GetLongField(obj, lobbyMemberPtrF);

    return (jboolean)member->CanLinkLobby();
}

/*
 * Class:     net_derfruhling_discord_socialsdk4j_LobbyMember
 * Method:    isConnected
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL
Java_net_derfruhling_discord_socialsdk4j_LobbyMember_isConnected
(JNIEnv *env, jobject obj) {
    if (!lobbyMemberPtrF) lobbyMemberPtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::LobbyMemberHandle *member = (discordpp::LobbyMemberHandle *)env->GetLongField(obj, lobbyMemberPtrF);

    return (jboolean)member->Connected();
}

/*
 * Class:     net_derfruhling_discord_socialsdk4j_LobbyMember
 * Method:    getUser
 * Signature: ()Lnet/derfruhling/discord/socialsdk4j/User;
 */
JNIEXPORT jobject JNICALL
Java_net_derfruhling_discord_socialsdk4j_LobbyMember_getUser
(JNIEnv *env, jobject obj) {
    if (!lobbyMemberPtrF) lobbyMemberPtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::LobbyMemberHandle *member = (discordpp::LobbyMemberHandle *)env->GetLongField(obj, lobbyMemberPtrF);

    std::optional<discordpp::UserHandle> user = member->User();
    discordpp::UserHandle *userP = user.has_value()
        ? new discordpp::UserHandle(std::move(user.value()))
        : nullptr;

    jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/User");
    jmethodID methodId = env->GetMethodID(clazz, "<init>", "(JJ)V");

    return env->NewObject(clazz, methodId, (jlong)userP, (jlong)userP->Id());
}

/*
 * Class:     net_derfruhling_discord_socialsdk4j_LobbyMember
 * Method:    getMetadataNative
 * Signature: ()[Lnet/derfruhling/discord/socialsdk4j/StringPair;
 */
JNIEXPORT jobjectArray JNICALL
Java_net_derfruhling_discord_socialsdk4j_LobbyMember_getMetadataNative
(JNIEnv *env, jobject obj) {
    if (!lobbyMemberPtrF) lobbyMemberPtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::LobbyMemberHandle *member = (discordpp::LobbyMemberHandle *)env->GetLongField(obj, lobbyMemberPtrF);

    jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/StringPair");
    jmethodID methodId = env->GetMethodID(clazz, "<init>", "(JJ)V");

    std::unordered_map<std::string, std::string> map = member->Metadata();
    jobjectArray array = env->NewObjectArray(map.size(), clazz, nullptr);

    int i = 0;
    for (const auto &[key, value] : map) {
        jobject obj = env->NewObject(clazz, methodId, env->NewStringUTF(key.c_str()), env->NewStringUTF(value.c_str()));
        env->SetObjectArrayElement(array, i++, obj);
    }

    return array;
}
