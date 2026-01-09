package br.edu.ifsudestemg.throne.model;

import com.google.gson.annotations.SerializedName;

public class NarrativeCard {
    private String title;
    private String description;
    private String yesResponse;
    private String noResponse;
    private String character;

    // Campos para o efeito do SIM (clicou/arrastou para direita)
    @SerializedName("yes_riqueza") private int yesWealth;
    @SerializedName("yes_povo") private int yesPeople;
    @SerializedName("yes_exercito") private int yesArmy;
    @SerializedName("yes_fe") private int yesFaith;

    // Campos para o efeito do NÃO (clicou/arrastou para esquerda)
    @SerializedName("no_riqueza") private int noWealth;
    @SerializedName("no_povo") private int noPeople;
    @SerializedName("no_exercito") private int noArmy;
    @SerializedName("no_fe") private int noFaith;

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getYesResponse() {
        return yesResponse;
    }

    public String getNoResponse() {
        return noResponse;
    }

    public String getCharacter() {
        return character;
    }

    public int getYesWealth() { return yesWealth; }
    public int getYesPeople() { return yesPeople; }
    public int getYesArmy() { return yesArmy; }
    public int getYesFaith() { return yesFaith; }

    public int getNoWealth() { return noWealth; }
    public int getNoPeople() { return noPeople; }
    public int getNoArmy() { return noArmy; }
    public int getNoFaith() { return noFaith; }
}