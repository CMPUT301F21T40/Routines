package com.example.routines;

import java.io.Serializable;

public class Request implements Serializable {
    private String requestUser;
    private String Status;

    public Request(String requestUser, String status) {
        this.requestUser = requestUser;
        Status = status;
    }

    public Request(String requestUser) {
        this.requestUser = requestUser;
    }

    public void setRequestUser(String requestUser) {
        this.requestUser = requestUser;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRequestUser() {
        return requestUser;
    }

    public String getStatus() {
        return Status;
    }
}
