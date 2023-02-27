package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResultPage extends AppCompatActivity {

    TextView searchResultDisplay;

    HashMap dataSet;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    DatabaseReference myRef;
    FirebaseDatabase database;

    Button btnForRoleSwitch;

    int volunteerFlag=0;    //if the search result is a volunteer then 1, else 0
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_page);

        btnForRoleSwitch=findViewById(R.id.button4);
        searchResultDisplay=findViewById(R.id.textViewForSearchName);
        uid=getIntent().getStringExtra("uid");  //getting data from previous activity
        gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc= GoogleSignIn.getClient(this,gso);
        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(this); //here we save the data of user(admin)
                                                                                        // for future use if required

        database = FirebaseDatabase.getInstance("https://eccloginmoduletest-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = database.getReference("users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                HashMap value = (HashMap) snapshot.getValue();


//                checking if user exists       ///////////////////////////////////
                if (value.containsKey(uid)){
                    HashMap searchResult= (HashMap) value.get(uid);

                    String searchMsg="Name: "+searchResult.get("name")+"\n"
                                    +"email: "+searchResult.get("email")+"\n"
                                    +"class: "+searchResult.get("class");
                    if (Integer.parseInt(searchResult.get("volunteer")+"")==1){
                        searchMsg=searchMsg+"\nThis student is a volunteer";
                        volunteerFlag=1;
                        btnForRoleSwitch.setText("Remove volunteer access");
                    }else{
                        searchMsg=searchMsg+"\nThis student is NOT a volunteer";
                        volunteerFlag=0;
                        btnForRoleSwitch.setText("Give volunteer access");
                    }
                    searchResultDisplay.setText(searchMsg);
                }else{
                    searchResultDisplay.setText("Student not found... please check uid and try again\n" +
                            "Recieved uid: "+uid);
                    btnForRoleSwitch.setVisibility(View.GONE);
                }
//                end checking if user exists  //////////////////////////////////////////
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w("database error", "Failed to read value.", error.toException());
            }
        });
//        firebase code for reading ends here/////////////////////////////////////////////////

        btnForRoleSwitch.setOnClickListener(view -> {
            updateDB(uid,volunteerFlag);
        });
    }
    //        firebase code for writing starts here//////////////////////////////////////////////////////
    private void updateDB(String uid, int flag) { //dont actually need to take the second param, but chuck
        // Read from the database
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    dataSet = (HashMap) task.getResult().getValue();
//                    Log.d("firebase", "onComplete: "+((HashMap)dataSet.get(uid)).get("volunteer"));


                    if (flag==1){
                        myRef.child(uid).child("volunteer").setValue(0);
                        volunteerFlag=0;
                    }else {
                        myRef.child(uid).child("volunteer").setValue(1);
                        volunteerFlag=1;
                    }
                }
            }
        });

        Log.d("firebase", "updateDB: "+dataSet);
    }
//        firebase code for writing ends here//////////////////////////////////////////////////////


}