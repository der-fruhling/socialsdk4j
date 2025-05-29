#include <jni.h>
#include <discordpp.h>

JNIEXPORT jlong JNICALL
Java_net_derfruhling_discord_socialsdk4j_Activity_createNewActivityNative
(JNIEnv *, jclass) {
    discordpp::Activity *activity = new discordpp::Activity;
    return reinterpret_cast<jlong>(activity);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Activity_deleteActivityNative
(JNIEnv *, jclass, jlong ptr) {
    delete reinterpret_cast<discordpp::Activity *>(ptr);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Activity_addButton
(JNIEnv *env, jclass, jlong ptr, jstring label, jstring url) {
    discordpp::Activity *activity = reinterpret_cast<discordpp::Activity *>(ptr);

    const char *labelS = env->GetStringUTFChars(label, nullptr);
    const char *urlS = env->GetStringUTFChars(url, nullptr);

    discordpp::ActivityButton button;
    button.SetLabel(labelS);
    button.SetUrl(urlS);
    activity->AddButton(button);

    env->ReleaseStringUTFChars(url, urlS);
    env->ReleaseStringUTFChars(label, labelS);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Activity_setName
(JNIEnv *env, jclass, jlong ptr, jstring name) {
    discordpp::Activity *activity = reinterpret_cast<discordpp::Activity *>(ptr);

    const char *nameS = env->GetStringUTFChars(name, nullptr);
    activity->SetName(nameS);
    env->ReleaseStringUTFChars(name, nameS);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Activity_setType
(JNIEnv *, jclass, jlong ptr, jint type) {
    discordpp::Activity *activity = reinterpret_cast<discordpp::Activity *>(ptr);

    activity->SetType(static_cast<discordpp::ActivityTypes>(type));
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Activity_setState
(JNIEnv *env, jclass, jlong ptr, jstring state) {
    discordpp::Activity *activity = reinterpret_cast<discordpp::Activity *>(ptr);

    const char *stateS = env->GetStringUTFChars(state, nullptr);
    activity->SetState(stateS);
    env->ReleaseStringUTFChars(state, stateS);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Activity_setDetails
(JNIEnv *env, jclass, jlong ptr, jstring details) {
    discordpp::Activity *activity = reinterpret_cast<discordpp::Activity *>(ptr);

    const char *detailsS = env->GetStringUTFChars(details, nullptr);
    activity->SetDetails(detailsS);
    env->ReleaseStringUTFChars(details, detailsS);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Activity_setActivityAssets
(JNIEnv *env, jclass, jlong ptr, jstring largeImage, jstring largeText, jstring smallImage, jstring smallText) {
    discordpp::Activity *activity = reinterpret_cast<discordpp::Activity *>(ptr);

    const char *largeImageS = largeImage ? env->GetStringUTFChars(largeImage, nullptr) : nullptr;
    const char *largeTextS = largeText ? env->GetStringUTFChars(largeText, nullptr) : nullptr;
    const char *smallImageS = smallImage ? env->GetStringUTFChars(smallImage, nullptr) : nullptr;
    const char *smallTextS = smallText ? env->GetStringUTFChars(smallText, nullptr) : nullptr;

    discordpp::ActivityAssets assets;
    assets.SetLargeImage(largeImageS ? std::optional(largeImageS) : std::nullopt);
    assets.SetLargeText(largeTextS ? std::optional(largeTextS) : std::nullopt);
    assets.SetSmallImage(smallImageS ? std::optional(smallImageS) : std::nullopt);
    assets.SetSmallText(smallTextS ? std::optional(smallTextS) : std::nullopt);
    activity->SetAssets(assets);

    if(largeImageS) env->ReleaseStringUTFChars(largeImage, largeImageS);
    if(largeTextS) env->ReleaseStringUTFChars(largeText, largeTextS);
    if(smallImageS) env->ReleaseStringUTFChars(smallImage, smallImageS);
    if(smallTextS) env->ReleaseStringUTFChars(smallText, smallTextS);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Activity_setTimestamps
(JNIEnv *, jclass, jlong ptr, jlong start, jlong end) {
    discordpp::Activity *activity = reinterpret_cast<discordpp::Activity *>(ptr);

    discordpp::ActivityTimestamps timestamps;
    timestamps.SetStart(start);
    timestamps.SetEnd(end);
    activity->SetTimestamps(timestamps);
}

JNIEXPORT void JNICALL Java_net_derfruhling_discord_socialsdk4j_Activity_setParty
(JNIEnv *env, jclass, jlong ptr, jstring id, jint size, jint maxSize, jboolean isPublic) {
    discordpp::Activity *activity = reinterpret_cast<discordpp::Activity *>(ptr);

    const char *idS = env->GetStringUTFChars(id, nullptr);

    discordpp::ActivityParty party;
    party.SetId(idS);
    party.SetCurrentSize(size);
    party.SetMaxSize(maxSize);
    party.SetPrivacy(isPublic
        ? discordpp::ActivityPartyPrivacy::Public
        : discordpp::ActivityPartyPrivacy::Private);
    activity->SetParty(party);

    env->ReleaseStringUTFChars(id, idS);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_Activity_setSecrets
(JNIEnv *env, jclass, jlong ptr, jstring joinSecret) {
    discordpp::Activity *activity = reinterpret_cast<discordpp::Activity *>(ptr);

    const char *joinSecretS = env->GetStringUTFChars(joinSecret, nullptr);

    discordpp::ActivitySecrets secrets;
    secrets.SetJoin(joinSecretS);
    activity->SetSecrets(secrets);

    env->ReleaseStringUTFChars(joinSecret, joinSecretS);
}
