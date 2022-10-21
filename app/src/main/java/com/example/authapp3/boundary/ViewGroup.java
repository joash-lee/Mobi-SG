package com.example.authapp3.boundary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authapp3.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

public class ViewGroup extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_group);
            Button backButton = findViewById(R.id.Back);
            backButton.setOnClickListener(view -> ViewGroup.this.finish());
            EditText groupNumberText = findViewById(R.id.viewGroupNumber);
            Button groupButton = findViewById(R.id.viewgroupbutton);
/*        TextView groupName = findViewById(R.id.groupName);
        groupName.setText("Pegasus");*/
            groupButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkIfUserInGroup();
                }

                private void checkIfUserInGroup() {
                    String groupNumber = groupNumberText.getText().toString().trim();
                    if (groupNumber.isEmpty()) {
                        groupNumberText.setError("Group Number is Required");
                        groupNumberText.requestFocus();
                        return;
                    }
/*                    FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                System.out.println("First one: "+ snapshot);
                                String groupname;
                                if (snapshot.hasChild("Group")) {
                                    if (snapshot.child("Group").child(groupNumber).exists()) {
                                        groupname = snapshot.child("Group").child(groupNumber).child("groupname").getValue().toString();
                                        System.out.println("WTF is gg on: " + groupname);
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });*/
                    FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().
                            getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean flag = false;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    if (snapshot.child(groupNumber).exists()) {
                                        flag = true;
                                        displayGroupEV(groupNumber);
                                        //groupname = snapshot.child("Group").child(groupNumber).child("groupname").getValue().toString();
                                }

                            }
                            if (flag==false){
                                Toast.makeText(ViewGroup.this, "User is not in group: " + groupNumber, Toast.LENGTH_LONG).show();
                                openHomePage();
                            }

                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

/*                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                            getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (snapshot.getKey().equals("Group")) {
                                    System.out.println("This is the value" + snapshot.child("xy"));
                                    System.out.println("Please work" + snapshot.child("Group").getValue());
                                    if (snapshot.child("Group").hasChild(groupNumber)) {
                                        System.out.println("User exists in the group");
                                        displayGroupEV(groupNumber);
                                        break;
                                    }
                                }
                            }
                            Toast.makeText(ViewGroup.this, "User is not in group: " + groupNumber, Toast.LENGTH_LONG).show();
                            openHomePage();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });*/
                }





            });
        }
    public void openHomePage() {
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
    private void displayGroupEV(String groupNumber) {
        System.out.println("Display your EVS");
        Toast.makeText(ViewGroup.this, "Display Group EVs",Toast.LENGTH_LONG).show();
    }


    }
