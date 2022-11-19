package com.zam.todolist;

public class Task {

    private String Title, description, date, time;
    private Boolean taskFinished=false;
    private String taskId;

    public Task(){}

    public Task(String Title, String description, String date, String time, String taskId, Boolean taskFinished) {
        this.Title = Title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.taskId =  taskId;
        this.taskFinished = taskFinished;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) { this.Title = Title; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) { this.description = description; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskFinished(Boolean taskFinished){ this.taskFinished=taskFinished;}

    public Boolean getTaskFinished(){ return taskFinished; }
}
