#include <net_derfruhling_discord_socialsdk4j_Lobby.h>
#include <discordpp.h>
#include <optional>
#include <unordered_map>
#include <vector>

jfieldID lobbyPtrF = nullptr;

JNIEXPORT jobject JNICALL
Java_net_derfruhling_discord_socialsdk4j_Lobby_getCallInfo
(JNIEnv *env, jobject obj) {
    if (!lobbyPtrF) lobbyPtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::LobbyHandle *lobby = (discordpp::LobbyHandle *)env->GetLongField(obj, lobbyPtrF);

    std::optional<discordpp::CallInfoHandle> optional = lobby->GetCallInfoHandle();

    if (optional.has_value()) {
        discordpp::CallInfoHandle callInfo = optional.value();
        jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/CallInfo");
        jmethodID methodId = env->GetMethodID(clazz, "<init>", "(JJ[J[Lnet/derfruhling/discord/socialsdk4j/VoiceStatePair;)V");

        std::vector<uint64_t> participants = callInfo.GetParticipants();
        jlongArray participantsArray = env->NewLongArray(participants.size());
        env->SetLongArrayRegion(participantsArray, 0, participants.size(), (jlong*)participants.data());

        jclass clazzPair = env->FindClass("net/derfruhling/discord/socialsdk4j/CallInfo$VoiceStatePair");
        jmethodID methodPairId = env->GetMethodID(clazz, "<init>", "(JZZ)V");
        jobjectArray pairArray = env->NewObjectArray(participants.size(), clazzPair, nullptr);

        int i = 0;
        for(const auto participantId : participants) {
            discordpp::VoiceStateHandle handle = callInfo.GetVoiceStateHandle(participantId).value();
            env->SetObjectArrayElement(pairArray, i++, env->NewObject(clazzPair, methodPairId,
                (jlong)participantId,
                (jboolean)handle.SelfDeaf(),
                (jboolean)handle.SelfMute()));
        }

        return env->NewObject(clazz, methodId,
            (jlong)callInfo.ChannelId(),
            (jlong)callInfo.GuildId(),
            participantsArray,
            pairArray);
    } else {
        return nullptr;
    }
}

JNIEXPORT jobject JNICALL
Java_net_derfruhling_discord_socialsdk4j_Lobby_getMember
(JNIEnv *env, jobject obj, jlong id) {
    if (!lobbyPtrF) lobbyPtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::LobbyHandle *lobby = (discordpp::LobbyHandle *)env->GetLongField(obj, lobbyPtrF);

    std::optional<discordpp::LobbyMemberHandle> handle = lobby->GetLobbyMemberHandle((uint64_t)id);

    if(handle.has_value()) {
        discordpp::LobbyMemberHandle *member = new discordpp::LobbyMemberHandle(std::move(handle.value()));

        jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/LobbyMember");
        jmethodID cons = env->GetMethodID(clazz, "<init>", "(JJ)V");

        return env->NewObject(clazz, cons, (jlong)member, (jlong)member->Id());
    } else {
        return nullptr;
    }
}

JNIEXPORT jobject JNICALL
Java_net_derfruhling_discord_socialsdk4j_Lobby_getLinkedChannel
(JNIEnv *env, jobject obj) {
    if (!lobbyPtrF) lobbyPtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::LobbyHandle *lobby = (discordpp::LobbyHandle *)env->GetLongField(obj, lobbyPtrF);

    std::optional<discordpp::LinkedChannel> link = lobby->LinkedChannel();

    if(link.has_value()) {
        jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/LinkedChannel");
        jmethodID cons = env->GetMethodID(clazz, "<init>", "(JLjava/lang/String;J)V");

        return env->NewObject(clazz, cons, (jlong)link->Id(), env->NewStringUTF(link->Name().c_str()), (jlong)link->GuildId());
    } else {
        return nullptr;
    }
}

JNIEXPORT jlongArray JNICALL
Java_net_derfruhling_discord_socialsdk4j_Lobby_getLobbyMemberIds
(JNIEnv *env, jobject obj) {
    if (!lobbyPtrF) lobbyPtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::LobbyHandle *lobby = (discordpp::LobbyHandle *)env->GetLongField(obj, lobbyPtrF);

    std::vector<uint64_t> members = lobby->LobbyMemberIds();
    jlongArray array = env->NewLongArray(members.size());
    env->SetLongArrayRegion(array, 0, members.size(), (jlong*)members.data());

    return array;
}

JNIEXPORT jobjectArray JNICALL
Java_net_derfruhling_discord_socialsdk4j_Lobby_getLobbyMembers
(JNIEnv *env, jobject obj) {
    if (!lobbyPtrF) lobbyPtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::LobbyHandle *lobby = (discordpp::LobbyHandle *)env->GetLongField(obj, lobbyPtrF);

    std::vector<discordpp::LobbyMemberHandle> members = lobby->LobbyMembers();

    jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/LobbyMember");
    jmethodID cons = env->GetMethodID(clazz, "<init>", "(JJ)V");
    jobjectArray array = env->NewObjectArray(members.size(), clazz, nullptr);

    int i = 0;
    for(auto &member : members) {
        discordpp::LobbyMemberHandle *p = new discordpp::LobbyMemberHandle(std::move(member));
        env->SetObjectArrayElement(array, i++, env->NewObject(clazz, cons, (jlong)p, (jlong)p->Id()));
    }

    return array;
}

/*
 * Class:     net_derfruhling_discord_socialsdk4j_Lobby
 * Method:    getMetadataNative
 * Signature: ()[Lnet/derfruhling/discord/socialsdk4j/StringPair;
 */
JNIEXPORT jobjectArray JNICALL
Java_net_derfruhling_discord_socialsdk4j_Lobby_getMetadataNative
(JNIEnv *env, jobject obj) {
    if (!lobbyPtrF) lobbyPtrF = env->GetFieldID(env->GetObjectClass(obj), "pointer", "J");
    discordpp::LobbyHandle *lobby = (discordpp::LobbyHandle *)env->GetLongField(obj, lobbyPtrF);

    jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/StringPair");
    jmethodID methodId = env->GetMethodID(clazz, "<init>", "(Ljava/lang/String;Ljava/lang/String;)V");

    std::unordered_map<std::string, std::string> map = lobby->Metadata();
    jobjectArray array = env->NewObjectArray(map.size(), clazz, nullptr);

    int i = 0;
    for (const auto &[key, value] : map) {
        jobject obj = env->NewObject(clazz, methodId, env->NewStringUTF(key.c_str()), env->NewStringUTF(value.c_str()));
        env->SetObjectArrayElement(array, i++, obj);
    }

    return array;
}
