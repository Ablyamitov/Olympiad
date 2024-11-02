package com.cfuv.olympus.web.dto.CustomResponse;

import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseUtil {
    public static <T> ResponseEntity<CustomResponse<T>> createResponse(T data, boolean status, List<String> errors) {
        CustomResponse<T> response = new CustomResponse<>(data, status, errors);
        return ResponseEntity.ok(response);
    }

}
