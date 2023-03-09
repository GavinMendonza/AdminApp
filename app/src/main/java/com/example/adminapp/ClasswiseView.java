package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClasswiseView extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DatabaseReference myRef;
    FirebaseDatabase database;
    List<Item> items = new ArrayList<Item>();
    RecyclerView recyclerView;

    int spinnerValue=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classwise_view);
        recyclerView = findViewById(R.id.recyclerview);



//-------------------------------------------------------------------------------------------------

//spinner start
        Spinner dropdown = findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        String[] items1 = new String[]{"TYIT", "BMM", "TYBMS"};
        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items1);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
//spinner end

//-------------------------------------------------------------------------------------------------

//start of db ref creation
        database = FirebaseDatabase.getInstance("https://eccloginmoduletest-default-rtdb.asia-southeast1.firebasedatabase.app/");
        myRef = database.getReference("users");
//end of db ref creation

//-------------------------------------------------------------------------------------------------

    }


    //using this method to update recycler view
    public void updateRecycler(){

//-------------------------------------------------------------------------------------------------

        //this is used to read db once
        myRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                //if sucessful then,
                else {
                    HashMap value = (HashMap) task.getResult().getValue();

                    items.clear();  //clearing the array, basically the screen first using this
                    for (Object i: value.keySet()) {
                        if (Integer.parseInt(String.valueOf(((HashMap)value.get(i)).get("class")))==spinnerValue){
                            items.add(new Item(((HashMap)value.get(i)).get("name")+"",((HashMap)value.get(i)).get("email")+"",Integer.parseInt(String.valueOf(((HashMap)value.get(i)).get("score")))));
                        }
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(new MyAdapter(getApplicationContext(),items));
                }
            }
        });

//-------------------------------------------------------------------------------------------------


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String choice=adapterView.getItemAtPosition(i).toString();
        if (choice=="TYIT"){
            spinnerValue=100;
        }else if(choice=="BMM"){
            spinnerValue=101;
        }else if(choice=="BMS"){
            spinnerValue=102;
        }
        updateRecycler();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}