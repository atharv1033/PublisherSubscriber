package com.developer.rv.publishersubscriber.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.developer.rv.publishersubscriber.R;
import com.developer.rv.publishersubscriber.Adapters.Posts_Adapter;
import com.developer.rv.publishersubscriber.Models.Posts_Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PublisherChannelActivity extends AppCompatActivity {

    String email,channel_name;
    RecyclerView posts_recyclerView;
    Posts_Adapter posts_adapter;
    List<Posts_Model> posts_List = new ArrayList<>();
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publisher_channel);

        email = getIntent().getStringExtra("email");
        channel_name = getIntent().getStringExtra("channel_name");
        db = FirebaseFirestore.getInstance();

        posts_recyclerView = findViewById(R.id.pub_posts_recyclerView);
        posts_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        posts_adapter = new Posts_Adapter(posts_List, new Posts_Adapter.OnPostClickListener() {
            @Override
            public void onPostClickListener(Posts_Model posts_model) {
                Intent intent = new Intent(PublisherChannelActivity.this,PublisherPostActivity.class);
                intent.putExtra("email",email);
                intent.putExtra("channel_name",channel_name);
                intent.putExtra("post_name",posts_model.getName());
                startActivity(intent);
            }

            @Override
            public void onPostLongClickListener(Posts_Model posts_model){
                db.collection("publisher").document(email).collection("channels").document(channel_name).collection("posts").document(posts_model.getName()).delete();
                posts_List.remove(posts_model);
                posts_adapter.notifyDataSetChanged();
            }
        }
        );
        posts_recyclerView.setAdapter(posts_adapter);
    }

    public void Add_post(View view) {
        Intent intent = new Intent(this,CreatePostActivity.class);
        intent.putExtra("email",email);
        intent.putExtra("channel_name",channel_name);
        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();
        try {

            db.collection("publisher").document(email).collection("channels").document(channel_name).collection("posts").get()
                    .addOnCompleteListener(PublisherChannelActivity.this, new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            posts_List.clear();
                            for(DocumentSnapshot doc : task.getResult()) {
                                Posts_Model posts_model = new Posts_Model();
                                posts_model.setName(doc.getId());
                                posts_List.add(posts_model);
                            }
                            posts_adapter.notifyDataSetChanged();
                        }
                    });

        } catch(Exception ex) {
            Log.e("FireBase",ex.getMessage());
            Toast.makeText(this, "No posts yet", Toast.LENGTH_SHORT).show();
        }
    }

}
