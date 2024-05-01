package com.example.olympiad.web.dto.task.feedback;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PointsValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPoints {
    String message() default "Invalid points";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
