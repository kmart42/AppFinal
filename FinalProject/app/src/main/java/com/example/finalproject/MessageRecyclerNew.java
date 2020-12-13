package com.example.finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageRecyclerNew
        extends RecyclerView.Adapter<MessageRecyclerNew.ViewHolder>
        {
            SimpleDateFormat localDateFormat= new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            private class MessageModel{
                public String postKey;
                public String uid;
                public String message;
//                public String url;
                public String date;
                public MessageModel(String uid, String message, String date, String key) {
                    this.uid=uid;
                    this.message=message;
//                    this.url=url;
                   this.date=date;
                   this.postKey=key;
                }
            }
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference allPostsRef = database.getReference("Messages");
            ChildEventListener usersRefListener;
            private FirebaseAuth mAuth;
            private FirebaseUser currentUser;
            private List<MessageModel> postsList;

            private RecyclerView r;

            public MessageRecyclerNew(RecyclerView recyclerView){
                postsList =new ArrayList<>();
                r=recyclerView;
                mAuth = FirebaseAuth.getInstance();
                currentUser = mAuth.getCurrentUser();
                allPostsRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                        MessageModel userModel=new MessageModel(dataSnapshot.child("uid").getValue().toString(),
                                dataSnapshot.child("message").getValue().toString(),
                                localDateFormat.format(new Date(Long.parseLong(dataSnapshot.child("timestamp").getValue().toString()))) ,
                                dataSnapshot.getKey());
                        postsList.add(userModel);
                        MessageRecyclerNew.this.notifyItemInserted(postsList.size()-1);
                        r.scrollToPosition(postsList.size()-1);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        String id = snapshot.child("uid").getValue().toString();
                        int position = -1;
                        for (int i=0; i<postsList.size();i++){
                            if(postsList.get(i).uid.equals(id)){
                                position = i;
                                break;
                            }
                        }
                        if(position!=-1){
                            MessageModel userModel=new MessageModel(snapshot.child("uid").getValue().toString(),
                                    snapshot.child("message").getValue().toString(),
//                                    snapshot.child("url").getValue().toString(),
                                    localDateFormat.format(new Date(Long.parseLong(snapshot.child("timestamp").getValue().toString()))) ,
                                    snapshot.getKey());
                            postsList.add(position, userModel);
                            MessageRecyclerNew.this.notifyItemRemoved(position);
                            r.scrollToPosition(position);
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                        String id = snapshot.child("uid").getValue().toString();
                        int position=-1;
                        for(int i=0;i<postsList.size();i++){
                            if(postsList.get(i).uid.equals(id)){
                                position=i;
                                break;
                            }
                        }
                        if(position!=-1){
                            postsList.remove(position);
                            MessageRecyclerNew.this.notifyItemRemoved(position);
                            r.scrollToPosition(position);
                        }
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view2, parent,false);
                final ViewHolder vh = new ViewHolder(v);
                return vh;
            }


            @Override
            public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
                final MessageModel u =postsList.get(position);
                final String key = u.postKey;
                final String uid=u.uid;
                if(holder.uref!=null && holder.urefListener!=null)
                {
                    holder.uref.removeEventListener(holder.urefListener);
                }
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                holder.uref = database.getReference("Messages").child(key);
                holder.uref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String tst = dataSnapshot.child("uid").getValue().toString();
                        DatabaseReference postsRef = database.getReference("Users").child(tst);
                        postsRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String name = (String)snapshot.child("displayname").getValue();
                                holder.fname_v.setText(name);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        holder.message_v.setText(dataSnapshot.child("message").getValue().toString());
                        holder.date_v.setText((localDateFormat.format( new Date(Long.parseLong(dataSnapshot.child("timestamp").getValue().toString())))));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
            public void removeListener(){
                if(allPostsRef !=null && usersRefListener!=null)
                    allPostsRef.removeEventListener(usersRefListener);
            }
            @Override
            public int getItemCount() {
                return postsList.size();
            }
            public static class ViewHolder extends RecyclerView.ViewHolder{
                public TextView fname_v;
                public TextView email_v;
                public TextView phone_v;
                public TextView date_v;
                public TextView message_v;
                DatabaseReference uref;
                DatabaseReference nameref;
                ValueEventListener urefListener;

                DatabaseReference likeCountRef;
                ValueEventListener likeCountRefListener;

                DatabaseReference likesRef;
                ValueEventListener likesRefListener;
                public ViewHolder(View v){
                    super(v);
                    fname_v = (TextView) v.findViewById(R.id.fname_view2);
                    message_v = (TextView) v.findViewById(R.id.message_view2);
                   date_v = (TextView) v.findViewById(R.id.date_view2);
                }
            }


}
