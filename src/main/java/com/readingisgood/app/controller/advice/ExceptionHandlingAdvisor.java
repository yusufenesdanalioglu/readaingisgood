package com.readingisgood.app.controller.advice;

import com.readingisgood.app.controller.advice.model.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.text.Collator;
import java.util.Comparator;
import java.util.Optional;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class ExceptionHandlingAdvisor extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        return buildResponseEntity(new ApiError(BAD_REQUEST, error, ex));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(getErrorMessage(ex));
        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addValidationError(ex.getBindingResult().getGlobalErrors());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(
            javax.validation.ConstraintViolationException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getConstraintViolations());
        return buildResponseEntity(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus
            status, WebRequest request) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage("Validation error");
        apiError.addValidationErrors(ex.getFieldErrors());
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
        return buildResponseEntity(new ApiError(HttpStatus.NOT_FOUND, ex));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex,
                                                                  WebRequest request) {
        if (ex.getCause() instanceof ConstraintViolationException) {
            return buildResponseEntity(new ApiError(HttpStatus.CONFLICT, "Database error", ex.getCause()));
        }
        return buildResponseEntity(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
        log.error(ex.getMessage(), ex);
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    protected ResponseEntity<Object> handleOptimisticLockException(Exception ex) {
        log.error(ex.getMessage(), ex);
        ResponseStatus annotation = ex.getClass().getAnnotation(ResponseStatus.class);
        HttpStatus status = annotation != null ? annotation.value() : INTERNAL_SERVER_ERROR;
        ApiError apiError = new ApiError(status);
        apiError.setMessage("This record changed by another user, please refresh page and try again.");
        return buildResponseEntity(apiError);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleOthers(Exception ex) {
        log.error(ex.getMessage(), ex);
        ResponseStatus annotation = ex.getClass().getAnnotation(ResponseStatus.class);
        HttpStatus status = annotation != null ? annotation.value() : INTERNAL_SERVER_ERROR;
        ApiError apiError = new ApiError(status);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    private String getErrorMessage(MethodArgumentNotValidException ex) {
        if (!CollectionUtils.isEmpty(ex.getBindingResult().getFieldErrors())) {
            Optional<FieldError> fieldError = ex.getBindingResult().getFieldErrors().stream().min(Comparator.comparing(FieldError::getField, Collator.getInstance()));
            return fieldError.map(error -> error.getField() + " " + error.getDefaultMessage()).orElse("Validation Error");
        }
        Optional<ObjectError> objectError = ex.getBindingResult().getGlobalErrors().stream().min(Comparator.comparing(ObjectError::getObjectName, Collator.getInstance()));
        if (!CollectionUtils.isEmpty(ex.getBindingResult().getFieldErrors())) {
            return objectError.map(error -> error.getObjectName() + " " + error.getDefaultMessage()).orElse("Validation Error");
        }
        return "Validation Error";
    }
}