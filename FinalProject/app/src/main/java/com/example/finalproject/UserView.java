package com.example.finalproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import static com.example.finalproject.UserList.user_list;

public class UserView extends AppCompatActivity {
    SimpleDateFormat localDateFormat= new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public class MiniPost{
        public String postKey;
        public String uid;
        public String description;
        public String url;
//        public String date;
        public String category;
        public String confidence;
        public MiniPost(String uid, String description, String url, String key, String confidence, String category) {
            this.uid=uid;
            this.description=description;
            this.url=url;
//            this.date=date;
            this.postKey=key;
            this.category=category;
            this.confidence=confidence;
        }
    }

    private String[] users = new String[4];
    TextView button_cat;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference allPostsRef = database.getReference("Posts");
    DatabaseReference userRef = database.getReference("Users");
    private CustomAdapter customAdapter;
    private UserAdapter postAdapter;
    private int index;
//    private String[] category_list;
    private String[] sub_list;
    private String[] out;
    private ArrayList<String> cat_out;
    private String[] cat_final;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_home);
        user_list = new String[UserCount.users.size() + 1];
        allPostsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
//                final String[] name = new String[1];
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final String tmp = snapshot.child("uid").getValue().toString();
                    for(String s : user_list){
                        if(tmp.equals(s)){
                            break;
                        }else{
                            user_list[i] = tmp;

                        }
                    }
                    if(i<UserCount.users.size()) {
                        i++;
                    }
                }
//                Set<String> set = new HashSet<String>(Arrays.asList(category_list));
//
////                Collections.addAll(set, category_list);
//                int k = 0;
//                for(String val : set) {
//                    if((val !=null)) {
//                        out[k] = val;
//                        k++;
//                    }
//
//                }
//                System.out.println(Arrays.toString(out));
//                for (String s: out){
//                    if(s!=null){
//                        cat_out.add(s);
//                    }
//                }

//                category_list = cat_out.toArray(new String[cat_out.size()]);


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        RecyclerView recyclerView=findViewById(R.id.recycler_labels);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        customAdapter=new CustomAdapter(user_list);
        recyclerView.setAdapter(customAdapter);


    }

    public void buttonClick(View view){

        loadCategory(view);
    }


    public void loadCategory(View view){
        users[0] = "PzhTMSygqfbfvXYosSRpeKuEa1S2";
        users[1] = "TaMS4zWZ3IagQkRd9SgbaahKYxq1";
        users[2] = "cMV1IaqD2NQxBdwixDqAoErPxiI3";
        users[3] = "xl2ADHQahFgYI6qLTnXNtgde0An1";
        final int[] i = {0};
//        sub_list = new String[10];
        button_cat = view.findViewById(R.id.textView);
        System.out.println(button_cat.getText().toString());
        final String cat_check = button_cat.getText().toString();
        if (cat_check.equals("Kevin-1")){
            index=1;
        }else if(cat_check.equals("Ricardo")){
            index=0;
        }else if(cat_check.equals("Romo")){
            index=2;
        }
        else if(cat_check.equals("ric2")){
            index=3;
        }

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserPost.posts.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    final String id = ds.child("uid").getValue().toString();
//                    String name = ds.getRef().child("Users").child(id).child("displayname").getKey().toString();
                    final String[] name = new String[1];
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            name[0] = snapshot.child(id).child("displayname").getValue().toString();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


//                            System.out.println(name[0]);
//                    if(cat_check.equals(id)) {
                    System.out.println(user_list[index]);
                    if(user_list[index].equals(id)){
                        MiniPost userModel=new MiniPost(ds.child("uid").getValue().toString(),
                                ds.child("description").getValue().toString(),
                                ds.child("url").getValue().toString(),
//                                localDateFormat.format(new Date(Long.parseLong(snapshot.child("timestamp").getValue().toString()))) ,
                                ds.getKey(),
                                ds.child("category").getValue().toString(),
                                ds.child("confidence").getValue().toString());
                        UserPost.posts.add(userModel);

                        i[0]++;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Realtime", "onCancelled", error.toException());
            }
        };
//        sub_list = CategoryPost.posts
//                .toArray(new String[0]);
        allPostsRef.addListenerForSingleValueEvent(valueEventListener);
//        userRef.addListenerForSingleValueEvent(valueEventListener);
        RecyclerView recyclerView=findViewById(R.id.recycler_cat);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        postAdapter=new UserAdapter(UserPost.posts);
        recyclerView.setAdapter(postAdapter);

    }
}
