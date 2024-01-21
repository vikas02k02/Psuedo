package com.vikas.pseudo.client;

import com.vikas.pseudo.R;
import com.vikas.pseudo.model.UsersInfo;

import java.util.ArrayList;

public class FirebaseClient {
    private static FirebaseClient instance;
    private UsersInfo userInfo;

    FirebaseClient(){

    }
    public static FirebaseClient getInstance(){
        if(instance ==null){
            instance=new FirebaseClient();
        }
        return instance;
    }

    public int getAvatar(int position){
        int AvatarResource;
        ArrayList<Integer> AvatarArray = new ArrayList<>();
        AvatarArray.add(R.drawable.logo);
        AvatarArray.add(R.drawable.avatar1person1);
        AvatarArray.add(R.drawable.avatar2cheetah);
        AvatarArray.add(R.drawable.avatar3spiderman);
        AvatarArray.add(R.drawable.avatar4tiger);
        AvatarArray.add(R.drawable.avatar5person2);
        AvatarResource = AvatarArray.get(position);
        return AvatarResource;
    }
//    public UsersInfo getUserInfo() {
//        return userInfo;
//    }
//
//    public void setUserInfo(UsersInfo userInfo) {
//        this.userInfo = userInfo;
//    }


}
