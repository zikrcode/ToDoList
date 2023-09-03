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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.zam.todolist.R;

public class PasswordReset extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private Toolbar tPr;
    private Button bPasswordReset;
    private TextInputLayout tilResetPassword;
    private EditText etResetPassword;
    private String emailPr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        firebaseAuth = FirebaseAuth.getInstance();
        tPr = findViewById(R.id.tPr);
        bPasswordReset = findViewById(R.id.bPasswordReset);

        setupViews();
    }

    private void setupViews() {
        tPr.setTitle(R.string.reset_your_password);
        tPr.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(tPr);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tPr.setNavigationIcon(R.drawable.ic_back);

        bPasswordReset.setOnClickListener(view -> sendPasswordResetEmail());
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
        tilResetPassword = findViewById(R.id.tilResetPassword);
        etResetPassword = findViewById(R.id.etResetPassword);
        emailPr = etResetPassword.getText().toString();

        if (TextUtils.isEmpty(emailPr)) {
            tilResetPassword.setHelperTextEnabled(true);
            tilResetPassword.setHelperText(getString(R.string.email_is_required));
        }
        if (!TextUtils.isEmpty(emailPr)) {
            tilResetPassword.setHelperTextEnabled(false);
            firebaseAuth.sendPasswordResetEmail(emailPr)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, getString(R.string.check_email), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, SignIn.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}