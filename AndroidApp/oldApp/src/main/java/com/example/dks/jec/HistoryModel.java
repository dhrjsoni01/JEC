package com.example.dks.jec;

/**
 * Created by DKS on 4/9/2017.
 */

public class HistoryModel {

    String branch,sem,subject,datetime;
    int status;

    public HistoryModel(String branch, String sem, String subject, String datetime, int status) {
        this.branch = branch;
        this.sem = sem;
        this.subject = subject;
        this.datetime = datetime;
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
