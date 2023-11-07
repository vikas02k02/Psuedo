package com.vikas.pseudo;

public class UsersInfo {
    String Username ,Email, Password,Status;
    public UsersInfo(String username , String email ,String password,String status){
        this.Username=username;
        this.Email=email;
        this.Password=password;
        this.Status=status;
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
}
