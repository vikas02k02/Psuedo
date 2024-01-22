package com.vikas.pseudo.model;

import java.util.HashMap;
import java.util.Map;

public class GlobalPost {
    private  String PostText ,PostDate ,Username,UserId,PostId;
    private  Integer PostLikes;
    private int AvatarId;
    private Map<String ,Boolean> likedBy ;
    public GlobalPost(String postId ,String postText , String postDate,String username,String userId ,Integer postLikes, Map<String , Boolean> userLiked,int avatarId){
        PostText=postText;
        PostDate=postDate;
        Username=username;
        UserId=userId;
        PostLikes=postLikes;
        PostId=postId;
        likedBy= userLiked;
        AvatarId=avatarId;


    }

    public  String getPostId() {
        return PostId;
    }

    public  void setPostId(String postId) {
        PostId = postId;
    }

    public  Integer getPostLikes() {
        return PostLikes;
    }

    public  void setPostLikes(Integer postLikes) {
        PostLikes = postLikes;
    }

    public  String getUsername() {
        return Username;
    }

    public  void setUsername(String username) {
        Username = username;
    }

    public  String getUserId() {
        return UserId;
    }

    public  void setUserId(String userId) {
        UserId = userId;
    }

    public String getPostText() {
        return PostText;
    }

    public void setPostText(String postText) {
        PostText = postText;
    }

    public String getPostDate() {
        return PostDate;
    }

    public void setPostDate(String postDate) {
        PostDate = postDate;
    }

    public Map<String, Boolean> getLikedBy() {
        return likedBy;
    }

    public void setLikedBy(Map<String, Boolean> likedBy) {
        this.likedBy = likedBy;
    }

    public int getAvatarId() {
        return AvatarId;
    }

    public void setAvatarId(int avatarId) {
        AvatarId = avatarId;
    }
}
