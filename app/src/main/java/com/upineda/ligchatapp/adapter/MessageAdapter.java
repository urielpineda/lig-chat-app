package com.upineda.ligchatapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.upineda.ligchatapp.R;
import com.upineda.ligchatapp.model.Message;

import java.util.ArrayList;

/**
 * MessageAdapter for the RecyclerView.
 * Determines whether the message is sent by the user and
 * inflates the co-responding view
 *
 * 05-18-2020
 * @author  Uriel Pineda
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {
    private ArrayList<Message> messageList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public MyViewHolder(View v) {
            super(v);
            view = v;
        }
    }

    public MessageAdapter(ArrayList<Message> messageL) {
        messageList = messageL;
    }

    @Override
    public MessageAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // Inflate the correct view (right or left chat bubble) for the message
        View v = LayoutInflater.from(parent.getContext())
                .inflate(viewType == 0 ? R.layout.right_chat_bubble : R.layout.left_chat_bubble,
                        parent,
                        false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TextView message = holder.view.findViewById(R.id.rightMessage);
        TextView sentBy = holder.view.findViewById(R.id.sentBy);
        message.setText(messageList.get(position).getMessage());
        sentBy.setText(messageList.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Check if the message is sent by the user or not
        return (messageList.get(position).getUsername().equals(Message.SENT_BY_SELF)) ? 0 : 1;
    }
}