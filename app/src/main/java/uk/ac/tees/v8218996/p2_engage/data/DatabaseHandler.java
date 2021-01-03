package uk.ac.tees.v8218996.p2_engage.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import uk.ac.tees.v8218996.p2_engage.model.User;
import uk.ac.tees.v8218996.p2_engage.util.DatabaseUtil;


public class DatabaseHandler extends SQLiteOpenHelper {

    private final Context context;

    public DatabaseHandler(Context context) {
        super(context, DatabaseUtil.DB_NAME, null, DatabaseUtil.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL - STRUCTURE QUERY LANGUAGE
        String CREATE_USER_TABLE = "CREATE TABLE " + DatabaseUtil.TABLE_NAME
                + "("
                + DatabaseUtil.KEY_UUID + " TEXT PRIMARY KEY, "
                + DatabaseUtil.KEY_NICK_NAME + " TEXT )";

        //create the table
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseUtil.TABLE_NAME);
        //CREATE THE TABLE AGAIN
        onCreate(db);
    }

    //CRUD

    public void addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        //using contact values

        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.KEY_NICK_NAME,user.getNickName());
        values.put(DatabaseUtil.KEY_UUID,user.getUid());


        db.insert(DatabaseUtil.TABLE_NAME,null ,values);

        Log.d("NEW DB handler", "user added" );
        db.close();
    }

    public User getLoggedUser(String id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DatabaseUtil.TABLE_NAME,
                new String[]{DatabaseUtil.KEY_UUID,DatabaseUtil.KEY_NICK_NAME},
                DatabaseUtil.KEY_UUID + "=?", new String[]{id}, null,null,null);
        if(cursor != null)
            cursor.moveToFirst();

        User user = new User();

        if (cursor !=null){
            user.setUid(cursor.getString(cursor.getColumnIndex(DatabaseUtil.KEY_UUID)));
            user.setNickName(cursor.getString(cursor.getColumnIndex(DatabaseUtil.KEY_NICK_NAME)));

        }
        return user;
    }
}
