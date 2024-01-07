package com.vikas.pseudo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.vikas.pseudo.model.UsersInfo;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    EditText RegUsername,RegEmail ,RegPassword ,RegConfirmPassword ;
    Button RegBtnRegister;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    UsersInfo userinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        RegUsername =findViewById(R.id.Reg_ET_Username);
        RegEmail =findViewById(R.id.Reg_ET_Email);
        RegPassword=findViewById(R.id.Reg_ET_Password);
        RegConfirmPassword=findViewById(R.id.Reg_ET_ConfirmPassword);
        RegBtnRegister =findViewById(R.id.Reg_Btn_Register);
        TextView RegBtnLogin =findViewById(R.id.Reg_Btn_Login);
        ProgressBar RegProgressBar =findViewById(R.id.Reg_ProgressBar);
        RegBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        RegBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Username = RegUsername.getText().toString();
                String Email = RegEmail.getText().toString();
                String Password = RegPassword.getText().toString();
                String ConfirmPassword = RegConfirmPassword.getText().toString();
                String Status = "Hey I'm Pseudonymous";
                if (TextUtils.isEmpty(Username) || TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(ConfirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Fill all details to register", Toast.LENGTH_SHORT).show();
                } else if (!Email.matches(emailPattern)) {
                    RegEmail.setError("Enter Valid Email Address");
                } else if (!Password.equals(ConfirmPassword)) {
                    RegConfirmPassword.setError("Not Matched");
                } else if (Password.length() <=6) {
                    RegPassword.setError("Password Must be greater than 6 character");
                } else {
                    RegProgressBar.setVisibility(View.VISIBLE);

                    auth.createUserWithEmailAndPassword(Username+"@psuedo.com",Password).addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            String Userid = Objects.requireNonNull(task.getResult().getUser()).getUid();
                            reference=database.getReference("Users").child(Userid);
                            userinfo=new UsersInfo(Username,Email,Password,Status, 0);
                            reference.child("Profile_details").setValue(userinfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        RegProgressBar.setVisibility(View.GONE);
                                        ImageView RegSuccess=findViewById(R.id.Reg_IV_Success);
                                        RegSuccess.setVisibility(View.VISIBLE);
                                        Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                    else {
                                        RegProgressBar.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else{
                            RegProgressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext() , Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                        }

                    });
                }
            }
        });
    }

}