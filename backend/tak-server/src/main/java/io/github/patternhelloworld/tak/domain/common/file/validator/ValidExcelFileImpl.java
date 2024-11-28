package io.github.patternhelloworld.tak.domain.common.file.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ValidExcelFileImpl implements ConstraintValidator<ValidExcelFile, MultipartFile> {

    @Override
    public void initialize(ValidExcelFile constraintAnnotation) {
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return false; // or throw an exception based on your requirement
        }

        String originalFilename = file.getOriginalFilename();
        return originalFilename != null && (originalFilename.endsWith(".xlsx") || originalFilename.endsWith(".xls"));
    }
}
