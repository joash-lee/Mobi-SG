package com.example.authapp3.boundary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.authapp3.R;

public class ContactUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Button backButton = findViewById(R.id.Back);
        backButton.setOnClickListener(view -> ContactUs.this.finish());
    }
}