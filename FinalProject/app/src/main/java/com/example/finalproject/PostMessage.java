package com.example.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostMessage extends AppCompatActivity {
    private List<String> keyList = null;
    private RecyclerView recyclerView;
    private MessageRecyclerNew messageRecycler;

    public static class MessageU {
        public String uid;
        public Object timestamp;
        public String message;
        public MessageU(String message, String author) {
            this.uid=author;
            this.message=message;
            this.timestamp= ServerValue.TIMESTAMP;
        }
        public Object getTimestamp(){
            return timestamp;
        }
        public MessageU() {

        }

    }

    public  static class Node {
        public String value1;
        public String value2;

        public Node(String v1, String v2) {
            value1 = v1;
            value2 = v2;
        }
    }
    EditText set_text;
    DatabaseReference mRootReference = FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_view);
        set_text = findViewById(R.id.enter_text);

        recyclerView = findViewById(R.id.recylcer_view2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        messageRecycler = new MessageRecyclerNew(recyclerView);
        recyclerView.setAdapter(messageRecycler);


//        description=findViewById(R.id.description);
//        mAuth = FirebaseAuth.getInstance();
//        currentUser = mAuth.getCurrentUser();

    }
    public void SendMessage(View view){
        FirebaseAuth mAuth;
        FirebaseUser currentUser;
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        FirebaseDatabase.getInstance()
                .getReference().child("Messages")
                .push()
                .setValue(new MessageU( set_text.getText().toString(),
                        currentUser.getUid()
                ));
    }

}
