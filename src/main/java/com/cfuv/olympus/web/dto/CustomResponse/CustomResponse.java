package com.cfuv.olympus.web.dto.CustomResponse;

import lombok.Data;

import java.util.List;

@Data
public class CustomResponse<T> {
    private T data;
    private boolean status;
    private List<String> errors;

    public CustomResponse(T data, boolean status, List<String> errors) {
        this.data = data;
        this.status = status;
        this.errors = errors;
    }

}
