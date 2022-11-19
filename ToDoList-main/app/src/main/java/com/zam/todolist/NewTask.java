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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewTask extends AppCompatActivity {

    private Intent intent;
    private Toolbar tNt;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String userId;
    private DatabaseReference databaseReference;
    private String taskId;
    private EditText etTitle,etDescription,etDate,etTime;
    private TextInputLayout tilDate,tilTime;
    private CheckBox cbTaskFinishedNT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        intent=getIntent();

        tNt=findViewById(R.id.tNt);

        if (intent.getExtras()!=null){
            tNt.setTitle(R.string.task);
        }
        else {
            tNt.setTitle(R.string.new_task);
        }
        tNt.setTitleTextColor(getColor(R.color.white));
        setSupportActionBar(tNt);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tNt.setNavigationIcon(R.drawable.ic_back);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        userId=firebaseUser.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("tasks").child(userId);
        taskId=databaseReference.push().getKey();

        etTitle=findViewById(R.id.etTitle);
        etDescription=findViewById(R.id.etDescription);
        etDate=findViewById(R.id.etDate);
        tilDate=findViewById(R.id.tilDate);
        etTime=findViewById(R.id.etTime);
        tilTime=findViewById(R.id.tilTime);
        cbTaskFinishedNT=findViewById(R.id.cbTaskFinishedNT);

        etDate.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date()));
        tilDate.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        etTime.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date()));
        tilTime.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker();
            }
        });

        if (intent.getExtras()!=null){
            taskId=intent.getStringExtra("TaskId");
            String sTitle=intent.getStringExtra("Title"),
                    sDescription=intent.getStringExtra("Description"),
                    sDate=intent.getStringExtra("Date"),
                    sTime=intent.getStringExtra("Time");
            boolean bTaskFinished=intent.getBooleanExtra("TaskFinished",false);

            etTime.setText(sTime);
            etTitle.setText(sTitle);
            etDescription.setText(sDescription);
            etDate.setText(sDate);
            cbTaskFinishedNT.setChecked(bTaskFinished);
        }

        MainActivity.loadNativeAd(this,findViewById(R.id.nAdNewTaskS));
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.new_task_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            finish();
        }
        else if (item.getItemId()==R.id.bSave){
            saveTask();
        }
        else{
            finish();
        }
        return true;
    }

    private void showDatePicker(){
        MaterialDatePicker picker= MaterialDatePicker.Builder
                .datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                etDate.setText(picker.getHeaderText());
            }
        });

        picker.show(getSupportFragmentManager(),"DATE");
    }

    private void showTimePicker(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        MaterialTimePicker picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(hour)
                .setMinute(minute)
                .build();

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, picker.getHour(),picker.getMinute());
                etTime.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime()));
            }
        });

        picker.show(getSupportFragmentManager(), "TIME");
    }

    private void saveTask() {
        String sTitle=etTitle.getText().toString(),
                sDescription=etDescription.getText().toString(),
                sDate=etDate.getText().toString(),
                sTime=etTime.getText().toString();
        Boolean bTaskDone=cbTaskFinishedNT.isChecked();

        if (TextUtils.isEmpty(sTitle)){
            etTitle.setError(getString(R.string.title_required));
            return;
        }
        if (TextUtils.isEmpty(sDescription)){
            etDescription.setError(getString(R.string.description_required));
            return;
        }
        else {
            Task task=new Task(sTitle,sDescription,sDate,sTime,taskId,bTaskDone);
            databaseReference.child(taskId).setValue(task).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(NewTask.this, getString(R.string.saved), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(NewTask.this,getString(R.string.something_went_wrong),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        finish();
    }
}