package com.developer.rv.publishersubscriber.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.rv.publishersubscriber.R;
import com.developer.rv.publishersubscriber.Models.Posts_Model;

import java.util.List;

public class Posts_Adapter extends RecyclerView.Adapter<Posts_Adapter.Posts_ViewHolder> {

    public interface OnPostClickListener {
        public void onPostClickListener(Posts_Model posts_model);
        public void onPostLongClickListener(Posts_Model posts_model);
    }

    List<Posts_Model> posts_List;
    OnPostClickListener listener;

    public Posts_Adapter(List<Posts_Model> posts_List, OnPostClickListener listener) {
        this.posts_List = posts_List;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Posts_Adapter.Posts_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_list_layout,parent,false);
        return new Posts_ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull Posts_Adapter.Posts_ViewHolder holder, int position) {
        holder.postName_textView.setText(posts_List.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return posts_List.size();
    }

    public class Posts_ViewHolder extends RecyclerView.ViewHolder {

        TextView postName_textView;

        public Posts_ViewHolder(View itemView) {
            super(itemView);
            postName_textView = itemView.findViewById(R.id.postName_textView);

            postName_textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onPostClickListener(posts_List.get(getAdapterPosition()));
                }
            });

            postName_textView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onPostLongClickListener(posts_List.get(getAdapterPosition()));
                    return true;
                }
            });
        }
    }
}
