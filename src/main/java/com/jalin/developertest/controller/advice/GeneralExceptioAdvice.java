package com.jalin.developertest.controller.advice;

import com.jalin.developertest.dto.error.DefaultErrorAttributes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@RestControllerAdvice
public class GeneralExceptioAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    @ExceptionHandler({Exception.class})
    public DefaultErrorAttributes Exception(Exception ex, HttpServletRequest request) {
        log.error("GeneralExceptionAdvice::Exception: {}", ex);
        return DefaultErrorAttributes.builder()
                .timestamp(LocalDateTime.now().toString())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(ex.getLocalizedMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .path(request.getRequestURI()).build();
    }
}
