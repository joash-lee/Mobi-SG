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
import java.util.Random;

public class JoinGroup extends AppCompatActivity {
    private static final CharSequence ALLOWED_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
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
                checkGroupNumber();}
            private void checkGroupNumber() {
                String groupNumber = editGroupNumber.getText().toString().trim();
                if (groupNumber.isEmpty()) {
                    editGroupNumber.setError("Group Number is Required");
                    editGroupNumber.requestFocus();
                    return;}
                FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
/*                            System.out.println(snapshot);*/
                            String groupname;
                            if (snapshot.hasChild("Group")) {
                                if (snapshot.child("Group").child(groupNumber).exists()) {
                                    groupname = snapshot.child("Group").child(groupNumber).child("groupname").getValue().toString();
                                    addGroupToUser(groupNumber, groupname);
                                    Toast.makeText(JoinGroup.this, "Group " + groupNumber + " joined successfully", Toast.LENGTH_LONG).show();
                                    break;
                                }else{
                                    Toast.makeText(JoinGroup.this, "Group " + groupNumber + " does not exist", Toast.LENGTH_LONG).show();
                                    break;
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                openHomePage();
            }
        });
    }
    public void openHomePage() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
    public void addGroupToUser(String groupnumber, String groupname) {
        FirebaseDatabase.getInstance().getReference().child("Users")
                .addValueEventListener(new ValueEventListener() {
                    //addlistenerforsinglevalueevent
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            if (snapshot.child("Group").child(groupnumber).exists()) {
                                Map<String, Object> groupmap = new HashMap<>();
                                groupmap.put(groupnumber,snapshot.child("Group").child(groupnumber).getValue());
                                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                                        getCurrentUser().getUid()).child("Group").updateChildren(groupmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                    }
                                });
                                break;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        checkCurrentUserEV(groupnumber);
    }

    public void checkCurrentUserEV(String groupnumber){
        DatabaseReference flagref = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getRef();
        boolean pleaseworkflag;
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            //addListenerForSingleValueEvent
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals("EV")) {
                        addevtouser(groupnumber);
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public void addevtouser(String groupnumber) {

        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            //addlistenerforsinglevalueevent
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals("EV")) {
                        EV ev1 = new EV(snapshot.child("chargeStatus").getValue().toString(), snapshot.child("colour").getValue().toString(), snapshot.child("model").getValue().toString(), Integer.parseInt(snapshot.child("batteryStatus").getValue().toString()), Boolean.parseBoolean(snapshot.child("manualInput").getValue().toString()));
/*                        Map<String, Object> evmap = new HashMap<>();*/
/*                        String evtext = groupNumberGenerator();*/
                            String evtext = groupNumberGenerator();
                            Map<String,Object> evmap = new HashMap<>();
                            evmap.put(evtext,ev1);
                        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    if (snapshot.child("Group").hasChild(groupnumber)) {
                                        if (snapshot.child("Group").hasChild(groupnumber)) {
                                            snapshot.child("Group").child(groupnumber).getRef().updateChildren(evmap);
                                            System.out.println("NEED TO ADD THE NEW EV HERE");
/*                                            FirebaseDatabase.getInstance().getReference("Users").child("Group").child(groupnumber).updateChildren(evmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                }
                                            });*/
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
/*                        evmap.put("EV1", ev1);*/
                        break;
                    }
                }
            };
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


/*



        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.hasChild("Group")) {
                        if (snapshot.child("Group").hasChild(groupnumber)) {
                            DatabaseReference ref2 = snapshot.child("Group").child(groupnumber).getRef();
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                //addlistenerforsinglevalueevent
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        if (snapshot.getKey().equals("EV")) {
                                            EV ev1 = new EV(snapshot.child("chargeStatus").getValue().toString(), snapshot.child("colour").getValue().toString(), snapshot.child("model").getValue().toString(), Integer.parseInt(snapshot.child("batteryStatus").getValue().toString()), Boolean.parseBoolean(snapshot.child("manualInput").getValue().toString()));
                                            Map<String, Object> evmap = new HashMap<>();
                                            String evtext = groupNumberGenerator();
                                            evmap.put(evtext,ev1);
*//*                                            evmap.put("EV1", ev1);*//*
                                            ref2.updateChildren(evmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    System.out.println("Donezo");
                                                }
                                            });
                                            break;
                                        }
                                    }
                                };

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
        });*/
    }
    public static String groupNumberGenerator() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999999);

        // this will convert any number sequence into 6 character.
        return String.format("%09d", number);
    }

}