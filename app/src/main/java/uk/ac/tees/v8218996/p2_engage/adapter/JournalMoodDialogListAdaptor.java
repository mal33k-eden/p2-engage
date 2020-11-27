package uk.ac.tees.v8218996.p2_engage.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import uk.ac.tees.v8218996.p2_engage.R;
import uk.ac.tees.v8218996.p2_engage.model.Mood;

public class JournalMoodDialogListAdaptor extends ArrayAdapter<Mood> {
    private final Context context;
    private final List<Mood> moodList;

    public JournalMoodDialogListAdaptor(@NonNull Context context, int resource, @NonNull List<Mood> objects) {
        super(context, resource, objects);
        this.context = context;
        this.moodList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.journal_mood_listview_layout, parent, false);
        ImageView moodIcon = rowView.findViewById(R.id.mood_icon);
        TextView moodName = rowView.findViewById(R.id.mood_name);
        Mood mood = moodList.get(position);

        moodName.setText(mood.getName());
        String icon = mood.getIcon();
        String uri = "@drawable/"+icon;

        int imgResource = context.getApplicationContext().getResources().getIdentifier(uri, null, "uk.ac.tees.v8218996.p2_engage");

        moodIcon.setImageResource(imgResource);

        return rowView;
    }
}
