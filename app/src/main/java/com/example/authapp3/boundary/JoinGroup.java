package com.example.authapp3.boundary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.authapp3.R;

public class JoinGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        Button backButton = findViewById(R.id.Back);
        backButton.setOnClickListener(view -> JoinGroup.this.finish());
        EditText editGroupNumber = findViewById(R.id.editGroupNumber);
        Button joinGroupButton = findViewById(R.id.joinGroupButton);

        String groupNumber =  editGroupNumber.getText().toString();
        joinGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkGroupNumber(groupNumber);
            }
        });
    }

    public void checkGroupNumber(String groupNumber){
        if(groupNumber.equals("123456")){
            Toast.makeText(JoinGroup.this, "You have successfully joined the group!", Toast.LENGTH_LONG).show();
            openViewGroup();
        }else{
            Toast.makeText(JoinGroup.this, "Group does not exist.", Toast.LENGTH_LONG).show();
        }
    }
    public void openViewGroup (){
        Intent intent = new Intent(this, ViewGroup.class);
        startActivity(intent);
    }
}