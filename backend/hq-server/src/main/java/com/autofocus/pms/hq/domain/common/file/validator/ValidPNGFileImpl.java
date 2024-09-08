package com.autofocus.pms.hq.domain.common.file.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ValidPNGFileImpl implements ConstraintValidator<ValidPNGFile, MultipartFile> {

    @Override
    public void initialize(ValidPNGFile constraintAnnotation) {
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return false; // or throw an exception based on your requirement
        }

        String originalFilename = file.getOriginalFilename();
        return originalFilename != null && originalFilename.endsWith(".png");
    }
}
