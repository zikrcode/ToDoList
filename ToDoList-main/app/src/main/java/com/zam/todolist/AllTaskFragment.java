package com.zam.todolist;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AllTaskFragment extends Fragment {

    private RecyclerView rvAtf;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String userId;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_task, container, false);

        rvAtf=view.findViewById(R.id.rvAtf);
        rvAtf.setLayoutManager(new LinearLayoutManager(getActivity()));

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        userId=firebaseUser.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("tasks").child(userId);

        FirebaseRecyclerAdapter fra = new FirebaseRecyclerAdapter();
        rvAtf.setAdapter(fra.getAdapter(databaseReference,getContext(),3));
        return view;
    }
}