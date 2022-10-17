package com.example.authapp3.boundary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.authapp3.R;
import com.example.authapp3.entity.Distance;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JoinGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        Button backButton = findViewById(R.id.Back);
        backButton.setOnClickListener(view -> JoinGroup.this.finish());
        EditText editGroupNumber = findViewById(R.id.editGroupNumber);
        Button joinGroupButton = findViewById(R.id.joinGroupButton);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child("Group");
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

/*                if (groupNumber.equals("123456")) {
                    Toast.makeText(JoinGroup.this, "You have successfully joined the group!", Toast.LENGTH_LONG).show();
                    openViewGroup();
                } else {
                    Toast.makeText(JoinGroup.this, "Group does not exist.", Toast.LENGTH_LONG).show();
                }*/
            }
        });
    }

        public void openViewGroup () {
            Intent intent = new Intent(this, ViewGroup.class);
            startActivity(intent);
        }

}