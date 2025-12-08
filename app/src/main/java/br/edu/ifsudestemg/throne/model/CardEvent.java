package br.edu.ifsudestemg.throne.model;

public class CardEvent {

    private String title;
    private String description;

    // Efeitos caso o jogador escolha SIM
    private int effectPeopleYes;
    private int effectArmyYes;
    private int effectWealthYes;
    private int effectFaithYes;

    // Efeitos caso o jogador escolha NÃO
    private int effectPeopleNo;
    private int effectArmyNo;
    private int effectWealthNo;
    private int effectFaithNo;

    public CardEvent(
            String title,
            String description,
            int effectPeopleYes,
            int effectArmyYes,
            int effectWealthYes,
            int effectFaithYes,
            int effectPeopleNo,
            int effectArmyNo,
            int effectWealthNo,
            int effectFaithNo
    ) {
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

    public int getEffectPeopleYes() {
        return effectPeopleYes;
    }

    public int getEffectArmyYes() {
        return effectArmyYes;
    }

    public int getEffectWealthYes() {
        return effectWealthYes;
    }

    public int getEffectFaithYes() {
        return effectFaithYes;
    }

    public int getEffectPeopleNo() {
        return effectPeopleNo;
    }

    public int getEffectArmyNo() {
        return effectArmyNo;
    }

    public int getEffectWealthNo() {
        return effectWealthNo;
    }

    public int getEffectFaithNo() {
        return effectFaithNo;
    }
}

