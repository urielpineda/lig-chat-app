package com.upineda.ligchatapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ChatRoomActivity extends AppCompatActivity {

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private FirebaseAuth firebaseAuth;
    private String temp_key;
    private String username;
    private MessageAdapter mApapter;
    private ArrayList<String> mArrData;
    ArrayList mArrUser = new ArrayList<String>();
    private ListView mListView;

    ArrayList msg = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);

        firebaseAuth = FirebaseAuth.getInstance();

        final FirebaseUser user = firebaseAuth.getCurrentUser();
        Button sendButton = findViewById(R.id.sendButton);
        final EditText message = findViewById(R.id.message);
        username = getIntent().getExtras().get("username").toString();
        root = FirebaseDatabase.getInstance().getReference().child("DefaultRoom");
        Button logoutButton = findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, Object> map = new HashMap<String,Object>();
                temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String, Object> map2 = new HashMap<String,Object>();

                map2.put("name",username);
                map2.put("msg",message.getText().toString());

                message_root.updateChildren(map2);
                message.setText("");
            }
        });

        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(ChatRoomActivity.this, " logged in"+ user.getDisplayName(), Toast.LENGTH_SHORT).show();
    }

    private String chat_msg, chat_user_name;
    private void append_chat_conversation(DataSnapshot dataSnapshot) {
        Iterator i = dataSnapshot.getChildren().iterator();
        while(i.hasNext()){
            chat_msg = (String) ((DataSnapshot)i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot)i.next()).getValue();
           // TextView messages = findViewById(R.id.messages);
            msg.add(chat_msg);
            if(chat_user_name.equals(username))
                chat_user_name = "You";
            mArrUser.add(chat_user_name);
           // messages.append(chat_user_name + " : " + chat_msg + " \n");
        }
//        final ScrollView scroll = findViewById(R.id.scroll);
//        scroll.post(new Runnable() {
//            @Override
//            public void run() {
//                scroll.fullScroll(View.FOCUS_DOWN);
//            }
//        });


        mListView = (ListView) findViewById(R.id.listView);
        mArrData = msg;

        mApapter = new MessageAdapter(ChatRoomActivity.this, mArrData,mArrUser);
        mListView.setAdapter(mApapter);
        mApapter.notifyDataSetChanged();
        mListView.setSelection(mApapter.getCount());
    }


    public class MessageAdapter extends BaseAdapter {

        private Context mContext;
        private ArrayList<String> messages;
        private ArrayList<String> users;

        public MessageAdapter(Context context, ArrayList arrMessages, ArrayList arrUsers) {
            super();
            mContext = context;
            messages = arrMessages;
            users = arrUsers;
        }

        public int getCount() {
            // return the number of records
            return messages.size();
        }

        // getView method is called for each item of ListView
        public View getView(int position, View view, ViewGroup parent) {
            // inflate the layout for each item of listView
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
            if(users.get(position).equals("You"))
                view = inflater.inflate(R.layout.right_chat_bubble, parent, false);
            else
           view = inflater.inflate(R.layout.left_chat_bubble,parent,false);

            // get the reference of textView and button
            TextView txtSchoolTitle = (TextView) view.findViewById(R.id.rightMessage);
            TextView txtUser = (TextView) view.findViewById(R.id.sentBy);

            // Set the title and button name
            txtSchoolTitle.setText(messages.get(position));
            txtUser.setText(users.get(position));

            return view;
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }}
}
