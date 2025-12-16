package br.edu.ifsudestemg.throne.model;

public class GameProgress {

    private int decisionCount;

    private String currentCardId;

    private boolean isTwistActive;

    private int currentTwistPart;

    public GameProgress() {
        this.decisionCount = 0;
        this.currentCardId = "root";
        this.isTwistActive = false;
        this.currentTwistPart = 0;
    }

    public int getDecisionCount() {
        return decisionCount;
    }

    public void setDecisionCount(int decisionCount) {
        this.decisionCount = decisionCount;
    }

    public String getCurrentCardId() {
        return currentCardId;
    }

    public void setCurrentCardId(String currentCardId) {
        this.currentCardId = currentCardId;
    }

    public boolean isTwistActive() {
        return isTwistActive;
    }

    public void setTwistActive(boolean twistActive) {
        isTwistActive = twistActive;
    }

    public int getCurrentTwistPart() {
        return currentTwistPart;
    }

    public void setCurrentTwistPart(int currentTwistPart) {
        this.currentTwistPart = currentTwistPart;
    }
}