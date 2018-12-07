package com.developer.rv.publishersubscriber.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.rv.publishersubscriber.R;
import com.developer.rv.publishersubscriber.Models.Channels_Model;

import java.util.List;

public class Channels_Adapter extends RecyclerView.Adapter<Channels_Adapter.Channels_ViewHolder> {

    public interface OnChannelClickedListener {
        public void onChannelClicked(Channels_Model channels_model);
    }

    List<Channels_Model> channels_List;
    OnChannelClickedListener listener ;

    public Channels_Adapter(List<Channels_Model> channels_List, OnChannelClickedListener listener) {
        this.channels_List = channels_List;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Channels_Adapter.Channels_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View channel = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.channel_list_layout,parent,false);
        return new Channels_ViewHolder(channel);
    }

    @Override
    public void onBindViewHolder(@NonNull Channels_Adapter.Channels_ViewHolder holder, int position) {
        holder.name_textTextView.setText(channels_List.get(position).getName());
        holder.subject_textTextView.setText(channels_List.get(position).getSubject());
        holder.topic_TextView.setText(channels_List.get(position).getTopic());
    }

    @Override
    public int getItemCount() {
        return channels_List.size();
    }

    public class Channels_ViewHolder extends RecyclerView.ViewHolder {

        TextView name_textTextView,subject_textTextView,topic_TextView,all_container_textView;

        public Channels_ViewHolder(View channelView) {
            super(channelView);
            name_textTextView = channelView.findViewById(R.id.name_textView);
            subject_textTextView = channelView.findViewById(R.id.subject_textView);
            topic_TextView = channelView.findViewById(R.id.topic_textView);

            name_textTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onChannelClicked(channels_List.get(getAdapterPosition()));

                    }
            });
            subject_textTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onChannelClicked(channels_List.get(getAdapterPosition()));

                }
            });
            topic_TextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onChannelClicked(channels_List.get(getAdapterPosition()));

                }
            });

        }
    }
}
