package uk.ac.tees.v8218996.p2_engage.data;

import android.content.res.Resources;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.v8218996.p2_engage.R;
import uk.ac.tees.v8218996.p2_engage.model.Mood;

public class MoodData  {

    private static final String TAG = MoodData.class.getSimpleName();
    public final String name;
    public final String icon;


    public MoodData(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    public  static List<Mood> initMoodEntryList(Resources resources){

        InputStream inputStream = resources.openRawResource(R.raw.moods);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];

        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int pointer;
            while((pointer = reader.read(buffer)) != -1){
                writer.write(buffer, 0, pointer);
            }
        }catch (IOException exception){
            Log.e(TAG,"Error writing/reading from the JSON file.", exception);
        }finally {
            try {
                inputStream.close();
            } catch (IOException exception) {
                Log.e(TAG, "Error closing the input stream.", exception);
            }
        }
        String jsonMoodString = writer.toString();
        Gson gson = new Gson();
        Type moodListType = new TypeToken<ArrayList<Mood>>() {
        }.getType();
        return gson.fromJson(jsonMoodString, moodListType);
    }

}
