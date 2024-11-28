package io.github.patternhelloworld.common.domain.file.util;

import org.springframework.web.multipart.MultipartFile;

public class FileUtil {

    public static String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return getFileExtension(fileName);
    }

    public static String getFileExtension(String fileName) {
        if (fileName != null && fileName.contains(".")) {
            // Extract the part of the string after the last dot
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return null; // Default case: no extension found
    }
}