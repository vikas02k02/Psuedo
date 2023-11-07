package com.vikas.pseudo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    EditText LogEmail , LogPassword;
    Button LogBtnLogin;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LogEmail =findViewById(R.id.Log_ET_Email);
        LogPassword=findViewById(R.id.Log_ET_Password);
        LogBtnLogin=findViewById(R.id.Log_Btn_Login);
        auth=FirebaseAuth.getInstance();
        LogBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String LogEmailInput =LogEmail.getText().toString();
                String LogPasswordInput=LogPassword.getText().toString();
                if(TextUtils.isEmpty(LogEmailInput)){
                    LogEmail.setError("Required");
                } else if (TextUtils.isEmpty(LogPasswordInput)) {
                    LogPassword.setError("Required");
                } else if (!LogEmailInput.matches(emailPattern)) {
                    LogEmail.setError("Enter Valid Email Address");

                }else{

                    auth.signInWithEmailAndPassword(LogEmailInput,LogPasswordInput).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                try {
                                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }


            }
        });
        TextView LogBtnRegister =findViewById(R.id.Log_Btn_Register);
        LogBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}