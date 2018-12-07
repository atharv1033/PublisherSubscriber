package com.developer.rv.publishersubscriber.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.developer.rv.publishersubscriber.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PublisherPostActivity extends AppCompatActivity {

    TextView postName_textView,postContent_textView;
    String email,channel_name,post_name;
    String post_content;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publisher_post);

        email = getIntent().getStringExtra("email");
        channel_name = getIntent().getStringExtra("channel_name");
        post_name = getIntent().getStringExtra("post_name");

        db = FirebaseFirestore.getInstance();

        postName_textView = findViewById(R.id.postName_textView);
        postContent_textView = findViewById(R.id.postContent_textView);
        postName_textView.setText(post_name);

            db.collection("publisher").document(email).collection("channels").document(channel_name).collection("posts").document(post_name).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            post_content = documentSnapshot.getString("content");
                            postContent_textView.setText(post_content);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            post_content = "FireBase Failure";
                            postContent_textView.setText(post_content);
                        }
                    });

    }
}
