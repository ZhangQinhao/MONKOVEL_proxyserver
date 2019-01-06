package com.zhangqinhao.monkovelproxy.bean;


import com.alibaba.fastjson.JSON;

public class BaseResponse<T extends Object> {
    private int code;    //code>=0 成功返回
    private T data;
    private String msg;
    private long time;

    public BaseResponse(){
        this(0,null,null,System.currentTimeMillis());
    }

    public BaseResponse(int code, String msg){
        this(code,null,msg,System.currentTimeMillis());
    }

    public BaseResponse(T data){
        this(0,data,null,System.currentTimeMillis());
    }

    public BaseResponse(T data, String msg){
        this(0,data,msg,System.currentTimeMillis());
    }

    public BaseResponse(int code, T data, String msg){
        this(code,data,msg,System.currentTimeMillis());
    }

    private BaseResponse(int code, T data, String msg, long time){
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.time = time;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String toJson(){
        return JSON.toJSONString(this);
    }
}
