package com.example.pavan.snapchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;


public class SecondActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    ListView listView;
    ArrayList<String> emails=new ArrayList<>();
    ArrayList<DataSnapshot> snapshots=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar=findViewById(R.id.my_tool_bar);
        setSupportActionBar(toolbar);
        firebaseAuth=FirebaseAuth.getInstance();

        listView=findViewById(R.id.yourSnapsList);
        arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,emails);
        listView.setAdapter(arrayAdapter);


        FirebaseDatabase.getInstance().getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).child("snaps").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String email=String.valueOf(dataSnapshot.child("from").getValue());
                emails.add(email);
                snapshots.add(dataSnapshot);
                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                //when removing a value it will be called
                int index=0;
                for (DataSnapshot snap:snapshots) {
                        if(Objects.equals(snap.getKey(), dataSnapshot.getKey())){
                            snapshots.remove(index);
                            emails.remove(index);
                        }
                        index++;
                }
                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(SecondActivity.this,messages.get(position),Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(SecondActivity.this,DisplaySnapActivity.class);
                DataSnapshot dataSnapshot=snapshots.get(position);
                intent.putExtra("imageName",dataSnapshot.child("imageName").getValue().toString());
                intent.putExtra("imageURL",dataSnapshot.child("imageURL").getValue().toString());
                intent.putExtra("message",dataSnapshot.child("message").getValue().toString());
                intent.putExtra("snapKey",dataSnapshot.getKey());
                startActivity(intent);


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.newSnap){

            Intent intent=new Intent(this,NewSnapActivity.class);
            startActivity(intent);

        }else if(item.getItemId() == R.id.logout){
            new AlertDialog.Builder(this)
                    .setTitle("Are You Sure?")
                    .setMessage("Do you Want to Logout :( ")
                    .setPositiveButton("Yeah", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            firebaseAuth.signOut();
                            finish();
                        }
                    }).setNegativeButton("No ;) ",null)
                    .setCancelable(false)
                    .show();
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this)
                .setTitle("Are You Sure?")
                .setMessage("Do you Want to Logout :( ")
                .setPositiveButton("Yeah", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                        finish();
                    }
                }).setNegativeButton("No ;) ",null)
                .setCancelable(false)
                .show();
       // super.onBackPressed();
    }
}
