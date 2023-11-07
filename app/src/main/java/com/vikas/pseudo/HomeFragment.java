package com.vikas.pseudo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    FirebaseAuth auth;
    FirebaseDatabase database;
    RecyclerView HomeRVPost;
    HomePostAdapter homePostAdapter;
    ArrayList<UsersPostInfo> HomeArrayList;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_home, container, false);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference().child("Post");
        HomeArrayList=new ArrayList<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String postDate = dataSnapshot.child("postDate").getValue(String.class);
                    String postText = dataSnapshot.child("postText").getValue(String.class);
                    UsersPostInfo post = new UsersPostInfo(postText,postDate);
                    HomeArrayList.add(0,post);
                }
                homePostAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        HomeRVPost=rootView.findViewById(R.id.Home_RV_Post);
        HomeRVPost.setLayoutManager(new LinearLayoutManager(getActivity()));
        homePostAdapter=new HomePostAdapter(getActivity(),HomeArrayList);
        HomeRVPost.setAdapter(homePostAdapter);
        // Inflate the layout for this fragment
        return rootView;
    }
}