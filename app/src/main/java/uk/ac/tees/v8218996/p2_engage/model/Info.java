package uk.ac.tees.v8218996.p2_engage.model;

import java.io.Serializable;
import java.net.URL;

public class Info {

    public String title;
    public String link;
    public String description;
    static final long serialUID = 40L;

    public Info() {
    }

    public Info(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
