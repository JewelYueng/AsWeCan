package org.k2.processmining.controller;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.k2.processmining.exception.*;
import org.k2.processmining.util.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by nyq on 2017/6/24.
 */
@ControllerAdvice
public class ExceptionController {


    private static Logger logger = LoggerFactory.getLogger(ExceptionController.class);


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    Object handle500Exception(Exception e){
        logger.error("msg",e);
        Map map = new HashMap();
        map.put("code","0");
        map.put("msg","服务器内部错误！");
        return map;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public @ResponseBody
    Object handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, Object> res = getRes(Message.INVALID_PARAMS);
        List<Map<String,String >> fieldErrors = new LinkedList<>();
        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            Map<String,String> fieldError = new HashMap<>();
            fieldError.put("field", ((PathImpl)constraintViolation.getPropertyPath()).getLeafNode().getName());
            fieldError.put("msg", constraintViolation.getMessage());
            fieldErrors.add(fieldError);
        }
        res.put("fieldErrors", fieldErrors);
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(res);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public @ResponseBody
    Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FiledErrorResource> filedErrorResources = e.getBindingResult().getFieldErrors().stream().map(fieldError ->
                new FiledErrorResource(fieldError.getObjectName(), fieldError.getCode(),
                        fieldError.getField(), fieldError.getDefaultMessage())
        ).collect(Collectors.toList());
        return ResponseEntity
                .badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResource(Message.INVALID_PARAMS, filedErrorResources));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public @ResponseBody
    Object handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(getRes(Message.MISS_PARAM + ":" + e.getParameterName()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public @ResponseBody
    Object handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(getRes(Message.INVALID_PARAMS));
    }

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

    private Map<String, Object> getRes(String msg) {
        Map<String, Object> res = new HashMap<>();
        res.put("code", 0);
        res.put("msg", msg);
        return res;
    }
}
