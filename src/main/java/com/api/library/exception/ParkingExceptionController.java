package com.api.library.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ParkingExceptionController extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NoCarToLeaveException.class)
    protected ResponseEntity<Object> handleCarNotFound(NoCarToLeaveException ex) {
        ApiError apiError = new ApiError(ex.getMessage(), "error.no.car.found");
        return buildResponseEntity(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WrongCarTypeException.class)
    protected ResponseEntity<Object> handleWrongCarType(WrongCarTypeException ex) {
        ApiError apiError = new ApiError(ex.getMessage(), "error.wrong.car.type");
        return buildResponseEntity(apiError, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(FullParkingException.class)
    protected ResponseEntity<Object> handleFullParking(FullParkingException ex) {
        ApiError apiError = new ApiError(ex.getMessage(), "error.parking.full");
        return buildResponseEntity(apiError, HttpStatus.NOT_ACCEPTABLE);
    }
    
    @ExceptionHandler(CarAlreadyParkedException.class)
    protected ResponseEntity<Object> handleCarAlreadyParked(CarAlreadyParkedException ex) {
        ApiError apiError = new ApiError(ex.getMessage(), "error.car.already.parked");
        return buildResponseEntity(apiError, HttpStatus.NOT_ACCEPTABLE);
    }

    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(ex.getMessage(), "error.json.mapping");
        return buildResponseEntity(apiError, status);
    }

    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiError apiError = new ApiError(ex.getMessage(), "error.request.parameter.missing");
        return buildResponseEntity(apiError, status);
    }


    private ResponseEntity<Object> buildResponseEntity(ApiError apiError, HttpStatus httpStatus) {
        return new ResponseEntity<>(apiError, httpStatus);
    }
	
}
