#ifndef SOCIALSDK4J_ACTIVITY_HPP
#define SOCIALSDK4J_ACTIVITY_HPP

#include <jni.h>
#include <discordpp.h>

inline jobject CreateActivityInfo(
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

#endif // SOCIALSDK4J_ACTIVITY_HPP
