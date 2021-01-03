package uk.ac.tees.v8218996.p2_engage.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import uk.ac.tees.v8218996.p2_engage.model.Info;
import uk.ac.tees.v8218996.p2_engage.model.Mood;

public class InfoListAdapter extends ArrayAdapter<Info> {
    public Context context;
    public List<Info> infoList;

    public InfoListAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this.context = context;
        this.infoList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("ViewHolder") View rowView = inflater.inflate(R.layout.information_listview_layout, parent, false);
        TextView title = rowView.findViewById(R.id.info_title);
        TextView des = rowView.findViewById(R.id.info_description);
       Info info = infoList.get(position);

        title.setText(info.getTitle());
        des.setText(info.getDescription());

        return rowView;
    }
}
