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

public class PublisherActivity extends AppCompatActivity {

    String email;
    RecyclerView pub_RecyclerView;
    List<Channels_Model> channels_List = new ArrayList<>();
    Channels_Adapter channels_adapter;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publisher);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = getIntent().getStringExtra("email");

        Toast.makeText(this, email, Toast.LENGTH_SHORT).show();

        db = FirebaseFirestore.getInstance();

        pub_RecyclerView = findViewById(R.id.channels_recyclerView);
        pub_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        channels_adapter = new Channels_Adapter(channels_List, new Channels_Adapter.OnChannelClickedListener() {
            @Override
            public void onChannelClicked(Channels_Model channels_model) {
                Intent intent = new Intent(PublisherActivity.this,PublisherChannelActivity.class);
                intent.putExtra("email",email);
                intent.putExtra("channel_name",channels_model.getName());
                startActivity(intent);
            }
        });

        pub_RecyclerView.setAdapter(channels_adapter);

    }

    void CreateChannel_fab(View view) {

        Intent intent = new Intent(this,CreateChannelActivity.class);
        intent.putExtra("email",email);
        startActivity(intent);

        Toast.makeText(this, "Add Channel", Toast.LENGTH_SHORT).show();
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
        try {
            db.collection("publisher").document(email).collection("channels").get()
                    .addOnCompleteListener(this, new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            channels_List.clear();
                            for (DocumentSnapshot doc : task.getResult()) {
                                Channels_Model channel = doc.toObject(Channels_Model.class);
                                channel.setName(doc.getId());
                                channels_List.add(channel);
                            }
                            channels_adapter.notifyDataSetChanged();
                        }
                    });
        } catch(Exception ex) {
            Log.e("OnResume Error", ex.getMessage());
            Toast.makeText(this, "No channels To show", Toast.LENGTH_SHORT).show();
        }

    }
}

