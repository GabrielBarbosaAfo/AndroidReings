package br.edu.ifsudestemg.throne.model;

import com.google.gson.annotations.SerializedName;

public class NarrativeCard {

    private String title;

    private String description;

    private String yesResponse;

    private String noResponse;

    @SerializedName("riqueza")
    private int wealth;

    @SerializedName("povo")
    private int people;

    @SerializedName("exercito")
    private int army;

    @SerializedName("fe")
    private int faith;

    public NarrativeCard() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYesResponse() {
        return yesResponse;
    }

    public void setYesResponse(String yesResponse) {
        this.yesResponse = yesResponse;
    }

    public String getNoResponse() {
        return noResponse;
    }

    public void setNoResponse(String noResponse) {
        this.noResponse = noResponse;
    }

    public int getWealth() {
        return wealth;
    }

    public void setWealth(int wealth) {
        this.wealth = wealth;
    }

    public int getPeople() {
        return people;
    }

    public void setPeople(int people) {
        this.people = people;
    }

    public int getArmy() {
        return army;
    }

    public void setArmy(int army) {
        this.army = army;
    }

    public int getFaith() {
        return faith;
    }

    public void setFaith(int faith) {
        this.faith = faith;
    }
}