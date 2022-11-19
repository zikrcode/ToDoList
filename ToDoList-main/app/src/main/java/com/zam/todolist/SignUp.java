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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    private TextInputLayout tilEmailSu, tilPasswordSu;
    private EditText etEmailSu, etPasswordSu;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private Button bSu;
    private TextView tvSi2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        tilEmailSu=findViewById(R.id.tilEmailSu);
        etEmailSu=findViewById(R.id.etEmailSu);
        tilPasswordSu=findViewById(R.id.tilPasswordSu);
        etPasswordSu=findViewById(R.id.etPasswordSu);

        progressDialog=new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();

        bSu=findViewById(R.id.bSu);
        bSu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=etEmailSu.getText().toString();
                String password=etPasswordSu.getText().toString();

                if (TextUtils.isEmpty(email)){
                    tilEmailSu.setHelperTextEnabled(true);
                    tilEmailSu.setHelperText(getString(R.string.email_is_required));
                    return;
                }
                if (!TextUtils.isEmpty(email)){
                    if (!isEmailValid(email)){
                        tilEmailSu.setHelperText(getString(R.string.email_invalid));
                        return;
                    }
                    else {
                        tilEmailSu.setHelperTextEnabled(false);
                    }
                }
                if (TextUtils.isEmpty(password)){
                    tilPasswordSu.setHelperTextEnabled(true);
                    tilPasswordSu.setHelperText(getString(R.string.password_is_required));
                    return;
                }
                if (!TextUtils.isEmpty(password)){
                    if (password.length()<6){
                        tilPasswordSu.setHelperText(getString(R.string.password_invalid));
                        return;
                    }
                    else {
                        tilPasswordSu.setHelperTextEnabled(false);
                    }
                }

                if (isEmailValid(email) && password.length()>5){
                    progressDialog.setMessage(getString(R.string.loading));
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isComplete()){
                                Intent intent=new Intent(SignUp.this,MainActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                            else {
                                Toast.makeText(SignUp.this,getString(R.string.signup_failed),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    progressDialog.dismiss();
                }
            }
        });

        tvSi2=findViewById(R.id.tvSi2);
        tvSi2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUp.this,SignIn.class);
                startActivity(intent);
                finishAffinity();
            }
        });
    }

    public boolean isEmailValid(String email)
    {
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        final Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}