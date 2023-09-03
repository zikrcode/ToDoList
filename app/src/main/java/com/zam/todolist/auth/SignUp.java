/*
 * Copyright (C) 2023 Zokirjon Mamadjonov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zam.todolist.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.zam.todolist.home.MainActivity;
import com.zam.todolist.R;
import com.zam.todolist.utils.AppConstants;

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

        tilEmailSu = findViewById(R.id.tilEmailSu);
        etEmailSu = findViewById(R.id.etEmailSu);
        tilPasswordSu = findViewById(R.id.tilPasswordSu);
        etPasswordSu = findViewById(R.id.etPasswordSu);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();

        bSu = findViewById(R.id.bSu);

        setupViews();
    }

    private void setupViews() {
        bSu.setOnClickListener(view -> {
            String email = etEmailSu.getText().toString();
            String password = etPasswordSu.getText().toString();

            if (TextUtils.isEmpty(email)) {
                tilEmailSu.setHelperTextEnabled(true);
                tilEmailSu.setHelperText(getString(R.string.email_is_required));
                return;
            }
            if (!TextUtils.isEmpty(email)) {
                if (!isEmailValid(email)) {
                    tilEmailSu.setHelperText(getString(R.string.email_invalid));
                    return;
                } else {
                    tilEmailSu.setHelperTextEnabled(false);
                }
            }
            if (TextUtils.isEmpty(password)) {
                tilPasswordSu.setHelperTextEnabled(true);
                tilPasswordSu.setHelperText(getString(R.string.password_is_required));
                return;
            }
            if (!TextUtils.isEmpty(password)) {
                if (password.length() < 6) {
                    tilPasswordSu.setHelperText(getString(R.string.password_invalid));
                    return;
                } else {
                    tilPasswordSu.setHelperTextEnabled(false);
                }
            }

            if (isEmailValid(email) && password.length() > 5) {
                progressDialog.setMessage(getString(R.string.loading));
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    } else {
                        Toast.makeText(this, getString(R.string.signup_failed), Toast.LENGTH_SHORT).show();
                    }
                });
                progressDialog.dismiss();
            }
        });

        tvSi2 = findViewById(R.id.tvSi2);
        tvSi2.setOnClickListener(view -> {
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
            finishAffinity();
        });
    }

    public boolean isEmailValid(String email) {
        final Pattern pattern = Pattern.compile(AppConstants.EMAIL_PATTERN);
        final Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}