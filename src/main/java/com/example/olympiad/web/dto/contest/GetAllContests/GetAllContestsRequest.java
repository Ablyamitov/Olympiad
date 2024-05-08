package com.example.olympiad.web.dto.contest.GetAllContests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class GetAllContestsRequest {
    @Min(value = 1, message = "page cannot be less then 1")
    @Schema(description = "Страница")
    private Integer page;
}
