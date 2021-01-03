package uk.ac.tees.v8218996.p2_engage.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import uk.ac.tees.v8218996.p2_engage.EngageActivity;
import uk.ac.tees.v8218996.p2_engage.R;
import uk.ac.tees.v8218996.p2_engage.model.ActivityLogs;
import uk.ac.tees.v8218996.p2_engage.model.Journal;
import uk.ac.tees.v8218996.p2_engage.util.SaveActivityLogs;

public class CommunityRecyclerViewAdapter extends RecyclerView.Adapter<CommunityRecyclerViewAdapter.ViewHolder> {
    private List<Journal> journalList;
    private Context context;

    public CommunityRecyclerViewAdapter( Context context,List<Journal> journalList) {
        this.journalList = journalList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommunityRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_row,parent,false);

        return new ViewHolder(view, context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CommunityRecyclerViewAdapter.ViewHolder holder, int position) {

        final Journal journal = journalList.get(position);
        holder.journalDate.setText(journal.getDate());
        holder.journalTitle.setText(journal.getTitle());
        holder.journalBody.setText(journal.getBody());
        holder.journalMood.setText(journal.getMood());
        holder.beginEngage.setText("Engage " + journal.getAuthor());
        String moodText = journal.getMood();
        String[] arr = moodText.split(" ",2);
        String icon= "emoji "+arr[1];
        String uri = "@drawable/"+icon.toLowerCase().replaceAll(" ","_");
        Log.d("TAG", uri);
        int imgResource = context.getApplicationContext().getResources().getIdentifier(uri, null, "uk.ac.tees.v8218996.p2_engage");
        holder.journalMood.setChipIconResource(imgResource);

        holder.beginEngage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {

                ActivityLogs createLog = new ActivityLogs();
                createLog.setDate(getCurrentDateAndTime());
                createLog.setNickName(journal.getAuthor());
                createLog.setType("Community Engagement");

                SaveActivityLogs.saveLog(createLog);

                Intent intent = new Intent(context, EngageActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getCurrentDateAndTime(){
        //String currentDateTimeString =   java.text.DateFormat.getDateTimeInstance().format(new Date());
        @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

        return date;
    }
    @Override
    public int getItemCount() {
        return journalList.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView journalDate;
        public TextView journalTitle;
        public TextView journalBody;
        public Chip journalMood;
        public Chip beginEngage;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            journalDate = itemView.findViewById(R.id.community_journal_date);
            journalTitle= itemView.findViewById(R.id.community_journal_title);
            journalBody= itemView.findViewById(R.id.community_journal_body);
            journalMood= itemView.findViewById(R.id.community_journal_mood);
            beginEngage= itemView.findViewById(R.id.community_begin_engage);


        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d("kjhgfdsa", String.valueOf(v.getId()));

            if (v.getId() == R.id.community_begin_engage) {

            }
        }
    }
}
