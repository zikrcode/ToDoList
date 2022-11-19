package com.zam.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextInputLayout tilEmailSi,tilPasswordSi;
    private EditText etEmailSi, etPasswordSi;
    private TextView tvForgotPassword,tvSu2;
    private ProgressDialog progressDialog;
    private Button bSi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth=FirebaseAuth.getInstance();


        if (firebaseAuth.getCurrentUser()!=null){
            Intent intent=new Intent(SignIn.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            tilEmailSi=findViewById(R.id.tilEmailSi);
            etEmailSi=findViewById(R.id.etEmailSi);
            tilPasswordSi=findViewById(R.id.tilPasswordSi);
            etPasswordSi=findViewById(R.id.etPasswordSi);
            tvForgotPassword=findViewById(R.id.tvForgotPassword);
            tvForgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(SignIn.this,PasswordReset.class);
                    startActivity(intent);
                }
            });

            progressDialog=new ProgressDialog(this);

            bSi=findViewById(R.id.bSi);
            bSi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email=etEmailSi.getText().toString();
                    String password=etPasswordSi.getText().toString();

                    if (TextUtils.isEmpty(email)){
                        tilEmailSi.setHelperTextEnabled(true);
                        tilEmailSi.setHelperText(getString(R.string.email_is_required));
                        return;
                    }
                    if (!TextUtils.isEmpty(email)){
                        tilEmailSi.setHelperTextEnabled(false);
                    }
                    if (TextUtils.isEmpty(password)){
                        tilPasswordSi.setHelperTextEnabled(true);
                        tilPasswordSi.setHelperText(getString(R.string.password_is_required));
                        return;
                    }
                    if (!TextUtils.isEmpty(password)){
                        tilPasswordSi.setHelperTextEnabled(false);
                    }

                    if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                        progressDialog.setMessage(getString(R.string.loading));
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Intent intent=new Intent(SignIn.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(SignIn.this,getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
                                }
                                progressDialog.dismiss();
                            }
                        });
                    }
                }
            });

        }

        tvSu2=findViewById(R.id.tvSu2);
        tvSu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignIn.this,SignUp.class);
                startActivity(intent);
            }
        });
    }
}