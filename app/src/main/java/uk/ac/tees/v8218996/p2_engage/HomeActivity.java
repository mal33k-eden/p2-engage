package uk.ac.tees.v8218996.p2_engage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import uk.ac.tees.v8218996.p2_engage.adapter.InfoListAdapter;
import uk.ac.tees.v8218996.p2_engage.data.DatabaseHandler;
import uk.ac.tees.v8218996.p2_engage.model.Info;
import uk.ac.tees.v8218996.p2_engage.model.Journal;
import uk.ac.tees.v8218996.p2_engage.model.User;
import uk.ac.tees.v8218996.p2_engage.ui.BottomTabMenu;

public class HomeActivity extends BottomTabMenu {
    private FirebaseAuth mAuth;
    private String nickname;
    private ChipGroup engageChipGroup;
    private FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
    DatabaseReference myDBRef = firebaseDB.getReference().child("info");
    private List<Info> infoList = new ArrayList <>();
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        this.listView = findViewById(R.id.information_listview);

        String uuid = currentUser.getUid();
        User user = db.getLoggedUser(uuid);
        this.nickname = user.getNickName().toUpperCase();

        this.engageChipGroup = findViewById(R.id.engage_chip_group);
        loadEngagementChips();

        TextView userNickName = findViewById(R.id.home_user);
        userNickName.setText(nickname);

        MaterialCardView homeJournalCard = findViewById(R.id.home_journal_card);
        homeJournalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), JournalActivity.class);
                 startActivity(intent);
            }
        });




//        Chip chip = findViewById(R.id.chip_1);
//
//        chip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(getApplicationContext(), EngageActivity.class);
//                startActivity(intent);
//            }
//        });

        loadTeessideInfo();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener);
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(R.id.home).setChecked(true);


    }



    public void loadEngagementChips(){
        int id = 0;
        String[] genres = {"I feel stressed", "I need help", "Studies is difficult","My assignments are too much"};
        for(String genre : genres) {
            id++;
            Chip chip = new Chip(this);
            chip.setText(genre);
            chip.setChipBackgroundColorResource(R.color.colorPrimaryDark);
            chip.setTextColor(getColor(R.color.colorText));
            chip.setTextSize(18);
            chip.setClickable(true);
            chip.setCheckable(true);
            Log.d("id test", "loadEngagementChips: " + id);
            engageChipGroup.addView(chip);
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int chipsCount = engageChipGroup.getChildCount();
                    int i =0;

                    while (i < chipsCount){
                        Chip chip = (Chip) engageChipGroup.getChildAt(i);
                        if (chip.isChecked()){
                            chip.setChecked(false);
                            Intent intent = new Intent();
                            intent.setClass(getApplicationContext(), EngageActivity.class);
                            intent.putExtra("CHAT_TEXT",chip.getText().toString());
                            startActivity(intent);

                        }
                        i++;
                    }
                }
            });
        }



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.user_profile) {
            showLoggedInUserProfile();
        }

        return super.onOptionsItemSelected(item);
    }

    public void showLoggedInUserProfile(){
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = layoutInflater.inflate(R.layout.activity_home_profile_dialog,null);

        TextView userNickname = dialogView.findViewById(R.id.activity_home_profile_nickname);
        userNickname.setText("Logged In As " + nickname );

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(dialogView)
                .setNegativeButton("CLOSE",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void loadTeessideInfo(){



        myDBRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                infoList.clear();
               for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    Info infoSnapshot = snapshot1.getValue(Info.class);

                    infoList.add(infoSnapshot);

                }

                ArrayAdapter arrayAdapter = new InfoListAdapter(HomeActivity.this,R.layout.information_listview_layout,infoList);
               listView.setAdapter(arrayAdapter);
               listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                   @Override
                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       Info info = infoList.get(position);

                       openLinkInBrowser(info);

                   }
               });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void openLinkInBrowser(final Info info){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Open Link In Browser")
                .setMessage("Your selected link will be opened in your default browser. \n\n\nDo you want to continue?")
                .setNegativeButton("CLOSE",null)
                .setPositiveButton("OPEN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            URL URL = new URL(info.getLink());
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(URL)));
                            startActivity(browserIntent);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
