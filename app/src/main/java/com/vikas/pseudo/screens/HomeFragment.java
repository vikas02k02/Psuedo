package com.vikas.pseudo.screens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vikas.pseudo.model.GlobalPost;
import com.vikas.pseudo.R;
import com.vikas.pseudo.utility.HomePostAdapter;

import java.util.ArrayList;
import java.util.Map;


public class HomeFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseDatabase database;
    RecyclerView HomeRVPost;
    HomePostAdapter homePostAdapter;
    ArrayList<GlobalPost> HomeArrayList;
    SwipeRefreshLayout swipeRefreshLayout;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_home, container, false);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        Query reference = database.getReference().child("GlobalPosts")
                .orderByChild("postDate")  // Assuming "postDate" is the field you want to order by
                .limitToLast(30);
        HomeArrayList=new ArrayList<>();
        HomeRVPost=rootView.findViewById(R.id.Home_RV_Post);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        HomeRVPost.setLayoutManager(new LinearLayoutManager(getActivity()));
        homePostAdapter=new HomePostAdapter(getActivity(),HomeArrayList);
        HomeRVPost.setAdapter(homePostAdapter);
        homePostAdapter.notifyDataSetChanged();
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                String postDate = dataSnapshot.child("postDate").getValue(String.class);
                String postText = dataSnapshot.child("postText").getValue(String.class);
                String Username = dataSnapshot.child("username").getValue(String.class);
                String userId = dataSnapshot.child("userId").getValue(String.class);
                Integer postLikes = dataSnapshot.child("postLikes").getValue(Integer.class);
                String postId = dataSnapshot.child("postId").getValue(String.class);
                Map<String, Boolean> userLiked = (Map<String, Boolean>) dataSnapshot.child("likedBy").getValue();
                Integer avatarId = dataSnapshot.child("avatarId").getValue(Integer.class);
                GlobalPost post = new GlobalPost(postId, postText, postDate, Username, userId, postLikes ,userLiked,avatarId!=null?avatarId:0);

                // Add the new post at the beginning of the list
                HomeArrayList.add(0, post);

                // Notify the adapter that a new item is added
                homePostAdapter.notifyItemInserted(0);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Clear the existing data before fetching new data
                HomeArrayList.clear();
                homePostAdapter.notifyDataSetChanged();

                // Fetch new data
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String postDate = dataSnapshot.child("postDate").getValue(String.class);
                            String postText = dataSnapshot.child("postText").getValue(String.class);
                            String Username = dataSnapshot.child("username").getValue(String.class);
                            String userId = dataSnapshot.child("userId").getValue(String.class);
                            Integer postLikes = dataSnapshot.child("postLikes").getValue(Integer.class);
                            String postId = dataSnapshot.child("postId").getValue(String.class);
                            Map<String, Boolean> userLiked = (Map<String, Boolean>) dataSnapshot.child("likedBy").getValue();
                            Integer avatarId = dataSnapshot.child("avatarId").getValue(Integer.class);
                            GlobalPost post = new GlobalPost(postId, postText, postDate, Username, userId, postLikes, userLiked, avatarId != null ? avatarId : 0);

                            // Add the new post at the beginning of the list
                            HomeArrayList.add(0, post);
                        }

                        // Notify the adapter that data has changed
                        homePostAdapter.notifyDataSetChanged();

                        // Stop the refreshing animation
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                        // Stop the refreshing animation
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });



        // Inflate the layout for this fragment
        return rootView;
    }
}