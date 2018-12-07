package com.developer.rv.publishersubscriber.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.rv.publishersubscriber.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;

public class SubscribeChannelActivity extends AppCompatActivity {

    TextView name_textView,subject_textView,topic_textView;
    String pub_email,sub_email,channel_name;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe_channel);

        pub_email = getIntent().getStringExtra("pub_email");
        sub_email = getIntent().getStringExtra("sub_email");
        channel_name = getIntent().getStringExtra("channel_name");
        Toast.makeText(this, channel_name, Toast.LENGTH_SHORT).show();

        db = FirebaseFirestore.getInstance();

        name_textView = (TextView) findViewById(R.id.Name_textView);
        subject_textView = (TextView)findViewById(R.id.Subject_textView);
        topic_textView = (TextView)findViewById(R.id.Topic_textView);

        name_textView.setText(channel_name);



        try{
            db.collection("publisher").document(pub_email).collection("channels").document(channel_name).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            subject_textView.setText(documentSnapshot.getString("subject"));
                            topic_textView.setText(documentSnapshot.getString("topic"));
                        }
                    });
        } catch (Exception ex) {
            Log.e("FireBase",ex.getMessage());
        }

    }

    public void subscribe_channel(View view) {

        final String subscriber_name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        try {
            db.collection("subscriber").document(sub_email).collection("Subscribed_emails").document(pub_email).set(Collections.singletonMap("exists",true))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            db.collection("subscriber").document(sub_email).collection("Subscribed_emails").document(pub_email).collection("Subscribed_channels").document(channel_name).set(Collections.singletonMap("exists",true))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            db.collection("publisher").document(pub_email).collection("channels").document(channel_name).collection("subscribers").document(sub_email).set(Collections.singletonMap("name",subscriber_name))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Intent intent = new Intent(SubscribeChannelActivity.this,SubscriberActivity.class);
                                            intent.putExtra("email",sub_email);
                                            startActivity(intent);
                                        }
                                    });
                                        }
                                    });
                        }
                    });

        } catch (Exception ex) {
            Log.e("FireBase",ex.getMessage());
        }
    }
}
