package br.edu.ifsudestemg.throne.narrative;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NarrativeGenerationContext {

    @NonNull
    private final String userContext;

    @NonNull
    private final KingdomState kingdomState;

    @Nullable
    private final String previousTwist;

    public NarrativeGenerationContext(
            @NonNull String userContext,
            @NonNull KingdomState kingdomState,
            @Nullable String previousTwist) {

        this.userContext = userContext.trim();
        this.kingdomState = kingdomState;
        this.previousTwist = previousTwist;
    }

    @NonNull
    public String getUserContext() {
        return userContext;
    }

    @NonNull
    public KingdomState getKingdomState() {
        return kingdomState;
    }

    @Nullable
    public String getPreviousTwist() {
        return previousTwist;
    }
}