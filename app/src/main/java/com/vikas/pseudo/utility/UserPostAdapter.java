package com.vikas.pseudo.utility;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vikas.pseudo.GlobalPost;
import com.vikas.pseudo.R;
import com.vikas.pseudo.client.FirebaseClient;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserPostAdapter extends RecyclerView.Adapter<UserPostAdapter.viewholder> {
    Context context;
    final ArrayList<GlobalPost> userPostArrayList;

    public UserPostAdapter(Context context,ArrayList<GlobalPost> userPostArrayList) {
        this.context =context;
        this.userPostArrayList = userPostArrayList;
    }

    @NonNull
    @Override
    public UserPostAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_layout,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserPostAdapter.viewholder holder, int position) {
        GlobalPost userPost = userPostArrayList.get(position);
        holder.HomeTVPostText.setText(userPost.getPostText());
        holder.HomeTVUsername.setText(userPost.getUsername());
        holder.HomeTVLikes.setText(String.valueOf(userPost.getPostLikes()));
        holder.HomeTVTrust.setText("");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userPost.getUserId()).child("Profile_details");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Integer AvatarId = snapshot.child("avatarId").getValue(Integer.class);
                    holder.avatarView.setImageResource(FirebaseClient.getInstance().getAvatar(AvatarId !=null?AvatarId:0));
                }else {
                    holder.avatarView.setImageResource(FirebaseClient.getInstance().getAvatar(0));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return userPostArrayList.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        TextView HomeTVUsername,HomeTVPostText,HomeTVTrust,HomeTVLikes;
        CircleImageView avatarView;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            HomeTVUsername=itemView.findViewById(R.id.Home_TV_Username);
            HomeTVPostText=itemView.findViewById(R.id.Home_TV_PostText);
            HomeTVLikes=itemView.findViewById(R.id.Home_TV_Likes_Count);
            HomeTVTrust=itemView.findViewById(R.id.Home_TV_trust);
            avatarView=itemView.findViewById(R.id.Home_TV_PostProfile);
        }
    }
}
