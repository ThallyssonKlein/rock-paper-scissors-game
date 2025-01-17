package com.rocketpaperscissors.ports.inbound.http.api.v1.controllers;

import com.rocketpaperscissors.ports.inbound.http.api.v1.exception.HttpException;
import com.rocketpaperscissors.ports.outbound.observability.MetricCollector;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.http.HttpStatus;

@RestControllerAdvice
@Slf4j
public class GlobalErrorHandler {
    private static final String GENERIC_ERROR_MESSAGE = "Internal Server Error!";

    @Autowired
    private MetricCollector metricCollector;

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<Object> handleHttpException(HttpException ex, WebRequest request) {
        log.warn("HTTP Exception: {}", ex.getMessage());
        metricCollector.incrementMetricWithTags("http.exception",
                "message:", ex.getMessage(), "status:", String.valueOf(ex.getStatus()), "name:", ex.getName());
        return ResponseEntity
                .status(ex.getStatus())
                .body(new ErrorResponse(ex.getMessage(), ex.getClass().getSimpleName(), ex.getStatus()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        log.error("Generic Exception: {}", ex.getMessage());
        metricCollector.incrementMetricWithTags("generic.exception", "message:", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(GENERIC_ERROR_MESSAGE, null, 500));
    }

    @AllArgsConstructor
    @Getter
    @Setter
    private static class ErrorResponse {
        private String message;
        private String name;
        private int status;
    }
}
