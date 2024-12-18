package com.cfuv.olympus.web.dto.task.feedback;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FeedbackRequestValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFeedbackRequest {
    String message() default "Invalid feedback request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
