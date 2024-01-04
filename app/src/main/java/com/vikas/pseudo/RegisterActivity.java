package com.vikas.pseudo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
//                    Runnable myRunnable = new Runnable() {
//                        @Override
//                        public void run() {
//                            RegProgressBar.setVisibility(View.GONE);
//                            Toast.makeText(getApplicationContext(),"Either user already exists or some server error occurred",Toast.LENGTH_LONG).show();
//                            recreate();
//                            // Code to be executed after 10 seconds
//                        }
//                    };
//                    Handler handler=new Handler();
//                    handler.postDelayed(myRunnable,10000);

                    auth.createUserWithEmailAndPassword(Username+"@psuedo.com",Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                String Userid = Objects.requireNonNull(task.getResult().getUser()).getUid();
                                reference=database.getReference("Users").child(Userid);
                                userinfo=new UsersInfo(Username,Email,Password,Status);
                                reference.setValue(userinfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(task.isSuccessful()){
                                            RegProgressBar.setVisibility(View.GONE);
//                                            handler.removeCallbacks(myRunnable);
                                            ImageView RegSuccess=findViewById(R.id.Reg_IV_Success);
//                                            RegProgressBar.setVisibility(View.GONE);
                                            RegSuccess.setVisibility(View.VISIBLE);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            },1000);

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
//                                recreate();
                            }

                        }
                    });
                }
            }
        });
    }

}