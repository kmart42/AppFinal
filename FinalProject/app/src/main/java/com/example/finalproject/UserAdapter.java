package com.example.finalproject;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

//    private String[] localDataSet;
    private List<UserView.MiniPost> postsList;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
//    public PostAdapter(String[] dataSet) {
//        localDataSet = dataSet;
//    }
    public UserAdapter(List<UserView.MiniPost> dataSet) {
        postsList = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_view, viewGroup, false);
        final MyRecyclerAdapter.ViewHolder vh = new MyRecyclerAdapter.ViewHolder(view);
        return vh;

    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyRecyclerAdapter.ViewHolder holder, final int position) {
        final UserView.MiniPost u =postsList.get(position);
        holder.description_v.setText(u.description);
        holder.cat_v.setText(u.category);
        holder.conf_v.setText(u.confidence );
        Picasso.get().load(u.url).into(holder.imageView);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return postsList.size();
    }
}

