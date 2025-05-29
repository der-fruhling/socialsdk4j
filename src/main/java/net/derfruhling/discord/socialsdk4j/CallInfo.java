package net.derfruhling.discord.socialsdk4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public record CallInfo(
        long channelId,
        long guildId,
        long[] participants,
        Map<Long, VoiceState> voiceStates
) {
    public record VoiceStatePair(long key, boolean selfDeaf, boolean selfMute) {}

    public CallInfo(long channelId, long guildId, long[] participants, VoiceStatePair[] voiceStates) {
        this(channelId, guildId, participants, Arrays.stream(voiceStates)
                .collect(Collectors.toMap(t -> t.key, t -> new VoiceState(t.selfDeaf, t.selfMute))));
    }
}
