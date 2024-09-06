package com.example.olympiad.web.dto.task.feedback;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@ValidFeedbackRequest
public class FeedbackRequest {

    @Min(value = 0, message = "id ответа должно быть не меньше 0")
    @Schema(description = "id ответа участника")
    private Long userTasksId;

    @Schema(description = "Флаг принят/отклонен ответ")
    boolean accepted;

    @Schema(description = "Очки за ответ")
    private Integer points;

    @Schema(description = "Комментарий от жури", example = "Отлично!")
    private String comment;

}


