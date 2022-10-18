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
        Group group = new Group(groupnumber,groupname);
        Map<String,Object> groupmap = new HashMap<>();
        groupmap.put(groupnumber, group);
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                getCurrentUser().getUid()).child("Group").updateChildren(groupmap);
    }

        public void openViewGroup () {
            Intent intent = new Intent(this, ViewGroup.class);
            startActivity(intent);
        }

}