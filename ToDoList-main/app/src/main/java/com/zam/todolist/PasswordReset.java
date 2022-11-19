package com.zam.todolist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordReset extends AppCompatActivity {

    private Toolbar tPr;
    private FirebaseAuth firebaseAuth;
    private Button bPasswordReset;
    private TextInputLayout tilResetPassword;
    private EditText etResetPassword;
    private String emailPr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        tPr=findViewById(R.id.tPr);
        tPr.setTitle(R.string.reset_your_password);
        tPr.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(tPr);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tPr.setNavigationIcon(R.drawable.ic_back);

        firebaseAuth=FirebaseAuth.getInstance();

        bPasswordReset=findViewById(R.id.bPasswordReset);
        bPasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPasswordResetEmail();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.password_reset_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    private void sendPasswordResetEmail(){
        tilResetPassword=findViewById(R.id.tilResetPassword);
        etResetPassword=findViewById(R.id.etResetPassword);
        emailPr=etResetPassword.getText().toString();

        if (TextUtils.isEmpty(emailPr)){
            tilResetPassword.setHelperTextEnabled(true);
            tilResetPassword.setHelperText(getString(R.string.email_is_required));
        }
        if (!TextUtils.isEmpty(emailPr)){
            tilResetPassword.setHelperTextEnabled(false);
            firebaseAuth.sendPasswordResetEmail(emailPr)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(PasswordReset.this,getString(R.string.check_email),Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(PasswordReset.this,SignIn.class);
                                startActivity(intent);
                                finishAffinity();
                            }
                            else {
                                Toast.makeText(PasswordReset.this,getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}