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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.vikas.pseudo.model.GlobalPost;
import com.vikas.pseudo.R;
import com.vikas.pseudo.client.FirebaseClient;
import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.viewholder> {
    Context context;
    final ArrayList<GlobalPost> globalPostArrayList;
    public HomePostAdapter(Context context, ArrayList<GlobalPost> globalPostArrayList){
        this.context=context;
        this.globalPostArrayList = globalPostArrayList;
    }
    @NonNull
    @Override
    public HomePostAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_layout,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePostAdapter.viewholder holder, int position) {
        String uId=Objects.requireNonNull(FirebaseAuth.getInstance().getUid());
        GlobalPost globalPost = globalPostArrayList.get(position);
        Log.d("globalPost",globalPost.getPostId()+position);
        holder.HomeTVPostText.setText(globalPost.getPostText());
        holder.HomeTVUsername.setText(globalPost.getUsername());
        holder.HomeTVLikes.setText(String.valueOf(globalPost.getPostLikes()));
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(globalPost.getUserId()).child("Profile_details");
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
        if (globalPost.getLikedBy().get(uId) != null
                && Boolean.TRUE.equals(globalPost.getLikedBy().get(uId))) {
            holder.LikeBtn.setBackgroundResource(R.drawable.liked_icon);
        } else {
            holder.LikeBtn.setBackgroundResource(R.drawable.likes_icon);
        }
        holder.LikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update the likedBy field in the database
                FirebaseDatabase PostDatabase = FirebaseDatabase.getInstance();
                DatabaseReference PostLikeRef = PostDatabase.getReference("GlobalPosts").child(globalPost.getPostId());

                // Check if the current user has already liked the post
                PostLikeRef.child("likedBy").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.child(uId).exists() ){
                            PostLikeRef.child("likedBy").child(uId).setValue(true);
                            Integer likes = globalPost.getPostLikes() + 1;
                            holder.HomeTVLikes.setText(String.valueOf(likes));
                            PostLikeRef.child("postLikes").setValue(likes);
                            holder.LikeBtn.setBackgroundResource(R.drawable.liked_icon);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle errors if any
                    }
                });
            }
        });


        holder.TrustedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference UserTrustRef = FirebaseDatabase.getInstance().getReference("Users").child(globalPost.getUserId()).child("Profile_details").child("trust_Level");
                UserTrustRef.runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        Integer currentTrustValue = mutableData.getValue(Integer.class);

                        if (currentTrustValue == null) {
                            mutableData.setValue(1);
                        } else {
                            mutableData.setValue(currentTrustValue + 1);
                        }

                        return Transaction.success(mutableData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                        if (databaseError != null) {
                            // Handle the error
                        } else {
                            // Transaction completed successfully
                        }
                    }
                });


                holder.HomeTVTrust.setText(R.string.Trusted);
            }
        });

    }

    @Override
    public int getItemCount() {
        return globalPostArrayList.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        TextView HomeTVUsername,HomeTVPostText,HomeTVTrust,HomeTVLikes;
        CircleImageView avatarView;
        AppCompatButton TrustedBtn ,LikeBtn;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            HomeTVUsername=itemView.findViewById(R.id.Home_TV_Username);
            HomeTVPostText=itemView.findViewById(R.id.Home_TV_PostText);
            HomeTVLikes=itemView.findViewById(R.id.Home_TV_Likes_Count);
            HomeTVTrust=itemView.findViewById(R.id.Home_TV_trust);
            TrustedBtn=itemView.findViewById(R.id.Home_Btn_Trust);
            LikeBtn=itemView.findViewById(R.id.Home_Btn_Heart);
            avatarView=itemView.findViewById(R.id.Home_TV_PostProfile);

        }
    }
}
