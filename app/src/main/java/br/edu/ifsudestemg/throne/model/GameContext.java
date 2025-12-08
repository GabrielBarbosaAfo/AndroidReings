package br.edu.ifsudestemg.throne.model;

public class GameContext {

    private String userContext;
    private long createdAt;
    private String initialSeed;

    public GameContext(String userContext) {
        this.userContext = userContext;
        this.createdAt = System.currentTimeMillis();
        this.initialSeed = "seed-" + createdAt;
    }

    public String getUserContext() {
        return userContext;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public String getInitialSeed() {
        return initialSeed;
    }
}

