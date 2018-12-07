package com.developer.rv.publishersubscriber.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.developer.rv.publishersubscriber.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;

public class CreatePostActivity extends AppCompatActivity {

    EditText postName_editText,postContent_editText;
    String postName,postContent,
           email,channel_name;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        email = getIntent().getStringExtra("email");
        channel_name = getIntent().getStringExtra("channel_name");

        db = FirebaseFirestore.getInstance();

        postName_editText = findViewById(R.id.postName_editText);
        postContent_editText = findViewById(R.id.postContent_editText);
    }

    public void Create_Post(View view) {
        postName = postName_editText.getText().toString();
        postContent = postContent_editText.getText().toString();

        if(!(postName.equals("")) && !(postContent.equals(""))) {
            try {
                db.collection("publisher").document(email).collection("channels").document(channel_name).collection("posts").document(postName)
                        .set(Collections.singletonMap("content", postContent));

                Intent intent = new Intent(this,PublisherChannelActivity.class);
                intent.putExtra("email",email);
                intent.putExtra("channel_name",channel_name);
                startActivity(intent);
                finish();

            } catch (Exception ex) {
                Log.e("Firebase", ex.getMessage());
                Toast.makeText(this, "FireBase Error", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Enter all Details", Toast.LENGTH_SHORT).show();
        }
    }
}
