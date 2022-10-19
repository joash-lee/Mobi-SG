package com.example.authapp3.boundary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.FillRequest;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.authapp3.R;
import com.example.authapp3.entity.Distance;
import com.example.authapp3.entity.EV;
import com.example.authapp3.entity.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class JoinGroup extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_join_group);
        Button backButton = findViewById(R.id.Back);
        backButton.setOnClickListener(view -> JoinGroup.this.finish());
        EditText editGroupNumber = findViewById(R.id.editGroupNumber);
        Button joinGroupButton = findViewById(R.id.joinGroupButton);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
        joinGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkGroupNumber();
            }

            private void checkGroupNumber() {

                String groupNumber = editGroupNumber.getText().toString().trim();
                if (groupNumber.isEmpty()) {
                    editGroupNumber.setError("Group Number is Required");
                    editGroupNumber.requestFocus();
                    return;
                }
                FirebaseDatabase.getInstance().getReference().child("Users")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    System.out.println(snapshot);
                                    String groupname;
                                    if (snapshot.hasChild("Group")){
                                        if (snapshot.child("Group").child(groupNumber).exists()){
            /*                                System.out.println(snapshot.child("Group").child(groupNumber).child("groupname").getValue());*/
                                            groupname = snapshot.child("Group").child(groupNumber).child("groupname").getValue().toString();
                                            addGroupToUser(groupNumber,groupname);
                                            Toast.makeText(JoinGroup.this,"Group "+groupNumber+" joined successfully",Toast.LENGTH_LONG).show();
                                            openHomePage();
                                        }
                                    }
                                }
                                openHomePage();
                                Toast.makeText(JoinGroup.this,"Group "+groupNumber+" does not exist",Toast.LENGTH_LONG).show();
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
            }
        );
    }
    public void openHomePage (){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
    public void addGroupToUser(String groupnumber, String groupname){
        FirebaseDatabase.getInstance().getReference().child("Users")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.child("Group").child(groupnumber).exists()){
                                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                                        getCurrentUser().getUid()).child("Group").setValue(snapshot.child("Group").getValue());
                                //NEED TO COPY THE GROUP HERE
                                break;
                            }

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
/*        Group group = new Group(groupnumber,groupname);
        Map<String,Object> groupmap = new HashMap<>();
        groupmap.put(groupnumber, group);
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                getCurrentUser().getUid()).child("Group").updateChildren(groupmap);*/
            addevtouser(groupnumber,groupname);

    }
    public void addevtouser(String groupnumber,String groupname){
        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            System.out.println(snapshot);
                            if (snapshot.hasChild("Group")){
                                if (snapshot.child("Group").hasChild(groupnumber)){
                                    DatabaseReference ref2 = snapshot.child("Group").child(groupnumber).getRef();
                                    System.out.println("Current ref");
                                    System.out.println(ref2);

                                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                if (snapshot.getKey().equals("EV")) {
                                                    EV ev1 = new EV(snapshot.child("chargeStatus").getValue().toString(),snapshot.child("colour").getValue().toString(),snapshot.child("model").getValue().toString(),Integer.parseInt(snapshot.child("batteryStatus").getValue().toString()),Boolean.parseBoolean(snapshot.child("manualInput").getValue().toString()));
                                                    Map<String, Object> evmap = new HashMap<>();
                                                    evmap.put("EV1",ev1);
                                                    /*                                System.out.println(snapshot.child("batteryStatus").getValue().toString());//20*/
 HB 
                                                    ref2.updateChildren(evmap).
                                                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    System.out.println("Donezo");
                                                                }
                                                            });
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });

                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

        public void openViewGroup () {
            Intent intent = new Intent(this, ViewGroup.class);
            startActivity(intent);
        }

}