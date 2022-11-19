package com.zam.todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;

public class FirebaseRecyclerAdapter {

    public com.firebase.ui.database.FirebaseRecyclerAdapter<Task, FirebaseRecyclerAdapter.TaskHolder> getAdapter(DatabaseReference databaseReference, Context context, int f){
        FirebaseRecyclerOptions<Task> options;

        if (f==1){
            options = new FirebaseRecyclerOptions.Builder<Task>()
                    .setQuery(databaseReference.orderByChild("taskFinished").equalTo(false), Task.class)
                    .build();
        }

        else if (f==2){
            options = new FirebaseRecyclerOptions.Builder<Task>()
                    .setQuery(databaseReference.orderByChild("taskFinished").equalTo(true), Task.class)
                    .build();
        }

        else {
            options = new FirebaseRecyclerOptions.Builder<Task>()
                    .setQuery(databaseReference, Task.class)
                    .build();
        }

        com.firebase.ui.database.FirebaseRecyclerAdapter<Task, FirebaseRecyclerAdapter.TaskHolder> adapter = new com.firebase.ui.database.FirebaseRecyclerAdapter<Task, FirebaseRecyclerAdapter.TaskHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FirebaseRecyclerAdapter.TaskHolder holder, int position, @NonNull Task model) {
                holder.getTaskFinished().setOnCheckedChangeListener(null); ///to avoid to change previous viewholder is chekbox
                String taskId=model.getTaskId();
                holder.setTaskFinished(model.getTaskFinished());
                holder.setTitle(model.getTitle());
                holder.setDate(model.getDate());
                holder.setTime(model.getTime());

                holder.getTaskFinished().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        databaseReference.child(taskId).child("taskFinished").setValue(b);
                    }
                });

                holder.getView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, NewTask.class);
                        intent.putExtra("TaskId", taskId);
                        intent.putExtra("Title", model.getTitle());
                        intent.putExtra("Description", model.getDescription());
                        intent.putExtra("Date", model.getDate());
                        intent.putExtra("Time", model.getTime());
                        intent.putExtra("TaskFinished", model.getTaskFinished());
                        context.startActivity(intent);
                    }
                });

                holder.getView().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        deleteTask(context, databaseReference.child(taskId));
                        return true;
                    }
                });
            }

            @NonNull
            @Override
            public FirebaseRecyclerAdapter.TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.task_holder,parent,false);
                return new TaskHolder(view);
            }

        };

        adapter.startListening();
        return adapter;
    }

    public class TaskHolder extends RecyclerView.ViewHolder{
        private final View view;
        private final CheckBox taskFinished;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            view=itemView;
            taskFinished = view.findViewById(R.id.cbTaskFinishedTH);
        }

        public View getView() {
            return view;
        }

        public CheckBox getTaskFinished() {
            return taskFinished;
        }

        public void setTaskFinished(Boolean bTaskFinished) {
            taskFinished.setChecked(bTaskFinished);
        }

        public void setTitle(String title){
            TextView tvTaskTh = view.findViewById(R.id.tvTitleTh);
            tvTaskTh.setText(title);
        }

        public void setDate(String date){
            TextView tvDateTh = view.findViewById(R.id.tvDateTh);
            tvDateTh.setText(date);
        }

        public void setTime(String time){
            TextView tvTimeTh = view.findViewById(R.id.tvTimeTh);
            tvTimeTh.setText(time);
        }
    }

    private void deleteTask(Context context, DatabaseReference databaseReference) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.delete_task));
        builder.setCancelable(false);
        builder.setPositiveButton(context.getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                databaseReference.removeValue();
            }
        });

        builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
