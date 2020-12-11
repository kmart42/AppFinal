package com.example.finalproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CategoryView extends AppCompatActivity {

    TextView button_cat;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference allPostsRef = database.getReference("Posts");
    private CustomAdapter customAdapter;
    private String[] category_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_home);
        category_list = new String[5];
        allPostsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String tmp = snapshot.child("category").getValue().toString();
                    for(String check : category_list){
                        if (tmp.equals(check)){
                            break;
                        }
                        category_list[i] = tmp;
                    }
                    i++;
                }
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
        customAdapter=new CustomAdapter(category_list);
        recyclerView.setAdapter(customAdapter);


    }


    public void loadCategory(View view){
        button_cat = view.findViewById(R.id.textView);
        String cat_check = button_cat.getText().toString();
        Query selected_cat = allPostsRef.child("category").equalTo(cat_check);
        selected_cat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println(snapshot.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Realtime", "onCancelled", error.toException());
            }
        });
    }
}
