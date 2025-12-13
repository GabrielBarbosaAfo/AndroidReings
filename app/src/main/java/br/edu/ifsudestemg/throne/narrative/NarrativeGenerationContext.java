package br.edu.ifsudestemg.throne.narrative;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NarrativeGenerationContext {

    @NonNull
    private final String userContext;

    @NonNull
    private final KingdomState kingdomState;

    @NonNull
    private final List<String> recentDecisions;

    @Nullable
    private final String previousTwist;

    public NarrativeGenerationContext(
            @NonNull String userContext,
            @NonNull KingdomState kingdomState,
            @NonNull List<String> recentDecisions) {

        this(userContext, kingdomState, recentDecisions, null);
    }

    public NarrativeGenerationContext(
            @NonNull String userContext,
            @NonNull KingdomState kingdomState,
            @NonNull List<String> recentDecisions,
            @Nullable String previousTwist) {

        this.userContext = userContext.trim();
        this.kingdomState = kingdomState;
        this.recentDecisions = new ArrayList<>(recentDecisions);

        while (this.recentDecisions.size() > 2) {
            this.recentDecisions.remove(0);
        }

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

    @NonNull
    public List<String> getRecentDecisions() {
        return Collections.unmodifiableList(recentDecisions);
    }

    @Nullable
    public String getPreviousTwist() {
        return previousTwist;
    }

    @NonNull
    public String getDecisionsSummary() {

        if (recentDecisions.isEmpty()) {
            return "Nenhuma decisão tomada ainda.";
        }

        StringBuilder summary = new StringBuilder("Histórico recente: ");

        for (int i = 0; i < recentDecisions.size(); i++) {

            if (i > 0)
                summary.append(" Depois, ");

            summary.append(recentDecisions.get(i));
        }

        summary.append(".");
        return summary.toString();
    }
}