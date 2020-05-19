package com.upineda.ligchatapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.upineda.ligchatapp.R;
import com.upineda.ligchatapp.adapter.MessageAdapter;
import com.upineda.ligchatapp.model.Message;
import com.upineda.ligchatapp.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 05-18-2020
 *
 * @author Uriel Pineda
 */
public class ChatRoomActivity extends AppCompatActivity {

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().getRoot();
    private User user;
    private ArrayList<Message> messageList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    static String DEFAULT_ROOM = "DefaultRoom";
    static String FIREBASE_MESSAGE = "msg";
    static String FIREBASE_MESSAGE_SENT_BY = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);

        Button sendButton = findViewById(R.id.sendButton);
        final EditText messageField = findViewById(R.id.message);
        user = (User) getIntent().getSerializableExtra(SetupViewActivity.USER_DATA);
        root = FirebaseDatabase.getInstance().getReference().child(DEFAULT_ROOM);
        Button logoutButton = findViewById(R.id.logoutButton);

        //Recycler View
        recyclerView = findViewById(R.id.recyclerListView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new MessageAdapter(messageList);

        recyclerView.setAdapter(mAdapter);

        // listen to the database for new messages
        root.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // new message received, update the conversation
                append_chat_conversation(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {/*noop*/}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {/*noop*/}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {/*noop*/}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {/*noop*/}
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signupIntent = new Intent(getApplicationContext(), SignupViewActivity.class);
                startActivity(signupIntent);
                finish();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message messageItem = new Message(messageField.getText().toString(), user.getUsername());
                Map<String, Object> map = new HashMap<String, Object>();

                // block empty messages
                if (messageItem.getMessage().isEmpty())
                    return;

                // clear the text field
                messageField.setText("");

                // generate unique key and push message to database
                String temp_key = root.push().getKey();
                root.updateChildren(map);

                DatabaseReference message_root = root.child(temp_key);
                Map<String, Object> map2 = new HashMap<String, Object>();

                map2.put(FIREBASE_MESSAGE_SENT_BY, messageItem.getUsername());
                map2.put(FIREBASE_MESSAGE, messageItem.getMessage());

                message_root.updateChildren(map2);
            }
        });
    }

    // This function is triggered when data is added in messages in the database
    private void append_chat_conversation(DataSnapshot dataSnapshot) {
        String chat_msg, chat_user_name;
        Iterator i = dataSnapshot.getChildren().iterator();

        // loop through the data and append it the the messageList
        while (i.hasNext()) {
            chat_msg = (String) ((DataSnapshot) i.next()).getValue();
            chat_user_name = (String) ((DataSnapshot) i.next()).getValue();

            // Check if message is sent to self
            if (chat_user_name.equals(user.getUsername()))
                chat_user_name = Message.SENT_BY_SELF;

            Message messageItem = new Message(chat_msg, chat_user_name);

            messageList.add(messageItem);
        }

        // notify recyclerview that data has been updated
        mAdapter.notifyDataSetChanged();
        // scroll down to the latest message
        recyclerView.getLayoutManager().scrollToPosition(messageList.size() - 1);
    }
}