package com.nikskul.kafkaspringbanking.exeption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(Exception.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> exception(RuntimeException ex, WebRequest request) throws Exception {
        log.error("Exception during execution of application", ex);
        return handleException(ex, request);
    }

    @ExceptionHandler
    public ResponseEntity<Object> exception(BadRequestException ex, WebRequest request) throws Exception {
        log.error("Exception during execution of application: ", ex);
        return handleException(ex, request);
    }
}
