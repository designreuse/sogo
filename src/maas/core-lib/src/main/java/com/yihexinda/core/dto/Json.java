package com.yihexinda.core.dto;

public class Json<T> {
    private boolean success = false;
    private String msg = "";
    private T obj = null;

    public Json() {
        this.success = false;
    }

    public Json(boolean success) {
        this.success = success;
    }

    public Json(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }
}