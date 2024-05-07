package com.example.olympiad.web.dto.contest.GetAllContests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetAllContestsRequest {
    @Min(value = 1, message = "page cannot be less then 1")
    private Integer page;
}
