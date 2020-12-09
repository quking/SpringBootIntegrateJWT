package com.jlu.preparedproject.Response;

import lombok.Data;

@Data
public class ResponseVO<T> {

    private int code;
    private String msg;
    private T data;

    ResponseVO(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseVO(int code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    static ResponseVO success(){
        return new ResponseVO(200, "success");
    }

    static<T> ResponseVO fail(T data){
        return new ResponseVO(404, "失败了", data);
    }
}
