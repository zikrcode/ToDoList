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

package com.zam.todolist.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zam.todolist.R;
import com.zam.todolist.auth.SignIn;
import com.zam.todolist.utils.AppConstants;

public class Settings extends AppCompatActivity {

    private Toolbar tSettings;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String userId;
    private DatabaseReference databaseReference;
    private String taskId;
    private Button bSo, bDeleteAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        tSettings = findViewById(R.id.tSettings);
        bSo = findViewById(R.id.bSo);
        bDeleteAccount = findViewById(R.id.bDeleteAccount);

        setupViews();
    }

    private void setupViews() {
        tSettings.setTitle(R.string.settings);
        tSettings.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(tSettings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tSettings.setNavigationIcon(R.drawable.ic_back);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userId = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(AppConstants.NODE_TASKS)
                .child(userId);

        bSo.setOnClickListener(view -> {
            firebaseAuth.signOut();
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
            finishAffinity();
        });

        MainActivity.loadNativeAd(this, findViewById(R.id.nAdSettingsS));

        bDeleteAccount.setOnClickListener(view -> deleteUser());
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    private void deleteUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_account);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.delete), (dialogInterface, i) -> {
            databaseReference.removeValue();
            firebaseUser.delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, R.string.account_deleted, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(this, SignIn.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Toast.makeText(this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                        }
                        dialogInterface.cancel();
                    });
        });

        builder.setNegativeButton(getString(R.string.cancel), (dialogInterface, i) -> dialogInterface.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}