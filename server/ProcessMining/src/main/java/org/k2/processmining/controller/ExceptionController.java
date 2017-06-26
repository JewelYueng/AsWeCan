package org.k2.processmining.controller;

import org.k2.processmining.exception.JSONBadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nyq on 2017/6/24.
 */
@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(JSONBadRequestException.class)
    public @ResponseBody
    Object handleBadRequest(JSONBadRequestException exception) {
        Map<String, Object> res = new HashMap<>();
        res.put("code", 0);
        res.put("msg", exception.getMessage());
        return ResponseEntity.badRequest().body(res);
    }
}
