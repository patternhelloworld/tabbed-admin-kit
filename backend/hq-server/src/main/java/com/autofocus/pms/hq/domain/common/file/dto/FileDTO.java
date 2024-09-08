package com.autofocus.pms.hq.domain.common.file.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
public class FileDTO {
    private String originalFilename;
    private String fileName;
    private String fileUrl;
    private String message;

    @Getter
    public static class TextEditor {
        private String imgSrcPath;

        public TextEditor(String imgSrcPath) {
            this.imgSrcPath = imgSrcPath;
        }
    }
}
