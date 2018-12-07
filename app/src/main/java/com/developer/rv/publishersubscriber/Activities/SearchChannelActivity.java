package com.developer.rv.publishersubscriber.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class SearchChannelActivity extends AppCompatActivity {

    EditText channel_search_editText;
    RecyclerView channel_search_recyclerView;
    List<Channels_Model> channels_List = new ArrayList<>();
    Channels_Adapter channels_adapter;
    FirebaseFirestore db;
    String pub_email,sub_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_channel);

        db = FirebaseFirestore.getInstance();

        sub_email = getIntent().getStringExtra("email");

        channel_search_recyclerView = findViewById(R.id.channel_search_recyclerView);
        channel_search_recyclerView.setLayoutManager(new LinearLayoutManager(this));

        channels_adapter =new Channels_Adapter(channels_List, new Channels_Adapter.OnChannelClickedListener() {
            @Override
            public void onChannelClicked(Channels_Model channels_model) {
                Intent intent = new Intent(SearchChannelActivity.this,SubscribeChannelActivity.class);
                intent.putExtra("pub_email",pub_email);
                intent.putExtra("sub_email",sub_email);
                intent.putExtra("channel_name",channels_model.getName());
                Toast.makeText(SearchChannelActivity.this, channels_model.getName(), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
        channel_search_editText = findViewById(R.id.channel_search_editText);
        channel_search_recyclerView.setAdapter(channels_adapter);
    }

    public void channel_search(View view) {

        pub_email = channel_search_editText.getText().toString();
        try {
            db.collection("publisher").document(pub_email).collection("channels").get()
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
