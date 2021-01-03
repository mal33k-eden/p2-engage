package uk.ac.tees.v8218996.p2_engage.model;

public class User {

    public String uid;
    public String nickName;

    public User() {
    }

    public User(String uid, String nickName) {
        this.uid = uid;
        this.nickName = nickName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }


}
