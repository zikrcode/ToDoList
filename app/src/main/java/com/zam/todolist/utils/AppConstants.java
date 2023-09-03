package com.zam.todolist.utils;

public class AppConstants {

    //SignUp
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    //MainActivity
    public static final String TAB_PENDING = "PENDING";
    public static final String TAB_FINISHED = "FINISHED";
    public static final String TAB_ALL = "ALL";
    public static final String AD_UNIT_ID = "ca-app-pub-7270738426543727/4960626218";

    //NewTask
    public static final String NODE_TASKS = "tasks";
    public static final String INTENT_EXTRA_TASK_ID = "TaskId";
    public static final String INTENT_EXTRA_TITLE = "Title";
    public static final String INTENT_EXTRA_DESCRIPTION = "Description";
    public static final String INTENT_EXTRA_DATE = "Date";
    public static final String INTENT_EXTRA_TIME = "Time";
    public static final String INTENT_EXTRA_TASK_FINISHED = "TaskFinished";

    public static final String TAG_DATE = "DATE";
    public static final String TAG_TIME = "TIME";

    //FirebaseRecyclerAdapter
    public static final String NODE_CHILD_TASK_FINISHED = "taskFinished";

    private AppConstants() {
        // Private constructor to prevent instantiation
    }
}

