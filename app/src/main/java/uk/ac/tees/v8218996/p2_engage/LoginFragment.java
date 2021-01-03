package uk.ac.tees.v8218996.p2_engage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;
import uk.ac.tees.v8218996.p2_engage.data.DatabaseHandler;
import uk.ac.tees.v8218996.p2_engage.model.User;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
    DatabaseReference myDBRef = firebaseDB.getReference();

    private static final String TAG = "AnonymousAuth";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        MaterialButton loginButton = view.findViewById(R.id.login_button);

        TextInputLayout textInputLayout = view.findViewById(R.id.nicknameText);
        final Editable userName = textInputLayout.getEditText().getText();


        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d("nicename", String.valueOf(userName));

                signInAnonymously(String.valueOf(userName));
            }
        });

        // Clear the error once more than 8 characters are typed.


        return view;
    }

    private boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 8;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }
    private void signInAnonymously(final String nickName) {

        // [START signin_anonymously]
        try {
            mAuth.signInAnonymously().addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInAnonymously:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                //store user
                               saveUserInDb(user,nickName);
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInAnonymously:failure", task.getException());
//                            Toast.makeText(AnonymousAuthActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                        }
                    });
            // [END signin_anonymously]
        }catch (Exception ex){
            Log.e("trycatcherror", ex.getLocalizedMessage().toString());
        }

    }

    private void saveUserInDb(FirebaseUser user,String nickName) {

        DatabaseHandler db = new DatabaseHandler(getContext());

        User newUser = new User();

        newUser.setNickName(nickName);
        newUser.setUid(user.getUid());

        db.addUser(newUser);

        //FIREBASE WRITE
        myDBRef.child("users").child(user.getUid()).setValue(newUser);
    }

    private void updateUI(FirebaseUser currentUser ) {
        boolean isSignedIn = (currentUser != null);


        if(isSignedIn){
            //user is signed in
            Log.d(TAG,  currentUser.getUid());
            Intent intent = new Intent();
            intent.setClass(getActivity(), HomeActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();

        }else{
            //user is not signed in
            Log.w(TAG, "signInAnonymously:failure -> not logged in");
        }
    }
    @Override
    public  void onStart() {
        super.onStart();

    }
}
