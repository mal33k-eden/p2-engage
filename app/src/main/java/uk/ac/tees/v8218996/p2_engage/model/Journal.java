package uk.ac.tees.v8218996.p2_engage.model;

public class Journal {

    private int id;
    private String journalTitle;
    private String journalContent;
    private String journalStatus;
    private String journalMood;
    private String journalDate;

    public Journal() {
    }

    public Journal(int id, String journalTitle, String journalContent, String journalStatus, String journalMood, String journalDate) {
        this.id = id;
        this.journalTitle = journalTitle;
        this.journalContent = journalContent;
        this.journalStatus = journalStatus;
        this.journalMood = journalMood;
        this.journalDate = journalDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJournalTitle() {
        return journalTitle;
    }

    public void setJournalTitle(String journalTitle) {
        this.journalTitle = journalTitle;
    }

    public String getJournalContent() {
        return journalContent;
    }

    public void setJournalContent(String journalContent) {
        this.journalContent = journalContent;
    }

    public String getJournalStatus() {
        return journalStatus;
    }

    public void setJournalStatus(String journalStatus) {
        this.journalStatus = journalStatus;
    }

    public String getJournalMood() {
        return journalMood;
    }

    public void setJournalMood(String journalMood) {
        this.journalMood = journalMood;
    }

    public String getJournalDate() {
        return journalDate;
    }

    public void setJournalDate(String journalDate) {
        this.journalDate = journalDate;
    }
}
