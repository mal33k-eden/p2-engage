package uk.ac.tees.v8218996.p2_engage.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import uk.ac.tees.v8218996.p2_engage.CommunityActivity;
import uk.ac.tees.v8218996.p2_engage.EngageActivity;
import uk.ac.tees.v8218996.p2_engage.HomeActivity;
import uk.ac.tees.v8218996.p2_engage.JournalActivity;
import uk.ac.tees.v8218996.p2_engage.R;



public class BottomTabMenu extends AppCompatActivity {

    public BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.home){
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);

                        return true;
                    }

                    if (item.getItemId() == R.id.journal_page){
                        Log.d("testtst", "onNavigationItemSelected: ");
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), JournalActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);

                        return true;
                    }
                    if (item.getItemId() == R.id.engage_page){
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), EngageActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);

                        return true;
                    }
                    if (item.getItemId() == R.id.community_page){
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(), CommunityActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent);

                        return true;
                    }
                    return false;
                }
            };
}
