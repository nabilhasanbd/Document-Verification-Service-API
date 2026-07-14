package com.example.documentverification.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FileValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFileType {
    
    String message() default "Invalid file type";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
}