#include <net_derfruhling_discord_socialsdk4j_User.h>
#include <discordpp.h>
#include <optional>
#include <utility>

#include "activity.hpp"

jfieldID userPtrF = nullptr;

JNIEXPORT jstring JNICALL
Java_net_derfruhling_discord_socialsdk4j_User_getAvatar
(JNIEnv *env, jobject obj) {
    if (!userPtrF) userPtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::UserHandle *user = (discordpp::UserHandle *)env->GetLongField(obj, userPtrF);

    std::optional<std::string> avatar = user->Avatar();

    return avatar.has_value()
        ? env->NewStringUTF(avatar.value().c_str())
        : nullptr;
}

JNIEXPORT jstring JNICALL
Java_net_derfruhling_discord_socialsdk4j_User_getDisplayName
(JNIEnv *env, jobject obj) {
    if (!userPtrF) userPtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::UserHandle *user = (discordpp::UserHandle *)env->GetLongField(obj, userPtrF);

    return env->NewStringUTF(user->DisplayName().c_str());
}

JNIEXPORT jobject JNICALL
Java_net_derfruhling_discord_socialsdk4j_User_getActivityInfo
(JNIEnv *env, jobject obj) {
    if (!userPtrF) userPtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::UserHandle *user = (discordpp::UserHandle *)env->GetLongField(obj, userPtrF);

    std::optional<discordpp::Activity> activity = user->GameActivity();
    return activity.has_value() ? CreateActivityInfo(env, activity.value()) : nullptr;
}

JNIEXPORT jstring JNICALL
Java_net_derfruhling_discord_socialsdk4j_User_getGlobalName
(JNIEnv *env, jobject obj) {
    if (!userPtrF) userPtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::UserHandle *user = (discordpp::UserHandle *)env->GetLongField(obj, userPtrF);

    std::optional<std::string> globalName = user->GlobalName();

    return globalName.has_value()
        ? env->NewStringUTF(globalName.value().c_str())
        : nullptr;
}

JNIEXPORT jboolean JNICALL
Java_net_derfruhling_discord_socialsdk4j_User_isProvisional
(JNIEnv *env, jobject obj) {
    if (!userPtrF) userPtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::UserHandle *user = (discordpp::UserHandle *)env->GetLongField(obj, userPtrF);

    return (jboolean)user->IsProvisional();
}

JNIEXPORT jobject JNICALL
Java_net_derfruhling_discord_socialsdk4j_User_getRelationship
(JNIEnv *env, jobject obj) {
    if (!userPtrF) userPtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::UserHandle *user = (discordpp::UserHandle *)env->GetLongField(obj, userPtrF);

    jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/Relationship");
    jmethodID cons = env->GetMethodID(clazz, "<init>", "(IIJJ)V");

    discordpp::RelationshipHandle rel = user->Relationship();
    return env->NewObject(clazz, cons,
        (jint) rel.DiscordRelationshipType(),
        (jint) rel.GameRelationshipType(),
        (jlong) rel.Id(),
        rel.User().has_value() ? (jlong) new discordpp::UserHandle(std::move(rel.User().value())) : 0);
}

JNIEXPORT jobject JNICALL
Java_net_derfruhling_discord_socialsdk4j_User_getStatus
(JNIEnv *env, jobject obj) {
    if (!userPtrF) userPtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::UserHandle *user = (discordpp::UserHandle *)env->GetLongField(obj, userPtrF);

    jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/UserStatus");
    jmethodID cons = env->GetStaticMethodID(clazz, "from", "(I)Lnet/derfruhling/discord/socialsdk4j/UserStatus;");
    return env->CallStaticObjectMethod(clazz, cons, (jint)user->Status());
}

JNIEXPORT jstring JNICALL
Java_net_derfruhling_discord_socialsdk4j_User_getUsername
(JNIEnv *env, jobject obj) {
    if (!userPtrF) userPtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::UserHandle *user = (discordpp::UserHandle *)env->GetLongField(obj, userPtrF);

    return env->NewStringUTF(user->Username().c_str());
}
