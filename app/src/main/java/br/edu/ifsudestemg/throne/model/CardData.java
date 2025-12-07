package br.edu.ifsudestemg.throne.model;

public class CardData {

    private final String title;

    private final String text;

    private final int imageRes;

    public CardData(String title, String text, int imageRes) {
        this.title = title;
        this.text = text;
        this.imageRes = imageRes;
    }

    public String getTitle() { return title; }
    public String getText() { return text; }
    public int getImageRes() { return imageRes; }
}

