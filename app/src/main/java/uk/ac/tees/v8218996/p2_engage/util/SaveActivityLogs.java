package uk.ac.tees.v8218996.p2_engage.util;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import uk.ac.tees.v8218996.p2_engage.model.ActivityLogs;

public class SaveActivityLogs {

    private static FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
    private static DatabaseReference myDBRef = firebaseDB.getReference().child("engagement-logs");


    public  static void saveLog(ActivityLogs logs){

        String key = myDBRef.push().getKey();//create journal ID
        //FIREBASE WRITE
        Map<String, Object> logValues = logs.toMap();

        myDBRef.child(key).setValue(logValues);
    }

}
