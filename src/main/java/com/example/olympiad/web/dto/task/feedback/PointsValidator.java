package com.example.olympiad.web.dto.task.feedback;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PointsValidator implements ConstraintValidator<ValidPoints, Integer> {
    private boolean accepted;

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isValid(Integer points, ConstraintValidatorContext context) {
        if (accepted) {
            return points >= 0;
        } else {
            return points == 0;
        }
    }
}
