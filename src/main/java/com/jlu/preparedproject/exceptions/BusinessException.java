package com.jlu.preparedproject.exceptions;

import lombok.Data;

@Data
public class BusinessException extends RuntimeException{

    private Integer code;
    private String msg;
    public BusinessException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
