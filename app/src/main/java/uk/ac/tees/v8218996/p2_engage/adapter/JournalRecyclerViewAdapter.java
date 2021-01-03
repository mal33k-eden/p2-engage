package uk.ac.tees.v8218996.p2_engage.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.MessageFormat;
import java.util.List;

import uk.ac.tees.v8218996.p2_engage.EngageActivity;
import uk.ac.tees.v8218996.p2_engage.R;
import uk.ac.tees.v8218996.p2_engage.data.MoodData;
import uk.ac.tees.v8218996.p2_engage.model.Journal;
import uk.ac.tees.v8218996.p2_engage.model.Mood;


public class JournalRecyclerViewAdapter extends RecyclerView.Adapter<JournalRecyclerViewAdapter.ViewHolder> {

    private final List<Journal> journalList;
    private Context context;


    public JournalRecyclerViewAdapter(Context context, List<Journal> journalList) {
        this.context= context;
        this.journalList = journalList;


    }

    @NonNull
    @Override
    public JournalRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.journal_row,parent,false);

        return new ViewHolder(view,context);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull JournalRecyclerViewAdapter.ViewHolder holder, int position) {



        Journal journal = journalList.get(position);

        holder.journalDate.setText(journal.getDate());
        holder.journalTitle.setText(journal.getTitle());
        holder.journalBody.setText(journal.getBody());
        holder.journalMood.setText(journal.getMood());
        String moodText = journal.getMood();
        String[] arr = moodText.split(" ",2);
        String icon= "emoji "+arr[1];
        String uri = "@drawable/"+icon.toLowerCase().replaceAll(" ","_");
        Log.d("TAG", uri);
        int imgResource = context.getApplicationContext().getResources().getIdentifier(uri, null, "uk.ac.tees.v8218996.p2_engage");
        holder.journalMood.setChipIconResource(imgResource);

        int privateRes = context.getApplicationContext().getResources().getIdentifier("@drawable/ic_locked", null, "uk.ac.tees.v8218996.p2_engage");
        int publicRes = context.getApplicationContext().getResources().getIdentifier("@drawable/ic_lock_open", null, "uk.ac.tees.v8218996.p2_engage");
        Boolean journalIsPrivate = journal.getPrivate();
        if (journalIsPrivate){
            holder.journalStatus.setText("Private");
            holder.journalStatus.setChipIconResource(privateRes);
            holder.journalStatus.setChipIconTint(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.colorAccent)));
        }else{
            holder.journalStatus.setText("Public");
            holder.journalStatus.setChipIconResource(publicRes);
        }




    }

    @Override
    public int getItemCount() {
        return journalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView journalDate;
        public TextView journalTitle;
        public TextView journalBody;
        public Chip journalMood;
        public Chip journalStatus;
        public Button deleteJournal;

        private FirebaseAuth mAuth;
        private FirebaseUser currentUser;

        private final FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
        DatabaseReference myJournalDBRef = firebaseDB.getReference().child("journals");
        DatabaseReference myUserJournalDBRef = firebaseDB.getReference().child("user-journals");



        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;


            journalDate = itemView.findViewById(R.id.journal_date);
            journalTitle= itemView.findViewById(R.id.journal_title);
            journalBody= itemView.findViewById(R.id.journal_body);
            journalMood= itemView.findViewById(R.id.journal_mood);
            journalStatus= itemView.findViewById(R.id.journal_status);
            deleteJournal = itemView.findViewById(R.id.deleteButton);


            deleteJournal.setOnClickListener(this);

            //initialize firestore
            mAuth = FirebaseAuth.getInstance();
            this.currentUser = mAuth.getCurrentUser();

            this.myUserJournalDBRef = myUserJournalDBRef.child(currentUser.getUid());

        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Journal journal = journalList.get(position);


            switch (v.getId()){

                case R.id.deleteButton:

                    deleteJournalRequest(journal,context);
                    break;

            }
        }

        private void deleteJournalRequest(final Journal journal, Context ctx) {

            final String journalKey = journal.getId();
            final Boolean journalStatus = journal.getPrivate();



            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setTitle("Delete Journal")
                    .setMessage("Are you sure you want to delete this journal?")
                    .setNegativeButton("CLOSE",null)
                    .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (!journalStatus){

                                myJournalDBRef.child(journalKey).removeValue();
                                myUserJournalDBRef.child(journalKey).removeValue();
                            }else{
                                myUserJournalDBRef.child(journalKey).removeValue();
                            }
                            Log.d("dialoi", "onClick: " + journalKey);
                            Toast.makeText(context,"Journal removed",Toast.LENGTH_LONG).show();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
    }
}
