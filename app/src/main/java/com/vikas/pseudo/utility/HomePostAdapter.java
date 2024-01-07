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
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.annotations.Nullable;
import com.vikas.pseudo.GlobalPost;
import com.vikas.pseudo.R;

import java.util.ArrayList;

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
        GlobalPost globalPost = globalPostArrayList.get(position);
        Log.d("globalPost",globalPost.getPostId()+position);
        holder.HomeTVPostText.setText(globalPost.getPostText());
        holder.HomeTVUsername.setText(globalPost.getUsername());
        holder.HomeTVLikes.setText(String.valueOf(globalPost.getPostLikes()));
        holder.LikeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Integer likes =globalPost.getPostLikes()+1;
                holder.HomeTVLikes.setText(String.valueOf(likes));
                FirebaseDatabase PostDatabase= FirebaseDatabase.getInstance();
                DatabaseReference PostLikeRef=PostDatabase.getReference("GlobalPosts").child(globalPost.getPostId());
                PostLikeRef.child("postLikes").setValue(likes);
            }
        });
        holder.TrustedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase PostDatabase= FirebaseDatabase.getInstance();
                DatabaseReference UserTrustRef = FirebaseDatabase.getInstance().getReference("Users").child(globalPost.getUserId()).child("Profile_details").child("trust_Level");
                UserTrustRef.runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        Integer currentTrustValue = mutableData.getValue(Integer.class);

                        if (currentTrustValue == null) {
                            // If the trustValue is null, set it to 1
                            mutableData.setValue(1);
                        } else {
                            // Increment the trustValue by 1
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
        AppCompatButton TrustedBtn ,LikeBtn;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            HomeTVUsername=itemView.findViewById(R.id.Home_TV_Username);
            HomeTVPostText=itemView.findViewById(R.id.Home_TV_PostText);
            HomeTVLikes=itemView.findViewById(R.id.Home_TV_Likes_Count);
            HomeTVTrust=itemView.findViewById(R.id.Home_TV_trust);
            TrustedBtn=itemView.findViewById(R.id.Home_Btn_Trust);
            LikeBtn=itemView.findViewById(R.id.Home_Btn_Heart);


        }
    }
}
