package uk.ac.tees.v8218996.p2_engage.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Chat {

    public String msg;
    public String uuid;
    public String sender;
    public String sentTime;


    public Chat(String msg, String uuid, String sender, String sentTime) {
        this.msg = msg;
        this.uuid = uuid;
        this.sender = sender;
        this.sentTime = sentTime;
    }

    public Chat() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSentTime() {
        return sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    @Exclude
    public Map<String, Object> toMap(){

        HashMap<String, Object> data = new HashMap<>();

        data.put("msg",this.msg);
        data.put("sentTime",this.sentTime);
        data.put("sender",this.sender);
        data.put("uuid",this.uuid);

        return data;
    }
}
