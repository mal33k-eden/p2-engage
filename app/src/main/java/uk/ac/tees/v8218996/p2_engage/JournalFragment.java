package uk.ac.tees.v8218996.p2_engage;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

import uk.ac.tees.v8218996.p2_engage.adapter.JournalMoodDialogListAdaptor;
import uk.ac.tees.v8218996.p2_engage.data.MoodData;
import uk.ac.tees.v8218996.p2_engage.model.Mood;

public class JournalFragment extends BottomSheetDialogFragment {
    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.layout_bottom_sheet, container, false);

        Chip journal_add_mood_chip = v.findViewById(R.id.journal_add_mood_chip);

        journal_add_mood_chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Mood> allMoods = MoodData.initMoodEntryList(getResources());
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.journal_mood_layout);
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // this is optional
                }
                ListView listView = dialog.findViewById(R.id.journal_mood_listView);
                ArrayAdapter arrayAdapter = new JournalMoodDialogListAdaptor(getContext(),R.layout.journal_mood_listview_layout,allMoods);
                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("joii", "onItemClick: ");
                    }
                });
                dialog.show();
            }
        });

        return v;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
