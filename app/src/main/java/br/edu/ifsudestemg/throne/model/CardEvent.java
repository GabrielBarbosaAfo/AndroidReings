package br.edu.ifsudestemg.throne.model;

public class CardEvent {
    private final String title;
    private final String description;
    private final int effectPeopleYes, effectArmyYes, effectWealthYes, effectFaithYes;
    private final int effectPeopleNo, effectArmyNo, effectWealthNo, effectFaithNo;

    public CardEvent(String title, String description,
                     int effectPeopleYes, int effectArmyYes, int effectWealthYes, int effectFaithYes,
                     int effectPeopleNo, int effectArmyNo, int effectWealthNo, int effectFaithNo) {

        this.title = title;
        this.description = description;
        this.effectPeopleYes = effectPeopleYes;
        this.effectArmyYes = effectArmyYes;
        this.effectWealthYes = effectWealthYes;
        this.effectFaithYes = effectFaithYes;
        this.effectPeopleNo = effectPeopleNo;
        this.effectArmyNo = effectArmyNo;
        this.effectWealthNo = effectWealthNo;
        this.effectFaithNo = effectFaithNo;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPeopleYes() {
        return effectPeopleYes;
    }

    public int getArmyYes() {
        return effectArmyYes;
    }

    public int getWealthYes() {
        return effectWealthYes;
    }

    public int getFaithYes() {
        return effectFaithYes;
    }

    public int getPeopleNo() {
        return effectPeopleNo;
    }

    public int getArmyNo() {
        return effectArmyNo;
    }

    public int getWealthNo() {
        return effectWealthNo;
    }

    public int getFaithNo() {
        return effectFaithNo;
    }
}