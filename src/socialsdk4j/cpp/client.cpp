#include <net_derfruhling_discord_socialsdk4j_Client.h>
#include <discordpp.h>
#include <optional>
#include <unordered_map>
#include <utility>
#include <vector>
#include "callbacks.hpp"
#include "client_result.hpp"

JNIEXPORT jobject JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_createAuthorizationCodeVerifierNative
(JNIEnv *env, jclass, jlong ptr) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    discordpp::AuthorizationCodeVerifier v = client->CreateAuthorizationCodeVerifier();

    discordpp::AuthorizationCodeChallenge *challengeValue = new discordpp::AuthorizationCodeChallenge(std::move(v.Challenge()));
    jstring verifier = env->NewStringUTF(v.Verifier().c_str());

    jclass challengeCls = env->FindClass("net/derfruhling/discord/socialsdk4j/CodeChallenge");
    jmethodID challengeCons = env->GetMethodID(challengeCls, "<init>", "(J)V");
    jobject challenge = env->NewObject(challengeCls, challengeCons, reinterpret_cast<jlong>(challengeValue));

    jclass verifierCls = env->FindClass("net/derfruhling/discord/socialsdk4j/CodeVerifier");
    jmethodID verifierCons = env->GetMethodID(verifierCls, "<init>", "(Lnet/derfruhling/discord/socialsdk4j/CodeChallenge;Ljava/lang/String;)V");
    jobject verifierObj = env->NewObject(verifierCls, verifierCons, challenge, verifier);

    return verifierObj;
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_authorizeNative
(JNIEnv *env, jclass, jlong ptr, jlong clientId, jstring scopes, jstring state, jlong codeChallenge, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    discordpp::AuthorizationArgs args;
    args.SetClientId(clientId);
    args.SetScopes(discordpp::Client::GetDefaultCommunicationScopes());
    args.SetCodeChallenge(*reinterpret_cast<discordpp::AuthorizationCodeChallenge *>(codeChallenge));

    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;Ljava/lang/String;Ljava/lang/String;)V");
    client->Authorize(args, [method, callback](discordpp::ClientResult res, std::string code, std::string redirectUri) {
        if(!res.Successful()) {
            cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)), nullptr, nullptr);
        } else {
            cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)), cbenv->NewStringUTF(code.c_str()), cbenv->NewStringUTF(redirectUri.c_str()));
        }

        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_getProvisionalTokenNative
(JNIEnv *env, jclass, jlong ptr, jlong applicationId, jint externalAuthType, jstring token, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    const char *tokenS = env->GetStringUTFChars(token, nullptr);

    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;Ljava/lang/String;Ljava/lang/String;IILjava/lang/String;)V");
    client->GetProvisionalToken(applicationId, static_cast<discordpp::AuthenticationExternalAuthType>(externalAuthType), tokenS, [token, tokenS, method, callback](discordpp::ClientResult res, std::string accessToken, std::string refreshToken, discordpp::AuthorizationTokenType type, int expiresIn, std::string scopes) {
        if (!res.Successful()) {
            cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)), nullptr, nullptr, 0, 0, nullptr);
        } else {
            cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)), cbenv->NewStringUTF(accessToken.c_str()), cbenv->NewStringUTF(refreshToken.c_str()), (jint)type, expiresIn, cbenv->NewStringUTF(scopes.c_str()));
        }

        cbenv->ReleaseStringUTFChars(token, tokenS);
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_getTokenNative
(JNIEnv *env, jclass, jlong ptr, jlong applicationId, jstring code, jstring codeVerifier, jstring redirectUri, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    const char *codeS = env->GetStringUTFChars(code, nullptr);
    const char *codeVerifierS = env->GetStringUTFChars(codeVerifier, nullptr);
    const char *redirectUriS = env->GetStringUTFChars(redirectUri, nullptr);

    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;Ljava/lang/String;Ljava/lang/String;IILjava/lang/String;)V");
    client->GetToken(applicationId, codeS, codeVerifierS, redirectUriS, [codeS, code, codeVerifierS, codeVerifier, redirectUriS, redirectUri, method, callback](discordpp::ClientResult res, std::string accessToken, std::string refreshToken, discordpp::AuthorizationTokenType type, int expiresIn, std::string scopes) {
        if (!res.Successful()) {
            cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)), nullptr, nullptr, 0, 0, nullptr);
        } else {
            cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)), cbenv->NewStringUTF(accessToken.c_str()), cbenv->NewStringUTF(refreshToken.c_str()), (jint)type, expiresIn, cbenv->NewStringUTF(scopes.c_str()));
        }

        cbenv->ReleaseStringUTFChars(code, codeS);
        cbenv->ReleaseStringUTFChars(codeVerifier, codeVerifierS);
        cbenv->ReleaseStringUTFChars(redirectUri, redirectUriS);
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_getTokenFromProvisionalMergeNative
(JNIEnv *env, jclass, jlong ptr, jlong applicationId, jstring code, jstring codeVerifier, jstring redirectUri, jint externalAuthType, jstring token, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    const char *codeS = env->GetStringUTFChars(code, nullptr);
    const char *codeVerifierS = env->GetStringUTFChars(codeVerifier, nullptr);
    const char *redirectUriS = env->GetStringUTFChars(redirectUri, nullptr);
    const char *tokenS = env->GetStringUTFChars(token, nullptr);

    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;Ljava/lang/String;Ljava/lang/String;IILjava/lang/String;)V");
    client->GetTokenFromProvisionalMerge(applicationId, codeS, codeVerifierS, redirectUriS, static_cast<discordpp::AuthenticationExternalAuthType>(externalAuthType), tokenS, [tokenS, token, codeS, code, codeVerifierS, codeVerifier, redirectUriS, redirectUri, method, callback](discordpp::ClientResult res, std::string accessToken, std::string refreshToken, discordpp::AuthorizationTokenType type, int expiresIn, std::string scopes) {
        if (!res.Successful()) {
            cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)), nullptr, nullptr, 0, 0, nullptr);
        } else {
            cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)), cbenv->NewStringUTF(accessToken.c_str()), cbenv->NewStringUTF(refreshToken.c_str()), (jint)type, expiresIn, cbenv->NewStringUTF(scopes.c_str()));
        }

        cbenv->ReleaseStringUTFChars(code, codeS);
        cbenv->ReleaseStringUTFChars(codeVerifier, codeVerifierS);
        cbenv->ReleaseStringUTFChars(redirectUri, redirectUriS);
        cbenv->ReleaseStringUTFChars(token, tokenS);
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_setStatusChangedCallbackNative
(JNIEnv *env, jclass, jlong ptr, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(III)V");
    client->SetStatusChangedCallback([callback, method](discordpp::Client::Status status, discordpp::Client::Error error, int32_t errorDetail) {
        cbenv->CallVoidMethod(callback, method, (jint)status, (jint)error, (jint)errorDetail);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_updateTokenNative
(JNIEnv *env, jclass, jlong ptr, jint type, jstring token, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    const char *tokenS = env->GetStringUTFChars(token, nullptr);

    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->UpdateToken(static_cast<discordpp::AuthorizationTokenType>(type), tokenS, [tokenS, token, method, callback](discordpp::ClientResult res) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)));
        cbenv->ReleaseStringUTFChars(token, tokenS);
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_connectNative
(JNIEnv *, jclass, jlong ptr) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    client->Connect();
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_disconnectNative
(JNIEnv *, jclass, jlong ptr) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    client->Disconnect();
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_abortAuthorizeNative
(JNIEnv *, jclass, jlong ptr) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    client->AbortAuthorize();
}

JNIEXPORT jboolean JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_isAuthenticatedNative
(JNIEnv *, jclass, jlong ptr) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    return (jboolean)client->IsAuthenticated();
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_provisionalMergeCompletedNative
(JNIEnv *, jclass, jlong ptr, jboolean success) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    client->ProvisionalUserMergeCompleted(success);
}

JNIEXPORT void JNICALL Java_net_derfruhling_discord_socialsdk4j_Client_refreshTokenNative
(JNIEnv *env, jclass, jlong ptr, jlong applicationId, jstring token, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    const char *tokenS = env->GetStringUTFChars(token, nullptr);

    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;Ljava/lang/String;Ljava/lang/String;IILjava/lang/String;)V");
    client->RefreshToken(applicationId, tokenS, [tokenS, token, method, callback](discordpp::ClientResult res, std::string accessToken, std::string refreshToken, discordpp::AuthorizationTokenType type, int expiresIn, std::string scopes) {
        if (!res.Successful()) {
            cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)), nullptr, nullptr, 0, 0, nullptr);
        } else {
            cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)), cbenv->NewStringUTF(accessToken.c_str()), cbenv->NewStringUTF(refreshToken.c_str()), (jint)type, expiresIn, cbenv->NewStringUTF(scopes.c_str()));
        }

        cbenv->ReleaseStringUTFChars(token, tokenS);
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_openConnectedGameSettingsInDiscordNative
(JNIEnv *env, jclass, jlong ptr, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->OpenConnectedGamesSettingsInDiscord([method, callback](discordpp::ClientResult res) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)));
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_setGameWindowPidNative
(JNIEnv *, jclass, jlong ptr, jint pid) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    client->SetGameWindowPid(pid);
}

JNIEXPORT jobject JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_getRelationshipNative
(JNIEnv *env, jclass, jlong ptr, jlong userId) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/Relationship");
    jmethodID cons = env->GetMethodID(clazz, "<init>", "(IIJJ)V");

    discordpp::RelationshipHandle rel = client->GetRelationshipHandle((uint64_t)userId);
    return env->NewObject(clazz, cons,
        (jint) rel.DiscordRelationshipType(),
        (jint) rel.GameRelationshipType(),
        (jlong) rel.Id(),
        rel.User().has_value() ? (jlong) new discordpp::UserHandle(std::move(rel.User().value())) : 0);
}

JNIEXPORT jobjectArray JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_getRelationshipsNative
(JNIEnv *env, jclass, jlong ptr) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/Relationship");
    jmethodID cons = env->GetMethodID(clazz, "<init>", "(IIJJ)V");

    std::vector<discordpp::RelationshipHandle> rels = client->GetRelationships();
    jobjectArray array = env->NewObjectArray(rels.size(), clazz, nullptr);

    for (int i = 0; i < rels.size(); i++) {
        const discordpp::RelationshipHandle &rel = rels[i];
        jobject obj = env->NewObject(clazz, cons,
            (jint) rel.DiscordRelationshipType(),
            (jint) rel.GameRelationshipType(),
            (jlong) rel.Id(),
            rel.User().has_value() ? (jlong) new discordpp::UserHandle(std::move(rel.User().value())) : 0);

        env->SetObjectArrayElement(array, i, obj);
    }

    return array;
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_sendDiscordFriendRequestNative
(JNIEnv *env, jclass, jlong ptr, jstring username, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    const char *usernameS = env->GetStringUTFChars(username, nullptr);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->SendDiscordFriendRequest(usernameS, [username, usernameS, callback, method](discordpp::ClientResult result) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(result)));
        cbenv->ReleaseStringUTFChars(username, usernameS);
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_sendDiscordFriendRequestByIdNative
(JNIEnv *env, jclass, jlong ptr, jlong userId, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->SendDiscordFriendRequestById((uint64_t)userId, [callback, method](discordpp::ClientResult result) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(result)));
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_sendGameFriendRequestNative
(JNIEnv *env, jclass, jlong ptr, jstring username, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    const char *usernameS = env->GetStringUTFChars(username, nullptr);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->SendGameFriendRequest(usernameS, [username, usernameS, callback, method](discordpp::ClientResult result) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(result)));
        cbenv->ReleaseStringUTFChars(username, usernameS);
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_sendGameFriendRequestByIdNative
(JNIEnv *env, jclass, jlong ptr, jlong userId, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->SendGameFriendRequestById((uint64_t)userId, [callback, method](discordpp::ClientResult result) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(result)));
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_acceptDiscordFriendRequestNative
(JNIEnv *env, jclass, jlong ptr, jlong userId, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->AcceptDiscordFriendRequest((uint64_t)userId, [callback, method](discordpp::ClientResult result) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(result)));
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_acceptGameFriendRequestNative
(JNIEnv *env, jclass, jlong ptr, jlong userId, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->AcceptGameFriendRequest((uint64_t)userId, [callback, method](discordpp::ClientResult result) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(result)));
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_cancelDiscordFriendRequestNative
(JNIEnv *env, jclass, jlong ptr, jlong userId, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->CancelDiscordFriendRequest((uint64_t)userId, [callback, method](discordpp::ClientResult result) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(result)));
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_cancelGameFriendRequestNative
(JNIEnv *env, jclass, jlong ptr, jlong userId, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->CancelGameFriendRequest((uint64_t)userId, [callback, method](discordpp::ClientResult result) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(result)));
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_setRelationshipCreatedCallbackNative
(JNIEnv *env, jclass, jlong ptr, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(JZ)V");
    client->SetRelationshipCreatedCallback([callback, method](uint64_t userId, bool isDiscordRelationshipUpdate) {
        cbenv->CallVoidMethod(callback, method, (jlong)userId, (jboolean)isDiscordRelationshipUpdate);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_setRelationshipDeletedCallbackNative
(JNIEnv *env, jclass, jlong ptr, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(JZ)V");
    client->SetRelationshipDeletedCallback([callback, method](uint64_t userId, bool isDiscordRelationshipUpdate) {
        cbenv->CallVoidMethod(callback, method, (jlong)userId, (jboolean)isDiscordRelationshipUpdate);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_rejectDiscordFriendRequestNative
(JNIEnv *env, jclass, jlong ptr, jlong userId, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->RejectDiscordFriendRequest((uint64_t)userId, [callback, method](discordpp::ClientResult result) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(result)));
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_rejectGameFriendRequestNative
(JNIEnv *env, jclass, jlong ptr, jlong userId, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->RejectGameFriendRequest((uint64_t)userId, [callback, method](discordpp::ClientResult result) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(result)));
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_removeDiscordAndGameFriendNative
(JNIEnv *env, jclass, jlong ptr, jlong userId, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->RemoveDiscordAndGameFriend((uint64_t)userId, [callback, method](discordpp::ClientResult result) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(result)));
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_removeGameFriendNative
(JNIEnv *env, jclass, jlong ptr, jlong userId, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->RemoveGameFriend((uint64_t)userId, [callback, method](discordpp::ClientResult result) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(result)));
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL Java_net_derfruhling_discord_socialsdk4j_Client_blockUserNative
(JNIEnv *env, jclass, jlong ptr, jlong userId, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->BlockUser((uint64_t)userId, [callback, method](discordpp::ClientResult result) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(result)));
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL Java_net_derfruhling_discord_socialsdk4j_Client_unblockUserNative
(JNIEnv *env, jclass, jlong ptr, jlong userId, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->UnblockUser((uint64_t)userId, [callback, method](discordpp::ClientResult result) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(result)));
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_updateRichPresenceNative
(JNIEnv *env, jclass, jlong ptr, jlong activityPtr, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    discordpp::Activity *activity = reinterpret_cast<discordpp::Activity *>(activityPtr);

    if(callback) {
        callback = env->NewGlobalRef(callback);

        jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
        client->UpdateRichPresence(*activity, [callback, method](discordpp::ClientResult result) {
            cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(result)));
            cbenv->DeleteGlobalRef(callback);
        });
    } else {
        client->UpdateRichPresence(*activity, [](discordpp::ClientResult) {});
    }
}

JNIEXPORT jobject JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_getCurrentUserNative
(JNIEnv *env, jclass, jlong ptr) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    discordpp::UserHandle *handle = new discordpp::UserHandle(std::move(client->GetCurrentUser()));

    jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/User");
    jmethodID method = env->GetMethodID(clazz, "<init>", "(JJ)V");
    return env->NewObject(clazz, method, (jlong)handle, (jlong)handle->Id());
}

JNIEXPORT jobject JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_getUserNative
(JNIEnv *env, jclass, jlong ptr, jlong userId) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    std::optional<discordpp::UserHandle> h = client->GetUser((uint64_t)userId);

    if(h.has_value()) {
        discordpp::UserHandle *handle = new discordpp::UserHandle(std::move(h.value()));

        jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/User");
        jmethodID method = env->GetMethodID(clazz, "<init>", "(JJ)V");
        return env->NewObject(clazz, method, (jlong)handle, (jlong)handle->Id());
    } else {
        return nullptr;
    }
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_getUserGuildsNative
(JNIEnv *env, jclass, jlong ptr, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;[Lnet/derfruhling/discord/socialsdk4j/GuildMinimal;)V");
    client->GetUserGuilds([method, callback](discordpp::ClientResult res, std::vector<discordpp::GuildMinimal> guilds) {
        jclass clazz = cbenv->FindClass("net/derfruhling/discord/socialsdk4j/GuildMinimal");
        jobjectArray array = cbenv->NewObjectArray(guilds.size(), clazz, nullptr);

        jmethodID guildCons = cbenv->GetMethodID(clazz, "<init>", "(JLjava/lang/String;)V");

        int size = guilds.size();
        for (int i = 0; i < size; i++) {
            const discordpp::GuildMinimal &guild = guilds[i];
            jstring s = cbenv->NewStringUTF(guild.Name().c_str());
            jobject obj = cbenv->NewObject(clazz, guildCons, (jlong)guild.Id(), s);
            cbenv->SetObjectArrayElement(array, i, obj);
        }

        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)), array);
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_getGuildChannelsNative
(JNIEnv *env, jclass, jlong ptr, jlong guildId, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;[Lnet/derfruhling/discord/socialsdk4j/GuildChannel;)V");
    client->GetGuildChannels((uint64_t)guildId, [method, callback](discordpp::ClientResult res, std::vector<discordpp::GuildChannel> channels) {
        jclass clazz = cbenv->FindClass("net/derfruhling/discord/socialsdk4j/GuildChannel");
        jclass clazzLinkedLobby = cbenv->FindClass("net/derfruhling/discord/socialsdk4j/LinkedLobby");
        jobjectArray array = cbenv->NewObjectArray(channels.size(), clazz, nullptr);
        jmethodID channelCons = cbenv->GetMethodID(clazz, "<init>", "(JLjava/lang/String;ZZLnet/derfruhling/discord/socialsdk4j/LinkedLobby;)V");
        jmethodID linkedLobbyCons = cbenv->GetMethodID(clazzLinkedLobby, "<init>", "(JJ)V");

        int size = channels.size();
        for (int i = 0; i < size; i++) {
            const discordpp::GuildChannel &channel = channels[i];
            jstring s = cbenv->NewStringUTF(channel.Name().c_str());
            jobject linkedLobby = nullptr;

            std::optional<discordpp::LinkedLobby> lobby = channel.LinkedLobby();

            if (lobby.has_value()) {
                discordpp::LinkedLobby v = lobby.value();
                linkedLobby = cbenv->NewObject(clazzLinkedLobby, linkedLobbyCons, (jlong)v.ApplicationId(), (jlong)v.LobbyId());
            }

            jobject obj = cbenv->NewObject(clazz, channelCons, (jlong)channel.Id(), s, (jboolean)channel.IsLinkable(), (jboolean)channel.IsViewableAndWriteableByAllMembers(), linkedLobby);
            cbenv->SetObjectArrayElement(array, i, obj);
        }

        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)), array);
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_createOrJoinLobbyNative
(JNIEnv *env, jclass, jlong ptr, jstring secret, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    const char *secretS = env->GetStringUTFChars(secret, nullptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;J)V");
    client->CreateOrJoinLobby(secretS, [secret, secretS, method, callback](discordpp::ClientResult res, uint64_t lobbyId) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)), (jlong)lobbyId);
        cbenv->ReleaseStringUTFChars(secret, secretS);
        cbenv->DeleteGlobalRef(callback);
    });
}

std::pair<std::string, std::string> pairFromJava(JNIEnv *env, jclass pairClazz, jobject obj) {
    jfieldID keyId = env->GetFieldID(pairClazz, "key", "Ljava/lang/String;");
    jfieldID valueId = env->GetFieldID(pairClazz, "value", "Ljava/lang/String;");

    jstring key = (jstring)env->GetObjectField(obj, keyId);
    jstring value = (jstring)env->GetObjectField(obj, valueId);

    const char *keyS = env->GetStringUTFChars(key, nullptr);
    const char *valueS = env->GetStringUTFChars(value, nullptr);

    return std::pair(keyS, valueS);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_createOrJoinLobbyWithMetadataNative
(JNIEnv *env, jclass, jlong ptr, jstring secret, jobjectArray lobbyMeta, jobjectArray memberMeta, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    const char *secretS = env->GetStringUTFChars(secret, nullptr);
    callback = env->NewGlobalRef(callback);

    std::unordered_map<std::string, std::string> lobbyMetadata, memberMetadata;

    jclass pairClazz = env->FindClass("net/derfruhling/discord/socialsdk4j/StringPair");

    int lobbyMetaSize = env->GetArrayLength(lobbyMeta);
    for (int i = 0; i < lobbyMetaSize; i++) {
        jobject obj = env->GetObjectArrayElement(lobbyMeta, i);
        lobbyMetadata.emplace(pairFromJava(env, pairClazz, obj));
    }

    int memberMetaSize = env->GetArrayLength(memberMeta);
    for (int i = 0; i < memberMetaSize; i++) {
        jobject obj = env->GetObjectArrayElement(memberMeta, i);
        memberMetadata.emplace(pairFromJava(env, pairClazz, obj));
    }

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;J)V");
    client->CreateOrJoinLobbyWithMetadata(secretS, lobbyMetadata, memberMetadata, [secret, secretS, method, callback](discordpp::ClientResult res, uint64_t lobbyId) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)), (jlong)lobbyId);
        cbenv->ReleaseStringUTFChars(secret, secretS);
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_leaveLobbyNative
(JNIEnv *env, jclass, jlong ptr, jlong lobbyId, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->LeaveLobby(lobbyId, [method, callback](discordpp::ClientResult res) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)));
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_setLobbyCreatedCallbackNative
(JNIEnv *env, jclass, jlong ptr, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(J)V");
    client->SetLobbyCreatedCallback([method, callback](uint64_t lobbyId) {
        cbenv->CallVoidMethod(callback, method, (jlong)lobbyId);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_setLobbyDeletedCallbackNative
(JNIEnv *env, jclass, jlong ptr, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(J)V");
    client->SetLobbyDeletedCallback([method, callback](uint64_t lobbyId) {
        cbenv->CallVoidMethod(callback, method, (jlong)lobbyId);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_setLobbyUpdatedCallbackNative
(JNIEnv *env, jclass, jlong ptr, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(J)V");
    client->SetLobbyUpdatedCallback([method, callback](uint64_t lobbyId) {
        cbenv->CallVoidMethod(callback, method, (jlong)lobbyId);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_setLobbyMemberAddedCallbackNative
(JNIEnv *env, jclass, jlong ptr, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(JJ)V");
    client->SetLobbyMemberAddedCallback([method, callback](uint64_t lobbyId, uint64_t memberId) {
        cbenv->CallVoidMethod(callback, method, (jlong)lobbyId, (jlong)memberId);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_setLobbyMemberRemovedCallbackNative
(JNIEnv *env, jclass, jlong ptr, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(JJ)V");
    client->SetLobbyMemberRemovedCallback([method, callback](uint64_t lobbyId, uint64_t memberId) {
        cbenv->CallVoidMethod(callback, method, (jlong)lobbyId, (jlong)memberId);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_setLobbyMemberUpdatedCallbackNative
(JNIEnv *env, jclass, jlong ptr, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(JJ)V");
    client->SetLobbyMemberUpdatedCallback([method, callback](uint64_t lobbyId, uint64_t memberId) {
        cbenv->CallVoidMethod(callback, method, (jlong)lobbyId, (jlong)memberId);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_setMessageCreatedCallbackNative
(JNIEnv *env, jclass, jlong ptr, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(J)V");
    client->SetMessageCreatedCallback([method, callback](uint64_t messageId) {
        cbenv->CallVoidMethod(callback, method, (jlong)messageId);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_setMessageDeletedCallbackNative
(JNIEnv *env, jclass, jlong ptr, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(JJ)V");
    client->SetMessageDeletedCallback([method, callback](uint64_t messageId, uint64_t channelId) {
        cbenv->CallVoidMethod(callback, method, (jlong)messageId, (jlong)channelId);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_setMessageUpdatedCallbackNative
(JNIEnv *env, jclass, jlong ptr, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(J)V");
    client->SetMessageUpdatedCallback([method, callback](uint64_t messageId) {
        cbenv->CallVoidMethod(callback, method, (jlong)messageId);
    });
}

JNIEXPORT jobject JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_getMessageNative
(JNIEnv *env, jclass, jlong ptr, jlong messageId) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    std::optional<discordpp::MessageHandle> msgOpt = client->GetMessageHandle((uint64_t)messageId);
    if(msgOpt) {
        discordpp::MessageHandle *msg = new discordpp::MessageHandle(std::move(*msgOpt));
        jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/Message");
        jmethodID cons = env->GetMethodID(clazz, "<init>", "(JJ)V");

        return env->NewObject(clazz, cons, (jlong)msg, (jlong)msg->Id());
    } else {
        return nullptr;
    }
}

JNIEXPORT jobject JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_getLobbyNative
(JNIEnv *env, jclass, jlong ptr, jlong lobbyId) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);

    std::optional<discordpp::LobbyHandle> handle = client->GetLobbyHandle((uint64_t)lobbyId);

    if (handle.has_value()) {
        discordpp::LobbyHandle *lobby = new discordpp::LobbyHandle(std::move(handle.value()));
        jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/Lobby");
        jmethodID methodId = env->GetMethodID(clazz, "<init>", "(JJ)V");

        return env->NewObject(clazz, methodId, (jlong)lobby, (jlong)lobby->Id());
    } else {
        return nullptr;
    }
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_linkChannelToLobbyNative
(JNIEnv *env, jclass, jlong ptr, jlong lobbyId, jlong channelId, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->LinkChannelToLobby(lobbyId, channelId, [method, callback](discordpp::ClientResult res) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)));
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_unlinkChannelFromLobbyNative
(JNIEnv *env, jclass, jlong ptr, jlong lobbyId, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->UnlinkChannelFromLobby(lobbyId, [method, callback](discordpp::ClientResult res) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)));
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_sendLobbyMessageNative
(JNIEnv *env, jclass, jlong ptr, jlong lobbyId, jstring message, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    const char *messageS = env->GetStringUTFChars(message, nullptr);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;J)V");
    client->SendLobbyMessage(lobbyId, messageS, [message, messageS, method, callback](discordpp::ClientResult res, uint64_t messageId) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)), (jlong)messageId);
        cbenv->DeleteGlobalRef(callback);
        cbenv->ReleaseStringUTFChars(message, messageS);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_sendLobbyMessageWithMetadataNative
(JNIEnv *env, jclass, jlong ptr, jlong lobbyId, jstring message, jobjectArray meta, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    const char *messageS = env->GetStringUTFChars(message, nullptr);

    std::unordered_map<std::string, std::string> metadata;

    jclass pairClazz = env->FindClass("net/derfruhling/discord/socialsdk4j/StringPair");

    int metaSize = env->GetArrayLength(meta);
    for (int i = 0; i < metaSize; i++) {
        jobject obj = env->GetObjectArrayElement(meta, i);
        metadata.emplace(pairFromJava(env, pairClazz, obj));
    }

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;J)V");
    client->SendLobbyMessageWithMetadata(lobbyId, messageS, metadata, [message, messageS, method, callback](discordpp::ClientResult res, uint64_t messageId) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)), (jlong)messageId);
        cbenv->DeleteGlobalRef(callback);
        cbenv->ReleaseStringUTFChars(message, messageS);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_sendUserMessageNative
(JNIEnv *env, jclass, jlong ptr, jlong userId, jstring message, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    const char *messageS = env->GetStringUTFChars(message, nullptr);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;J)V");
    client->SendUserMessage(userId, messageS, [message, messageS, method, callback](discordpp::ClientResult res, uint64_t messageId) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)), (jlong)messageId);
        cbenv->DeleteGlobalRef(callback);
        cbenv->ReleaseStringUTFChars(message, messageS);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_sendUserMessageWithMetadataNative
(JNIEnv *env, jclass, jlong ptr, jlong userId, jstring message, jobjectArray meta, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    const char *messageS = env->GetStringUTFChars(message, nullptr);

    std::unordered_map<std::string, std::string> metadata;

    jclass pairClazz = env->FindClass("net/derfruhling/discord/socialsdk4j/StringPair");

    int metaSize = env->GetArrayLength(meta);
    for (int i = 0; i < metaSize; i++) {
        jobject obj = env->GetObjectArrayElement(meta, i);
        metadata.emplace(pairFromJava(env, pairClazz, obj));
    }

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;J)V");
    client->SendUserMessageWithMetadata(userId, messageS, metadata, [message, messageS, method, callback](discordpp::ClientResult res, uint64_t messageId) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)), (jlong)messageId);
        cbenv->DeleteGlobalRef(callback);
        cbenv->ReleaseStringUTFChars(message, messageS);
    });
}

JNIEXPORT jobject JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_startCallNative
(JNIEnv *env, jclass, jlong ptr, jlong channelId) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    discordpp::Call *call = new discordpp::Call(std::move(client->StartCall(channelId)));

    jclass clazz = env->FindClass("net/derfruhling/discord/socialsdk4j/Call");
    jmethodID methodId = env->GetMethodID(clazz, "<init>", "(J)V");

    return env->NewObject(clazz, methodId, (jlong)call);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_endCallNative
(JNIEnv *env, jclass, jlong ptr, jlong channelId, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "()V");
    client->EndCall(channelId, [method, callback]() {
        cbenv->CallVoidMethod(callback, method);
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_endCallsNative
(JNIEnv *env, jclass, jlong ptr, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "()V");
    client->EndCalls([method, callback]() {
        cbenv->CallVoidMethod(callback, method);
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_sendActivityInviteNative
(JNIEnv *env, jclass, jlong ptr, jlong userId, jstring contentNullable, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    const char *content = env->GetStringUTFChars(contentNullable, nullptr);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->SendActivityInvite(userId, content, [content, contentNullable, method, callback](discordpp::ClientResult res) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)));
        cbenv->ReleaseStringUTFChars(contentNullable, content);
        cbenv->DeleteGlobalRef(callback);
    });
}

static jfieldID senderIdField = nullptr;
static jfieldID channelIdField = nullptr;
static jfieldID messageIdField = nullptr;
static jfieldID typeField = nullptr;
static jfieldID typeValueField = nullptr;
static jfieldID applicationIdField = nullptr;
static jfieldID partyIdField = nullptr;
static jfieldID sessionIdField = nullptr;
static jfieldID isValidField = nullptr;

inline jclass MaybeInitActivityInviteFields(JNIEnv *env) {
    jclass cls = env->FindClass("Lnet/derfruhling/discord/socialsdk4j/ActivityInvite;");
    if(!senderIdField) senderIdField = env->GetFieldID(cls, "senderId", "J");
    if(!channelIdField) channelIdField = env->GetFieldID(cls, "channelId", "J");
    if(!messageIdField) messageIdField = env->GetFieldID(cls, "messageId", "J");
    if(!typeField) typeField = env->GetFieldID(cls, "type", "Lnet/derfruhling/discord/socialsdk4j/ActivityInvite$ActionType;");
    if(!typeValueField) typeValueField = env->GetFieldID(env->FindClass("Lnet/derfruhling/discord/socialsdk4j/ActivityInvite$ActionType;"), "value", "I");
    if(!applicationIdField) applicationIdField = env->GetFieldID(cls, "applicationId", "J");
    if(!partyIdField) partyIdField = env->GetFieldID(cls, "partyId", "Ljava/lang/String;");
    if(!sessionIdField) sessionIdField = env->GetFieldID(cls, "sessionId", "Ljava/lang/String;");
    if(!isValidField) isValidField = env->GetFieldID(cls, "isValidField", "Z");
    return cls;
}

discordpp::ActivityInvite ReconstructActivityInvite(JNIEnv *env, jobject obj) {
    discordpp::ActivityInvite invite;

    MaybeInitActivityInviteFields(env);

    invite.SetSenderId(env->GetLongField(obj, senderIdField));
    invite.SetChannelId(env->GetLongField(obj, channelIdField));
    invite.SetMessageId(env->GetLongField(obj, messageIdField));

    jobject typeEnumValue = env->GetObjectField(obj, typeField);
    invite.SetType((discordpp::ActivityActionTypes)env->GetIntField(typeEnumValue, typeValueField));

    invite.SetApplicationId(env->GetLongField(obj, applicationIdField));

    jobject partyId = env->GetObjectField(obj, partyIdField);
    const char *partyIdS = env->GetStringUTFChars((jstring)partyId, nullptr);
    invite.SetPartyId(partyIdS);

    jobject sessionId = env->GetObjectField(obj, sessionIdField);
    const char *sessionIdS = env->GetStringUTFChars((jstring)sessionId, nullptr);
    invite.SetSessionId(sessionIdS);

    invite.SetIsValid(isValidField);

    env->ReleaseStringUTFChars((jstring)partyId, partyIdS);
    env->ReleaseStringUTFChars((jstring)sessionId, sessionIdS);

    return invite;
}

jobject DeconstructActivityInvite(JNIEnv *env, discordpp::ActivityInvite invite) {
    jclass cls = env->FindClass("Lnet/derfruhling/discord/socialsdk4j/ActivityInvite;");
    jmethodID cons = env->GetMethodID(cls, "<init>", "(JJJIJLjava/lang/String;Ljava/lang/String;Z)V");

    return env->NewObject(cls, cons,
        (jlong)invite.SenderId(),
        (jlong)invite.ChannelId(),
        (jlong)invite.MessageId(),
        (jint)invite.Type(),
        (jlong)invite.ApplicationId(),
        env->NewStringUTF(invite.PartyId().c_str()),
        env->NewStringUTF(invite.SessionId().c_str()),
        (jboolean)invite.IsValid());
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_acceptActivityInviteNative
(JNIEnv *env, jclass, jlong ptr, jobject inviteObj, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;Ljava/lang/String;)V");
    client->AcceptActivityInvite(ReconstructActivityInvite(env, inviteObj), [method, callback](discordpp::ClientResult res, std::string joinSecret) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)), cbenv->NewStringUTF(joinSecret.c_str()));
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_sendActivityJoinRequestNative
(JNIEnv *env, jclass, jlong ptr, jlong userId, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->SendActivityJoinRequest(userId, [method, callback](discordpp::ClientResult res) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)));
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_sendActivityJoinRequestReplyNative
(JNIEnv *env, jclass, jlong ptr, jobject inviteObj, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ClientResult;)V");
    client->SendActivityJoinRequestReply(ReconstructActivityInvite(env, inviteObj), [method, callback](discordpp::ClientResult res) {
        cbenv->CallVoidMethod(callback, method, CreateClientResult(cbenv, std::move(res)));
        cbenv->DeleteGlobalRef(callback);
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_setActivityInviteCreatedCallbackNative
(JNIEnv *env, jclass, jlong ptr, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ActivityInvite;)V");
    client->SetActivityInviteCreatedCallback([method, callback](discordpp::ActivityInvite invite) {
        cbenv->CallVoidMethod(callback, method, DeconstructActivityInvite(cbenv, invite));
    });
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_setActivityInviteUpdatedCallbackNative
(JNIEnv *env, jclass, jlong ptr, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Lnet/derfruhling/discord/socialsdk4j/ActivityInvite;)V");
    client->SetActivityInviteUpdatedCallback([method, callback](discordpp::ActivityInvite invite) {
        cbenv->CallVoidMethod(callback, method, DeconstructActivityInvite(cbenv, invite));
    });
}

JNIEXPORT void JNICALL Java_net_derfruhling_discord_socialsdk4j_Client_setActivityJoinCallbackNative
(JNIEnv *env, jclass, jlong ptr, jobject callback) {
    discordpp::Client *client = reinterpret_cast<discordpp::Client *>(ptr);
    callback = env->NewGlobalRef(callback);

    jmethodID method = env->GetMethodID(env->GetObjectClass(callback), "invoke", "(Ljava/lang/String;)V");
    client->SetActivityJoinCallback([method, callback](std::string joinSecert) {
        cbenv->CallVoidMethod(callback, method, cbenv->NewStringUTF(joinSecert.c_str()));
    });
}

JNIEXPORT jstring JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_getDefaultCommunicationsScopesNative
(JNIEnv *env, jclass) {
    return env->NewStringUTF(discordpp::Client::GetDefaultCommunicationScopes().c_str());
}

JNIEXPORT jstring JNICALL
Java_net_derfruhling_discord_socialsdk4j_Client_getDefaultPresenceScopesNative
(JNIEnv *env, jclass) {
    return env->NewStringUTF(discordpp::Client::GetDefaultPresenceScopes().c_str());
}
