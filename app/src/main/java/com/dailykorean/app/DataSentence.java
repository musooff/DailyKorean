package com.dailykorean.app;

/**
 * Created by moshe on 01/05/2017.
 */

public class DataSentence {

    private String orgnc_sentence;
    private String trsl_orgnc_sentence;
    private String speaker;
    private String gender;
    private String showen;

    public DataSentence(String orgnc_sentence, String trsl_orgnc_sentence, String speaker,String gender) {
        this.orgnc_sentence = orgnc_sentence;
        this.trsl_orgnc_sentence = trsl_orgnc_sentence;
        this.speaker = speaker;
        this.gender = gender;
    }

    public String getOrgnc_sentence() {
        return orgnc_sentence;
    }

    public String getTrsl_orgnc_sentence(){
        return trsl_orgnc_sentence;
    }
    public String  getSpeaker(){
        return speaker;
    }
    public String getGender(){ return gender;}
    public int getLength(){
        int length = orgnc_sentence.split("ъ").length;
        return length;
    }

    public String getShowen(){
        return showen;
    }
    public void setShowen(String string){
        showen = string;
    }
}
