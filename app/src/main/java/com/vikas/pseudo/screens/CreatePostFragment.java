package com.vikas.pseudo.screens;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vikas.pseudo.R;
import com.vikas.pseudo.model.GlobalPost;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreatePostFragment extends Fragment {
    EditText PostText ;
    Button PostBtn;
    FirebaseDatabase PostDatabase;
    FirebaseAuth PostAuth;


    public CreatePostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_create_post, container, false);
        PostBtn=rootView.findViewById(R.id.Post_Btn_Post);
        PostText=rootView.findViewById(R.id.Post_ET_Text);
        PostDatabase=FirebaseDatabase.getInstance();

        PostAuth=FirebaseAuth.getInstance();
        PostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PostTextInput=PostText.getText().toString();
                if(TextUtils.isEmpty(PostTextInput)){
                    Toast.makeText(getActivity(),"Post Can't be empty",Toast.LENGTH_SHORT).show();
                }else {
                      String userId= Objects.requireNonNull(PostAuth.getCurrentUser()).getUid();
                      String email = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
                    assert email != null;
                      String username = email.substring(0,email.indexOf('@'));
                      DatabaseReference PostDataRef=PostDatabase.getReference("GlobalPosts");
                      String postId= PostDataRef.push().getKey();
                      LocalDate currentDate= LocalDate.now();
                      String formattedCurrentDate=currentDate.toString();
                      Map<String , Boolean> isliked = new HashMap<>();
                      isliked.put(userId,false);
                      GlobalPost postInfo=new GlobalPost(postId,PostTextInput,formattedCurrentDate,username,userId ,0,isliked,0);
                      assert postId != null;
                    PostDataRef.child(postId).setValue(postInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                          @Override
                          public void onComplete(@NonNull Task<Void> task) {
                              if(task.isSuccessful()){
                                  PostText.setText("");
                                  Toast.makeText(requireContext(),"Post Successfully Created",Toast.LENGTH_SHORT).show();
                              }else{
                                  Toast.makeText(requireContext(), Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                              }
                          }
                      });
                }
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }
}