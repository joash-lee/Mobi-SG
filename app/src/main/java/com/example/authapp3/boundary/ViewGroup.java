package com.example.authapp3.boundary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.authapp3.R;

public class ViewGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);

        Button backButton = findViewById(R.id.Back);
        backButton.setOnClickListener(view -> ViewGroup.this.finish());
        TextView groupName = findViewById(R.id.groupName);

        groupName.setText("Pegasus");
    }
}