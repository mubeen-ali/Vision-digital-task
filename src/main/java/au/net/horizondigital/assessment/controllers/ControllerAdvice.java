package au.net.horizondigital.assessment.controllers;

import au.net.horizondigital.assessment.exceptions.DataNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
@Slf4j
public class ControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = DataNotFoundException.class)
    public ResponseEntity handleDataNotFoundException(DataNotFoundException ex, WebRequest request) {
        log.error("##DataNotFoundException##", ex);
        return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity handleGenericException(Exception ex) {
        log.error("##Exception##", ex);
        return new ResponseEntity("Something went wrong!!!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
