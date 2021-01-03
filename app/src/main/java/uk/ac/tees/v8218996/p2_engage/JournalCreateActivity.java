package uk.ac.tees.v8218996.p2_engage;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.ac.tees.v8218996.p2_engage.adapter.JournalMoodDialogListAdaptor;
import uk.ac.tees.v8218996.p2_engage.data.DatabaseHandler;
import uk.ac.tees.v8218996.p2_engage.data.MoodData;
import uk.ac.tees.v8218996.p2_engage.model.Journal;
import uk.ac.tees.v8218996.p2_engage.model.Mood;
import uk.ac.tees.v8218996.p2_engage.model.User;
import uk.ac.tees.v8218996.p2_engage.ui.BottomTabMenu;
import uk.ac.tees.v8218996.p2_engage.util.FirebaseConstants;

public class JournalCreateActivity extends BottomTabMenu {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
    DatabaseReference myDBRef = firebaseDB.getReference();
    private TextInputEditText journalTitle;
    private TextInputEditText journalBody;
    private SwitchMaterial journalStatus;


    private Chip journal_add_mood_chip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_create);

        //get ids
        journal_add_mood_chip = findViewById(R.id.journal_add_mood_chip);
        MaterialToolbar toolbar = findViewById(R.id.journal_topAppBar);
        this.journalTitle = findViewById(R.id.journal_title);
        this.journalBody = findViewById(R.id.journal_body);
        this.journalStatus = findViewById(R.id.isPrivate);


        //initialize firestore
        mAuth = FirebaseAuth.getInstance();
        this.currentUser = mAuth.getCurrentUser();


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.save_new_journal){
                    saveNewJournal();
                }
                return true;
            }
        });

        journal_add_mood_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoodDialog();
            }
        });
        journalStatus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (journalStatus.isChecked()){
                    journalStatus.setText("Private");
                }else{
                    journalStatus.setText("Public");
                }
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener);
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.journal_page).setChecked(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void saveNewJournal(){

        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        User user = db.getLoggedUser(currentUser.getUid());
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = myDBRef.child("journals").push().getKey();//create journal ID


        String title = journalTitle.getText().toString();
        String body = journalBody.getText().toString();
        Boolean isPrivate = journalStatus.isChecked();
        String mood = journal_add_mood_chip.getText().toString();

        if (title.equals("") || body.equals("") || mood.equals("What is your mood?") ){
            Toast.makeText(JournalCreateActivity.this,"Error Adding Journal. Fields must be filled properly.",Toast.LENGTH_LONG).show();
        }else{
            Journal journal = new Journal();
            journal.setTitle(title.toUpperCase());
            journal.setId(key);
            journal.setBody(body);
            journal.setPrivate(isPrivate);
            journal.setMood(mood);
            journal.setDate(getCurrentDateAndTime());
            journal.setUuid(currentUser.getUid());
            journal.setAuthor(user.getNickName());

            //FIREBASE WRITE
            Map<String, Object> journalValues = journal.toMap();

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/journals/" + key, journalValues);
            childUpdates.put("/user-journals/" + currentUser.getUid() + "/" + key, journalValues);

            myDBRef.updateChildren(childUpdates)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(JournalCreateActivity.this,"New Journal Added",Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getApplicationContext(), JournalActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(JournalCreateActivity.this,"Error Adding Journal",Toast.LENGTH_LONG).show();
                }
            });
        }


    }

    private  void showMoodDialog(){
        final List<Mood> allMoods = MoodData.initMoodEntryList(getResources());
        final Dialog dialog = new Dialog(JournalCreateActivity.this );
        dialog.setContentView(R.layout.journal_mood_layout);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // this is optional
        }
        ListView listView = dialog.findViewById(R.id.journal_mood_listView);
        ArrayAdapter arrayAdapter = new JournalMoodDialogListAdaptor(JournalCreateActivity.this,R.layout.journal_mood_listview_layout,allMoods);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("joii", "onItemClick: ");

                Mood mood =  allMoods.get(position);
                String selectedFeeling =  mood.getName();
                String displayedFeeling = "Feeling " + selectedFeeling;
                String icon = mood.getIcon();
                String uri = "@drawable/"+icon;
                int imgResource = getApplicationContext().getResources().getIdentifier(uri, null, "uk.ac.tees.v8218996.p2_engage");

                journal_add_mood_chip.setText(displayedFeeling);
                journal_add_mood_chip.setChipIconResource(imgResource);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getCurrentDateAndTime(){
        //String currentDateTimeString =   java.text.DateFormat.getDateTimeInstance().format(new Date());
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("EE, MMM dd, yyyy hh:mm a").format(new Date());

        return date;
    }
}