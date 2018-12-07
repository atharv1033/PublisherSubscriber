package com.developer.rv.publishersubscriber.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.developer.rv.publishersubscriber.Adapters.Channels_Adapter;
import com.developer.rv.publishersubscriber.Models.Channels_Model;
import com.developer.rv.publishersubscriber.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SubscriberActivity extends AppCompatActivity {

    String email;
    FirebaseFirestore db;

    RecyclerView sub_recyclerView;
    List<Channels_Model> channels_List = new ArrayList<>();
    Channels_Adapter channel_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscriber);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = getIntent().getStringExtra("email");

        db = FirebaseFirestore.getInstance();

        sub_recyclerView = findViewById(R.id.sub_recyclerView);
        sub_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        channel_adapter = new Channels_Adapter(channels_List, new Channels_Adapter.OnChannelClickedListener() {
            @Override
            public void onChannelClicked(Channels_Model channels_model) {
                Intent intent = new Intent(SubscriberActivity.this,SubscriberChannelActivity.class);
                intent.putExtra("email",channels_model.getPub_email());
                intent.putExtra("channel_name",channels_model.getName());
                startActivity(intent);
            }
        });
        sub_recyclerView.setAdapter(channel_adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();

        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        channels_List.clear();
        try {
           db.collection("subscriber").document(email).collection("Subscribed_emails").get()
                   .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                       String pub_email , channel_name;
                       @Override
                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                             for(DocumentSnapshot doc : task.getResult()) {
                                 pub_email = doc.getId();
                                 db.collection("subscriber").document(email).collection("Subscribed_emails").document(pub_email).collection("Subscribed_channels").get()
                                         .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                             @Override
                                             public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                 for (DocumentSnapshot doc : task.getResult()) {
                                                     channel_name = doc.getId();
                                                     db.collection("publisher").document(pub_email).collection("channels").document(channel_name).get()
                                                             .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                 @Override
                                                                 public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                     DocumentSnapshot doc = task.getResult();
                                                                     Channels_Model channel = new Channels_Model();
                                                                     channel.setPub_email(pub_email);
                                                                     channel.setName(channel_name);
                                                                     channel.setSubject(doc.getString("subject"));
                                                                     channel.setTopic(doc.getString("topic"));
                                                                     channels_List.add(channel);

                                                                     channel_adapter.notifyDataSetChanged();
                                                                 }
                                                             });

                                                 }
                                             }
                                         });
                             }
                       }
                   });
        } catch(Exception ex) {
            Log.e("OnResume Error", ex.getMessage());
            Toast.makeText(this, "No channels To show", Toast.LENGTH_SHORT).show();
        }


    }

    public void SubscribeChannel_fab(View view) {

        Intent intent = new Intent(this,SearchChannelActivity.class);
        intent.putExtra("email",email);
        startActivity(intent);

        Toast.makeText(this, "Join Channel", Toast.LENGTH_SHORT).show();
    }
}
