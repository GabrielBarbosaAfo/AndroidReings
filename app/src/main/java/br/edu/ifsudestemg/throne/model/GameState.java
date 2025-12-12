package br.edu.ifsudestemg.throne.model;

public class GameState {

    private int people = 50;
    private int army = 50;
    private int wealth = 50;
    private int faith = 50;

    public int getPeople() {
        return people;
    }

    public int getArmy() {
        return army;
    }

    public int getWealth() {
        return wealth;
    }

    public int getFaith() {
        return faith;
    }

    public void setPeople(int value) {
        this.people = clamp(value);
    }

    public void setArmy(int value) {
        this.army = clamp(value);
    }

    public void setWealth(int value) {
        this.wealth = clamp(value);
    }

    public void setFaith(int value) {
        this.faith = clamp(value);
    }

    public void applyChoice(CardEvent card, boolean isYes) {
        if (isYes) {
            setPeople(people + card.getPeopleYes());
            setArmy(army + card.getArmyYes());
            setWealth(wealth + card.getWealthYes());
            setFaith(faith + card.getFaithYes());
        } else {
            setPeople(people + card.getPeopleNo());
            setArmy(army + card.getArmyNo());
            setWealth(wealth + card.getWealthNo());
            setFaith(faith + card.getFaithNo());
        }
    }

    private int clamp(int v) {
        return Math.max(0, Math.min(100, v));
    }
}