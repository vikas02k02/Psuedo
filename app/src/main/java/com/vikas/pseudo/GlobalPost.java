package com.vikas.pseudo;

public class GlobalPost {
    private  String PostText ,PostDate ,Username,UserId,PostId;
    private  Integer PostLikes;
    public GlobalPost(String postId ,String postText , String postDate,String username,String userId ,Integer postLikes){
        PostText=postText;
        PostDate=postDate;
        Username=username;
        UserId=userId;
        PostLikes=postLikes;
        PostId=postId;

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
}
