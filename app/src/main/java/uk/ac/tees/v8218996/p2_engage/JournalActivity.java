package uk.ac.tees.v8218996.p2_engage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ClipData;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.v8218996.p2_engage.adapter.JournalRecyclerViewAdapter;
import uk.ac.tees.v8218996.p2_engage.model.Journal;


public class JournalActivity extends AppCompatActivity  {

    private RecyclerView recyclerView;
    private JournalRecyclerViewAdapter recyclerViewAdapter;
    private List<Journal> journalList;
    public ImageView journalOpenBottomSheetIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal);

        journalOpenBottomSheetIcon = findViewById(R.id.icon_open_journal_sheet);

        journalList = new ArrayList<>();

        recyclerView = findViewById(R.id.journalRecyclerContainer);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewAdapter = new JournalRecyclerViewAdapter(this, journalList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();


        journalOpenBottomSheetIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                JournalFragment bottomSheetDialog = new JournalFragment();

                bottomSheetDialog.show(getSupportFragmentManager(),"exampleBottomSheet");

                /*final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        JournalActivity.this, R.style.BottomSheetDialogTheme
                );

                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                        R.layout.layout_bottom_sheet, (LinearLayout)findViewById(R.id.journalBottomSheetContainer)
                );

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();*/

            }
        });

    }
}
