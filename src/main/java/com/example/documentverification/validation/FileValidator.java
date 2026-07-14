package com.example.documentverification.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class FileValidator implements ConstraintValidator<ValidFileType, String> {
    
    private static final String[] ALLOWED_EXTENSIONS = 
        {".jpg", ".jpeg", ".png", ".pdf", ".doc", ".docx"};
    
    @Override
    public boolean isValid(String fileName, ConstraintValidatorContext context) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }
        
        String lowerCaseFileName = fileName.toLowerCase();
        for (String extension : ALLOWED_EXTENSIONS) {
            if (lowerCaseFileName.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }
}