package br.edu.ifsudestemg.throne.model;

import java.util.ArrayList;
import java.util.List;

public class GameState {

    private int people = 50;
    private int army = 50;
    private int wealth = 50;
    private int faith = 50;

    private final List<String> lastEvents = new ArrayList<>();

    public int getPeople() { return people; }
    public int getArmy() { return army; }
    public int getWealth() { return wealth; }
    public int getFaith() { return faith; }

    public void applyEffects(int p, int a, int w, int f) {
        people = clamp(people + p);
        army = clamp(army + a);
        wealth = clamp(wealth + w);
        faith = clamp(faith + f);
    }

    public void addEvent(String eventDescription) {
        if (lastEvents.size() > 3) {
            lastEvents.remove(0);
        }
        lastEvents.add(eventDescription);
    }

    public List<String> getLastEvents() {
        return lastEvents;
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(100, value));
    }
}
