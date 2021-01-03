package uk.ac.tees.v8218996.p2_engage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import uk.ac.tees.v8218996.p2_engage.adapter.JournalRecyclerViewAdapter;
import uk.ac.tees.v8218996.p2_engage.model.Journal;
import uk.ac.tees.v8218996.p2_engage.ui.BottomTabMenu;


public class JournalActivity extends BottomTabMenu {

    private RecyclerView recyclerView;
    private JournalRecyclerViewAdapter recyclerViewAdapter;
    private List<Journal> journalList;
    public ImageView journalOpenBottomSheetIcon;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private TextView journalCount;
    private MaterialToolbar appBar;

    private FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
    DatabaseReference myDBRef = firebaseDB.getReference().child("user-journals");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);
        //initialize firestore
        mAuth = FirebaseAuth.getInstance();
        this.currentUser = mAuth.getCurrentUser();
        this.myDBRef = myDBRef.child(currentUser.getUid());


        journalOpenBottomSheetIcon = findViewById(R.id.icon_open_journal_sheet);
        journalCount = findViewById(R.id.mood_journal_count);
        appBar = findViewById(R.id.journal_topAppBar);

        appBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
            }
        });


        journalList = new ArrayList<>();
        recyclerView = findViewById(R.id.journalRecyclerContainer);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewAdapter = new JournalRecyclerViewAdapter(this, journalList);
        recyclerView.setAdapter(recyclerViewAdapter);
       myDBRef.addValueEventListener(new ValueEventListener() {
           @SuppressLint("SetTextI18n")
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {

               if(snapshot.getChildrenCount() > 1){
                   journalCount.setText((int) snapshot.getChildrenCount() + " Journals");
               }else{
                   journalCount.setText((int) snapshot.getChildrenCount() + " Journal");
               }

               journalList.clear();


               for (DataSnapshot snapshot1 : snapshot.getChildren()){
                   Journal journalSnapShot = snapshot1.getValue(Journal.class);

                   journalList.add(journalSnapShot);
                   Log.d("onDataChange", snapshot1.getKey());
               }
               Collections.reverse(journalList);
               recyclerViewAdapter.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

        Log.d("TAG", String.valueOf(journalList));





        journalOpenBottomSheetIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), JournalCreateActivity.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener);
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.journal_page).setChecked(true);

    }


    @Override
    protected void onStart() {
        super.onStart();



    }
}
