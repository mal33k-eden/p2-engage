package uk.ac.tees.v8218996.p2_engage.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uk.ac.tees.v8218996.p2_engage.R;
import uk.ac.tees.v8218996.p2_engage.model.Journal;


public class JournalRecyclerViewAdapter extends RecyclerView.Adapter<JournalRecyclerViewAdapter.ViewHolder> {

    private List<Journal> journalList;
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

    @Override
    public void onBindViewHolder(@NonNull JournalRecyclerViewAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

        }

        @Override
        public void onClick(View v) {

        }
    }
}
