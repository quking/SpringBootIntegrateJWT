package com.jlu.preparedproject.exceptions;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MyControllerAdvice {

    @ExceptionHandler(value = BusinessException.class)
    public Map errorHandler(BusinessException ex) {
        Map map = new HashMap();
        map.put("code", 100);
        map.put("msg", ex.getMsg());
        return map;
    }
}
