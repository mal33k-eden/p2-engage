package uk.ac.tees.v8218996.p2_engage.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ActivityLogs {

    public String nickName;
    public String type;
    public String date;

    public ActivityLogs() {
    }

    public ActivityLogs(String nickName, String type, String date) {
        this.nickName = nickName;
        this.type = type;
        this.date = date;
    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Exclude
    public Map<String, Object> toMap(){

        HashMap<String, Object> result = new HashMap<>();

        result.put("nickName",nickName);
        result.put("type",type);
        result.put("date",date);

        return result;
    }
}
