package uk.ac.tees.v8218996.p2_engage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements LoginFragment.OnFragmentInteractionListener {
    private FirebaseAuth mAuth;
    private static final String TAG = "AnonymousAuth";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.mainContainer, new LoginFragment())
                    .commit();
        }


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }








}
