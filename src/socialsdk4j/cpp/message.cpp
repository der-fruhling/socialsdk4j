#include <algorithm>
#include <jni.h>
#include <discordpp.h>
#include <optional>
#include <vector>

jfieldID messagePtrF = nullptr;

JNIEXPORT jobject JNICALL
Java_net_derfruhling_discord_socialsdk4j_Message_getAdditionalContent
(JNIEnv *env, jobject obj) {
    if (!messagePtrF) messagePtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::MessageHandle *msg = (discordpp::MessageHandle *)env->GetLongField(obj, messagePtrF);

    std::optional<discordpp::AdditionalContent> content = msg->AdditionalContent();

    if(content) {
        jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/Message$AdditionalContent");
        jmethodID cons = env->GetMethodID(clazz, "<init>", "(ILjava/lang/String;I)V");

        std::optional<std::string> title = content->Title();

        return env->NewObject(clazz, cons,
            (jint)content->Type(),
            title ? env->NewStringUTF(title->c_str()) : nullptr,
            (jint)content->Count());
    } else {
        return nullptr;
    }
}

JNIEXPORT jobject JNICALL
Java_net_derfruhling_discord_socialsdk4j_Message_getAuthor
(JNIEnv *env, jobject obj) {
    if (!messagePtrF) messagePtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::MessageHandle *msg = (discordpp::MessageHandle *)env->GetLongField(obj, messagePtrF);

    std::optional<discordpp::UserHandle> user = msg->Author();
    if(!user) return nullptr;

    discordpp::UserHandle *handle = new discordpp::UserHandle(std::move(*user));

    jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/User");
    jmethodID method = env->GetMethodID(clazz, "<init>", "(JJ)V");
    return env->NewObject(clazz, method, (jlong)handle, (jlong)handle->Id());
}

JNIEXPORT jlong JNICALL
Java_net_derfruhling_discord_socialsdk4j_Message_getAuthorId
(JNIEnv *env, jobject obj) {
    if (!messagePtrF) messagePtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::MessageHandle *msg = (discordpp::MessageHandle *)env->GetLongField(obj, messagePtrF);

    return (jlong)msg->AuthorId();
}

JNIEXPORT jobject JNICALL
Java_net_derfruhling_discord_socialsdk4j_Message_getChannel
(JNIEnv *env, jobject obj) {
    if (!messagePtrF) messagePtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::MessageHandle *msg = (discordpp::MessageHandle *)env->GetLongField(obj, messagePtrF);

    std::optional<discordpp::ChannelHandle> channel = msg->Channel();
    if(!channel) return nullptr;

    std::vector<uint64_t> recipients = channel->Recipients();
    jlongArray recipientsArray = env->NewLongArray(recipients.size());
    env->SetLongArrayRegion(recipientsArray, 0, recipients.size(), (jlong*)recipients.data());

    jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/Channel");
    jmethodID method = env->GetMethodID(clazz, "<init>", "(JLjava/lang/String;[JI)V");
    return env->NewObject(clazz, method, (jlong)channel->Id(), env->NewStringUTF(channel->Name().c_str()), recipientsArray, (jint)channel->Type());
}

JNIEXPORT jlong JNICALL
Java_net_derfruhling_discord_socialsdk4j_Message_getChannelId
(JNIEnv *env, jobject obj) {
    if (!messagePtrF) messagePtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::MessageHandle *msg = (discordpp::MessageHandle *)env->GetLongField(obj, messagePtrF);

    return (jlong)msg->ChannelId();
}

JNIEXPORT jstring JNICALL
Java_net_derfruhling_discord_socialsdk4j_Message_getContent
(JNIEnv *env, jobject obj) {
    if (!messagePtrF) messagePtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::MessageHandle *msg = (discordpp::MessageHandle *)env->GetLongField(obj, messagePtrF);

    return env->NewStringUTF(msg->Content().c_str());
}

JNIEXPORT jint JNICALL
Java_net_derfruhling_discord_socialsdk4j_Message_getDisclosureTypeNative
(JNIEnv *env, jobject obj) {
    if (!messagePtrF) messagePtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::MessageHandle *msg = (discordpp::MessageHandle *)env->GetLongField(obj, messagePtrF);

    std::optional<discordpp::DisclosureTypes> disc = msg->DisclosureType();

    if(disc) {
        return (jint)*disc;
    } else {
        return -1;
    }
}

JNIEXPORT jlong JNICALL
Java_net_derfruhling_discord_socialsdk4j_Message_getEditedTimestamp
(JNIEnv *env, jobject obj) {
    if (!messagePtrF) messagePtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::MessageHandle *msg = (discordpp::MessageHandle *)env->GetLongField(obj, messagePtrF);

    return (jlong)msg->EditedTimestamp();
}

JNIEXPORT jobject JNICALL
Java_net_derfruhling_discord_socialsdk4j_Message_getLobby
(JNIEnv *env, jobject obj) {
    if (!messagePtrF) messagePtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::MessageHandle *msg = (discordpp::MessageHandle *)env->GetLongField(obj, messagePtrF);

    std::optional<discordpp::LobbyHandle> handle = msg->Lobby();

    if (handle.has_value()) {
        discordpp::LobbyHandle *lobby = new discordpp::LobbyHandle(std::move(handle.value()));
        jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/Lobby");
        jmethodID methodId = env->GetMethodID(clazz, "<init>", "(JJ)V");

        return env->NewObject(clazz, methodId, (jlong)lobby, (jlong)lobby->Id());
    } else {
        return nullptr;
    }
}

JNIEXPORT jobjectArray JNICALL
Java_net_derfruhling_discord_socialsdk4j_Message_getMetadataNative
(JNIEnv *env, jobject obj) {
    if (!messagePtrF) messagePtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::MessageHandle *msg = (discordpp::MessageHandle *)env->GetLongField(obj, messagePtrF);

    jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/StringPair");
    jmethodID methodId = env->GetMethodID(clazz, "<init>", "(Ljava/lang/String;Ljava/lang/String;)V");

    std::unordered_map<std::string, std::string> map = msg->Metadata();
    jobjectArray array = env->NewObjectArray(map.size(), clazz, nullptr);

    int i = 0;
    for (const auto &[key, value] : map) {
        jobject obj = env->NewObject(clazz, methodId, env->NewStringUTF(key.c_str()), env->NewStringUTF(value.c_str()));
        env->SetObjectArrayElement(array, i++, obj);
    }

    return array;
}

JNIEXPORT jstring JNICALL
Java_net_derfruhling_discord_socialsdk4j_Message_getRawContent
(JNIEnv *env, jobject obj) {
    if (!messagePtrF) messagePtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::MessageHandle *msg = (discordpp::MessageHandle *)env->GetLongField(obj, messagePtrF);

    return env->NewStringUTF(msg->RawContent().c_str());
}

JNIEXPORT jobject JNICALL
Java_net_derfruhling_discord_socialsdk4j_Message_getRecipient
(JNIEnv *env, jobject obj) {
    if (!messagePtrF) messagePtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::MessageHandle *msg = (discordpp::MessageHandle *)env->GetLongField(obj, messagePtrF);

    std::optional<discordpp::UserHandle> user = msg->Recipient();
    if(!user) return nullptr;

    discordpp::UserHandle *handle = new discordpp::UserHandle(std::move(*user));

    jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/User");
    jmethodID method = env->GetMethodID(clazz, "<init>", "(JJ)V");
    return env->NewObject(clazz, method, (jlong)handle, (jlong)handle->Id());
}

JNIEXPORT jlong JNICALL
Java_net_derfruhling_discord_socialsdk4j_Message_getRecipientId
(JNIEnv *env, jobject obj) {
    if (!messagePtrF) messagePtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::MessageHandle *msg = (discordpp::MessageHandle *)env->GetLongField(obj, messagePtrF);

    return (jlong)msg->RecipientId();
}

JNIEXPORT jboolean JNICALL
Java_net_derfruhling_discord_socialsdk4j_Message_isSentFromGame
(JNIEnv *env, jobject obj) {
    if (!messagePtrF) messagePtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::MessageHandle *msg = (discordpp::MessageHandle *)env->GetLongField(obj, messagePtrF);

    return (jboolean)msg->SentFromGame();
}

JNIEXPORT jlong JNICALL
Java_net_derfruhling_discord_socialsdk4j_Message_getSentTimestamp
(JNIEnv *env, jobject obj) {
    if (!messagePtrF) messagePtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::MessageHandle *msg = (discordpp::MessageHandle *)env->GetLongField(obj, messagePtrF);

    return (jlong)msg->SentTimestamp();
}
