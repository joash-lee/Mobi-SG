package com.example.authapp3.boundary;

import static java.lang.Boolean.parseBoolean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authapp3.R;
import com.example.authapp3.entity.EV;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CreateGroup extends AppCompatActivity {
    private EditText evModelError, evColourError, evBatteryLevel;
    private ProgressBar progressBarEV;
    private Spinner spinnerEVModel, spinnerEVColour;
    private List<String> evModels, evColours;
    private String userid, selectedEVModel, selectedEVColour, selectedEVBatteryLevelText, evModel, chargingStatus = "Not Charging";
    private String[] evModelsList, evColoursList;
    private AlertDialog dialog;
    private DatabaseReference userReference;
    private FirebaseAuth mAuth;
    private DatabaseReference groupreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        userReference = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference evModelsReference = FirebaseDatabase.getInstance().getReference("EVModels");
        Intent intent = getIntent();
        userid = intent.getStringExtra("userID");
        mAuth = FirebaseAuth.getInstance();
        Button backButton = findViewById(R.id.Back);
        backButton.setOnClickListener(view -> CreateGroup.this.finish());
        TextView generatedGroupNumber = findViewById(R.id.generatedGroupNumber);
        Button createGroupButton = findViewById(R.id.createGroupButton);
        EditText editGroupName = findViewById(R.id.editGroupName);
        String groupnumber = groupNumberGenerator();
        generatedGroupNumber.setText(groupnumber);
        groupreference = FirebaseDatabase.getInstance().getReference();
        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroup();
            }
            private void createGroup() {
                String groupname = editGroupName.getText().toString().trim();
                if (groupname.isEmpty()) {
                    editGroupName.setError("Group Name is Required");
                    editGroupName.requestFocus();
                    return;
                }
                Group group = new Group(groupnumber, groupname);
                Map<String, Object> groupmap = new HashMap<>();
                groupmap.put(groupnumber, group);
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                        getCurrentUser().getUid()).child("Group").updateChildren(groupmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateGroup.this, "Group has been created", Toast.LENGTH_LONG).show();
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                                    addValueEventListener(new ValueEventListener() {
                                //addListenerForSingleValueEvent
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        if (snapshot.getKey().equals("EV")) {
                                            EV ev1 = new EV(snapshot.child("chargeStatus").getValue().toString()
                                                    ,snapshot.child("colour").getValue().toString()
                                                    ,snapshot.child("model").getValue().toString()
                                                    ,Integer.parseInt(snapshot.child("batteryStatus").getValue().toString())
                                                    ,Boolean.parseBoolean(snapshot.child("manualInput").getValue().toString()));
                                            Map<String, Object> evmap = new HashMap<>();
                                            evmap.put("Creator EV",ev1);
                                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().
                                                    getCurrentUser().getUid()).child("Group").child(groupnumber).updateChildren(evmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                }
                                            });
                                        }
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                }
                            });
                            openHomePage();
                        } else {
                            Toast.makeText(CreateGroup.this, "Group creation failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
    public static String groupNumberGenerator() {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        // this will convert any number sequence into 6 character.
        return String.format("%06d", number);
    }
    public void openHomePage (){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
}







/*                groupreference.push().setValue("Group");

                FirebaseDatabase.getInstance().getReference().child("Group").setValue(groupmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CreateGroup.this, "Group has been created", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(CreateGroup.this, "Group creation failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });*/
/*    private void checkAndLink() {
        evModelError.setError(null);
        evColourError.setError(null);
        if(selectedEVModel.equals("Model")) {
            evModelError.setError("Please select model!");
            evModelError.requestFocus();
            return;
        }
        if(selectedEVColour.equals("Colour")) {
            evColourError.setError("Please select colour!");
            evColourError.requestFocus();
            return;
        }
        int selectedEVBatteryLevelInt;
        try
        {
            selectedEVBatteryLevelInt = Integer.parseInt(selectedEVBatteryLevelText);
            if(selectedEVBatteryLevelInt < 0 | selectedEVBatteryLevelInt > 100) {
                evBatteryLevel.setError("Must be an integer between 0 to 100");
                evBatteryLevel.requestFocus();
                return;
            }
        }
        catch (NumberFormatException e)
        {
            evBatteryLevel.setError("Must be an integer between 0 to 100");
            evBatteryLevel.requestFocus();
            return;
        }
        progressBarEV.setVisibility(View.VISIBLE);
        EV ev = new EV(chargingStatus, selectedEVColour, selectedEVModel, selectedEVBatteryLevelInt, true);
        userReference.child(userid).child("EV").setValue(ev);
        Toast.makeText(CreateGroup.this, "EV successfully linked", Toast.LENGTH_LONG).show();
        progressBarEV.setVisibility(View.GONE);
        dialog.cancel();
    }*/
/*    private void linkEVDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final View evPopupView = getLayoutInflater().inflate(R.layout.popup_linkev, null);
        Button save = evPopupView.findViewById(R.id.link_ev_save);
        Button cancel = evPopupView.findViewById(R.id.link_ev_cancel);

        evModelError = evPopupView.findViewById(R.id.evModelError);
        evColourError = evPopupView.findViewById(R.id.evColourError);
        evBatteryLevel = evPopupView.findViewById(R.id.evBatteryLevel);
        progressBarEV = evPopupView.findViewById(R.id.progressBarEV);

        spinnerEVModel = evPopupView.findViewById(R.id.spinnerEVModel);

        ArrayAdapter<String> adapterEVModel = new ArrayAdapter<>(CreateGroup.this, android.R.layout.simple_spinner_item, evModelsList);
        adapterEVModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEVModel.setAdapter(adapterEVModel);

        spinnerEVColour = evPopupView.findViewById(R.id.spinnerEVColour);

        ArrayAdapter<String> adapterEVColour = new ArrayAdapter<>(CreateGroup.this, android.R.layout.simple_spinner_item, evColoursList);
        adapterEVColour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEVColour.setAdapter(adapterEVColour);

        dialogBuilder.setView(evPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        save.setOnClickListener(view -> {
            selectedEVModel = spinnerEVModel.getSelectedItem().toString();
            selectedEVColour = spinnerEVColour.getSelectedItem().toString();
            selectedEVBatteryLevelText = evBatteryLevel.getText().toString().trim();
            checkAndLink();
        });

        cancel.setOnClickListener(view -> dialog.cancel());
    }*/
/*                    FirebaseDatabase.getInstance().getReference().child("Users")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                String groupname;
                if (snapshot.hasChild("Group")){
                    if (snapshot.child("Group").child(groupNumber).exists()){
                        *//*                                System.out.println(snapshot.child("Group").child(groupNumber).child("groupname").getValue());*//*
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
    });*/
/*        evModels = new ArrayList<>();
        evModelsReference.child("ModelList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                evModels.clear();
                evModels.add("Model");
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String model = postSnapshot.getValue(String.class);
                    evModels.add(model);
                }
                evModelsList = evModels.toArray(new String[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        evColours = new ArrayList<>();
        evModelsReference.child("ColourList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                evColours.clear();
                evColours.add("Colour");
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String colour = postSnapshot.getValue(String.class);
                    evColours.add(colour);
                }
                evColoursList = evColours.toArray(new String[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        linkGroupVehicleBorder.setOnClickListener(view -> linkEVDialog());*/