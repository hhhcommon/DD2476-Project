2
https://raw.githubusercontent.com/bezkoder/spring-boot-upload-multiple-files/master/src/main/java/com/bezkoder/spring/files/uploadmultiple/exception/FileUploadExceptionAdvice.java
package com.bezkoder.spring.files.uploadmultiple.exception;

import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bezkoder.spring.files.uploadmultiple.model.ResponseMessage;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class FileUploadExceptionAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ResponseMessage> handleMaxSizeException(MaxUploadSizeExceededException exc) {
    return ResponseEntity
        .status(HttpStatus.EXPECTATION_FAILED)
        .body(new ResponseMessage("One or more files are too large!"));
  }
}