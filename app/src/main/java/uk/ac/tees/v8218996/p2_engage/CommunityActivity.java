package uk.ac.tees.v8218996.p2_engage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
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

import uk.ac.tees.v8218996.p2_engage.adapter.CommunityRecyclerViewAdapter;
import uk.ac.tees.v8218996.p2_engage.adapter.JournalRecyclerViewAdapter;
import uk.ac.tees.v8218996.p2_engage.model.Journal;
import uk.ac.tees.v8218996.p2_engage.ui.BottomTabMenu;

public class CommunityActivity extends BottomTabMenu {

    private RecyclerView recyclerView;
    private CommunityRecyclerViewAdapter recyclerViewAdapter;
    public List<Journal> journalList;
    private MaterialToolbar toolbar;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    public Chip communityBeginEngage;

    private FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
    private DatabaseReference myDBRef = firebaseDB.getReference().child("journals");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        this.toolbar = findViewById(R.id.communitytopAppBar);
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
        //this.myDBRef = myDBRef.child(currentUser.getUid());

        journalList = new ArrayList<>();
        recyclerView = findViewById(R.id.communityRecyclerContainer);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewAdapter = new CommunityRecyclerViewAdapter(this, journalList);
        recyclerView.setAdapter(recyclerViewAdapter);
        myDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("onDataChange", String.valueOf(snapshot));
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Journal journalSnapShot = snapshot1.getValue(Journal.class);

                    journalList.add(journalSnapShot);
                    Log.d("onDataChange", journalSnapShot.getBody());
                }
                Collections.reverse(journalList);
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener);
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.community_page).setChecked(true);
    }
}