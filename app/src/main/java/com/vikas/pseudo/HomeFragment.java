package com.vikas.pseudo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vikas.pseudo.utility.HomePostAdapter;

import java.util.ArrayList;
import java.util.Map;


public class HomeFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseDatabase database;
    RecyclerView HomeRVPost;
    HomePostAdapter homePostAdapter;
    ArrayList<GlobalPost> HomeArrayList;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_home, container, false);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference().child("GlobalPosts");
        HomeArrayList=new ArrayList<>();
        HomeRVPost=rootView.findViewById(R.id.Home_RV_Post);
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


        // Inflate the layout for this fragment
        return rootView;
    }
}