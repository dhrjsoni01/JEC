package com.example.dks.jec;

/**
 * Created by DKS on 4/9/2017.
 */

public class RecordModel {

    String subject,pesence,percentage;

    public RecordModel(String subject, String pesence, String percentage) {
        this.subject = subject;
        this.pesence = pesence;
        this.percentage = percentage;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPesence() {
        return pesence;
    }

    public void setPesence(String pesence) {
        this.pesence = pesence;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}
