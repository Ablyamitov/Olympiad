package com.cfuv.olympus.web.dto.contest;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class DeleteContestRequest {
    @Schema(description = "Сессия")
    private Long contestSession;
}
