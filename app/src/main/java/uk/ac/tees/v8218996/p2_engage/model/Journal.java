package uk.ac.tees.v8218996.p2_engage.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Journal {

    public String id;
    public String uuid;
    public String author;
    public String title;
    public String body;
    public Boolean isPrivate;
    public String mood;
    public String date;
    public int likeCount = 0;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    //we must have an empty constructor for firestore
    public  Journal(){

    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public Journal(String id,String uuid, String title, String body, Boolean isPrivate, String mood, String date) {

        this.uuid = uuid;
        this.id=id;
        this.title = title;
        this.body = body;
        this.isPrivate = isPrivate;
        this.mood = mood;
        this.date = date;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getPrivate() {
        return isPrivate;
    }

    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();

        result.put("id",id);
        result.put("uuid",uuid);
        result.put("author",author);
        result.put("title",title);
        result.put("body",body);
        result.put("isPrivate",isPrivate);
        result.put("mood",mood);
        result.put("date",date);

        return result;

    }


}
