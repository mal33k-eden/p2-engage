package uk.ac.tees.v8218996.p2_engage.model;

public class Mood {

    public String name;
    public String icon;

    public Mood(String name, String icon) {

        this.name = name;
        this.icon = icon;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


}
