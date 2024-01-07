package com.vikas.pseudo.model;

public class UsersInfo {
    private String Username ,Email, Password,Status ;
    private int Trust_Level;
    public UsersInfo(String username , String email , String password, String status, int trustLevel){
        this.Username=username;
        this.Email=email;
        this.Password=password;
        this.Status=status;
        this.Trust_Level = trustLevel;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getTrust_Level() {
        return Trust_Level;
    }

    public void setTrust_Level(int trust_Level) {
        Trust_Level = trust_Level;
    }
}
