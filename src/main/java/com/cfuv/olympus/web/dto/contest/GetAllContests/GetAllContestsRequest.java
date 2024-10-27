package com.cfuv.olympus.web.dto.contest.GetAllContests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class GetAllContestsRequest {
    @Min(value = 1, message = "Страница должна быть не меньше 1")
    @Schema(description = "Страница")
    private Integer page;
}
