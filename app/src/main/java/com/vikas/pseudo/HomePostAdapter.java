package com.vikas.pseudo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.viewholder> {
    Context context;
    ArrayList<UsersPostInfo> usersPostInfoArrayList;
    public HomePostAdapter(Context context, ArrayList<UsersPostInfo> usersPostInfoArrayList){
        this.context=context;
        this.usersPostInfoArrayList=usersPostInfoArrayList;
    }
    @NonNull
    @Override
    public HomePostAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_layout,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomePostAdapter.viewholder holder, int position) {
        UsersPostInfo usersPostInfo=usersPostInfoArrayList.get(position);
        holder.HomeTVPostText.setText(usersPostInfo.PostText);
        holder.HomeTVUsername.setText("Post Date : "+usersPostInfo.PostDate);

    }

    @Override
    public int getItemCount() {
        return usersPostInfoArrayList.size();
    }

    public static class viewholder extends RecyclerView.ViewHolder {
        TextView HomeTVUsername;
        TextView HomeTVPostText;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            HomeTVUsername=itemView.findViewById(R.id.Home_TV_Username);
            HomeTVPostText=itemView.findViewById(R.id.Home_TV_PostText);

        }
    }
}
