package com.vikas.pseudo;

public class UsersPostInfo {
    String PostText ,PostDate;
    public UsersPostInfo(String postText ,String postDate){
        this.PostText=postText;
        this.PostDate=postDate;
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
