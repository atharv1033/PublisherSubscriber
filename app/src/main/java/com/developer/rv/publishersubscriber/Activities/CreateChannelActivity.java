package com.developer.rv.publishersubscriber.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.rv.publishersubscriber.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateChannelActivity extends AppCompatActivity {

    TextView Cname,Csubject,Ctopic;
    String name,subject,topic,email;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_channel);

        email = getIntent().getStringExtra("email");
        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();
        db = FirebaseFirestore.getInstance();

        Cname = findViewById(R.id.Cname);
        Csubject = findViewById(R.id.Csubject);
        Ctopic = findViewById(R.id.Ctopic);
    }

    void ChannelCreate(View view) {

        name = Cname.getText().toString();
        subject = Csubject.getText().toString();
        topic = Ctopic.getText().toString();

        if(!(name.equals("")) && !(subject.equals("")) && !(topic.equals(""))) {

            Map<String,String> channelDetails = new HashMap<>();
            channelDetails.put("subject",subject);
            channelDetails.put("topic",topic);

            try{
                db.collection("publisher").document(email).collection("channels").document(name)
                        .set(channelDetails)
                        .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(CreateChannelActivity.this, PublisherActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                                finish();
                            }
                        });

            } catch(Exception ex) {
                Log.e("FireStore",ex.getMessage());
                Toast.makeText(this, "Channel Already Exist with same name", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Enter All Details", Toast.LENGTH_SHORT).show();
        }

    }
}
