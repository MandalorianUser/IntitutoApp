package com.example.institutoapp.Models;

import java.util.Map;

public class FCMBody {
    private String to;
    private String priority;
    private Map<String , String> data;

    public FCMBody(String to, String priority, Map<String, String> data) {
        this.to = to;
        this.priority = priority;
        this.data =  data;
    }
// Getter Methods

    public String getTo() {
        return this.to;
    }

    public String getPriority() {
        return this.priority;
    }

    public Map<String, String> getData() {
        return  this.data;
    }

    // Setter Methods

    public void setTo(String to) {
        this.to = to;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }


        }






