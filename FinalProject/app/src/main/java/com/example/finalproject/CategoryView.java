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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

public class CategoryView extends AppCompatActivity {
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

    TextView button_cat;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference allPostsRef = database.getReference("Posts");
    private CategoryAdapter categoryAdapter;
    private PostAdapter postAdapter;
    private String[] category_list;
    private String[] sub_list;
    private String[] out;
    private ArrayList<String> cat_out;
    private String[] cat_final;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_home);
        category_list = new String[CategoryCount.categories.size() + 1];
        allPostsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String tmp = snapshot.child("category").getValue().toString();;
                    for(String s : category_list){
                        if(tmp.equals(s)){
                            break;
                        }else{
                            category_list[i] = tmp;
                        }
                    }
                    if(i<CategoryCount.categories.size()) {
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
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        categoryAdapter=new CategoryAdapter(category_list);
        recyclerView.setAdapter(categoryAdapter);


    }

    public void buttonClick(View view){

        loadCategory(view);
    }


    public void loadCategory(View view){
        final int[] i = {0};
//        sub_list = new String[10];
        button_cat = view.findViewById(R.id.textView);
        final String cat_check = button_cat.getText().toString();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CategoryPost.posts.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    String tmp = ds.child("category").getValue().toString();
                    if(cat_check.equals(tmp)) {
                        MiniPost userModel=new MiniPost(ds.child("uid").getValue().toString(),
                                ds.child("description").getValue().toString(),
                                ds.child("url").getValue().toString(),
//                                localDateFormat.format(new Date(Long.parseLong(snapshot.child("timestamp").getValue().toString()))) ,
                                ds.getKey(),
                                ds.child("category").getValue().toString(),
                                ds.child("confidence").getValue().toString());
                        CategoryPost.posts.add(userModel);

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
        RecyclerView recyclerView=findViewById(R.id.recycler_cat);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        postAdapter=new PostAdapter(CategoryPost.posts);
        recyclerView.setAdapter(postAdapter);

    }
}
