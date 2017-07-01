package org.k2.processmining.controller;

import org.k2.processmining.exception.BadRequestException;
import org.k2.processmining.exception.ForbiddenException;
import org.k2.processmining.exception.InternalServerErrorException;
import org.springframework.http.HttpStatus;
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

    @ExceptionHandler(BadRequestException.class)
    public @ResponseBody
    Object handleBadRequest(BadRequestException exception) {
        return ResponseEntity.badRequest().body(getRes(exception.getMessage()));
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public @ResponseBody
    Object handleInternalServerError(InternalServerErrorException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getRes(e.getMessage()));
    }

    public @ResponseBody
    @ExceptionHandler(ForbiddenException.class)
    Object handleForbidden(ForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(getRes(e.getMessage()));
    }

    private Map<String, Object> getRes(String msg) {
        Map<String, Object> res = new HashMap<>();
        res.put("code", 0);
        res.put("msg", msg);
        return res;
    }
}
