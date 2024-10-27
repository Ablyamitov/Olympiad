package com.cfuv.olympus.web.dto.task.feedback;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FeedbackRequestValidator implements ConstraintValidator<ValidFeedbackRequest, FeedbackRequest> {

    public boolean isValid(FeedbackRequest feedbackRequest, ConstraintValidatorContext context) {
        if (feedbackRequest.isAccepted() && feedbackRequest.getPoints() < 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Points must be at least 0 when accepted is true")
                    .addPropertyNode("points")
                    .addConstraintViolation();
            return false;
        } else if (!feedbackRequest.isAccepted() && feedbackRequest.getPoints() != 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Points must be 0 when accepted is false")
                    .addPropertyNode("points")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
