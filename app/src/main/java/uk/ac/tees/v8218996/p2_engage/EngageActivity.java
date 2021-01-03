package uk.ac.tees.v8218996.p2_engage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import pl.droidsonroids.gif.GifImageView;
import uk.ac.tees.v8218996.p2_engage.adapter.EngagementRecyclerViewAdapter;
import uk.ac.tees.v8218996.p2_engage.data.AffirmationData;
import uk.ac.tees.v8218996.p2_engage.data.DatabaseHandler;
import uk.ac.tees.v8218996.p2_engage.model.ActivityLogs;
import uk.ac.tees.v8218996.p2_engage.model.Affirmation;
import uk.ac.tees.v8218996.p2_engage.model.Chat;
import uk.ac.tees.v8218996.p2_engage.model.User;
import uk.ac.tees.v8218996.p2_engage.ui.BottomTabMenu;
import uk.ac.tees.v8218996.p2_engage.util.SaveActivityLogs;

public class EngageActivity extends BottomTabMenu {
    public TextInputEditText inputMessage;
   // public ImageButton sendMessage;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
    DatabaseReference myDBRef = firebaseDB.getReference();
    private RecyclerView recyclerView;
    private EngagementRecyclerViewAdapter recyclerViewAdapter;

    private List<Chat> chatList = new ArrayList<>();
    private String CHAT_FROM_HOME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_engage);
        Intent i = getIntent();

        MaterialToolbar toolbar =  findViewById(R.id.engage_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
            }
        });

        //initialize firestore
        mAuth = FirebaseAuth.getInstance();
        this.currentUser = mAuth.getCurrentUser();




        recyclerView = findViewById(R.id.engageRecyclerContainer);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new EngagementRecyclerViewAdapter(this, chatList,currentUser.getUid());
        recyclerView.setAdapter(recyclerViewAdapter);


        ImageButton sendMessage = findViewById(R.id.engage_send_message);
        inputMessage = (TextInputEditText)findViewById(R.id.engage_chat_input);
        if (i.hasExtra("CHAT_TEXT")){


            this.CHAT_FROM_HOME = i.getStringExtra("CHAT_TEXT");
            inputMessage.setText(CHAT_FROM_HOME);
            Log.d("testing", "onClick: " + CHAT_FROM_HOME);
        }
        loadChatMessages();

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                String textMsg = inputMessage.getText().toString();
                sendChatMessage(textMsg);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener);
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.engage_page).setChecked(true);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendChatMessage(String msg){
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        User user = db.getLoggedUser(currentUser.getUid());

        if (!TextUtils.isEmpty(msg)){
            String key = myDBRef.child("messages").push().getKey();//create message ID
            Chat chat = new Chat(msg,user.getUid(),user.getNickName(),getCurrentDateAndTime());
            Map<String, Object> chatVals = chat.toMap();


            assert key != null;
            myDBRef.child("chats").child(key).setValue(chatVals).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    inputMessage.setText("");
                    Toast.makeText(EngageActivity.this,"New message Added",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EngageActivity.this,"Action Failed",Toast.LENGTH_LONG).show();
                }
            });


        }
    }

    public void loadChatMessages(){

        myDBRef.child("chats").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                Chat chatSnapShot = snapshot.getValue(Chat.class);
                chatList.add(chatSnapShot);
                recyclerViewAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(Objects.requireNonNull(recyclerView.getAdapter()).getItemCount() -1);

                Log.d("testing", "onChildAdded: " + snapshot.getValue());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.engage_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.engage_breathe_action) {

            openBreathingActivity();
            return true;
        }
        if (id == R.id.engage_confession_action){

            openConfessionActivity();
            return true;
        }
        if (id == R.id.kill_switch){

            killEngagement();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openConfessionActivity() {
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        final User user = db.getLoggedUser(currentUser.getUid());


        final List<Affirmation> allAfirmations = AffirmationData.initAffirmEntryList(getResources());
        final int affirmationListSize = allAfirmations.size();
        Random random = new Random();
        final int randVal = random.nextInt(affirmationListSize - 1+1) + 1;

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View dialogView = layoutInflater.inflate(R.layout.activity_affirmation_dialog,null);

        final Button startAffirmation = (Button) dialogView.findViewById(R.id.start_affirmation);
        final MaterialCardView affrimCard = dialogView.findViewById(R.id.afrimation_card);
        final TextView affirmText = dialogView.findViewById(R.id.affirmation_text);

        startAffirmation.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Log.d("button", "onClick: wooo");

                affirmText.setText(allAfirmations.get(randVal).getPhrase());
                startAffirmation.setVisibility(View.INVISIBLE);
                affrimCard.setVisibility(View.VISIBLE);

                ActivityLogs createLog = new ActivityLogs();
                createLog.setDate(getCurrentDateAndTime());
                createLog.setNickName(user.getNickName());
                createLog.setType("Affirmation Engagement");
                SaveActivityLogs.saveLog(createLog);

                Log.d("teashba", "onClick: " +  allAfirmations.get(randVal).getPhrase());
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Affirmation Activity")
                .setView(dialogView)
                .setNegativeButton("CLOSE",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void openBreathingActivity(){
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        final User user = db.getLoggedUser(currentUser.getUid());

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View dialogView = layoutInflater.inflate(R.layout.activity_breating_dialog, null);

        final Button startBreathing = (Button) dialogView.findViewById(R.id.start_breathing);
        final GifImageView breathingGif = dialogView.findViewById(R.id.breathing_gif);

        startBreathing.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Log.d("button", "onClick: wooo");

                startBreathing.setVisibility(View.INVISIBLE);
                breathingGif.setVisibility(View.VISIBLE);
                breathingGif.setImageResource(R.drawable.breathing);

                ActivityLogs createLog = new ActivityLogs();
                createLog.setDate(getCurrentDateAndTime());
                createLog.setNickName(user.getNickName());
                createLog.setType("Breathing Engagement");
                SaveActivityLogs.saveLog(createLog);

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Breathing Activity")
                .setView(dialogView)
                .setNegativeButton("CLOSE",null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getCurrentDateAndTime(){
        //String currentDateTimeString =   java.text.DateFormat.getDateTimeInstance().format(new Date());
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("EE, MMM dd, yyyy hh:mm a").format(new Date());

        return date;
    }

    private void killEngagement(){
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
        final User user = db.getLoggedUser(currentUser.getUid());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kill Engagement")
                .setMessage("Tapping the KILL button will automatically close this application. You can choose to come back any time you deem convenient. \n\n Do you want to stop this process?")
                .setNegativeButton("CLOSE",null)
                .setPositiveButton("KILL", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityLogs createLog = new ActivityLogs();
                        createLog.setDate(getCurrentDateAndTime());
                        createLog.setNickName(user.getNickName());
                        createLog.setType("Kill Engagement");
                        SaveActivityLogs.saveLog(createLog);

                        finishAffinity();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}