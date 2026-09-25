#include <net_derfruhling_discord_socialsdk4j_ActivityBuilder.h>
#include "socialsdk4j.hpp"

jobject s4j::createActivityInfo(
    JNIEnv *env,
    const discordpp::Activity &activity
) {
    jclass cls = env->FindClass("net/derfruhling/discord/socialsdk4j/ActivityInfo");
    jmethodID method = env->GetMethodID(cls, "<init>", "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;[Lnet/derfruhling/discord/socialsdk4j/ActivityInfo$Button;JLjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;JJLjava/lang/String;IIZLjava/lang/String;I)V");
    auto buttons = activity.GetButtons();

    jclass buttonCls = env->FindClass("net/derfruhling/discord/socialsdk4j/ActivityInfo$Button");
    jmethodID buttonCons = env->GetMethodID(buttonCls, "<init>", "(Ljava/lang/String;Ljava/lang/String;)V");
    jobjectArray buttonsJni = env->NewObjectArray(buttons.size(), buttonCls, nullptr);

    for (int i = 0; i < buttons.size(); i++) {
        const auto &button = buttons[i];

        jstring label = env->NewStringUTF(button.Label().c_str());
        jstring url = env->NewStringUTF(button.Url().c_str());

        jobject obj = env->NewObject(buttonCls, buttonCons, label, url);
        env->SetObjectArrayElement(buttonsJni, i, obj);
    }

    jstring largeImage, largeText, smallImage, smallText;

    if (activity.Assets().has_value()) {
        auto assets = activity.Assets().value();
        if(assets.LargeImage().has_value())
            largeImage = env->NewStringUTF(assets.LargeImage().value().c_str());
        if(assets.LargeText().has_value())
            largeText = env->NewStringUTF(assets.LargeText().value().c_str());
        if(assets.SmallImage().has_value())
            smallImage = env->NewStringUTF(assets.SmallImage().value().c_str());
        if(assets.SmallText().has_value())
            smallText = env->NewStringUTF(assets.SmallText().value().c_str());
    }

    auto ts = activity.Timestamps();
    auto party = activity.Party();

    return env->NewObject(cls, method,
        (jint)activity.Type(),
        env->NewStringUTF(activity.Name().c_str()),
        activity.State().has_value() ? env->NewStringUTF(activity.State().value().c_str()) : nullptr,
        activity.Details().has_value() ? env->NewStringUTF(activity.State().value().c_str()) : nullptr,
        buttonsJni,
        (jlong)activity.ApplicationId().value_or(0),
        largeImage,
        largeText,
        smallImage,
        smallText,
        ts.has_value() ? (jlong)ts.value().Start() : 0,
        ts.has_value() ? (jlong)ts.value().End() : 0,
        party.has_value() ? env->NewStringUTF(party.value().Id().c_str()) : nullptr,
        party.has_value() ? party.value().CurrentSize() : 0,
        party.has_value() ? party.value().MaxSize() : 0,
        party.has_value() ? party.value().Privacy() == discordpp::ActivityPartyPrivacy::Public : false,
        activity.Secrets().has_value() ? env->NewStringUTF(activity.Secrets().value().Join().c_str()) : nullptr,
        (jint)activity.SupportedPlatforms()
    );
}

JNIEXPORT jlong JNICALL
Java_net_derfruhling_discord_socialsdk4j_ActivityBuilder_createNewActivityNative
(JNIEnv *, jclass) {
    discordpp::Activity *activity = new discordpp::Activity;
    return reinterpret_cast<jlong>(activity);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_ActivityBuilder_delete
(JNIEnv *, jclass, jlong ptr) {
    delete reinterpret_cast<discordpp::Activity *>(ptr);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_ActivityBuilder_addButton
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
Java_net_derfruhling_discord_socialsdk4j_ActivityBuilder_setName
(JNIEnv *env, jclass, jlong ptr, jstring name) {
    discordpp::Activity *activity = reinterpret_cast<discordpp::Activity *>(ptr);

    const char *nameS = env->GetStringUTFChars(name, nullptr);
    activity->SetName(nameS);
    env->ReleaseStringUTFChars(name, nameS);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_ActivityBuilder_setType
(JNIEnv *, jclass, jlong ptr, jint type) {
    discordpp::Activity *activity = reinterpret_cast<discordpp::Activity *>(ptr);

    activity->SetType(static_cast<discordpp::ActivityTypes>(type));
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_ActivityBuilder_setState
(JNIEnv *env, jclass, jlong ptr, jstring state) {
    discordpp::Activity *activity = reinterpret_cast<discordpp::Activity *>(ptr);

    const char *stateS = env->GetStringUTFChars(state, nullptr);
    activity->SetState(stateS);
    env->ReleaseStringUTFChars(state, stateS);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_ActivityBuilder_setDetails
(JNIEnv *env, jclass, jlong ptr, jstring details) {
    discordpp::Activity *activity = reinterpret_cast<discordpp::Activity *>(ptr);

    const char *detailsS = env->GetStringUTFChars(details, nullptr);
    activity->SetDetails(detailsS);
    env->ReleaseStringUTFChars(details, detailsS);
}

JNIEXPORT void JNICALL
Java_net_derfruhling_discord_socialsdk4j_ActivityBuilder_setActivityAssets
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
Java_net_derfruhling_discord_socialsdk4j_ActivityBuilder_setTimestamps
(JNIEnv *, jclass, jlong ptr, jlong start, jlong end) {
    discordpp::Activity *activity = reinterpret_cast<discordpp::Activity *>(ptr);

    discordpp::ActivityTimestamps timestamps;
    timestamps.SetStart(start);
    timestamps.SetEnd(end);
    activity->SetTimestamps(timestamps);
}

JNIEXPORT void JNICALL Java_net_derfruhling_discord_socialsdk4j_ActivityBuilder_setParty
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
Java_net_derfruhling_discord_socialsdk4j_ActivityBuilder_setSecrets
(JNIEnv *env, jclass, jlong ptr, jstring joinSecret) {
    discordpp::Activity *activity = reinterpret_cast<discordpp::Activity *>(ptr);

    const char *joinSecretS = env->GetStringUTFChars(joinSecret, nullptr);

    discordpp::ActivitySecrets secrets;
    secrets.SetJoin(joinSecretS);
    activity->SetSecrets(secrets);

    env->ReleaseStringUTFChars(joinSecret, joinSecretS);
}
