package com.dailykorean.app;

/**
 * Created by moshe on 02/05/2017.
 */

public class DataCard {

    private String orgnc_word;
    private String trsl_orgnc_word;
    private String sound_str;
    private String showen;

    public DataCard(String orgnc_word, String trsl_orgnc_word, String sound_str) {
        this.orgnc_word = orgnc_word;
        this.trsl_orgnc_word = trsl_orgnc_word;
        this.sound_str = sound_str;
    }

    public String getOrgnc_word() {
        return orgnc_word;
    }

    public String getTrsl_orgnc_word(){
        return trsl_orgnc_word;
    }
    public String  getSound_str(){
        return sound_str;
    }

    public String getShowen(){
        return showen;
    }
    public void setShowen(String string){
        showen = string;
    }
}