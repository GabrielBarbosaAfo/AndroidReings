package br.edu.ifsudestemg.throne.narrative;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;

public class KingdomState {

    private final int wealth;

    private final int people;

    private final int army;

    private final int faith;

    public KingdomState(int wealth, int people, int army, int faith) {
        this.wealth = Math.max(0, Math.min(100, wealth));
        this.people = Math.max(0, Math.min(100, people));
        this.army = Math.max(0, Math.min(100, army));
        this.faith = Math.max(0, Math.min(100, faith));
    }

    public int getWealth() {
        return wealth;
    }

    public int getPeople() {
        return people;
    }

    public int getArmy() {
        return army;
    }

    public int getFaith() {
        return faith;
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Override
    public String toString() {
        return String.format("Riqueza=%d, Povo=%d, Exército=%d, Fé=%d", wealth, people, army, faith);
    }
}