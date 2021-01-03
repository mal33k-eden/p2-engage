package uk.ac.tees.v8218996.p2_engage.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

import uk.ac.tees.v8218996.p2_engage.R;
import uk.ac.tees.v8218996.p2_engage.model.Chat;

public class EngagementRecyclerViewAdapter extends RecyclerView.Adapter<EngagementRecyclerViewAdapter.ViewHolder> {
    private Context context;
    public List<Chat> messageList;
    public String userUuid;

    public EngagementRecyclerViewAdapter(Context context, List<Chat> messageList,String uuid) {
        this.context = context;
        this.messageList= messageList;
        this.userUuid= uuid;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_engage_row,parent,false);


        return new ViewHolder(view,context);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Chat message = messageList.get(position);


        if(message.getUuid().equals(userUuid)){
            holder.senderMessage.setText(message.getMsg());
            holder.sent_at.setText(message.getSentTime());
            holder.senderMessage.setBackgroundResource(R.drawable.engage_user_chat_bg);
            holder.sender.setText("From You");
        }else{

            holder.senderMessage.setText(message.getMsg());
            holder.sent_at.setText(message.getSentTime());
            holder.senderMessage.setBackgroundResource(R.drawable.engage_chat_bg);
            holder.sender.setText("From " + message.getSender());
        }

    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView senderMessage;
        public TextView sender;
        public TextView sent_at;

        public ViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);

            context = ctx;

            senderMessage = itemView.findViewById(R.id.sent_from);
            sender = itemView.findViewById(R.id.sent_by);
            sent_at = itemView.findViewById(R.id.sent_at);

        }

        @Override
        public void onClick(View v) {

        }
    }
}
