package br.edu.ifsudestemg.throne.model;

public class CardData {

    private String title;

    private String description;

    private final int imageRes;

    private String yesResponse;

    private String noResponse;

    public CardData(String title, String description, int imageRes, String yesResponse, String noResponse) {
        this.title = title;
        this.description = description;
        this.imageRes = imageRes;
        this.yesResponse = yesResponse;
        this.noResponse = noResponse;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImageRes() {
        return imageRes;
    }

    public String getYesResponse() {
        return yesResponse;
    }

    public String getNoResponse() {
        return noResponse;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setYesResponse(String yesResponse) {
        this.yesResponse = yesResponse;
    }

    public void setNoResponse(String noResponse) {
        this.noResponse = noResponse;
    }
}