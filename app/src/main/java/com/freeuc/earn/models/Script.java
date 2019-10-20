package com.freeuc.earn.models;

public class Script {
    private String title;
    private int amount;
    private String link;

    public Script() {
    }

    public Script(String title, int amount, String link) {
        this.title = title;
        this.amount = amount;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
