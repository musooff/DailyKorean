package com.dailykorean.app;

/**
 * Created by moshe on 18/04/2017.
 */

public class Data {
    private String engTitle;
    private String korTitle;
    private String date;

    private boolean sunday = false;

    public Data(String engTitle, String korTitle, String date, boolean sunday) {
        this.engTitle = engTitle;
        this.korTitle = korTitle;
        this.date = date;
        this.sunday = sunday;
    }

    public String getEngTitle() {
        return engTitle;
    }

    public String getKorTitle(){
        return korTitle;
    }
    public String  getDate(){
        return date;
    };

}