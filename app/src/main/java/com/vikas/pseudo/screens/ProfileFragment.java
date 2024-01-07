package com.vikas.pseudo.screens;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.vikas.pseudo.GlobalPost;
import com.vikas.pseudo.R;
import com.vikas.pseudo.model.UsersInfo;
import com.vikas.pseudo.utility.HomePostAdapter;
import com.vikas.pseudo.utility.UserPostAdapter;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {
    private CircleImageView profile_Avatar ;
    private TextView profile_user_name ,profile_status ,profile_trust_level;
    private FirebaseAuth auth ;
    private TextInputEditText update_status ,update_email;
    private LinearLayout editProfileLayout ;
    private String UId ,username,status,email;
    private Integer trust;
    private RecyclerView UserRVPost;
    private UserPostAdapter userPostAdapter;
    private ArrayList<GlobalPost> userArrayList;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View profile_root =inflater.inflate(R.layout.fragment_profile, container, false);
        profile_Avatar = profile_root.findViewById(R.id.avatar);
        Button edit_profile = profile_root.findViewById(R.id.edit_profile_button);
        Button update_button = profile_root.findViewById(R.id.update_info_button);
        Button chat_room = profile_root.findViewById(R.id.chat_room_button);
        Button edit_profile_cancel_button = profile_root.findViewById(R.id.update_info_cancel_button);
        profile_user_name =profile_root.findViewById(R.id.profile_user_name);
        profile_status = profile_root.findViewById(R.id.profile_status);
        profile_trust_level =profile_root.findViewById(R.id.trust_level);
        update_status= profile_root.findViewById(R.id.update_status);
        update_email = profile_root.findViewById(R.id.update_email);
        editProfileLayout = profile_root.findViewById(R.id.edit_profile_layout);
        UserRVPost=profile_root.findViewById(R.id.post_recyclerview);
        auth = FirebaseAuth.getInstance();
        UId = auth.getUid();

        //User Details Updated
        fetchUserDetails(getActivity());

        userArrayList=new  ArrayList<>();
        UserRVPost.setLayoutManager(new LinearLayoutManager(getActivity()));
        userPostAdapter=new UserPostAdapter(getActivity(),userArrayList);
        UserRVPost.setAdapter(userPostAdapter);
        userPostAdapter.notifyDataSetChanged();
        FirebaseDatabase  database = FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference().child("GlobalPosts");
        Query query = reference.child(UId);
        query.addChildEventListener(new ChildEventListener() {
            @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                String postDate = dataSnapshot.child("postDate").getValue(String.class);
                String postText = dataSnapshot.child("postText").getValue(String.class);
                String Username = dataSnapshot.child("username").getValue(String.class);
                String userId = dataSnapshot.child("userId").getValue(String.class);
                Integer postLikes = dataSnapshot.child("postLikes").getValue(Integer.class);
                String postId = dataSnapshot.child("postId").getValue(String.class);

                GlobalPost post = new GlobalPost(postId, postText, postDate, Username, userId, postLikes);

                // Add the new post at the beginning of the list
                userArrayList.add(0, post);

                // Notify the adapter that a new item is added
                userPostAdapter.notifyItemInserted(0);
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







        edit_profile.setOnClickListener(view -> {
            editProfileLayout.setVisibility(View.VISIBLE);
            update_email.setText(email);
        });
        update_button.setOnClickListener(view -> updateUserDetails(getActivity()));
        edit_profile_cancel_button.setOnClickListener(view -> editProfileLayout.setVisibility(View.GONE));



        return profile_root;
    }


    void fetchUserDetails(Context context){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(UId).child("Profile_details");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                     username = snapshot.child("username").getValue(String.class);
                     status = snapshot.child("status").getValue(String.class);
                     trust = snapshot.child("trust_Level").getValue(Integer.class);
                     email = snapshot.child("Email").getValue(String.class);
                    profile_user_name.setText(username);
                    profile_status.setText(status);
                    profile_trust_level.setText(new StringBuilder().append("Trust Count: ").append(trust).toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context , error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    void updateUserDetails(Context context){
        String UpdatedStatus = Objects.requireNonNull(update_status.getText()).toString();
        String email = Objects.requireNonNull(update_email.getText()).toString();
        if(UpdatedStatus.equals("")){
            update_status.setError("Field Can't be Empty");
            return;
        }
        if(email.equals("")){
            update_email.setError("Field Can't be Empty");
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(UId).child("Profile_details");

        reference.child("email").setValue(email).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                editProfileLayout.setVisibility(View.GONE);
            }else{
                Toast.makeText(context , Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
            }

        });
        reference.child("status").setValue(UpdatedStatus).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                editProfileLayout.setVisibility(View.GONE);
                Toast.makeText(context , "Profile Updated Successfully",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context , Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
            }

        });
    };


}
