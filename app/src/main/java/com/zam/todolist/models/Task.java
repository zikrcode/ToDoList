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

package com.zam.todolist.models;

public class Task {

    private String Title, description, date, time;
    private Boolean taskFinished = false;
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

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskFinished(Boolean taskFinished) {
        this.taskFinished = taskFinished;
    }

    public Boolean getTaskFinished() {
        return taskFinished;
    }
}
