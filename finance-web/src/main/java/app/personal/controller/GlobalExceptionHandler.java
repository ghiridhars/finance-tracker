package app.personal.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.multipart.MultipartException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApiException(ApiException ex) {
        ApiError err = new ApiError(ex.getStatus(), HttpStatus.valueOf(ex.getStatus()).getReasonPhrase(), ex.getMessage());
        return ResponseEntity.status(ex.getStatus()).body(err);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(FileNotFoundException ex) {
        ApiError err = new ApiError(404, "Not Found", ex.getMessage());
        return ResponseEntity.status(404).body(err);
    }

    @ExceptionHandler({MissingServletRequestPartException.class, MultipartException.class})
    public ResponseEntity<ApiError> handleBadRequest(Exception ex) {
        ApiError err = new ApiError(400, "Bad Request", ex.getMessage());
        return ResponseEntity.status(400).body(err);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleOther(Exception ex) {
        ApiError err = new ApiError(500, "Internal Server Error", ex.getMessage());
        return ResponseEntity.status(500).body(err);
    }
}
